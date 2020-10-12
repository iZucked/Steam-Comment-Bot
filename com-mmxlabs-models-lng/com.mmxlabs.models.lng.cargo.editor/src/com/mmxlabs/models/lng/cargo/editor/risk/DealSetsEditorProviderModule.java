package com.mmxlabs.models.lng.cargo.editor.risk;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.cargo.util.DefaultExposuresCustomiser;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;

public class DealSetsEditorProviderModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(IExposuresCustomiser.class).to(DefaultExposuresCustomiser.class);
	}
}
