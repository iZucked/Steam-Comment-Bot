/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;

/**
 * Class to provide labels for the elements in the scenario selector tree.
 * 
 * @author Simon McGregor
 * 
 */
public class ScenarioLabelProvider implements ILabelProvider {
	@Override
	public void addListener(final ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	@Override
	public void removeListener(final ILabelProviderListener listener) {
	}

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof Folder) {
			return ((Folder) element).getName();
		}
		if (element instanceof ScenarioInstance) {
			return ((ScenarioInstance) element).getName();
		}
		if (element instanceof ScenarioService) {
			return ((ScenarioService) element).getName();
		}
		if (element instanceof NamedObject) {
			return ((NamedObject) element).getName();
		}
		return element.toString();
	}

}