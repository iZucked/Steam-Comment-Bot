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
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.mmxlabs.lingo.its.internal.Activator;
import com.mmxlabs.lingo.its.utils.MigrationHelper;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.migration.IMigrationRegistry;
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

	@Nullable
	private IScenarioCipherProvider getScenarioCipherProvider() {
		final Bundle bundle = FrameworkUtil.getBundle(AbstractOptimisationResultTester.class);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
			if (serviceReference != null) {
				return bundleContext.getService(serviceReference);
			}
		}
		return null;
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
	public void runScenario(final URL url) throws Exception {

		final URI uri = URI.createURI(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20"));

		final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, getScenarioCipherProvider());

		File f = null;
		try {
			f = MigrationHelper.migrateAndLoad(instance);

			final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();

			runScenario(originalScenario, url);
		} finally {
			if (f != null && f.exists()) {
				f.delete();
			}
		}
	}

	public ScenarioRunner evaluateScenario(final URL url) throws Exception {

		final URI uri = URI.createURI(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20"));

		final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, getScenarioCipherProvider());

		File f = null;
		try {
			f = MigrationHelper.migrateAndLoad(instance);

			final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();

			return evaluateScenario(originalScenario, url);
		} finally {
			if (f != null && f.exists()) {
				f.delete();
			}
		}
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
	public ScenarioRunner runScenario(final LNGScenarioModel originalScenario, final URL origURL) throws IOException, IncompleteScenarioException {

		// TODO: Does EcoreUtil.copy work -- do we need to do it here?
		final LNGScenarioModel copy = duplicate(originalScenario);
		if (false) {
			final IMigrationRegistry migrationRegistry = Activator.getDefault().getMigrationRegistry();
			final String context = migrationRegistry.getDefaultMigrationContext();
			if (context == null) {
				throw new NullPointerException("Context cannot be null");
			}
			int version = migrationRegistry.getLatestContextVersion(context);
			if (version < 0) {
				version = migrationRegistry.getLastReleaseVersion(context);
			}
			ScenarioTools.storeToFile(copy, new File("C:/temp/scen.lingo"), context, version);
		}
		// Create two scenario runners.
		// TODO are two necessary?
		final ScenarioRunner originalScenarioRunner = new ScenarioRunner(originalScenario);
		originalScenarioRunner.init();
		final EList<Fitness> currentOriginalFitnesses = originalScenarioRunner.getIntialSchedule().getFitnesses();

		if (!storeFitnessMap) {
			final URL propsURL = new URL(FileLocator.toFileURL(new URL(origURL.toString() + ".properties")).toString().replaceAll(" ", "%20"));

			final Properties props = new Properties();
			props.load(propsURL.openStream());

			// Assert old and new are equal
			testOriginalAndCurrentFitnesses(props, originalFitnessesMapName, currentOriginalFitnesses);
		}

		// originalScenarioRunner.run();
		final ScenarioRunner endScenarioRunner = new ScenarioRunner(copy);
		endScenarioRunner.init();
		endScenarioRunner.run();

		// get the fitnesses.
		final EList<Fitness> currentEndFitnesses = endScenarioRunner.getFinalSchedule().getFitnesses();

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
				final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(origURL.toString())).toString().replaceAll(" ", "%20"));
				final File f1 = new File(expectedReportOutput.toURI());
				final File file2 = new File(f1.getAbsoluteFile() + ".properties");
				props.store(new FileOutputStream(file2), "Created by " + AbstractOptimisationResultTester.class.getName());
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
		return endScenarioRunner;
	}

	public ScenarioRunner evaluateScenario(final LNGScenarioModel originalScenario, final URL origURL) throws IOException, IncompleteScenarioException {

		// TODO: Does EcoreUtil.copy work -- do we need to do it here?
		if (false) {
			final LNGScenarioModel copy = duplicate(originalScenario);
			final IMigrationRegistry migrationRegistry = Activator.getDefault().getMigrationRegistry();
			final String context = migrationRegistry.getDefaultMigrationContext();
			if (context == null) {
				throw new NullPointerException("Context cannot be null");
			}
			int version = migrationRegistry.getLatestContextVersion(context);
			if (version < 0) {
				version = migrationRegistry.getLastReleaseVersion(context);
			}
			ScenarioTools.storeToFile(copy, new File("C:/temp/scen.lingo"), context, version);
		}
		// Create two scenario runners.
		// TODO are two necessary?
		final ScenarioRunner originalScenarioRunner = new ScenarioRunner(originalScenario);
		originalScenarioRunner.init();

		return originalScenarioRunner;
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

	public void testReports(final URL scenarioURL, final String reportID, final String extension) throws Exception {

		final URI uri = URI.createURI(FileLocator.toFileURL(scenarioURL).toString().replaceAll(" ", "%20"));
		final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, getScenarioCipherProvider());
		File f = null;
		try {
			f = MigrationHelper.migrateAndLoad(instance);
			testReports(instance, scenarioURL, reportID, extension);
		} finally {
			if (f != null && f.exists()) {
				f.delete();
			}
		}
	}

	public void testReports(final LNGScenarioModel model, final URL scenarioURL, final String reportID, final String extension) throws Exception {
		final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		scenarioInstance.setMetadata(ScenarioServiceFactory.eINSTANCE.createMetadata());
		scenarioInstance.setName(scenarioURL.toString());
		scenarioInstance.setInstance(model);
		testReports(scenarioInstance, scenarioURL, reportID, extension);
	}

	public void testReports(final ScenarioInstance instance, final URL scenarioURL, final String reportID, final String extension) throws Exception {

		final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();
		final ScenarioRunner runner = evaluateScenario(originalScenario, scenarioURL);
		runner.updateScenario();

		final ReportTester reportTester = new ReportTester();
		final IReportContents reportContents = reportTester.getReportContents(instance, reportID);

		Assert.assertNotNull(reportContents);
		final String actualContents = reportContents.getStringContents();
		Assert.assertNotNull(actualContents);
		if (storeReports) {

			final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(scenarioURL.toString())).toString().replaceAll(" ", "%20"));

			final File f1 = new File(expectedReportOutput.toURI());
			final File file2 = new File(f1.getAbsoluteFile() + "." + reportID + "." + extension);
			try (PrintWriter pw = new PrintWriter(file2)) {
				pw.print(actualContents);
			}
		} else {
			final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(scenarioURL.toString() + "." + reportID + "." + extension)).toString().replaceAll(" ", "%20"));
			final StringBuilder expectedOutputBuilder = new StringBuilder();
			{
				try (InputStream is = expectedReportOutput.openStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
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
			Assert.assertEquals(expectedOutputBuilder.toString(), actualContents);
		}

	}
}