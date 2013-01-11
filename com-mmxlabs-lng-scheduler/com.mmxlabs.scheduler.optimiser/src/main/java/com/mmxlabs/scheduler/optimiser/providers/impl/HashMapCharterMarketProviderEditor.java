/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProvider;

/**
 * @since 2.0
 */
public class HashMapCharterMarketProviderEditor implements ICharterMarketProviderEditor {

	private static class DefaultCharterMarketOptions implements CharterMarketOptions {

		private final int price;

		private final int dateKey;

		private final int minDuration;

		public DefaultCharterMarketOptions(final int dateKey, final int price, final int minDuration) {
			this.dateKey = dateKey;
			this.price = price;
			this.minDuration = minDuration;
		}

		@Override
		public int getCharterPrice() {
			return price;
		}

		@Override
		public int getMinDuration() {
			return minDuration;
		}

		@Override
		public int getDateKey() {
			return dateKey;
		}

	}

	@Inject
	private IDateKeyProvider dateKeyProvider;

	private final String name;

	private final Map<IVesselClass, List<Pair<ICurve, Integer>>> charterInOptions = new HashMap<IVesselClass, List<Pair<ICurve, Integer>>>();
	private final Map<IVesselClass, List<Pair<ICurve, Integer>>> charterOutOptions = new HashMap<IVesselClass, List<Pair<ICurve, Integer>>>();

	public HashMapCharterMarketProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	public Collection<CharterMarketOptions> getCharterInOptions(final IVesselClass vesselClass, final int time) {
		final int dateKey = dateKeyProvider.getDateKeyFromHours(time);
		return getOptions(charterInOptions, vesselClass, dateKey);
	}

	@Override
	public Collection<CharterMarketOptions> getCharterOutOptions(final IVesselClass vesselClass, final int time) {
		final int dateKey = dateKeyProvider.getDateKeyFromHours(time);
		return getOptions(charterOutOptions, vesselClass, dateKey);
	}

	@Override
	public void addCharterInOption(final IVesselClass vesselClass, final ICurve curve) {
		addOptions(charterInOptions, vesselClass, curve, 0);
	}

	@Override
	public void addCharterOutOption(final IVesselClass vesselClass, final ICurve curve, final int minDuration) {
		addOptions(charterOutOptions, vesselClass, curve, minDuration);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		charterInOptions.clear();
		charterOutOptions.clear();
	}

	private Collection<CharterMarketOptions> getOptions(final Map<IVesselClass, List<Pair<ICurve, Integer>>> options, final IVesselClass vesselClass, final int dateKey) {

		if (options.containsKey(vesselClass)) {
			final List<Pair<ICurve, Integer>> entry = options.get(vesselClass);
			final List<CharterMarketOptions> result = new ArrayList<CharterMarketOptions>(entry.size());
			for (final Pair<ICurve, Integer> p : entry) {
				final ICurve curve = p.getFirst();
				final int minDuration = p.getSecond();
				result.add(new DefaultCharterMarketOptions(dateKey, (int) curve.getValueAtPoint(dateKey), minDuration));
			}
			return result;
		}

		return Collections.emptySet();
	}

	private void addOptions(final Map<IVesselClass, List<Pair<ICurve, Integer>>> options, final IVesselClass vesselClass, final ICurve curve, final int minDuration) {
		final List<Pair<ICurve, Integer>> entry;
		if (options.containsKey(vesselClass)) {
			entry = options.get(vesselClass);
		} else {
			entry = new LinkedList<Pair<ICurve, Integer>>();
			options.put(vesselClass, entry);
		}

		final Pair<ICurve, Integer> p = new Pair<ICurve, Integer>(curve, minDuration);
		entry.add(p);
	}
}
