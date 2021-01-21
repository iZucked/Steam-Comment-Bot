/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contingencytime;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ContingencyIdleTimeModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		bindService(ContingencyIdleTimeTransformerFactory.class).export();
	}

}
