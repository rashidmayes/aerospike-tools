package com.rashidmayes.aerospike.examples;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.rashidmayes.aerospike.annotations.AerospikeBin;
import com.rashidmayes.aerospike.annotations.AerospikeKey;
import com.rashidmayes.aerospike.annotations.AerospikeRecord;

@AerospikeRecord(namespace="primary", set="people")
public class Person implements Serializable {
	
	@AerospikeKey
	@AerospikeBin(name="ssn")
	private String ssn; 

	@AerospikeBin(name="firstName")
	private String firstName;
	
	@AerospikeBin(name="lastName")
	private String lastName;
	
	@AerospikeBin(name="age")
	private long age;
	
	@AerospikeBin(name="height")
	private double height;
	
	@AerospikeBin(name="photo")
	private byte[] photo;

	@AerospikeBin(name="list")
	private List<String> list;
	
	@AerospikeBin(name="map")
	private Map<String, Person> map;
	
	
	@AerospikeBin(name="stringArray")
	private String[] stringArray;
	
	
	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public void setMap(Map<String, Person> map) {
		this.map = map;
	}

	public void setStringArray(String[] stringArray) {
		this.stringArray = stringArray;
	}

	public List<String> getList() {
		return list;
	}

	public Map<String, Person> getMap() {
		return map;
	}

	public String[] getStringArray() {
		return stringArray;
	}
	
	
}
