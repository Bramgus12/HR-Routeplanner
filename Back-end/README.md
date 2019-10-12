# API Documentation

Navigation:
1. [Install instructions](#Install)
2. [Address](#Address)
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
Gives you a list of all addresses in the format of:

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

    
HTTP-Statuses: 400, 200

### GET /api/address/{id}
Gives you a list with the list you want in the format of:

    {
        "id": [integer],
        "street": [string], 
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
HTTP-statuses: 400, 200

### POST /api/address
To post something you have to use a document in the form of this:

    {
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
You will get an object back in the form of this:

        {
            "id": [Integer],
            "street": [string],
            "number": [integer],
            "city": [string],
            "postal": [string]
        }
    
    
HTTP-statuses: 400, 200

### DELETE /api/address/{id}
To delete something you go to the link with the right id.

You will get the deleted object back in the form of this:

    {
        "id": [Integer],
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }


HTTP-statuses: 400, 200

### PUT /api/address/{id}
To put something you have to send the whole document, not only the value that has to be changed, 
also the id has to be in the link and in the document:

    {
        "id": [integer],
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
After you send this object, you will get an object back in the form of this:

    {
        "id" : [Integer]
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
HTTP-Statuses: 400, 200

## Building
### GET /api/building
Gives you a list of all buildings in the form of this:

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
    
HTTP-statuses: 400, 200
    
### GET /api/building/{id}
Gives you a specific building in the form of this:

    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
HTTP-statuses: 400, 200
    
### POST /api/building
Create a new building by sending a body with the link in the form of this:

    {
        "address_id": [Integer],
        "name": [String]
    } 

You get the created building immediately back with an id in the form of this:

    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
HTTP-statuses: 400, 200

### DELETE /api/building/{id}
Delete a building by id

You get the deleted bulding back in the form of this:


    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
HTTP-statuses: 400, 200

### PUT /api/building/{id}
Update a building by sending the whole object with the right id. The id in the body must correspond with the id in the url.

Send it in the form of this:

    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
    
You get the changes back in the form of this:


    {
        "id": [Integer],
        "address_id": [Integer],
        "name": [String]
    }
