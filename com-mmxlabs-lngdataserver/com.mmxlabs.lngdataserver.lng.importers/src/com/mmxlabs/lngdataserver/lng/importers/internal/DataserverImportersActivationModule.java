/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.internal;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.lngdataserver.lng.importers.vesselsimport.VesselImporterCommand;

public class DataserverImportersActivationModule extends PeaberryActivationModule {
	@Override
	protected void configure() {
		bindService(VesselImporterCommand.class).export();
//		bindService(LingoReferenceVesselImportCommand.class).export();
	}
}