package com.mmxlabs.models.ui.validation;

import org.eclipse.core.runtime.IStatus;

public interface IStatusProvider {

	public interface IStatusChangedListener {
		void onStatusChanged(IStatusProvider provider, IStatus status);
	}

	IStatus getStatus();

	void addStatusChangedListener(IStatusChangedListener l);

	void removeStatusChangedListener(IStatusChangedListener l);
}
