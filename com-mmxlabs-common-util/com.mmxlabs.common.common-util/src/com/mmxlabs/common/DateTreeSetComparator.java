/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.Comparator;
import java.util.Date;

/**
 * A {@link Comparator} for use in the {@link DateTreeSet}. It is used to compare {@link Date} objects. However objects of the templated type will be converted to a {@link Date} using the
 * {@link ITransformer} passed in to the constructor. NOTE: It is assumed objects will be instance of {@link Date} or the template type. No checks are made to ensure this.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 */
public final class DateTreeSetComparator<T> implements Comparator<Object> {

	private final ITransformer<T, Date> transformer;

	public DateTreeSetComparator(final ITransformer<T, Date> transformer) {
		this.transformer = transformer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Object o1, final Object o2) {

		final Date d1 = toDate(o1);
		final Date d2 = toDate(o2);

		final int value = compare(d1, d2);

		if (value == 0) {
			final int h1 = System.identityHashCode(o1);
			final int h2 = System.identityHashCode(o2);
			if (h1 < h2) {
				return -1;
			} else if (h1 > h2) {
				return 1;
			} else {
				return 0;
			}
		}
		return value;
	}

	/**
	 * Converts the Object (assumed to be the template type or a {@link Date}) to a {@link Date} instance.
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Date toDate(final Object obj) {
		final Date d;
		if (obj instanceof Date) {
			d = (Date) obj;
		} else {
			d = transformer.transform((T) obj);
		}
		return d;
	}

	/**
	 * Perform the {@link Date} comparison.
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int compare(final Date d1, final Date d2) {

		if (d1 == null) {
			return 1;
		} else if (d2 == null) {
			return -1;
		} else {
			return d1.compareTo(d2);
		}
	}
}
