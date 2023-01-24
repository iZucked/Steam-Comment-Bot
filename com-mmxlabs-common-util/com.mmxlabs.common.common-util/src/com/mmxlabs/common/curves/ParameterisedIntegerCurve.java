/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mmxlabs.common.Pair;

/**
 * An implementation of ICurve which consists of a bunch of intervals on the
 * x-axis with corresponding fixed values on the y-axis. Intervals are specified
 * using {@link ParameterisedIntegerCurve#setValueAfter(int, int)}, proceeding
 * from least x-value to greatest x-value.
 * 
 * @author hinton
 * 
 */
public class ParameterisedIntegerCurve implements IParameterisedCurve {

	private final Pair<Integer, TreeMap<Integer, Integer>> nonParameterised;
	private final LoadingCache<Map<String, String>, Pair<Integer, TreeMap<Integer, Integer>>> cache;
	private Set<String> parameterNames;

	public ParameterisedIntegerCurve(final Function<Map<String, String>, Pair<Integer, TreeMap<Integer, Integer>>> generator, Set<String> parameterNames) {

		this.parameterNames = parameterNames;

		if (parameterNames.isEmpty()) {
			this.nonParameterised = generator.apply(Collections.emptyMap());
			this.cache = null;
		} else {
			final CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder() //
					.maximumSize(2);

			this.cache = builder //
					.build(new CacheLoader<Map<String, String>, Pair<Integer, TreeMap<Integer, Integer>>>() {

						@Override
						public Pair<Integer, TreeMap<Integer, Integer>> load(final Map<String, String> key) throws Exception {
							return generator.apply(key);
						}
					});
			this.nonParameterised = null;
		}
	}

	@Override
	public boolean hasParameters() {
		return nonParameterised == null;
	}

	@Override
	public int getValueAtPoint(final int timePoint, final Map<String, String> params) {
		final Pair<Integer, TreeMap<Integer, Integer>> entry;
		if (nonParameterised != null) {
			entry = nonParameterised;
		} else {
			// Reduce the input map to just keys we are interested in.
			Map<String, String> subMap = params.entrySet().stream().filter(x -> parameterNames.contains(x.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			entry = cache.getUnchecked(subMap);
		}
		final Map.Entry<Integer, Integer> value = entry.getSecond().lowerEntry(timePoint + 1);
		return value == null ? entry.getFirst() : value.getValue().intValue();
	}
}
