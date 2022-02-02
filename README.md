# Proiect IoT - Purificator Smart

Acest proiect este realizat in cadrul cursului de Inginerie Software, impreuna cu [Alina Dorneanu](https://github.com/alina0311) , [Gabriel Badescu](https://github.com/BadescuGabi) , [Radu Negulescu](https://github.com/radunegulescu) si [Irina Pavalasc](https://github.com/IrinaPavalasc).

In cadrul lui vom dezvolta partea de back-end a unui purificator smart ce are urmatoarele functionalitati:

- detectarea nivelului de praf și înștiințarea utilizatorului 
- identificarea nivelului de umiditate și stabilizarea acestuia între 40-60%
- detectarea nivelului de CO2 și afișarea de notificări în situațiile în care nivelul depășește 1000 PPM
- alertarea în situații deosebite precum acumularea de fum sau scurgeri de gaz
- emanarea de uleiuri esențiale pentru relaxare
- crearea unei atmosfere plăcute prin jocuri de lumini
- pornirea automată pe baza unui program stabilit

Implementarea proiectului se va realiza in Java.

Link catre documentul de analiza: https://docs.google.com/document/d/1zegxxW3Ma2Hy8idONQcIOMgB3yz-dOM4IdGJO5W8LvM/edit#

## Documentare
Aplicatia expune un Rest **API HTTP** – documentat folosind Open API ([Swagger](http://localhost:8080/swagger-ui.html#/demo-application)) si un **API MQTT** – documentat folosind  ![AsyncAPI](asyncapi.yaml)

## Instalare
Ca sistem de build e folosit **Maven**

## Utilizare
Trebuie deschis urmatorul link: [Swagger](http://localhost:8080/swagger-ui.html#/demo-application)

## Testare
