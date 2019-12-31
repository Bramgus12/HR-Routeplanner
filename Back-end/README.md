# API Documentation

## Swagger-UI

**Swagger-ui on link `[host]:[port]/swagger-ui.html` when server is running. This contains a more detailed and interactive API-Documentation**

**When running on your own pc in the IDE, that would be `localhost:8080/swagger-ui.html`**

## Authentication

**This api uses BASIC API AUTHENTICATION for all the POST, PUT and DELETE endpoints. There are two roles a user can have: `"ADMIN"` and `"USER"`. Only admins can create and change users. Passwords will be encrypted and cannot be read by anyone after encryption.**

## Navigation:
1. [Address](#address)
    1. [Get all addresses](#get-apiaddress)
    2. [Get a certain address](#get-apiaddressid)
    3. [Get an address by buildingname](#get-apiaddressbuilding)
    4. [Get an address by roomcode](#get-apiaddressroom)
    5. [Post](#post-apiadminaddress)
    6. [Delete](#delete-apiadminaddressid)
    7. [Update](#put-apiadminaddressid)
2. [Building](#building)
    1. [Get all buildings](#get-apibuilding)
    2. [Get a certain building](#get-apibuildingid)
3. [Institute](#institute)
    1. [Get all institutes](#get-apiinstitute)
    2. [Get a certain institute](#get-apiinstituteid)
    3. [Post](#post-apiadmininstitute)
    4. [Delete](#delete-apiadmininstituteid)
    5. [Update](#put-apiadmininstituteid)
4. [BuildingInstitute](#buildinginstitute)
    1. [Get all buildingInstitutes](#get-apibuildinginstitute)
    2. [Get a certain buildingInstitute](#get-apibuildinginstituteid)
    3. [Post](#post-apiadminbuildinginstitute)
    4. [Put](#put-apiadminbuildinginstituteid)
    5. [Delete](#delete-apiadminbuildinginstituteid)
5. [LocationNodeNetwork](#locationnodenetwork)
    1. [Get a certain locationnodenetwork](#get-apilocationnodenetworklocationname)
    2. [Post](#post-apiadminlocationnodenetworkaddressid)
    3. [Delete](#delete-apiadminlocationnodenetworklocationname)
    4. [Put](#put-apiadminlocationnodenetwork)
    5. [Get all nodes by type](#get-apilocationnodenetwork)
    6. [Get all nodes that are a room](#get-apilocationnodenetworkroom)
6. [Route-engine](#route-engine)
    1. [Get the route between two nodes.](#get-apiroutes)
7. [electiveCourse](#elective-course)
    1. [Get a list of all elective courses](#get-apielective-course)
    2. [Get a description of a specific elective course](#get-apielectivecoursecoursecode)
    3. [Get all descriptions of elective courses](#get-apielectivecoursedescription)
    4. [Upload elective course file](#post-apiadminelectivecourseupload)
    5. [Create a description for an elective course](#post-apiadminelectivecourse)
    6. [Update the elective course excel file](#put-apiadminelectivecourse)
    7. [update the description of an elective course](#put-apiadminelectivecoursecoursecode)
    8. [Delete the description of an elective course](#delete-apiadminelectivecoursecoursecode)
8. [Users](#users)
    1. [Get all users](#get-apiusers)
    2. [Create a user](#post-apiusers)
    3. [Update a user](#put-apiusersid)
    4. [Delete a user](#delete-apiusersid)


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

**Requested pathvariable:**
```properties
id=[integer]
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

### POST `/api/admin/address`
Create a new address.
 
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
    
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

****

### DELETE `/api/admin/address/{id}`
Delete an address by id.

**Requested pathvariable:**
```properties
id=[integer]
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

**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

****

### PUT `/api/admin/address/{id}`
Update a certain address.

**Requested pathvariable:**
```properties
id=[integer]
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
    
**HTTP-statuses:** 400, 401, 200

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

**Requested pathvariable:**
```properties
id=[integer]
```

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

**Requested pathvariable:**
```properties
id=[integer]
```

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

### POST `/api/admin/institute`
Add/Create a new Institute.



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
    
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

****

### DELETE `/api/admin/institute/{id}`
Delete an Institute by id.

**Requested pathvariable:**
```properties
id=[integer]
```

**Response:**

``` json
{
    "id": "Integer",
    "name": "String"
}
```

**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

****

### PUT `/api/admin/institute/{id}`
Update an institute.

**Requested pathvariable:**
```properties
id=[integer]
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
    
**HTTP-statuses:** 400, 401, 200

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

**Requested pathvariable:**
```properties
id=[integer]
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

### POST `/api/admin/buildinginstitute`
Create a new buildingInstitute


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

**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

****

### PUT `/api/admin/buildinginstitute/{id}`
Update a certain buldinginstitute by id

**Requested pathvariable:**
```properties
id=[integer]
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
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

****

### DELETE `/api/admin/buildinginstitute/{id}`
Delete a certain buildingInstitute by id

**Requested pathvariable:**
```properties
id=[integer]
```

**Response:**
```json
{
    "buildingId": 0,
    "id": 0,
    "instituteId": 0
}
```

**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)


****
    
## LocationNodeNetwork

**LocationNodeNetwork is not stored in an SQL-Database. It is stored inside of a JSON file.**

****

### GET `/api/locationnodenetwork/{locationName}`
Get a certain locationNodeNetwork object by locationName.

**Requested pathvariable:**
```properties
locationname=[string]
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
    
### POST `/api/admin/locationnodenetwork/{Addressid}`
Post a node into the server. You can only do this when it does not exist already on the server otherwhise you will get an error. You also have to specify an address that corresponds with the locationNodeNetwork that you are about to add. It will then automatically add a new building to the database with in it the locatonName of the locationNodeNetwork and the address_id of the address that you specified.

Put the address_id of the address that corresponds with the locationNodeNetwork in the URL.

**Requested pathvariable:**
```properties
addressid=[string]
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

**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)


****

### DELETE `/api/admin/locationnodenetwork/{locationname}`
Deletes the locationNodeNetwork indicated by the locationName in the URL. This also deletes the buildingobject in the database.

**Requested pathvariable:**
```properties
locationname=[string]
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
    
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)


****
    
### PUT `/api/admin/locationnodenetwork`
Update a certain locationNodeNetwork. It deletes the old one and replaces it with this one. This also updates the buildingobject in the database.

**Requested parameters:**
```properties
addressId=[integer]
locationName=[string]
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
    
**HTTP-statuses:** 400, 401, 200

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
    },
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
```properties
from=[integer]
to=[integer]
locationName=[string]
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


## Elective Course


****

### GET `/api/electivecourse`
Get a list of all elective courses.

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

### GET `/api/electivecourse/{coursecode}`
Get a description of the elective course by coursecode

**Requested pathvariable:**
```properties
coursecode=[string]
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

### GET `/api/electivecourse/description`
Get all descriptions of the elective courses in a list

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
### POST `/api/admin/electivecourse/upload`
Upload an EXCEL file with the elective courses of the Rotterdam University of Applied Sciences. This file can be found on https://hint.hr.nl/nl/HR/Studie/keuzes-in-je-studie/Keuzecursussen/


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
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)


****

### POST `/api/admin/electivecourse`
Add a specific elective Course with its description, use https://www.freeformatter.com/json-escape.html to escape text


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

**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)


****

### PUT `/api/admin/electivecourse`
Update elective Course excel file in elective Course folder by deleting the file first if exist then upload again. The file can be found on https://hint.hr.nl/nl/HR/Studie/keuzes-in-je-studie/Keuzecursussen/


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
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)


****

### PUT `/api/admin/electivecourse/{coursecode}`
Update a specific description of an elective-course

**Requested pathvariable:**
```properties
coursecode=[string]
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
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)


****
### DELETE `/api/admin/electivecourse/{coursecode}`
Delete a specific elective Course with its description

**Requested pathvariable:**
```properties
coursecode=[string]
```

**Response:**
```json
{
    "courseCode": "string",
    "description": "string",
    "name": "string"
}
```
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

***

## Users 

***

* ID will be auto-assigned by the database.
* The password will be encrypted after posting it. No way to get the password back after encrypring.
* Authority should be either `"ROLE_USER"` or `"ROLE_ADMIN"`.
* Enabled has to be true. Otherwise he may have the role but still can't do anything other in the API than GET requests.
* Only people with role `"ADMIN"` are able to use these functions.

***

### GET `/api/users`

Get a list of all users

**Response:**

``` json
[
    {
        "id": 0,
        "user_name": "string",
        "password": "string",
        "authority": "string",
        "enabled": true
    },
    {
        "id": 0,
        "user_name": "string",
        "password": "string",
        "authority": "string",
        "enabled": true
    }
]
```

**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

***

### POST `/api/users`

Create a new user

**Requested body:**
```json
{
    "user_name": "string",
    "password": "string",
    "authority": "string",
    "enabled": true
}
```

**Response:**
```json
{
    "id": 0,
    "user_name": "string",
    "password": "string",
    "authority": "string",
    "enabled": true
}
```
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

***

### PUT `/api/users/{id}`

Update a certain user with new credentials.

**Requested pathvariable:**
```properties
id=[integer]
```

**Requested body:**
```json
{
    "id": 0,
    "user_name": "string",
    "password": "string",
    "authority": "string",
    "enabled": true
}
```

**Response:**
```json
{
    "id": 0,
    "user_name": "string",
    "password": "string",
    "authority": "string",
    "enabled": true
}
```
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)

***

### DELETE `/api/users/{id}`

Delete a certain user.

**Requested pathvariable:**
```properties
id=[integer]
```

**Response:**
```json
{
    "id": 0,
    "user_name": "string",
    "password": "string",
    "authority": "string",
    "enabled": true
}
```
**HTTP-statuses:** 400, 401, 200

[Back to navigation](#navigation)
