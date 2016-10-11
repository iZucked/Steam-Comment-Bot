/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.io.ByteStreams;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

public class MigrationHelper {
	private static class MigrationHelperScenarioService extends AbstractScenarioService {

		public MigrationHelperScenarioService(final String name) {
			super(name);
		}

		@Override
		public ScenarioInstance insert(final Container container, final EObject rootObject) throws IOException {
			return null;
		}

		@Override
		public void delete(final Container container) {
		}

		@Override
		protected ScenarioService initServiceModel() {
			return null;
		}

		@Override
		public URI resolveURI(final String uri) {
			return URI.createURI(uri);
		}

		@Override
		public void moveInto(final List<Container> elements, final Container destination) {

		}

		@Override
		public void makeFolder(final Container parent, final String name) {

		}

	}

	public static void migrateAndLoad(@NonNull final ScenarioInstance instance, final IScenarioCipherProvider scenarioCipherProvider) throws IOException {
		ServiceHelper.withCheckedService(IMigrationRegistry.class, migrationRegistry -> {
			migrateAndLoad(instance, scenarioCipherProvider, migrationRegistry);
		});
	}

	public static void migrateAndLoad(@NonNull final ScenarioInstance instance) throws IOException {
		ServiceHelper.withCheckedOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			migrateAndLoad(instance, scenarioCipherProvider);
		});
	}

	public static void migrateAndLoad(@NonNull final ScenarioInstance instance, final IScenarioCipherProvider scenarioCipherProvider, final IMigrationRegistry migrationRegistry) throws IOException {

		final ScenarioMigrationService migrationService = new ScenarioMigrationService();
		migrationService.setMigrationRegistry(migrationRegistry);
		migrationService.setScenarioCipherProvider(scenarioCipherProvider);

		final MigrationHelperScenarioService scenarioService = new MigrationHelperScenarioService("MigrationScenarioService");

		scenarioService.setScenarioMigrationService(migrationService);
		scenarioService.setScenarioCipherProvider(scenarioCipherProvider);

		File f = null;
		try {

			final String subModelURI = instance.getRootObjectURI();
			assert subModelURI != null;

			final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

			// Get original URI's as a list
			final URI uri = scenarioService.resolveURI(subModelURI);
			// Copy data files for manipulation
			assert uri != null;
			f = File.createTempFile("migration", ".xmi");
			// Create a temp file and generate a URI to it to pass into migration code.
			final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
			assert tmpURI != null;
			copyURIData(uc, uri, tmpURI);

			// Store the URI
			// Add a mapping between the original URI and the temp URI. This should permit internal references to resolve to the new data file.
			// TODO: Check to see whether or not the URI is the original URI or the "resolved" uri.
			uc.getURIMap().put(uri, tmpURI);

			instance.setRootObjectURI(tmpURI.toString());

			scenarioService.load(instance);
		} finally {
			if (f != null && f.exists()) {
				FileDeleter.delete(f, LicenseFeatures.isPermitted("features:secure-delete"));
				f = null;
			}
		}

	}

	public static void copyURIData(@NonNull final URIConverter uc, @NonNull final URI sourceURI, @NonNull final URI destURI) throws IOException {
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
