/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class representing a template-driven JSON output array. Instances can be created from a JSON object in 
 * template data, or a JSON array in template data.  
 * 
 * @author simonmcgregor
 *
 */
public class DefaultingJsonArray extends DefaultingJsonStructure<JSONArray, @Nullable DefaultingJsonArray> {
	private String arrayKey;
	private JSONArray inputJSONArray;
	private @NonNull JSONArray outputJSONArray = new JSONArray();
		
	/**
	 * This constructor is deliberately package-access to avoid external code invoking it.
	 * Please use DefaultingJsonEngine#newDefaultingJsonStructure() instead.
	 * 
	 * @param input
	 * @param template
	 * @throws IOException 
	 */
	DefaultingJsonArray(JSONArray array, String arrayKey, DefaultingJsonEngine engine, DefaultingJsonArray template) throws IOException {
		super(engine, template); 
		this.inputJSONArray = array;
		this.arrayKey = arrayKey;		
		
		for (Object item: getOutputItems().values()) {
			if (item instanceof DefaultingJsonStructure) {
				item = ((DefaultingJsonStructure<?,?>) item).getOutput();
			}
			
			outputJSONArray.put(item);
		}
		
	}

	
	String getArrayKey() {
		return arrayKey;
	}

	/**
	 * Returns a list of the items in the JSON array, after overrides have been applied.
	 * 
	 * Unless the template for this object provides a "*key" property, then any input JSON for this object simply overwrites the list of items in the reuse target with a new list. 
	 * 
	 * If there is a "*key" property, then it can be used to override defaults
	 * from the template selectively with data from the input JSON for this object. 
	 * Namely, all the items in that template should be in the return result from this method,
	 * with the exception of items that have been explicitly "*removed" by entries in the input JSON for this
	 * object. Those items should be overridden selectively with any properties that are explicitly specified in the 
	 * input JSON items (the "*key" field is used to match items from the input JSON to items from the template).
	 * 
	 * Examples:
	 * 
	 * Based on
	 * 
	 * {
	 *   "*id": "xxx",
	 *   "*key": "name",
	 *   "*content": [
	 *     { "name": "Alice", "age": 10 }, 
	 *     { "name": "Bob", "age": 11 }, 
	 *     { "name": "Charlie", "age": 12 }, 
	 *   ]
	 * }
	 * 
	 * then 
	 * 
	 * {
	 *    "*reuse": "xxx",
	 *    "*content": [
	 *      { "name": "Alice", "age": 11 },
	 *      { "name": "Bob", "*removed": true },
	 *      { "name": "Deirdre", "age": 13 }
	 *    ]
	 * }
	 * 
	 * evaluates to the JSONArray
	 * 
	 * [
	 *     { "name": "Alice", "age": 11 },
	 *     { "name": "Charlie", "age": 12 },
	 *     { "name": "Deirdre", "age": 13 },
	 * ]
	 *
	 * @return
	 * @throws IOException 
	 */
	public LinkedHashMap<Object, Object> getOutputItems() throws IOException {
		if (inputJSONArray == null && template == null) {
			throw new RuntimeException("No array in JSON to get values from.");
		}		
		
		// if we are using a 'key' to override entries, 
		if (arrayKey != null) {
			return getOutputItemsUsingKey(arrayKey);
		}
		return getOutputItemsWithoutKey();
	}
	
	/**
	 * Returns a list of the virtual items for this object, based on overriding an optional 
	 * default list, without using a 'key' to selectively override items in the default list.
	 * 
	 * Without a key, the only way to override is to provide a new list that
	 * overwrites the default list in its entirety.
	 * 
	 * @return
	 * @throws IOException 
	 */
	private LinkedHashMap<Object, Object> getOutputItemsWithoutKey() throws IOException {
		// if there is no array in the source JSON
		if (inputJSONArray == null) {
			// fall back on the template
			if (template != null) {
				return template.getOutputItems();
			}
			// or throw an exception
			else {
				throw new RuntimeException("Attempt to list items of array with no content or template.");
			}
		}
		// if there is an array in the source JSON
		else {
			// completely override the template
			LinkedHashMap<Object, Object> entries = new LinkedHashMap<>();;
			for (int i = 0; i < inputJSONArray.length(); i++) {
				Object item = inputJSONArray.get(i);
				
				// we can't associate this object with any template because we can't match it to any item in the template's array of items
				// but we still want to apply defaulting rules to it if it is a JSONObject or JSONArray (since its children might declare 
				// template use)
				if (item instanceof JSONObject) {
					item = engine.newDefaultingJsonStructure((JSONObject) item, null);
				}
				else if (item instanceof JSONArray) {
					item = engine.newDefaultingJsonStructure((JSONArray) item, null);
					
				}
				
				entries.put(item, item);
			}
			return entries;
		}
	}
	
