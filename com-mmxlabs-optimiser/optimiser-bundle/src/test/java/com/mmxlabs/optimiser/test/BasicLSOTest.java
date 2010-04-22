package com.mmxlabs.optimiser.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.Constants;
import com.mmxlabs.optimiser.IConstraintChecker;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.IOptimiser;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.distance.DistanceMatrixFitnessCoreFactory;
import com.mmxlabs.optimiser.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.impl.OptimisationContext;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move3over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over1GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.MoveSnakeGeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;
import com.mmxlabs.optimiser.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.scenario.impl.OptimisationData;

/**
 * Test class to run a "system" test of the LSO to optimise a sequence of
 * elements based upon distance. I.e. find minimum travel distance for each
 * resource.
 * 
 * @author Simon Goodall
 * 
 */
public class BasicLSOTest {

	// ///
	// todo
	//	
	// * make creation of lso easier -- spring "template"?
	// * fitness tracking
	// * Solution tracking
	// * move tracking
	// * Check DMatrix works (fitness no change?)
	// * Remove "failback" in evaluateSquences(seu, afected);

	class Element {

		int v;

		Element(final int v) {
			this.v = v;
		}

		@Override
		public String toString() {
			return Integer.toString(v);
		}
	}

	IMatrixProvider<Element, Number> buildMatrix(final List<Element> elements) {
		final HashMapMatrixProvider<Element, Number> matrix = new HashMapMatrixProvider<Element, Number>(
				Constants.DATA_PROVIDER_KEY_distance_matrix);

		for (final Element from : elements) {
			for (final Element to : elements) {
				matrix.set(from, to, Math.abs(from.v - to.v));
			}
		}

		return matrix;
	}

	public IOptimisationData<Element> buildData(final int numElements,
			final int numResources) {

		// Create the elements locations
		final List<Element> elements = new ArrayList<Element>(numElements);
		for (int i = 0; i < numElements; ++i) {
			final Element e = new Element(i);
			elements.add(e);
		}

		// Create resources
		final List<IResource> resources = new ArrayList<IResource>(numResources);
		for (int i = 0; i < numResources; ++i) {
			final int id = i;
			final IResource resource = new IResource() {
				@Override
				public String toString() {
					return "Resource: " + id;
				}
			};
			resources.add(resource);
		}

		// Create distance matrix
		final IMatrixProvider<Element, Number> distanceMatrix = buildMatrix(elements);

		final OptimisationData<Element> data = new OptimisationData<Element>();
		data.setResources(resources);
		data.setSequenceElements(elements);

		data.addDataComponentProvider(
				Constants.DATA_PROVIDER_KEY_distance_matrix, distanceMatrix);

		return data;
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
	<T> ISequences<T> createInitialSequences(final IOptimisationData<T> data,
			final long seed) {
		final Random random = new Random(seed);
		final Collection<T> sequenceElements = data.getSequenceElements();
		final List<T> shuffledElements = new ArrayList<T>(sequenceElements);
		Collections.shuffle(shuffledElements, random);

		final List<IResource> resources = data.getResources();
		final IModifiableSequences<T> sequences = new ModifiableSequences<T>(
				resources);
		for (final T e : shuffledElements) {
			final int nextInt = random.nextInt(resources.size());
			sequences.getModifiableSequence(nextInt).add(e);
		}
		return sequences;
	}

	@Test
	public void testLSO() {
		// Build opt data
		final IOptimisationData<Element> data = buildData(10, 2);
		// Generate initial state
		final ISequences<Element> initialSequences = createInitialSequences(
				data, 1);

		final FitnessFunctionRegistry registry = new FitnessFunctionRegistry();

		final DistanceMatrixFitnessCoreFactory factory = new DistanceMatrixFitnessCoreFactory();

		registry.registerFitnessCoreFactory(factory);

		final OptimisationContext<Element> context = new OptimisationContext<Element>(
				data,
				new ArrayList<String>(factory.getFitnessComponentNames()),
				initialSequences, registry);

		final IOptimiser<Element> optimiser = buildOptimiser(1000, 50.0,
				context);

		final LinearSimulatedAnnealingFitnessEvaluator<Element> fitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator<Element>) ((DefaultLocalSearchOptimiser<Element>) optimiser)
				.getFitnessEvaluator();

		optimiser.optimise(context);
		System.out
				.println("Final fitness " + fitnessEvaluator.getBestFitness());

		// TODO: How to verify result?
	}

	<T> IOptimiser<T> buildOptimiser(final int numIterations,
			final double temperature, final IOptimisationContext<T> context) {

		// Initialise random number generator
		final Random random = new Random(1);

		final FitnessHelper<T> fitnessHelper = new FitnessHelper<T>();
		final LinearSimulatedAnnealingFitnessEvaluator<T> fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator<T>();
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		fitnessEvaluator.setTemperature(temperature);
		fitnessEvaluator.setNumberOfIterations(numIterations);

		final FitnessComponentInstantiator fci = new FitnessComponentInstantiator();
		final List<IFitnessComponent<T>> fitnessComponents = fci
				.instantiateFitnesses(context.getFitnessFunctionRegistry());

		fitnessEvaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> weightsMap = CollectionsUtil.makeHashMap(
				fitnessComponents.iterator().next().getName(), 1.0);
		fitnessEvaluator.setFitnessComponentWeights(weightsMap);
		fitnessEvaluator.init();

		final IMoveGenerator<T> moveGenerator = createMoveGenerator(random);

		final LocalSearchOptimiser<T> lso = new DefaultLocalSearchOptimiser<T>();

		final List<IConstraintChecker<T>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);
		lso.setNumberOfIterations(numIterations);
		lso.setSequenceManipulator(new NullSequencesManipulator<T>());
		lso.setMoveGenerator(moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);

		lso.init();

		return lso;
	}

	private <T> RandomMoveGenerator<T> createMoveGenerator(final Random random) {
		final RandomMoveGenerator<T> moveGenerator = new RandomMoveGenerator<T>();
		moveGenerator.setRandom(random);

		// Register RNG move generator units
		moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new MoveSnakeGeneratorUnit<T>());

		return moveGenerator;
	}
}
