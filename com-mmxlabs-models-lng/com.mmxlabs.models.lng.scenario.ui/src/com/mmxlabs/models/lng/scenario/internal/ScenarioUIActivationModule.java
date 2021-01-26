/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.internal;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.scenario.importWizards.inventory.mull.MullRelativeEntitlementImporterCommand;

public class ScenarioUIActivationModule extends PeaberryActivationModule {
	@Override
	protected void configure() {
		bindService(MullRelativeEntitlementImporterCommand.class).export();
	}
}
