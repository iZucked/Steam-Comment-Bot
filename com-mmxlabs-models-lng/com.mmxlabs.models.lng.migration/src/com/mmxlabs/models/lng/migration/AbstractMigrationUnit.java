/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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

import com.mmxlabs.common.Triple;
import com.mmxlabs.models.migration.DataManifest;
import com.mmxlabs.models.migration.DataManifest.EObjectData;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.scenario.service.model.manager.ISharedDataModelType;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.ResourceHelper;

/**
 * Abstract implementation of {@link IMigrationUnit}. This class takes care of loading and saving the ecore model resources. Subclasses are provided with a Map of {@link ModelsLNGSet_v1} to root
 * {@link EObject} instances.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractMigrationUnit implements IMigrationUnit {
//	protected MetamodelLoader destinationLoader;
//	protected Map<URI, PackageData> extraPackages;
 
	/**
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getDestinationVersion()}
	 * 
	 * @param extraPackages
	 * 
	 * @return
	 */
	protected MetamodelLoader createMetamodelLoader(@Nullable final Map<URI, PackageData> extraPackages) {
//		if (extraPackages != null) {
//			this.extraPackages = extraPackages;
//		}
		return  MetamodelVersionsUtil.createVNLoader(getScenarioDestinationVersion(), extraPackages);
	}

	/**
	 * Perform the migration. Root object instance references are not expected to be changed.
	 * 
	 * @param models
	 */
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		// Do nothing be default
	}
 

	/**
	 */
	@Override
	public final void migrate(@Nullable final Map<URI, PackageData> extraPackages, final @NonNull DataManifest dataManifest) throws Exception {
		
		final MetamodelLoader metamodelLoader = createMetamodelLoader(extraPackages);

		// Load all the current model versions
		final ResourceSet resourceSet = metamodelLoader.getResourceSet();
		assert resourceSet != null;

		final Resource modelResource = ResourceHelper.loadResource(resourceSet, dataManifest.getRootObjectURI());

		final EObjectWrapper modelRoot = (EObjectWrapper) modelResource.getContents().get(0);
		assert modelRoot != null;

		final MigrationModelRecord migrationModelRecord = new MigrationModelRecord(modelRoot, metamodelLoader);

		for (final EObjectData data : dataManifest.getEObjectData()) {
			Resource r = resourceSet.getResource(ScenarioStorageUtil.createArtifactURI(dataManifest.getArchiveURI(), data.getURIFragment()), true);
			final EObjectWrapper model = (EObjectWrapper) r.getContents().get(0);
			migrationModelRecord.setDataModelRoot(data.getKey(), model);
		}

		// Migrate!
		doMigration(migrationModelRecord);

		for (final EObjectData data : dataManifest.getEObjectData()) {
			Resource r = resourceSet.getResource(ScenarioStorageUtil.createArtifactURI(dataManifest.getArchiveURI(), data.getURIFragment()), false);
			ResourceHelper.saveResource(r);
		}
		for (Triple<String, EObject, ISharedDataModelType<?>> newData : migrationModelRecord.getNewEObjectData()) {
			Resource r = resourceSet.createResource(ScenarioStorageUtil.createArtifactURI(dataManifest.getArchiveURI(), newData.getFirst()));
			assert newData.getSecond().eResource() == null;

			r.getContents().add(newData.getSecond());
			ResourceHelper.saveResource(r);
			dataManifest.add(newData.getThird(), newData.getFirst(), "1");
		}
		// Save the root model.
		ResourceHelper.saveResource(modelResource);

	}
}