	/**
	 * Helper class to allow the order of items in a LinkedHashMap to be rearranged. 
	 * 
	 * <p>Call {@link LinkedHashMapRearranger#moveBefore} to move an item with a particular key before the item with another key.</p>
	 * <p>Call {@link LinkedHashMapRearranger#moveAfter} to move an item with a particular key before the item with another key.</p>
	 * <p>Call {@link LinkedHashMapRearranger#rearrange} to return a LinkedHashMap, with its items rearranged according to the specified moves.</p>
	 * @author simonmcgregor
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class LinkedHashMapRearranger<K, V> {
		private Map<K, List<K>> preInsertionsByLocation = new HashMap<>();
		private Map<K, List<K>> postInsertionsByLocation = new HashMap<>();
		private Set<K> moved = new HashSet<>();

		/**
		 * Appends entries from {@code source} to {@code result} based on a map {@code insertions} of relative insertions and a key {@code key}
		 * that might need entries inserted in a particular order relative to it.
		 * 
		 * @param result
		 * @param key
		 * @param source
		 * @param insertions
		 */
		private void appendFromRelativeInsertions(LinkedHashMap<K, V> result, K key, Map<K, V> source, Map<K, List<K>> insertions) {
			if (insertions.containsKey(key)) {
				for (K insertion: insertions.get(key)) {
					result.put(insertion, source.get(insertion));
				}
			}			
		}
		
		/**
		 * Returns a LinkedHashMap that has the same iteration order as {@code source} except for the 
		 * moves specified by rearrangement instructions to the {@link LinkedHashMapRearranger}.
		 * 
		 * If no moves were specified, this returns the original {@code source} object, which is immutable; 
		 * otherwise, it returns a new {@link LinkedHashMap}.  
		 * 
		 * @param source
		 * @return
		 */
		public LinkedHashMap<K, V> rearrange(LinkedHashMap<K, V> source) {
			if (moved.isEmpty()) {
				return source;
			}
			
			LinkedHashMap<K, V> result = new LinkedHashMap<>();
			
			for (K key: source.keySet()) {
				appendFromRelativeInsertions(result, key, source, preInsertionsByLocation);
				
				if (moved.contains(key) == false) {
					result.put(key, source.get(key));
				}
				
				appendFromRelativeInsertions(result, key, source, postInsertionsByLocation);
				
			}
			
			return result;
		}
		
		/**
		 * Marks a particular key to say that its entry should occur in some position relative to another key
		 * when the output map is iterated.
		 * 
		 * @param objectKey
		 * @param locationKey
		 * @param map
		 */
		private void moveRelative(K objectKey, K locationKey, Map<K, List<K>> map) {			
			if (moved.contains(locationKey)) {
				throw new RuntimeException("Cannot set an element's location relative to a moved element's location.");
			}			
			
			if (moved.contains(objectKey)) {
				throw new RuntimeException("Cannot move an element's location more than once.");
			}
			
			if (map.containsKey(locationKey) == false) {
				map.put(locationKey, new LinkedList<>());
			}
			map.get(locationKey).add(objectKey);
			moved.add(objectKey);			
		}
		
		/**
		 * Instructs the {@link LinkedHashMapRearranger} to move the entry with key {@code object} so that it is before 
		 * the entry with key {@code location} in the source map's iteration order, when {@link LinkedHashMapRearranger#rearrange} 
		 * is called.
		 * @param object
		 * @param location
		 */
		public void moveBefore(K object, K location) {
			moveRelative(object, location, preInsertionsByLocation);
		}
		
