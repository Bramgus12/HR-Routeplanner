import bpy

from . GeneralFunctions import *

def showWireframes(show=True):
    buildingObjects = []
    buildingObjects.extend( getObjectsByBuildingPart('Floor') )
    buildingObjects.extend( getObjectsByBuildingPart('Wall') )
    for buildingObject in buildingObjects:
        buildingObject.show_wire = show
    return {'FINISHED'}