/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.migration.IClientMigrationUnit;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.scenario.service.util.ResourceHelper;

/**
 * Abstract implementation of {@link IMigrationUnit}. This class takes care of loading and saving the ecore model resources. Subclasses are provided with a Map of {@link ModelsLNGSet_v1} to root
 * {@link EObject} instances.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractClientMigrationUnit implements IClientMigrationUnit {

	protected MetamodelLoader sourceLoader;
	protected MetamodelLoader destinationLoader;
	protected Map<URI, PackageData> extraPackages;

	public abstract int getScenarioVersion();

	@Override
	public final int getScenarioSourceVersion() {
		return getScenarioVersion();
	}

	@Override
	public final int getScenarioDestinationVersion() {
		return getScenarioVersion();
	}

	/**
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getSourceVersion()}.
	 * 
	 * @return
	 */
	protected MetamodelLoader getSourceMetamodelLoader(@Nullable final Map<URI, PackageData> extraPackages) {
		if (extraPackages != null) {
			this.extraPackages = extraPackages;
		}
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createVNLoader(getScenarioSourceVersion(), this.extraPackages);
		}
		return sourceLoader;
	}

	/**
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getDestinationVersion()}
	 * 
	 * @param extraPackages
	 * 
	 * @return
	 */
	protected MetamodelLoader getDestinationMetamodelLoader(@Nullable final Map<URI, PackageData> extraPackages) {
		if (extraPackages != null) {
			this.extraPackages = extraPackages;
		}
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createVNLoader(getScenarioDestinationVersion(), this.extraPackages);
		}
		return destinationLoader;
	}

	/**
	 * Perform the migration. Root object instance references are not expected to be changed.
	 * 
	 * @param models
	 */
	protected void doMigration(@NonNull final EObject model) {
		// Throw some kind of exception if we get here. Sublcasses should override this method or doMigrationWithHelper
		throw new UnsupportedOperationException("Not yet implemented");
	}

	protected void doMigrationWithHelper(@NonNull final MetamodelLoader metamodelLoader, @NonNull final EObjectWrapper model) {
		doMigration(model);
	}

	/**
	 * Overrideable method to allow sub-classes to choose which meta-model version to load the datamodel under.
	 * 
	 * @param extraPackages
	 * @return
	 */
	protected MetamodelLoader getMigrationLoader(@Nullable final Map<URI, PackageData> extraPackages) {
		return getDestinationMetamodelLoader(extraPackages);
	}

	/**
	 */
	@Override
	public void migrate(final @NonNull URI baseURI, @Nullable final Map<URI, PackageData> extraPackages) throws Exception {

		final MetamodelLoader metamodelLoader = getMigrationLoader(extraPackages);

		// Load all the current model versions
		final ResourceSet resourceSet = metamodelLoader.getResourceSet();
		assert resourceSet != null;

		final Resource modelResource = ResourceHelper.loadResource(resourceSet, baseURI);

		final EObjectWrapper eObject = (EObjectWrapper) modelResource.getContents().get(0);
		assert eObject != null;

		// Migrate!
		doMigrationWithHelper(metamodelLoader, eObject);

		// Save the model.
		ResourceHelper.saveResource(modelResource);
	}
}
