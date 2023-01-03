/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
public class SimpleTabularReportContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof Collection<?> collection) {
			return collection.toArray();
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Collection<?> collection) {
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
