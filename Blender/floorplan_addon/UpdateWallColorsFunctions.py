import bpy
from . GeneralFunctions import *
import re

def updateWallColors():
    floorRoots = getObjectsByBuildingPart('FloorRoot')
    for floorRoot in floorRoots:
        if floorRoot.get('wallColor') == None:
            floorRoot['wallColor'] = ""
        
        if re.match(r"^[0-9A-F]{6}$", floorRoot['wallColor'], re.IGNORECASE):
            for childObj in floorRoot.children:
                if childObj.get('buildingPart') == "Wall" and len(childObj.material_slots) > 0:
                    principledBsdfNode = childObj.material_slots[0].material.node_tree.nodes.get('Principled BSDF')
                    if principledBsdfNode != None:
                        r, g, b = hexToRgb(floorRoot['wallColor'].upper())
                        principledBsdfNode.inputs['Base Color'].default_value[0] = (r/255)*1.0
                        principledBsdfNode.inputs['Base Color'].default_value[1] = (g/255)*1.0
                        principledBsdfNode.inputs['Base Color'].default_value[2] = (b/255)*1.0
                
    
    return { 'FINISHED' }