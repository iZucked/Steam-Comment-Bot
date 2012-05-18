/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public abstract class MMXContentAdapter extends EContentAdapter implements IMMXAdapter {
	private List<Notification> missedNotifications = new LinkedList<Notification>();
	/**
	 * @since 2.0
	 */
	protected final HashSet<EStructuralFeature> ignoredFeatures = new HashSet<EStructuralFeature>();
	
	public MMXContentAdapter() {
		ignoredFeatures.add(MMXCorePackage.eINSTANCE.getMMXObject_Proxies());
		ignoredFeatures.addAll(MMXCorePackage.eINSTANCE.getMMXProxy().getEStructuralFeatures());
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		if (ignoredFeatures.contains(notification.getFeature())) return;
		if (enabled) reallyNotifyChanged(notification);
		else missedNotifications.add(notification);
	}
	
	/**
	 * @since 2.0
	 */
	public abstract void reallyNotifyChanged(final Notification notification);

	/**
	 * @since 2.0
	 */
	protected void missedNotifications(final List<Notification> missed) {
		
	}
	
	boolean enabled = true;
	@Override
	public void disable() {
		enabled = false;
		missedNotifications.clear();
	}

	@Override
	public void enable() {
		if (missedNotifications.isEmpty() == false) {
			missedNotifications(Collections.unmodifiableList(missedNotifications));
			missedNotifications.clear();
		}
		enabled = true;
	}
}
