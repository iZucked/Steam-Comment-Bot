/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getSourceVersion()}.
	 * 
	 * @return
	 */
	protected abstract MetamodelLoader getSourceMetamodelLoader(Map<String, URI> extraPackages);

	/**
	 * Returns a {@link MetamodelLoader} for the {@link IMigrationUnit#getDestinationVersion()}
	 * 
	 * @param extraPackages
	 * 
	 * @return
	 */
	protected abstract MetamodelLoader getDestinationMetamodelLoader(Map<String, URI> extraPackages);

	/**
	 * Perform the migration. Root object instance references are not expected to be changed.
	 * 
	 * @param models
	 */
	protected abstract void doMigration(Map<ModelsLNGSet_v1, EObject> models);

	@Override
	public void migrate(final @NonNull List<URI> baseURIs, final @NonNull URIConverter uc, @Nullable final Map<String, URI> extraPackages) throws Exception {

		final Map<MetamodelVersionsUtil.ModelsLNGSet_v1, EObject> models = new HashMap<MetamodelVersionsUtil.ModelsLNGSet_v1, EObject>();

		final MetamodelLoader destinationLoader = getDestinationMetamodelLoader(extraPackages);

		// Load all the current model versions
		final ResourceSet resourceSet = destinationLoader.getResourceSet();

		// Standard options
		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());

		final HashMap<String, EObject> intrinsicIDToEObjectMap = new HashMap<String, EObject>();

		// Record features which have no meta-model equivalent so we can perform migration
		resourceSet.getLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);

		// Pass in URI Convertor to help URI resolution
		resourceSet.setURIConverter(uc);

		// For models known by this migrator, load them and add to a map.
		for (final URI uri : baseURIs) {
			final String nsURI = EcoreHelper.getPackageNS(uri);
			final ModelsLNGSet_v1 type = MetamodelVersionsUtil.getTypeFromNS(nsURI);
			// This could potentially happen if we load the wrong type of scenario in the application...
			if (type == null) {
				throw new RuntimeException("Unknown namespace: " + nsURI);
			}

			final XMIResource r = (XMIResource) resourceSet.createResource(uri);
			if (r instanceof ResourceImpl) {
				((ResourceImpl) r).setIntrinsicIDToEObjectMap(intrinsicIDToEObjectMap);
			}
			r.setTrackingModification(true);
			r.load(resourceSet.getLoadOptions());

			final EObject eObject = r.getContents().get(0);
			assert eObject != null;

			models.put(type, eObject);
		}

		// Request new model instances should they be needed
		final Map<ModelsLNGSet_v1, EObject> newModels = new HashMap<ModelsLNGSet_v1, EObject>();
		hookInNewModels(destinationLoader, models, newModels);

		// For new models, we need to create a resource object for persistence
		for (final Map.Entry<ModelsLNGSet_v1, EObject> e : newModels.entrySet()) {

			// Generate a tmp file
			// TODO: This file is *NOT* cleaned up
			final File f = File.createTempFile("new-migration", ".xmi");
			// Create a temp file and generate a URI to it to pass into migration code.
			final URI uri = URI.createFileURI(f.getCanonicalPath());
			assert uri != null;
			// Create new resource
			final Resource r = resourceSet.createResource(uri);
			// Add model to resource
			r.getContents().add(e.getValue());
			// Peform an initial save
			r.save(Collections.emptyMap());
			// Add into main model map
			models.put(e.getKey(), e.getValue());
			// Add to input URI's list, calling code should detect change in size
			baseURIs.add(uri);
			// Track modifications so we can save again if needed.
			r.setTrackingModification(true);
		}

		// Migrate!
		doMigration(Collections.unmodifiableMap(models));

		// Save the models. We check isModified as we cannot save metamodels which are also listed as resources here
		final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
		for (final Resource r : resourceSet.getResources()) {
			if (r.isLoaded() && r.isModified()) {
				// Save the changed scenarios
				r.save(saveOptions);
			}
		}
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
