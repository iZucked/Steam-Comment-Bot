/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

/**
 * This module binds values to the Guice injection framework that are taken from a JSON parse tree. 
 * 
 * Derived classes should declare a constructor that puts importers in the "importers" field,
 * e.g.
 * 
 * <pre>
 *class MyModule extends JsonConfiguredModule {
 *    public MyModule(final JsonNode data) {
 *        super(data);
 *        importers.put("boundName", (name, value) -> this.bindFromJsonNode(value, name) );
 *    }
 *}
 * 
 * </pre>
 * 
 * 
 * @author simonmcgregor
 *
 */
public abstract class JsonConfiguredModule extends AbstractModule {

	//protected final OptimiserConfigurationOptions settings;
	protected final JsonNode data;
	
	protected final Map<String, BiConsumer<String, JsonNode>> importers;
	
	public class UnhandledJsonTypeException extends RuntimeException {
		public UnhandledJsonTypeException(JsonNode node) {
			super(String.format("Cannot auto-bind value '%s' with type '%s", node.toString(), node.getNodeType().toString()));
		}
	}

	public JsonConfiguredModule(final JsonNode data) {
		this.data = data;
		this.importers = new HashMap<>();
	}
	
	@Override
	protected void configure() {
		Iterator<Entry<String, JsonNode>> it = data.fields();
		
		while (it.hasNext()) {
			Entry<String, JsonNode> entry = it.next();
			
			String name = entry.getKey();
			if (importers.containsKey(name)) {
				importers.get(name).accept(name, entry.getValue());
			}
		}
		
	}
	
	/**
	 * Binds the specified name to Boolean or String data auto-detected from the given node's value type.
	 * 
	 * @param node
	 * @param name
	 */
	protected void bindFromJsonNode(JsonNode node, String name) {
		// default binding using automatable value types
		
		switch(node.getNodeType()) {
			case BOOLEAN: {
				bindFromJsonNode(node, name, new TypeLiteral<Boolean>() {});
				break;
			}
			case STRING: {
				bindFromJsonNode(node, name, new TypeLiteral<String>() {});
				break;
			}
			default: {
				throw new UnhandledJsonTypeException(node); // cannot auto-bind Number since it is too coarse-grained
			}
		}
		
		
	}

	/**
	 * Binds the specified name to data of the specified type, attempting to convert automatically from the JSON data.
	 * 
	 * @param node A JsonNode containing data that should be bound using the Guice dependency injection framework.
	 * @param name The name to use in the Guice binding.
	 * @param typeLiteral The type to import the data as. Currently, the only types supported are Integer, Boolean, 
	 * Float, Double, String, and {@code Map<String, X>} where X is one of the previously listed primitive types.
	 */
	protected <T> void bindFromJsonNode(JsonNode node, String name, TypeLiteral<T> typeLiteral) {
		bind(typeLiteral).annotatedWith(Names.named(name)).toInstance(getJsonNodeValueAs(node, typeLiteral));		
	}
	
	private <T> T getJsonNodeValueAs(JsonNode node, TypeLiteral<T> tl) {
		Class<? super T> rt = tl.getRawType();
		
		if (rt == Map.class) {
			ParameterizedType t = (ParameterizedType) tl.getType();
			Type[] args = t.getActualTypeArguments();
			return (T) getJsonNodeValueAsMap(node, (Class) args[1]);
			
		}
		else  {
			return (T) getJsonNodeValueAs(node, rt); 
		}
		
	}
	
	/**
	 * Transforms the node contents into a {@code Map<String, X>} where X is the specified class. 
	 * @param node
	 * @param clazz
	 * @return
	 */
	private <T> Map<String, T> getJsonNodeValueAsMap(JsonNode node, Class<T> clazz) {
		Map<String, T> result = new HashMap<>();
		
		Iterator<String> it = node.fieldNames();
		while (it.hasNext()) {
			String key = it.next();
			T value = getJsonNodeValueAs(node.get(key), clazz);
			result.put(key, value);
		}
		
		return result;		
	}
	
	/**
	 * Attempts to automatically convert the specified node into a value of the given type.
	 * 
	 * @param node
	 * @param clazz
	 * @return
	 */
	private <T> T getJsonNodeValueAs(JsonNode node, Class<T> clazz) {
		if (clazz == Boolean.class) {
			return (T) Boolean.valueOf(node.asBoolean());
		}
		else if (clazz == String.class) {
			return (T) node.asText();
		}
		else if (clazz == Float.class) {
			return (T) Float.valueOf(node.floatValue());
		}
		else if (clazz == Integer.class) {
			return (T) Integer.valueOf(node.asInt());
		}
		else if (clazz == Double.class) {
			return (T) Double.valueOf(node.asDouble());
		}
		
		throw new UnhandledJsonTypeException(node);
	}
	

}
