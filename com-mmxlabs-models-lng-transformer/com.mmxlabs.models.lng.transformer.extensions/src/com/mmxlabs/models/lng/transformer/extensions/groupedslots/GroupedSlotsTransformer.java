/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.groupedslots;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.providers.IGroupedSlotsConstraintDataProviderEditor;

public class GroupedSlotsTransformer implements ITransformerExtension {

	private LNGScenarioModel rootObject;

	@Inject
	private IGroupedSlotsConstraintDataProviderEditor groupedSlotsConstraintEditor;

	@Inject
	private ModelEntityMap modelEntityMap;

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
	}

	@Override
	public void finishTransforming() {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GROUPED_OPTIONAL_SLOTS_CONSTRAINTS)) {
			return;
		}
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(rootObject);
		for (final GroupedSlotsConstraint<SalesContract, DischargeSlot> eConstraint : cargoModel.getGroupedDischargeSlots()) {
			final List<IDischargeOption> oSlots = eConstraint.getSlots().stream().map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IDischargeOption.class)).toList();
			groupedSlotsConstraintEditor.addMinDischargeSlots(oSlots, eConstraint.getMinimumBound());
		}
	}
}
