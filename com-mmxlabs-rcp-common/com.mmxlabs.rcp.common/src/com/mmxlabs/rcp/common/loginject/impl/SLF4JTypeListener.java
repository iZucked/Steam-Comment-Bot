/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.loginject.impl;

import java.lang.reflect.Field;

import org.slf4j.Logger;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mmxlabs.rcp.common.loginject.Log;

public class SLF4JTypeListener implements TypeListener {
	@Override
	public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
		for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
			// TODO: Add in other types of Logger
			if (field.getType() == Logger.class && field.isAnnotationPresent(Log.class)) {
				typeEncounter.register(new SLF4JMembersInjector<T>(field));
			}
		}
	}
}