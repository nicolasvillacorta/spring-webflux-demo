package com.accenture.webfluxdemo.playground;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackpressureTest {

	@Test
	public void backPressureTest() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10)
			.log();
		
		StepVerifier.create(finiteFlux)
			.expectSubscription()
			.thenRequest(1)
			.expectNext(1)
			.thenRequest(1)
			.expectNext(2)
			.thenCancel()
			.verify();
	}
	
	@Test
	public void backPressure() throws InterruptedException {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10)
				.log();

		finiteFlux.subscribe((element) -> System.out.println("Element is: " + element),
				(e) -> System.err.println("Exception is: " + e),
				() -> System.out.println("Done"),
				(subscription -> subscription.request(2))); // El 4rto parametro del subscribe es el que regula el backpressure.
	}
	
	@Test
	public void backPressure_cancel() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10)
				.log();
		
		finiteFlux.subscribe((element) -> System.out.println("Element is: " + element),
				(e) -> System.err.println("Exception is: " + e),
				() -> System.out.println("Done"), 
				(subscription -> subscription.cancel())); // Apenas se subscribe, se cancela.
	}
	
	@Test
	public void customizedBackPressure() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10)
				.log();
		
		// Aca uso una implementacion de BaseSubscriber, es mas customizado.
		finiteFlux.subscribe(new BaseSubscriber<Integer>() {
			
			@Override
			protected void hookOnNext(Integer value) {
				request(1);
				System.out.println("Value received is: " + value);
				
				if(value == 4) {
					cancel();
				}
			}
			
		});
	}
}
