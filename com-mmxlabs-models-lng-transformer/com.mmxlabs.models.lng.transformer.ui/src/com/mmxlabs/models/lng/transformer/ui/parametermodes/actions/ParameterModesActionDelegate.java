/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;

public abstract class ParameterModesActionDelegate extends ActionDelegate implements IEditorActionDelegate /* , replaced with run() method IMenuCreator */ {
	protected IEditorPart editor;
	protected IAction action;

	protected IParameterModesRegistry registry;
	/**
	 * Cascading menu
	 */
	private Menu fCreatedMenu;

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		this.action = action;
	}

	@Override
	public void dispose() {
		action = null;
		editor = null;

		if (fCreatedMenu != null) {
			fCreatedMenu.dispose();
		}
	}

	@Override
	public abstract void run(final IAction action);

	protected IParameterModesRegistry getParameterModesRegistry() {
		final Bundle bundle = FrameworkUtil.getBundle(ParameterModesActionDelegate.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<IParameterModesRegistry> serviceReference = bundleContext.getServiceReference(IParameterModesRegistry.class);
		if (serviceReference != null) {
			return bundleContext.getService(serviceReference);
		}
		return null;
	}
}
