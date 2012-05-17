package com.mmxlabs.models.ui.validation.gui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Simple {@link ITreeContentProvider} for {@link IStatus} object hierarchies.
 * 
 * @author Simon Goodall
 * 
 */
public class ValidationStatusContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof IStatus) {
			final IStatus status = (IStatus) inputElement;
			if (status.isMultiStatus()) {
				return status.getChildren();
			} else {
				return new Object[] { status };
			}
		}

		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {

		return getElements(parentElement);
	}

	@Override
	public Object getParent(final Object element) {

		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof IStatus) {
			return ((IStatus) element).isMultiStatus();
		}

		return false;
	}
}
