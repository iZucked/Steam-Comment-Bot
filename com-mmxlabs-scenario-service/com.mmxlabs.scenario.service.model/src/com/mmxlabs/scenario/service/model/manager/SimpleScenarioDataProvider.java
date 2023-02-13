/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.HashMap;
import java.util.Map;

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

/**
 * A {@link IScenarioDataProvider} implementation intended for use with scenarios created programatically rather than loaded from a .lingo file. Typically will be used in tests or on a temporary to
 * construct a new scenario before copying into a .lingo file. There is no support for ModelReferences/ModelRecords. Closing this sdp will clean up associated data.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public class SimpleScenarioDataProvider implements IScenarioDataProvider {

	private final @NonNull Manifest manifest;
	private final @NonNull EObject rootModel;
	private final @NonNull EditingDomain editingDomain;
	private final @NonNull CommandStack commandStack;

	private final Map<ISharedDataModelType<?>, EObject> extraData = new HashMap<>();
	private final Map<ISharedDataModelType<?>, Object> extraDataProviders = new HashMap<>();

	public static SimpleScenarioDataProvider make(final Manifest manifest, final EObject rootModel) {
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
		assert rootModel.eResource() == null;

		r.getContents().add(rootModel);

		editingDomain.getResourceSet().getResources().add(r);

		return new SimpleScenarioDataProvider(manifest, rootModel, editingDomain, commandStack);
	}

	public SimpleScenarioDataProvider(final Manifest manifest, final EObject rootModel, final EditingDomain editingDomain, final CommandStack commandStack) {
		this.manifest = manifest;
		this.rootModel = rootModel;
		this.editingDomain = editingDomain;
		this.commandStack = commandStack;
	}

	@Override
	public Manifest getManifest() {
		return manifest;
	}

	@Override
	public EObject getScenario() {
		return rootModel;
	}

	@Override
	public <T extends EObject> T getTypedScenario(final Class<T> cls) {
		return cls.cast(rootModel);
	}

	@Override
	public void close() {
		extraData.clear();
		extraDataProviders.clear();
	}

	@Override
	public <T> T getExtraData(@NonNull final ISharedDataModelType<T> key) {
		return (T) extraData.get(key);
	}

	public void setExtraData(@NonNull final ISharedDataModelType<?> key, final EObject value) {
		extraData.put(key, value);
	}

	@Override
	public <T> T getExtraDataProvider(@NonNull final ISharedDataModelType<?> key, final Class<T> cls) {
		return cls.cast(extraDataProviders.computeIfAbsent(key, k -> ISharedDataModelType.REGISTRY.getMakerFunction(k).apply(SimpleScenarioDataProvider.this)));
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

	// @Override
	// public String getExtraDataVersion(ISharedDataModelType<?> key) {
	// return null;
	// }
}
