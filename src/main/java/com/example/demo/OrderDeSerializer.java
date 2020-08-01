package com.example.demo;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderDeSerializer implements Deserializer<Order> {

	@Override
	public void configure(Map<String, ?> map, boolean b) {

	}

	@Override
	public Order deserialize(String arg0, byte[] arg1) {
		ObjectMapper mapper = new ObjectMapper();
		Order order = null;
		try {
			order = mapper.readValue(arg1, Order.class);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return order;
	}

	@Override
	public void close() {

	}
}
