/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IExportContext {

	public MMXRootObject getRootObject();

	public char getDecimalSeparator();
}
