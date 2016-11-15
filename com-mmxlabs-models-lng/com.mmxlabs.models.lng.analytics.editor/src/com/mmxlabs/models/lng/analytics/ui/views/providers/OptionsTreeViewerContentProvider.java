package com.mmxlabs.models.lng.analytics.ui.views.providers;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;

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
		if (parentElement instanceof OptionAnalysisModel && ((OptionAnalysisModel) parentElement).getChildren() != null) {
			final OptionAnalysisModel model = (OptionAnalysisModel) parentElement;
			return model.getChildren().toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof OptionAnalysisModel) {
			if (((OptionAnalysisModel) element).getChildren().size() > 0) {
				return true;
			}
		}
		return false;
	}

}
