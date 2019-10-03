import bpy

# ImportHelper is a helper class, defines filename and
# invoke() function which calls the file selector.
from bpy_extras.io_utils import ImportHelper
from bpy.props import StringProperty, BoolProperty, EnumProperty, IntProperty, FloatProperty
from bpy.types import Operator
from . FloorplanImporterFunctions import loadFloorplan


class FloorplanImporter(Operator, ImportHelper):
    """Imports a json file and png for a floor plan of Hogeschool Rotterdam."""
    bl_idname = "floorplan.import"  # important since its how bpy.ops.import_test.some_data is constructed
    bl_label = "Import Some Data"

    # ImportHelper mixin class uses this
    filename_ext = ".json"

    filter_glob: StringProperty(
        default="*.json",
        options={'HIDDEN'},
        maxlen=255,  # Max internal buffer length, longer would be clamped.
    )

    # List of operator properties, the attributes will be assigned
    # to the class instance from the operator settings before calling.
    autoBuildingName: BoolProperty(
        name="Auto Building Name",
        description="Automatically retrieve the building name from the json file.",
        default=True
    )

    buildingName: StringProperty(
        name="Building Name",
        description="Name of the entire building.",
        default="Building"
    )

    autoFloorNumber: BoolProperty(
        name="Auto Floor Number",
        description="Automatically retrieve the floor number from the json file.",
        default=True
    )

    floorNumber: IntProperty(
        name="Floor Number",
        description="Number of the floor. 0 is the ground floor.",
        default=0
    )

    floorHeight: FloatProperty(
        name="Floor Height",
        description="Height of each floor in meters.",
        default=3.0
    )

    # type: EnumProperty(
    #     name="Example Enum",
    #     description="Choose between two items",
    #     items=(
    #         ('OPT_A', "First Option", "Description one"),
    #         ('OPT_B', "Second Option", "Description two"),
    #     ),
    #     default='OPT_A',
    # )

    def execute(self, context):
        # return read_some_data(context, self.filepath, self.use_setting)
        return loadFloorplan(context, self.filepath, self.buildingName, self.floorNumber, self.floorHeight, self.autoBuildingName, self.autoFloorNumber)