/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.its.internal.Activator;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalCalendarReportView;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.scenario.service.manifest.ManifestPackage;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.util.encryption.impl.PassthroughCipherProvider;

/**
 * 
 * Abstract class to test the optimisation reproducibility of a scenario. Super classes pass in a URL to a scenario resource. There are two modes of operation, determined by the boolean
 * {@link #storeFitnessMap}. When set to false (default) a properties file (located at the input URL with ".properties" appended) contains the expected initial and final fitness values of the
 * scenario. The second mode ({@link #storeFitnessMap} set to true) will generate the properties file. Note this mode will only work for file:// URLs. It is expected that a JUnit test run will provide
 * file based URLs. It is expected JUnit plugin tests will not.
 * 
 * <a href="https://mmxlabs.fogbugz.com/default.asp?220">Case 220: Optimisation Result Test</a>
 * 
 * @author Adam Semenenko
 * 
 */
public class AbstractOptimisationResultTester {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractOptimisationResultTester.class);

	/**
	 * Toggle between storing fitness names and values in a properties file and testing the current fitnesses against the stored values. Should be run as part of a plugin test.
	 */
	private static final boolean storeFitnessMap = false;
	private static final boolean storeReports = false;

	static {
		// Trigger EMF initialisation outside of eclipse environment.
		@SuppressWarnings("unused")
		Object instance = null;
		instance = MMXCorePackage.eINSTANCE;
		instance = ManifestPackage.eINSTANCE;
		instance = AnalyticsPackage.eINSTANCE;
		instance = CargoPackage.eINSTANCE;
		instance = CommercialPackage.eINSTANCE;
		instance = FleetPackage.eINSTANCE;
		instance = ParametersPackage.eINSTANCE;
		instance = PortPackage.eINSTANCE;
		instance = PricingPackage.eINSTANCE;
		instance = SchedulePackage.eINSTANCE;
		instance = SpotMarketsPackage.eINSTANCE;
		// Add other packages?

		// Enforce UK Locale Needed for running tests on build server. Keeps date format consistent.
		Locale.setDefault(Locale.UK);

		System.setProperty(AbstractVerticalCalendarReportView.PROPERTY_RUNNING_ITS, Boolean.TRUE.toString());
	}

	// Key prefixes used in the properties file.
	private static final String originalFitnessesMapName = "originalFitnesses";
	private static final String endFitnessesMapName = "endFitnesses";

	// // Register a cipher provider with the osgi framework for running these tests
	private static ServiceRegistration<IScenarioCipherProvider> cipherServiceRef = null;

	@BeforeClass
	public static void registerCipherProvider() {
		final Bundle bundle = FrameworkUtil.getBundle(AbstractOptimisationResultTester.class);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			final IScenarioCipherProvider provider = new PassthroughCipherProvider();
			final Dictionary<String, Object> properties = new Hashtable<>();
			cipherServiceRef = bundleContext.registerService(IScenarioCipherProvider.class, provider, properties);
		}
	}

	@AfterClass
	public static void dregisterCipherProvider() {
		if (cipherServiceRef != null) {
			cipherServiceRef.unregister();
			cipherServiceRef = null;
		}
	}

	public AbstractOptimisationResultTester() {
		super();
	}

	/**
	 * If run on two separate occasions the fitnesses generated need to be identical. This method tests this by being run twice. The first execution prints out a map that maps the name of the fitness
	 * to the value to the console. This is copied and pasted into the method. The second execution will test that map against a the fitnesses that have been generated again.
	 * 
	 * @throws Exception
	 * 
	 * @throws MigrationException
	 * @throws InterruptedException
	 */
	public LNGScenarioRunner runScenario(@NonNull final URL url) throws Exception {

		final LNGScenarioModel originalScenario = getScenarioModelFromURL(url);

		return runScenario(originalScenario, url);
	}

	private LNGScenarioModel getScenarioModelFromURL(final URL url) throws IOException {
		final URI uri = URI.createURI(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20"));

		final BundleContext bundleContext = FrameworkUtil.getBundle(AbstractOptimisationResultTester.class).getBundleContext();
		final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
		try {
			final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, bundleContext.getService(serviceReference));
			final LNGScenarioModel originalScenario = getScenarioModel(instance);
			return originalScenario;
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public LNGScenarioRunner evaluateScenario(@NonNull final URL url) throws Exception {

		final LNGScenarioModel originalScenario = getScenarioModelFromURL(url);

		return evaluateScenario(originalScenario, url);
	}

	public LNGScenarioRunner evaluateScenario(@NonNull final URL url, OptimiserSettings optimiserSettings) throws Exception {

		final LNGScenarioModel originalScenario = getScenarioModelFromURL(url);
		final LNGScenarioRunner originalScenarioRunner = createScenarioRunner(originalScenario, optimiserSettings);

		return evaluateScenario(originalScenario, url, originalScenarioRunner);
	}

	public LNGScenarioRunner evaluateScenario(@NonNull final LNGScenarioModel originalScenario, @NonNull final URL origURL) throws IOException, IncompleteScenarioException {

		// TODO: Does EcoreUtil.copy work -- do we need to do it here?
		if (false) {
			saveScenarioModel(originalScenario);
		}
		// Create scenario runner with optimisation params incase we want to run optimisation outside of the opt run method.
		final LNGScenarioRunner originalScenarioRunner = createScenarioRunner(originalScenario);
		return evaluateScenario(originalScenario, origURL, originalScenarioRunner);
	}

	public LNGScenarioRunner evaluateScenario(@NonNull final LNGScenarioModel originalScenario, @Nullable final URL origURL, @NonNull LNGScenarioRunner scenarioRunner)
			throws IOException, IncompleteScenarioException {
		// scenarioRunner.initAndEval(new TransformerExtensionTestBootstrapModule(), 10000);
		scenarioRunner.evaluateInitialState();
		return scenarioRunner;
	}

	private LNGScenarioModel getScenarioModel(final ScenarioInstance instance) throws IOException {
		MigrationHelper.migrateAndLoad(instance);

		final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();
		return originalScenario;
	}

	public LNGScenarioRunner runScenario(@NonNull final LNGScenarioModel originalScenario, @NonNull final URL origURL) throws IOException, IncompleteScenarioException {

		final LNGScenarioRunner scenarioRunner = createScenarioRunner(originalScenario);
		assert scenarioRunner != null;
		return runScenario(originalScenario, origURL, scenarioRunner);
	}

	/**
	 * If run on two separate occasions the fitnesses generated need to be identical. This method tests this by being run twice. The first execution prints out a map that maps the name of the fitness
	 * to the value to the console. This is copied and pasted into the method. The second execution will test that map against a the fitnesses that have been generated again.
	 * 
	 * @throws IOException
	 * @throws IncompleteScenarioException
	 * @throws MigrationException
	 * @throws InterruptedException
	 */
	public LNGScenarioRunner runScenario(@NonNull final LNGScenarioModel originalScenario, @NonNull final URL origURL, @NonNull LNGScenarioRunner scenarioRunner)
			throws IOException, IncompleteScenarioException {

		if (false) {
			saveScenarioModel(originalScenario);
		}

		// Limit number of iterations to keep runtime down.
		// scenarioRunner.initAndEval(new TransformerExtensionTestBootstrapModule(), 10000);
		scenarioRunner.evaluateInitialState();

		Schedule intialSchedule = scenarioRunner.getIntialSchedule();
		Assert.assertNotNull(intialSchedule);

		final EList<Fitness> currentOriginalFitnesses = intialSchedule.getFitnesses();
		if (!storeFitnessMap) {
			final URL propsURL = new URL(FileLocator.toFileURL(new URL(origURL.toString() + ".properties")).toString().replaceAll(" ", "%20"));

			final Properties props = new Properties();
			props.load(propsURL.openStream());

			// Assert old and new are equal
			testOriginalAndCurrentFitnesses(props, originalFitnessesMapName, currentOriginalFitnesses);
		}

		scenarioRunner.run();

		// get the fitnesses.
		Schedule finalSchedule = scenarioRunner.getFinalSchedule();
		Assert.assertNotNull(finalSchedule);

		final EList<Fitness> currentEndFitnesses = finalSchedule.getFitnesses();

		if (storeFitnessMap) {

			/**
			 * Extend to save properties in a sorted order for ease of reading
			 */
			@SuppressWarnings("serial")
			final Properties props = new Properties() {
				@Override
				public Set<Object> keySet() {
					return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
				}

				@Override
				public synchronized Enumeration<Object> keys() {
					return Collections.enumeration(new TreeSet<Object>(super.keySet()));
				}
			};

			storeFitnesses(props, originalFitnessesMapName, currentOriginalFitnesses);
			storeFitnesses(props, endFitnessesMapName, currentEndFitnesses);

			// {

			try {
				final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(origURL.toString() + ".properties")).toString().replaceAll(" ", "%20"));

				// final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(origURL.toString())).toString().replaceAll(" ", "%20"));
				final File file2 = new File(expectedReportOutput.toURI());
				// final File file2 = new File(f1.getAbsoluteFile() + ".properties");
				try (FileOutputStream out = new FileOutputStream(file2)) {
					props.store(out, "Created by " + AbstractOptimisationResultTester.class.getName());
				}
			} catch (final URISyntaxException e) {
				e.printStackTrace();
			}

		} else {
			final URL propsURL = new URL(FileLocator.toFileURL(new URL(origURL.toString() + ".properties")).toString().replaceAll(" ", "%20"));

			final Properties props = new Properties();
			props.load(propsURL.openStream());

			// Assert old and new are equal
			// testOriginalAndCurrentFitnesses(props, originalFitnessesMapName, currentOriginalFitnesses);
			testOriginalAndCurrentFitnesses(props, endFitnessesMapName, currentEndFitnesses);
		}
		return scenarioRunner;
	}

	private LNGScenarioRunner createScenarioRunner(final LNGScenarioModel originalScenario) {
		OptimiserSettings createDefaultSettings = LNGScenarioRunnerUtils.createDefaultSettings();
		createDefaultSettings.getAnnealingSettings().setIterations(10000);
		return createScenarioRunner(originalScenario, createDefaultSettings);
	}

	private LNGScenarioRunner createScenarioRunner(final LNGScenarioModel originalScenario, OptimiserSettings settings) {
		final LNGScenarioRunner originalScenarioRunner = new LNGScenarioRunner(originalScenario, settings, new TransformerExtensionTestBootstrapModule(), LNGTransformerHelper.HINT_OPTIMISE_LSO);
		return originalScenarioRunner;
	}

	private void saveScenarioModel(@NonNull final LNGScenarioModel scenario) throws IOException {
		saveScenarioModel(scenario, new File("C:/temp/scen.lingo"));
	}

	private void saveScenarioModel(@NonNull final LNGScenarioModel scenario, @NonNull File destinationFile) throws IOException {

		final LNGScenarioModel copy = duplicate(scenario);
		final IMigrationRegistry migrationRegistry = Activator.getDefault().getMigrationRegistry();
		final String context = migrationRegistry.getDefaultMigrationContext();
		if (context == null) {
			throw new NullPointerException("Context cannot be null");
		}
		int version = migrationRegistry.getLatestContextVersion(context);
		if (version < 0) {
			version = migrationRegistry.getLastReleaseVersion(context);
		}
		ScenarioTools.storeToFile(copy, destinationFile, context, version);
	}

	/**
	 * Stores the fitness values into the {@link Properties} object.
	 * 
	 * @param mapName
	 *            The variable name prefix.
	 * @param fitnesses
	 *            A list of fitnesses to store.
	 */

	private void storeFitnesses(final Properties props, final String mapName, final EList<Fitness> fitnesses) {

		for (final Fitness f : fitnesses) {
			props.setProperty(mapName + "." + f.getName(), Long.toString(f.getFitnessValue()));
		}
	}

	/**
	 * Test the original (previously generated) fitnesses against the current. Also test that the total of the original and current are equal.
	 */
	private void testOriginalAndCurrentFitnesses(final Properties props, final String mapName, final EList<Fitness> currentFitnesses) {

		long totalOriginalFitness = 0;
		long totalCurrentFitness = 0;
		System.out.println(">>>> " + mapName + " <<<<");
		// Information dump
		for (final Fitness f : currentFitnesses) {

			// get the values
			final long originalFitnessValue = Long.parseLong(props.getProperty(mapName + "." + f.getName(), "0"));
			final long currentFitness = f.getFitnessValue();
			System.out.println(f.getName() + ": " + originalFitnessValue + " -> " + currentFitness);
		}

		// Grab expected total fitness from props
		for (final Object fName : props.keySet()) {
			final String str = fName.toString();
			if (str.startsWith(mapName + ".")) {
				final long originalFitnessValue = Long.parseLong(props.getProperty(str, "0"));
				// add to total
				totalOriginalFitness += originalFitnessValue;
			}
		}

		// Validation
		final Set<String> seenFitnesses = new HashSet<String>();
		for (final Fitness f : currentFitnesses) {

			// get the values
			final long originalFitnessValue = Long.parseLong(props.getProperty(mapName + "." + f.getName(), "0"));
			final long currentFitness = f.getFitnessValue();

			// test they are equal
			Assert.assertEquals(f.getName() + " - Previous fitness matches current fitness", originalFitnessValue, currentFitness);

			// add to total
			totalCurrentFitness += currentFitness;

			seenFitnesses.add(mapName + "." + f.getName());
		}

		// test totals are equal
		Assert.assertEquals("Total original fitnesses equal current fitnesses", totalOriginalFitness, totalCurrentFitness);

		// Check that the expected fitness names appeared in the output
		for (final Object fName : props.keySet()) {
			final String str = fName.toString();
			if (str.startsWith(mapName + ".")) {
				Assert.assertTrue(seenFitnesses.contains(fName));
			}
		}
	}

	LNGScenarioModel duplicate(final LNGScenarioModel original) {

		return EcoreUtil.copy(original);
	}

	public void testReports(final URL scenarioURL, final String reportID, final String shortName, final String extension) throws Exception {

		final URI uri = URI.createURI(FileLocator.toFileURL(scenarioURL).toString().replaceAll(" ", "%20"));

		final BundleContext bundleContext = FrameworkUtil.getBundle(AbstractOptimisationResultTester.class).getBundleContext();
		final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
		try {
			final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, bundleContext.getService(serviceReference));
			MigrationHelper.migrateAndLoad(instance);
			testReports(instance, scenarioURL, reportID, shortName, extension);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public void testReports(final LNGScenarioModel model, final URL scenarioURL, final String reportID, final String shortName, final String extension) throws Exception {
		final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		scenarioInstance.setMetadata(ScenarioServiceFactory.eINSTANCE.createMetadata());
		scenarioInstance.setName(scenarioURL.getPath());
		scenarioInstance.setInstance(model);
		testReports(scenarioInstance, new URL(scenarioURL.toString()), reportID, shortName, extension);
	}

	public void testReports(final ScenarioInstance instance, final URL scenarioURL, final String reportID, final String shortName, final String extension) throws Exception {

		final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();
		final LNGScenarioRunner runner = evaluateScenario(originalScenario, scenarioURL);

		final ReportTester reportTester = new ReportTester();
		final IReportContents reportContents = reportTester.getReportContents(instance, reportID);

		Assert.assertNotNull(reportContents);
		final String actualContents = reportContents.getStringContents();
		Assert.assertNotNull(actualContents);
		if (storeReports) {

			final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(scenarioURL.toString())).toString().replaceAll(" ", "%20"));

			final File f1 = new File(expectedReportOutput.toURI());
			final String slash = f1.isDirectory() ? "/" : "";
			final File file2 = new File(f1.getAbsoluteFile() + slash + "reports" + "." + shortName + "." + extension);
			try (PrintWriter pw = new PrintWriter(file2, StandardCharsets.UTF_8.name())) {
				pw.print(actualContents);
			}
		} else {
			final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(scenarioURL.toString() + "reports" + "." + shortName + "." + extension)).toString().replaceAll(" ", "%20"));
			final StringBuilder expectedOutputBuilder = new StringBuilder();
			{
				try (InputStream is = expectedReportOutput.openStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
					String line = reader.readLine();
					if (line != null) {
						expectedOutputBuilder.append(line);
					}
					while (line != null) {
						line = reader.readLine();
						if (line != null) {
							expectedOutputBuilder.append("\n");
							expectedOutputBuilder.append(line);
						}
					}
				}
			}
			if (!expectedOutputBuilder.toString().equals(actualContents)) {
				LOG.warn("Expected " + expectedOutputBuilder.toString());
				LOG.warn("Actual " + actualContents);
			}
			Assert.assertEquals(expectedOutputBuilder.toString(), actualContents);
		}

	}
}