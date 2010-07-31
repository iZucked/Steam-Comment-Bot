package com.mmxlabs.jobcontroller.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.StepThresholder;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move3over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over1GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.MoveSnakeGeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.XYPortEuclideanDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

/**
 * Utility class to help build up test components.
 * 
 * @author Simon Goodall
 * 
 */
public final class TestUtils {

	public static <T> IMoveGenerator<T> createRandomMoveGenerator(
			final Random random) {
		final RandomMoveGenerator<T> moveGenerator = new RandomMoveGenerator<T>();
		moveGenerator.setRandom(random);

		// Register RNG move generator units
		moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit<T>());
		// moveGenerator.addMoveGeneratorUnit(new MoveSnakeGeneratorUnit<T>());
		// moveGenerator.addMoveGeneratorUnit(new MoveHydraGeneratorUnit<T>());

		return moveGenerator;
	}

	public static <T> LinearSimulatedAnnealingFitnessEvaluator<T> createLinearSAFitnessEvaluator(
			final int stepSize, final int numIterations,
			final List<IFitnessComponent<T>> fitnessComponents) {
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

		// Thresholder params
		final int initialThreshold = stepSize * numIterations;

		final StepThresholder thresholder = new StepThresholder();
		thresholder.setStepSize(stepSize);
		thresholder.setInitialThreshold(initialThreshold);

		fitnessEvaluator.setThresholder(thresholder);

		fitnessEvaluator.init();

		return fitnessEvaluator;
	}

	public static <T> LocalSearchOptimiser<T> buildOptimiser(
			final IOptimisationContext<T> context, final Random random,
			final int numberOfIterations, final int stepSize,
			final IOptimiserProgressMonitor<T> monitor) {

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

		final LinearSimulatedAnnealingFitnessEvaluator<T> fitnessEvaluator = TestUtils
				.createLinearSAFitnessEvaluator(stepSize, numberOfIterations,
						fitnessComponents);		
		final IMoveGenerator<T> moveGenerator = TestUtils
				.createRandomMoveGenerator(random);

		final DefaultLocalSearchOptimiser<T> lso = new DefaultLocalSearchOptimiser<T>();

		lso.setNumberOfIterations(numberOfIterations);
		lso.setSequenceManipulator(new NullSequencesManipulator<T>());
		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);

		lso.setProgressMonitor(monitor);
		lso.setReportInterval(Math.max(10, numberOfIterations / 100));

		lso.init();

		return lso;
	}

	public static IOptimisationData<ISequenceElement> createProblem() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		// Build XY ports so distance is automatically populated`
		// TODO: Add API to determine which distance provider to use
		
		// Load ports
		final IPort port1 = builder.createPort("port-1", 0, 0);
		final IPort port2 = builder.createPort("port-2", 0, 5000);
		final IPort port3 = builder.createPort("port-3", 5000, 0);
		
		final List<IPort> loadPorts = CollectionsUtil.makeArrayList(port1,
				port2, port3);
		
		// Discharge ports
		final IPort port4 = builder.createPort("port-4", 5000, 5000);
		final IPort port5 = builder.createPort("port-5", 0, 10000);
		final IPort port6 = builder.createPort("port-6", 5000, 10000);

		final List<IPort> dischargePorts = CollectionsUtil.makeArrayList(port4,
				port5, port6);

		buildFleet(builder, new Random(1), loadPorts, loadPorts);

		buildCargoes(builder, 100, loadPorts, dischargePorts, new Random(1));

		// TODO: Set port durations
		// ....

		// Generate the optimisation data
		final IOptimisationData<ISequenceElement> data = builder
				.getOptimisationData();

		// TODO: Make this part of the builder API -- no direct access to sequence elements
		final IElementDurationProviderEditor editor = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_elementDurationsProvider,
						IElementDurationProviderEditor.class);
		
		if (editor != null) {
			for (final ISequenceElement e : data.getSequenceElements()) {
				for (final IResource r : data.getResources()) {
					editor.setElementDuration(e, r, 24);
				}
			}
		}

		builder.dispose();

		return data;
	}

	private static void buildCargoes(final SchedulerBuilder builder,
			final int numberOfCargoes, final List<IPort> loadPorts,
			final List<IPort> dischargePorts, final Random random) {

		final IXYPortDistanceCalculator distanceProvider = new XYPortEuclideanDistanceCalculator();

		for (int i = 0; i < numberOfCargoes; ++i) {

			final IXYPort port1 = (IXYPort) loadPorts.get(random
					.nextInt(loadPorts.size()));
			final IXYPort port2 = (IXYPort) dischargePorts.get(random
					.nextInt(dischargePorts.size()));

			// Link duration between windows to distance and speed
			final double distance = distanceProvider.getDistance(port1, port2);
			final int minTime = (int) Math.ceil(distance / 20.0 / 24.0);
			final int maxTime = (int) Math.ceil(distance / 12.0 / 24.0);

			final int start = random.nextInt(365);
			final int end = 1 + start + minTime
					+ random.nextInt(maxTime - minTime + 15);

			final ITimeWindow tw1 = builder.createTimeWindow(start * 24,
					(start + 1) * 24);
			final ITimeWindow tw2 = builder.createTimeWindow(end * 24,
					(end + 1) * 24);

			int extra = random.nextInt(100) * 1000;
			int salesPrice = 170000 + extra;

			int purchasePrice = salesPrice - 20000;

			final ILoadSlot load = builder.createLoadSlot("load" + i, port1,
					tw1, 0, 200000000, purchasePrice);
			final IDischargeSlot discharge = builder.createDischargeSlot(
					"discharge" + i, port2, tw2, 0, 200000000, salesPrice);
			builder.createCargo("cargo" + i, load, discharge);
		}
	}

	private static void buildFleet(final SchedulerBuilder builder, Random random, List<IPort> startPorts, List<IPort> endPorts) {

		// Consumption Curves
		// DFDE is approx 40 MT less than steam at 20 knots. Steam is roughly
		// 180MT at 20 knots
		final TreeMap<Integer, Long> dfdeKeypoints = new TreeMap<Integer, Long>();
		dfdeKeypoints.put(12000, 8000l);
		dfdeKeypoints.put(20000, 16000l);
		final InterpolatingConsumptionRateCalculator dfdeConsumptionCalculator = new InterpolatingConsumptionRateCalculator(
				dfdeKeypoints);

		final TreeMap<Integer, Long> steamKeypoints = new TreeMap<Integer, Long>();
		steamKeypoints.put(12000, 12000l);
		steamKeypoints.put(20000, 20000l);
		final InterpolatingConsumptionRateCalculator steamConsumptionCalculator = new InterpolatingConsumptionRateCalculator(
				steamKeypoints);

		// TODO: Update boil-off speeds

		// TODO: Test boil-off speeds are even working

		// WHY DO WE ONLY GET MIN/MAX SPEEDS IN OUTPUT?

		final IVesselClass vesselClass4 = builder.createVesselClass(
				"STEAM-126", 12000, 19500, 126000000l, 0, 200000);
		final IVesselClass vesselClass1 = builder.createVesselClass(
				"STEAM-138", 12000, 20000, 138000000l, 0, 200000);
		final IVesselClass vesselClass2 = builder.createVesselClass(
				"STEAM-145", 12000, 20000, 145000000l, 0, 200000);
		final IVesselClass vesselClass3 = builder.createVesselClass("DFDE-177",
				12000, 20000, 177000000l, 0, 200000);

		builder.setVesselClassStateParamaters(vesselClass1, VesselState.Laden,
				138000 / 24, 118000 / 24, 10000 / 24,
				steamConsumptionCalculator, 15000);
		builder.setVesselClassStateParamaters(vesselClass1,
				VesselState.Ballast, 138000 / 24, 118000 / 24, 1000 / 24,
				steamConsumptionCalculator, 15000);

		// Nile Eagle is 0.15% boil off laden
		builder.setVesselClassStateParamaters(vesselClass2, VesselState.Laden,
				145000 / 24, 125000 / 24, 10000 / 24,
				steamConsumptionCalculator, 15000);
		builder.setVesselClassStateParamaters(vesselClass2,
				VesselState.Ballast, 145000 / 24, 125000 / 24, 1000 / 24,
				steamConsumptionCalculator, 15000);

		builder.setVesselClassStateParamaters(vesselClass3, VesselState.Laden,
				177000 / 24, 157000 / 24, 10000 / 24,
				dfdeConsumptionCalculator, 15000);
		builder.setVesselClassStateParamaters(vesselClass3,
				VesselState.Ballast, 177000 / 24, 157000 / 24, 1000 / 24,
				dfdeConsumptionCalculator, 15000);

		builder.setVesselClassStateParamaters(vesselClass4, VesselState.Laden,
				126000 / 24, 106000 / 24, 10000 / 24,
				steamConsumptionCalculator, 15000);
		builder.setVesselClassStateParamaters(vesselClass4,
				VesselState.Ballast, 126000 / 24, 106000 / 24, 1000 / 24,
				steamConsumptionCalculator, 15000);

		// Owned Vessels
		builder.createVessel("Methane Rita Andrea", vesselClass1, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Methane Jane Elizabeth", vesselClass2, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Methane Lydon Volney", vesselClass2, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Methane Shirley Elizabeth", vesselClass2, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Methane Heather Sally", vesselClass2, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Methane Alison Victoria", vesselClass2, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Methane Nile Eagle", vesselClass2, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));

		builder.createVessel("Methane Julia Louise", vesselClass3, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		// Vessel due to come online July 10
		builder.createVessel("Methane Becki Anne", vesselClass3, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		// Vessel due to come online Sept 10
		builder.createVessel("Methane Mickie Harper", vesselClass3, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		// Vessel due to come online Oct 10
		builder.createVessel("Methane Unknown", vesselClass3, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));

		// Charter Vessels
		builder.createVessel("Hilli", vesselClass4, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Gimi", vesselClass4, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Khannur", vesselClass4, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Golar Freeze", vesselClass4, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Methane Princes", vesselClass1, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));

		// Other vessels

		builder.createVessel("Charter-1", vesselClass1, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Charter-2", vesselClass2, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Charter-3", vesselClass3, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Charter-4", vesselClass4, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
		builder.createVessel("Charter-5", vesselClass1, startPorts.get(random.nextInt(startPorts.size())), endPorts.get(random.nextInt(endPorts.size())));
	}

	/**
	 * Create a randomly generated {@link ISequences} object using the given
	 * seed
	 * 
	 * @param <T>
	 * @param data
	 * @param seed
	 * @return
	 */
	public static <T> ISequences<T> createInitialSequences(
			final IOptimisationData<T> data, final long seed) {

		// TODO: Fix this to take into account load/discharge pairs

		final Random random = new Random(seed);
		final Collection<T> sequenceElements = data.getSequenceElements();
		final List<T> shuffledElements = new ArrayList<T>(sequenceElements);
		// Collections.shuffle(shuffledElements, random);

		final List<IResource> resources = data.getResources();
		final IModifiableSequences<T> sequences = new ModifiableSequences<T>(
				resources);

		List<T> ends = new ArrayList<T>(resources.size()); 
		// First elements pairs are the vessel start/end  ports
		for (int i = 0; i < resources.size(); i++) {
			sequences.getModifiableSequence(i).add(shuffledElements.get(2*i));
			ends.add(shuffledElements.get(2*i+1));
		}

		// remaining set are in load/discharge pairs
		for (int i = resources.size() * 2; i < shuffledElements.size() ; i += 2) {
			final int nextInt = random.nextInt(resources.size());
			sequences.getModifiableSequence(nextInt).add(
					shuffledElements.get(i));
			sequences.getModifiableSequence(nextInt).add(
					shuffledElements.get(i + 1));
		}
		
		for (int i = 0; i < resources.size(); i++) {
			sequences.getModifiableSequence(i).add(ends.get(i));
		}
		return sequences;
	}

	public static IConstraintCheckerRegistry createConstraintRegistry() {
		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();
		{
			final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(
					SchedulerConstants.DCP_orderedElementsProvider);
			constraintRegistry
					.registerConstraintCheckerFactory(constraintFactory);
		}
		{
			final ResourceAllocationConstraintCheckerFactory constraintFactory = new ResourceAllocationConstraintCheckerFactory(
					SchedulerConstants.DCP_resourceAllocationProvider);
			constraintRegistry
					.registerConstraintCheckerFactory(constraintFactory);
		}

		return constraintRegistry;
	}

	public static FitnessFunctionRegistry createFitnessRegistry() {
		final FitnessFunctionRegistry fitnessRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();

		fitnessRegistry.registerFitnessCoreFactory(factory);
		return fitnessRegistry;
	}
}
