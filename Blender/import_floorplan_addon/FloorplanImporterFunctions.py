import bpy
import json
import bmesh

def pathToExtension(path, extension):
    return ".".join(path.split(".")[:-1]) + "." + extension

def createBuildingRoot(buildingName):
    part = "BuildingRoot"
    bpy.ops.object.empty_add(type='PLAIN_AXES', location=(bpy.context.scene.cursor.location.x, bpy.context.scene.cursor.location.y, 0))
    buildingRoot = bpy.context.view_layer.objects.active
    buildingRoot.name = "[{}] {}".format(part, buildingName)
    buildingRoot["buildingName"] = buildingName
    buildingRoot["buildingPart"] = part
    return buildingRoot

def createFloorRoot(buildingRoot, floorNumber, floorHeight):
    part = "FloorRoot"
    bpy.ops.object.empty_add(type='PLAIN_AXES', location=(buildingRoot.location.x, buildingRoot.location.y, buildingRoot.location.z+floorNumber*floorHeight) )
    floorRoot = bpy.context.view_layer.objects.active
    floorName = "{}.{}".format(buildingRoot["buildingName"], floorNumber)
    floorRoot.name = "[{}] {}".format(part, floorName)
    floorRoot["buildingName"] = buildingRoot["buildingName"]
    floorRoot["buildingPart"] = part
    floorRoot["floorNumber"] = floorNumber
    floorRoot["floorHeight"] = floorHeight
    floorRoot["floorName"] = floorName

    floorRoot.parent = buildingRoot
    floorRoot.matrix_parent_inverse = buildingRoot.matrix_world.inverted()
    return floorRoot

def createAlignPoint(referenceImage):
    part = 'AlignPoint'
    alignPoint = bpy.data.objects.new( '[{}] {}'.format(part, referenceImage['floorName']) , None )
    bpy.context.scene.collection.objects.link( alignPoint )

    alignPoint["buildingName"] = referenceImage["buildingName"]
    alignPoint["buildingPart"] = part
    alignPoint["floorNumber"] = referenceImage["floorNumber"]

    offsetX = (0.42 * referenceImage['scale'])/2
    offsetY = (0.297 * referenceImage['scale'])/2
    alignPoint.location = (referenceImage.location.x+offsetX, referenceImage.location.y+offsetY, referenceImage.location.z)
    alignPoint.parent = referenceImage
    alignPoint.matrix_parent_inverse = referenceImage.matrix_basis.inverted()
    alignPoint.lock_location[2] = True
    return alignPoint

def createReferenceImage(floorplan, filepath, floorRoot):
    part = "ReferenceImage"
    imagePath = pathToExtension(filepath, "png")
    bpy.ops.object.load_reference_image(filepath=imagePath, view_align=False)
    referenceImage = bpy.context.view_layer.objects.active
    referenceImage.name = "[{}] {}".format(part, floorRoot["floorName"])
    referenceImage["buildingName"] = floorRoot["buildingName"]
    referenceImage["buildingPart"] = part
    referenceImage["floorNumber"] = floorRoot["floorNumber"]
    referenceImage["floorHeight"] = floorRoot["floorHeight"]
    referenceImage["floorName"] = floorRoot["floorName"]
    referenceImage["originalWidth"] = floorplan["width"]
    referenceImage["originalHeight"] = floorplan["height"]
    referenceImage["scale"] = floorplan["scale"]
    
    referenceImage.location = floorRoot.location
    referenceImage.parent = floorRoot
    referenceImage.matrix_parent_inverse = floorRoot.matrix_world.inverted()

    referenceImage.use_empty_image_alpha = True
    referenceImage.empty_image_offset[0] = 0.0
    referenceImage.empty_image_offset[1] = 0.0
    referenceImage.empty_display_size = 0.42 * floorplan['scale']

    return referenceImage

