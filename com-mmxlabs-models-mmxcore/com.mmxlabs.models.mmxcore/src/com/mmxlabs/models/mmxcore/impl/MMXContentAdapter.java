package com.mmxlabs.models.mmxcore.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

public abstract class MMXContentAdapter extends EContentAdapter implements IMMXAdapter {
	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		if (enabled) reallyNotifyChanged(notification);
	}
	
	public abstract void reallyNotifyChanged(final Notification notification);

	boolean enabled = true;
	@Override
	public void disable() {
		enabled = false;
	}

	@Override
	public void enable() {
		enabled = true;
	}

}
