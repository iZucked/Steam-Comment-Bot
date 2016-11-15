/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.BitSet;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexBits;
import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * Uses a BitSet to back
 * 
 * @author hinton
 * 
 */
public class ArrayIndexBits<T extends IIndexedObject> implements IIndexBits<T> {
	final BitSet bits = new BitSet();

	@Override
	public boolean isSet(final @NonNull T element) {
		return bits.get(element.getIndex());
	}

	@Override
	public void set(final @NonNull T element) {
		bits.set(element.getIndex());
	}

	@Override
	public void clear(final @NonNull T element) {
		bits.clear(element.getIndex());
	}
}
