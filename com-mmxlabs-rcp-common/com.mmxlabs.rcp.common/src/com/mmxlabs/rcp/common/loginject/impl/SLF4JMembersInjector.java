/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.loginject.impl;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.MembersInjector;

public class SLF4JMembersInjector<T> implements MembersInjector<T> {

	private final Field field;
	private final Logger logger;

	public SLF4JMembersInjector(final Field field) {
		this.field = field;
		this.logger = LoggerFactory.getLogger(field.getDeclaringClass());
		field.setAccessible(true);
	}

	@Override
	public void injectMembers(final T instance) {
		try {
			field.set(instance, logger);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
