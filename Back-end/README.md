# API Documentation

Navigation:
1. [Install instructions](#Install)
2. [Address](#Address)
    1. [Get all addresses](#GET-/api/address)
    2. [Get a certain address](#GET-/api/address/{id})
    3. [Post](#POST-/api/address)
    4. [Delete](#DELETE-/api/address/{id})
    5. [Update](#PUT-/api/address/{id})
    


## Install
To run this api on your own computer, follow these steps:
1. You have to download Intellij IDEA community or ultimate (Eclipse will probably also work).
2. Clone the GitHub Repo to a place where you can find it easily.
3. Open the Back-end folder in IDEA.
4. Choose SDK version 11.0.4 when it is being asked.
5. Create a file that is called: `Database_config.properties` that has the layout 
like this below and put it in the folder `~/ProjectC/Back-end/src/main/resources`


    db_url=jdbc:postgresql://[host]/[Database name]:[database port]
    db_username=[username]
    db_password=[password]

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
    
You will get a object back in the form of this:

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

HTTP-statuses: 400, 204

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
