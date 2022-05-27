/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;

/**
 * A Sequence that combines multiple availabilities that use the same vessel
 * 
 * @author achurchill
 *
 */
public class CombinedSequence {
	private List<Sequence> sequences = new LinkedList<>();
	private Vessel vessel;

	public CombinedSequence(Vessel vessel) {
		this.setVessel(vessel);
	}

	public List<Sequence> getSequences() {
		return sequences;
	}

	public void setSequences(List<Sequence> sequences) {
		this.sequences = sequences;
	}

	@Override
	public @NonNull String toString() {
		return getVessel() == null ? "<Unallocated>" : getVessel().getName();
	}

	public static List<CombinedSequence> createCombinedSequences(Collection<Sequence> sequences) {
		// find multiple charters
		Map<Vessel, List<Sequence>> vesselChartersMap = new HashMap<>();
		List<CombinedSequence> combinedSequences = new LinkedList<>();
		List<Sequence> unassigned = sequences.stream() //
				.filter(s -> s.getSequenceType() == SequenceType.VESSEL) //
				.filter(s -> s.getVesselCharter() != null) //
				.sorted((a, b) -> a.getVesselCharter().getStartBy() == null ? -1
						: b.getVesselCharter().getStartBy() == null ? 1 : a.getVesselCharter().getStartBy().compareTo(b.getVesselCharter().getStartBy()))
				.collect(Collectors.toList());
		while (!unassigned.isEmpty()) {
			@NonNull
			Sequence thisSequence = unassigned.get(0);
			List<Sequence> matches = unassigned.stream().filter(s -> s.getVesselCharter().getVessel().equals(thisSequence.getVesselCharter().getVessel())).collect(Collectors.toList());
			vesselChartersMap.put(thisSequence.getVesselCharter().getVessel(), matches);
			unassigned.removeAll(matches);
		}
		for (Vessel vessel : sequences.stream().map(s -> s.getVesselCharter() == null ? null : s.getVesselCharter().getVessel()).filter(Objects::nonNull).distinct().collect(Collectors.toList())) {
			List<Sequence> linkedSequences = vesselChartersMap.get(vessel);
			CombinedSequence cs = new CombinedSequence(vessel);
			cs.setSequences(linkedSequences);
			combinedSequences.add(cs);
		}
		return combinedSequences;
	}

	public Vessel getVessel() {
		return vessel;
	}

	public void setVessel(Vessel vessel) {
		this.vessel = vessel;
	}
}
