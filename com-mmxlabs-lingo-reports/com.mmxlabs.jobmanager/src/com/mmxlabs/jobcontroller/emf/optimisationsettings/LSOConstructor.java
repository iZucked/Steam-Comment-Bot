package com.mmxlabs.jobcontroller.emf.optimisationsettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.MoveGeneratorSettings;
import scenario.optimiser.lso.RandomMoveGeneratorSettings;

import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.core.impl.ChainedSequencesManipulator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.CalibratingGeometricThresholder;
import com.mmxlabs.optimiser.lso.impl.thresholders.InstrumentingThresholder;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move2over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move3over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over1GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator;
import com.mmxlabs.scheduler.optimiser.manipulators.StartLocationRemovingSequenceManipulator;

/**
 * A class for converting the settings in the EMF into optimisation jobs
 * @author hinton
 *
 */
public class LSOConstructor {
	private LSOSettings settings;
	private boolean instrumenting = false;

	public LSOConstructor(LSOSettings settings) {
		this.settings = settings;
	}
	
	/**
	 * Build an LSO according to the LSO settings passed in
	 * @param <T>
	 * @param context
	 * @return
	 */
	public <T> LocalSearchOptimiser<T> buildOptimiser(final IOptimisationContext<T> context, final ISequencesManipulator<T> manipulator) {
		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker<T>> constraintCheckers = constraintCheckerInstantiator
				.instantiateConstraintCheckers(
						context.getConstraintCheckerRegistry(),
						context.getConstraintCheckers());
		
		for (final IConstraintChecker<T> checker : constraintCheckers) {
			checker.setOptimisationData(context.getOptimisationData());
		}

		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent<T>> fitnessComponents = fitnessComponentInstantiator
				.instantiateFitnesses(context.getFitnessFunctionRegistry(),
						context.getFitnessComponents());
		
		final IMoveGenerator<T> moveGenerator =
			createMoveGenerator(context);
		
		
		final InstrumentingMoveGenerator<T> instrumentingMoveGenerator =
			instrumenting  ?
			new InstrumentingMoveGenerator<T>(moveGenerator)
			: null;
		
		final IFitnessEvaluator<T> fitnessEvaluator = 
			createFitnessEvaluator(fitnessComponents, instrumentingMoveGenerator);
		
		final DefaultLocalSearchOptimiser<T> lso = new DefaultLocalSearchOptimiser<T>();

		lso.setNumberOfIterations(getNumberOfIterations());
		lso.setSequenceManipulator(manipulator);
		
		lso.setMoveGenerator(instrumenting ? instrumentingMoveGenerator : moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);

//		lso.setProgressMonitor(monitor);
		lso.setReportInterval(Math.max(10, getNumberOfIterations() / 100));

//		lso.init();

		return lso;
	}
	
	private <T> IMoveGenerator<T> createMoveGenerator(IOptimisationContext<T> context) {
		final MoveGeneratorSettings generatorSettings = settings.getMoveGeneratorSettings();
		if (generatorSettings != null && generatorSettings instanceof RandomMoveGeneratorSettings) {
			//Ideally this code should go in the EMF classes, to be honest.
			final RandomMoveGeneratorSettings rmgs = (RandomMoveGeneratorSettings) generatorSettings;
			final RandomMoveGenerator<T> moveGenerator = new RandomMoveGenerator<T>();
			moveGenerator.setRandom(getRandom());
			if (rmgs.isUsing2over2())
				moveGenerator.addMoveGeneratorUnit(new Move2over2GeneratorUnit<T>());
			if (rmgs.isUsing3over2())
				moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit<T>());
			if (rmgs.isUsing4over2())
				moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit<T>());
			if (rmgs.isUsing4over1())
				moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit<T>());
			
			return moveGenerator;
		} else {
			final ConstrainedMoveGenerator<T> cmg = new ConstrainedMoveGenerator<T>(context);
			cmg.setRandom(getRandom());
			return cmg;
		}
	}
	
	private <T> IFitnessEvaluator<T> createFitnessEvaluator(List<IFitnessComponent<T>> fitnessComponents, InstrumentingMoveGenerator<T> img) {
		//create a linear FE for now.
		
		final FitnessHelper<T> fitnessHelper = new FitnessHelper<T>();

		final LinearSimulatedAnnealingFitnessEvaluator<T> fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator<T>();

		fitnessEvaluator.setFitnessComponents(fitnessComponents);
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		final Map<String, Double> weightsMap = new HashMap<String, Double>();
		for (final IFitnessComponent<T> component : fitnessComponents) {
			weightsMap.put(component.getName(), 1.0);
		}

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);

		fitnessEvaluator.setFitnessCombiner(combiner);

		final IThresholder thresholder = 
			instrumenting ? 
			new InstrumentingThresholder( getThresholder(), img )
			: getThresholder();
			
		fitnessEvaluator.setThresholder(thresholder);

		fitnessEvaluator.init();

		return fitnessEvaluator;
	}

	private IThresholder getThresholder() {
		//For now we are just going to generate a self-calibrating thresholder
		return new CalibratingGeometricThresholder(getRandom(), 
				settings.getThresholderSettings().getEpochLength(),
				settings.getThresholderSettings().getInitialAcceptanceRate(), 
				settings.getThresholderSettings().getAlpha());
	}

	private Random getRandom() {
		return new Random(settings.getRandomSeed());
	}

	private int getNumberOfIterations() {
		return settings.getNumberOfSteps();
	}
}
