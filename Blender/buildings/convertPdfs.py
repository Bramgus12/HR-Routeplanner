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
import tempfile
from PyPDF2 import PdfFileReader
import xml.etree.ElementTree as ET

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
            floorplanFormat = formatMatch.group(0)

    pdftohtmlCommand = ["pdftohtml", "-xml", "-noroundcoord", "-hidden", "-stdout", pdfPath]
    pdftohtmlProcess = subprocess.Popen(pdftohtmlCommand, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    pdftohtmlOutput, err = pdftohtmlProcess.communicate()
    xmlStr = pdftohtmlOutput.decode('utf-8')

    xmlTree = ET.ElementTree(ET.fromstring(xmlStr))
    xmlRoot = xmlTree.getroot()

    pageNum = 1
    pageElements = [element for element in xmlRoot if element.tag == 'page']
    for pageElement in pageElements:

        pathExtra = "-p" + str(pageNum) if len(pageElements) > 1 else ""
        jsonPath = path_to_extension(pdfPath, "json", extra=pathExtra )
        if len(pageElements) > 1:
            print("\tpage {}/{}".format(pageNum, len(pageElements)))
        pageNum += 1
        if os.path.isfile(jsonPath):
            print(jsonPath, "already exists, skipping.")
            continue

        floorplan = {
            'buildingName': '',
            'width': float(pageElement.attrib.get('width')),
            'height': float(pageElement.attrib.get('height')),
            'orientation': 'Landscape',
            'scale': 100.0,
            'floorNumberDetected': False,
            'floorNumber': 0,
            'rooms': [],
            'format': floorplanFormat
        }

        detectedFloorNumbers = dict()
        roomTextHeights = dict()

        for textElement in [element for element in pageElement if element.tag == 'text' and element.text != None]:
            text = textElement.text
            roomMatch = re.search(r"^[0-9A-Z]+\.[\-0-9A-Za-z]+\.[0-9A-Za-z]+$", text)
            if(roomMatch != None):
                if(floorplan['buildingName'] == ''):
                    floorplan['buildingName'] = roomMatch.group(0).split('.')[0]

                floorMatch = re.search("[-0-9]+", roomMatch.group(0).split('.')[1])
                # Detect floor number from room code
                if floorMatch != None or roomMatch.group(0).split('.')[1] == 'K':
                    floorNumber = -1 if roomMatch.group(0).split('.')[1] == 'K' else int(floorMatch.group(0))
                    if detectedFloorNumbers.get(floorNumber) == None:
                        detectedFloorNumbers[floorNumber] = 1
                    else:
                        detectedFloorNumbers[floorNumber] += 1
                # Get text height from room code
                roomTextHeight = float(textElement.attrib.get('height'))
                if roomTextHeights.get(roomTextHeight) == None:
                    roomTextHeights[roomTextHeight] = 1
                else:
                    roomTextHeights[roomTextHeight] += 1
                # The text object is a room, save the corresponding data
                room = {
                    'code': roomMatch.group(0),
                    'label': '',
                    'x1': float(textElement.attrib.get('left')),
                    'y1': floorplan['height'] - float(textElement.attrib.get('top')),
                    'x2': float(textElement.attrib.get('left')) + float(textElement.attrib.get('width')),
                    'y2': floorplan['height'] - float(textElement.attrib.get('top')) + float(textElement.attrib.get('height'))
                }
                floorplan['rooms'].append(room)
            
            scaleMatch = re.search("(?<=Schaal) *1 *: *[0-9]*( *, *[0-9]*)?", text, re.IGNORECASE)
            if(scaleMatch != None):
                # The text object contains the floor plan scale
                floorplan['scale'] = float(scaleMatch.group(0).split(':')[-1].replace(',','.'))
            
        if len(detectedFloorNumbers) > 0:
            floorplan['floorNumber'] = max(detectedFloorNumbers)
            floorplan['floorNumberDetected'] = True
        
        if len(roomTextHeights) > 0:
            roomTextHeight = max(roomTextHeights)
            floorplanScale = ( 13.928625 / roomTextHeight ) * 100
            if floorplanScale > 100.0:
                floorplan['scale'] = floorplanScale

        # Save floorplan data to a json file
        jsonFile = open(jsonPath, 'w')
        json.dump(floorplan, jsonFile)
        jsonFile.close()
        
        # save the floor plan as a png file
        pngPath = path_to_extension(pdfPath, "png", extra=pathExtra)
        if os.path.isfile(pngPath):
            print(pngPath, "already exists, skipping.")
            continue
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
