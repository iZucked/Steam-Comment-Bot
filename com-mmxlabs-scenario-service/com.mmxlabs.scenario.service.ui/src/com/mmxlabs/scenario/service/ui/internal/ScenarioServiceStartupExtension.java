/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * A startup extension to register a {@link IPartListener} to track active editor selection. Note this is never de-registered. We use an {@link IStartup} rather than bundle activator as the bundle
 * could start before the workbench is active. Potentially this class should interact with the bundle activator to co-ordinate the listener creation and disposal.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioServiceStartupExtension implements IStartup {

	@Override
	public void earlyStartup() {

		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				final IWorkbench workbench = PlatformUI.getWorkbench();
				final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
				final ScenarioServicePartListener partListener = new ScenarioServicePartListener();
				activeWorkbenchWindow.getPartService().addPartListener(partListener);
				partListener.partActivated(activeWorkbenchWindow.getActivePage().getActiveEditor());
			}
		});
	}
}
