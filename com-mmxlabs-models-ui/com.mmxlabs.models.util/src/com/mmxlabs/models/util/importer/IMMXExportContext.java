/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IMMXExportContext extends IExportContext {

	MMXRootObject getRootObject();
}
