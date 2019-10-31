import bpy

from . GeneralFunctions import *

def showReferenceImages(visible):
    referenceImages = getObjectsByBuildingPart('ReferenceImage')
    for referenceImage in referenceImages:
        referenceImage.hide_viewport = not visible
    return {'FINISHED'}