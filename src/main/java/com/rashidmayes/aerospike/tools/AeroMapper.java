package com.rashidmayes.aerospike.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.Value;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.rashidmayes.aerospike.annotations.AerospikeBin;
import com.rashidmayes.aerospike.annotations.AerospikeKey;
import com.rashidmayes.aerospike.annotations.AerospikeRecord;
import com.sun.istack.NotNull;

public class AeroMapper {

	AerospikeClient mClient;
	
	public AeroMapper(@NotNull AerospikeClient client) {
		this.mClient = client;
	}

	
	public void save(@NotNull Object object) throws AerospikeException {
		
        Class<?> clazz = object.getClass();
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	String namespace = recordAnnotation.namespace();
        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	
        	String set = recordAnnotation.set();
        	int ttl = recordAnnotation.ttl();
        	
        	Key key = null;
        	String binName;
        	List<Bin> bins = new ArrayList<>();
        	
        	try {
            	for (Field field: clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                
                    if (field.isAnnotationPresent(AerospikeKey.class)) {      	
                    	key = new Key(namespace, set, Value.get(field.get(object)));
                    }
                    
                    if (field.isAnnotationPresent(AerospikeBin.class)) {
           	
                    	binName = field.getAnnotation(AerospikeBin.class).name();
                    	bins.add(new Bin(binName, field.get(object)));
                    }
                }
        	} catch (IllegalAccessException e) {
        		throw new AerospikeException(e);
        	}
        	
        	if ( key == null ) {
        		throw new AerospikeException("Null key from annotated object.");
        	} else {
        		if ( ttl == 0 ) {
        			mClient.put(null, key, bins.toArray(new Bin[bins.size()]));
        		} else {
        			WritePolicy policy = new WritePolicy();
        			policy.expiration = ttl;
        			mClient.put(policy, key, bins.toArray(new Bin[bins.size()]));
        		}	
        	}
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	
	public void save(@NotNull String namespace, @NotNull Object object) throws AerospikeException {
		
        Class<?> clazz = object.getClass();
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);
        	
        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	String set = recordAnnotation.set();
        	int ttl = recordAnnotation.ttl();
        	
        	Key key = null;
        	String binName;
        	List<Bin> bins = new ArrayList<>();
        	
        	try {
            	for (Field field: clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                
                    if (field.isAnnotationPresent(AerospikeKey.class)) {      	
                    	key = new Key(namespace, set, Value.get(field.get(object)));
                    }
                    
                    if (field.isAnnotationPresent(AerospikeBin.class)) {
           	
                    	binName = field.getAnnotation(AerospikeBin.class).name();
                    	bins.add(new Bin(binName, field.get(object)));
                    }
                }
        	} catch (IllegalAccessException e) {
        		throw new AerospikeException(e);
        	}
        	
        	if ( key == null ) {
        		throw new AerospikeException("Null key from annotated object.");
        	} else {
        		if ( ttl == 0 ) {
        			mClient.put(null, key, bins.toArray(new Bin[bins.size()]));
        		} else {
        			WritePolicy policy = new WritePolicy();
        			policy.expiration = ttl;
        			mClient.put(policy, key, bins.toArray(new Bin[bins.size()]));
        		}	
        	}
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	public <T> T read(@NotNull Class<T> clazz, Object userKey) throws AerospikeException {
		
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	String namespace = recordAnnotation.namespace();
        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	String set = recordAnnotation.set();

        	Key key = new Key(namespace, set, Value.get(userKey));
        	Record record = mClient.get(null, key);
        	
        	if ( record == null ) {
        		return null;
        	} else {

            	try {
            		T result = convertRecord(clazz, record);
                	
                	return result;
            	} catch (ReflectiveOperationException e) {
            		throw new AerospikeException(e);
            	}
        	}
        	
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	
	public <T> T read(@NotNull Class<T> clazz, @NotNull String namespace, Object userKey) throws AerospikeException {
		
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	String set = recordAnnotation.set();

        	Key key = new Key(namespace, set, Value.get(userKey));
        	Record record = mClient.get(null, key);
        	
        	if ( record == null ) {
        		return null;
        	} else {

            	try {
            		T result = convertRecord(clazz, record);
                	
                	return result;
            	} catch (ReflectiveOperationException e) {
            		throw new AerospikeException(e);
            	}
        	}
        	
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	
	
	public boolean delete(@NotNull Class<?> clazz, @NotNull Object userKey) throws AerospikeException {
		
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	String namespace = recordAnnotation.namespace();
        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	String set = recordAnnotation.set();

        	Key key = new Key(namespace, set, Value.get(userKey));
        	return mClient.delete(null, key);      	
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	
	public boolean delete(@NotNull Class<?> clazz, @NotNull String namespace, @NotNull Object userKey) throws AerospikeException {
		
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	String set = recordAnnotation.set();

        	Key key = new Key(namespace, set, Value.get(userKey));
        	return mClient.delete(null, key);      	
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	
	public boolean delete(@NotNull Object object) throws AerospikeException {
		
        Class<?> clazz = object.getClass();
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	String namespace = recordAnnotation.namespace();
        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	
        	String set = recordAnnotation.set();
        	
        	Key key = null;
        	try {
            	for (Field field: clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                
                    if (field.isAnnotationPresent(AerospikeKey.class)) {      	
                    	key = new Key(namespace, set, Value.get(field.get(object)));
                    }
                }
        	} catch (IllegalAccessException e) {
        		throw new AerospikeException(e);
        	}
        	
        	if ( key == null ) {
        		throw new AerospikeException("Null key from annotated object.");
        	} else {
        		return mClient.delete(null, key);	
        	}
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	
	public boolean delete(@NotNull String namespace, @NotNull Object object) throws AerospikeException {
		
        Class<?> clazz = object.getClass();
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	
        	String set = recordAnnotation.set();
        	
        	Key key = null;
        	try {
            	for (Field field: clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                
                    if (field.isAnnotationPresent(AerospikeKey.class)) {      	
                    	key = new Key(namespace, set, Value.get(field.get(object)));
                    }
                }
        	} catch (IllegalAccessException e) {
        		throw new AerospikeException(e);
        	}
        	
        	if ( key == null ) {
        		throw new AerospikeException("Null key from annotated object.");
        	} else {
        		return mClient.delete(null, key);	
        	}
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	
	
	public <T> void find(@NotNull Class<T> clazz, Function<T,Boolean> function) throws AerospikeException {
		
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	String namespace = recordAnnotation.namespace();
        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	String set = recordAnnotation.set();

        	Statement statement = new Statement();
        	statement.setNamespace(namespace);
        	statement.setSetName(set);
        	
        	RecordSet recordSet = null;
        	try {
            	recordSet = mClient.query(null, statement);
            	T result;
            	while ( recordSet.next() ) {
            		result = convertRecord(clazz, recordSet.getRecord());
            		if  ( !function.apply(result) ) {
            			break;
            		}
            	}  
        	} catch (ReflectiveOperationException e) {
				throw new AerospikeException(e);
			} finally {
        		if ( recordSet != null ) {
        			recordSet.close();
        		}
        	}
        	
      	
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	
	public <T> void find(@NotNull Class<T> clazz, String namespace, Function<T,Boolean> function) throws AerospikeException {
		
        if ( clazz.isAnnotationPresent(AerospikeRecord.class) ) {
        	AerospikeRecord recordAnnotation = clazz.getAnnotation(AerospikeRecord.class);

        	if ( StringUtils.isBlank(namespace) ) {
        		throw new AerospikeException("Namespace not specified in annotation.");
        	}
        	String set = recordAnnotation.set();

        	Statement statement = new Statement();
        	statement.setNamespace(namespace);
        	statement.setSetName(set);
        	
        	RecordSet recordSet = null;
        	try {
            	recordSet = mClient.query(null, statement);
            	T result;
            	while ( recordSet.next() ) {
            		result = convertRecord(clazz, recordSet.getRecord());
            		if  ( !function.apply(result) ) {
            			break;
            		}
            	}  
        	} catch (ReflectiveOperationException e) {
				throw new AerospikeException(e);
			} finally {
        		if ( recordSet != null ) {
        			recordSet.close();
        		}
        	}
        	
      	
        } else {
        	throw new AerospikeException("No annotations specified");
        }
	}
	
	public <T> T convertRecord(Class<T> clazz, Record record) throws ReflectiveOperationException {
		T result = clazz.getConstructor().newInstance();
		String binName;
		
    	for (Field field: clazz.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(AerospikeBin.class)) {
            	binName = field.getAnnotation(AerospikeBin.class).name();
            	if ( record.bins.containsKey(binName) ) {
            		field.set(result, record.bins.get(binName));
            	}
            }
        }
    	
    	return result;
	}
}
