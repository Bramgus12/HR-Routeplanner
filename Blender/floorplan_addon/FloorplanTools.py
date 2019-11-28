import bpy
from bpy.types import Operator, Panel

from . AlignFloorsFunctions import alignFloors
from . ReferenceImageFunctions import showReferenceImages
from . RoomNodeFunctions import showRoomNodes
from . WallFunctions import createWalls, removeWalls
from . IgnoreVerticesFunctions import ignoreVertices
from . SolidifyFloorFunctions import solidifyFloors
from . WireframeVisibilityFunctions import showWireframes
from . NodeNetworkFunctions import createNode, connectRoomNodes
from . AmbientOcclusionFunctions import setupAmbientOcclusion
from . UpdateWallColorsFunctions import updateWallColors

class FloorplanToolsPanel(Panel):
    bl_idname = "FLOORPLAN_PT_PANEL"
    bl_label = "Floorplan Panel"
    bl_category = "Floorplan"
    bl_space_type = "VIEW_3D"
    bl_region_type = "UI"

    def draw(self, context):
        layout = self.layout

        row = layout.row()
        row.label(text="Floors")

        row = layout.row()
        row.operator('floorplan.align_floors', text="Align Floors")

        row = layout.row()
        row.operator('floorplan.hide_reference_images', text="Hide Refs")
        row.operator('floorplan.show_reference_images', text="Show Refs")

        row = layout.row()
        row.operator('floorplan.hide_room_nodes', text="Hide Rooms")
        row.operator('floorplan.show_room_nodes', text="Show Rooms")

        row = layout.row()
        row.operator('floorplan.remove_walls', text="Remove Walls")
        row.operator('floorplan.create_walls', text="Create Walls")

        row = layout.row()
        row.operator('floorplan.ignore_vertices', text="Ignore Vertices")
        row.operator('floorplan.include_vertices', text="Include Vertices")

        row = layout.row()
        row.operator('floorplan.unsolidify_floors', text="Unsolodify Floors")
        row.operator('floorplan.solidify_floors', text="Solidify Floors")

        row = layout.row()
        row.operator('floorplan.hide_wireframes', text="Hide Wireframes")
        row.operator('floorplan.show_wireframes', text="Show Wireframes")

        row = layout.row()
        row.operator('floorplan.update_wall_colors', text="Update Viewport Wall Colors")

        row = layout.row()
        row.label(text="Node Network")

        row = layout.row()
        row.operator('floorplan.create_node', text="Create Node")

        row = layout.row()
        row.operator('floorplan.connect_nodes_to_rooms', text="Connect Nodes To Rooms")

        row = layout.row()
        row.operator('floorplan.connect_rooms_to_nodes', text="Connect Rooms To Nodes")

        row = layout.row()
        row.label(text="Ambient Occlusion")

        row = layout.row()
        row.operator('floorplan.bake_ao', text="Bake Ambient Occlusion")

class AlignFloors(Operator):
    bl_idname = "floorplan.align_floors"
    bl_label = "Align Floors"
    bl_description = "Align two floors by selecting two room nodes that are in the same location on both floors."

    def execute(self, context):
        return alignFloors()

class HideReferenceImages(Operator):
    bl_idname = "floorplan.hide_reference_images"
    bl_label = "Hide Reference Images"
    bl_description = "Hide all floorplan reference images."

    def execute(self, context):
        return showReferenceImages(False)

class ShowReferenceImages(Operator):
    bl_idname = "floorplan.show_reference_images"
    bl_label = "Show Reference Images"
    bl_description = "Show all floorplan reference images."

    def execute(self, context):
        return showReferenceImages(True)

class HideRoomNodes(Operator):
    bl_idname = "floorplan.hide_room_nodes"
    bl_label = "Hide Room Nodes"
    bl_description = "Hide all room nodes."

    def execute(self, context):
        return showRoomNodes(False)

