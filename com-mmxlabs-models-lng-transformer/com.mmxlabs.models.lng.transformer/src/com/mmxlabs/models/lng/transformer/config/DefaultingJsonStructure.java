/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.config;

import java.io.IOException;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents a JSON-like structure (object, literal value or array) that is potentially capable of reading unspecified property values from 
 * other structures. The structure should be initialised from a JSONObject that can possess special 'magic' properties, e.g.
 * <pre>
 * {
 *   "*reuse": "xxxx",
 *   "foo": "bar",
 *   "child": {
 *   	"foo": "bar"
 *   },
 *   "elements": [
 *   	{ "a": 2 }, 
 *   	{ "b": 3 }
 *   ]
 * }
 * </pre>
 * 
 * If basic (non-structured) property values are specified, they will always override any defaults. If structured property values 
 * (arrays or objects) are specified, they will (usually) be treated recursively as overrides. For instance, in the above example,
 * the evaluated result of obj.get("child").get("x") will be inherited from the "child" property of the reused object.
 * 
 * An array with overrideable default values can also be created, although it must be represented by a basic JSON object that has the form
 * {
 *    "*id": "yyyy",
 *    "*content": [ ... ]
 * }
 * ...in order to allow a UID to be attached to the array for reuse.
 * 
 * @author simonmcgregor
 *
 */
public abstract class DefaultingJsonStructure<T, @Nullable U extends DefaultingJsonStructure<T, U>> {
	public static final String ID_DEFINITION = "*id";
	public static final String TEMPLATE_TARGET = "*reuse";
	public static final String CONTENT_VALUE = "*content";
	public static final String ARRAY_KEY = "*key";
	public static final String REMOVE_ARRAY_ITEM = "*removed";
	public static final String ARRAY_POST_POSITION = "*after";
	public static final String ARRAY_PRE_POSITION = "*before";
	public static final String REQUIRED_FILES = "*requires";
	
	final DefaultingJsonEngine engine;
	protected U template = null;		
	
	
	/**
	 * This constructor is deliberately package-access to avoid external code invoking it.
	 * Please use DefaultingJsonEngine#newDefaultingJsonStructure() instead.
	 * 
	 * @param input
	 * @param target
	 * @throws IOException 
	 */
	DefaultingJsonStructure(DefaultingJsonEngine engine, U target) {
		this.engine = engine;
		template = target;		
	}
	
	
	public abstract T getOutput();
	
	public String outputString() {
		T output = getOutput();
		if (output == null) {
			return "null";
		}
		else {
			return output.toString();
		}
	}
	
	
	/**
	 * Performs a simple test on input objects.
	 * 
	 * @param inputs
	 */
	public static void testObjects(String ...inputs) {
		DefaultingJsonEngine engine = new DefaultingJsonEngine();
		try {
			for (String input: inputs) {
				DefaultingJsonStructure<?,?> object = engine.newDefaultingJsonStructure(new JSONObject(input));
				Object output = object.getOutput();
				
				if (output instanceof JSONObject) {				
					System.out.println(((JSONObject) output).toString(3));
				}
				else if (output instanceof JSONArray) {
					System.out.println(((JSONArray) output).toString(3));			
				}			
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) {
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
				"  },\r\n" + 
				"  \"people\": {\r\n" + 
				"    \"*key\": \"name\",\r\n" + 
				"    \"*content\": [\r\n" + 
				"      {\r\n" + 
				"        \"name\": \"Alice\",\r\n" + 
				"        \"age\": 10,\r\n" + 
				"        \"awards\": [ 2016, 2018 ]\r\n" + 
				"      },\r\n" + 
				"      { \"name\": \"Bob\", \"age\": 11 },\r\n" + 
				"      { \"name\": \"Charlie\", \"age\": 12 },\r\n" + 
				"      { \"name\": \"Deirdre\", \"age\": 13, \"height\": 120 }\r\n" + 
				"    ]\r\n" + 
				"  }\r\n" + 
				"}";

		String jTrg = "{\r\n" + 
				"  \"*reuse\": \"srcObject\",\r\n" + 
				"  \"bar\": {\r\n" + 
				"    \"z\": \"new_z\",\r\n" + 
				"    \"baz\": {\r\n" + 
				"      \"a\": \"new_a\"\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  \"people\": [\r\n" + 
				"    { \"name\": \"Alice\", \"age\": { \"*id\": \"literal1\", \"*content\": true } },\r\n" + 
				"    { \"name\": \"Emily\", \"age\": { \"*reuse\": \"literal1\" } },\r\n" + 
				"    { \"name\": \"Charlie\", \"*removed\": true },\r\n" + 
				"    { \"name\": \"Deirdre\", \"family\": \"Capone\", \"*reuse\": null, \"*before\": \"Alice\" }\r\n" + 
				"  ]\r\n" + 
				"}";
		
		testObjects( jSrc, jTrg );
	}	

}
