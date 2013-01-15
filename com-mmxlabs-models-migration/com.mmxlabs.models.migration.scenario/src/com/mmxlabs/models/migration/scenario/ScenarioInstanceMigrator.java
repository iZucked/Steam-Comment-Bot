package com.mmxlabs.models.migration.scenario;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioInstanceMigrator {

	private final IMigrationRegistry migrationRegistry;

	public ScenarioInstanceMigrator(final IMigrationRegistry migrationRegistry) {
		this.migrationRegistry = migrationRegistry;
	}

	public void performMigration(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) throws Exception {
		final String context = scenarioInstance.getVersionContext();
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
				final File f = File.createTempFile("migration", "xmi");
				// Create a temp file and generate a URI to it to pass into migration code.
				final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
				copyURIData(uc, uri, tmpURI);

				// Store the URI
				tmpURIs.add(tmpURI);
				// Add a mapping between the original URI and the temp URI. This should permit internal references to resolve to the new data file.
				// TODO: Check to see whether or not the URI is the original URI or the "resolved" uri.
				uc.getURIMap().put(uri, tmpURI);
			}

			// Apply Migration Chain
			applyMigrationChain(context, scenarioVersion, latestVersion, tmpURIs, uc);

			// Copy back over original data
			for (int i = 0; i < uris.size(); ++i) {
				copyURIData(uc, tmpURIs.get(i), uris.get(i));
			}
			scenarioInstance.setScenarioVersion(latestVersion);

		} finally {
			// Done! Clean up
			for (final File f : tmpFiles) {
				f.delete();
			}
		}
	}

	public void applyMigrationChain(final String context, final int scenarioVersion, final int latestVersion, final List<URI> tmpURIs, URIConverter uc) throws Exception {

		final List<IMigrationUnit> chain = migrationRegistry.getMigrationChain(context, scenarioVersion, latestVersion);

		for (final IMigrationUnit unit : chain) {

			unit.migrate(tmpURIs, uc);
		}
	}

	public void copyURIData(final ExtensibleURIConverterImpl uc, final URI uri, final URI tmpURI) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			// Get input stream from original URI
			is = uc.createInputStream(uri);

			os = uc.createOutputStream(tmpURI);

			// Copy XMI file contents
			// TODO: Tweak buffer size
			// TODO: Java 7 APIs?
			final byte[] buf = new byte[4096];
			int c;
			while ((c = is.read(buf)) > 0) {
				os.write(buf, 0, c);
			}

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final Exception e) {

				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (final Exception e) {

				}
			}
		}
	}
}
