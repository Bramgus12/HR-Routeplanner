import bpy
import bmesh
import math
import mathutils

from . GeneralFunctions import *

def createDoorCutout(width, depth):
    part = "DoorCutout"
    name = '[{}]'.format(part)

    mesh = bpy.data.meshes.get(name)
    if mesh == None:
        mesh = bpy.data.meshes.new(name)
        mesh['buildingPart'] = part

        bm = bmesh.new()
        bmesh.ops.create_cube(bm, size=width)
        bmesh.ops.translate(bm, verts=bm.verts, vec=(0.0, 0.0, 0.5))
        minY = min( [vert.co.x for vert in bm.verts] )
        maxY = max( [vert.co.x for vert in bm.verts] )
        minZ = min( [vert.co.z for vert in bm.verts] )
        maxZ = max( [vert.co.z for vert in bm.verts] )
        topVerts = list( filter(lambda vert: vert.co.z == maxZ , bm.verts) )
        bottomVerts = list( filter(lambda vert: vert.co.z == minZ , bm.verts) )
        frontVerts = list( filter(lambda vert: vert.co.y == maxY , bm.verts) )
        backVerts = list( filter(lambda vert: vert.co.y == minY , bm.verts) )
        bmesh.ops.translate(bm, verts=topVerts, vec=(0.0, 0.0, width))
        bmesh.ops.translate(bm, verts=bottomVerts, vec=(0.0, 0.0, -2))
        moveDepth = (width/2)-depth/2
        bmesh.ops.translate(bm, verts=frontVerts, vec=(0.0, -moveDepth, 0.0))
        bmesh.ops.translate(bm, verts=backVerts, vec=(0.0, moveDepth, 0.0))
        
        bm.to_mesh(mesh)
        bm.free()
    
    doorCutout = bpy.data.objects.new(name, mesh)
    doorCutout['buildingPart'] = part
    doorCutout.display_type = 'WIRE'
    doorCutout.hide_render = True
    return doorCutout

def cleanupDoorCutoutMeshes():
    doorCutoutMeshes = filter( lambda mesh: mesh.get('buildingPart') == 'DoorCutout', bpy.data.meshes )
    for doorCutoutMesh in doorCutoutMeshes:
        bpy.data.meshes.remove(doorCutoutMesh)

def fixDoorWallOverlap(doorCutout, wallLine, doorWidth, offset):
    '''Checks if a door cutout overlaps with one of the two vectors of an edge. Adjusts the location if it overlaps.'''
    vector1 = wallLine[0]
    vector2 = wallLine[1]

    for vector in (vector1, vector2):
        # Calculate distance between doorcutout and vector
        difX = doorCutout.location.x - vector.x
        difY = doorCutout.location.y - vector.y
        distance = math.sqrt( difX**2 + difY**2 ) - (offset+0.001)
        if distance <= doorWidth / 2:
            # Overlap detected
            move = (doorWidth / 2) - distance
            factor = move / distance
            moveX = factor * difX
            moveY = factor * difY
            doorCutout.location.x += moveX
            doorCutout.location.y += moveY
            break

