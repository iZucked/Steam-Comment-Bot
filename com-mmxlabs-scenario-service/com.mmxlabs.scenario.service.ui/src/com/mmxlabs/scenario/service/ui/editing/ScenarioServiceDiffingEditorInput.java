/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.provider.ScenarioServiceItemProviderAdapterFactory;
import com.mmxlabs.scenario.service.ui.internal.DefaultDiffEditHandler;

public class ScenarioServiceDiffingEditorInput implements IScenarioServiceDiffingEditorInput /* , IPersistableElement */{

	private final ScenarioServiceItemProviderAdapterFactory adapterFactory = new ScenarioServiceItemProviderAdapterFactory();

	private final ScenarioInstance referenceScenarioInstance;
	private final ScenarioInstance scenarioInstance;

	private final IDiffEditHandler diffEditHandler;

	public ScenarioServiceDiffingEditorInput(final ScenarioInstance scenarioInstance, final ScenarioInstance referenceScenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.referenceScenarioInstance = referenceScenarioInstance;
		diffEditHandler = new DefaultDiffEditHandler(scenarioInstance, referenceScenarioInstance);
	}

	@Override
	public ScenarioInstance getCurrentScenarioInstance() {
		return scenarioInstance;
	}

	@Override
	public ScenarioInstance getReferenceScenarioInstance() {
		return referenceScenarioInstance;
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
		return null; /* this; */
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
	public Object getAdapter(@SuppressWarnings("rawtypes") final Class adapter) {
		if (ScenarioInstance.class.isAssignableFrom(adapter)) {
			return scenarioInstance;
		}

		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public int hashCode() {
		return scenarioInstance.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof ScenarioServiceDiffingEditorInput) {
			final ScenarioServiceDiffingEditorInput other = (ScenarioServiceDiffingEditorInput) obj;

			// TODO: Implement equals?
			// use UUIDS?
			return other.scenarioInstance.equals(scenarioInstance);
		}
		return false;
	}

	/*
	 * @Override public void saveState(final IMemento memento) { ScenarioServiceElementFactory.saveState(memento, this); }
	 * 
	 * @Override public String getFactoryId() { return ScenarioServiceElementFactory.ID_FACTORY; }
	 */
	@Override
	public IDiffEditHandler getDiffEditHandler() {
		return diffEditHandler;
	}
}
