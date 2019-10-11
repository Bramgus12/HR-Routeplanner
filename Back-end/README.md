#API Documentation
##GET /api/address
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

##GET /api/address/{id}
Gives you a list with the list you want in the format of:

    {
        "id": [integer],
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
If the values are all null that means that the document does not exist at the asked id number.

##POST /api/address/{id}
To post something you have to use a document in the form of this:

    {
        "id": [integer],
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
HTTP-statuses: 400, 204

##DELETE /api/address/{id}
To delete something you go to the link with the right id.

HTTP-statuses: 400, 204

##PUT /api/address/{id}
To put something you have to send the whole document, not only the value that has to be changed, 
also the id has to be in the link and in the document:

    {
        "id": [integer],
        "street": [string],
        "number": [integer],
        "city": [string],
        "postal": [string]
    }
    
HTTP-Statuses: 400, 204