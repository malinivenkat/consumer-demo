package com.example.demo;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderSerializer implements Serializer<Order>{
	
	@Override
	public void configure(Map<String, ?> map, boolean b) {
		
	}
	
	@Override 
	public byte[] serialize(String arg0, Order arg1) {
	    byte[] retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(arg1).getBytes();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
	  }

	  @Override public void close() {

	  }
}
