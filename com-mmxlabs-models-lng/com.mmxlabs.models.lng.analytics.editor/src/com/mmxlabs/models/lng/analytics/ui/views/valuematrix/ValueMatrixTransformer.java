package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;

public class ValueMatrixTransformer {
	public ITreeContentProvider createContentProvider() {
		return new ITreeContentProvider() {
			private SwapValueMatrixModel model = null;

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				model = null;
				if (newInput instanceof @NonNull SwapValueMatrixModel newModel) {
					model = newModel;
				}
			}

			@Override
			public void dispose() {
				model = null;
			}

			@Override
			public boolean hasChildren(Object element) {
				return false;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return new Object[] { inputElement };
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return null;
			}
		};
	}
}
