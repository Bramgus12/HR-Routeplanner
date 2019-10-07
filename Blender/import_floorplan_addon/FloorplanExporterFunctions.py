import bpy
import json
from . FloorplanToolsFunctions import *

def exportFloorplan(context, filepath):
    # Object for all data to export
    floorplan = {
        'name': bpy.context.scene.name,
        'nodes': []
    }

    # Add nodes to export data
    nodeNetwork = bpy.context.scene.objects.get('[NodeNetwork]')
    roomNodes = list(filter(lambda obj: obj.get('buildingPart') == 'RoomNode', bpy.context.scene.objects))
    if nodeNetwork != None:
        for vertex in nodeNetwork.data.vertices:
            node = {
                'number': vertex.index,
                'isRoom': False,
                'code': '',
                'label': '',
                'connections': [],
                'x': vertex.co.x,
                'y': vertex.co.y,
                'z': vertex.co.z
            }
            for roomNode in roomNodes:
                if isSameLocation(roomNode.matrix_world.translation, vertex.co, 0.001):
                    node['isRoom'] = True
                    node['code'] = roomNode.get('roomCode')
                    node['label'] = roomNode.get('roomLabel')
                    break
            for edge in nodeNetwork.data.edges:
                if edge.vertices[0] == vertex.index or edge.vertices[1] == vertex.index:
                    connectedVertex = nodeNetwork.data.vertices[ edge.vertices[0] if edge.vertices[0] != vertex.index else edge.vertices[1] ]
                    connection = {
                        'number': connectedVertex.index,
                        'distance': distance(vertex.co, connectedVertex.co)
                    }
                    node['connections'].append(connection)
                    
            floorplan['nodes'].append(node)

    with open(filepath, 'w') as jsonFile:
        json.dump(floorplan, jsonFile)

    return {'FINISHED'}