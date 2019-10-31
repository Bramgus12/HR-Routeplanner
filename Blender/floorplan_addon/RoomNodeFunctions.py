import bpy

from . GeneralFunctions import *

def showRoomNodes(visible):
    roomNodes = getObjectsByBuildingPart('RoomNode')
    for roomNode in roomNodes:
        roomNode.hide_viewport = not visible
    return {'FINISHED'}