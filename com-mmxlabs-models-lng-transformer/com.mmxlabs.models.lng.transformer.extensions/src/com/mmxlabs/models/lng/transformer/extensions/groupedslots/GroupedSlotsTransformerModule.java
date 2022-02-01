/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.groupedslots;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.transformer.period.extensions.impl.GroupedSlotsPeriodTransformerExtension;
import com.mmxlabs.scheduler.optimiser.constraints.impl.GroupedSlotsConstraintCheckerFactory;

/**
 * This is a PeaberryActivationModule to hook in the grouped slots constraints extensions into the optimiser
 * 
 * 
 * @author Miten Mistry
 * 
 */
public class GroupedSlotsTransformerModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		bindService(GroupedSlotsTransformerFactory.class).export();
		bindService(GroupedSlotsConstraintCheckerFactory.class).export();
		bindService(GroupedSlotsPeriodTransformerExtension.class).export();
	}
}
