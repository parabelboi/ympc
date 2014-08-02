package eu.jmpd.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Entity implements Map<String,Object> {
	private final Map<String, Object> attributes;

	public void clear() {
		attributes.clear();
	}

	public boolean containsKey(Object key) {
		return attributes.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return attributes.containsValue(value);
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return attributes.entrySet();
	}

	public boolean equals(Object o) {
		return attributes.equals(o);
	}

	public Object get(Object key) {
		return attributes.get(key);
	}

	public int hashCode() {
		return attributes.hashCode();
	}

	public boolean isEmpty() {
		return attributes.isEmpty();
	}

	public Set<String> keySet() {
		return attributes.keySet();
	}

	public Object put(String key, Object value) {
		return attributes.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		attributes.putAll(m);
	}

	public Object remove(Object key) {
		return attributes.remove(key);
	}

	public int size() {
		return attributes.size();
	}

	public Collection<Object> values() {
		return attributes.values();
	}

	public Entity(Map<String, Object> attributes) {
		super();
		this.attributes = attributes;
	}
	
}
