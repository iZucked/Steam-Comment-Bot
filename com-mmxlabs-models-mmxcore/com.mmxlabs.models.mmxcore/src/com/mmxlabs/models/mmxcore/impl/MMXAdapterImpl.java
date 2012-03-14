package com.mmxlabs.models.mmxcore.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

public abstract class MMXAdapterImpl extends AdapterImpl implements IMMXAdapter {
	boolean enabled = true;
	@Override
	public void notifyChanged(final Notification notification) {
		if (enabled) reallyNotifyChanged(notification);
	}
	
	public abstract void reallyNotifyChanged(final Notification notification);

	@Override
	public void disable() {
		enabled = false;
	}

	@Override
	public void enable() {
		enabled = true;
	}
}
