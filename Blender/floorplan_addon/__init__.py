import bpy

bl_info = {
    "name" : "Import Floor Plan",
    "author" : "Pedro Bronsveld",
    "description" : "Import HR floor plans.",
    "blender" : (2, 80, 0),
    "version" : (0, 0, 1),
    "location" : "View3D",
    "warning" : "",
    "category" : "Import-Export"
}

from . FloorplanImporter import FloorplanImporter
from . FloorplanExporter import FloorplanExporter
from . FloorplanTools import *


# Only needed if you want to add into a dynamic menu
def menu_func_import(self, context):
    self.layout.operator(FloorplanImporter.bl_idname, text="HR Floor Plan")

def menu_func_export(self, context):
    self.layout.operator(FloorplanExporter.bl_idname, text="HR Floor Plan")

def register():
    bpy.utils.register_class(FloorplanImporter)
    bpy.types.TOPBAR_MT_file_import.append(menu_func_import)
    bpy.utils.register_class(FloorplanExporter)
    bpy.types.TOPBAR_MT_file_export.append(menu_func_export)
    
    addonClasses = [ 
        AlignFloors,
        HideReferenceImages,
        ShowReferenceImages,
        HideRoomNodes,
        ShowRoomNodes,
        UpdateWallColors,
        CreateWalls,
        CreateNode,
        ConnectNodesToRooms,
        ConnectRoomsToNodes,
        MarkEntrances,
        UnmarkEntrances,
        RemoveWalls,
        SolidifyFloors,
        UnsolidifyFloors,
        IgnoreVertices,
        IncludeVertices,
        ShowWireframes,
        HideWireframes,
        BakeAmbientOcclusion,
        
        FloorplanToolsPanel
    ]

    for addonClass in addonClasses:
        bpy.utils.register_class(addonClass)


def unregister():
    bpy.utils.unregister_class(FloorplanImporter)
    bpy.types.TOPBAR_MT_file_import.remove(menu_func_import)
    bpy.utils.unregister_class(FloorplanExporter)
    bpy.types.TOPBAR_MT_file_export.remove(menu_func_export)

    addonClasses = [ 
        AlignFloors,
        HideReferenceImages,
        ShowReferenceImages,
        HideRoomNodes,
        ShowRoomNodes,
        UpdateWallColors,
        CreateWalls,
        CreateNode,
        ConnectNodesToRooms,
        ConnectRoomsToNodes,
        MarkEntrances,
        UnmarkEntrances,
        RemoveWalls,
        SolidifyFloors,
        UnsolidifyFloors,
        IgnoreVertices,
        IncludeVertices,
        ShowWireframes,
        HideWireframes,
        BakeAmbientOcclusion,

        FloorplanToolsPanel
    ]

    for addonClass in addonClasses:
        bpy.utils.unregister_class(addonClass)


if __name__ == "__main__":
    register()

    # test call
    bpy.ops.import_test.some_data('INVOKE_DEFAULT')
