/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.editor;

import java.util.BitSet;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import com.mmxlabs.common.Pair;

/**
 * A helper class for maintaining a stack of dates, used for rendering in the {@link AssignmentEditor}.
 * 
 * The two useful methods are {@link #addRange(Date, Date, Object)} and {@link #removeRange(Object)}
 * 
 * @author hinton
 *
 */
public class DateRangeTracker {
	boolean collapseStacks = false;
	
	int rangeCounter = 0;
	private class Range {
		final Integer count;
		public final Date start;
		public final Date end;
		public final Object value;
		public int currentDepth = 0;
		public Range(Date first, Date second, Object value) {
			super();
			this.start = first;
			this.end = second;
			this.value = value;
			count = rangeCounter++;
		}
		
		public boolean overlaps(final Range two) {
			return !(end.before(two.start) || two.end.before(start));
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((end == null) ? 0 : end.hashCode());
			result = prime * result + ((start == null) ? 0 : start.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return "(" + start + ", " + end + ", " + value + ")";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Range other = (Range) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (end == null) {
				if (other.end != null)
					return false;
			} else if (!end.equals(other.end))
				return false;
			if (start == null) {
				if (other.start != null)
					return false;
			} else if (!start.equals(other.start))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		private DateRangeTracker getOuterType() {
			return DateRangeTracker.this;
		}
	}
	
	private HashMap<Object, Range> rangeByValue = new HashMap<Object, Range>();
	
	private TreeSet<Range> contentsByStartDate = new TreeSet<Range>(
			new Comparator<Range>() {

				@Override
				public int compare(Range o1, Range o2) {
					final int compare = o1.start.compareTo(o2.start);
					if (compare == 0) {
						return o1.count.compareTo(o2.count);
					}
					return compare;
				}
				
			}
			);
	private TreeSet<Range> contentsByEndDate = new TreeSet<Range>(
			new Comparator<Range>() {
				@Override
				public int compare(Range o1, Range o2) {
					final int compare = o1.end.compareTo(o2.end);
					if (compare == 0) {
						return o1.count.compareTo(o2.count);
					}
					return compare;
				}
			});
	
	public DateRangeTracker(boolean collapse) {
		this.collapseStacks = collapse;
	}

	/**
	 * Add an object with an associated range, and return the number of things previously added
	 * which overlapped with that range.
	 * 
	 * @param from
	 * @param to
	 * @param value
	 * @return
	 */
	public int addRange(final Date from, final Date to, final Object value) {
		final Range range = new Range(from, to, value);
		
		final int depth = getCurrentDepth(range);
		contentsByEndDate.add(range);
		contentsByStartDate.add(range);
		
		rangeByValue.put(value, range);
		
		return depth;
	}

	/**
	 * Remove a previously added range by its associated value (the third argument to {@link #addRange(Date, Date, Object)}
	 * @param value
	 */
	public void removeRange(final Object value) {
		final Range range = rangeByValue.get(value);
		if (range != null) {
			contentsByEndDate.remove(range);
			contentsByStartDate.remove(range);
			rangeByValue.remove(value);
		}
	}
	
	/**
	 * Returns the number of ranges previously passed to {@link #addRange(Pair)} which overlap the given range.
	 * @param element
	 * @return
	 */
	private int getCurrentDepth(final Range element) {
		// find elements which could matter; nothing which ends before from or starts after to can matter.
		final HashSet<Range> test = new HashSet<Range>(
				// get things which start before to, inclusively
				contentsByStartDate.headSet(new Range(new Date(element.end.getTime() + 1), null, null), true));
		
		// remove everything whose end is before our start date
		test.removeAll(contentsByEndDate.headSet(new Range(null, new Date(element.start.getTime() - 1), null), true));
		
		if (collapseStacks) {
			final BitSet occupied = new BitSet();
			// sum up overlaps

			for (final Range candidate : test) {
				if (element.overlaps(candidate)) {
					occupied.set(candidate.currentDepth);
				}
			}

			for (int n = 0; n < occupied.size(); n++) {
				if (occupied.get(n) == false) {
					element.currentDepth = n;
					return n;
				}
			}
			element.currentDepth = occupied.size() + 1;
			return element.currentDepth;
		} else {
			int maxDepth = 0;
			for (final Range candidate : test) {
				if (element.overlaps(candidate)) {
					maxDepth = Math.max(maxDepth, candidate.currentDepth);
				}
			}
			element.currentDepth = maxDepth + 1;
			return element.currentDepth - 1;
		}
	}
}
