package com.mmxlabs.models.lng.transformer.extensions.fobsalerotations;

import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;

public class FobSaleRotationExporterExtensionFactory implements IExporterExtensionFactory {

	@Override
	public IExporterExtension createInstance() {
		return new FobSaleRotationExporterExtension();
	}

}
