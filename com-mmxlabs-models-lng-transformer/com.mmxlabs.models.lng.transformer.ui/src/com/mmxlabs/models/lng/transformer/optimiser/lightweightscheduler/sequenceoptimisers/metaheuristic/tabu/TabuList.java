/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class TabuList {
	List<Set<Integer>> tabuList = new ArrayList<>(100);
	Set<Integer> tabuSet = new HashSet<>();

	
	public void addToTabu(Collection<Integer> cargoes) {
		if (cargoes.size() > 0) {
			tabuList.add(new HashSet<>(cargoes));
			tabuSet.addAll(cargoes);
		}
	}
	
	public void removeOldestEntry() {
		tabuSet.removeAll(tabuList.get(0));
		tabuList.remove(0);
	}
	
	Set<Integer> getTabuSet() {
		return ImmutableSet.copyOf(tabuSet);
	}
	
	public int sizeOfList() {
		return tabuList.size();
	}
}
