/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.views;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * Wrapper around the tree iterator which only contains IJobDescriptors. We assume there is only a single type of object in the table
 */
class SelectedJobIterator implements Iterator<IJobDescriptor> {
	private final Iterator<?> selectionItr;

	private IJobDescriptor next = null;

	public SelectedJobIterator(final IStructuredSelection selection) {
		selectionItr = selection.iterator();
	}

	@Override
	public boolean hasNext() {
		next = null;
		while (selectionItr.hasNext() && (next == null)) {
			final Object obj = selectionItr.next();
			if (obj instanceof IJobDescriptor) {
				next = (IJobDescriptor) obj;
				return true;
			}
		}
		return false;
	}

	@Override
	public IJobDescriptor next() {
		// Assume null means end of list.
		if (next == null) {
			throw new NoSuchElementException();
		}

		final IJobDescriptor job = next;
		next = null;
		return job;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not implemeneted");
	}
}