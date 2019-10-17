# API Documentation

Navigation:
1. [Install instructions](#install)
2. [Address](#address)
    1. [Get all addresses](#get-apiaddress)
    2. [Get a certain address](#get-apiaddressid)
    3. [Post](#post-apiaddress)
    4. [Delete](#delete-apiaddressid)
    5. [Update](#put-apiaddressid)
3. [Building](#Building)
    1. [Get all buildings](#get-apibuilding)
    2. [Get a certain building](#get-apibuildingid)
    3. [Post](#post-apibuilding)
    4. [Delete](#delete-apibuildingid)
    5. [Update](#put-apibuildingid)
4. [LocationNodeNetwork](#locationnodenetwork)
    1. [Get a certain locationnodenetwork](#get-apilocationnodenetworklocationname)
    2. [Post](#post-apilocationnodenetwork)
    3. [Delete](#delete-apilocationnodenetworklocationname)
    4. [Put](#put-apilocationnodenetworklocationname)
5. [Route-engine](#route-engine)
    1. [Get the route between two nodes.](#get-apiroutes)


## Install
To run this api on your own computer, follow these steps:
1. You have to download Intellij IDEA community or ultimate (Eclipse will probably also work).
2. Clone the GitHub Repo to a place where you can find it easily.
3. Open the Back-end folder in IDEA.
4. Choose SDK version 11.0.4 when it is being asked.
5. Create a file that is called: `Database_config.properties` that has the layout 
like this below and put it in the folder `~/ProjectC/Back-end/src/main/resources`
```
db_url=jdbc:postgresql://[host]/[Database name]:[database port]
db_username=[username]
db_password=[password]
```    
    
6. Build the front-end en put it in the `~/ProjectC/Back-end/src/main/resources/static` folder of the project. (Project will run without the front-end)
7. Run the file `ProjectsApplication.java` which is located in the `~/ProjectC/Back-end/src/main/java/com/bramgussekloo/projects/ProjectsApplication.java` folder.
8. You're good to go.

## Address
### GET /api/address
Get a list of all addresses.

**Response:**

    [
        {
            "id": [integer],
            "street": [string],
            "number": [integer],
            "city": [string],
            "postal": [string]
        },
        {
            "id": [integer],
            "street": [string],
            "number": [integer],
            "city": [string],
            "postal": [string]
        }
    ]

    
**HTTP-statuses:** 400, 200

### GET /api/address/{id}
Get a certain address.

**Response:**

    {
        "id": [integer],
        "street": [string], 
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
**HTTP-statuses:** 400, 200

### POST /api/address
Create a new address.

**Requested body:**

    {
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
**Response:**

    {
        "id": [Integer],
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
    
**HTTP-statuses:** 400, 200

### DELETE /api/address/{id}
Delete an address by id.

**Response:**

    {
        "id": [Integer],
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }


**HTTP-statuses:** 400, 200

### PUT /api/address/{id}
Update a certain address.

**Requested body:**

    {
        "id": [integer],
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
**Response:**

    {
        "id" : [Integer]
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
**HTTP-statuses:** 400, 200

## Building
### GET /api/building
Gives you a list of all buildings.

**Response:**

    [
        {
            "id": [Integer],
            "address_id": [Integer],
            "name": [String]
        },
        {
            "id": [Integer],
            "address_id": [Integer],
            "name": [String]
        }
    ]
    
**HTTP-statuses:** 400, 200
    
### GET /api/building/{id}
Get a specific building.

**Response:**

    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
**HTTP-statuses:** 400, 200
    
### POST /api/building
Create a new building.

**Requested body:**

    {
        "address_id": [Integer],
        "name": [String]
    } 

**Response:**

    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
**HTTP-statuses:** 400, 200

### DELETE /api/building/{id}
Delete a building by id.

**Response:**

    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
HTTP-statuses: 400, 200

### PUT /api/building/{id}
Update a building.

**Requested body:**

    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
**Response**

    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
**HTTP-statuses:** 400, 200
    
## LocationNodeNetwork
**LocationNodeNetwork is not stored in an SQL-Database. It is stored inside of a JSON file.**
### GET /api/locationnodenetwork/{locationName}
Get a certain locationNodeNetwork object by locationName.

**Response:**

    {
        "locationName": [String],
        "nodes": [
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            },
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            }
        ],
        "connections": [
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            },
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            }
        ]
    }
    
**HTTP-statuses:** 400, 200
    
### POST /api/locationnodenetwork
Post a node into the server. You can only do this when it does not exist already on the server.

**Requested body:**

    {
        "locationName": [String],
        "nodes": [
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            },
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            }
        ],
        "connections": [
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            },
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            }
        ]
    }
    
**Response:**

    {
        "locationName": [String],
        "nodes": [
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            },
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            }
        ],
        "connections": [
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            },
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            }
        ]
    }

**HTTP-statuses:** 400, 200

### DELETE /api/locationnodenetwork/{locationname}
Deletes the locationNodeNetwork indicated by the locationName in the URL.

**Response:**


    {
        "locationName": [String],
        "nodes": [
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            },
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            }
        ],
        "connections": [
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            },
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            }
        ]
    }
    
**HTTP-statuses:** 400, 200
    
### PUT /api/locationnodenetwork/{locationName}
Update a certain locationNodeNetwork. It deletes the old one and replaces it with this one.

**Requested body:**

    {
        "locationName": [String],
        "nodes": [
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            },
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            }
        ],
        "connections": [
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            },
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            }
        ]
    }
    
**Response:**

    {
        "locationName": [String],
        "nodes": [
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            },
            {
                "number": [Integer],
                "type": [String],
                "code": [String],
                "label": [String],
                "x": [Double],
                "y": [Double],
                "z": [Double]
            }
        ],
        "connections": [
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            },
            {
                "node1": [Integer],
                "node2": [Integer],
                "distance": [Double]
            }
        ]
    }
    
**HTTP-statuses:** 400, 200

## Route-engine
### GET /api/routes
Get the route between two nodes.

**Requested parameters:**
* `"from": [Integer]`
* `"to": [Integer]`
* `"locationName": [String]`

**Response:**

    [
        {
            "number": [Integer],
            "type": [String],
            "code": [String],
            "label": [String],
            "x": [Double],
            "y": [Double],
            "z": [Double]
        },
        {
            "number": [Integer],
            "type": [String],
            "code": [String],
            "label": [String],
            "x": [Double],
            "y": [Double],
            "z": [Double]
        }
    ]

**HTTP-statuses:** 400, 200
