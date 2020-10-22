package com.accenture.webfluxdemo.controllermodel;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveController {

	// El browser actua como subscriber para nuestro flujo. La anotacion RestController por default devuelve un JSON. Por eso 
	// 	termina de procesar el flux y recien ahi se recibe la respuesta.
	@GetMapping("/flux")
	public Flux<Integer> returnFlux(){
		
		 return Flux.just(1, 2, 3, 4)
				 .delayElements(Duration.ofSeconds(1))
				 .log();
		 
	}
	
	// Aca mantiene la conexion abierta. Es un cold publisher, si lo accedo de mas de un cliente, reinicia siempre de 0
	@GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Long> returnFluxAsStream(){
		
		return Flux.interval(Duration.ofSeconds(1))
				.log();
	}
	
	// Cuando usamos Mono? Cuando queremos 1 solo valor de forma no bloqueante.
	@GetMapping("/mono")
	public Mono<Integer> returnMono(){
		
		return Mono.just(1)
				.log();
		
	}
	
	
	
}
