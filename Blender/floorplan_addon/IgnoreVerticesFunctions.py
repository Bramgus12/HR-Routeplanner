import bpy

def ignoreVertices(ignore=True):
    activeObject = bpy.context.view_layer.objects.active
    if not(activeObject != None and activeObject.mode == 'EDIT'):
        return('CANCELLED')
    
    bpy.ops.object.mode_set(mode='OBJECT')
    selectedObjects = bpy.context.selected_objects
    for selectedObject in selectedObjects:
        ignoreVertexGroup = selectedObject.vertex_groups.get('Ignore')
        if ignoreVertexGroup != None:
            selectedVerticesIndexes = [ vert.index for vert in list( filter( lambda vertex: vertex.select , selectedObject.data.vertices ) ) ]
            if ignore:
                ignoreVertexGroup.add( selectedVerticesIndexes, 1.0, 'ADD')
            else:
                ignoreVertexGroup.remove( selectedVerticesIndexes )

    bpy.ops.object.mode_set(mode='EDIT')

    return {'FINISHED'}