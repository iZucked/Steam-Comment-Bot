package com.mmxlabs.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

public class RouletteWheel<T> {

	private final LinkedHashMap<T, Double> wheel = new LinkedHashMap<>();

	public RouletteWheel(@NonNull final Collection<@NonNull T> validMoves, final Map<String, Double> distribution, final boolean equalDistributions) {

		final double divisions = validMoves.size();
		final double segment = 1 / divisions;

		final Iterator<@NonNull T> itr = validMoves.iterator();
		double currentTotal = 0.0;
		int curInd = 0;

		while (itr.hasNext()) {

			currentTotal += segment;
			if (equalDistributions) {
				wheel.put(itr.next(), currentTotal);
			} else {
				@NonNull
				T element = itr.next();
				double value = getCorrespondingDistribution(element.toString(), distribution);
				wheel.put(element, value);
				curInd++;
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

	public double getCorrespondingDistribution(String entry, Map<String, Double> distribution) {
		String jsonName = entry.toLowerCase().replace("_", "-");
		Object[] keys = distribution.keySet().toArray();

		for (int i = 0; i < keys.length; i++) {
			if (jsonName.equals(keys[i])) {
				Double x = distribution.get(keys[i]);
				return x;
			}
		}
		return 0.0;
	}

}
