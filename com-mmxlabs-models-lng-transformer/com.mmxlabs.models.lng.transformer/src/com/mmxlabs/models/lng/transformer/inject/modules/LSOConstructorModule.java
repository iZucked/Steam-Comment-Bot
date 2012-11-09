package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.optimiser.Constraint;
import com.mmxlabs.models.lng.optimiser.Objective;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.core.impl.ChainedSequencesManipulator;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.GeometricThresholder;
import com.mmxlabs.optimiser.lso.impl.thresholders.InstrumentingThresholder;
import com.mmxlabs.optimiser.lso.movegenerators.impl.CompoundMoveGenerator;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;

/**
 * 
 * @author Simon Goodall, Tom Hinton
 * @since 2.0
 */
public class LSOConstructorModule extends AbstractModule {

	private static final String ENABLED_FITNESS_NAMES = "EnabledFitnessNames";
	private static final String ENABLED_CONSTRAINT_NAMES = "EnabledConstraintNames";
	private final boolean instrumenting = true;

	@Override
	protected void configure() {
		install(new SequencesManipulatorModule());
	}

	@Provides
	@Singleton
	private IMoveGenerator provideMoveGenerator(final ConstrainedMoveGenerator normalMoveGenerator, final OptimiserSettings settings) {

		final CompoundMoveGenerator moveGenerator = new CompoundMoveGenerator();
		// final CounterCharterOutMoveGenerator removeCharterOuts = new CounterCharterOutMoveGenerator();
		//
		// // removeCharterOuts.setRandom(getRandom());
		// final HashSet<IResource> badResource = new HashSet<IResource>();
		// final IVesselProvider vesselProvider = context.getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		// for (final IResource resource : context.getOptimisationData().getResources()) {
		// if (vesselProvider.getVessel(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
		// badResource.add(resource);
		// }
		// }
		// // removeCharterOuts.setBadResources(badResource);

		moveGenerator.addGenerator(normalMoveGenerator, 1);
		// moveGenerator.addGenerator(removeCharterOuts, 5);
		moveGenerator.setRandom(new Random(settings.getSeed()));

		return moveGenerator;
	}

	@Provides
	@Singleton
	private InstrumentingMoveGenerator provideInstrumentingMoveGenerator(final IMoveGenerator moveGenerator) {

		final InstrumentingMoveGenerator instrumentingMoveGenerator = instrumenting ? new InstrumentingMoveGenerator(moveGenerator, true // profile moves (true) or just rate
																																			// (false)
				, false // don't log moves to file
		)
				: null;
		return instrumentingMoveGenerator;

	}

	@Provides
	@Singleton
	LocalSearchOptimiser buildOptimiser(final Injector injector, final IOptimisationContext context, final ISequencesManipulator manipulator, final IMoveGenerator moveGenerator,
			final InstrumentingMoveGenerator instrumentingMoveGenerator, final IFitnessEvaluator fitnessEvaluator, final OptimiserSettings settings, final List<IConstraintChecker> constraintCheckers) {

		final DefaultLocalSearchOptimiser lso = new DefaultLocalSearchOptimiser();

		final int numberOfIterations = getNumberOfIterations(settings);

		lso.setNumberOfIterations(numberOfIterations);
		lso.setSequenceManipulator(manipulator);

		lso.setMoveGenerator(instrumenting ? instrumentingMoveGenerator : moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);

		// lso.setProgressMonitor(monitor);
		lso.setReportInterval(Math.max(10, numberOfIterations / 100));

		// lso.init();

		return lso;
	}

