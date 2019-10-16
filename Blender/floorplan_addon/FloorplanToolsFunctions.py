import bpy
import bmesh
import math
from mathutils import Vector
from . FloorplanImporterFunctions import linkToFloorCollection

def getReferenceImages():
    return filter(lambda obj: obj.type == 'EMPTY' and obj.get('buildingPart') == 'ReferenceImage', bpy.context.scene.objects)

def getObjectsByBuildingPart(part):
    return list( filter(lambda obj: obj.get('buildingPart') == part, bpy.context.scene.objects) )

def showReferenceImages(visible):
    referenceImages = getReferenceImages()
    for referenceImage in referenceImages:
        referenceImage.hide_viewport = not visible
    return {'FINISHED'}

def showRoomNodes(visible):
    roomNodes = getObjectsByBuildingPart('RoomNode')
    for roomNode in roomNodes:
        roomNode.hide_viewport = not visible
    return {'FINISHED'}

def alignFloors():
    targeAlignPoint = bpy.context.view_layer.objects.active
    if targeAlignPoint.get('buildingPart') != 'AlignPoint':
        return {'CANCELLED'}
    alignPoints = list(filter(lambda obj: obj.get('buildingPart') == 'AlignPoint' and obj.get('buildingName') == targeAlignPoint.get('buildingName') and obj != targeAlignPoint, bpy.context.scene.objects))
    for alignPoint in alignPoints:
        offsetX = alignPoint.matrix_world.translation.x - targeAlignPoint.matrix_world.translation.x
        offsetY = alignPoint.matrix_world.translation.y - targeAlignPoint.matrix_world.translation.y
        referenceImage = alignPoint.parent
        if referenceImage.parent != None:
            referenceImage.matrix_world.translation.x -= offsetX
            referenceImage.matrix_world.translation.y -= offsetY
    return {'FINISHED'}

def createDoorCutout():
    part = "DoorCutout"
    name = '[{}]'.format(part)
    mesh = bpy.data.meshes.new(name)

    bm = bmesh.new()
    bmesh.ops.create_cube(bm, size=1.0)
    bmesh.ops.translate(bm, verts=bm.verts, vec=(0.0, 0.0, 0.5))
    minY = min( [vert.co.x for vert in bm.verts] )
    maxY = max( [vert.co.x for vert in bm.verts] )
    minZ = min( [vert.co.z for vert in bm.verts] )
    maxZ = max( [vert.co.z for vert in bm.verts] )
    topVerts = list( filter(lambda vert: vert.co.z == maxZ , bm.verts) )
    bottomVerts = list( filter(lambda vert: vert.co.z == minZ , bm.verts) )
    frontVerts = list( filter(lambda vert: vert.co.y == maxY , bm.verts) )
    backVerts = list( filter(lambda vert: vert.co.y == minY , bm.verts) )
    bmesh.ops.translate(bm, verts=topVerts, vec=(0.0, 0.0, 1.0))
    bmesh.ops.translate(bm, verts=bottomVerts, vec=(0.0, 0.0, -0.1))
    bmesh.ops.translate(bm, verts=frontVerts, vec=(0.0, -0.3, 0.0))
    bmesh.ops.translate(bm, verts=backVerts, vec=(0.0, 0.3, 0.0))
    
    bm.to_mesh(mesh)
    bm.free()
    
    doorCutout = bpy.data.objects.new(name, mesh)
    doorCutout['buildingPart'] = part
    doorCutout.display_type = 'WIRE'
    doorCutout.hide_render = True
    return doorCutout

