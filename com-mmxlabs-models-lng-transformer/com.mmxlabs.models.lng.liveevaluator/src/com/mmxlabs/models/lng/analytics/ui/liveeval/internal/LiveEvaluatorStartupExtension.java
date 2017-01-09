/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval.internal;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.analytics.ui.liveeval.ILiveEvaluatorService;

public class LiveEvaluatorStartupExtension implements IStartup {

	@Override
	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final ICommandService service = (ICommandService) workbench.getService(ICommandService.class);

		final Command command = service.getCommand("com.mmxlabs.models.lng.analytics.editor.LiveEvaluatorEnabled");
		if (command == null) {
			return;
		}
		final State state = command.getState("org.eclipse.ui.commands.toggleState");

		if (state != null && state.getValue() instanceof Boolean) {
			final boolean enabled = (Boolean) state.getValue();

			final BundleContext bundleContext = Activator.getDefault().getBundle().getBundleContext();
			final ServiceReference<ILiveEvaluatorService> serviceReference = bundleContext.getServiceReference(ILiveEvaluatorService.class);

			if (serviceReference != null) {
				final ILiveEvaluatorService evaluatorService = bundleContext.getService(serviceReference);

				if (evaluatorService != null) {
					evaluatorService.setLiveEvaluatorEnabled(enabled);
				}
			}
		}
	}
}
