/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class OptionsTreeViewerContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof Collection) {
			Collection collection = (Collection) inputElement;
			return collection.toArray();
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		return false;
	}

}
