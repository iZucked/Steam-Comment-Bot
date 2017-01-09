/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	final TreeMap<Integer, Integer> values = new TreeMap<Integer, Integer>();

	public void setValueAtPoint(final int time, final int discountValue) {
		values.put(time, discountValue);
	}

	@Override
	public int getValueAtPoint(final int point) {
		final Entry<Integer, Integer> above = values.ceilingEntry(point);
		final Entry<Integer, Integer> below = values.floorEntry(point);

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
	private int interpolate(final Entry<Integer, Integer> below, final Entry<Integer, Integer> above, final int point) {
		if (below == above) {
			return below.getValue();
		}
		final int d = above.getKey() - below.getKey();
		return below.getValue() + (((above.getValue() - below.getValue()) * (above.getKey() - point)) / d);
	}

}
