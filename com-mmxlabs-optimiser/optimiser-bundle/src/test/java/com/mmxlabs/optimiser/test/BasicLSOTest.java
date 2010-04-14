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
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.distance.DistanceMatrixFitnessCoreFactory;
import com.mmxlabs.optimiser.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.impl.OptimisationContext;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move3over2GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over1GeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.Move4over2GeneratorUnit;
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
	// * MoveGen.set(...) improve
	// * Check DMatrix works (fitness no change?)
	// * Remove "failback" in evalutteSquences(seu, afected);
	// * Make it "!optimise"

	class Element {

		int v;

		Element(int v) {
			this.v = v;
		}

		@Override
		public String toString() {
			return Integer.toString(v);
		}
	}

	IMatrixProvider<Element, Number> buildMatrix(List<Element> elements) {
		HashMapMatrixProvider<Element, Number> matrix = new HashMapMatrixProvider<Element, Number>(
				Constants.DATA_PROVIDER_KEY_distance_matrix);

		for (Element from : elements) {
			for (Element to : elements) {
				matrix.set(from, to, Math.abs(from.v - to.v));
			}
		}

		return matrix;
	}

	public IOptimisationData<Element> buildData(int numElements,
			int numResources) {

		// Create the elements locations
		List<Element> elements = new ArrayList<Element>(numElements);
		for (int i = 0; i < numElements; ++i) {
			Element e = new Element(i);
			elements.add(e);
		}

		// Create resources
		List<IResource> resources = new ArrayList<IResource>(numResources);
		for (int i = 0; i < numResources; ++i) {
			final int id = i;
			IResource resource = new IResource() {
				@Override
				public String toString() {
					return "Resource: " + id;
				}
			};
			resources.add(resource);
		}

		// Create distance matrix
		IMatrixProvider<Element, Number> distanceMatrix = buildMatrix(elements);

		OptimisationData<Element> data = new OptimisationData<Element>();
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
	<T> ISequences<T> createInitialSequences(IOptimisationData<T> data,
			long seed) {
		Random random = new Random(seed);
		Collection<T> sequenceElements = data.getSequenceElements();
		List<T> shuffledElements = new ArrayList<T>(sequenceElements);
		Collections.shuffle(shuffledElements, random);

		List<IResource> resources = data.getResources();
		IModifiableSequences<T> sequences = new ModifiableSequences<T>(
				resources);
		for (T e : shuffledElements) {
			int nextInt = random.nextInt(resources.size());
			sequences.getModifiableSequence(nextInt).add(e);
		}
		return sequences;
	}

	@Test
	public void testLSO() {
		// Build opt data
		IOptimisationData<Element> data = buildData(10, 2);
		// Generate initial state
		ISequences<Element> initialSequences = createInitialSequences(data, 1);
		
		// 
		DistanceMatrixFitnessCoreFactory factory = new DistanceMatrixFitnessCoreFactory();

		List<IFitnessComponent<Element>> fitnessComponents = new ArrayList<IFitnessComponent<Element>>();
		IFitnessCore<Element> fitnessCore = factory.instantiate();
		fitnessComponents.addAll(fitnessCore.getFitnessComponents());

		OptimisationContext<Element> context = new OptimisationContext<Element>(
				data, fitnessComponents, initialSequences);

		IOptimiser<Element> optimiser = buildOptimiser(2000, 50.0, context);

		LinearSimulatedAnnealingFitnessEvaluator<Element> fitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator<Element>) ((DefaultLocalSearchOptimiser<Element>) optimiser)
				.getFitnessEvaluator();

		optimiser.optimise(context);
		System.out
				.println("Final fitness " + fitnessEvaluator.getBestFitness());
	}

	<T> IOptimiser<T> buildOptimiser(int numIterations, double temperature,
			IOptimisationContext<T> context) {
		// Initialise random number generator
		final Random random = new Random(1);

		final FitnessHelper<T> fitnessHelper = new FitnessHelper<T>();
		final LinearSimulatedAnnealingFitnessEvaluator<T> fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator<T>();
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		fitnessEvaluator.setTemperature(temperature);
		fitnessEvaluator.setNumberOfIterations(numIterations);
		List<IFitnessComponent<T>> fitnessComponents = context
				.getFitnessComponents();
		fitnessEvaluator.setFitnessComponents(fitnessComponents);

		final Map<String, Double> weightsMap = CollectionsUtil.makeHashMap(
				fitnessComponents.iterator().next().getName(), 1.0);

		fitnessEvaluator.setFitnessComponentWeights(weightsMap);

		fitnessEvaluator.init();

		final RandomMoveGenerator<T> moveGenerator = new RandomMoveGenerator<T>();
		moveGenerator.setRandom(random);

		// Register RNG move generator units
		moveGenerator.addMoveGeneratorUnit(new Move3over2GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over1GeneratorUnit<T>());
		moveGenerator.addMoveGeneratorUnit(new Move4over2GeneratorUnit<T>());

		final DefaultLocalSearchOptimiser<T> lso = new DefaultLocalSearchOptimiser<T>();

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
}
