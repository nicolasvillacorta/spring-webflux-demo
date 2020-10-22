package com.accenture.webfluxdemo.playground.operations;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class CombineTest {

	@Test
	public void combineUsingMerge() {
		
		// Imaginemos 2 respuestas de servicios o bases de datos y queremos combinarlas...
		Flux<String> flux1 = Flux.just("A", "B", "C");
		Flux<String> flux2 = Flux.just("D", "E", "F");
		
		Flux<String> mergedFlux = Flux.merge(flux1, flux2);
		
		StepVerifier.create(mergedFlux.log())
			.expectSubscription() // Se espera una subscripcion.
			.expectNext("A", "B", "C", "D", "E", "F")
			.verifyComplete();

	}
	
	@Test
	public void combineUsingMergeWithDelay() {
		
		Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));
		
		// Cuando se hace merge, los elementos no van a salir en el orden esperado, porque van a correr los flujos en threads paralelos.
		// para evitar eso esta el concat en lugar del merge.
		Flux<String> mergedFlux = Flux.merge(flux1, flux2);
		
		StepVerifier.create(mergedFlux.log())
			.expectSubscription()
			.expectNextCount(6)
			.verifyComplete();

	}
	
	@Test
	public void combineUsingConcat() {
		
		Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D", "E", "F");
		
		// La subscripcion al flux2 no se hace hasta que el flux1 esta completo con el concat.
		Flux<String> mergedFlux = Flux.concat(flux1, flux2);
		
		StepVerifier.create(mergedFlux.log())
			.expectSubscription()
			.expectNext("A", "B", "C", "D", "E", "F")
			.verifyComplete();

	}
	
	@Test
	public void combineUsingConcatWithDelay() {
		
		VirtualTimeScheduler.getOrSet();
		
		Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));
		
		Flux<String> mergedFlux = Flux.concat(flux1, flux2);
		
		StepVerifier.withVirtualTime(() -> mergedFlux.log())
			.expectSubscription()
			.thenAwait(Duration.ofSeconds(6)) // Sin el .thenAwait NO FUNCIONA EL VIRTUALTIME
			.expectNextCount(6)
			.verifyComplete();
		
		// Comente este StepVerifier porque no usa el virtual time.
//		StepVerifier.create(mergedFlux.log())
//			.expectSubscription()
//			.expectNext("A", "B", "C", "D", "E", "F")
//			.verifyComplete();

	}
	
	@Test
	public void combineUsingZip() {
		
		Flux<String> flux1 = Flux.just("A", "B", "C");
		Flux<String> flux2 = Flux.just("D", "E", "F");
		
		// La subscripcion al flux2 no se hace hasta que el flux1 esta completo con el concat.
		Flux<String> mergedFlux = Flux.zip(flux1, flux2, (t1, t2)->{
			return t1.concat(t2);
		}); // A,D : B,E : C,F t1 y t2 representa cada elemento de cada flux.
		
		StepVerifier.create(mergedFlux.log())
			.expectSubscription()
			.expectNext("AD", "BE", "CF")
			.verifyComplete();

	}
	
}
