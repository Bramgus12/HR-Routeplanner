import bpy
import json
from . FloorplanToolsFunctions import *

def exportFloorplan(context, filepath):
    # Object for all data to export
    floorplan = {
        'locationName': bpy.context.scene.name,
        'nodes': [],
        'connections': []
    }

    connectionSets = []

    # Add nodes to export data
    nodeNetwork = bpy.context.scene.objects.get('[NodeNetwork]')
    roomNodes = list(filter(lambda obj: obj.get('buildingPart') == 'RoomNode', bpy.context.scene.objects))
    if nodeNetwork != None:
        for vertex in nodeNetwork.data.vertices:
            node = {
                'number': vertex.index,
                'type': 'Standard',
                'code': None,
                'label': None,
                'x': vertex.co.x,
                'y': vertex.co.y,
                'z': vertex.co.z
            }

            for roomNode in roomNodes:
                if isSameLocation(roomNode.matrix_world.translation, vertex.co, 0.001):
                    node['type'] = 'Room'
                    node['code'] = roomNode.get('roomCode')
                    node['label'] = roomNode.get('roomLabel')
                    break
            for edge in nodeNetwork.data.edges:
                if edge.vertices[0] == vertex.index or edge.vertices[1] == vertex.index:
                    connectedVertex = nodeNetwork.data.vertices[ edge.vertices[0] if edge.vertices[0] != vertex.index else edge.vertices[1] ]
                    connectionSet = {vertex.index, connectedVertex.index}
                    if connectionSet not in connectionSets:
                        connectionSets.append( connectionSet )
                    
            floorplan['nodes'].append(node)
    
    for connectionSet in connectionSets:
        nodeList = list(connectionSet)
        connection = {
            'node1': nodeList[0],
            'node2': nodeList[1],
            'distance': distance( nodeNetwork.data.vertices[nodeList[0]].co , nodeNetwork.data.vertices[nodeList[1]].co )
        }
        floorplan['connections'].append( connection )

    with open(filepath, 'w') as jsonFile:
        json.dump(floorplan, jsonFile)

    return {'FINISHED'}