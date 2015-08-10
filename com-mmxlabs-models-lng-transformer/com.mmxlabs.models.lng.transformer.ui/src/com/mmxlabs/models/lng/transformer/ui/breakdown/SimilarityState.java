package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class SimilarityState {

	@Inject
	private IPortTypeProvider portTypeProvider;

	private final Map<Integer, Integer> loadDischargeMap = new HashMap<>();
	private final Map<Integer, Integer> dischargeLoadMap = new HashMap<>();
	private final Map<Integer, ISequenceElement> elementMap = new HashMap<>();
	private final Map<Integer, IResource> elementIdxResourceMap = new HashMap<>();

	private final Map<Pair<Integer, Integer>, Pair<Integer, Integer>> cargoToPrevCargoMap = new HashMap<>();
	private final Map<Pair<Integer, Integer>, Pair<Integer, Integer>> cargoToNextCargoMap = new HashMap<>();

	public long[] baseMetrics = new long[MetricType.values().length];

	public void init(@NonNull final ISequences fullSequences) {
		for (final IResource resource : fullSequences.getResources()) {
			final ISequence sequence = fullSequences.getSequence(resource);
			ISequenceElement prev = null;
			Pair<Integer, Integer> prevCargo = new Pair<>(-2, -2); // start
			Pair<Integer, Integer> currCargo = new Pair<>(-1, -1); // end
			for (final ISequenceElement current : sequence) {
				if (elementMap.put(current.getIndex(), current) != null) {
					assert false;
				}
				if (prev != null) {
					if (portTypeProvider.getPortType(prev) == PortType.Load) {
						if (portTypeProvider.getPortType(current) == PortType.Discharge) {
							loadDischargeMap.put(prev.getIndex(), current.getIndex());
							dischargeLoadMap.put(current.getIndex(), prev.getIndex());
							currCargo = new Pair<>(prev.getIndex(), current.getIndex());
							cargoToNextCargoMap.put(prevCargo, currCargo);
							cargoToPrevCargoMap.put(currCargo, prevCargo);
							prevCargo = currCargo;
						}
					}
				}
				elementIdxResourceMap.put(current.getIndex(), resource);
				prev = current;
			}
			cargoToNextCargoMap.put(prevCargo, new Pair<>(-1, -1));
		}
		for (final ISequenceElement current : fullSequences.getUnusedElements()) {
			if (elementMap.put(current.getIndex(), current) != null) {
				assert false;
			}
		}
	}

	public Integer getLoadForDischarge(@NonNull final ISequenceElement discharge) {
		return dischargeLoadMap.get(discharge.getIndex());
	}

	public Integer getDischargeForLoad(@NonNull final ISequenceElement load) {
		return loadDischargeMap.get(load.getIndex());
	}

	public IResource getResourceForElement(@NonNull final ISequenceElement element) {
		return elementIdxResourceMap.get(element.getIndex());
	}

	public ISequenceElement getElementForIndex(final int index) {
		return elementMap.get(index);
	}

	public Pair<Integer, Integer> getPreviousCargo(final Pair<Integer, Integer> cargo) {
		return cargoToPrevCargoMap.get(cargo);
	}

	public Pair<Integer, Integer> getNextCargo(final Pair<Integer, Integer> cargo) {
		return cargoToNextCargoMap.get(cargo);
	}

}