/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;
import com.mmxlabs.common.recorder.conversion.ITypeConvertorService;

public final class TypeConvertorService implements ITypeConvertorService {

	private final Map<String, ITypeConvertor> map = new HashMap<String, ITypeConvertor>();

	public TypeConvertorService() {
		this(true);
	}

	public TypeConvertorService(boolean registerDefault) {

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
	public void registerTypeConvertor(final ITypeConvertor convertor) {
		registerTypeConvertor(convertor.getDataType().getCanonicalName(),
				convertor);
	}

	public void registerTypeConvertor(final String type,
			final ITypeConvertor convertor) {
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
		throw new IllegalArgumentException("Type: " + className
				+ " has no registered type convertor");
	}

	@Override
	public String toString(final String className, final Object object) {
		if (map.containsKey(className)) {
			return map.get(className).toString(object);
		}
		throw new IllegalArgumentException("Type: " + className
				+ " has no registered type convertor");
	}
}
