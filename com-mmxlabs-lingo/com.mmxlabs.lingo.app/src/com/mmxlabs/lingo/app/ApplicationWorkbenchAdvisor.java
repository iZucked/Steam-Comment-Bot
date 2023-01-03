/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.mmxlabs.lingo.app.internal.Activator;
import com.mmxlabs.lingo.reports.customizable.CustomReportsRegistry;
import com.mmxlabs.rcp.common.application.DelayedOpenFileProcessor;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceSaveHook;

@SuppressWarnings("restriction")
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private final DelayedOpenFileProcessor delayedOpenFileProcessor;

	private static final String PERSPECTIVE_ID = "com.mmxlabs.lingo.app.perspective.analysis";

	/**
	 */
	public ApplicationWorkbenchAdvisor(final DelayedOpenFileProcessor processor) {
		super();
		this.delayedOpenFileProcessor = processor;
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void initialize(final IWorkbenchConfigurer configurer) {
		super.initialize(configurer);

		configurer.setSaveAndRestore(true);

		// register shared images
		declareWorkbenchImages();

	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public void preStartup() {

		Job.getJobManager().suspend();
	}

	@Override
	public IAdaptable getDefaultPageInput() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	@Override
	public void postStartup() {

		// Resume background jobs after we startup
		Job.getJobManager().resume();
	}

	@Override
	public boolean preShutdown() {

		// Hook into our save call back before passing on to the platform.
		boolean ret = ScenarioServiceSaveHook.saveScenarioService();

		// Save latest state of Custom reports.
		CustomReportsRegistry.getInstance().regenerateReportsPluginXMLFile();

		return ret && super.preShutdown();
	}

	@Override
	public void postShutdown() {
		disconnectFromWorkspace();
	}

	/**
	 * Disconnect from the core workspace.
	 */
	public static void disconnectFromWorkspace() {
		// save the workspace
		try {
			final ProgressMonitorDialog p = new ProgressMonitorDialog(null);

			final IRunnableWithProgress runnable = new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) {
					try {
						ResourcesPlugin.getWorkspace().save(true, monitor);

					} catch (final CoreException e) {
						Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					}
				}
			};

			p.run(true, false, runnable);
		} catch (final InvocationTargetException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (final InterruptedException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	private void declareWorkbenchImages() {
		CommonImages.declareWorkbenchImages(getWorkbenchConfigurer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.WorkbenchAdvisor#eventLoopIdle(org.eclipse.swt.
	 * widgets.Display)
	 */
	@Override
	public void eventLoopIdle(final Display display) {
		if (delayedOpenFileProcessor != null) {
			delayedOpenFileProcessor.processEvents(display);
		}
		super.eventLoopIdle(display);
	}
}
