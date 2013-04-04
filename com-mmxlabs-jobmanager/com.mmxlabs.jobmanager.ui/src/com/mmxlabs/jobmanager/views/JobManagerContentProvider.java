/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.views;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.manager.IJobManager;

public final class JobManagerContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Object[] getElements(final Object inputElement) {

		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(final Object parentElement) {

		if (parentElement instanceof IEclipseJobManager) {
			return ((IEclipseJobManager) parentElement).getJobManagers().toArray();
		}

		if (parentElement instanceof IJobManager) {
			return ((IJobManager) parentElement).getJobs().toArray();
		}

		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		if (parentElement instanceof Collection<?>) {
			return ((Collection<?>) parentElement).toArray();
		}

		return null;
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		return getChildren(element) != null;
	}
}
