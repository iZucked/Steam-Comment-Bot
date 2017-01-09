/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;
import com.mmxlabs.common.recorder.conversion.ITypeConvertorService;

/**
 * Default implementation of {@link ITypeConvertorService}. This implementation will register {@link ITypeConvertor} for primitive and their Object counterparts. This can be disabled calling
 * {@link #TypeConvertorService(boolean)} with false.
 * 
 * @author Simon Goodall
 * 
 */
public final class TypeConvertorService implements ITypeConvertorService {

	private final Map<String, ITypeConvertor<?>> map = new HashMap<String, ITypeConvertor<?>>();

	/**
	 * Create instance of {@link TypeConvertorService} registering default set of {@link ITypeConvertor} for primitives.
	 */
	public TypeConvertorService() {
		this(true);
	}

	/**
	 * Create instance of {@link TypeConvertorService} specifying whether to register the default set of {@link ITypeConvertor} for primitives.
	 */
	public TypeConvertorService(final boolean registerDefault) {

		if (registerDefault) {
			// Register default handlers
			registerTypeConvertor(new StringTypeConvertor());
			registerTypeConvertor(new ShortTypeConvertor());
			registerTypeConvertor(new IntegerTypeConvertor());
			registerTypeConvertor(new LongTypeConvertor());
			registerTypeConvertor(new FloatTypeConvertor());
			registerTypeConvertor(new DoubleTypeConvertor());

			// Handle primitive types
			registerTypeConvertor("short", new ShortTypeConvertor());
			registerTypeConvertor("int", new IntegerTypeConvertor());
			registerTypeConvertor("long", new LongTypeConvertor());
			registerTypeConvertor("float", new FloatTypeConvertor());
			registerTypeConvertor("double", new DoubleTypeConvertor());
		}
	}

	@Override
	public void registerTypeConvertor(final ITypeConvertor<?> convertor) {
		registerTypeConvertor(convertor.getDataType().getCanonicalName(), convertor);
	}

	public void registerTypeConvertor(final String type, final ITypeConvertor<?> convertor) {
		map.put(type, convertor);
	}

	@Override
	public void unregisterTypeConvertor(final String className) {
		map.remove(className);
	}

	@Override
	public Object toObject(final String className, final String value) {
		if (map.containsKey(className)) {
			return map.get(className).toObject(value);
		}
		throw new IllegalArgumentException("Type: " + className + " has no registered type convertor");
	}

	@Override
	public String toString(final String className, final Object object) {
		if (map.containsKey(className)) {
			return map.get(className).toString(object);
		}
		throw new IllegalArgumentException("Type: " + className + " has no registered type convertor");
	}
}
