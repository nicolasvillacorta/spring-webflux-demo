package com.accenture.webfluxdemo.playground;

import org.junit.jupiter.api.Test;

import com.accenture.webfluxdemo.CustomException;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ErrorTest {

	@Test
	public void fluxErrorHandlingOnErrorResume() {
		
		Flux<String> stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(new RuntimeException("Error!")))
				.concatWith(Flux.just("D"))
				.onErrorResume((e) -> { 
					System.out.println("Exception is: " + e);
					return Flux.just("default", "default1");
				}); // Al haber un error se ejecuta esta funcion.
		
		StepVerifier.create(stringFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C")
				// .expectError(RuntimeException.class) // Esta linea rompe el test, porque el error se controla.
				.expectNext("default", "default1")
				.verifyComplete();
		
	}
	
	@Test
	public void fluxErrorHandlingOnErrorReturn() {
		
		Flux<String> stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(new RuntimeException("Error!")))
				.concatWith(Flux.just("D"))
				.onErrorReturn("default");
				
		StepVerifier.create(stringFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C")
				.expectNext("default")
				.verifyComplete();
		
	}
	
	@Test
	public void fluxErrorHandlingOnErrorMap() {
		
		Flux<String> stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(new RuntimeException("Error!")))
				.concatWith(Flux.just("D"))
				.onErrorMap((e) -> new CustomException(e)); // Transforma la excepcion en otra cosa.
				
		StepVerifier.create(stringFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C")
				.expectError(CustomException.class)
				.verify();
		
	}
	
	@Test
	public void fluxErrorHandlingOnErrorMapWithRetry() {

		// Supongamos que trae un dato mal de un servicio y  antes de pasar el error queremos hacer 
		// 	un retry (reintentar obtener el flujo).
		Flux<String> stringFlux = Flux.just("A", "B", "C")
				.concatWith(Flux.error(new RuntimeException("Error!")))
				.concatWith(Flux.just("D"))
				.onErrorMap((e) -> new CustomException(e))
				.retry(2); // Reintenta SOLO SI HUBO UN ERROR, SINO NO.
				
		StepVerifier.create(stringFlux.log())
				.expectSubscription()
				.expectNext("A", "B", "C")
				.expectNext("A", "B", "C")
				.expectNext("A", "B", "C")
				.expectError(CustomException.class)
				.verify();
		
	}
	
	
}
