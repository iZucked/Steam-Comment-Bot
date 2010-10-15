package com.mmxlabs.common.indexedobjects.impl;

import java.util.ArrayList;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * An arraylist which extends itself with null elements when you set beyond its current size.
 * @author hinton
 *
 * @param <E>
 */
public class AutoSizingArrayList<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public E set(int index, E element) {
		ensureCapacity(index+1);
		while (index >= size()) {
			add(null);
		}
		return super.set(index, element);
	}
	
	public E get(final IIndexedObject obj) {
		return get(obj.getIndex());
	}
	
	public E set(final IIndexedObject obj, final E value) {
		return set(obj.getIndex(), value);
	}
	
	public E maybeGet(final IIndexedObject obj) {
		final int ix = obj.getIndex();
		if (ix < size()) {
			return get(ix);
		} else {
			return null;
		}
	}
}
