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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.io.ByteStreams;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * The {@link ScenarioInstanceMigrator} controls the migration process for a {@link ScenarioInstance}. It will attempt to migrate a scenario if required. It will create a copy of the scenario data to
 * migrate and only overwrite the original when the migration process completes without an {@link Exception}. Note, incorrectly coded migration steps could still corrupt a scenario.
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
		int scenarioVersion = scenarioInstance.getScenarioVersion();
		final String subModelURI = scenarioInstance.getRootObjectURI();

		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

		// Get original URI's as a list
		final URI originalURI = scenarioService.resolveURI(subModelURI);

		final List<File> tmpFiles = new ArrayList<File>();
		try {
			// Copy data files for manipulation
			assert originalURI != null;
			final File f = File.createTempFile("migration", ".xmi");
			// Create a temp file and generate a URI to it to pass into migration code.
			final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
			assert tmpURI != null;
			copyURIData(uc, originalURI, tmpURI);

			if (scenarioVersion < 0) {
				int lastReleaseVersion = migrationRegistry.getLastReleaseVersion(context);
				scenarioVersion = lastReleaseVersion;
			}
			// Apply Migration Chain
			final int migratedVersion = applyMigrationChain(context, scenarioVersion, latestVersion, tmpURI);

			// Sanity check - can we load the new scenario without error?
			{
				// Construct a normal resource set. This will use the global package registry etc
				final ResourceSetImpl resourceSet = new ResourceSetImpl();
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

				Map<String, EObject> intrinsicIDToEObjectMap = new HashMap<String, EObject>();
				try {
					// Create a sample instance object
					final Resource r = resourceSet.createResource(tmpURI);
					if (r instanceof ResourceImpl) {
						((ResourceImpl) r).setIntrinsicIDToEObjectMap(intrinsicIDToEObjectMap);
					}
					r.load(null);
					final Object submodel = r.getContents().get(0);
					if (submodel == null) {
						throw new RuntimeException("Error loading migrated scenario model. Aborting");
					}

				} catch (final Exception e) {
					throw new RuntimeException("Error loading migrated scenario. Aborting", e);
				}
			}

			// Copy back over original data
			{
				assert tmpURI != null;
				final URI uri = originalURI;
				assert uri != null;
				// Use a new URI Convertor otherwise the previous map will cause source == dest!
				copyURIData(new ExtensibleURIConverterImpl(), tmpURI, uri);
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
	 * Returns latest version number to store scenario against. This will be the value of the latestVersion param unless this is negative (snapshot) in which case the previous version number is
	 * returned.
	 * 
	 * @param context
	 * @param scenarioVersion
	 * @param latestVersion
	 * @param tmpURIs
	 * @param uc
	 * @return
	 * @throws Exception
	 * @since 3.0
	 */
	public int applyMigrationChain(@NonNull final String context, final int scenarioVersion, final int latestVersion, @NonNull final URI tmpURI) throws Exception {

		final List<IMigrationUnit> chain = migrationRegistry.getMigrationChain(context, scenarioVersion, latestVersion);

		int version = scenarioVersion;
		for (final IMigrationUnit unit : chain) {

			unit.migrate(tmpURI, Collections.<String, URI> emptyMap());

			// Only return real version numbers - ignore snapshot versions
			if (unit.getDestinationVersion() >= 0) {
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
