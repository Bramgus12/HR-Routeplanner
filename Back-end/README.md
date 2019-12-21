# API Documentation

## Swagger-UI

**Swagger-ui on link `[host]:[port]/swagger-ui.html` when server is running. This contains a more detailed and interactive API-Documentation**

**When running on your own pc in the IDE, that would be `localhost:8080/swagger-ui.html`**

## Authentication

**This API uses an authentication key that needs to be in the users table.**

## Navigation:
1. [Address](#address)
    1. [Get all addresses](#get-apiaddress)
    2. [Get a certain address](#get-apiaddressid)
    3. [Get an address by buildingname](#get-apiaddressbuilding)
    4. [Get an address by roomcode](#get-apiaddressroom)
    5. [Post](#post-apiaddress)
    6. [Delete](#delete-apiaddressid)
    7. [Update](#put-apiaddressid)
2. [Building](#building)
    1. [Get all buildings](#get-apibuilding)
    2. [Get a certain building](#get-apibuildingid)
3. [Institute](#institute)
    1. [Get all institutes](#get-apiinstitute)
    2. [Get a certain institute](#get-apiinstituteid)
    3. [Post](#post-apiinstitute)
    4. [Delete](#delete-apiinstituteid)
    5. [Update](#put-apiinstituteid)
4. [BuildingInstitute](#buildinginstitute)
    1. [Get all buildingInstitutes](#get-apibuildinginstitute)
    2. [Get a certain buildingInstitute](#get-apibuildinginstituteid)
    3. [Post](#post-apibuildinginstitute)
    4. [Put](#put-apibuildinginstituteid)
    5. [Delete](#delete-apibuildinginstituteid)
5. [LocationNodeNetwork](#locationnodenetwork)
    1. [Get a certain locationnodenetwork](#get-apilocationnodenetworklocationname)
    2. [Post](#post-apilocationnodenetworkaddressid)
    3. [Delete](#delete-apilocationnodenetworklocationname)
    4. [Put](#put-apilocationnodenetworklocationname)
    5. [Get all nodes by type](#get-apilocationnodenetwork)
    6. [Get all nodes that are a room](#get-apilocationnodenetworkroom)
6. [Route-engine](#route-engine)
    1. [Get the route between two nodes.](#get-apiroutes)
7. [ElectionCourse](#election-course)
    1. [Get a list of all election courses](#get-apielection-course)
    2. [Get a description of a specific election course](#get-apielection-coursecoursecode)
    3. [Get all descriptions of election courses](#get-apielection-coursedescription)
    4. [Upload election course file](#post-apielection-courseupload)
    5. [Create a description for an election course](#post-apielection-course)
    6. [Update the election course excel file](#put-apielection-course)
    7. [update the description of an election course](#put-apielection-coursecoursecode)
    8. [Delete the description of an election course](#delete-apielection-coursecoursecode)


****

## Address
### GET `/api/address`
Get a list of all addresses.

**Response:**
```json
[
    {
        "id": 0,
        "street": "string",
        "number": 0,
        "city": "string",
        "postal": "string",
        "addition": "string"
    },
    {
        "id": 0,
        "street": "string",
        "number": 0,
        "city": "string",
        "postal": "string",
        "addition": "string"
    }
]
```

    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### GET `/api/address/{id}`
Get a certain address.

**Response:**
```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string",
    "addition": "string"
}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### GET `/api/address/building`
Get and address by buildingname.

**Requested parameter:**
```properties
name=[string]
```
**Response**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string",
    "addition": "string"
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### GET `/api/address/room`

Get an address by roomcode.

**Requsted parameter:**
```properties
code=[string]
```

**Response**
```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string",
    "addition": "string"
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### POST `/api/address`
Create a new address.

**Requested parameter:**

```properties
key=[string]
```
 
**Requested body:**

```json
{
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string",
    "addition": "string"
}
```
    
**Response:**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string",
    "addition": "string"
}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### DELETE `/api/address/{id}`
Delete an address by id.

**Requested parameter:**

```properties
key=[string]
```

**Response:**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string",
    "addition": "string"
}
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### PUT `/api/address/{id}`
Update a certain address.

**Requested parameter:**

```properties
key=[string]
```

**Requested body:**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string",
    "addition": "string"
}
```
 

**Response:**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string",
    "addition": "string"

}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

## Building
### GET `/api/building`
Gives you a list of all buildings.

**Response:**

``` json
[
    {
        "id": 0,
        "address_id": 0,
        "name": "string"
    },
    {
        "id": 0,
        "address_id": 0,
        "name": "String"
    }
]
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****
    
### GET `/api/building/{id}`
Get a specific building.

**Response:**
``` json
{
    "id": 0,
    "address_id": 0,
    "name": "string"
}
``` 
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

## Institute

****

### GET `/api/institute`
Gives you a list of all Institutes.

**Response:**
``` json
[
    {
        "id": 0,
        "name": "String"
    },
    {
        "id": 0,
        "name": "String"
    }
]
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****
    
### GET `/api/institute/{id}`
Get a specific institute by ID.

**Response:**
``` json
{
    "id": 0,
    "name": "String"
}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### POST `/api/institute`
Add/Create a new Institute.

**Requested parameter:**

```properties
key=[string]
```

**Requested body:**
``` json
{
    "name": "String"
} 
```

**Response:**
``` json
{
    "id": "Integer",
    "name": "String"
}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### DELETE `/api/institute/{id}`
Delete an Institute by id.

**Requested parameter:**

```properties
key=[string]
```

**Response:**

``` json
{
    "id": "Integer",
    "name": "String"
}
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### PUT `/api/institute/{id}`
Update an institute.

**Requested parameter:**

```properties
key=[string]
```

**Requested body:**

``` json
{
    "id": "Integer",
    "name": "String"
}
```
    
**Response**

``` json
{
    "id": "Integer",
    "name": "String"
}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

## BuildingInstitute

****
### GET `/api/buildinginstitute`
Get a list of all buildingInstitutes

**Response:**
```json
[
    {
        "buildingId": 0,
        "id": 0,
        "instituteId": 0
    },
    {
        "buildingId": 0,
        "id": 0,
        "instituteId": 0
    }
]
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### GET `/api/buildinginstitute/{id}`
Get a certain buildinginstitute by id

**Response:**
```json
{
    "buildingId": 0,
    "id": 0,
    "instituteId": 0
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### POST `/api/buildinginstitute`
Create a new buildingInstitute

**Requested parameter:**

```properties
key=[string]
```

**Requested body:**
```json
{
    "buildingId": 0,
    "instituteId": 0
}
```

**Response:**
```json
{
    "buildingId": 0,
    "id": 0,
    "instituteId": 0
}
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### PUT `/api/buildinginstitute/{id}`
Update a certain buldinginstitute by id

**Requested parameter:**

```properties
key=[string]
```

**Requested body:**
```json
{
    "buildingId": 0,
    "id": 0,
    "instituteId": 0
}
```

**Response:**
```json
{
    "buildingId": 0,
    "id": 0,
    "instituteId": 0
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### DELETE `/api/buildinginstitute/{id}`
Delete a certain buildingInstitute by id

**Requested parameter:**

```properties
key=[string]
```

**Response:**
```json
{
    "buildingId": 0,
    "id": 0,
    "instituteId": 0
}
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****
    
## LocationNodeNetwork

**LocationNodeNetwork is not stored in an SQL-Database. It is stored inside of a JSON file.**

****

### GET `/api/locationnodenetwork/{locationName}`
Get a certain locationNodeNetwork object by locationName.

**Response:**
``` json
{
    "locationName": "String",
    "nodes": [
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        },
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        }
    ],
    "connections": [
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        },
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        }
    ]
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****
    
### POST `/api/locationnodenetwork/{Addressid}`
Post a node into the server. You can only do this when it does not exist already on the server otherwhise you will get an error. You also have to specify an address that corresponds with the locationNodeNetwork that you are about to add. It will then automatically add a new building to the database with in it the locatonName of the locationNodeNetwork and the address_id of the address that you specified.

Put the address_id of the address that corresponds with the locationNodeNetwork in the URL.

**Requested parameter:**

```properties
key=[string]
```

**Upload file with the following layout:**

``` json
{
    "locationName": "String",
    "nodes": [
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        },
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        }
    ],
    "connections": [
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        },
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        }
    ]
}
```
    
**Response:**

``` json
{
    "locationName": "String",
    "nodes": [
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        },
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        }
    ],
    "connections": [
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        },
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        }
    ]
}
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****

### DELETE `/api/locationnodenetwork/{locationname}`
Deletes the locationNodeNetwork indicated by the locationName in the URL. This also deletes the buildingobject in the database.

**Requested parameter:**

```properties
key=[string]
```

**Response:**

``` json
{
    "locationName": "String",
    "nodes": [
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        },
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        }
    ],
    "connections": [
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        },
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        }
    ]
}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****
    
### PUT `/api/locationnodenetwork`
Update a certain locationNodeNetwork. It deletes the old one and replaces it with this one. This also updates the buildingobject in the database.

**Requested parameters:**
```properties
addressId=[integer]
locationName=[string]
key=[string]
```

**Upload a file with the following layout:**

 ``` json
{
    "locationName": "String",
    "nodes": [
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        },
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        }
    ],
    "connections": [
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        },
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        }
    ]
}
```
    
**Response:**

``` json
{
    "locationName": "String",
    "nodes": [
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        },
        {
            "number": 0,
            "type": "String",
            "code": "String",
            "label": "String",
            "x": 0.0,
            "y": 0.0,
            "z": 0.0
        }
    ],
    "connections": [
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        },
        {
            "node1": 0,
            "node2": 0,
            "distance": 0.0
        }
    ]
}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****
### GET `/api/locationnodenetwork`
Get nodes by type in a locationNodeNetwork

**Requested parameters:**
```properties
locationName=[string]
type=[string]
```

**Response:**

``` json
[
    {
        "number": 0,
        "type": "String",
        "code": "String",
        "label": "String",
        "x": 0.0,
        "y": 0.0,
        "z": 0.0
    },
    {
        "number": 0,
        "type": "String",
        "code": "String",
        "label": "String",
        "x": 0.0,
        "y": 0.0,
        "z": 0.0
    }
]
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****
### GET `/api/locationnodenetwork/room`
Get all nodes that are a room

**Response:**
``` json
[
    {
        "locationName": "string",
        "nodes": [
            {
                "code": "string",
                "label": "string",
                "number": 0,
                "type": "string",
                "x": 0.0,
                "y": 0.0,
                "z": 0.0
            }
        ]
    }
]
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****

## Route-engine

****

### GET `/api/routes`
Get the route between two nodes.

**Requested parameters:**
```
from": [integer]
to": [integer]
locationName": [string]
```

**Response:**
``` json
[
    {
        "number": 0,
        "type": "String",
        "code": "String",
        "label": "String",
        "x": 0.0,
        "y": 0.0,
        "z": 0.0
    },
    {
        "number": 0,
        "type": "String",
        "code": "String",
        "label": "String",
        "x": 0.0,
        "y": 0.0,
        "z": 0.0
    }
]
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****


## Election Course


****

### GET `/api/election-course`
Get a list of all election courses.

**Response:**
``` json
[
    {
        "classroom": "string",
        "courseCode": "string",
        "dayOfTheWeek": "string",
        "endTime": "string",
        "groupNumber": "string",
        "location": "string",
        "name": "string",
        "period": "string",
        "startTime": "string",
        "teacher": "string"
    },
    {
        "classroom": "string",
        "courseCode": "string",
        "dayOfTheWeek": "string",
        "endTime": "string",
        "groupNumber": "string",
        "location": "string",
        "name": "string",
        "period": "string",
        "startTime": "string",
        "teacher": "string"
    }
]
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### GET `/api/election-course/{coursecode}`
Get a description of the election course by coursecode

**Response:**
```json
{
    "courseCode": "string",
    "description": "string",
    "name": "string"
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### GET `/api/election-course/description`
Get all descriptions of the election courses in a list

**Response:**
```json
[
    {
        "courseCode": "string",
        "description": "string",
        "name": "string"
    },
    {
        "courseCode": "string",
        "description": "string",
        "name": "string"
    }
]
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****
### POST `/api/election-course/upload`
Upload an EXCEL file with the election courses of the Rotterdam University of Applied Sciences. This file can be found on https://hint.hr.nl/nl/HR/Studie/keuzes-in-je-studie/Keuzecursussen/

**Requested parameter:**

```properties
key=[string]
```

**Response:**
``` json
[
    {
        "classroom": "string",
        "courseCode": "string",
        "dayOfTheWeek": "string",
        "endTime": "string",
        "groupNumber": "string",
        "location": "string",
        "name": "string",
        "period": "string",
        "startTime": "string",
        "teacher": "string"
    },
    {
        "classroom": "string",
        "courseCode": "string",
        "dayOfTheWeek": "string",
        "endTime": "string",
        "groupNumber": "string",
        "location": "string",
        "name": "string",
        "period": "string",
        "startTime": "string",
        "teacher": "string"
    }
]
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****

### POST `/api/election-course`
Add a specific Election Course with its description, use https://www.freeformatter.com/json-escape.html to escape text

**Requested parameter:**

```properties
key=[string]
```

**Requested body:**
``` json
{
    "courseCode": "string",
    "description": "string",
    "name": "string"
}
```

**Response:**
``` json
{
    "courseCode": "string",
    "description": "string",
    "name": "string"
}
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****

### PUT `/api/election-course`
Update Election Course excel file in Election Course folder by deleting the file first if exist then upload again. The file can be found on https://hint.hr.nl/nl/HR/Studie/keuzes-in-je-studie/Keuzecursussen/

**Requested parameter:**

```properties
key=[string]
```

**Response:**
``` json
[
    {
        "classroom": "string",
        "courseCode": "string",
        "dayOfTheWeek": "string",
        "endTime": "string",
        "groupNumber": "string",
        "location": "string",
        "name": "string",
        "period": "string",
        "startTime": "string",
        "teacher": "string"
    },
    {
        "classroom": "string",
        "courseCode": "string",
        "dayOfTheWeek": "string",
        "endTime": "string",
        "groupNumber": "string",
        "location": "string",
        "name": "string",
        "period": "string",
        "startTime": "string",
        "teacher": "string"
    }
]
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****

### PUT `/api/election-course/{coursecode}`
Update a specific description of an election-course

**Requested parameter:**

```properties
key=[string]
```

**Requested body:**
```json
{
    "courseCode": "string",
    "description": "string",
    "name": "string"
}
```

**Response:**
```json
{
    "courseCode": "string",
    "description": "string",
    "name": "string"
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)


****
### DELETE `/api/election-course/{coursecode}`
Delete a specific Election Course with its description

**Requested parameter:**

```properties
key=[string]
```

**Response:**
```json
{
    "courseCode": "string",
    "description": "string",
    "name": "string"
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

