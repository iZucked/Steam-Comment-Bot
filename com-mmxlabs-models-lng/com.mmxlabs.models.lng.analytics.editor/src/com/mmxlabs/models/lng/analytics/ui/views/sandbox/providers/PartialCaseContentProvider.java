/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;

public class PartialCaseContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof OptionAnalysisModel) {
			final OptionAnalysisModel model = (OptionAnalysisModel) inputElement;
			return model.getPartialCase().getPartialCase().toArray();
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
		if (element instanceof OptionAnalysisModel) {
			return true;
		}
		return false;
	}

}
