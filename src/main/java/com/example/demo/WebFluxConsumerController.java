package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@CrossOrigin
@RestController
public class WebFluxConsumerController {
	@Autowired
	KafkaReceiver<String, String> kafkaReceiver;

	@GetMapping(path = "/stream-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<String> getEventsFlux() {
		Flux<ReceiverRecord<String, String>> kafkaFlux = kafkaReceiver.receive();
		return kafkaFlux.checkpoint("Consumption started...").log().doOnNext(r -> r.receiverOffset().acknowledge())
				.map(ReceiverRecord::value).checkpoint("Consumption completed");
	}

}
