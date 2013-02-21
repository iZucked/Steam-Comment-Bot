/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.io.ByteStreams;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.mmxcore.util.MMXCoreBinaryResourceFactoryImpl;
import com.mmxlabs.models.mmxcore.util.MMXCoreHandlerUtil;
import com.mmxlabs.models.mmxcore.util.MMXCoreResourceFactoryImpl;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * The {@link ScenarioInstanceMigrator} controls the migration process for a {@link ScenarioInstance}. It will attempt to migrate a scenario if required. It will create a copy of the scenario data to
 * migrate and only overwrite the orginal when the migration process completes without an {@link Exception}. Note, incorrectly coded migration steps could still corrupt a scenario.
 * 
 * @since 2.0
 */
public class ScenarioInstanceMigrator {

	private final IMigrationRegistry migrationRegistry;

	public ScenarioInstanceMigrator(final IMigrationRegistry migrationRegistry) {
		this.migrationRegistry = migrationRegistry;
	}

	public void performMigration(@NonNull final IScenarioService scenarioService, @NonNull final ScenarioInstance scenarioInstance) throws Exception {
		// Check inputs
		final String context = scenarioInstance.getVersionContext();

		checkArgument(context != null, "Scenario has no version context. Unable to migrate");
		checkArgument(scenarioInstance.getInstance() == null, "Scenario already loaded. Unable to migrate");

		assert context != null;

		final int latestVersion = migrationRegistry.getLatestContextVersion(context);
		final int scenarioVersion = scenarioInstance.getScenarioVersion();
		final EList<String> subModelURIs = scenarioInstance.getSubModelURIs();

		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

		// Get original URI's as a list
		final List<URI> uris = new ArrayList<URI>();
		for (final String uriStr : subModelURIs) {
			final URI uri = scenarioService.resolveURI(uriStr);
			uris.add(uri);
		}

		final List<File> tmpFiles = new ArrayList<File>();
		try {
			// Copy data files for manipulation
			final List<URI> tmpURIs = new ArrayList<URI>();
			for (final URI uri : uris) {
				assert uri != null;
				final File f = File.createTempFile("migration", ".xmi");
				// Create a temp file and generate a URI to it to pass into migration code.
				final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
				assert tmpURI != null;
				copyURIData(uc, uri, tmpURI);

				// Store the URI
				tmpURIs.add(tmpURI);
				// Add a mapping between the original URI and the temp URI. This should permit internal references to resolve to the new data file.
				// TODO: Check to see whether or not the URI is the original URI or the "resolved" uri.
				uc.getURIMap().put(uri, tmpURI);
			}

			assert tmpURIs.size() == uris.size();

			// Apply Migration Chain
			final int migratedVersion = applyMigrationChain(context, scenarioVersion, latestVersion, tmpURIs, uc);

			// Sanity check - can we load the new scenario without error?
			{
				// Construct a normal resource set. This will use the global package registry etc
				final ResourceSetImpl resourceSet = new ResourceSetImpl();
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new MMXCoreResourceFactoryImpl());
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmb", new MMXCoreBinaryResourceFactoryImpl());
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

				try {
					// Create a sample instance object
					for (final URI uri : tmpURIs) {
						final Resource r = resourceSet.createResource(uri);
						r.load(null);
						final Object submodel = r.getContents().get(0);
						if (submodel == null) {
							throw new RuntimeException("Error loading migrated scenario model. Aborting");
						}
					}

					// Attempt to resolve inter-model references.
					MMXCoreHandlerUtil.restoreProxiesForResources(resourceSet.getResources());
				} catch (final Exception e) {
					throw new RuntimeException("Error loading migrated scenario. Aborting", e);
				}
			}

			// Copy back over original data
			for (int i = 0; i < uris.size(); ++i) {
				final URI tmpURI = tmpURIs.get(i);
				assert tmpURI != null;
				final URI uri = uris.get(i);
				assert uri != null;
				// Use a new URI Convertor otherwise the previous map will cause source == dest!
				copyURIData(new ExtensibleURIConverterImpl(), tmpURI, uri);
			}

			// For new URI's added to the collection, we need to add them to the scenario model
			if (tmpURIs.size() > uris.size()) {
				for (int i = uris.size(); i < tmpURIs.size(); ++i) {
					final URI tmpURI = tmpURIs.get(i);
					assert tmpURI != null;

					// Construct new URI for model store - here we are using knowledge of how uri's are constructed. We should really create some API around this in scenario service.
					final String newURIStr = "./" + scenarioInstance.getUuid() + "-" + i + ".xmi";
					// Copy data into the new data file
					URI destURI = scenarioService.resolveURI(newURIStr);

					if (destURI.isRelative()) {
						final File f = File.createTempFile("migration", ".xmi");
						destURI = URI.createFileURI(f.getCanonicalPath());
					}
					assert destURI != null;
					copyURIData(new ExtensibleURIConverterImpl(), tmpURI, destURI);

					// Add submodel URI to the model def
					scenarioInstance.getSubModelURIs().add(destURI.toString());
				}
			}

			scenarioInstance.setScenarioVersion(migratedVersion);

		} finally {
			// Done! Clean up
			for (final File f : tmpFiles) {
				f.delete();
			}
		}
	}

	/**
	 * Returns latest version number to store scenario against. This will be the value of the latestVersion param unless this is "-1" (snapshot) in which case the previous version numebr is returned.
	 * 
	 * @param context
	 * @param scenarioVersion
	 * @param latestVersion
	 * @param tmpURIs
	 * @param uc
	 * @return
	 * @throws Exception
	 */
	public int applyMigrationChain(@NonNull final String context, final int scenarioVersion, final int latestVersion, @NonNull final List<URI> tmpURIs, @NonNull final URIConverter uc)
			throws Exception {

		final List<IMigrationUnit> chain = migrationRegistry.getMigrationChain(context, scenarioVersion, latestVersion);

		int version = scenarioVersion;
		for (final IMigrationUnit unit : chain) {

			unit.migrate(tmpURIs, uc, Collections.<String, URI> emptyMap());

			if (unit.getDestinationVersion() == -1) {
				version = unit.getSourceVersion();
			} else {
				version = unit.getDestinationVersion();
			}
		}
		return version;
	}

	@SuppressWarnings("resource")
	public void copyURIData(@NonNull final URIConverter uc, @NonNull final URI sourceURI, @NonNull final URI destURI) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {

			// Get input stream from original URI
			is = uc.createInputStream(sourceURI);
			os = uc.createOutputStream(destURI);

			ByteStreams.copy(is, os);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {

				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (final IOException e) {

				}
			}
		}
	}
}