class ShowRoomNodes(Operator):
    bl_idname = "floorplan.show_room_nodes"
    bl_label = "Show Room Nodes"
    bl_description = "Show all room nodes."

    def execute(self, context):
        return showRoomNodes(True)

class CreateWalls(Operator):
    bl_idname = "floorplan.create_walls"
    bl_label = "Create Walls"
    bl_description = "Create walls for floors."

    def execute(self, context):
        return createWalls()

class RemoveWalls(Operator):
    bl_idname = "floorplan.remove_walls"
    bl_label = "Remove Walls"
    bl_description = "Remove walls for floors."

    def execute(self, context):
        return removeWalls()

class CreateNode(Operator):
    bl_idname = "floorplan.create_node"
    bl_label = "Create a node."
    bl_description = "Create a new node in the network."

    def execute(self, context):
        return createNode()

class ConnectNodesToRooms(Operator):
    bl_idname = "floorplan.connect_nodes_to_rooms"
    bl_label = "Connect nodes to rooms."
    bl_description = "Connect nodes that are close to a room to that room."

    def execute(self, context):
        return connectRoomNodes(False)

class ConnectRoomsToNodes(Operator):
    bl_idname = "floorplan.connect_rooms_to_nodes"
    bl_label = "Connect rooms to nodes.."
    bl_description = "Connect each unconnected room node to the closest node in the existing node network."

    def execute(self, context):
        return connectRoomNodes(True)

class SolidifyFloors(Operator):
    bl_idname = "floorplan.solidify_floors"
    bl_label = "Add solidify modifier to floors."
    bl_description = "Adds a solidify modifier to all floors in the scene."

    def execute(self, context):
        return solidifyFloors(True)

class UnsolidifyFloors(Operator):
    bl_idname = "floorplan.unsolidify_floors"
    bl_label = "Remove solidify modifier from floors."
    bl_description = "Removed the solidify modifiers from the floors that have one."

    def execute(self, context):
        return solidifyFloors(False)

class IgnoreVertices(Operator):
    bl_idname = "floorplan.ignore_vertices"
    bl_label = "Ignore selected vertices when creating walls."
    bl_description = "Ignore selected vertices when creating walls."

    def execute(self, context):
        return ignoreVertices(True)

class IncludeVertices(Operator):
    bl_idname = "floorplan.include_vertices"
    bl_label = "Include selected vertices when creating walls."
    bl_description = "Include selected vertices when creating walls."

    def execute(self, context):
        return ignoreVertices(False)

class ShowWireframes(Operator):
    bl_idname = "floorplan.show_wireframes"
    bl_label = "Show wireframes."
    bl_description = "Show the wireframes for building parts."

    def execute(self, context):
        return showWireframes(True)

class HideWireframes(Operator):
    bl_idname = "floorplan.hide_wireframes"
    bl_label = "Hide wireframes."
    bl_description = "Hide the wireframes for building parts."

    def execute(self, context):
        return showWireframes(False)

class UpdateWallColors(Operator):
    bl_idname = "floorplan.update_wall_colors"
    bl_label = "Update wall colors."
    bl_description = "Update the color of each wall to the color given in the custom property of the floor root."

    def execute(self, context):
        return updateWallColors()

class BakeAmbientOcclusion(Operator):
    bl_idname = "floorplan.bake_ao"
    bl_label = "Bake Ambient Occlusion"
    bl_description = "Sets up materials and uv maps for building parts and bakes the ambient occlusion to an image."

    interrupted = False
    done = False

    def modal(self, context, event):

        if self.done:
            return {'FINISHED'}
        elif event.type in {'ESC'}:
            self.interrupted = True
            print("Escape was pressed, interrupting baking.")
            return {'CANCELLED'}
        
        return {'PASS_THROUGH'}

    def execute(self, context):
        res = setupAmbientOcclusion(self, context)
        if res == {'RUNNING_MODAL'}:
            context.window_manager.modal_handler_add(self)
        return res
