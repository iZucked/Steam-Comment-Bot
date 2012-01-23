/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Represents a solution undergoing evaluation as a list of lists of voyageplans, start times, resources etc.
 * 
 * Also stores information about cargo allocations and load prices, once they have been filled in and updated.
 * 
 * @author hinton
 * 
 */
public final class ScheduledSequences extends ArrayList<ScheduledSequence>  {
	private static final long serialVersionUID = 1L;
	
	private Collection<IAllocationAnnotation> allocations = null;
	private final List<VirtualCargo> virtualCargoes = new LinkedList<VirtualCargo>();

	public final Collection<IAllocationAnnotation> getAllocations() {
		return allocations;
	}

	public final void setAllocations(Collection<IAllocationAnnotation> allocations) {
		this.allocations = allocations;
	}

	public void addScheduledSequence(final IResource resource, final int startTime, final List<VoyagePlan> voyagePlans) {
		add(new ScheduledSequence(resource, startTime, voyagePlans));
	}
	
	public List<VirtualCargo> getVirtualCargoes() {
		return Collections.unmodifiableList(virtualCargoes);
	}
	
	public void addVirtualCargo(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargeTime) {
		virtualCargoes.add(new VirtualCargo(loadOption, dischargeOption, loadTime, dischargeTime));
	}
}