def createWalls():
    part = 'Wall'
    removeWalls()
    floors = getObjectsByBuildingPart('Floor')
    for floor in floors:
        if bpy.context.view_layer.objects.active != None and bpy.context.view_layer.objects.active.mode != 'OBJECT':
            bpy.ops.object.mode_set(mode='OBJECT')
        name = '[{}] {}'.format(part, floor.get('floorName'))
        # Create a wall object by copying the floor mesh.
        wall = bpy.context.scene.objects.get(name)
        if wall != None:
            bpy.ops.object.delete({"selected_objects": [wall]})
        wall = floor.copy()
        wall.data = floor.data.copy()
        wall.name = name
        wall.data.name = name
        wall['buildingPart'] = part

        # Add wall object to scene.
        linkToFloorCollection(wall, wall['buildingName'], wall['floorNumber'])

        # Remove solidify modifier from object, might be on there from the floor object.
        removeModifier(wall, 'SOLIDIFY')
        
        # Select the wall object
        bpy.ops.object.select_all(action='DESELECT')
        bpy.context.view_layer.objects.active = wall
        wall.select_set(True)

        # Switch to edit mode and deselect al vertices.
        bpy.ops.object.mode_set(mode='EDIT')
        bpy.ops.mesh.select_all(action='DESELECT')
        bpy.ops.mesh.select_mode(type="VERT")
        bpy.ops.object.mode_set(mode='OBJECT')

        # Delete all vertices that shouldn't be extruded.
        ignoreVertegGroup = wall.vertex_groups.get('Ignore')
        if ignoreVertegGroup != None:
            for vertex in wall.data.vertices:
                for vertexGroup in vertex.groups:
                    if vertexGroup.group == ignoreVertegGroup.index:
                        vertex.select = True
        bpy.ops.object.mode_set(mode='EDIT')
        bpy.ops.mesh.delete(type='EDGE')
        bpy.ops.object.mode_set(mode='OBJECT')

        # Add solidify modifier
        solidifyModifier = solidifyObject(wall, 0.01)
        solidifyModifier.offset = 0.0
        solidifyModifier.use_even_offset = True
        # Hiding modifier here to prevent console being flooded with error messages from Blender.
        solidifyModifier.show_viewport = False
        solidifyModifier.show_render = False

        # Check where edges intersect with the node network to create doorways.
        for wallEdge in wall.data.edges:
            nodeNetwork = nodeNetwork = bpy.context.scene.objects.get('[NodeNetwork]')
            # Calculate global locations of edge vectors
            wallEdgeVector1 = wall.matrix_world @ wall.data.vertices[wallEdge.vertices[0]].co
            wallEdgeVector2 = wall.matrix_world @ wall.data.vertices[wallEdge.vertices[1]].co
            minZ = min(wallEdgeVector1.z, wallEdgeVector2.z) - 0.1
            maxZ = max(wallEdgeVector1.z, wallEdgeVector2.z) + 0.1
            wallLine = (wallEdgeVector1 , wallEdgeVector2)

            for networkEdge in nodeNetwork.data.edges:
                networkEdgeVector1 = nodeNetwork.data.vertices[networkEdge.vertices[0]].co
                networkEdgeVector2 = nodeNetwork.data.vertices[networkEdge.vertices[1]].co
                # Only check network edge if it's close to the same height as the wall edge
                if not(networkEdgeVector1.z >= minZ and networkEdgeVector1.z <= maxZ or networkEdgeVector2.z >= minZ and networkEdgeVector2.z <= maxZ):
                    continue
                networkLine = ( networkEdgeVector1, networkEdgeVector2 )

                intersection = lineIntersection( wallLine, networkLine )
                if intersection != None:
                    intersectionVector = Vector( (intersection[0], intersection[1], wallEdgeVector1.z) )
                    bpy.context.scene.cursor.location = intersectionVector
                    doorCutout = createDoorCutout()
                    booleanModifier = wall.modifiers.new(name='Boolean', type='BOOLEAN')
                    booleanModifier.operation = 'DIFFERENCE'
                    booleanModifier.object = doorCutout

                    doorRotation = math.atan2(wallEdgeVector1.y-wallEdgeVector2.y, wallEdgeVector1.x-wallEdgeVector2.x)
                    doorCutout.rotation_euler.z = doorRotation
                    doorCutout.location = intersectionVector
                    doorCutout.parent = wall
                    doorCutout.matrix_parent_inverse = wall.matrix_world.inverted()
                    linkToFloorCollection(doorCutout, wall.get('buildingName'), wall.get('floorNumber'))

        # Switch to edit mode and extrude the walls upward.
        bpy.ops.object.mode_set(mode='EDIT')
        bpy.ops.mesh.select_all(action='SELECT')
        bpy.ops.mesh.delete(type='ONLY_FACE')
        bpy.ops.mesh.select_mode(type="VERT")
        bpy.ops.mesh.select_all(action='SELECT')
        bpy.ops.mesh.extrude_region_move(MESH_OT_extrude_region={"use_normal_flip":False, "mirror":False}, TRANSFORM_OT_translate={"value":(0, 0, wall.get('floorHeight')-0.3 ), "orient_type":'LOCAL', "orient_matrix":((1, 0, 0), (0, 1, 0), (0, 0, 1)), "orient_matrix_type":'LOCAL', "constraint_axis":(False, False, True), "mirror":False, "use_proportional_edit":False, "proportional_edit_falloff":'SMOOTH', "proportional_size":1, "use_proportional_connected":False, "use_proportional_projected":False, "snap":False, "snap_target":'CLOSEST', "snap_point":(0, 0, 0), "snap_align":False, "snap_normal":(0, 0, 0), "gpencil_strokes":False, "cursor_transform":False, "texture_space":False, "remove_on_cancel":False, "release_confirm":False, "use_accurate":False})
        bpy.ops.object.mode_set(mode='OBJECT')      

        solidifyModifier.show_viewport = True
        solidifyModifier.show_render = True

    return {'FINISHED'}

