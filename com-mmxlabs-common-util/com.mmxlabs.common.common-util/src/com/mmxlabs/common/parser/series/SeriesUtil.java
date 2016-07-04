/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.Arrays;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class SeriesUtil {
	public static final int[] mergeChangePoints(final int[] a, final int[] b) {
		int ia = 0;
		int ib = 0;
		final int[] c = new int[a.length + b.length];
		int ic = 0;
		while (ia < a.length && ib < b.length) {
			if (a[ia] == b[ib]) {
				c[ic] = a[ia];
				ic++;
				ia++;
				ib++;
			} else if (a[ia] < b[ib]) {
				c[ic] = a[ia];
				ia++;
				ic++;
			} else if (b[ib] < a[ia]) {
				c[ic] = b[ib];
				ib++;
				ic++;
			}
		}

		while (ia < a.length) {
			c[ic++] = a[ia++];
		}

		while (ib < b.length) {
			c[ic++] = b[ib++];
		}

		return Arrays.copyOf(c, ic);
	}

	public static void main(final String[] args) {
		final SortedSet<Integer> s1 = new TreeSet<Integer>();
		final SortedSet<Integer> s2 = new TreeSet<Integer>();
		final SortedSet<Integer> s3 = new TreeSet<Integer>();

		final Random r = new Random();

		for (int i = 0; i < 20; i++) {
			final int r1 = r.nextInt(100);
			final int r2 = r.nextInt(100);
			s1.add(r1);
			s2.add(r2);
			s3.add(r1);
			s3.add(r2);
		}

		final int[] ia1 = new int[s1.size()];
		final int[] ia2 = new int[s2.size()];
		int i = 0;
		for (final int k : s1) {
			ia1[i++] = k;
		}
		i = 0;
		for (final int k : s2) {
			ia2[i++] = k;
		}

		final int[] cia = mergeChangePoints(ia1, ia2);
		System.err.println(Arrays.toString(ia1));
		System.err.println(Arrays.toString(ia2));
		System.err.println(Arrays.toString(cia));
	}

	public static int floor(final int[] points, final int point) {
		final int pos = Arrays.binarySearch(points, point);
		// If positive, then we have an exact match
		if (pos >= 0) {
			return pos;
		}
		// If negative, then convert to the index at which we would insert the item to keep sorted
		final int insertionIndex = (-pos) - 1;
		// Subtract one for floor
		final int pos2 = insertionIndex - 1;
		// Bounds checks
		if (pos2 < 0)
			return 0;
		if (pos2 >= points.length)
			return points.length - 1;
		return pos2;
	}

	public static String toString(final ISeries s) {
		final int[] points = s.getChangePoints();
		final StringBuffer sb = new StringBuffer();
		for (final int p : points) {
			sb.append(p);
			sb.append("=>");
			sb.append(s.evaluate(p));
			sb.append(" ");
		}
		return sb.toString();
	}
}
