# Proiect IoT - Purificator Smart

Acest proiect este realizat in cadrul cursului de Inginerie Software, impreuna cu [Alina Dorneanu](https://github.com/alina0311) , [Gabriel Badescu](https://github.com/BadescuGabi) , [Radu Negulescu](https://github.com/radunegulescu) si [Irina Pavalasc](https://github.com/IrinaPavalasc).

In cadrul lui vom dezvolta partea de back-end a unui purificator smart ce are urmatoarele functionalitati:

- detectarea nivelului de CO/SO2/NO2/O3 
- afișarea unor notificări
- crearea unei atmosfere plăcute prin jocuri de lumini
- pornirea automată pe baza unui program stabilit
- crearea unor moduri speciale
- pornirea unor alarme în cazul depășirii anumitor parametri

Implementarea proiectului se va realiza in Java.

Link catre documentul de analiza: https://docs.google.com/document/d/1zegxxW3Ma2Hy8idONQcIOMgB3yz-dOM4IdGJO5W8LvM/edit#

## Documentare
Aplicatia expune un Rest **API HTTP** – documentat folosind Open API ([Swagger](http://localhost:8080/swagger-ui.html#/demo-application)) si un **API MQTT** – documentat folosind  [AsyncAPI](asyncapi.yaml)

## Instalare
Ca sistem de build e folosit **Maven**. 
Pentru configurația MQTT:
- instalarea _mosquitto_ (broker MQTT). Pentru download [apasa aici](https://mosquitto.org/download/)
- în folder-ul în care s-a descărcat mosquitto se va crea un fișier _broker.conf_ cu următorul conținut:
_listener 1883
allow_anonymous true_
- într-un CMD cu rol de administrator se va introduce următoarea comandă: _mosquitto -v -c broker.conf_
Pentru verificarea configurației, într-un CMD se poate introduce următoarea comandă: _mosquitto_pub -t mytopic -m "Hello world"_ și mai apoi, se introduce următoarea comandă în alt CMD: _mosquitto_sub -t mytopic_. Dacă în fereastra de subscribe se afisează "Hello world", configurația este corectă și conexiunea a fost realizată de către broker. 


## Utilizare
Trebuie deschis urmatorul link: [Swagger](http://localhost:8080/swagger-ui.html#/demo-application) și să testăm request-urile.
Înainte de testarea request-urilor va apărea un pop-ul în care utilizatorul va trebui să introducă un username și o parolă.
Dacă purificatorul utilizatorului nu este pornit, alte request-uri nu vor funcționa, așa că va trebui trimis request-ul _/pickOnSchedule_.

## Testare
