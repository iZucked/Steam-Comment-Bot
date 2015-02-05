/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
public abstract class AbstractMigrationUnit implements IMigrationUnit {

	/**
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getSourceVersion()}.
	 * 
	 * @return
	 */
	protected abstract MetamodelLoader getSourceMetamodelLoader(Map<URI, PackageData> extraPackages);

	/**
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getDestinationVersion()}
	 * 
	 * @param extraPackages
	 * 
	 * @return
	 */
	protected abstract MetamodelLoader getDestinationMetamodelLoader(Map<URI, PackageData> extraPackages);

	/**
	 * Perform the migration. Root object instance references are not expected to be changed.
	 * 
	 * @param models
	 */
	protected void doMigration(final EObject model) {
		// Throw some kind of exception if we get here. Sublcasses should override this method or doMigrationWithHelper
		throw new UnsupportedOperationException("Not yet implemented");
	}

	protected void doMigrationWithHelper(final MetamodelLoader metamodelLoader, final EObjectWrapper model) {
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

		// // Standard options
		// resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_ATTACHMENT, true);
		//
		// resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		// resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
		// resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());

		// final HashMap<String, EObject> intrinsicIDToEObjectMap = new HashMap<String, EObject>();

		// Record features which have no meta-model equivalent so we can perform migration
		// resourceSet.getLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);

		final Resource modelResource = ResourceHelper.loadResource(resourceSet, baseURI);

		final EObjectWrapper eObject = (EObjectWrapper) modelResource.getContents().get(0);
		assert eObject != null;

		// Migrate!
		doMigrationWithHelper(metamodelLoader, eObject);

		// Save the model.
		ResourceHelper.saveResource(modelResource);
	}
}
