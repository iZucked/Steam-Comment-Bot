/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.ChainedSequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator.EndLocationRule;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for setting up the normal sequence manipulators
 * 
 * @author hinton
 * 
 */
public class SequencesManipulatorUtil {
	/**
	 * Create the default set of sequence manipulators for the given optimisation data
	 * 
	 * @return
	 */
	public static ChainedSequencesManipulator createDefaultSequenceManipulators(final IOptimisationData data) {

		/**
		 * A chained manipulator, because we need two sequence manipulators by default
		 */
		final ChainedSequencesManipulator chainedManipulator = new ChainedSequencesManipulator();

		/**
		 * A start location removing manipulator, to take out spot charter dummy start locations
		 */
		final StartLocationRemovingSequenceManipulator startLocationRemover = new StartLocationRemovingSequenceManipulator();

		/**
		 * An end location sequence manipulator, to return spot charters to their first load port
		 */
		final EndLocationSequenceManipulator endLocationManipulator = new EndLocationSequenceManipulator();

		/*
		 * Set the various DCPs. Some of these are editors, which is dodgy, but I can see no other simple way to do this. The alternative is some significant adjustment of the mechanism.
		 */

		final IPortTypeProvider portTypeProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);

		final IPortProvider portProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portProvider, IPortProvider.class);

		final IReturnElementProvider returnElementProvider = data.getDataComponentProvider(SchedulerConstants.DCP_returnElementProvider, IReturnElementProvider.class);

		final IStartEndRequirementProvider startEndProvider = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);
		@SuppressWarnings("unchecked")
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, IMultiMatrixProvider.class);

		endLocationManipulator.setPortTypeProvider(portTypeProvider);
		endLocationManipulator.setReturnElementProvider(returnElementProvider);
		endLocationManipulator.setPortProvider(portProvider);
		endLocationManipulator.setStartEndRequirementProvider(startEndProvider);
		endLocationManipulator.setDistanceProvider(distanceProvider);

		/*
		 * Set end location rules; charter in vessels return to their first load port. Fleet vessels return to their last visited load port.
		 */
		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);

		for (final IResource resource : data.getResources()) {
			final VesselInstanceType vesselInstanceType = vesselProvider.getVessel(resource).getVesselInstanceType();
			if (vesselInstanceType == VesselInstanceType.DES_PURCHASE || vesselInstanceType == VesselInstanceType.FOB_SALE) {
				endLocationManipulator.setEndLocationRule(resource, EndLocationRule.NONE);
			} else if (vesselInstanceType == VesselInstanceType.CARGO_SHORTS) {
				endLocationManipulator.setEndLocationRule(resource, EndLocationRule.NONE);
				startLocationRemover.setShouldRemoveStartLocation(resource, true);
			} else if (vesselInstanceType.equals(VesselInstanceType.SPOT_CHARTER)) {
				startLocationRemover.setShouldRemoveStartLocation(resource, true);
				endLocationManipulator.setEndLocationRule(resource, EndLocationRule.RETURN_TO_FIRST_LOAD);
			} else {
				// Some fleet vessels will have an existing end location
				// requirement;
				// return to last load does not apply there
				// however, fleet vessels which do not have an end location requirement
				// should return to their last load port.

				final IStartEndRequirement endRequirement = startEndProvider.getEndRequirement(resource);
				if (!endRequirement.hasPortRequirement()) {
					endLocationManipulator.setEndLocationRule(resource, EndLocationRule.RETURN_TO_LAST_LOAD);
				} else if (endRequirement.hasPortRequirement() && endRequirement.getLocation() == null) {
					endLocationManipulator.setEndLocationRule(resource, EndLocationRule.RETURN_TO_CLOSEST_IN_SET);
				}
			}
		}

		/*
		 * Add them to the chained manipulator.
		 */
		chainedManipulator.addDelegate(startLocationRemover);
		chainedManipulator.addDelegate(endLocationManipulator);

		return chainedManipulator;
	}
}
