package com.mmxlabs.lingo.app.headless;

import java.io.File;
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
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.mmxlabs.lingo.app.headless.exporter.FitnessTraceExporter;
import com.mmxlabs.lingo.app.headless.exporter.IRunExporter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class HeadlessApplication implements IApplication {

	private static final String FITNESS_TRACE = "exportFitnessTrace";
	private static final String SCENARIO = "scenario";
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

		final ScenarioRunner runner = new ScenarioRunner(rootObject, settings);

		runner.initStage1();

		final LinearSimulatedAnnealingFitnessEvaluator lsafe = (LinearSimulatedAnnealingFitnessEvaluator) runner.getInjector().getInstance(IFitnessEvaluator.class);

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
		for (final IRunExporter exporter : exporters) {
			exporter.exportData();
		}

		return IApplication.EXIT_OK;

	}

	/**
	 * Iterate through commandLineArgs array until we find "-scenario", then return the next argument
	 * 
	 * @param commandLineArgs
	 * @return
	 */
	private String getScenarioArgs(final String[] commandLineArgs) {

		boolean foundScenario = false;
		for (final String arg : commandLineArgs) {

			if (foundScenario) {
				return arg;
			}

			foundScenario = false;
			if ("-scenario".equals(arg)) {
				foundScenario = true;
			}
		}

		return null;
	}

	private LNGScenarioModel loadScenario(final String scenarioFile) {

		// Create instance and preload scenario.
		final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(URI.createFileURI(scenarioFile), true);

		if (instance == null) {
			return null;
		}

		// Get the root object
		final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();

		return originalScenario;
	}

	@Override
	public void stop() {

	}

	@SuppressWarnings("static-access")
	private boolean parseOptions(final String[] commandLineArgs, final SettingsOverride settings, final List<IRunExporter> exporters) {

		// create the Options
		final Options options = new Options();

		options.addOption(OptionBuilder.withLongOpt(SCENARIO).withDescription("Input scenario file").hasArg().isRequired().create());
		options.addOption(ITERATIONS, true, "Number of iterations");
		options.addOption(SEED, true, "Random seed");

		// Options for OSGi/Eclipse compat - not used, but needs to be specified
		options.addOption("product", true, "Product ID");
		options.addOption("console", false, "Enable OSGi Console");
		options.addOption("consoleLog", false, "Enable logging to console");
		options.addOption("nosplash", false, "Disable splash screen");

		// Enable Fitness Exporter
		options.addOption(OptionBuilder.withLongOpt(FITNESS_TRACE).isRequired(false).withDescription("Export fitness trace to a file").hasArg().create());

		// create the command line parser
		final CommandLineParser parser = new PosixParser();
		try {
			final CommandLine line = parser.parse(options, commandLineArgs);
			if (line.hasOption(SCENARIO)) {
				settings.setScenario(line.getOptionValue(SCENARIO));
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
}
