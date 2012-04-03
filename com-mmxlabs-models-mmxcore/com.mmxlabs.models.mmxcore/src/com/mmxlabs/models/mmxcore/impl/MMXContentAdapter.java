package com.mmxlabs.models.mmxcore.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

public abstract class MMXContentAdapter extends EContentAdapter implements IMMXAdapter {
	private boolean missed = false;
	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		if (enabled) reallyNotifyChanged(notification);
		else missed = true;
	}
	
	public abstract void reallyNotifyChanged(final Notification notification);

	protected void missedNotification() {
		
	}
	
	boolean enabled = true;
	@Override
	public void disable() {
		enabled = false;
		missed = false;
	}

	@Override
	public void enable() {
		if (missed) {
			missedNotification();
		}
		missed = false;
		enabled = true;
	}

}
