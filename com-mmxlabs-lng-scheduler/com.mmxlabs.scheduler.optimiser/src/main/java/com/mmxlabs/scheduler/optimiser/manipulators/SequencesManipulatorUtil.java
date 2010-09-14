package com.mmxlabs.scheduler.optimiser.manipulators;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.ChainedSequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator.EndLocationRule;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for setting up the normal sequence manipulators
 * @author hinton
 *
 */
public class SequencesManipulatorUtil {
	/**
	 * Create the default set of sequence manipulators for the given
	 * optimisation data. This cannot be type parameterised, because the 
	 * dummy sequence element constructor has to be made concrete somewhere
	 * @return
	 */
	public static ChainedSequencesManipulator<ISequenceElement>
		createDefaultSequenceManipulators(final IOptimisationData<ISequenceElement> data) {
		
		/**
		 * A chained manipulator, because we need two sequence manipulators by default
		 */
		ChainedSequencesManipulator<ISequenceElement> chainedManipulator = 
			new ChainedSequencesManipulator<ISequenceElement>();
		
		/**
		 * A start location removing manipulator, to take out spot charter dummy start locations
		 */
		StartLocationRemovingSequenceManipulator<ISequenceElement> startLocationRemover =
			new StartLocationRemovingSequenceManipulator<ISequenceElement>();
		
		/**
		 * An end location sequence manipulator, to return spot charters to their first load port
		 */
		EndLocationSequenceManipulator<ISequenceElement> endLocationManipulator = 
			new EndLocationSequenceManipulator<ISequenceElement>(
					new EndLocationSequenceManipulator.IElementFactory<ISequenceElement>() {
						@Override
						public ISequenceElement createElement() {
							return new SequenceElement();
						}
					});
		
		/*
		 * Set the various DCPs. Some of these are editors, which is dodgy,
		 * but I can see no other simple way to do this. The alternative
		 * is some significant adjustment of the mechanism.
		 */

		@SuppressWarnings("unchecked")
		final IPortTypeProvider<ISequenceElement> portTypeProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portTypeProvider,
						IPortTypeProvider.class);
		final IPortProviderEditor portProviderEditor = data
				.getDataComponentProvider(SchedulerConstants.DCP_portProvider,
						IPortProviderEditor.class);

		@SuppressWarnings("unchecked")
		final IPortSlotProviderEditor<ISequenceElement> portSlotProviderEditor = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProviderEditor.class);

		endLocationManipulator.setPortSlotProviderEditor(portSlotProviderEditor);
		endLocationManipulator.setPortTypeProvider(portTypeProvider);
		endLocationManipulator.setPortProviderEditor(portProviderEditor);
		
		/*
		 * Set up manipulators for use with spot charter vessels only
		 */
		final IVesselProvider vesselProvider =
			data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);

		for (IResource resource : data.getResources()) {
			if (vesselProvider.getVessel(resource).getVesselInstanceType()
					.equals(VesselInstanceType.SPOT_CHARTER)) {
				startLocationRemover.setShouldRemoveStartLocation(resource, true);
				endLocationManipulator.setEndLocationRule(resource, EndLocationRule.RETURN_TO_FIRST_LOAD);
			}
		}
		
		/*
		 * Add them to the chained manipulator. 
		 */
		chainedManipulator.addDelegate(startLocationRemover);
//		chainedManipulator.addDelegate(endLocationManipulator);
		
		return chainedManipulator;
	}
}
