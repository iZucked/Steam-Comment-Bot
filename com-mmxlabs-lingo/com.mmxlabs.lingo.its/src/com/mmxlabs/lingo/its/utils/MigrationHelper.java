/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.utils;

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
import com.mmxlabs.lingo.its.internal.Activator;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class MigrationHelper {
	private static class TestScenarioService extends AbstractScenarioService {

		public TestScenarioService(final String name) {
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
		public void moveInto(List<Container> elements, Container destination) {
			
		}

		@Override
		public void makeFolder(Container parent, String name) {
			
		}

	}

	public static void migrateAndLoad(final ScenarioInstance instance) throws IOException {
		final IMigrationRegistry migrationRegistry = Activator.getDefault().getMigrationRegistry();

		final ScenarioMigrationService migrationService = new ScenarioMigrationService();
		migrationService.setMigrationRegistry(migrationRegistry);

		// ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(migrationRegistry);
		final TestScenarioService scenarioService = new TestScenarioService("Test");
		String context = instance.getVersionContext();
		if (context == null || context.isEmpty()) {
			context = migrationRegistry.getDefaultMigrationContext();
			instance.setVersionContext(context);
			instance.setScenarioVersion(0);
		}

		// migrator.performMigration(ss, instance);
		scenarioService.setScenarioMigrationService(migrationService);

		{

			final String subModelURI = instance.getRootObjectURI();

			final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

			// Get original URI's as a list
			final URI uri = scenarioService.resolveURI(subModelURI);
			// Copy data files for manipulation
			assert uri != null;
			final File f = File.createTempFile("migration", ".xmi");
			// Create a temp file and generate a URI to it to pass into migration code.
			final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
			assert tmpURI != null;
			copyURIData(uc, uri, tmpURI);

			// Store the URI
			// Add a mapping between the original URI and the temp URI. This should permit internal references to resolve to the new data file.
			// TODO: Check to see whether or not the URI is the original URI or the "resolved" uri.
			uc.getURIMap().put(uri, tmpURI);

			instance.setRootObjectURI(tmpURI.toString());

		}

		scenarioService.load(instance);
	}

	@SuppressWarnings("resource")
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
