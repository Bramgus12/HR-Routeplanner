import bpy
import bmesh

from . GeneralFunctions import *

def createNodeVisual():
    part = 'NodeVisual'
    nodeVisual = bpy.context.scene.objects.get('[{}]'.format(part))
    if nodeVisual == None:
        bpy.ops.mesh.primitive_uv_sphere_add(segments=16, ring_count=8, radius=0.15, enter_editmode=False, location=(0,0,0))
        nodeVisual = bpy.context.view_layer.objects.active
        nodeVisual.name = '[{}]'.format(part)
        # nodeVisual.hide_viewport = True
        # nodeVisual.hide_render = True
        color = (1.0, 0.0, 0.0, 1.0)
        material = bpy.data.materials.get("[{}]".format(part))
        if material == None:
            material = bpy.data.materials.new(name="[{}]".format(part))
            material.diffuse_color = color
        nodeVisual.data.materials.append(material)
        nodeVisual.lock_location[0] = True
        nodeVisual.lock_location[1] = True
        nodeVisual.lock_location[2] = True
    return nodeVisual

def createNode():
    part = 'NodeNetwork'
    nodeNetwork = bpy.context.scene.objects.get('[{}]'.format(part))
    if nodeNetwork == None:
        # Create the node network object
        mesh = bpy.data.meshes.get('[{}]'.format(part))
        if mesh != None:
            bpy.data.meshes.remove(mesh)
        mesh = bpy.data.meshes.new('[{}]'.format(part))
        nodeNetwork = bpy.data.objects.new('[{}]'.format(part), mesh)
        nodeNetwork.lock_location[0] = True
        nodeNetwork.lock_location[1] = True
        nodeNetwork.lock_location[2] = True
        bpy.context.scene.collection.objects.link(nodeNetwork)
        nodeNetwork.show_in_front = True
        nodeVisual = createNodeVisual()
        nodeVisual.parent = nodeNetwork
        nodeVisual.matrix_parent_inverse = nodeVisual.matrix_basis.inverted()
        nodeNetwork.instance_type = 'VERTS'

    elif nodeNetwork == bpy.context.view_layer.objects.active:
        bpy.ops.object.mode_set(mode='OBJECT')

    location = bpy.context.scene.cursor.location
    floors = list(filter(lambda obj: obj.get('buildingPart') == 'Floor' and abs(obj.location.z - location.z) < 1.0 , bpy.context.scene.objects))
    if len(floors) > 0:
        closestFloor = floors[0]
        for floor in floors:
            if abs(floor.location.z - location.z) < abs(floor.location.z - closestFloor.location.z):
                closestFloor = floor
        location.z = closestFloor.location.z
        
    mesh = nodeNetwork.data
    bm = bmesh.new()
    bm.from_mesh(mesh)
    
    bm.verts.new(location)

    bm.to_mesh(mesh)
    bm.free()
    mesh.update()
    bpy.ops.object.select_all(action='DESELECT')
    bpy.context.view_layer.objects.active = nodeNetwork
    nodeNetwork.select_set(True)
    bpy.ops.object.mode_set(mode='EDIT')
    bpy.ops.mesh.select_mode(type="VERT")

    return {'FINISHED'}

def connectRoomNodes(roomsToNodes=False):
    nodeNetwork = nodeNetwork = bpy.context.scene.objects.get('[NodeNetwork]')
    reenterEditMode = nodeNetwork.mode == 'EDIT'
    if nodeNetwork.mode != 'OBJECT':
        bpy.ops.object.mode_set(mode='OBJECT')
    if nodeNetwork == None:
        return {'CANCELLED'}
    roomNodes = list(filter(lambda obj: obj.get('buildingPart') == 'RoomNode' and obj.parent != nodeNetwork , bpy.context.scene.objects))
    for roomNode in roomNodes:
        vertices = list(filter(lambda vertex: abs(vertex.co.z - roomNode.matrix_world.translation.z) <= 0.5 and distanceBetween2D(roomNode.matrix_world.translation, vertex.co) <= 1.0 , nodeNetwork.data.vertices))
        if len(vertices) > 0:
            closestVertex = vertices[0]
            closestVertexDistance = distanceBetween2D(roomNode.matrix_world.translation, closestVertex.co)
            for vertex in vertices:
                distance = distanceBetween2D(roomNode.matrix_world.translation, vertex.co)
                if distance < closestVertexDistance:
                    closestVertex = vertex
                    closestVertexDistance = distance
            if not isSameLocation(roomNode.matrix_world.translation, closestVertex.co, 0.001):
                if roomsToNodes:
                    roomNode.matrix_world.translation = closestVertex.co
                else:
                    closestVertex.co = roomNode.matrix_world.translation
    
    if reenterEditMode:
        bpy.ops.object.mode_set(mode='EDIT')

    return {'FINISHED'}