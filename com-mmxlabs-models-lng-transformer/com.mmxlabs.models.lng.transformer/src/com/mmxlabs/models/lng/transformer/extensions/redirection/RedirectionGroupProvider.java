/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * Small storage class used to pass information between the {@link RedirectionContractTransformer} and the {@link RedirectionPostExportProcessor} and {@link RedirectionTravelConstraintChecker}
 * 
 * @author Simon Goodall
 * 
 */
public class RedirectionGroupProvider {

	private final Map<Slot, Collection<Slot>> redirectionGroups = new HashMap<>();
	private final Map<IPortSlot, Integer> fobLoadTime = new HashMap<>();
	private final Map<IPortSlot, Integer> shippingHours = new HashMap<>();

	@Inject
	private ModelEntityMap modelEntityMap;

	/**
	 * Define a redirection contract group - a set of exchangeable slots, the original FOB load time and the limit on the number of hours travel&idle time between load port and discharge.
	 * 
	 * @param group
	 * @param fobLoadTime
	 * @param shippingHours
	 */
	public void createRedirectionGroup(@NonNull final Collection<Slot> group, final int fobLoadTime, final int shippingHours) {
		for (final Slot slot : group) {
			this.redirectionGroups.put(slot, group);
			this.fobLoadTime.put(modelEntityMap.getOptimiserObject(slot, IPortSlot.class), fobLoadTime);
			this.shippingHours.put(modelEntityMap.getOptimiserObject(slot, IPortSlot.class), shippingHours);
		}
	}

	public @Nullable
	Collection<Slot> getRedirectionGroup(@NonNull final Slot slot) {
		return redirectionGroups.get(slot);
	}

	public @Nullable
	Integer getFOBLoadTime(@NonNull final IPortSlot slot) {
		return fobLoadTime.get(slot);
	}

	public @Nullable
	Integer getShippingHours(@NonNull final IPortSlot slot) {
		return shippingHours.get(slot);
	}
}
