import bpy
import json
from bpy_extras.io_utils import ImportHelper
from bpy.props import StringProperty, BoolProperty, EnumProperty, IntProperty, FloatProperty
from bpy.types import Operator

from . FloorplanImporterFunctions import *

class FloorplanImporter(Operator, ImportHelper):
    """Imports a json file and png for a floor plan of Hogeschool Rotterdam."""
    bl_idname = "floorplan.import"
    bl_label = "Import Json Floorplan"

    # ImportHelper mixin class uses this
    filename_ext = ".json"

    filter_glob: StringProperty(
        default="*.json",
        options={'HIDDEN'},
        maxlen=255,
    )

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

    def execute(self, context):
        # return read_some_data(context, self.filepath, self.use_setting)
        return loadFloorplan(context, self.filepath, self.buildingName, self.floorNumber, self.floorHeight, self.autoBuildingName, self.autoFloorNumber)

