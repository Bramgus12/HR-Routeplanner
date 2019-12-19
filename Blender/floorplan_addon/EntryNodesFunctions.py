import bpy

from . GeneralFunctions import *

def mark(state):
    roomNodes = getObjectsByBuildingPart('RoomNode')
    for roomNode in roomNodes:
        if roomNode.select_get():
            if state:
                roomNode['isEntrance'] = True
            elif not state and roomNode.get('isEntrance'):
                roomNode['isEntrance'] = False

def markEntrances():
    mark(True)
    return {'FINISHED'}

def unmarkEntrances():
    mark(False)
    return {'FINISHED'}