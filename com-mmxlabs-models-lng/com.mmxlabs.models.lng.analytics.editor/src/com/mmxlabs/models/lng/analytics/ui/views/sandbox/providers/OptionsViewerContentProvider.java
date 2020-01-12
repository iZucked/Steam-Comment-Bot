/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers;

import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;

public class OptionsViewerContentProvider implements ITreeContentProvider {

	private final EReference feature;

	public OptionsViewerContentProvider(EReference feature) {
		this.feature = feature;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof AbstractAnalysisModel) {
			final AbstractAnalysisModel model = (AbstractAnalysisModel) inputElement;
			return ((List<?>) model.eGet(feature)).toArray();
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
		if (element instanceof AbstractAnalysisModel) {
			return true;
		}
		return false;
	}

}