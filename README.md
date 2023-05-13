# SmartMat

##

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

