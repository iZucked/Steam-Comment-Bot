/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Added a {@link NavigableSet} implementation wrapping a {@link TreeSet} to mix the element type and {@link Date} objects. A {@link ITransformer} is used to convert the element type to a {@link Date}
 * object for sorting and querying. This class has additional {@link Date} methods as an alternative to query by element type.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Element type.
 */
@SuppressWarnings("unchecked")
public final class DateTreeSet<T> implements NavigableSet<T> {

	private final TreeSet<Object> treeSet;

	private final ITransformer<T, Date> transformer;

	private final DateTreeSetComparator<T> comparator;

	public DateTreeSet(final ITransformer<T, Date> transformer) {
		this.transformer = transformer;
		this.comparator = new DateTreeSetComparator<T>(this.transformer);
		this.treeSet = new TreeSet<Object>(comparator);
	}

	/**
	 * Copy constructor for {@link #clone()}. Clones internal {@link TreeSet}.
	 * 
	 * @param dateTreeSet
	 */
	private DateTreeSet(final DateTreeSet<T> dts) {
		this.transformer = dts.transformer;
		this.comparator = dts.comparator;
		// Clone the tree set to make an independent copy.
		this.treeSet = (TreeSet<Object>) dts.treeSet.clone();
	}

	@Override
	public T ceiling(final T arg0) {
		return (T) treeSet.ceiling(arg0);
	}

	public T ceiling(final Date date) {
		return (T) treeSet.ceiling(date);
	}

	@Override
	public Iterator<T> descendingIterator() {
		return (Iterator<T>) treeSet.descendingIterator();
	}

	@Override
	public NavigableSet<T> descendingSet() {
		return (NavigableSet<T>) treeSet.descendingSet();
	}

	@Override
	public T floor(final T e) {
		return (T) treeSet.floor(e);
	}

	public T floor(final Date date) {
		return (T) treeSet.floor(date);
	}

	@Override
	public SortedSet<T> headSet(final T toElement) {
		return (SortedSet<T>) treeSet.headSet(toElement);
	}

	public SortedSet<T> headSet(final Date toDate) {
		return (SortedSet<T>) treeSet.headSet(toDate);
	}

	@Override
	public NavigableSet<T> headSet(final T toElement, final boolean inclusive) {
		return (NavigableSet<T>) treeSet.headSet(toElement, inclusive);
	}

	public NavigableSet<T> headSet(final Date toDate, final boolean inclusive) {
		return (NavigableSet<T>) treeSet.headSet(toDate, inclusive);
	}

	@Override
	public T higher(final T e) {
		return (T) treeSet.higher(e);
	}

	public T higher(final Date date) {
		return (T) treeSet.higher(date);
	}

	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) treeSet.iterator();
	}

	@Override
	public T lower(final T e) {
		return (T) treeSet.lower(e);
	}

	public T lower(final Date date) {
		return (T) treeSet.lower(date);
	}

	@Override
	public T pollFirst() {
		return (T) treeSet.pollFirst();
	}

	@Override
	public T pollLast() {
		return (T) treeSet.pollLast();
	}

	@Override
	public SortedSet<T> subSet(final T fromElement, final T toElement) {
		return (SortedSet<T>) treeSet.subSet(fromElement, toElement);
	}

	public SortedSet<T> subSet(final Date fromDate, final Date toDate) {
		return (SortedSet<T>) treeSet.subSet(fromDate, toDate);
	}

	@Override
	public NavigableSet<T> subSet(final T fromElement, final boolean fromInclusive, final T toElement, final boolean toInclusive) {
		return (NavigableSet<T>) treeSet.subSet(fromElement, fromInclusive, toElement, toInclusive);
	}

	public NavigableSet<T> subSet(final Date fromDate, final boolean fromInclusive, final Date toDate, final boolean toInclusive) {
		return (NavigableSet<T>) treeSet.subSet(fromDate, fromInclusive, toDate, toInclusive);
	}

	@Override
	public SortedSet<T> tailSet(final T fromElement) {
		return (SortedSet<T>) treeSet.tailSet(fromElement);
	}

	public SortedSet<T> tailSet(final Date fromDate) {
		return (SortedSet<T>) treeSet.tailSet(fromDate);
	}

	@Override
	public NavigableSet<T> tailSet(final T fromElement, final boolean inclusive) {
		return (NavigableSet<T>) treeSet.tailSet(fromElement, inclusive);
	}

	public NavigableSet<T> tailSet(final Date fromDate, final boolean inclusive) {
		return (NavigableSet<T>) treeSet.tailSet(fromDate, inclusive);
	}

	@Override
	public Comparator<? super T> comparator() {
		return treeSet.comparator();
	}

	@Override
	public T first() {
		return (T) treeSet.first();
	}

	@Override
	public T last() {
		return (T) treeSet.last();
	}

	@Override
	public boolean add(final T arg0) {
		return treeSet.add(arg0);
	}

	@Override
	public boolean addAll(final Collection<? extends T> arg0) {
		return treeSet.addAll(arg0);
	}

	@Override
	public void clear() {
		treeSet.clear();
	}

	@Override
	public boolean contains(final Object arg0) {
		return treeSet.contains(arg0);
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		return treeSet.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return treeSet.isEmpty();
	}

	@Override
	public boolean remove(final Object arg0) {
		return treeSet.remove(arg0);
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		return treeSet.removeAll(arg0);
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		return treeSet.retainAll(arg0);
	}

	@Override
	public int size() {
		return treeSet.size();
	}

	@Override
	public Object[] toArray() {
		return treeSet.toArray();
	}

	@Override
	public <U> U[] toArray(final U[] arg0) {
		return treeSet.toArray(arg0);
	}

	@Override
	protected DateTreeSet<T> clone() throws CloneNotSupportedException {
		return new DateTreeSet<T>(this);
	}
}
