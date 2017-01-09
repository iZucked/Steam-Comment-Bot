/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;

import com.mmxlabs.models.ui.validation.IStatusProvider;

/**
 * 
 * @author Simon Goodall
 * 
 */
public abstract class DefaultStatusProvider implements IStatusProvider {
	private final Set<IStatusChangedListener> listeners = new HashSet<IStatusChangedListener>();

	@Override
	public void addStatusChangedListener(final IStatusChangedListener l) {
		listeners.add(l);

	}

	@Override
	public void removeStatusChangedListener(final IStatusChangedListener l) {
		listeners.remove(l);
	}

	public void fireStatusChanged(final IStatus status) {
		final List<IStatusChangedListener> copy = new ArrayList<IStatusChangedListener>(listeners);
		for (final IStatusChangedListener l : copy) {
			l.onStatusChanged(this, status);
		}
	}
}
