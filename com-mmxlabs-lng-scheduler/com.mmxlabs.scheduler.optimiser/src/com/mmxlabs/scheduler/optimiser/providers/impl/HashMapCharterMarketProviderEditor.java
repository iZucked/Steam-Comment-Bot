/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Triple;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProvider;

/**
 */
public class HashMapCharterMarketProviderEditor implements ICharterMarketProviderEditor {

	private static class DefaultCharterMarketOptions implements CharterMarketOptions {

		private final int dateKey;

		private final int minDuration;

		private final @NonNull Set<@NonNull IPort> allowedPorts;

		private final @NonNull ILongCurve priceCurve;

		public DefaultCharterMarketOptions(final int dateKey, final int minDuration, final @NonNull Set<@NonNull IPort> allowedPorts, final @NonNull ILongCurve priceCurve) {
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
		public @NonNull Set<@NonNull IPort> getAllowedPorts() {
			return this.allowedPorts;
		}
	}

	@Inject
	@NonNull
	private IDateKeyProvider dateKeyProvider;

	private final @NonNull Map<@NonNull IVesselClass, @NonNull List<@NonNull Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>>>> charterInOptions = new HashMap<>();
	private final @NonNull Map<@NonNull IVesselClass, @NonNull List<@NonNull Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>>>> charterOutOptions = new HashMap<>();

	private int charterOutStartTime = 0;

	private final @NonNull Map<@NonNull IVesselClass, @NonNull Set<@NonNull IPort>> charteringPortsForVesselClassMap = new HashMap<>();

	@Override
	public Collection<CharterMarketOptions> getCharterInOptions(final @NonNull IVesselClass vesselClass, final int time) {
		final int dateKey = dateKeyProvider.getDateKeyFromHours(time);
		return getOptions(charterInOptions, vesselClass, dateKey);
	}

	@Override
	public Collection<CharterMarketOptions> getCharterOutOptions(final @NonNull IVesselClass vesselClass, final int time) {
		final int dateKey = dateKeyProvider.getDateKeyFromHours(time);
		return getOptions(charterOutOptions, vesselClass, dateKey);
	}

	@Override
	public void addCharterInOption(final @NonNull IVesselClass vesselClass, final @NonNull ILongCurve curve) {
		addOptions(charterInOptions, vesselClass, curve, 0, Collections.emptySet());
	}

	@Override
	public void addCharterOutOption(final @NonNull IVesselClass vesselClass, final @NonNull ILongCurve curve, final int minDuration, final @NonNull Set<@NonNull IPort> allowedPorts) {
		addOptions(charterOutOptions, vesselClass, curve, minDuration, allowedPorts);
	}

	@NonNull
	private Collection<@NonNull CharterMarketOptions> getOptions(
			final @NonNull Map<@NonNull IVesselClass, @NonNull List<@NonNull Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>>>> options, final @NonNull IVesselClass vesselClass,
			final int dateKey) {

		if (options.containsKey(vesselClass)) {
			final List<@NonNull Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>>> entry = options.get(vesselClass);
			final List<@NonNull CharterMarketOptions> result = new ArrayList<>(entry.size());
			for (final Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>> p : entry) {
				final ILongCurve curve = p.getFirst();
				final int minDuration = p.getSecond();
				final Set<IPort> ports = p.getThird();
				result.add(new DefaultCharterMarketOptions(dateKey, minDuration, ports, curve));
			}
			return result;
		}

		return Collections.emptySet();
	}

	private void addOptions(final @NonNull Map<@NonNull IVesselClass, @NonNull List<@NonNull Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>>>> options,
			final @NonNull IVesselClass vesselClass, final @NonNull ILongCurve curve, final int minDuration, final @NonNull Set<@NonNull IPort> allowedPorts) {
		final @NonNull List<@NonNull Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>>> entry;
		final @NonNull Set<@NonNull IPort> vesselClassCharteringPorts;
		if (options.containsKey(vesselClass)) {
			entry = options.get(vesselClass);
			vesselClassCharteringPorts = charteringPortsForVesselClassMap.get(vesselClass);
		} else {
			entry = new LinkedList<>();
			options.put(vesselClass, entry);
			vesselClassCharteringPorts = new LinkedHashSet<>();
			charteringPortsForVesselClassMap.put(vesselClass, vesselClassCharteringPorts);
		}

		final @NonNull Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>> p = new Triple<>(curve, minDuration, allowedPorts);
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
