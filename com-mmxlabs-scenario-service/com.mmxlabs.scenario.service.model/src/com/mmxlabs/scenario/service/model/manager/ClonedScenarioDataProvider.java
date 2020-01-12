/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scenario.service.manifest.Manifest;

@NonNullByDefault
public class ClonedScenarioDataProvider implements IScenarioDataProvider {
	private final EObject scenarioModel;
	private final IScenarioDataProvider originalScenarioDataProvider;
	private @NonNull final EditingDomain editingDomain;
	private @NonNull final CommandStack commandStack;

	public static ClonedScenarioDataProvider make(final EObject scenarioModel, final IScenarioDataProvider originalScenarioDataProvider) {
		final BasicCommandStack commandStack = new BasicCommandStack() {
			@Override
			protected void handleError(final Exception exception) {
				throw new RuntimeException(exception);
			}
		};
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);

		final Resource r = new ResourceImpl();
		assert scenarioModel.eResource() == null;
		r.getContents().add(scenarioModel);

		editingDomain.getResourceSet().getResources().add(r);

		return new ClonedScenarioDataProvider(scenarioModel, originalScenarioDataProvider, editingDomain, commandStack);
	}

	public ClonedScenarioDataProvider(final EObject scenarioModel, final IScenarioDataProvider originalScenarioDataProvider, final EditingDomain editingDomain, final CommandStack commandStack) {
		this.scenarioModel = scenarioModel;
		this.originalScenarioDataProvider = originalScenarioDataProvider;
		this.editingDomain = editingDomain;
		this.commandStack = commandStack;
	}

	@Override
	public Manifest getManifest() {
		return originalScenarioDataProvider.getManifest();
	}

	@Override
	public @NonNull EObject getScenario() {
		return scenarioModel;
	}

	@Override
	public <T extends EObject> T getTypedScenario(final Class<T> cls) {
		return cls.cast(scenarioModel);
	}

	@Override
	public void close() {

	}

	// @Override
	// public @NonNull String getExtraDataVersion(@NonNull ISharedDataModelType<?> key) {
	// return originalScenarioDataProvider.getExtraDataVersion(key);
	// }

	@Override
	public <T> T getExtraData(@NonNull final ISharedDataModelType<T> key) {
		return originalScenarioDataProvider.getExtraData(key);
	}

	@Override
	public <T> T getExtraDataProvider(@NonNull final ISharedDataModelType<?> key, final Class<T> cls) {
		return originalScenarioDataProvider.getExtraDataProvider(key, cls);
	}

	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	@Override
	public CommandStack getCommandStack() {
		return commandStack;
	}

	@Override
	public void setLastEvaluationFailed(boolean failed) {

	}

	@Override
	public @NonNull ModelReference getModelReference() {
		throw new UnsupportedOperationException();
	}
}
