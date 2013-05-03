package com.mmxlabs.lingo.its.utils;

import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.io.ByteStreams;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.lingo.models.migration.LingoMigrationConstants;
import com.mmxlabs.lingo.models.migration.units.LingoMigrateToV1;
import com.mmxlabs.model.service.impl.ModelService;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.lng.migration.units.MigrateToV1;
import com.mmxlabs.models.lng.migration.units.MigrateToV2;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.extensions.DefaultMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionPoint;
import com.mmxlabs.models.migration.impl.MigrationRegistry;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class MigrationHelper {
	private static class TestScenarioService extends AbstractScenarioService {

		public TestScenarioService(final String name) {
			super(name);
			setModelService(new ModelService());
		}

		@Override
		public ScenarioInstance insert(final Container container, final Collection<ScenarioInstance> dependencies, final Collection<EObject> models) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void delete(final Container container) {
			// TODO Auto-generated method stub
		}

		@Override
		protected ScenarioService initServiceModel() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public URI resolveURI(final String uri) {
			return URI.createURI(uri);
		}

	}

	private static class TestMigrationContextExtensionPoint implements MigrationContextExtensionPoint {

		private final String context;
		private final int latestVersion;

		public TestMigrationContextExtensionPoint(final String context, final int latestVersion) {
			this.context = context;
			this.latestVersion = latestVersion;
		}

		@Override
		public String getContextName() {
			return context;
		}

		@Override
		public String getLatestVersion() {
			return Integer.toString(latestVersion);
		}

	}

	private static class TestMigrationUnitExtensionPoint implements MigrationUnitExtensionPoint {

		private final IMigrationUnit unit;

		public TestMigrationUnitExtensionPoint(final IMigrationUnit unit) {
			this.unit = unit;
		}

		@Override
		public String getContext() {
			return unit.getContext();
		}

		@Override
		public String getFrom() {
			return Integer.toString(unit.getSourceVersion());
		}

		@Override
		public String getTo() {
			return Integer.toString(unit.getDestinationVersion());
		}

		@Override
		public IMigrationUnit createMigrationUnit() {
			return unit;
		}

	}

	private static class TestDefaultMigrationContextExtensionPoint implements DefaultMigrationContextExtensionPoint {

		private final String context;

		public TestDefaultMigrationContextExtensionPoint(final String context) {
			this.context = context;
		}

		@Override
		public String getContext() {
			return context;
		}

	}

	public static MigrationRegistry createMigrationRegistry() {
		final MigrationRegistry migrationRegistry = new MigrationRegistry();
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IMigrationRegistry.class).toInstance(migrationRegistry);

				// Bind some empty lists so that the injection for the registry has some data to use.
				// We will fill in the real data further down
				final List<MigrationContextExtensionPoint> migrationContexts = new ArrayList<MigrationContextExtensionPoint>();
				final List<MigrationUnitExtensionPoint> migrationUnits = new ArrayList<MigrationUnitExtensionPoint>();
				final List<DefaultMigrationContextExtensionPoint> defaultMigrationContexts = new ArrayList<DefaultMigrationContextExtensionPoint>();

				bind(iterable(MigrationContextExtensionPoint.class)).toInstance(migrationContexts);
				bind(iterable(MigrationUnitExtensionPoint.class)).toInstance(migrationUnits);
				bind(iterable(DefaultMigrationContextExtensionPoint.class)).toInstance(defaultMigrationContexts);

			}
		});

		// The real data to pass to the registry
		final List<MigrationContextExtensionPoint> migrationContexts = new ArrayList<MigrationContextExtensionPoint>();
		final List<MigrationUnitExtensionPoint> migrationUnits = new ArrayList<MigrationUnitExtensionPoint>();
		final List<DefaultMigrationContextExtensionPoint> defaultMigrationContexts = new ArrayList<DefaultMigrationContextExtensionPoint>();

		// Populate!
		migrationContexts.add(new TestMigrationContextExtensionPoint(ModelsLNGMigrationConstants.Context, -2));
		migrationContexts.add(new TestMigrationContextExtensionPoint(LingoMigrationConstants.Context, -2));

		migrationUnits.add(new TestMigrationUnitExtensionPoint(new MigrateToV1()));
		migrationUnits.add(new TestMigrationUnitExtensionPoint(new MigrateToV2()));
		// These need the migration registry injected
		migrationUnits.add(new TestMigrationUnitExtensionPoint(injector.getInstance(LingoMigrateToV1.class)));

		defaultMigrationContexts.add(new TestDefaultMigrationContextExtensionPoint(LingoMigrationConstants.Context));

		migrationRegistry.init(migrationContexts, migrationUnits, defaultMigrationContexts);
		return migrationRegistry;
	}

	public static void migrateAndLoad(final ScenarioInstance instance) throws IOException {
		final MigrationRegistry migrationRegistry = createMigrationRegistry();

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

			final EList<String> subModelURIs = instance.getSubModelURIs();

			final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

			// Get original URI's as a list
			final List<URI> uris = new ArrayList<URI>();
			for (final String uriStr : subModelURIs) {
				final URI uri = scenarioService.resolveURI(uriStr);
				uris.add(uri);
			}
			instance.getSubModelURIs().clear();
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

				instance.getSubModelURIs().add(tmpURI.toString());

			}
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
