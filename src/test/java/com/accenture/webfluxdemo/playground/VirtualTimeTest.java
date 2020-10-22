package com.accenture.webfluxdemo.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class VirtualTimeTest {

	@Test
	public void testingWithoutVirtualTime() {
		
		Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
				.take(3);
		
		StepVerifier.create(longFlux.log())
			.expectSubscription()
			.expectNext(0l, 1l, 2l)
			.verifyComplete();
	}
	
	@Test
	public void testingWithVirtualTime() {
		
		// El VirtualTime de Reactor se usa para no perder mucho tiempo en los tests. o va a usar el reloj de la maquina, sino uno virtual.
		VirtualTimeScheduler.getOrSet(); 
		
		Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
				.take(3);
		
		StepVerifier.withVirtualTime(() -> longFlux.log()) // Recibe un valor como supplier. (FuncInt no usa argumentos y devuelve un valor)
				.expectSubscription()
				.thenAwait(Duration.ofSeconds(3)) // No va a esperar los 3 segundos porque virtualizamos el tiempo. Sin el .thenAwait, no funciona el virtual timee.
				.expectNext(0l, 1l, 2l)
				.verifyComplete();
	}
}
