/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import java.io.IOException;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.navigator.SaveablesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioServiceContentProvider;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

/**
 * A {@link Saveable} implementation to hook into the {@link ScenarioServiceContentProvider} internal {@link SaveablesProvider} implementation.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioInstanceSavable extends Saveable {
	private static final Logger log = LoggerFactory.getLogger(ScenarioInstanceSavable.class);

	private final ScenarioInstance scenarioInstance;

	private boolean deleted = false;

	public ScenarioInstanceSavable(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	@Override
	public String getName() {
		return scenarioInstance.getName();
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doSave(final IProgressMonitor monitor) throws CoreException {
		try {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(final IProgressMonitor monitor) throws CoreException {
					try {
						// saving = true;
						monitor.beginTask("Saving", 1);
						scenarioInstance.save();
						monitor.worked(1);
					} catch (final IOException e) {
						log.error("IO Error during save", e);
					} finally {
						monitor.done();
					}
				}

			}, new ScenarioInstanceSchedulingRule(scenarioInstance), 0, monitor);
		} catch (final CoreException e) {
			log.error("Error during save", e);
		}
	}

	@Override
	public boolean isDirty() {

		if (deleted) {
			return false;
		}
		return scenarioInstance.isDirty();
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof ScenarioInstanceSavable) {
			final ScenarioInstanceSavable scenarioInstanceSavable = (ScenarioInstanceSavable) object;
			return scenarioInstance.equals(scenarioInstanceSavable.scenarioInstance);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		final int hash = scenarioInstance.hashCode();
		return hash * PRIME + Activator.PLUGIN_ID.hashCode();
	}

	/**
	 * Call before deleting a scenario to pretend we are no longer dirty.
	 */
	public void setDeleted() {
		this.deleted = true;
	}

}
