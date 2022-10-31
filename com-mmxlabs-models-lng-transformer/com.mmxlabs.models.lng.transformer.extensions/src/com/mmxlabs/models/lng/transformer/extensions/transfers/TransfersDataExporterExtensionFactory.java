/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.transfers;

import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;

public class TransfersDataExporterExtensionFactory implements IExporterExtensionFactory {

	@Override
	public IExporterExtension createInstance() {
		return new TransfersDataExtensionExporter();
	}

}
