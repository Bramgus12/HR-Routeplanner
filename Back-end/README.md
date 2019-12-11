# API Documentation

**Swagger-ui on link `[host]:[port]/swagger-ui.html` when server is running. This contains a more detailed and interactive API-Documentation**

**When running on your own pc in the IDE, that would be `localhost:8080/swagger-ui.html`**

## Navigation:
1. [Install IDE](#install-in-development-environment)
2. [Install production](#install-in-production)
3. [Address](#address)
    1. [Get all addresses](#get-apiaddress)
    2. [Get a certain address](#get-apiaddressid)
    3. [Get an address by buildingname](#get-apiaddressbuilding)
    4. [Get an address by roomcode](#get-apiaddressroom)
    5. [Post](#post-apiaddress)
    6. [Delete](#delete-apiaddressid)
    7. [Update](#put-apiaddressid)
4. [Building](#building)
    1. [Get all buildings](#get-apibuilding)
    2. [Get a certain building](#get-apibuildingid)
    3. [Post](#post-apibuilding)
    4. [Delete](#delete-apibuildingid)
    5. [Update](#put-apibuildingid)
5. [Institute](#institute)
    1. [Get all institutes](#get-apiinstitute)
    2. [Get a certain institute](#get-apiinstituteid)
    3. [Post](#post-apiinstitute)
    4. [Delete](#delete-apiinstituteid)
    5. [Update](#put-apiinstituteid)
6. [BuildingInstitute](#buildinginstitute)
    1. [Get all buildingInstitutes](#get-apibuildinginstitute)
    2. [Get a certain buildingInstitute](#get-apibuildinginstituteid)
    3. [Post](#post-apibuildinginstitute)
    4. [Put](#put-apibuildinginstituteid)
    5. [Delete](#delete-apibuildinginstituteid)
7. [LocationNodeNetwork](#locationnodenetwork)
    1. [Get a certain locationnodenetwork](#get-apilocationnodenetworklocationname)
    2. [Post](#post-apilocationnodenetworkaddressid)
    3. [Delete](#delete-apilocationnodenetworklocationname)
    4. [Put](#put-apilocationnodenetworklocationname)
    5. [Get all nodes by type](#get-apilocationnodenetwork)
    6. [Get all nodes that are a room](#get-apilocationnodenetworkroom)
8. [Route-engine](#route-engine)
    1. [Get the route between two nodes.](#get-apiroutes)
9. [ElectionCourse](#election-course)
    1. [Get a list of all election courses](#get-apielection-course)
    2. [Get a description of a specific election course](#get-apielection-coursecoursecode)
    3. [Get all descriptions of election courses](#get-apielection-coursedescription)
    4. [Upload election course file](#post-apielection-courseupload)
    5. [Create a description for an election course](#post-apielection-course)
    6. [Update the election course excel file](#put-apielection-course)
    7. [update the description of an election course](#put-apielection-coursecoursecode)
    8. [Delete the description of an election course](#delete-apielection-coursecoursecode)


*****

## Install in Development environment
To run this api on your own computer, follow these steps:
1. You have to download Intellij IDEA community or ultimate (Eclipse will probably also work).
2. Clone the GitHub Repo to a place where you can find it easily.
3. Open the Back-end folder in IDEA.
4. Choose SDK version 11.0.4 or 11.0.5 when it is being asked.
5. For the 3 points below we also made example files. These files are in the resources folder on github
6. Create a file that is called: `Database_config.properties` that has the layout 
like this below and put it in the folder `~/ProjectC/Back-end/src/main/resources`

``` properties
# the url to the database server which your server has to connect to
db_url=jdbc:postgresql://[host]/[Database name]:[database port]

# The username you have to use to login to the database
db_username=[username]

# The password you have to use to login to the database
db_password=[password]
```
    
7. Create a file that is called: `file_path.properties` that has the layout like this below and put it in the folder `~/ProjectC/Back-end/src/main/resources`

    The folder needs to have two subfolders that are called ElectionCourse and Locations

``` properties
# The path to your resources folder
file_path=src/main/resources/
```
8. Create a file that is called: `application.properties` that has the layout like one of the types below and put it in the folder `~/ProjectC/Back-end/src/main/resources`
``` properties
# The port the server runs on
server.port=8080

# Enable response compression
server.compression.enabled=true

# The comma-separated list of mime types that should be compressed
server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json

# Compress the response only if the response size is at least 1KB
server.compression.min-response-size=1024

# Disable Stack Trace error
server.error.include-stacktrace=never
```
9. Build the front-end en put it in the `~/ProjectC/Back-end/src/main/resources/static` folder of the project. (Project will run without the front-end)
10. Run the file `ProjectsApplication.java` which is located in the `~/ProjectC/Back-end/src/main/java/com/bramgussekloo/projects` folder. 
11. You're good to go.

[Back to navigation](#navigation)
****

## Install in production
To run this api on your production Ubuntu server, follow these steps:
1. Make sure java is installed by running the `sudo apt install default-jdk`
2. Clone the GitHub Repo to a place where you can find it easily.
3. For the 3 points below we also made example files. These files are in the resources folder
4. Create a file that is called: `Database_config.properties` that has the layout 
like this below and put it in the folder `~/ProjectC/Back-end/src/main/resources`

``` properties
# the url to the database server which your server has to connect to
db_url=jdbc:postgresql://[host]/[Database name]:[database port]

# The username you have to use to login to the database
db_username=[username]

# The password you have to use to login to the database
db_password=[password]
```
    
5. Create a file that is called: `file_path.properties` that has the layout like this below (if you want to use that folder as your resources folder. Otherwise specify the right folder) and put it in the folder `~/ProjectC/Back-end/src/main/resources`

    The folder needs to have two subfolders that are called ElectionCourse and Locations

``` properties
# The path to your resources folder
file_path=src/main/resources/
```
6. Create a file that is called: `application.properties` that has the layout like one of the types below and put it in the folder `~/ProjectC/Back-end/src/main/resources`  Make sure you have a keystore in the same folder. (You can make a keystore by using an ssl certificate from let's encrypt)

``` properties
# the port the server runs on.
server.port=443

# Enable response compression
server.compression.enabled=true

# The comma-separated list of mime types that should be compressed
server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application>

# Compress the response only if the response is at least 1KB
server.compression.min-response-size=1024

# Disable stack trace in the api response
server.error.include-stacktrace=never

# Enable ssl
server.ssl.enabled=true

# Tell the server where to look for the keystore (SSL certificate)
server.ssl.key-store=classpath:[name of the keystore file]

# Type of the keystore
server.ssl.key-store-type=PKCS12

# The password of the keystore
server.ssl.key-store-password=[password]

# Alias provided in the keystore
server.ssl.key-alias=[alias]

# Provider of the keystore
server.ssl.key-store-provider=SUN
```
7. Put the file `HttpsRedirectConf.java` in the folder `~/ProjectC/Back-end/src/main/java/com/bramgussekloo/projects` with the following code in it:
``` java
package com.bramgussekloo.projects;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsRedirectConf {
    private final static String SECURITY_USER_CONSTRAINT = "CONFIDENTIAL";
    private final static String REDIRECT_PATTERN = "/*";
    private final static String CONNECTOR_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
    private final static String CONNECTOR_SCHEME = "http";


    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat =
                new TomcatServletWebServerFactory() {

                    @Override
                        protected void postProcessContext(Context context) {
                        SecurityConstraint securityConstraint = new SecurityConstraint();
                        securityConstraint.setUserConstraint(SECURITY_USER_CONSTRAINT);
                        SecurityCollection collection = new SecurityCollection();
                        collection.addPattern(REDIRECT_PATTERN);
                        securityConstraint.addCollection(collection);
                        context.addConstraint(securityConstraint);
                    }
                };
        tomcat.addAdditionalTomcatConnectors(createHttpConnector());
        return tomcat;
    }

    private Connector createHttpConnector() {
        Connector connector =
                new Connector(CONNECTOR_PROTOCOL);
        connector.setScheme(CONNECTOR_SCHEME);
        connector.setSecure(false);
        connector.setPort(80);
        connector.setRedirectPort(443);
        return connector;
    }
}
```
8. Build the front-end en put it in the `~/ProjectC/Back-end/src/main/resources/static` folder of the project. (Project will run without the front-end)
9. Build the back-end to a jar by running `./gradlew build jar`.
10. If you want to run the jar on 443 then you have to do some adjustments to the ubuntu server. You have to run some commands in the terminal. This makes sure that you can run on port 443 and 80 when you are non-root:
``` shell
sudo apt install authbind
sudo touch /etc/authbind/byport/80
sudo touch /etc/authbind/byport443
sudo chmod 777 /etc/authbind/byport/80
sudo chmod 777 /etc/authbind/byport/443
```
11. Now if you wanna run your jar, run the command:
``` shell
authbind --deep java -jar [your jar name].jar
```
11. You're good to go.

[Back to navigation](#navigation)
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
        "postal": "string"
    },
    {
        "id": 0,
        "street": "string",
        "number": 0,
        "city": "string",
        "postal": "string"
        
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
    "postal": "string"
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
    "postal": "string"
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
    "postal": "string"
}
```
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### POST `/api/address`
Create a new address.
 
**Requested body:**

```json
{
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string"
}
```
    
**Response:**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string"
}
```
    
**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### DELETE `/api/address/{id}`
Delete an address by id.

**Response:**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string"
}
```

**HTTP-statuses:** 400, 200

[Back to navigation](#navigation)

****

### PUT `/api/address/{id}`
Update a certain address.

**Requested body:**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string"
}
```
 

**Response:**

```json
{
    "id": 0,
    "street": "string", 
    "number": 0,
    "city": "string",
    "postal": "string"
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
Deletes the locationNodeNetwork indicated by the locationName in the URL.

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
    
### PUT `/api/locationnodenetwork/{locationName}`
Update a certain locationNodeNetwork. It deletes the old one and replaces it with this one.

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