	@Provides
	@Singleton
	private ConstrainedMoveGenerator provideConstrainedMoveGenerator(final IOptimisationContext context, final OptimiserSettings settings) {
		// final MoveGeneratorSettings generatorSettings = settings.getMoveGeneratorSettings();
		// if (generatorSettings != null && generatorSettings instanceof RandomMoveGeneratorSettings) {
		// // Ideally this code should go in the EMF classes, to be honest.
		// final RandomMoveGeneratorSettings rmgs = (RandomMoveGeneratorSettings) generatorSettings;
		// final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();
		//
		// moveGenerator.setRandom(getRandom());
		//
		// moveGenerator.addMoveGeneratorUnit(new Move2over2GeneratorUnit(), rmgs.getWeightFor2opt2());
		// moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit(), rmgs.getWeightFor3opt2());
		// moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit(), rmgs.getWeightFor4opt2());
		// moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit(), rmgs.getWeightFor4opt1());
		//
		// return moveGenerator;
		// } else {
		final ConstrainedMoveGenerator cmg = new ConstrainedMoveGenerator(context);
		cmg.setRandom(getRandom(settings));
		cmg.init();
		return cmg;
		// }
	}

	@Provides
	@Singleton
	private IFitnessEvaluator createFitnessEvaluator(final IThresholder thresholder, final InstrumentingMoveGenerator img, final OptimiserSettings settings,
			final List<IFitnessComponent> fitnessComponents) {
		// create a linear FE for now.

		final FitnessHelper fitnessHelper = new FitnessHelper();

		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		fitnessEvaluator.setFitnessComponents(fitnessComponents);
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		// Initialise to zero, then take optimiser settings
		final Map<String, Double> weightsMap = new HashMap<String, Double>();
		for (final IFitnessComponent component : fitnessComponents) {
			weightsMap.put(component.getName(), 0.0);
		}

		for (final Objective objective : settings.getObjectives()) {
			if (objective.isEnabled()) {
				if (weightsMap.containsKey(objective.getName())) {
					weightsMap.put(objective.getName(), objective.getWeight());
				}
			}
		}

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);

		fitnessEvaluator.setFitnessCombiner(combiner);

		final IThresholder wrappedThresholder = instrumenting ? new InstrumentingThresholder(thresholder, img) : thresholder;

		fitnessEvaluator.setThresholder(wrappedThresholder);

		fitnessEvaluator.init();

