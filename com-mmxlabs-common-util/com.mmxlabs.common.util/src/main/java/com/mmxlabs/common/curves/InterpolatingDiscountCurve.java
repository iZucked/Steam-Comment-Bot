/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Might be a bit slow.
 * 
 * @author Tom Hinton
 * 
 */
public class InterpolatingDiscountCurve implements ICurve {
	final TreeMap<Integer, Double> values = new TreeMap<Integer, Double>();

	public void setValueAtPoint(final int time, final double discountValue) {
		values.put(time, discountValue);
	}

	@Override
	public double getValueAtPoint(final double point) {
		final Entry<Integer, Double> above = values.ceilingEntry((int) point);
		final Entry<Integer, Double> below = values.floorEntry((int) point);

		if (above == null) {
			if (below == null) {
				return 1;
			} else {
				return below.getValue();
			}
		} else if (below == null) {
			return above.getValue();
		} else {
			return interpolate(below, above, point);
		}

		// return 0;
	}

	/**
	 * @param below
	 * @param above
	 * @param point
	 * @return
	 */
	private double interpolate(final Entry<Integer, Double> below, final Entry<Integer, Double> above, final double point) {
		if (below == above) {
			return below.getValue();
		}
		final double d = above.getKey() - below.getKey();
		return below.getValue() + (((above.getValue() - below.getValue()) * (above.getKey() - point)) / d);
	}

}
