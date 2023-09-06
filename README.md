# SmartMat
SmartMat was a 3+1 week project which made up the course Systemutvikling 2. We were provided with a Vision Document by our product owners describing the aims and requirements for the application. The goal was to develop a application that helps the customer manage his food inventory and provide ways to reduce food waste. Our team consisting of 7 people were to use Scrum agile project management to structure and manage our workflow throughout the project. We were graded based on the product, process and documentation. We split the workforce mostly based on feature to improve each members learning experience. I mostly worked on the things regarding group/family administration, the main menu, and the recipe part of the system. 

## Some screenshots from the application: 

![home](https://github.com/kristianvaula/SmartMat/blob/Main/screenshots/home.png)

![refrigerator](https://github.com/kristianvaula/SmartMat/blob/Main/screenshots/kjoleskab.png)

![phone](https://github.com/kristianvaula/SmartMat/blob/Main/screenshots/phone.png)

![settings](https://github.com/kristianvaula/SmartMat/blob/Main/screenshots/settings'.png)

![recipes](https://github.com/kristianvaula/SmartMat/blob/Main/screenshots/recipe_dark.png)

![weekmenu](https://github.com/kristianvaula/SmartMat/blob/Main/screenshots/week_dark.png)

# Frontend

### Forutsetninger
- Node.js
- NPM

### Installere avhengigheter
For å installere avhengighetene, naviger til frontend-mappen i prosjektet og kjør følgende kommando i terminalen:

```
npm i

```

### Development Server
For å starte utviklingsserveren, kjør følgende kommando i terminalen:
```
npm run dev

``` 
Dette vil starte utviklingsserveren på http://localhost:3000.


### Building 
For å bygge frontend-applikasjonen for produksjon, kjør følgende kommando i terminalen:
```
npm run build

``` 
Dette vil generere en dist-mappe med bygde statiske filer som kan distribueres til en webserver.




## Backend
### Forutsetninger
- JDK 18
- Maven

### Installere avhengigheter
For å installere avhengighetene, naviger til backend-mappen i prosjektet og kjør følgende kommando i terminalen:
```
mvn install
```

### Starte serveren
For å starte backend-serveren, kjør følgende kommando i terminalen:
```
mvn spring-boot:run
```

### Bygging
For å bygge backend-applikasjonen for produksjon, kjør følgende kommando i terminalen:
```
mvn package
```
Dette vil generere en kjørbar .jar-fil i target-mappen som kan distribueres til en server.

