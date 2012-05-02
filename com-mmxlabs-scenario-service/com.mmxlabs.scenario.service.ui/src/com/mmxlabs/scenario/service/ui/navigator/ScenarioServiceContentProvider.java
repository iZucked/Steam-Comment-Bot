/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

public class ScenarioServiceContentProvider extends AdapterFactoryContentProvider {

	public ScenarioServiceContentProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
	}

	@Override
	public Object[] getElements(final Object object) {
		final Object[] elements = super.getElements(object);

		// Skip root node if there is only one item
		if (elements.length == 1 && super.getParent(object) == null) {
			return getChildren(elements[0]);
		}
		return elements;
	}

	@Override
	public void notifyChanged(Notification notification) {
		// TODO Auto-generated method stub
		super.notifyChanged(notification);
	}
	
	
}
