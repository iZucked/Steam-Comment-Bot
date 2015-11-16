/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
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

import com.mmxlabs.common.NonNullPair;
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
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.manifest.ManifestPackage;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
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
	protected static final boolean storeFitnessMap = false;

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
	protected static final String originalFitnessesMapName = "originalFitnesses";
	protected static final String endFitnessesMapName = "endFitnesses";

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

	//
	// /**
	// * If run on two separate occasions the fitnesses generated need to be identical. This method tests this by being run twice. The first execution prints out a map that maps the name of the
	// fitness
	// * to the value to the console. This is copied and pasted into the method. The second execution will test that map against a the fitnesses that have been generated again.
	// *
	// * @throws Exception
	// *
	// * @throws MigrationException
	// * @throws InterruptedException
	// */
	public LNGScenarioRunner runScenario(@NonNull final URL url) throws Exception {

		final LNGScenarioModel originalScenario = LNGScenarioRunnerCreator.getScenarioModelFromURL(url);

		return runScenario(originalScenario, url);
	}

	//
	public LNGScenarioRunner evaluateScenario(@NonNull final URL url) throws Exception {

		final LNGScenarioModel originalScenario = LNGScenarioRunnerCreator.getScenarioModelFromURL(url);

		return evaluateScenario(originalScenario, url);
	}

	public LNGScenarioRunner evaluateScenario(@NonNull final URL url, @NonNull final OptimiserSettings optimiserSettings) throws Exception {

		final LNGScenarioModel originalScenario = LNGScenarioRunnerCreator.getScenarioModelFromURL(url);
		final LNGScenarioRunner originalScenarioRunner = LNGScenarioRunnerCreator.createScenarioRunner(originalScenario, optimiserSettings);

		return evaluateScenario(originalScenario, url, originalScenarioRunner);
	}

	//
	public LNGScenarioRunner evaluateScenario(@NonNull final LNGScenarioModel originalScenario, @NonNull final URL origURL) throws IOException, IncompleteScenarioException {

		if (false) {
			LNGScenarioRunnerCreator.saveScenarioModel(originalScenario, new File("C:\\temp\\test-scenario.lingo"));
		}
		// Create scenario runner with optimisation params incase we want to run optimisation outside of the opt run method.
		final LNGScenarioRunner originalScenarioRunner = LNGScenarioRunnerCreator.createScenarioRunner(originalScenario);
		return evaluateScenario(originalScenario, origURL, originalScenarioRunner);
	}

	public LNGScenarioRunner evaluateScenario(@NonNull final LNGScenarioModel originalScenario, @Nullable final URL origURL, @NonNull final LNGScenarioRunner scenarioRunner)
			throws IOException, IncompleteScenarioException {
		scenarioRunner.initAndEval(new TransformerExtensionTestModule(), 10000);

		return scenarioRunner;
	}

	public LNGScenarioRunner runScenario(@NonNull final LNGScenarioModel originalScenario, @NonNull final URL origURL) throws IOException, IncompleteScenarioException {

		OptimiserSettings optimiserSettings = ScenarioUtils.createDefaultSettings();
		optimiserSettings = LNGScenarioRunnerCreator.createExtendedSettings(optimiserSettings);

		optimiserSettings.getAnnealingSettings().setIterations(10000);
		final LNGScenarioRunner scenarioRunner = LNGScenarioRunnerCreator.createScenarioRunner(originalScenario, optimiserSettings);
		assert scenarioRunner != null;
		scenarioRunner.initAndEval(new TransformerExtensionTestModule());
		optimiseScenario(scenarioRunner, origURL, ".properties");
		return scenarioRunner;
	}

	public static void optimiseBasicScenario(@NonNull final LNGScenarioRunner scenarioRunner, @NonNull final URL origURL, @NonNull final String propertiesSuffix) throws IOException {

		scenarioRunner.initAndEval();
		optimiseScenario(scenarioRunner, origURL, propertiesSuffix);
	}

	public static void optimiseScenario(@NonNull final LNGScenarioRunner scenarioRunner, @NonNull final URL origURL, @NonNull final String propertiesSuffix) throws IOException {

		final Schedule intialSchedule = scenarioRunner.getIntialSchedule();
		Assert.assertNotNull(intialSchedule);

		final EList<Fitness> currentOriginalFitnesses = intialSchedule.getFitnesses();
		final URL propertiesURL = new URL(origURL.toString() + propertiesSuffix);
		final Properties props = TesterUtil.getProperties(propertiesURL, storeFitnessMap);
		if (!storeFitnessMap) {
			// Assert old and new are equal
			TesterUtil.testOriginalAndCurrentFitnesses(props, originalFitnessesMapName, currentOriginalFitnesses);
		} else {
			TesterUtil.storeFitnesses(props, originalFitnessesMapName, currentOriginalFitnesses);
		}

		final IMultiStateResult result = scenarioRunner.run();

		boolean checkSolutions = true;
		if (storeFitnessMap) {
			props.put("solution-count", Integer.toString(result.getSolutions().size()));
		} else {
			int solutionCount = 0;
			if (props.contains("solution-count")) {
				solutionCount = Integer.valueOf(props.getProperty("solution-count")).intValue();
				Assert.assertEquals(solutionCount, result.getSolutions().size());
			} else {
				checkSolutions = false;
			}
		}

		// Check final optimised result
		{
			final List<Fitness> currentEndFitnesses = TesterUtil.getFitnessFromExtraAnnotations(result.getBestSolution().getSecond());
			if (storeFitnessMap) {
				TesterUtil.storeFitnesses(props, endFitnessesMapName, currentEndFitnesses);
			} else {
				// Assert old and new are equal
				TesterUtil.testOriginalAndCurrentFitnesses(props, endFitnessesMapName, currentEndFitnesses);
			}
		}
		if (!result.getSolutions().isEmpty()) {
			int i = 0;
			for (final NonNullPair<ISequences, Map<String, Object>> p : result.getSolutions()) {
				final List<Fitness> currentEndFitnesses = TesterUtil.getFitnessFromExtraAnnotations(p.getSecond());
				final String mapName = String.format("solution-%d", i++);
				if (storeFitnessMap) {
					TesterUtil.storeFitnesses(props, mapName, currentEndFitnesses);
				} else {
					// Check for old test cases where we do not have this data stored, we do not want to abort here.
					if (checkSolutions) {
						// Assert old and new are equal
						TesterUtil.testOriginalAndCurrentFitnesses(props, mapName, currentEndFitnesses);
					}
				}
			}

			if (storeFitnessMap) {
				try {
					final URL expectedReportOutput = new URL(FileLocator.toFileURL(origURL).toString().replaceAll(" ", "%20") + propertiesSuffix);
					final File file2 = new File(expectedReportOutput.toURI());
					TesterUtil.saveProperties(props, file2);
				} catch (final URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Stores the fitness values into the {@link Properties} object.
	 * 
	 * @param mapName
	 *            The variable name prefix.
	 * @param fitnesses
	 *            A list of fitnesses to store.
	 */

	public void testReports(final URL scenarioURL, final String reportID, final String shortName, final String extension) throws Exception {

		final URI uri = URI.createURI(FileLocator.toFileURL(scenarioURL).toString().replaceAll(" ", "%20"));

		final BundleContext bundleContext = FrameworkUtil.getBundle(AbstractOptimisationResultTester.class).getBundleContext();
		final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
		try {
			final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, bundleContext.getService(serviceReference));
			MigrationHelper.migrateAndLoad(instance);
			ReportTester.testReports(instance, scenarioURL, reportID, shortName, extension);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}
}