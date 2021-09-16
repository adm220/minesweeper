# minesweeper-API

That is a java springboot application, built to be the backend of a minesweeper game.  

## Architecture
1. Java + Springboot 2 + maven
2. Dockerhub as the registry to the game's image
3. Aws AppRunner to deploy the game

## Out of Scope
* Pipeline
* Infrastructure as Code
* High level database
* Multiples Profiles

## Running the application
```
JASYPT_DECRYPT_PASSWORD=deviget mvn spring-boot:run
```
### Debug mode
```
JASYPT_DECRYPT_PASSWORD=deviget mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
```
## Build a docker image
```
JASYPT_DECRYPT_PASSWORD=deviget mvn package dockerfile:build
```
---
## What was built

That is an API to play the minesweeper game. You can select a level(EASY, MEDIUM or HARD) to have different sizes of 
board and mines. You have to authenticate yourself in the API although. Your game will be saved in your username.

The player can stop playing the game and return any time, but the player can just have one active game.

When the game finishes, won or not, a new game need to be started.

An OpenAPI based documentation could be accessed by <https://xdnpsusfzc.us-east-1.awsapprunner.com/v2/api-docs> or, 
if you prefer, access <https://xdnpsusfzc.us-east-1.awsapprunner.com/swagger-ui.html> to see friendly interface of the documentation.
You can use the interface to test the API to, but an authentication is needed.

There is a **minesweeper_node_client.zip** file, that is a typescript transpiled client to access the API.

## Technical Decisions
The Springboot java application was chosen because this is a stack very used nowadays in APIs, specially java based API.
A simple spring security authentication was built and the api-key was encrypted by jasypt framework.

Just to facilitate the whole architecture, I decided to keep an in memory database to persist data.

For logging, a json encoder was used to create an indexed log, so we can use a stack to monitor the application like: elastic stack, loki + grafana, 
Cloudwatch(app runner use it by default). Indexed log help us to make specific research using query.


For documentation, a swagger/openapi java implementation was built, with the goal to create a useful interface to read and test the API.
So this implementation generates a UI to use the api and a yaml documentation's format.

To become easy to deploy, scale and mitigate risks, the implementation has an integration with a maven plugin to generate docker images.
My public repository in dockerhub was used to share the images I generated in development process.

Every challenge I get, even for interview purposes, I always try to learn something new. 
Of course this decision is technical based, it is not a "felt right". For this challenge I toke two new things to stay in touch:
* AWS App Runner Service
* Nodejs/Typescrit environment

App Runner because AWS is the bigger cloud company nowadays, and they released recently this service that brings features like Heroku.

Node/Typescript environment was chosen because I still intend to build a ReactJs(learning purposes) interface for this game, so I made some research to find Typescript as 
a good language to do that and this code be reused by React after.
 
### Another important data
#### Aplication domain
```
https://xdnpsusfzc.us-east-1.awsapprunner.com
```
#### Api-keys to be used
```
8946fbf3-7b07-4dbd-8cc6-4dd3455ae3b1 or 37bb4b30-4414-406d-badb-6400756a9529
```

#### Jasypt Secret Key
```
deviget
```
#### DockerHub image URL
```
https://hub.docker.com/r/diogomartinez/challenge-deviget/
```
#### Another projects I built

* <https://bitbucket.org/diogo_martinez/cadastro-clientes/> -- a Customer API built in java + terraform + aws ecs fargate
* <https://bitbucket.org/diogo_martinez/desafiom4u/> -- an API that consumes rest and soap client api in java springboot with a lot of tests(unit and integration tests)