def removeWalls():
    walls = getObjectsByBuildingPart('Wall')
    doorCutouts = getObjectsByBuildingPart('DoorCutout')
    bpy.ops.object.delete({"selected_objects": walls})
    bpy.ops.object.delete({"selected_objects": doorCutouts})
    return {'FINISHED'}

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

def distanceBetween2D(v1, v2):
    return math.sqrt( (v2.x - v1.x)**2 + (v2.y - v1.y)**2 )

def distance(v1, v2):
    return math.sqrt( (v2.x - v1.x)**2 + (v2.y - v1.y)**2 + (v2.z - v1.z)**2 )

def isSameLocation(vector1, vector2, margin):
    for i in range(3):
        if abs( vector1[i] - vector2[i] ) > margin:
            return False
    return True

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

def removeModifier(obj, modifierType):
    for modifier in obj.modifiers:
        if modifier.type == modifierType:
            obj.modifiers.remove(modifier)

def solidifyObject(obj, thickness=0.1):
    # Remove existing solidify modifiers
    removeModifier(obj, 'SOLIDIFY')
    # Add new solidify modifier
    modifier = obj.modifiers.new(name='Solidify', type='SOLIDIFY')
    modifier.thickness = thickness
    return modifier

def solidifyFloors(solidify=True):
    floors = getObjectsByBuildingPart('Floor')
    for floor in floors:
        if solidify:
            solidifyObject(floor, 0.3)
        else:
            removeModifier(floor, 'SOLIDIFY')

    return {'FINISHED'}

def lineIntersection(line1, line2, infinite=False):
    '''Calculate the point where two 2D lines cross.
    Returns None if the lines don't intersect.
    Source: https://stackoverflow.com/a/20677983'''
    xdiff = (line1[0][0] - line1[1][0], line2[0][0] - line2[1][0])
    ydiff = (line1[0][1] - line1[1][1], line2[0][1] - line2[1][1])

    def det(a, b):
        return a[0] * b[1] - a[1] * b[0]

    div = det(xdiff, ydiff)
    if div == 0:
       return None

    d = (det(*line1), det(*line2))
    x = det(d, xdiff) / div
    y = det(d, ydiff) / div
    if not infinite:
        for line in (line1, line2):
            minX = min( (line[0][0], line[1][0]) )
            maxX = max( (line[0][0], line[1][0]) )
            minY = min( (line[0][1], line[1][1]) )
            maxY = max( (line[0][1], line[1][1]) )
            if x < minX or x > maxX or y < minY or y > maxY:
                return None
    return x, y
