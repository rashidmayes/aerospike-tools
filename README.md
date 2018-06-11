# aerospike-tools
Tools for interacting with the Aerospike database

# Aerospike Annotations

Annotations simplify reading and writing objects to the Aerospike database. For example, consider the code below that saves a Person object to Aerospike.

```java
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
}
```

To write person to Aerospike, simple use:

```java
Person p = new Person();
p.setFirstName("John");
p.setLastName("Doe");
p.setSsn("123456789");
p.setAge(17);

AerospikeClient client = new AerospikeClient("aerospike hostname",3000);
AeroMapper mapper = new AeroMapper(client);
mapper.save("test",p);
```
 
To read:
 
```java
Person person = mapper.read(Person.class, "123456789");
```
 
To delete:
 
```java
 mapper.delete(person);
```
 
To find:
 
```java
Function<Person,Boolean> function = person -> {
    System.out.println(person.getSsn());
    return true;
};
        	
mapper.find(Person.class, function);
```
