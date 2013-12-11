/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.mmxlabs.lingo.app.headless.exporter.FitnessTraceExporter;
import com.mmxlabs.lingo.app.headless.exporter.IRunExporter;
import com.mmxlabs.lingo.app.headless.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationService;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

/**
 * Note duplication with various bits of ITS including ScenarioRunner and MigrationHelper
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessApplication implements IApplication {

	private static final String FITNESS_TRACE = "exportFitnessTrace";
	private static final String SCENARIO = "scenario";
	private static final String OUTPUT = "output";
	private static final String ITERATIONS = "iterations";
	private static final String SEED = "seed";

	@Override
	public Object start(final IApplicationContext context) throws Exception {

		final List<IRunExporter> exporters = new LinkedList<IRunExporter>();
		final String[] commandLineArgs = Platform.getCommandLineArgs();
		final SettingsOverride settings = new SettingsOverride();
		if (!parseOptions(commandLineArgs, settings, exporters)) {
			return IApplication.EXIT_OK;
		}

		final String scenarioFile = settings.getScenario();
		if (scenarioFile == null || scenarioFile.isEmpty()) {
			System.err.println("No scenario specified");
			return IApplication.EXIT_OK;
		}

		final LNGScenarioModel rootObject = loadScenario(scenarioFile);
		if (rootObject == null) {
			System.err.println("Unable to load scenario");
			return IApplication.EXIT_OK;
		}

		final String outputFile = settings.getOutput();

		final ScenarioRunner runner = new ScenarioRunner(rootObject, settings);

		runner.initStage1();

		for (final IRunExporter exporter : exporters) {
			exporter.setScenarioRunner(runner);
		}

		final IOptimiserProgressMonitor monitor = new IOptimiserProgressMonitor() {
			@Override
			public void begin(final IOptimiser optimiser, final long initialFitness, final IAnnotatedSolution annotatedSolution) {

				for (final IRunExporter exporter : exporters) {
					exporter.begin(optimiser, initialFitness, annotatedSolution);
				}
			}

			@Override
			public void report(final IOptimiser optimiser, final int iteration, final long currentFitness, final long bestFitness, final IAnnotatedSolution currentSolution,
					final IAnnotatedSolution bestSolution) {

				for (final IOptimiserProgressMonitor exporter : exporters) {
					exporter.report(optimiser, iteration, currentFitness, bestFitness, currentSolution, bestSolution);
				}
			}

			@Override
			public void done(final IOptimiser optimiser, final long bestFitness, final IAnnotatedSolution annotatedSolution) {

				for (final IOptimiserProgressMonitor exporter : exporters) {
					exporter.done(optimiser, bestFitness, annotatedSolution);
				}
			}

		};

		runner.initStage2(monitor);

		System.out.println("LNGResult(");
		System.out.println("\tscenario='" + scenarioFile + "',");
		if (outputFile != null) {
			System.out.println("\toutput='" + outputFile + "',");
		}
		System.out.println("\titerations=" + settings.getIterations() + ",");
		System.out.println("\tseed=" + settings.getSeed() + ",");

		System.err.println("Starting run...");

		final long startTime = System.currentTimeMillis();

		runner.run();

		final long runTime = System.currentTimeMillis() - startTime;
		final Schedule finalSchedule = runner.getFinalSchedule();
		if (finalSchedule == null) {
			System.err.println("Error optimising scenario");
		}

		System.out.println("\truntime=" + runTime / 1000 + ",");

		System.err.println("Optimised!");

		if (outputFile != null) {
			runner.updateScenario();
			saveScenario(outputFile, runner.getScenario());
		}

		for (final IRunExporter exporter : exporters) {
			exporter.exportData();
		}

		return IApplication.EXIT_OK;

	}

	private LNGScenarioModel loadScenario(final String scenarioFile) throws IOException {

		// Create instance and preload scenario.
		final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(URI.createFileURI(scenarioFile), false);

		if (instance == null) {
			return null;
		}

		migrateAndLoad(instance);

		// Get the root object
		final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();

		return originalScenario;
	}

	private void saveScenario(final String outputFile, LNGScenarioModel scenario) throws IOException {
		final ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();

		manifest.setScenarioType("com.mmxlabs.shiplingo.platform.models.manifest.scnfile");

		final IMigrationRegistry migrationRegistry = Activator.getDefault().getMigrationRegistry();
		String versionContext = migrationRegistry.getDefaultMigrationContext();
		manifest.setScenarioVersion(migrationRegistry.getLastReleaseVersion(versionContext));
		manifest.setVersionContext(versionContext);

		File file = new File(outputFile);
		final URI manifestURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/MANIFEST.xmi");
		final Resource manifestResource = resourceSet.createResource(manifestURI);

		manifestResource.getContents().add(manifest);

		final URI relativeURI = URI.createURI("/rootObject.xmi");

		manifest.getModelURIs().add(relativeURI.toString());
		final URI resolved = relativeURI.resolve(manifestURI);
		final Resource r2 = resourceSet.createResource(resolved);
		r2.getContents().add(scenario);
		r2.save(null);
		manifestResource.save(null);
	}

	@Override
	public void stop() {

	}

	@SuppressWarnings("static-access")
	private boolean parseOptions(final String[] commandLineArgs, final SettingsOverride settings, final List<IRunExporter> exporters) {

		// create the Options
		final Options options = new Options();

		options.addOption(OptionBuilder.withLongOpt(OUTPUT).withDescription("Output scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(SCENARIO).withDescription("Input scenario file").hasArg().isRequired().create());
		options.addOption(ITERATIONS, true, "Number of iterations");
		options.addOption(SEED, true, "Random seed");

		// Options for OSGi/Eclipse compat - not used, but needs to be specified
		options.addOption("application", true, "(OSGi) Application ID");
		options.addOption("arch", true, "(OSGi) Set arch");
		options.addOption("clean", false, "(OSGi) Clean any framework cached data");
		options.addOption("configuration", true, "(OSGi) OSGi configuration area");
		options.addOption(OptionBuilder.withLongOpt("console").withDescription("[port] (OSGi) Enable OSGi Console").hasOptionalArg().create());
		options.addOption("consoleLog", false, "(OSGi) Enable logging to console");
		options.addOption("data", true, "(OSGi) OSGi instance area");
		options.addOption(OptionBuilder.withLongOpt("debug").withDescription("[options file] (OSGi) debug mode").hasOptionalArg().create());
		options.addOption(OptionBuilder.withLongOpt("dev").withDescription("[entires] (OSGi) dev mode").hasOptionalArg().create());
//		options.addOption("eclipse.keyring", true, "(Equinox) Set to override location of the default secure storage");
//		options.addOption("eclipse.password", true,
//				"(Equinox) If specified, the secure storage treats contents of the file as a default password. When not set, password providers are used to obtain a password.");
		options.addOption("feature", true, "(Equinox) equivalent to setting eclipse.product to <feature id>");
		options.addOption("framework", true, "(Equinox) equivalent to setting osgi.framework to <location>");
		options.addOption(
				"initialize ",
				false,
				"(OSGi) initializes the configuration being run. All runtime related data structures and caches are refreshed. Any user/plug-in defined configuration data is not purged. No application is run, any product specifications are ignored and no UI is presented (e.g., the splash screen is not drawn)");
		options.addOption("install", true, "(OSGi) OSGi install area");
		options.addOption("keyring ", true, "(OSGi) the location of the authorization database on disk. This argument has to be used together with the -password argument.");
		// various --launcher options

		options.addOption(
				"name",
				true,
				"(OSGi) The name to be displayed in the task bar item for the splash screen when the application starts up (not applicable on Windows). Also used as the title of error dialogs opened by the launcher. When not set, the name is the name of the executable.");

		options.addOption("nl", true, "(OSGi) equivalent to setting osgi.nl to <locale>");
		options.addOption("noExit", false, "(OSGi) equivalent to setting osgi.noShutdown to \"true\"");
		options.addOption("noLazyRegistryCacheLoading ", false, "(OSGi) equivalent to setting eclipse.noLazyRegistryCacheLoading to \"true\"");
		options.addOption("noRegistryCache ", false, "(OSGi)equivalent to setting eclipse.noRegistryCache to \"true\"");
		options.addOption("nosplash", false, "(OSGi) Disable splash screen");
		options.addOption("os", true, "(OSGi) equivalent to setting osgi.os to <operating system>");
		options.addOption("password", true, "(OSGi) the password for the authorization database");
		options.addOption("pluginCustomization ", true, "(OSGi) equivalent to setting eclipse.pluginCustomization to <location>");
		options.addOption("product", true, "(OSGi) Product ID");

		options.addOption("registryMultiLanguage ", false, "(OSGi) equivalent to setting eclipse.registry.MultiLanguage to \"true\"");
		options.addOption(
				"showSplash",
				true,
				"(OSGi) The location of jar used to startup eclipse. The jar referred to should have the Main-Class attribute set to org.eclipse.equinox.launcher.Main. If this parameter is not set, the executable will look in the plugins directory for theorg.eclipse.equinox.launcher bundle with the highest version.");

		options.addOption("startup", true, "(OSGi) Set location of startup jar");
		options.addOption("user", true, "(OSGi) OSGi user area");
		options.addOption("vm", true, "(OSGi) used to locate the Java VM");
		options.addOption(OptionBuilder.withLongOpt("vmargs").withDescription("(OSGi) Java VM arguments").hasArgs().create());
		options.addOption("ws", true, "(OSGi) Set window system");

		// Enable Fitness Exporter
		options.addOption(OptionBuilder.withLongOpt(FITNESS_TRACE).isRequired(false).withDescription("Export fitness trace to a file").hasArg().create());

		// create the command line parser
		final CommandLineParser parser = new PosixParser();
		try {
			final CommandLine line = parser.parse(options, commandLineArgs);
			if (line.hasOption(SCENARIO)) {
				settings.setScenario(line.getOptionValue(SCENARIO));
			}
			if (line.hasOption(OUTPUT)) {
				settings.setOutput(line.getOptionValue(OUTPUT));
			}
			if (line.hasOption(ITERATIONS)) {
				settings.setIterations(Integer.parseInt(line.getOptionValue(ITERATIONS)));
			}
			if (line.hasOption(SEED)) {
				settings.setIterations(Integer.parseInt(line.getOptionValue(SEED)));
			}
			if (line.hasOption(FITNESS_TRACE)) {
				final FitnessTraceExporter exporter = new FitnessTraceExporter();
				exporter.setOutputFile(new File(line.getOptionValue(FITNESS_TRACE)));
				exporters.add(exporter);
			}
		} catch (final ParseException ex) {
			System.err.println("Bad option : " + ex.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Headless Application", options);
			return false;
		}

		return true;
	}

	public static void migrateAndLoad(final ScenarioInstance instance) throws IOException {
		final IMigrationRegistry migrationRegistry = Activator.getDefault().getMigrationRegistry();

		final ScenarioMigrationService migrationService = new ScenarioMigrationService();
		migrationService.setMigrationRegistry(migrationRegistry);

		// ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(migrationRegistry);
		final TestScenarioService scenarioService = new TestScenarioService("headless");
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

	@SuppressWarnings("resource")
	public static void copyURIData(final URIConverter uc, final URI sourceURI, final URI destURI) throws IOException {
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
