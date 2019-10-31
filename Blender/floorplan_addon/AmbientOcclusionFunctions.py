import bpy
import os
import functools
import re

from . GeneralFunctions import *

def setupAmbientOcclusion(operator, context):

    texturesDir = createTextureDirectory()

    # Get a list of all objects to be baked.
    floors = getObjectsByBuildingPart('Wall')
    walls = getObjectsByBuildingPart('Floor')
    bakeObjects = floors + walls

    nodeNetwork = bpy.context.scene.objects.get('[NodeNetwork]')
    if nodeNetwork != None:
        nodeNetwork.hide_render = True

    bpy.ops.object.mode_set(mode='OBJECT')

    for bakeObject in bakeObjects:
        # Create image for the ambient occlusion map
        width, height = 512, 512
        aoName = "AO {}".format(bakeObject.name)
        filename = re.sub(r"[\[\]]","", aoName) + ".png"
        filepath = os.path.join(texturesDir, filename)

        aoImage = bpy.data.images.get(aoName)
        if aoImage != None:
            bpy.data.images.remove(aoImage)

        aoImage = bpy.data.images.new(aoName, width=width, height=height)
        aoImage.filepath_raw = '//' + filepath
        aoImage.file_format = 'PNG'

        # Create a material
        aoMaterial = bpy.data.materials.get(aoName)
        if aoMaterial != None:
            bpy.data.materials.remove(aoMaterial)

        aoMaterial = bpy.data.materials.new(name=aoName)
        aoMaterial.use_nodes = True
        nodeTree = aoMaterial.node_tree
        nodes = nodeTree.nodes

        # Texture Node
        imageTextureNode = nodes.new('ShaderNodeTexImage')
        imageTextureNode.location = (-735, 208)
        imageTextureNode.image = aoImage

        # Separate RGB node
        separateRgbNode = nodes.new('ShaderNodeSeparateRGB')
        separateRgbNode.location = (-415, 208)

        nodeTree.links.new( imageTextureNode.outputs['Color'], separateRgbNode.inputs['Image'] )

        # Ambient Occlusion Node
        # Needed for gltf exporter
        aoNode = nodes.new('ShaderNodeGroup')
        aoNode.name = 'Gltf Ambient Occlusion'
        aoNode.location = (-215, 215)

        gltfGroupName = 'glTF Settings'
        gltfNodeGroup = bpy.data.node_groups.get(gltfGroupName)
        if gltfNodeGroup == None:        
            gltfNodeGroup = bpy.data.node_groups.new(gltfGroupName, 'ShaderNodeTree')
            gltfNodeGroup.inputs.new("NodeSocketFloat", "Occlusion")
            gltfNodeGroup.nodes.new('NodeGroupOutput')
            gltfNodeGroup.nodes.new('NodeGroupInput')

        aoNode.node_tree = gltfNodeGroup

        nodeTree.links.new( separateRgbNode.outputs['R'], aoNode.inputs['Occlusion'] )

        # Add material to object
        if bakeObject.data.materials:
            bakeObject.data.materials[0] = aoMaterial
        else:
            bakeObject.data.materials.append(aoMaterial)
        # Create uv map
        aoMapName = 'AmbientOcclusion'
        aoMap = bakeObject.data.uv_layers.get(aoMapName)
        if aoMap == None:
            aoMap = bakeObject.data.uv_layers.new(name=aoMapName)
        aoMap.active = True
        # Select the wall object
        bpy.ops.object.select_all(action='DESELECT')
        bpy.context.view_layer.objects.active = bakeObject
        bakeObject.select_set(True)
        # Select all vertices and unwrap
        bpy.ops.object.mode_set(mode='EDIT')
        bpy.ops.mesh.select_all(action='SELECT')    
        bpy.ops.uv.smart_project(island_margin=0.08)
        bpy.ops.object.mode_set(mode='OBJECT')

        aoImage.save()

    # Setup render settings
    scene = bpy.context.scene
    scene.render.engine = 'CYCLES'
    scene.cycles.device = 'GPU'
    scene.cycles.samples = 1024
    scene.render.tile_x = 512
    scene.render.tile_y = 512
    scene.cycles.bake_type = 'AO'
    scene.render.bake.margin = 1
    scene.render.bake.use_clear = True

    # Get lists of collections and objects in those collections to be baked.
    collections = list(filter( lambda collection: collection.get('floorNumber') != None, bpy.data.collections ))
    bakeParts = {'Floor', 'Wall'}

    bakingData = {
        'collections': [],
        'objects': [],
        'objectsQueue': [],
        'objectsDone': 0,
        'objectsTotal': 0,
        'state': 'ready',
        'currentObject': None
    }

    for collection in collections:
        bakingData['collections'].append(collection)

        bakeObjects = list( filter(lambda obj: obj.get('buildingPart') in bakeParts, collection.objects) )
        bakingData['objectsQueue'] += bakeObjects
        bakingData['objects'] += bakeObjects
        bakingData['objectsTotal'] += len(bakeObjects)

        for obj in bakeObjects:
            toggleSolidifyModifier(obj, False)
    
    windowId = context.window_manager.windows[:].index(context.window)

    bpy.app.timers.register(functools.partial(bakingListener, operator, bakingData, windowId))

    return {'RUNNING_MODAL'}

