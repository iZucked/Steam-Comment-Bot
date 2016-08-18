/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

public abstract class MMXAdapterImpl extends AdapterImpl implements IMMXAdapter {
	private List<Notification> missedNotifications = new LinkedList<Notification>();
	
	/**
	 * @since 2.2
	 */
	protected final HashSet<EStructuralFeature> ignoredFeatures = new HashSet<EStructuralFeature>();
	
	public MMXAdapterImpl() {
	}
	
	@Override
	public void notifyChanged(final Notification notification) {
		if (ignoredFeatures.contains(notification.getFeature())) return;
		if (enabled) reallyNotifyChanged(notification);
		else missedNotifications.add(notification);
	}
	
	public abstract void reallyNotifyChanged(final Notification notification);
	
	
	/**
	 * @since 2.2
	 */
	protected void missedNotifications(final List<Notification> missed) {
		
	}
	
	boolean enabled = true;
	@Override
	public void disable() {
		missedNotifications.clear();
		enabled = false;
	}

	@Override
	public void enable() {
		enable(false);
	}
	
	/**
	 * @since 2.2
	 */
	public void enable(final boolean skip) {
		if (missedNotifications.isEmpty() == false) {
			if (!skip) missedNotifications(Collections.unmodifiableList(missedNotifications));
			missedNotifications.clear();
		}
		enabled = true;
	}
}
