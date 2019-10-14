import bpy

# ImportHelper is a helper class, defines filename and
# invoke() function which calls the file selector.
from bpy_extras.io_utils import ExportHelper
from bpy.props import StringProperty, BoolProperty, EnumProperty, IntProperty, FloatProperty
from bpy.types import Operator
from . FloorplanExporterFunctions import exportFloorplan


class FloorplanExporter(Operator, ExportHelper):
    """Exports a 3D floor plan."""
    bl_idname = "floorplan.export"  # important since its how bpy.ops.import_test.some_data is constructed
    bl_label = "Export Floorplan"

    filename_ext = ""

    filter_glob: StringProperty(
        default="*.glb;*.json",
        options={'HIDDEN'},
        maxlen=255,  # Max internal buffer length, longer would be clamped.
    )

    def execute(self, context):
        # return read_some_data(context, self.filepath, self.use_setting)
        return exportFloorplan(context, self.filepath)