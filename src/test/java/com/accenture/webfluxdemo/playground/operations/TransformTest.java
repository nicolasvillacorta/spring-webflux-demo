package com.accenture.webfluxdemo.playground.operations;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class TransformTest {

	List<String> names = Arrays.asList("nicolas", "santiago", "shark", "boquitaelmasgrandepapa");
	
	@Test
	public void transformUsingMap() {
		
		Flux<String> namesFlux = Flux.fromIterable(names)
				.map(n ->  n.toUpperCase()) 
				.log();
		
		StepVerifier.create(namesFlux)
			.expectNext("NICOLAS", "SANTIAGO", "SHARK", "BOQUITAELMASGRANDEPAPA")
			.verifyComplete();
		
	}
	
	@Test
	public void transformUsingMap_Lenght() {
		
		Flux<Integer> lengthsFlux = Flux.fromIterable(names)
				.map(n ->  n.length()) 
				.log();
		
		StepVerifier.create(lengthsFlux)
			.expectNext(7, 8, 5, 22)
			.verifyComplete();
		
	}
	
	@Test
	public void transformUsingMap_Lenght_repeat() {
		
		Flux<Integer> lengthsFlux = Flux.fromIterable(names)
				.map(n ->  n.length())
				.repeat(1) // Repite el flujo.
				.log();
		
		StepVerifier.create(lengthsFlux)
			.expectNext(7, 8, 5, 22)
			.expectNext(7, 8, 5, 22)
			.verifyComplete();
		
	}
	
	@Test
	public void transformUsingMap_Filter() {
		
		Flux<String> lengthsFlux = Flux.fromIterable(names)
				.filter(n  -> n.length() > 15)
				.map(n ->  n.toUpperCase())
				.log();
		
		StepVerifier.create(lengthsFlux)
			.expectNext("BOQUITAELMASGRANDEPAPA")
			.verifyComplete();
		
	}
	
	@Test
	public void transformUsingFlatMap() {
		
		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) // A, B, C, D, E, F
			.flatMap(s -> {
				return Flux.fromIterable(convertToList(s)); // A -> List[A, newValue], B -> List[B, newValue]
			}) // DB or external service call that returns a flux. s -> Flux<String>. 
			.log(); 

		StepVerifier.create(stringFlux)
			.expectNextCount(12)
			.verifyComplete();
	}
	
	
	@Test // Asi no tarda 6 segundos, tarda menos.
	public void transformUsingFlatMap_usingparallel() {
		
		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) 
				.window(2) // Va a esperar 2 elementos antes de ir al proximo step.  (Flux<Flux<String>>) -> (A, B), (C, D), (E, F)
				.flatMap((s) -> 
					s.map(this::convertToList).subscribeOn(Schedulers.parallel())) // Flux<List<String>>
					.flatMap(s -> Flux.fromIterable(s)) // Flux<String>
				.log();
		
		StepVerifier.create(stringFlux)
			.expectNextCount(12)
			.verifyComplete();
	}
	
	@Test
	public void transformUsingFlatMap_usingparallel_maintainOrder() {
		
		// Es igual al anterior, pero con flatMapSequential, para que no pierdan el orden los valores del flujo original.
		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) 
				.window(2) // Va a esperar 2 elementos antes de ir al proximo step.  (Flux<Flux<String>>) -> (A, B), (C, D), (E, F)
				.flatMapSequential((s) -> 
					s.map(this::convertToList).subscribeOn(Schedulers.parallel())) // Flux<List<String>>
					.flatMap(s -> Flux.fromIterable(s)) // Flux<String>
				.log();
		
		StepVerifier.create(stringFlux)
			.expectNextCount(12)
			.verifyComplete();
	}
	
	
	
	
	
	
	private List<String> convertToList(String s) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Arrays.asList(s, "newValue");
	}
	
	
}
