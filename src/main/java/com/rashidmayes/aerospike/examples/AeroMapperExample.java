package com.rashidmayes.aerospike.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import com.aerospike.client.AerospikeClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rashidmayes.aerospike.tools.AeroMapper;

public class AeroMapperExample {
	
	public static void main(String[] args) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
		
        try {
        	AerospikeClient client = new AerospikeClient("ec2-52-200-188-251.compute-1.amazonaws.com",3000);
        	AeroMapper mapper = new AeroMapper(client);
        	
        	
    		Person p = new Person();
    		p.setFirstName("John");
    		p.setLastName("Doe");
    		p.setSsn("123456789");
    		p.setAge(17);
    		p.setHeight(5.10f);
    		p.setPhoto("RandomStringUtils.random(256)".getBytes());
        	
    		System.out.println(":::: IN ::::");
    		System.out.println(objectWriter.writeValueAsString(p));
        	
    		mapper.save(p);

        	
        	p = mapper.read(Person.class, "123456789");
        	

    		System.out.println("\n\n:::: OUT ::::");
        	System.out.println(objectWriter.writeValueAsString(p));
        	

        	List<String> temp = new ArrayList<>();
        	Map<String, Person> map = new HashMap<>();
        	map.put("1", new Person());
        	for ( int i = 0; i < 100; i++ ) {
        		p = new Person();
        		p.setAge(RandomUtils.nextInt());
        		p.setFirstName(RandomStringUtils.randomAlphabetic(8));
        		p.setLastName(RandomStringUtils.randomAlphabetic(8));
        		p.setHeight(RandomUtils.nextFloat());
        		p.setSsn(String.valueOf(i));
        		temp.add(p.getSsn());
        		p.setList(temp);
        		p.setMap(map);
        		mapper.save(p);
        	}
        	
        	
        	Function<Person,Boolean> function = person -> {
            	try {
            		System.out.println(String.format("\n\n:::: %s ::::", person.getSsn()));
					System.out.println(objectWriter.writeValueAsString(person));
					
					//mapper.delete(person);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
        		return true;
        	};
        	
        	mapper.find(Person.class, function);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
