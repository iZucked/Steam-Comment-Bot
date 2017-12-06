/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.providers;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.Result;
import com.mmxlabs.models.lng.analytics.ResultSet;

public class ResultsViewerContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof OptionAnalysisModel) {
			final OptionAnalysisModel model = (OptionAnalysisModel) inputElement;
			Result result = model.getResults();
			if (result != null) {
				return result.getResultSets().toArray();
			}
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof ResultSet) {
			final ResultSet model = (ResultSet) parentElement;
			return model.getRows().toArray();
		}
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
		if (element instanceof ResultSet) {
			return true;
		}
		return false;
	}

}
