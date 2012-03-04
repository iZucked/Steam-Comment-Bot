/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
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
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A class for converting the settings in the EMF into optimisation jobs
 * 
 * @author hinton
 * 
 */
public class LSOConstructor {
	private final OptimiserSettings settings;
	private final boolean instrumenting = true;

	public LSOConstructor(final OptimiserSettings settings) {
		this.settings = settings;
	}

	/**
	 * Build an LSO according to the LSO settings passed in
	 * 
	 * @param context
	 * @return
	 */
	public LocalSearchOptimiser buildOptimiser(final IOptimisationContext context, final ISequencesManipulator manipulator) {
		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> constraintCheckers = constraintCheckerInstantiator.instantiateConstraintCheckers(context.getConstraintCheckerRegistry(), context.getConstraintCheckers(),
				context.getOptimisationData());

		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent> fitnessComponents = fitnessComponentInstantiator.instantiateFitnesses(context.getFitnessFunctionRegistry(), context.getFitnessComponents());

		final IMoveGenerator normalMoveGenerator = createMoveGenerator(context);

		final CompoundMoveGenerator moveGenerator = new CompoundMoveGenerator();
		// final CounterCharterOutMoveGenerator removeCharterOuts = new CounterCharterOutMoveGenerator();

		// removeCharterOuts.setRandom(getRandom());
		final HashSet<IResource> badResource = new HashSet<IResource>();
		final IVesselProvider vesselProvider = context.getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		for (final IResource resource : context.getOptimisationData().getResources()) {
			if (vesselProvider.getVessel(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
				badResource.add(resource);
			}
		}
		// removeCharterOuts.setBadResources(badResource);

		moveGenerator.addGenerator(normalMoveGenerator, 1);
		// moveGenerator.addGenerator(removeCharterOuts, 5);
		moveGenerator.setRandom(getRandom());

		final InstrumentingMoveGenerator instrumentingMoveGenerator = instrumenting ? new InstrumentingMoveGenerator(moveGenerator, true // profile moves (true) or just rate
																																			// (false)
				, false // don't log moves to file
		)
				: null;

		final IFitnessEvaluator fitnessEvaluator = createFitnessEvaluator(fitnessComponents, instrumentingMoveGenerator);

		final DefaultLocalSearchOptimiser lso = new DefaultLocalSearchOptimiser();

		lso.setNumberOfIterations(getNumberOfIterations());
		lso.setSequenceManipulator(manipulator);

		lso.setMoveGenerator(instrumenting ? instrumentingMoveGenerator : moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);

		// lso.setProgressMonitor(monitor);
		lso.setReportInterval(Math.max(10, getNumberOfIterations() / 100));

		// lso.init();

		return lso;
	}

	private IMoveGenerator createMoveGenerator(IOptimisationContext context) {
//		final MoveGeneratorSettings generatorSettings = settings.getMoveGeneratorSettings();
//		if (generatorSettings != null && generatorSettings instanceof RandomMoveGeneratorSettings) {
//			// Ideally this code should go in the EMF classes, to be honest.
//			final RandomMoveGeneratorSettings rmgs = (RandomMoveGeneratorSettings) generatorSettings;
//			final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();
//
//			moveGenerator.setRandom(getRandom());
//
//			moveGenerator.addMoveGeneratorUnit(new Move2over2GeneratorUnit(), rmgs.getWeightFor2opt2());
//			moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit(), rmgs.getWeightFor3opt2());
//			moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit(), rmgs.getWeightFor4opt2());
//			moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit(), rmgs.getWeightFor4opt1());
//
//			return moveGenerator;
//		} else {
			final ConstrainedMoveGenerator cmg = new ConstrainedMoveGenerator(context);
			cmg.setRandom(getRandom());
			return cmg;
//		}
	}

	private IFitnessEvaluator createFitnessEvaluator(List<IFitnessComponent> fitnessComponents, InstrumentingMoveGenerator img) {
		// create a linear FE for now.

		final FitnessHelper fitnessHelper = new FitnessHelper();

		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		fitnessEvaluator.setFitnessComponents(fitnessComponents);
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		final Map<String, Double> weightsMap = new HashMap<String, Double>();
		for (final IFitnessComponent component : fitnessComponents) {
			weightsMap.put(component.getName(), 1.0);
		}

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);

		fitnessEvaluator.setFitnessCombiner(combiner);

		final IThresholder thresholder = instrumenting ? new InstrumentingThresholder(getThresholder(), img) : getThresholder();

		fitnessEvaluator.setThresholder(thresholder);

		fitnessEvaluator.init();

		return fitnessEvaluator;
	}

	private IThresholder getThresholder() {
		// For now we are just going to generate a self-calibrating thresholder
		
		return new GeometricThresholder(getRandom(), settings.getAnnealingSettings().getEpochLength(), settings.getAnnealingSettings().getInitialTemperature(), settings.getAnnealingSettings().getCooling());
		// return new MovingAverageThresholder(getRandom(), ts.getInitialAcceptanceRate(), ts.getAlpha(), ts.getEpochLength(), 3000);
		// return new CalibratingGeometricThresholder(getRandom(), ts.getEpochLength(), ts.getInitialAcceptanceRate(), ts.getAlpha());
	}

	private Random getRandom() {
		return new Random(settings.getSeed());
	}

	private int getNumberOfIterations() {
		return settings.getAnnealingSettings().getIterations();
	}
}