def createFloorPlane(floorRoot, floorplan, new=False):
    part = "Floor"
    if floorRoot["floorNumber"] == 0 or new:
        # We assume the ground floor is the first one to be added, so we create a new plane for it.
        mesh = bpy.data.meshes.new("[{}] {}".format(part, floorRoot["floorName"]))
        floorPlane = bpy.data.objects.new("[{}] {}".format(part, floorRoot["floorName"]), mesh)
        floorPlane.location=floorRoot.location
        bm = bmesh.new()
        
        offset = 1
        calcNum = lambda func, key, direction, meters, offset: (func([ room[key] for room in floorplan['rooms'] ]) / floorplan[direction]) * meters * floorplan['scale'] + offset
        minX = calcNum(min, 'x1', 'width', 0.42, -offset)
        minY = calcNum(min, 'y1', 'height', 0.297, -offset)
        maxX = calcNum(max, 'x2', 'width', 0.42, offset)
        maxY = calcNum(max, 'y2', 'height', 0.297, offset) 

        verts = [ 
            bm.verts.new((minX, minY, 0.0)),
            bm.verts.new((minX, maxY, 0.0)),
            bm.verts.new((maxX, maxY, 0.0)),
            bm.verts.new((maxX, minY, 0.0))
        ]
        verts.reverse()
        bm.faces.new(verts)
        bm.to_mesh(mesh)
        bm.free()

        bpy.context.scene.collection.objects.link(floorPlane)

        floorPlane.name = "[{}] {}".format(part, floorRoot["floorName"])
        floorPlane["buildingName"] = floorRoot["buildingName"]
        floorPlane["buildingPart"] = part
        floorPlane["floorNumber"] = floorRoot["floorNumber"]
        floorPlane["floorHeight"] = floorRoot["floorHeight"]
        floorPlane["floorName"] = floorRoot["floorName"]

        floorPlane.parent = floorRoot
        floorPlane.matrix_parent_inverse = floorRoot.matrix_world.inverted()

        floorPlane.vertex_groups.new(name='Doors')
        return floorPlane
    else:
        # Find the first existing floor object below this floor and duplicate it.
        step = floorRoot["floorNumber"] // abs(floorRoot["floorNumber"])
        otherFloorNum = floorRoot["floorNumber"] - step
        while otherFloorNum != -step:
            otherFloorPlane = bpy.context.view_layer.objects.get("[{}] {}.{}".format(part, floorRoot["buildingName"], otherFloorNum))
            if otherFloorPlane != None:
                # Duplicate the other floor.
                floorPlane = otherFloorPlane.copy()
                floorPlane.data =  otherFloorPlane.data.copy()
                floorPlane.location = floorRoot.location

                floorPlane.name = "[{}] {}".format(part, floorRoot["floorName"])
                floorPlane.data.name = floorPlane.name
                floorPlane["buildingName"] = floorRoot["buildingName"]
                floorPlane["buildingPart"] = part
                floorPlane["floorNumber"] = floorRoot["floorNumber"]
                floorPlane["floorHeight"] = floorRoot["floorHeight"]
                floorPlane["floorName"] = floorRoot["floorName"]

                floorPlane.parent = floorRoot
                floorPlane.matrix_parent_inverse = floorRoot.matrix_world.inverted()
                bpy.context.collection.objects.link(floorPlane)

                return floorPlane

            otherFloorNum -= step
        return createFloorPlane(floorRoot, floorplan, True)

def createRoomNode(floorplan, room, buildingName, floorNumber, referenceImage):
    part = "RoomNode"
    x = ( ((room['x1']+room['x2'])/2) / floorplan['width'] ) * 0.42 * floorplan['scale']
    y = ( ((room['y1']+room['y2'])/2) / floorplan['height'] ) * 0.297 * floorplan['scale']

    color = (1.0, 0.0, 0.0, 1.0)
    material = bpy.data.materials.get("[{}]".format(part))
    if material == None:
        material = bpy.data.materials.new(name="[{}]".format(part))
        material.diffuse_color = color

    roomNode = None
    mesh = bpy.data.meshes.get('[{}]'.format(part))
    if mesh == None:
        bpy.ops.mesh.primitive_uv_sphere_add(segments=16, ring_count=8, radius=0.3, enter_editmode=False, location=(referenceImage.location.x + x, referenceImage.location.y + y, referenceImage.location.z))
        roomNode = bpy.context.view_layer.objects.active
        roomNode.data.name = '[{}]'.format(part)
        roomNode.data.materials.append(material)
    else:
        roomNode = bpy.data.objects.new('[{}] {}'.format(part, room['code']), mesh)
        roomNode.location = (referenceImage.location.x + x, referenceImage.location.y + y, referenceImage.location.z)
        bpy.context.scene.collection.objects.link(roomNode)
    roomNode.color = color

    roomNode.name = '[{}] {}'.format(part, room['code'])
    roomNode['roomCode'] = room['code']
    roomNode['roomLabel'] = room['label']
    roomNode['x1'] = room['x1']
    roomNode['y1'] = room['y1']
    roomNode['x2'] = room['x2']
    roomNode['y2'] = room['y2']
    roomNode["buildingPart"] = part
    roomNode['floorNumber'] = floorNumber
    roomNode['buildingName'] = buildingName

    roomNode.parent = referenceImage
    roomNode.matrix_parent_inverse = referenceImage.matrix_basis.inverted()
    return roomNode

def loadFloorplan(context, filepath, buildingName, floorNumber, floorHeight, autoBuildingName, autoFloorNumber):

    # Load floorplan json data
    floorplan = {}
    with open(filepath) as json_file:
        floorplan = json.load(json_file)
    
    if autoBuildingName and floorplan['buildingName'] != '':
        buildingName = floorplan['buildingName']
    
    if autoFloorNumber and floorplan['floorNumberDetected']:
        floorNumber = floorplan['floorNumber']

    # Check if an empty root object for the building already exists.
    buildingRoot = bpy.context.view_layer.objects.get("[BuildingRoot] {}".format(buildingName))
    if buildingRoot == None:
        # Create an empty root object for the building
        buildingRoot = createBuildingRoot(buildingName)
    
    # Create a root object for the floor
    floorRoot = createFloorRoot(buildingRoot, floorNumber, floorHeight)

    # Create reference image for floor
    referenceImage = createReferenceImage(floorplan, filepath, floorRoot)
    referenceImage.location.z += 0.01

    # Create the empty object used to align different floors.
    alignPoint = createAlignPoint(referenceImage)

    # Create room nodes
    for room in floorplan['rooms']:
        roomNode = createRoomNode(floorplan, room, buildingName, floorNumber, referenceImage)
        roomNode.location.z -= 0.01

    createFloorPlane(floorRoot, floorplan)

    bpy.ops.object.select_all(action='DESELECT')
    bpy.context.view_layer.objects.active = alignPoint
    alignPoint.select_set(True)

    return {'FINISHED'}
