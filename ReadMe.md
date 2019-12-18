# Project C - Routeplanner Elective Course
A website routeplanner for elective course from start point to destination building and indoor gps in the form of a 3D model to show you the way to your destination.

## Front-end by
* [M. Booman](https://github.com/Matthbo)
* [P. Bronsveld](https://github.com/Pedro-Bronsveld)

## Back-end by
* [B. Gussekloo](https://github.com/Bramgus12)
* [G.H. Wu](https://github.com/guanhaowu)

# Navigation
1. [Installation instructions in IDE](#installation-instructions-in-ide)
    1. [Back-end](#back-end)
    2. [Front-end](#front-end)
2. [Installation instructions in Production](#Installation-instructions-in-Production)

# Installation instructions in IDE

## Back-end:
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
10. You're good to go. The back-end will run at `localhost:8080` and if you want to go to the interactive documentation you will have to go to `localhost:8080/swagger-ui.html`

****

## Front-end:
1. Download the newest version of Node.js.
2. Download an IDE or text editor that feeds your needs. (We recommend either Sublime text, Visual Studio code or Jetbrains Webstorm).
3. Create a file named `api_keys.ts` with the format like below and put it in the folder `~/ProjectC/Front-end/src/app/3rdparty`.
``` ts
export const keys = {
  openrouteservice: '[API key]',
  google_maps: '[API key]'
}
```
4. Run the commands:
``` bash
npm install -g @angular/cli
npm install
```
5. If you are using Sublime or VSCode run the command below. if you are using Jetbrains webstorm, just press the play button in the IDE.
``` bash
ng serve
# If that doesn't work because it can't find ng, try running:
npm run ng serve
```
6. You have to run the back end with the front end if you want to have the api to work in your front-end project.
7. The Front-end will run at `localhost:4200`.

# Installation instructions in Production

To run this api on your production Ubuntu server, follow these steps:
1. Make sure java and npm are installed by running the following commands:
``` bash
sudo apt install default-jdk
sudo apt install npm
```
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
6. Create a file that is called: `application.properties` that has the layout like one of the types below and put it in the folder `~/ProjectC/Back-end/src/main/resources`  
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

7. Get an ssl-certificate:
    1. Clone the git repository of certbot to the server and cd into the folder by running:
    ``` bash
    git clone https://github.com/certbot/certbot 
    cd certbot
    ```
    2. Create a key for your domain:
    ``` bash
    ./certbot-auto certonly -a standalone -d [domain] -d [domain]
    ```
    3. Use the following commands to make sure the key is ready for a Spring boot application.
    ``` bash
    sudo su
    cd /etc/letsencrypt/live/[domain name]
    openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name tomcat -CAfile chain.pem -caname root
    exit
    ```
    4. Now put the keystore in the resources folder of your project:
    ``` bash
    sudo cp /etc/letsencrypt/live/[domain name]/keystore.p12 /[specify path]/ProjectC/Back-end/src/main/resources/keystore.p12
    ```
    5. Your ssl certificate is good to go.


8. Put the file `HttpsRedirectConf.java` in the folder `~/ProjectC/Back-end/src/main/java/com/bramgussekloo/projects` with the following code in it:
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
9. Build the front-end:
    1. Cd into the Front-end folder.
    2. Install the angular cli:
    ``` bash
    npm install -g @angular/cli
    ```
    3. Install the project:
    ``` bash
    npm install
    ```
    4. build the project:
    ``` bash
    ng build --prod
    # if that doesn't work try installing the angular cli again or build the
    # front end by using the following command (note that service worker doesn't
    # work with this command):
    npm run ng build
    ```
    5. Copy the files of the just built front-end from `~/ProjectC/Front-end/dist/[folder name of the built front end]` to `~/ProjectC/Back-end/src/main/resources/static`
10. Build the back-end to a jar by running `./gradlew build jar`.
11. If you want to run the jar on 443 then you have to do some adjustments to the ubuntu server. You have to run some commands in the terminal. This makes sure that you can run on port 443 and 80 when you are non-root:
``` bash
sudo apt install authbind
sudo touch /etc/authbind/byport/80
sudo touch /etc/authbind/byport443
sudo chmod 777 /etc/authbind/byport/80
sudo chmod 777 /etc/authbind/byport/443
```
12. Now we want to have the jar running as a service. So make a file in `/etc/systemd/system` that is called `[service name].service` and put the following in it:
``` properties
[Unit]
Description=[Description of the application]

[Service]
User=[username]
ExecStart=authbind --deep [path to java] -jar [path to jar]
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```
13. Run the service by first reloading the systemctl daemon and then starting the service:
``` bash
sudo systemctl daemon-reload
sudo systemctl start [service name].service
sudo systemctl enable [service name].service
```

14. You're good to go.