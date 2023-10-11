/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.errordialog;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class SandboxDefineErrorContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof Collection<?> c) {
			return c.toArray();
		} else if (inputElement instanceof SolutionErrorSet s) {
			return s.errors().toArray();
		}

		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof Collection<?> c) {
			return c.toArray();
		} else if (parentElement instanceof SolutionErrorSet s) {
			return s.errors().toArray();
		}

		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		return (element instanceof Collection<?> || element instanceof SolutionErrorSet);
	}
}