def bakingListener(operator, bakingData, windowId):

    aoImage = bakingData['currentObject'].material_slots[0].material.node_tree.nodes['Image Texture'].image if bakingData['currentObject'] != None else None

    # Check if escape was pressed
    try:
        if operator.interrupted:
            print("Baking was interrupted")
            return None
    except ReferenceError:
        print("Baking was interrupted")
        return None

    # Check if the image is dirty, if it is the bake of the current object is done.
    if aoImage != None and aoImage.is_dirty:
        # Save image and set state to ready
        bakingData['objectsDone'] += 1
        bakingData['state'] = 'ready'
        bakingData['currentObject'] = None
        aoImage.save()

    # When ready to bake, take the first object from collection queue
    elif bakingData['state'] == 'ready' and len(bakingData['objectsQueue']) > 0:
        bakeObject = bakingData['objectsQueue'].pop(0)

        # Enable only the collection for this floor for rendering
        for collection in bakingData['collections']:
            collection.hide_render = collection.objects.get(bakeObject.name) == None
            if not collection.hide_render:
                for obj in collection.objects:
                    obj.hide_render =  not(obj.get('buildingPart') in {'Wall', 'Floor'} and obj.get('buildingName') == bakeObject.get('buildingName'))
            
        # Select the current object
        bpy.ops.object.select_all(action='DESELECT')
        bpy.context.view_layer.objects.active = bakeObject
        bakeObject.select_set(True)
        # Start baking        
        # https://blender.stackexchange.com/a/135995
        window = bpy.context.window_manager.windows[windowId]
        screen = window.screen
        views_3d = sorted( [a for a in screen.areas if a.type == 'VIEW_3D'], key=lambda a: (a.width * a.height))
        a = views_3d[0]
        override = {"window" : window,
            "screen" : screen,
            "area" : a,
            "space_data": a.spaces.active,
            "region" : a.regions[-1]
        }
        bakingData['state'] = 'baking'
        bakingData['currentObject'] = bakeObject
        print("Baking [{}/{}]: {}".format(bakingData['objectsDone']+1, bakingData['objectsTotal'], bakeObject.name))
        bpy.ops.object.bake(override, 'INVOKE_DEFAULT', type='AO')

    if bakingData['objectsDone'] >= bakingData['objectsTotal']:
        bakingData['state'] = 'finished'            
        for obj in bakingData['objects']:
            toggleSolidifyModifier(obj, True)
        print("Ambient Occlusion baking complete.")
        operator.done = True

        return None

    return 0.5

def toggleSolidifyModifier(obj, enable):
    modifier = obj.modifiers.get('Solidify')
    if modifier != None:
        modifier.show_render = enable

def createTextureDirectory():
    '''Create a directory for textures in the same folder as the .blend file.'''
    dirName = 'textures'
    rootDir = os.path.dirname(bpy.data.filepath)
    texturesDir = os.path.join(rootDir, dirName)
    if not os.path.exists(texturesDir):
        os.mkdir(texturesDir)
    return dirName

def saveCompressedImage(image):
    '''Saves the given image with optimal settings without changing the scene settings.'''
    # Copy the original settings
    imageSettings = bpy.context.scene.render.image_settings
    ogCompression= imageSettings.compression
    ogColorMode = imageSettings.color_mode
    ogColorDepth = imageSettings.color_depth

    imageSettings.compression = 100
    imageSettings.color_mode = 'BW'
    imageSettings.color_depth = '8'

    image.save_render(filepath=bpy.path.abspath(image.filepath), scene=bpy.context.scene)

    imageSettings.compression = ogCompression
    imageSettings.color_mode = ogColorMode
    imageSettings.color_depth = ogColorDepth
