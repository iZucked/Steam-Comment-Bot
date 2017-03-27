/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

public class RouletteWheel<T> {

	private final LinkedHashMap<T, Double> wheel = new LinkedHashMap<>();

	public RouletteWheel(@NonNull final Collection<@NonNull T> validMoves, final Map<String, Double> distribution, final boolean equalDistributions) {

		final double divisions = validMoves.size();
		final double segment = 1 / divisions;

		final Iterator<@NonNull T> itr = validMoves.iterator();
		double currentTotal = 0.0;

		while (itr.hasNext()) {

			currentTotal += segment;
			if (equalDistributions) {
				wheel.put(itr.next(), currentTotal);
			} else {
				@NonNull
				final T element = itr.next();
				final double value = getCorrespondingDistribution(element.toString(), distribution);
				wheel.put(element, value);
			}
			itr.remove();
		}

	}

	public T spin(final Random random) {
		final double spinVal = random.nextDouble();
		for (final Entry<T, Double> entry : wheel.entrySet()) {
			if (spinVal < entry.getValue()) {
				return entry.getKey();
			}
		}
		return null; // Something has gone wrong;
	}

	public List<T> getMoves() {
		return new ArrayList<>(wheel.keySet());
	}

	public List<Double> getDistributions() {
		return new ArrayList<Double>(wheel.values());
	}

	public double getCorrespondingDistribution(final String entry, final Map<String, Double> distribution) {
		final String jsonName = entry.toLowerCase().replace("_", "-");
		final Object[] keys = distribution.keySet().toArray();

		for (int i = 0; i < keys.length; i++) {
			if (jsonName.equals(keys[i])) {
				final Double x = distribution.get(keys[i]);
				return x;
			}
		}
		return 0.0;
	}

}