		/**
		 * Instructs the {@link LinkedHashMapRearranger} to move the entry with key {@code object} so that it is after 
		 * the entry with key {@code location} in the source map's iteration order, when {@link LinkedHashMapRearranger#rearrange} 
		 * is called.
		 * @param object
		 * @param location
		 */
		public void moveAfter(K object, K location) {
			moveRelative(object, location, postInsertionsByLocation);
		}		
	}
	
	/**
	 * Returns a list of the virtual items for this object, based on overriding an optional
	 * default list, using a 'key' to allow for selective overriding. 
	 * 
	 * An item in the overriding list will override any item in the default list that shares the 
	 * same key value. If there is no matching item in the default list, the overriding item is
	 * added to the list of virtual items. A '*removed' directive on an overriding item can be 
	 * used to remove a default item with the same key from the list of virtual items.
	 * 
	 * The order of new or overridden items in the list can be partially controlled by using a
	 * '*before' or '*after' directive to specify the key of an item that the new / overridden
	 * item is supposed to be located relative to. This mechanism does not work if the relative
	 * reference item was itself moved using a '*before' or '*after' directive.
	 * 
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	LinkedHashMap<Object, Object> getOutputItemsUsingKey(String key) throws IOException {
		LinkedHashMap<Object, Object> entries;
		LinkedHashMap<Object, Object> templateEntries;
		LinkedHashMapRearranger<Object, Object> rearranger = new LinkedHashMapRearranger<>();
		
		// read the default values from the template, if any
		if (template == null) {
			templateEntries = null;
			entries = new LinkedHashMap<>();
		}
		else {
			templateEntries = template.getOutputItems();
			entries = (LinkedHashMap<Object, Object>) templateEntries.clone();
		}
				
		if (inputJSONArray == null) {
			return entries;
		}
		
		// override the default values selectively
		for (int i = 0; i < inputJSONArray.length(); i++) {
			Object item = inputJSONArray.get(i);
			
			if (item instanceof JSONObject == false || ((JSONObject) item).has(key) == false) {
				throw new RuntimeException("Item found in JSON array without expected key '" + key + "'");
			}
			
			JSONObject jItem = (JSONObject) item;
			String thisKey = (String) jItem.get(key);
			
			// check if the item is supposed to delete an element
			if (jItem.has(REMOVE_ARRAY_ITEM)) {
				entries.remove(thisKey);
			}
			else {
				DefaultingJsonStructure<?,?> newItem;
				
				// if the item is supposed to override a template item, return an overrider structure for that element
				if (templateEntries != null && templateEntries.containsKey(thisKey)) {
					newItem = engine.newDefaultingJsonStructure(jItem, (DefaultingJsonObject) templateEntries.get(thisKey));
				}
				// if not, return an overrider structure based only on the source JSON 
				else {
					newItem = engine.newDefaultingJsonStructure(jItem);
				}

				entries.put(thisKey, newItem);					
				
				// TODO: allow the item to be inserted in a particular location
				if (jItem.has(ARRAY_POST_POSITION)) {
					rearranger.moveAfter(thisKey, jItem.getString(ARRAY_POST_POSITION));
				}
				
				if (jItem.has(ARRAY_PRE_POSITION)) {
					rearranger.moveBefore(thisKey, jItem.getString(ARRAY_PRE_POSITION));
				}
				
			}													
		}
		
		return rearranger.rearrange(entries);		
	}
		
	@Override
	public @NonNull JSONArray getOutput() {
		return outputJSONArray;
	}		

	public static void main(String args[]) {
		String jSrc = "{\r\n" + 
				"  \"*id\": \"srcObject\",\r\n" + 
				"  \"*key\": \"name\",\r\n" + 
				"  \"*content\": [\r\n" + 
				"    { \"name\": \"Alice\", \"age\": 10 },\r\n" + 
				"    { \"name\": \"Bob\", \"age\": 11 },\r\n" + 
				"    { \"name\": \"Charlie\", \"age\": 12 }\r\n" + 
				"  ]\r\n" + 
				"}";

		String jTrg = "{\r\n" + 
				"  \"*reuse\": \"srcObject\",\r\n" + 
				"  \"*content\": [\r\n" + 
				"    { \"name\": \"Alice\", \"age\": \"new_age\", \"*after\": \"Bob\" },\r\n" + 
				"    { \"name\": \"Charlie\", \"*removed\": true },\r\n" + 
				"    { \"name\": \"Deirdre\", \"age\": 13, \"*before\": \"Bob\" }\r\n" + 
				"  ]\r\n" + 
				"}";
				
		testObjects(jSrc, jTrg);
	}

}
