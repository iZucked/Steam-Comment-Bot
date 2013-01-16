/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.XMLResource;

import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.utils.EcoreHelper;
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
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getSourceVersion()}
	 * 
	 * @return
	 */
	protected abstract MetamodelLoader getSourceMetamodelLoader();

	/**
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getDestinationVersion()}
	 * 
	 * @return
	 */
	protected abstract MetamodelLoader getDestinationMetamodelLoader();

	/**
	 * Perform the migration. Root object instance references are not expected to be changed.
	 * 
	 * @param models
	 */
	protected abstract void doMigration(Map<ModelsLNGSet_v1, EObject> models);

	@Override
	public void migrate(final List<URI> baseURIs, final URIConverter uc) throws Exception {

		final Map<MetamodelVersionsUtil.ModelsLNGSet_v1, EObject> models = new HashMap<MetamodelVersionsUtil.ModelsLNGSet_v1, EObject>();

		final MetamodelLoader destinationLoader = getDestinationMetamodelLoader();

		// Record features which have no meta-model equivalent so we can perform migration
		final Map<Object, Object> loadOptions = new HashMap<Object, Object>();
		loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);

		// Load all the current model versions
		final ResourceSet resourceSet = destinationLoader.getResourceSet();

		// Pass in URI Convertor to help URI resolution
		resourceSet.setURIConverter(uc);

		// For models known by this migrator, load them and add to a map.
		for (final URI uri : baseURIs) {
			final String nsURI = EcoreHelper.getPackageNS(uri);
			final ModelsLNGSet_v1 type = MetamodelVersionsUtil.getTypeFromNS(nsURI);
			final Resource r = resourceSet.getResource(uri, true);
			if (type != null) {
				r.load(loadOptions);
				models.put(type, r.getContents().get(0));
			}
		}

		// Migrate!
		doMigration(Collections.unmodifiableMap(models));

		// Save the models
		final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
		for (final Resource r : resourceSet.getResources()) {
			if (r.isLoaded() && r.isModified()) {
				r.save(saveOptions);
			}
		}
	}

}