		return fitnessEvaluator;

	}

	@Provides
	@Singleton
	private IThresholder provideThresholder(final OptimiserSettings settings) {
		// For now we are just going to generate a self-calibrating thresholder

		return new GeometricThresholder(getRandom(settings), settings.getAnnealingSettings().getEpochLength(), settings.getAnnealingSettings().getInitialTemperature(), settings.getAnnealingSettings()
				.getCooling());
		// return new MovingAverageThresholder(getRandom(), ts.getInitialAcceptanceRate(), ts.getAlpha(), ts.getEpochLength(), 3000);
		// return new CalibratingGeometricThresholder(getRandom(), ts.getEpochLength(), ts.getInitialAcceptanceRate(), ts.getAlpha());
	}

	@Provides
	@Singleton
	private List<IConstraintChecker> provideConstraintCheckers(final Injector injector, final IOptimisationContext context) {
		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> constraintCheckers = constraintCheckerInstantiator.instantiateConstraintCheckers(context.getConstraintCheckerRegistry(), context.getConstraintCheckers(),
				context.getOptimisationData());

		for (final IConstraintChecker checker : constraintCheckers) {
			injector.injectMembers(checker);
		}
		return constraintCheckers;
	}

	@Provides
	@Singleton
	private List<IFitnessComponent> provideFitnessComponents(final Injector injector, final IOptimisationContext context) {

		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent> fitnessComponents = fitnessComponentInstantiator.instantiateFitnesses(context.getFitnessFunctionRegistry(), context.getFitnessComponents());
		final Set<IFitnessCore> cores = new HashSet<IFitnessCore>();
		for (final IFitnessComponent c : fitnessComponents) {
			injector.injectMembers(c);
			cores.add(c.getFitnessCore());
		}

		for (final IFitnessCore c : cores) {
			injector.injectMembers(c);
		}

		return fitnessComponents;
	}

	private Random getRandom(final OptimiserSettings settings) {
		return new Random(settings.getSeed());
	}

	private int getNumberOfIterations(final OptimiserSettings settings) {
		return settings.getAnnealingSettings().getIterations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.transformer.IOptimisationTransformer#createOptimisationContext(com.mmxlabs.optimiser.core.scenario.IOptimisationData,
	 * com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap)
	 */

	@Provides
	@Singleton
	@Named("Initial")
	private ISequences provideInitialSequences(final IOptimisationTransformer optimisationTransformer, final IOptimisationData data, final ResourcelessModelEntityMap mem) {

		final ISequences sequences = optimisationTransformer.createInitialSequences(data, mem);

		return sequences;
	}

	@Provides
	@Singleton
	private IOptimisationContext createOptimisationContext(final IOptimisationData data, @Named("Initial") final ISequences sequences, final OptimiserSettings settings,
			final IFitnessFunctionRegistry fitnessFunctionRegistry, final IConstraintCheckerRegistry constraintCheckerRegistry, final IEvaluationProcessRegistry evaluationProcessRegistry,
			@Named(ENABLED_CONSTRAINT_NAMES) final List<String> enabledConstraintNames, @Named(ENABLED_FITNESS_NAMES) final List<String> enabledFitnessNames) {

		final List<String> components = new ArrayList<String>(enabledFitnessNames);
		components.retainAll(fitnessFunctionRegistry.getFitnessComponentNames());

		final List<String> checkers = new ArrayList<String>(enabledConstraintNames);
		checkers.retainAll(constraintCheckerRegistry.getConstraintCheckerNames());

		// Enable all processes
		// final List<String> evaluationProcesses = getEnabledEvaluationProcessNames();
		// log.debug("Available evaluation processes: " + evaluationProcesses);
		// evaluationProcesses.retainAll(evaluationProcessRegistry.getEvaluationProcessNames());

		final List<String> evaluationProcesses = new ArrayList<String>(evaluationProcessRegistry.getEvaluationProcessNames());

		return new OptimisationContext(data, sequences, components, fitnessFunctionRegistry, checkers, constraintCheckerRegistry, evaluationProcesses, evaluationProcessRegistry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.transformer.IOptimisationTransformer#createOptimiserAndContext(com.mmxlabs.optimiser.core.scenario.IOptimisationData,
	 * com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap)
	 */
	@Provides
	@Singleton
	private Pair<IOptimisationContext, LocalSearchOptimiser> createOptimiserAndContext(final Injector injector, final IOptimisationContext context, final IOptimisationData data,
			final LocalSearchOptimiser lso) {

		// .. Should not be performed here, but needs to be somewhere.
		// Need to co-ordinate with AnalyticsTransformer over the init method. Analytics Transformer creates the opt data manually.
		final ChainedSequencesManipulator sequencesManipulator = injector.getInstance(ChainedSequencesManipulator.class);
		sequencesManipulator.init(data);

		return new Pair<IOptimisationContext, LocalSearchOptimiser>(context, lso);
	}

	@Provides
	@Singleton
	@Named(ENABLED_CONSTRAINT_NAMES)
	private List<String> provideEnabledConstraintNames(final OptimiserSettings settings) {
		final List<String> result = new ArrayList<String>();

		for (final Constraint c : settings.getConstraints()) {
			if (c.isEnabled()) {
				result.add(c.getName());
			}
		}

		return result;
	}

	@Provides
	@Singleton
	@Named(ENABLED_FITNESS_NAMES)
	private List<String> provideEnabledFitnessFunctionNames(final OptimiserSettings settings) {
		final List<String> result = new ArrayList<String>();

		for (final Objective o : settings.getObjectives()) {
			if (o.isEnabled() && o.getWeight() > 0) {
				result.add(o.getName());
			}
		}

		return result;
	}

	@Provides
	@Singleton
	private IInitialSequenceBuilder provideIInitialSequenceBuilder(@Named(ENABLED_CONSTRAINT_NAMES) final List<String> enabledConstraintNames, final IConstraintCheckerRegistry constraintCheckerRegistry) {
		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(constraintCheckerRegistry.getConstraintCheckerFactories(enabledConstraintNames));

		return builder;
	}
}
