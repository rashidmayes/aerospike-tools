package com.rashidmayes.aerospike.examples;

import com.rashidmayes.aerospike.annotations.AerospikeBin;
import com.rashidmayes.aerospike.annotations.AerospikeKey;
import com.rashidmayes.aerospike.annotations.AerospikeRecord;

@AerospikeRecord(namespace="test", set="people")
public class Person {
	
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
	
	
}
