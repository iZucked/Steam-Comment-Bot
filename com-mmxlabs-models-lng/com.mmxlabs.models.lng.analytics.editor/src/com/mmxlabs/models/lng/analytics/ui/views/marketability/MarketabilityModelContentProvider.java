/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;

public class MarketabilityModelContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof MarketabilityModel model) {
			return model.getRows().toArray();
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
		return element instanceof OptionAnalysisModel;
	}

}
