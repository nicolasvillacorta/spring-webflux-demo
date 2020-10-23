package com.accenture.webfluxdemo.functionalmodel.handler;


import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SampleHandlerFunction {

	public Mono<ServerResponse> flux(ServerRequest serverRequest){
		
		return ServerResponse.ok()
					.contentType(MediaType.APPLICATION_STREAM_JSON)
					.body(
							Flux.just(2, 4, 6, 8, 10, 12, 14).delayElements(Duration.ofSeconds(1)).log(),  // Body de la respuesta (Siempre flux o mono)
							Integer.class //Hace falta aclarar el tipo del contenido de la misma.
					);
		
	}
	
	public Mono<ServerResponse> mono(ServerRequest serverRequest){
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(
						Mono.just(1).log(),  
						Integer.class 
				);
		
	}
	
	
}
