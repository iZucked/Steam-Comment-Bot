/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * Generic content provider for simple table views.
 * 
 * @author Simon McGregor
 * 
 */
public abstract class AbstractSimpleTabularReportContentProvider<T> implements ITreeContentProvider {

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) inputElement;
			return collection.toArray();
		}
		return new Object[0];
	}

	@Override
	public void dispose() {

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) parentElement;
			return collection.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof Collection<?>;
	}
}
