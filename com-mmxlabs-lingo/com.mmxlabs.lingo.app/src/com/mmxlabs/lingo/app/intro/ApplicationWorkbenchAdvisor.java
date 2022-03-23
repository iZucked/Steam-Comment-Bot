/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.intro;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEInternalPreferences;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

import com.mmxlabs.lingo.app.Activator;
import com.mmxlabs.lingo.reports.customizable.CustomReportsRegistry;
import com.mmxlabs.rcp.common.application.DelayedOpenFileProcessor;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
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

		// register workspace adapters
		IDE.registerAdapters();

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

		try {
			refreshFromLocal();
		} finally {
			// Resume background jobs after we startup
			Job.getJobManager().resume();
		}
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

	// / Methods taken from IDE Workbench advisor

	private void refreshFromLocal() {
		final String[] commandLineArgs = Platform.getCommandLineArgs();
		final IPreferenceStore store = IDEWorkbenchPlugin.getDefault().getPreferenceStore();
		final boolean refresh = store.getBoolean(IDEInternalPreferences.REFRESH_WORKSPACE_ON_STARTUP);
		if (!refresh) {
			return;
		}

		// Do not refresh if it was already done by core on startup.
		for (int i = 0; i < commandLineArgs.length; i++) {
			if (commandLineArgs[i].equalsIgnoreCase("-refresh")) { //$NON-NLS-1$
				return;
			}
		}

		final IContainer root = ResourcesPlugin.getWorkspace().getRoot();
		final Job job = new WorkspaceJob(IDEWorkbenchMessages.Workspace_refreshing) {
			@Override
			public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
				root.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				return Status.OK_STATUS;
			}
		};
		job.setRule(root);
		job.schedule();
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

	/**
	 * Declares all IDE-specific workbench images. This includes both "shared"
	 * images (named in {@link IDE.SharedImages}) and internal images (named in
	 * {@link org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages}).
	 * 
	 * @see IWorkbenchConfigurer#declareImage
	 */
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
