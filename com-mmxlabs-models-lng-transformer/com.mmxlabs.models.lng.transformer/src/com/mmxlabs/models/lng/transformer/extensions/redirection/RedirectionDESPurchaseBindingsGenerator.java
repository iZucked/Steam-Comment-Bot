/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.IDESPurchaseSlotBindingsGenerator;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class RedirectionDESPurchaseBindingsGenerator implements IDESPurchaseSlotBindingsGenerator {

	@Inject
	private ModelEntityMap map;

	private Set<IPort> dischargePorts = null;

	@Override
	public void bindDischargeSlotsToDESPurchase(ISchedulerBuilder builder, LoadSlot loadSlot, ILoadOption load) {

		if (loadSlot.isSetContract() && loadSlot.getContract().getPriceInfo() instanceof RedirectionPriceParameters) {
			// Redirection contracts can go to anywhere
			builder.bindDischargeSlotsToDESPurchase(load, getAllDischargePorts());
		} else {
			// Default LNG Transformer code ..
			final Set<IPort> ports = Collections.singleton(load.getPort());
			builder.bindDischargeSlotsToDESPurchase(load, ports);
		}
	}

	private Collection<IPort> getAllDischargePorts() {
		if (dischargePorts == null) {
			// Lazily build up the port data
			dischargePorts = new HashSet<IPort>();

			final Collection<Port> allModelObjects = map.getAllModelObjects(Port.class);
			for (final Port p : allModelObjects) {
				if (p.getCapabilities().contains(PortCapability.DISCHARGE)) {
					dischargePorts.add(map.getOptimiserObject(p, IPort.class));
				}
			}
		}
		return dischargePorts;

	}
}
