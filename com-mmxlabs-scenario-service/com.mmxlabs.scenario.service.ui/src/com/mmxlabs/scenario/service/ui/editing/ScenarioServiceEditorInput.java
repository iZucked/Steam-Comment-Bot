/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioServiceEditorInput implements IScenarioServiceEditorInput {

	private final ScenarioInstance scenarioInstance;

	public ScenarioServiceEditorInput(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	@Override
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	@Override
	public boolean exists() {
		return scenarioInstance != null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return scenarioInstance.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return scenarioInstance.getMetadata().getComment();
	}

	@Override
	public String getContentType() {
		Metadata metadata = scenarioInstance.getMetadata();
		if (metadata == null) {
			return null;
		}
		return metadata.getContentType();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") final Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public int hashCode() {
		return scenarioInstance.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof ScenarioServiceEditorInput) {
			final ScenarioServiceEditorInput other = (ScenarioServiceEditorInput) obj;
			return other.scenarioInstance.equals(scenarioInstance);
		}
		return false;
	}
}
