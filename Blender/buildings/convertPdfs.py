import os
import subprocess
from pdf2image import convert_from_path, convert_from_bytes
from pdf2image.exceptions import (
    PDFInfoNotInstalledError,
    PDFPageCountError,
    PDFSyntaxError
)
import re
import json
from pdfminer.pdfparser import PDFParser, PDFDocument
from pdfminer.pdfinterp import PDFResourceManager
from pdfminer.pdfinterp import PDFPageInterpreter
from pdfminer.pdfdevice import PDFDevice
from pdfminer.converter import PDFPageAggregator
from pdfminer.layout import LAParams, LTTextBox, LTTextLine
import pdfminer
from PyPDF2 import PdfFileReader

def path_to_extension(path, extension, extra=""):
    return ".".join(path.split(".")[:-1]) + extra + "." + extension

root = os.path.dirname(os.path.realpath(__file__))
pdfPaths = []

# get list with all pdf files
for path, subdirs, files in os.walk(root):
    for name in files:
        if name.endswith(".pdf"):
            pdfPaths.append(os.path.join(path, name))

pdfNum = 1
for pdfPath in pdfPaths:
    print( "[{}/{}] Converting '{}' to png and json files.".format(pdfNum, len(pdfPaths), os.path.split(pdfPath)[-1] ) )
    pdfNum += 1

    floorplanFormat = 'A3'

    with open(pdfPath, 'rb') as pdfFile:
        pdf = PdfFileReader(pdfFile)
        info = pdf.getDocumentInfo()
        formatMatch = re.search("^A[0-9]$", info.title)
        if formatMatch != None:
            floorplan['format'] = formatMatch.group(0)

    # Extract text and text position with pdf miner
    with open(pdfPath, 'rb') as pdf_file:
        parser = PDFParser(pdf_file)
        document = PDFDocument(parser)
        parser.set_document(document)
        document.set_parser(parser)
        document.initialize()
        rsrcmgr = PDFResourceManager()
        laparams = LAParams()
        device = PDFPageAggregator(rsrcmgr, laparams=laparams)
        interpreter = PDFPageInterpreter(rsrcmgr, device)

        pagesDimensions = list(document.get_pages())
        pageNum = 1
        for dimensions in pagesDimensions:
            pathExtra = "-p" + str(pageNum) if len(pagesDimensions) > 1 else ""
            jsonPath = path_to_extension(pdfPath, "json", extra=pathExtra )
            if len(pagesDimensions) > 1:
                print("\tpage {}/{}".format(pageNum, len(pagesDimensions)))
            pageNum += 1
            if os.path.isfile(jsonPath):
                print(jsonPath, "already exists, skipping.")
                continue

            floorplan = {
                'buildingName': '',
                'width': 0,
                'height': 0,
                'orientation': 'Landscape',
                'scale': 100.0,
                'floorNumberDetected': False,
                'floorNumber': 0,
                'rooms': [],
                'format': formatMatch
            }

            floorplan['width'] = dimensions.mediabox[2]
            floorplan['height'] = dimensions.mediabox[3]

            interpreter.process_page(dimensions)
            layout = device.get_result()
            detectedFloorNumbers = dict()

            for lt_obj in layout:
                if isinstance(lt_obj, LTTextBox) or isinstance(lt_obj, LTTextLine):
                    text = lt_obj.get_text()
                    roomMatch = re.search(r"[0-9A-Z]+\.[\-0-9A-Za-z]+\.[0-9A-Za-z]+", text)
                    if(roomMatch != None):
                        if(floorplan['buildingName'] == ''):
                            floorplan['buildingName'] = roomMatch.group(0).split('.')[0]

                        floorMatch = re.search("[-0-9]+", roomMatch.group(0).split('.')[1])
                        if floorMatch != None or roomMatch.group(0).split('.')[1] == 'K':
                            floorNumber = -1 if roomMatch.group(0).split('.')[1] == 'K' else int(floorMatch.group(0))
                            if detectedFloorNumbers.get(floorNumber) == None:
                                detectedFloorNumbers[floorNumber] = 1
                            else:
                                detectedFloorNumbers[floorNumber] += 1
                        # The text object is a room, save the corresponding data
                        room = {
                            'code': roomMatch.group(0),
                            'label': re.sub(r'\n[\s\S]*[0-9]+[\s\S]*|[\s\S]*[0-9] m2[\s\S]*', '', text[roomMatch.span()[1]:].strip() ),
                            'x1': lt_obj.bbox[0],
                            'y1': lt_obj.bbox[1],
                            'x2': lt_obj.bbox[2],
                            'y2': lt_obj.bbox[3]
                        }
                        floorplan['rooms'].append(room)
                    
                    scaleMatch = re.search("(?<=Schaal) *1 *: *[0-9]*( *, *[0-9]*)?", text, re.IGNORECASE)
                    if(scaleMatch != None):
                        # The text object contains the floor plan scale
                        floorplan['scale'] = float(scaleMatch.group(0).split(':')[-1].replace(',','.'))

            if len(detectedFloorNumbers) > 0:
                floorplan['floorNumber'] = max(detectedFloorNumbers)
                floorplan['floorNumberDetected'] = True
            
            # Save floorplan data to a json file
            jsonFile = open(jsonPath, 'w')
            json.dump(floorplan, jsonFile)
            jsonFile.close()

            # save the floor plan as a png file
            pngPath = path_to_extension(pdfPath, "png", extra=pathExtra)
            image = convert_from_path(pdfPath, size=(5000, None), first_page=pageNum-1)[0].convert("RGBA")
            # make background transparent
            pixdata = image.load()
            for y in range(image.size[1]):
                for x in range(image.size[0]):
                    if pixdata[x, y] == (255, 255, 255, 255):
                        pixdata[x, y] = (0, 0, 0, 0)
                    else:
                        pixdata[x, y] = (0, 255, 255, 255)
            image.save(pngPath)
