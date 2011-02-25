/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for setting up the normal sequence manipulators
 * @author hinton
 *
 */
public class SequencesManipulatorUtil {
	/**
	 * Create the default set of sequence manipulators for the given
	 * optimisation data
	 * @return
	 */
	public static <T> ChainedSequencesManipulator<T>
		createDefaultSequenceManipulators(final IOptimisationData<T> data) {
		
		/**
		 * A chained manipulator, because we need two sequence manipulators by default
		 */
		final ChainedSequencesManipulator<T> chainedManipulator = 
			new ChainedSequencesManipulator<T>();
		
		/**
		 * A start location removing manipulator, to take out spot charter dummy start locations
		 */
		final StartLocationRemovingSequenceManipulator<T> startLocationRemover =
			new StartLocationRemovingSequenceManipulator<T>();
		
		/**
		 * An end location sequence manipulator, to return spot charters to their first load port
		 */
		final EndLocationSequenceManipulator<T> endLocationManipulator = 
			new EndLocationSequenceManipulator<T>();
		
		/*
		 * Set the various DCPs. Some of these are editors, which is dodgy,
		 * but I can see no other simple way to do this. The alternative
		 * is some significant adjustment of the mechanism.
		 */

		@SuppressWarnings("unchecked")
		final IPortTypeProvider<T> portTypeProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portTypeProvider,
						IPortTypeProvider.class);
		
		final IPortProvider portProvider = data
				.getDataComponentProvider(SchedulerConstants.DCP_portProvider,
						IPortProvider.class);
		
		@SuppressWarnings("unchecked")
		final IReturnElementProvider<T> returnElementProvider = data
		.getDataComponentProvider(SchedulerConstants.DCP_returnElementProvider,
				IReturnElementProvider.class);
		
		
		endLocationManipulator.setPortTypeProvider(portTypeProvider);
		endLocationManipulator.setReturnElementProvider(returnElementProvider);
		endLocationManipulator.setPortProvider(portProvider);
		
		/*
		 * Set up manipulators for use with spot charter vessels only
		 */
		final IVesselProvider vesselProvider =
			data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);

		for (final IResource resource : data.getResources()) {
			if (vesselProvider.getVessel(resource).getVesselInstanceType()
					.equals(VesselInstanceType.SPOT_CHARTER)) {
				startLocationRemover.setShouldRemoveStartLocation(resource, true);
				endLocationManipulator.setEndLocationRule(resource, EndLocationRule.RETURN_TO_FIRST_LOAD);
			} else {
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
