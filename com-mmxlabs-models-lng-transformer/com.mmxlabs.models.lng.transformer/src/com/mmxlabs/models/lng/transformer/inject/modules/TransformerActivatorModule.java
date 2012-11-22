package com.mmxlabs.models.lng.transformer.inject.modules;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.transformer.contracts.RestrictedElementsTransformerFactory;
import com.mmxlabs.models.lng.transformer.contracts.SimpleContractTransformerFactory;

/**
 * Module to register Transformer extension factories as a service
 * 
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public class TransformerActivatorModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		bindService(SimpleContractTransformerFactory.class).export();
		bindService(RestrictedElementsTransformerFactory.class).export();
	}

}
