
# Spring WebFlux
Spring WebFlux proporciona a las aplicaciones Spring un stack web reactivo, completamente no bloqueante con soporte para back pressure. 

## Modelos de programación web.
WebFlux ofrece dos modelos distintos de programación web; 

* **Controllers con annotations:** Igual al modelo web de Spring MVC, Controllers con annotations; @Controller, @RestController, @GetMapping, @PostMapping, etc...
* **Endpoints funcionales;** Basados en dos interfaces. **RouterFunction** se utiliza para armar las rutas/mapping de los endpoints y delegar las solicitudes a un handler.| **HandlerFunction**, utiliza ServerRequest y ServerResponse para procesar las peticiones http.

## Tips para tests.

Los controladores convencionales se testean con la anotacion @WebFluxTest.
Los HandlerFunctions se testean con las anotaciones @SpringBootTest y @AutoconfigureWebTestClient (@WebFluxTest no sirve).

En ambos casos se inyecta un WebTestClient con @Autowired, que las anotaciones de clase generaron en contexto. El mismo se va a usar para testear los endpoints.

Algunos tests estan excluidos en el pom.xml para poder hacer el build, rompen, son solo para "jugar" con las librerias.

# Links utiles
 
[Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web-reactive.html)

[Project Reactor](https://projectreactor.io/)

[Project Reactor's Gitter](https://gitter.im/reactor/reactor)
