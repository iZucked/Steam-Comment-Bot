package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.Map;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A sequence manipulator which removes the dummy start location from spot charter vessels
 * @author hinton
 *
 * @param <T>
 */
public class StartLocationRemovingSequenceManipulator<T> implements
		ISequencesManipulator<T> {
	
	private IVesselProvider vesselProvider;
	
	@Override
	public void manipulate(IModifiableSequences<T> sequences) {
		if (vesselProvider == null) {
			throw new RuntimeException("Vessel Provider has not been set in StartLocationRemovingSequenceManipulator");
		}
		// Loop through each sequence in turn and manipulate
		for (final Map.Entry<IResource, IModifiableSequence<T>> entry : sequences
				.getModifiableSequences().entrySet()) {
			manipulate(entry.getKey(), entry.getValue());
		}
	}

	private void manipulate(IResource resource, IModifiableSequence<T> sequence) {
		if (vesselProvider.getVessel(resource).getVesselInstanceType().
				equals(VesselInstanceType.SPOT_CHARTER)) {
			// Remove start element
			sequence.remove(0);
		}
	}

	@Override
	public void dispose() {
		vesselProvider = null;
	}

	public IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public void setVesselProvider(IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

	public void setOptimisationData(IOptimisationData<T> data) {
		setVesselProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class));
	}
}
