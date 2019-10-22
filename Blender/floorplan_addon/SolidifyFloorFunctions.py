import bpy

from . GeneralFunctions import *

def solidifyFloors(solidify=True):
    floors = getObjectsByBuildingPart('Floor')
    for floor in floors:
        if solidify:
            solidifyObject(floor, 0.3)
        else:
            removeModifier(floor, 'SOLIDIFY')

    return {'FINISHED'}