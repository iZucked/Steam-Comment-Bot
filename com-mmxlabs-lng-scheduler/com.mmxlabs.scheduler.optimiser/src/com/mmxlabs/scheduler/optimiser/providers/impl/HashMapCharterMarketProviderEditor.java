/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProvider;

/**
 */
public class HashMapCharterMarketProviderEditor implements ICharterMarketProviderEditor {

	private static class DefaultCharterMarketOptions implements CharterMarketOptions {

		private final int dateKey;

		private final int minDuration;

		private final Set<IPort> allowedPorts;

		private final ICurve priceCurve;

		public DefaultCharterMarketOptions(final int dateKey, final int minDuration, final Set<IPort> allowedPorts, final ICurve priceCurve) {
			this.dateKey = dateKey;
			this.minDuration = minDuration;
			this.allowedPorts = allowedPorts;
			this.priceCurve = priceCurve;
		}

		@Override
		public int getCharterPrice() {
			return getCharterPrice(dateKey);
		}

		@Override
		public int getCharterPrice(int date) {
			return (int) priceCurve.getValueAtPoint(date);
		}

		@Override
		public int getMinDuration() {
			return minDuration;
		}

		@Override
		public int getDateKey() {
			return dateKey;
		}

		@Override
		public Set<IPort> getAllowedPorts() {
			return this.allowedPorts;
		}
	}

	@Inject
	private IDateKeyProvider dateKeyProvider;

	private final Map<IVesselClass, List<Triple<ICurve, Integer, Set<IPort>>>> charterInOptions = new HashMap<IVesselClass, List<Triple<ICurve, Integer, Set<IPort>>>>();
	private final Map<IVesselClass, List<Triple<ICurve, Integer, Set<IPort>>>> charterOutOptions = new HashMap<IVesselClass, List<Triple<ICurve, Integer, Set<IPort>>>>();

	private int charterOutStartTime = 0;

	private final Map<IVesselClass, Set<IPort>> charteringPortsForVesselClassMap = new HashMap<IVesselClass, Set<IPort>>();

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
		addOptions(charterInOptions, vesselClass, curve, 0, null);
	}

	@Override
	public void addCharterOutOption(final IVesselClass vesselClass, final ICurve curve, final int minDuration, final Set<IPort> allowedPorts) {
		addOptions(charterOutOptions, vesselClass, curve, minDuration, allowedPorts);
	}

	private Collection<CharterMarketOptions> getOptions(final Map<IVesselClass, List<Triple<ICurve, Integer, Set<IPort>>>> options, final IVesselClass vesselClass, final int dateKey) {

		if (options.containsKey(vesselClass)) {
			final List<Triple<ICurve, Integer, Set<IPort>>> entry = options.get(vesselClass);
			final List<CharterMarketOptions> result = new ArrayList<CharterMarketOptions>(entry.size());
			for (final Triple<ICurve, Integer, Set<IPort>> p : entry) {
				final ICurve curve = p.getFirst();
				final int minDuration = p.getSecond();
				final Set<IPort> ports = p.getThird();
				result.add(new DefaultCharterMarketOptions(dateKey, minDuration, ports, curve));
			}
			return result;
		}

		return Collections.emptySet();
	}

	private void addOptions(final Map<IVesselClass, List<Triple<ICurve, Integer, Set<IPort>>>> options, final IVesselClass vesselClass, final ICurve curve, final int minDuration,
			final Set<IPort> allowedPorts) {
		final List<Triple<ICurve, Integer, Set<IPort>>> entry;
		final Set<IPort> vesselClassCharteringPorts;
		if (options.containsKey(vesselClass)) {
			entry = options.get(vesselClass);
			vesselClassCharteringPorts = charteringPortsForVesselClassMap.get(vesselClass);
		} else {
			entry = new LinkedList<Triple<ICurve, Integer, Set<IPort>>>();
			options.put(vesselClass, entry);
			vesselClassCharteringPorts = new LinkedHashSet<IPort>();
			charteringPortsForVesselClassMap.put(vesselClass, vesselClassCharteringPorts);
		}

		final Triple<ICurve, Integer, Set<IPort>> p = new Triple<ICurve, Integer, Set<IPort>>(curve, minDuration, allowedPorts);
		entry.add(p);
		vesselClassCharteringPorts.addAll(allowedPorts);
	}

	@Override
	public int getCharterOutStartTime() {
		return charterOutStartTime;
	}

	@Override
	public void setCharterOutStartTime(int startTime) {
		this.charterOutStartTime = startTime;
	}

	@Override
	public Set<IPort> getCharteringPortsForVesselClass(IVesselClass vesselClass) {
		if (charteringPortsForVesselClassMap.containsKey(vesselClass)) {
			return charteringPortsForVesselClassMap.get(vesselClass);
		}
		return new LinkedHashSet<IPort>();
	}
}
