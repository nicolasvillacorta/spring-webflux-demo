package com.accenture.webfluxdemo.playground.flux;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTests {
	
	@Test
	public void fluxTest() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("Error message")))
				.concatWith(Flux.just("After error")) // Esto no se logea, ya que cuando sucede el evento onError, se detiene el flujo.
				.log(); // Este flujo va a logear cada evento/mensaje anterior a este metodo.
		
		
		// Sin un subscriber no podemos acceder a los datos del Flux. El subscriber va iniciar el flujo de datos.
		stringFlux
			.subscribe(System.out::println, // Aca empieza a emitir los values al subscriber. Como es un flujo, salen de a uno.
					(e) -> System.err.println("Exception: " + e), // Segundo parametro del subscribe; ¿Que pasa en caso de un  error?
					() -> System.out.println("Se completo el flujo.")); // Tercer parametro, ¿Que pasa al completar el flujo?
//		
//		
//		// Reactor provee formas de testear los flux, son muy distintos a Java normal.
		
	}
	
	@Test
	public void fluxTestElements_WithoutError() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.log();
		
		
		// Funciona como assert del flujo.
		StepVerifier.create(stringFlux)
			.expectNext("Spring")
			.expectNext("Spring Boot")
			.expectNext("Reactive Spring")
				.verifyComplete(); // Siempre hay que tener el verifyComplete, porque es el que va a iniciar el flujo, como el subscribe.
		
	}
	
	@Test
	public void fluxTestElements_WithError() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("Exception ocurred")))
				.log();
		
		StepVerifier.create(stringFlux)
			.expectNext("Spring")
			.expectNext("Spring Boot")
			.expectNext("Reactive Spring")
			//.expectError(RuntimeException.class) -> No se puede tener el expectError y tambien el expectErrorMessage.
			.expectErrorMessage("Exception ocurred")
				.verify(); 
		
	}
	
	@Test
	public void fluxTestElements_WithError1() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("Exception ocurred")))
				.log();
		
		StepVerifier.create(stringFlux)
			.expectNext("Spring", "Spring Boot", "Reactive Spring") // Esta es otra manera de assertiar varios onNext...
			.expectErrorMessage("Exception ocurred")
				.verify(); 
		
	}
	
	@Test
	public void fluxTestElementsCount_WithErro() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("Exception ocurred")))
				.log();
		
		StepVerifier.create(stringFlux)
			.expectNextCount(3) // Espera 3 onNext...
			.expectErrorMessage("Exception ocurred")
				.verify(); 
		
	}

}
