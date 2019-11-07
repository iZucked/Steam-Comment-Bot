/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.config;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;

public class DefaultingJsonLiteral extends DefaultingJsonStructure<Object, @Nullable DefaultingJsonLiteral> {
	private final Object value;
	
	/**
	 * The Java classes that are permissible values for a {@link DefaultingJsonLiteral} object.
	 */
	@SuppressWarnings("rawtypes")
	static final Class [] permissibleClasses = {
		String.class,
		Integer.class,
		Float.class,
		Double.class,
		Boolean.class,
		JSONObject.NULL.getClass()
	};

	/**
	 * This constructor is deliberately package-access to avoid external code invoking it.
	 * Please use DefaultingJsonEngine#newDefaultingJsonStructure() instead.
	 * 
	 * @param input
	 * @param template
	 * @throws IOException 
	 */
	public DefaultingJsonLiteral(Object value, DefaultingJsonEngine engine, DefaultingJsonLiteral template) {
		super(engine, template);
		
		if (value != null) {
			if (canMakeLiteral(value)) {
				this.value = value;
			}
			else {
				throw new IllegalArgumentException("Tried to initialise a DefaultingJsonLiteral with a value of type %s");
			}
		}
		else {
			this.value = null;
		}
	}
	
	/**
	 * Returns {@code true} if its parameter can be made into a {@code DefaultingJsonLiteral}, {@code false} 
	 * otherwise.
	 * 
	 * @param object
	 * @return
	 */
	public static boolean canMakeLiteral(Object object) {
		Class<? extends @NonNull Object> clazz = object.getClass();

		return Arrays.asList(permissibleClasses).contains(clazz);
	}
	

	@Override
	public Object getOutput() {
		if (value == null && template != null) {
			return template.getOutput();
		}
		
		return value;
	}



}
