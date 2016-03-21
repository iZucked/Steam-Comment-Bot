/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.provider.ScenarioServiceItemProviderAdapterFactory;
import com.mmxlabs.scenario.service.ui.editing.internal.ScenarioServiceElementFactory;

public class ScenarioServiceEditorInput implements IScenarioServiceEditorInput, IPersistableElement {

	private final ScenarioServiceItemProviderAdapterFactory adapterFactory = new ScenarioServiceItemProviderAdapterFactory();

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
		final IItemLabelProvider lp = (IItemLabelProvider) adapterFactory.adapt(scenarioInstance, IItemLabelProvider.class);
		return ExtendedImageRegistry.INSTANCE.getImageDescriptor(lp.getImage(scenarioInstance));
	}

	@Override
	public String getName() {
		return scenarioInstance.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		final String comment = scenarioInstance.getMetadata().getComment();
		if (comment == null) {
			return getName();
		}
		return comment;
	}

	@Override
	public String getContentType() {
		final Metadata metadata = scenarioInstance.getMetadata();
		if (metadata == null) {
			return null;
		}
		return metadata.getContentType();
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (ScenarioInstance.class.isAssignableFrom(adapter)) {
			return (T) scenarioInstance;
		}

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

			// TODO: Implement equals?
			// use UUIDS?
			return other.scenarioInstance.equals(scenarioInstance);
		}
		return false;
	}

	@Override
	public void saveState(final IMemento memento) {
		ScenarioServiceElementFactory.saveState(memento, this);
	}

	@Override
	public String getFactoryId() {
		return ScenarioServiceElementFactory.ID_FACTORY;
	}
}
