package com.accenture.webfluxdemo.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class ColdAndHotPublisherTest {

	@Test
	public void coldPublisherTest() throws InterruptedException {
		
		// Cold publisher:  Cada vez que nos subscribimos envia los valores desde el comienzo.
		Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
				.delayElements(Duration.ofSeconds(1));
		
		stringFlux.subscribe(s -> System.out.println("Subscriber 1: " + s)); // Cuando alguien se subscribe, emite un valor del comienzo.
		
		Thread.sleep(2000);
		
		stringFlux.subscribe(s -> System.out.println("Subscriber 2: " + s));
		
		Thread.sleep(4000);
	}
	
	@Test
	public void hotPublisherTest() throws InterruptedException {
		
		Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
				.delayElements(Duration.ofSeconds(1));
		
		ConnectableFlux<String> connectableFlux = stringFlux.publish();
		connectableFlux.connect(); // Esta linea hace que actue como un hot publisher.
		
		connectableFlux.subscribe((s) -> System.out.println("Subscriber 1 :" + s));
		
		Thread.sleep(3000);
		
		// No emite los values desde el comienzo, sino el primero que aun no entrego el publisher. 
		connectableFlux.subscribe((s) -> System.out.println("Subscriber 2 :" + s)); 
		
		Thread.sleep(4000);
	}
}
