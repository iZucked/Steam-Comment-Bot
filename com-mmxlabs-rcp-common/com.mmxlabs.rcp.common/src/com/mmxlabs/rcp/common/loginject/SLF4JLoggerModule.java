/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.loginject;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import com.mmxlabs.rcp.common.loginject.impl.SLF4JTypeListener;

/**
 * A Guice {@link Module} entry point to bind {@link Logger}s annotated with {@link Log}.
 * 
 * @author Simon Goodall
 */
public class SLF4JLoggerModule extends AbstractModule {

	@Override
	protected void configure() {
		bindListener(Matchers.any(), new SLF4JTypeListener());
	}
}
