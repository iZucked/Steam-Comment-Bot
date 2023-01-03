/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import java.io.IOException;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public interface ILingoReferenceVesselImportCommandProvider {
	public void run(final FleetModel fleetModel, final IMMXImportContext context) throws IOException;
}
