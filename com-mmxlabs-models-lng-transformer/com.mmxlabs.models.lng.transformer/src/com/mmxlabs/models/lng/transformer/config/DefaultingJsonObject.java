/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

public class DefaultingJsonObject extends DefaultingJsonStructure<JSONObject, @Nullable DefaultingJsonObject> {
	private JSONObject inputJSONObject; // guaranteed to be a JSON object
	private JSONObject outputJSONObject = null; 
	private Map<String, Object> properties = new HashMap<>();
		
	/**
	 * This constructor is deliberately package-access to avoid external code invoking it.
	 * Please use DefaultingJsonEngine#newDefaultingJsonStructure() instead.
	 * 
	 * @param input
	 * @param template
	 * @throws IOException 
	 */
	DefaultingJsonObject(JSONObject input, DefaultingJsonEngine engine, DefaultingJsonObject template) throws IOException {
		super(engine, template);
		inputJSONObject = input;
		
		for (String key: getOutputKeys()) {
			properties.put(key, generateProperty(key));
		}
	}		
	
	public Object getProperty(String key) {
		if (properties.containsKey(key)) {
			return properties.get(key);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Generates a value for the named property.
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	private Object generateProperty(String key) throws IOException {
		// if the JSON structure this is based on has the specified key,
		// read the data from there; we may need to produce a derived template for it
		// and apply it to that template
		if (inputJSONObject.has(key)) {
			Object inputProperty = inputJSONObject.get(key);
			DefaultingJsonStructure<?,?> propertyTemplate = templateFor(key);
			
			// if the data is a JSONObject, we need to create a DefaultingJsonObject or DefaultingJsonArray from it
			if (inputProperty instanceof JSONObject) {
				return engine.newDefaultingJsonStructure((JSONObject) inputProperty, propertyTemplate);
			}
			// if the data is a JSONArray, and it extends a template, create a DefaultingJsonArray from it
			else if (inputProperty instanceof JSONArray) {
				if (propertyTemplate instanceof DefaultingJsonArray) {
					return engine.newDefaultingJsonStructure((JSONArray) inputProperty, (DefaultingJsonArray) propertyTemplate);
				}
				else if (propertyTemplate != null) {
					throw new RuntimeException("Cannot convert JSON array that extends a non-pseudo-array object.");
				}
				else {
					return engine.newDefaultingJsonStructure((JSONArray) inputProperty, null);
				}
			}
			
			// otherwise return the data directly
			return inputProperty;
		}
		
		// otherwise, check to see if we are meant to get the data from a
		// default object
		else if (template != null) {
			return template.getProperty(key);
		}
		
		// otherwise, no data!
		return null;
		// throw new RuntimeException(String.format("Cannot find property '%s' in input JSON or template", key));
 	}
	
	private DefaultingJsonStructure<?,?> templateFor(String key) {
		if (template != null) {
			Object templateProperty = template.getProperty(key);
			
			if (templateProperty instanceof DefaultingJsonStructure<?,?>) {
				return (DefaultingJsonStructure<?,?>) templateProperty;
			}			
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> getOutputKeys() {
		Set<String> result = new HashSet<>();
				
		result.addAll(inputJSONObject.keySet());
		
		if (template != null) {
			result.addAll(template.getOutputKeys());
		}		
				
		return result.stream().filter(key -> (key.startsWith("*") == false) ).collect(Collectors.toSet());
	}
	
		
	public JSONObject getOutput() {
		if (outputJSONObject == null) {		
			outputJSONObject = new JSONObject();
			
			for (String key: getOutputKeys()) {
				Object data = properties.get(key);
				
				if (data instanceof DefaultingJsonStructure) {
					data = ((DefaultingJsonStructure<?,?>) data).getOutput();
				}
				
				outputJSONObject.put(key, data);
			}
		}
		
		return outputJSONObject;
	}	

	public static void main(String [] args) {
		String jSrc = "{\r\n" + 
				"  \"*id\": \"srcObject\",\r\n" + 
				"  \"foo\": 1,\r\n" + 
				"  \"bar\": {\r\n" + 
				"    \"x\": 2,\r\n" + 
				"    \"y\": 3,\r\n" + 
				"    \"baz\": {\r\n" + 
				"      \"a\": 4,\r\n" + 
				"      \"b\": 5\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";

		String jTrg = "{\r\n" + 
				"  \"*reuse\": \"srcObject\",\r\n" + 
				"  \"bar\": {\r\n" + 
				"    \"z\": \"new_z\",\r\n" + 
				"    \"baz\": {\r\n" + 
				"      \"a\": \"new_a\"\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
		
		testObjects(jSrc, jTrg);
	}
}
