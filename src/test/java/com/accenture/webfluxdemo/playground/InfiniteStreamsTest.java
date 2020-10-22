package com.accenture.webfluxdemo.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class InfiniteStreamsTest {

	@Test
	public void infiniteSequenceTroll() throws InterruptedException {
		
		Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100))
				.log(); // Inicia desde 0 --> ....
		
		// Por la manera asincrona de trabajar del subscribe (con .interval), el bloque se va a terminar de ejecutar  aunque nos hayamos subscrito a un flujo
		// 	infinito. El test corre en el thread main, pero el flujo infinito va a estar en un parallel.
		infiniteFlux
			.subscribe((element) -> System.out.println("Value is: " + element));
		
		Thread.sleep(3000);
		
	}
	
	@Test
	public void infiniteSequenceTest() {
		
		Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(100))
				.take(3) // Toma solo los primeros N elementos del flujo.
				.log(); 
		
		StepVerifier.create(finiteFlux)
				.expectSubscription()
				.expectNext(0L, 1L, 2L)
				.verifyComplete();
		
	}
	
	@Test
	public void infiniteSequenceMap() {
		
		Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
				.map(l -> new Integer(l.intValue())) // El map convierte los long en int.
				.take(3)
				.log(); 
		
		StepVerifier.create(finiteFlux)
				.expectSubscription()
				.expectNext(0, 1, 2)
				.verifyComplete();
		
	}
	
	
	@Test
	public void infiniteSequenceMapWithDelay() {
		
		Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
				.delayElements(Duration.ofSeconds(1))
				.map(l -> new Integer(l.intValue())) 
				.take(3)
				.log(); 
		
		StepVerifier.create(finiteFlux)
				.expectSubscription()
				.expectNext(0, 1, 2)
				.verifyComplete();
		
	}
	
}
