/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Triple;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProvider;

/**
 */
@NonNullByDefault
public class HashMapCharterMarketProviderEditor implements ICharterMarketProviderEditor {

	private static class DefaultCharterMarketOptions implements CharterMarketOptions {

		private final int dateKey;

		private final int minDuration;

		private final Set<@NonNull IPort> allowedPorts;

		private final ILongCurve priceCurve;

		private int maxDuration;

		public DefaultCharterMarketOptions(final int dateKey, final int minDuration, final int maxDuration, final Set<IPort> allowedPorts, final ILongCurve priceCurve) {
			this.dateKey = dateKey;
			this.minDuration = minDuration;
			this.maxDuration = maxDuration;
			this.allowedPorts = allowedPorts;
			this.priceCurve = priceCurve;
		}

		@Override
		public int getCharterPrice() {
			return getCharterPrice(dateKey);
		}

		@Override
		public int getCharterPrice(final int date) {
			return (int) priceCurve.getValueAtPoint(date);
		}

		@Override
		public int getMinDuration() {
			return minDuration;
		}

		@Override
		public int getMaxDuration() {
			return maxDuration;
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

	/**
	 * Inner class to avoid repetitive generics
	 *
	 */
	private static class CharterOutData {
		public ILongCurve curve;
		public int minDuration;
		public int maxDuration;
		public Set<IPort> ports;

		public CharterOutData(final ILongCurve curve, final int minDuration, final int maxDuration, final Set<@NonNull IPort> ports) {
			this.curve = curve;
			this.minDuration = minDuration;
			this.maxDuration = maxDuration;
			this.ports = ports;
		}

	}

	@Inject
	private IDateKeyProvider dateKeyProvider;

	private final Map<@NonNull IVessel, List<@NonNull CharterOutData>> charterInOptions = new HashMap<>();

	private final Map<@NonNull IVessel, List<@NonNull CharterOutData>> charterOutOptions = new HashMap<>();

	private int charterOutStartTime = 0;

	private int charterOutEndTime = 0;

	private final Map<@NonNull IVessel, @NonNull Set<@NonNull IPort>> charteringPortsForVesselMap = new HashMap<>();

	@Override
	public Collection<CharterMarketOptions> getCharterInOptions(final @NonNull IVessel vessel, final int time) {
		final int dateKey = dateKeyProvider.getDateKeyFromHours(time);
		return getOptions(charterInOptions, vessel, dateKey);
	}

	@Override
	public Collection<CharterMarketOptions> getCharterOutOptions(final @NonNull IVessel vessel, final int time) {
		final int dateKey = dateKeyProvider.getDateKeyFromHours(time);
		return getOptions(charterOutOptions, vessel, dateKey);
	}

	@Override
	public void addCharterInOption(final @NonNull IVessel vessel, final @NonNull ILongCurve curve) {
		addOptions(charterInOptions, vessel, curve, 0, Integer.MAX_VALUE, Collections.emptySet());
	}

	@Override
	public void addCharterOutOption(final @NonNull IVessel vessel, final @NonNull ILongCurve curve, final int minDuration, final int maxDuration, final @NonNull Set<@NonNull IPort> allowedPorts) {
		addOptions(charterOutOptions, vessel, curve, minDuration, maxDuration, allowedPorts);
	}

	private Collection<@NonNull CharterMarketOptions> getOptions(final Map<@NonNull IVessel, @NonNull List<@NonNull CharterOutData>> options, final IVessel vessel, final int dateKey) {

		if (options.containsKey(vessel)) {
			final List<@NonNull CharterOutData> entry = options.get(vessel);
			final List<@NonNull CharterMarketOptions> result = new ArrayList<>(entry.size());
			for (final CharterOutData p : entry) {
				final ILongCurve curve = p.curve;
				final int minDuration = p.minDuration;
				final int maxDuration = p.maxDuration;
				final Set<IPort> ports = p.ports;
				result.add(new DefaultCharterMarketOptions(dateKey, minDuration, maxDuration, ports, curve));
			}
			return result;
		}

		return Collections.emptySet();
	}

	private void addOptions(final Map<@NonNull IVessel, @NonNull List<@NonNull CharterOutData>> options, final IVessel vessel, final ILongCurve curve, final int minDuration,
			final int maxDuration, final Set<@NonNull IPort> allowedPorts) {
		final @NonNull List<@NonNull CharterOutData> entry;
		final @NonNull Set<@NonNull IPort> vesselCharteringPorts;
		if (options.containsKey(vessel)) {
			entry = options.get(vessel);
			vesselCharteringPorts = charteringPortsForVesselMap.get(vessel);
		} else {
			entry = new LinkedList<>();
			options.put(vessel, entry);
			vesselCharteringPorts = new LinkedHashSet<>();
			charteringPortsForVesselMap.put(vessel, vesselCharteringPorts);
		}

		final @NonNull CharterOutData p = new CharterOutData(curve, minDuration, maxDuration, allowedPorts);
		entry.add(p);
		vesselCharteringPorts.addAll(allowedPorts);
	}

	@Override
	public int getCharterOutStartTime() {
		return charterOutStartTime;
	}

	@Override
	public void setCharterOutStartTime(final int startTime) {
		this.charterOutStartTime = startTime;
	}

	@Override
	public void setCharterOutEndTime(final int endTime) {
		this.charterOutEndTime = endTime;
	}

	@Override
	public Set<IPort> getCharteringPortsForVessel(final IVessel vessel) {
		if (charteringPortsForVesselMap.containsKey(vessel)) {
			return charteringPortsForVesselMap.get(vessel);
		}
		return new LinkedHashSet<>();
	}

	@Override
	public int getCharterOutEndTime() {
		return charterOutEndTime;
	}
}
