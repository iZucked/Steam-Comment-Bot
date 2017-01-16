/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;

/**
 * Small storage class used to pass information between the {@link RedirectionContractTransformer} and the {@link RedirectionPostExportProcessor}
 * 
 * @author Simon Goodall
 * 
 */
public class RedirectionGroupProvider {

	private final Map<Slot, Collection<Slot>> redirectionGroups = new HashMap<>();

	/**
	 * Define a redirection contract group - a set of exchangeable slots.
	 * 
	 * @param group
	 * @param fobLoadTime
	 * @param shippingHours
	 */
	public void createRedirectionGroup(@NonNull final Collection<Slot> group) {
		for (final Slot slot : group) {
			this.redirectionGroups.put(slot, group);
		}
	}

	public @Nullable
	Collection<Slot> getRedirectionGroup(@NonNull final Slot slot) {
		return redirectionGroups.get(slot);
	}
}
