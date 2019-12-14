# Project C - Routeplanner Elective Course
A website routeplanner for elective course from start point to destination building and indoor gps in the form of a 3D model to show you the way to your destination.

## Front-end by
* [M. Booman](https://github.com/Matthbo)
* [P. Bronsveld](https://github.com/Pedro-Bronsveld)

## Back-end by
* [B. Gussekloo](https://github.com/Bramgus12)
* [G.H. Wu](https://github.com/guanhaowu)

# Installation instructions in IDE

## Back end:
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
9. Run the file `ProjectsApplication.java` which is located in the `~/ProjectC/Back-end/src/main/java/com/bramgussekloo/projects` folder. 
10. You're good to go.

## Front end:
1. Download the newest version of Node.js.
2. Download an IDE or text editor that feeds your needs. (We recommend either Sublime text, Visual Studio code or Jetbrains Webstorm).
3. Run the commands:
``` shell
npm install -g @angular/cli
npm install
```
4. If you are using Sublime or VSCode run the command below. if you are using Jetbrains webstorm, just press the play button in the IDE.
``` shell
ng serve
```
