/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.ChainedSequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
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

		endLocationManipulator.setPortTypeProvider(portTypeProvider);
		endLocationManipulator.setReturnElementProvider(returnElementProvider);
		endLocationManipulator.setPortProvider(portProvider);

		/*
		 * Set end location rules; charter in vessels return to their first load port. Fleet vessels return to their last visited load port.
		 */
		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);

		for (final IResource resource : data.getResources()) {
			if (vesselProvider.getVessel(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
				startLocationRemover.setShouldRemoveStartLocation(resource, true);
				endLocationManipulator.setEndLocationRule(resource, EndLocationRule.RETURN_TO_FIRST_LOAD);
			} else {
				// Some fleet vessels will have an existing end location
				// requirement;
				// return to last load does not apply there
				// however, fleet vessels which do not have an end location requirement
				// should return to their last load port.

				if (!startEndProvider.getEndRequirement(resource).hasPortRequirement())
					endLocationManipulator.setEndLocationRule(resource, EndLocationRule.RETURN_TO_LAST_LOAD);
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
