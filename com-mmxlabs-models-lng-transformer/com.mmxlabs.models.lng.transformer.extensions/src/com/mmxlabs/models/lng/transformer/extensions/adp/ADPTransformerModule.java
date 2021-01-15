/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.adp;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

/**
 * This is a PeaberryActivationModule to hook in the ADP extensions into the optimiser
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class ADPTransformerModule extends PeaberryActivationModule {

	@Override
	protected void configure() {

		bindService(ADPConstraintsTransformerFactory.class).export();
	}
}
