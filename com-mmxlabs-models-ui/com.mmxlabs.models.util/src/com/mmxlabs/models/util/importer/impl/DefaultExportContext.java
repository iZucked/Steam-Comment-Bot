/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IExportContext;

public class DefaultExportContext implements IExportContext {

	private final MMXRootObject rootObject;

	private final char decimalSeparator;

	public DefaultExportContext(final MMXRootObject rootObject, final char decimalSeparator) {
		this.rootObject = rootObject;
		this.decimalSeparator = decimalSeparator;

	}

	@Override
	public MMXRootObject getRootObject() {
		return rootObject;
	}

	@Override
	public char getDecimalSeparator() {
		return decimalSeparator;
	}
}
