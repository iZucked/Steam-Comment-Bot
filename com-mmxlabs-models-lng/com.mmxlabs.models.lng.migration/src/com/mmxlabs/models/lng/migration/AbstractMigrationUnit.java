/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

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
	protected abstract void doMigration(EObject model);

	@Override
	public void migrate(final @NonNull URI baseURI, @Nullable final Map<URI, PackageData> extraPackages) throws Exception {

		final MetamodelLoader destinationLoader = getDestinationMetamodelLoader(extraPackages);

		// Load all the current model versions
		final ResourceSet resourceSet = destinationLoader.getResourceSet();

//		// Standard options
//		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_ATTACHMENT, true);
//
//		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
//		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
//		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());

		final HashMap<String, EObject> intrinsicIDToEObjectMap = new HashMap<String, EObject>();

		// Record features which have no meta-model equivalent so we can perform migration
		// resourceSet.getLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);

		final XMIResource modelResource = (XMIResource) resourceSet.createResource(baseURI);
		if (modelResource instanceof ResourceImpl) {
			((ResourceImpl) modelResource).setIntrinsicIDToEObjectMap(intrinsicIDToEObjectMap);
		}
		modelResource.load(resourceSet.getLoadOptions());

		final EObject eObject = modelResource.getContents().get(0);
		assert eObject != null;

		// Migrate!
		doMigration(eObject);

		// Save the model.
		modelResource.save(null);
	}

	/**
	 * Sub classes can initialise new sub model instances here and add them to the map.
	 * 
	 * @param loader
	 *            (in)
	 * @param existingModels
	 *            (in) The set of existing models in the scenario
	 * @param newModels
	 *            (out) A map to store new model references in.
	 */
	protected void hookInNewModels(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> existingModels, final Map<ModelsLNGSet_v1, EObject> newModels) {

	}

}
