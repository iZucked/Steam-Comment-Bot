/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.extensions;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

public class LingoExtensionsActivatorModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		bindService(LingoOptimiserModuleService.class).export();
	}
}
