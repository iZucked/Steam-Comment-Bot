/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.paperdeals;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

/**
 * This is a PeaberryActivationModule to hook in the restricted elements constraints into the optimisation. This in not intended to be registered directly in the MANIFEST.MF file, but installed in a
 * 
 * @link{TransformerActivatorModule}
 * 
 * @author FM
 * 
 */
public class PaperDealDataModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		bindService(PaperDealDataTransformerFactory.class).export();
	}
}
