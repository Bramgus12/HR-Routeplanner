import bpy

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