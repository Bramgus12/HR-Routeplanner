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
        calcRealPos = lambda obj, ref, key1, key2, key3, size: ( ((obj[key1]+obj[key2])/2) / ref[key3] ) * size * ref['scale']
        targetX = calcRealPos(targetRoomNode, targetReferenceImage, 'x1', 'x2', 'originalWidth', 0.42)
        targetY = calcRealPos(targetRoomNode, targetReferenceImage, 'y1', 'y2', 'originalHeight', 0.297)
        for roomNode in selectedRoomNodes[1:]:
            referenceImage = roomNode.parent
            roomX = calcRealPos(roomNode, referenceImage, 'x1', 'x2', 'originalWidth', 0.42)
            roomY = calcRealPos(roomNode, referenceImage, 'y1', 'y2', 'originalHeight', 0.297)


    return {'FINISHED'}