/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;

public class PartialCaseContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof OptionAnalysisModel model) {
			return model.getPartialCase().getGroups().stream().flatMap(g -> g.getRows().stream()).toArray();
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
