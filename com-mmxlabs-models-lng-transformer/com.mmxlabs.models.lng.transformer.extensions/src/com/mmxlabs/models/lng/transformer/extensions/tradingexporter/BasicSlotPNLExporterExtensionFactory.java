/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.tradingexporter;

import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;

public class BasicSlotPNLExporterExtensionFactory implements IExporterExtensionFactory {

	@Override
	public IExporterExtension createInstance() {
		return new BasicSlotPNLExporterExtension();
	}

}
