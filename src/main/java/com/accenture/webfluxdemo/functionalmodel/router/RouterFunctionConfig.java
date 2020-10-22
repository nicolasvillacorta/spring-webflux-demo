package com.accenture.webfluxdemo.functionalmodel.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.accenture.webfluxdemo.functionalmodel.handler.SampleHandlerFunction;

@Configuration
public class RouterFunctionConfig {

	@Bean
	public RouterFunction<ServerResponse> route(SampleHandlerFunction handlerFunction) {

		return RouterFunctions
				.route(GET("/functional/flux"), handlerFunction::flux)
				.andRoute(GET("/functional/mono"), handlerFunction::mono);

	}

}
