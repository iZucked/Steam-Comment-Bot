/**
 * All rights reserved.
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 */
package com.mmxlabs.models.lng.transformer.extensions.tradingexporter;

import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;

/**
 * @since 3.0
 */
public class TradingExporterExtensionFactory implements IExporterExtensionFactory {

	@Override
	public IExporterExtension createInstance() {
		return new TradingExporterExtension();
	}

}
