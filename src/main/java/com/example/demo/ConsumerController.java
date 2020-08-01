package com.example.demo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@CrossOrigin
@RestController
public class ConsumerController {

	private static final String BOOTSTRAP_SERVERS = "localhost:9092";
	private static final String TOPIC = "order";
	private List<String> orders;
	private Logger logger;
	
	//uses a Kafka library to consume
	private KafkaConsumer<Integer, String> kafkaConsumer;

	public ConsumerController() {
		logger = LoggerFactory.getLogger(ConsumerController.class);

		// Creating consumer properties
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "GROUP1");
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// creating consumer
		kafkaConsumer = new KafkaConsumer<Integer, String>(properties);
		// Subscribing
		kafkaConsumer.subscribe(Arrays.asList(TOPIC));
	}

//	@GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	public Flux<String> streamFlux() {
//		return Flux.interval(Duration.ofSeconds(1)).map(sequence -> "Flux - " + LocalTime.now().toString());
//	}

	@GetMapping(path = "/stream-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamEvents() {
		return Flux.interval(Duration.ofSeconds(1)).onBackpressureDrop().map(this::consumer).flatMapIterable(x -> x);
	}

	@GetMapping(path = "/hello")
	public String sayHello() {
		return "Hello";
	}

	private List<String> consumer(long interval) {
		// polling
//		while (true) 
		{
			orders = new ArrayList<String>();

			ConsumerRecords<Integer, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
			System.out.println(records.count());
			for (ConsumerRecord<Integer, String> record : records) {
				System.out.println("Key: " + record.key() + ", Value:" + record.value());
				logger.info("Partition:" + record.partition() + ",Offset:" + record.offset());

				orders.add(record.value());
			}
		}
		return orders;
	}

}