def createWalls():
    part = 'Wall'
    removeWalls()
    floors = filter( lambda floor: floor.visible_get() ,getObjectsByBuildingPart('Floor'))
    for floor in floors:
        # if bpy.context.view_layer.objects.active != None and bpy.context.view_layer.objects.active.mode != 'OBJECT':
        #     bpy.ops.object.mode_set(mode='OBJECT')
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
        linkToFloorCollection(wall, wall['floorNumber'])

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
        wallThickness = 0.1
        solidifyModifier = solidifyObject(wall, wallThickness)
        solidifyModifier.offset = 0.0
        # Hiding modifier here to prevent console being flooded with error messages from Blender.
        solidifyModifier.show_viewport = False
        solidifyModifier.show_render = False
        doorCutouts = []

        # Check where edges intersect with the node network to create doorways.
        nodeNetwork = nodeNetwork = bpy.context.scene.objects.get('[NodeNetwork]')
        if nodeNetwork != None:
            for wallEdge in wall.data.edges:
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
                        intersectionVector = mathutils.Vector( (intersection[0], intersection[1], wallEdgeVector1.z) )
                        doorWidth = 1.0
                        doorDepth = wallThickness + 0.1
                        doorCutout = createDoorCutout(doorWidth, doorDepth)

                        # Set the rotation of the door
                        doorRotation = math.atan2(wallEdgeVector1.y-wallEdgeVector2.y, wallEdgeVector1.x-wallEdgeVector2.x)
                        doorCutout.rotation_euler.z = doorRotation
                        doorCutout.location = intersectionVector

                        fixDoorWallOverlap(doorCutout, wallLine, doorWidth, wallThickness)

                        # Check if door overlaps with another door in this wall with the same rotation
                        otherDoorCutouts = filter( lambda obj: obj.rotation_euler.z == doorCutout.rotation_euler.z, doorCutouts)
                        abort = False
                        for otherDoorCutout in otherDoorCutouts:
                            if distanceBetween2D(otherDoorCutout.location, doorCutout.location) <= doorWidth/2:
                                abort = True
                                break
                        if abort:
                            bpy.data.objects.remove(doorCutout, do_unlink=True)
                            continue

                        # Add boolean modifier to wall to create a hole in the wall.
                        booleanModifier = wall.modifiers.new(name='Boolean', type='BOOLEAN')
                        booleanModifier.operation = 'DIFFERENCE'
                        booleanModifier.object = doorCutout
                        
                        doorCutout.parent = wall
                        doorCutout.matrix_parent_inverse = wall.matrix_world.inverted()

                        doorCutouts.append(doorCutout)
                        linkToFloorCollection(doorCutout, wall.get('floorNumber'))

        # Switch to edit mode and extrude the walls upward.
        bpy.ops.object.mode_set(mode='EDIT')
        bpy.ops.mesh.select_all(action='SELECT')
        bpy.ops.mesh.delete(type='ONLY_FACE')
        bpy.ops.mesh.select_mode(type="VERT")
        bpy.ops.mesh.select_all(action='SELECT')
        bpy.ops.transform.translate(value=(0, 0, -0.3), orient_type='LOCAL', orient_matrix=((1, 0, 0), (0, 1, 0), (0, 0, 1)), orient_matrix_type='GLOBAL', constraint_axis=(False, False, True), mirror=True, use_proportional_edit=False, proportional_edit_falloff='SMOOTH', proportional_size=1, use_proportional_connected=False, use_proportional_projected=False)
        bpy.ops.mesh.extrude_region_move(MESH_OT_extrude_region={"use_normal_flip":False, "mirror":False}, TRANSFORM_OT_translate={"value":(0, 0, wall.get('floorHeight')-0.001 ), "orient_type":'LOCAL', "orient_matrix":((1, 0, 0), (0, 1, 0), (0, 0, 1)), "orient_matrix_type":'LOCAL', "constraint_axis":(False, False, True), "mirror":False, "use_proportional_edit":False, "proportional_edit_falloff":'SMOOTH', "proportional_size":1, "use_proportional_connected":False, "use_proportional_projected":False, "snap":False, "snap_target":'CLOSEST', "snap_point":(0, 0, 0), "snap_align":False, "snap_normal":(0, 0, 0), "gpencil_strokes":False, "cursor_transform":False, "texture_space":False, "remove_on_cancel":False, "release_confirm":False, "use_accurate":False})

        # Separate all faces on edges
        bpy.ops.mesh.select_all(action='SELECT')
        bpy.ops.mesh.edge_split()
        bpy.ops.object.mode_set(mode='OBJECT')

        solidifyModifier.show_viewport = True
        solidifyModifier.show_render = True

        # Apply all modifiers
        modifierNames = [ modifier.name for modifier in wall.modifiers ]
        for modifierName in modifierNames:
            bpy.ops.object.modifier_apply(modifier=modifierName)
        
        # Remove door cutouts
        for doorCutout in doorCutouts:
            bpy.data.objects.remove(doorCutout, do_unlink=True)

    cleanupDoorCutoutMeshes()

    return {'FINISHED'}

def removeWalls():
    walls = getObjectsByBuildingPart('Wall')
    doorCutouts = getObjectsByBuildingPart('DoorCutout')
    objects = walls + doorCutouts
    for obj in objects:
        data = obj.data
        bpy.data.objects.remove(obj, do_unlink=True)
        bpy.data.meshes.remove(data)
    return {'FINISHED'}

def lineIntersection(line1, line2, infinite=False):
    '''Calculate the point where two 2D lines cross.
    Returns None if the lines don't intersect.'''

    intersect = mathutils.geometry.intersect_line_line_2d( 
        mathutils.Vector( (line1[0][0], line1[0][1]) ),
        mathutils.Vector( (line1[1][0], line1[1][1]) ),
        mathutils.Vector( (line2[0][0], line2[0][1]) ),
        mathutils.Vector( (line2[1][0], line2[1][1]) )
    )

    if intersect != None and not infinite:
        x, y = intersect
        for line in (line1, line2):
            minX = min( (line[0][0], line[1][0]) )
            maxX = max( (line[0][0], line[1][0]) )
            minY = min( (line[0][1], line[1][1]) )
            maxY = max( (line[0][1], line[1][1]) )
            if x < minX or x > maxX or y < minY or y > maxY:
                return None
    return intersect