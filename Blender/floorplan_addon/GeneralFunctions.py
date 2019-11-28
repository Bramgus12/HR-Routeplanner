import bpy
import math
import os

def pathToExtension(path, extension):
    '''Returns the input path with the correct extension.
    Replaces the extension if the input path already has one.'''
    return os.path.join( os.path.dirname(path), os.path.splitext(os.path.basename(path))[0] + "." + extension )

def getObjectsByBuildingPart(part):
    '''Returns a list of objects of the given building part.'''
    return list( filter(lambda obj: obj.get('buildingPart') == part, bpy.context.scene.objects) )

def distanceBetween2D(v1, v2):
    '''Returns the distance between two vectors in 2D space.'''
    return math.sqrt( (v2.x - v1.x)**2 + (v2.y - v1.y)**2 )

def distance(v1, v2):
    '''Returns the distance between two vectors in 3D space.'''
    return math.sqrt( (v2.x - v1.x)**2 + (v2.y - v1.y)**2 + (v2.z - v1.z)**2 )

def isSameLocation(vector1, vector2, margin):
    '''Checks if two vectors are roughly in the same location, within a given error margin.'''
    for i in range(3):
        if abs( vector1[i] - vector2[i] ) > margin:
            return False
    return True

def linkToFloorCollection(obj ,floorNumber):
    '''Adds the object to the floor collection of the given floor.
    Creates the floor collection if it doesn't exist yet.'''
    part = 'FloorCollection'
    name = '[{}] {}'.format(part, floorNumber)
    collection = bpy.data.collections.get(name)
    if collection == None:
        collection = bpy.data.collections.new(name)
    if bpy.context.scene.collection.children.get(collection.name) == None:
        bpy.context.scene.collection.children.link(collection)
    if collection.objects.get(obj.name) == None:
        collection.objects.link(obj)
    collection['buildingPart'] = part
    collection['floorNumber'] = floorNumber

def unlinkFromCollections(obj):
    '''Unlinks the object from all collections it's in.'''
    for collection in bpy.data.collections:
        if collection.objects.get(obj.name) != None:
            collection.objects.unlink(obj)

def removeModifier(obj, modifierType):
    '''Adds a modifier of the given type to the object.'''
    for modifier in obj.modifiers:
        if modifier.type == modifierType:
            obj.modifiers.remove(modifier)

def solidifyObject(obj, thickness=0.1):
    '''Removed all solidify modifiers from the object, then creates a new solidify modifier.'''
    # Remove existing solidify modifiers
    removeModifier(obj, 'SOLIDIFY')
    # Add new solidify modifier
    modifier = obj.modifiers.new(name='Solidify', type='SOLIDIFY')
    modifier.thickness = thickness
    return modifier

def hexToRgb(h):
    '''Returns the rgb integers of a hex color value.'''
    h = h.lstrip('#')
    hlen = len(h)
    return tuple( int(h[i:i+hlen//3], 16) for i in range(0, hlen, hlen//3) )