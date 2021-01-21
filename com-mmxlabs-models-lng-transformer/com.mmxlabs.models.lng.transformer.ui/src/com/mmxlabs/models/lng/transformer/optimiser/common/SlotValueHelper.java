/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.common;

import java.util.Collections;
import java.util.Iterator;

import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.PortShipSizeConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculatorStep;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenIdleTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.MinMaxSlotGroupConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.MinMaxVolumeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;

public class SlotValueHelper {
	public static LoadDischargePairValueCalculatorStep createLoadDischargeCalculatorUnit(final LNGDataTransformer dataTransformer) {
		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
		// TODO: Filter
		final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
		while (iterator.hasNext()) {
			final Constraint constraint = iterator.next();
			if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(MinMaxSlotGroupConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(PortExclusionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(PortShipSizeConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
		}
		ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);
		ScenarioUtils.createOrUpdateContraints(MinMaxVolumeConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);
		ScenarioUtils.createOrUpdateContraints(LadenIdleTimeConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

		return new LoadDischargePairValueCalculatorStep(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(),
				constraintAndFitnessSettings, dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), Collections.emptyList());

	}

}
