/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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

		public DefaultCharterMarketOptions(final int dateKey, final int minDuration, final Set<IPort> allowedPorts, final ILongCurve priceCurve) {
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
		public int getCharterPrice(final int date) {
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

	/**
	 * Inner class to avoid repetitive generics
	 *
	 */
	private static class MyTriple extends Triple<@NonNull ILongCurve, @NonNull Integer, @NonNull Set<@NonNull IPort>> {

		public MyTriple(final ILongCurve a, final int b, final Set<@NonNull IPort> c) {
			super(a, b, c);
		}

	}

	@Inject
	private IDateKeyProvider dateKeyProvider;

	private final Map<@NonNull IVessel, List<@NonNull MyTriple>> charterInOptions = new HashMap<>();

	private final Map<@NonNull IVessel, List<@NonNull MyTriple>> charterOutOptions = new HashMap<>();

	private int charterOutStartTime = 0;

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
		addOptions(charterInOptions, vessel, curve, 0, Collections.emptySet());
	}

	@Override
	public void addCharterOutOption(final @NonNull IVessel vessel, final @NonNull ILongCurve curve, final int minDuration, final @NonNull Set<@NonNull IPort> allowedPorts) {
		addOptions(charterOutOptions, vessel, curve, minDuration, allowedPorts);
	}

	private Collection<@NonNull CharterMarketOptions> getOptions(final Map<@NonNull IVessel, @NonNull List<@NonNull MyTriple>> options, final IVessel vessel, final int dateKey) {

		if (options.containsKey(vessel)) {
			final List<@NonNull MyTriple> entry = options.get(vessel);
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

	private void addOptions(final Map<@NonNull IVessel, @NonNull List<@NonNull MyTriple>> options, final IVessel vessel, final ILongCurve curve, final int minDuration,
			final Set<@NonNull IPort> allowedPorts) {
		final @NonNull List<@NonNull MyTriple> entry;
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

		final @NonNull MyTriple p = new MyTriple(curve, minDuration, allowedPorts);
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
	public Set<IPort> getCharteringPortsForVessel(final IVessel vessel) {
		if (charteringPortsForVesselMap.containsKey(vessel)) {
			return charteringPortsForVesselMap.get(vessel);
		}
		return new LinkedHashSet<>();
	}
}
