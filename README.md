# Spring WebFlux
Spring WebFlux proporciona a las aplicaciones Spring un stack web reactivo, completamente no bloqueante con soporte para back pressure. 

## Modelos de programación web.
WebFlux ofrece dos modelos distintos de programación web; 

* **Controllers con annotations:** Igual al modelo web de Spring MVC, Controllers con annotations; @Controller, @RestController, @GetMapping, @PostMapping, etc...
* **Endpoints funcionales;** Basados en dos interfaces. **RouterFunction** se utiliza para armar las rutas/mapping de los endpoints y delegar las solicitudes a un handler.| **HandlerFunction**, utiliza ServerRequest y ServerResponse para procesar las peticiones http.

## Hot Publishers Vs Cold Publishers.

Por defecto tanto Mono como Flux son cold publishers, un cold publisher es un publisher que generea datos nuevos cada vez que alguien se subscribe, mientras que el Hot Publisher genera data nueva a partir de donde no haya sido consumida. 

Ej: Un flujo de 10 numeros, siendo cold, el publisher recibira 2 request con un segundo de diferencia y para ambas emitira los valores del 1 al 10. Siendo Hot el publisher, comenzara a emitir al segundo publisher los datos de a partir de donde el primero haya llegado.

[Post Medium](https://medium.com/@kirupakaranh/project-reactor-simple-introduction-and-exhot-vs-cold-publishers-72391d66416e#:~:text=Hot%20and%20Cold%20publishers&text=This%20is%20because%20by%20default,new%20subscriber%20subscribes%20to%20them.&text=On%20the%20other%20hand%2C%20Hot,do%20not%20depend%20upon%20publishers.)

## Tips para tests.

Los controladores convencionales se testean con la anotacion @WebFluxTest.
Los HandlerFunctions se testean con las anotaciones @SpringBootTest y @AutoconfigureWebTestClient (@WebFluxTest no sirve).

En ambos casos se inyecta un WebTestClient con @Autowired, que las anotaciones de clase generaron en contexto. El mismo se va a usar para testear los endpoints.

Algunos tests estan excluidos en el pom.xml para poder hacer el build, rompen, son solo para "jugar" con las librerias.

**Virtual Time**

Reactor provee una clase llamada VirtualTime, para simular tiempos en test unitarios, por ejemplo si tenemos un flujo que tarda 20 minutos en emitirse completo, podemos hacer que en 10 segundos se simulen los 20 minutos.

# Links utiles
 
[Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web-reactive.html)

[Project Reactor](https://projectreactor.io/)

[Project Reactor's Gitter](https://gitter.im/reactor/reactor)

[Presentacion Tokyo Java Day 17/05/2017](https://www.slideshare.net/makingx/spring-framework-50-reactive-web-application-javadaytokyo)
