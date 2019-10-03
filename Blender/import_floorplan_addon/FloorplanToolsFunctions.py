import bpy

def getReferenceImages():
    return filter(lambda obj: obj.type == 'EMPTY' and obj.get('buildingPart') == 'ReferenceImage', bpy.context.scene.objects)

def showReferenceImages(visible):
    referenceImages = getReferenceImages()
    for referenceImage in referenceImages:
        referenceImage.hide_viewport = not visible
    return {'FINISHED'}

def alignFloors():
    selectedRoomNodes = list(filter(lambda obj: obj.type == 'MESH' and obj.get('buildingPart') == 'RoomNode' and type(obj.get('floorNumber')) == int and obj.parent.get('buildingPart') == 'ReferenceImage' , bpy.context.selected_objects))
    if len(selectedRoomNodes) >= 2:
        targetRoomNode = selectedRoomNodes[0]
        targetReferenceImage = targetRoomNode.parent
        calcRealPos = lambda obj, ref, key1, key2, size: ( obj[key1] / ref[key2] ) * size * ref['scale']
        targetX = calcRealPos(targetRoomNode, targetReferenceImage, 'x1', 'originalWidth', 0.42)
        targetY = calcRealPos(targetRoomNode, targetReferenceImage, 'y1', 'originalHeight', 0.297)
        for roomNode in selectedRoomNodes[1:]:
            referenceImage = roomNode.parent
            roomX = calcRealPos(roomNode, referenceImage, 'x1', 'originalWidth', 0.42)
            roomY = calcRealPos(roomNode, referenceImage, 'y1', 'originalHeight', 0.297)
            offsetX = roomX - targetX
            offsetY = roomY - targetY
            if referenceImage.parent != None:
                referenceImage.location.x = referenceImage.parent.location.x - offsetX
                referenceImage.location.y = referenceImage.parent.location.y - offsetY


    return {'FINISHED'}

def createWalls():
    floors = filter(lambda obj: obj.type == 'MESH' and obj.get('buildingPart') == 'Floor', bpy.context.scene.objects)
    for floor in floors:
        walls = floor.copy()
        walls.data = floor.data.copy()
        bpy.context.scene.collection.objects.link(walls)
        bpy.context.view_layer.objects.active = walls

    return {'FINISHED'}