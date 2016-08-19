/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IComparisonFactory;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.util.ResourceHelper;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

public class TesterUtil {

	@NonNull
	public static List<Fitness> getFitnessFromExtraAnnotations(@NonNull final Map<String, Object> extraAnnotations) {
		final List<Fitness> outputFitnesses = new LinkedList<>();

		final Map<String, Long> fitnesses = (Map<String, Long>) extraAnnotations.get(OptimiserConstants.G_AI_fitnessComponents);
		if (fitnesses != null) {

			for (final Map.Entry<String, Long> entry : fitnesses.entrySet()) {
				final Fitness fitness = ScheduleFactory.eINSTANCE.createFitness();
				fitness.setName(entry.getKey());
				fitness.setFitnessValue(entry.getValue());
				outputFitnesses.add(fitness);
			}
		}

		return outputFitnesses;
	}

	public static void storeFitnesses(final Properties props, final String prefix, final List<Fitness> fitnesses) {

		for (final Fitness f : fitnesses) {
			props.setProperty(prefix + "." + f.getName(), Long.toString(f.getFitnessValue()));
		}
	}

	@NonNull
	public static Properties getProperties(final URL propertiesURL, final boolean create) throws MalformedURLException, IOException {
		if (create) {
			/**
			 * Extend to save properties in a sorted order for ease of reading
			 */
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
			return props;
		} else {

			final Properties props = new Properties();
			props.load(propertiesURL.openStream());
			return props;
		}
	}

	public static void saveProperties(@NonNull final Properties props, @NonNull final File file) throws IOException {
		try (FileOutputStream out = new FileOutputStream(file)) {
			props.store(out, "Created by " + TesterUtil.class.getName());
		}
	}

	/**
	 * Test the original (previously generated) fitnesses against the current. Also test that the total of the original and current are equal.
	 */
	public static void testOriginalAndCurrentFitnesses(final Properties props, final String mapName, final List<Fitness> currentFitnesses) {

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
				if (fName.equals("originalFitnesses.SimilarityFitnessCore")) {
					continue;
				}
				Assert.assertTrue(seenFitnesses.contains(fName));
			}
		}
	}

	public static boolean validateReloadedState(final LNGScenarioModel original) throws Exception {

		final File f = File.createTempFile("TesterUtil", ".xmi");
		f.deleteOnExit();
		try {
			ServiceHelper.withCheckedOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

				final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
				final Resource resource = resourceSet.createResource(URI.createFileURI(f.getAbsolutePath()));
				resource.getContents().add(EcoreUtil.copy(original));
				ResourceHelper.saveResource(resource);
			});
			return ServiceHelper.withCheckedOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

				final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
				final Resource resource = ResourceHelper.loadResource(resourceSet, URI.createFileURI(f.getAbsolutePath()));
				final EObject loadedCopy = resource.getContents().get(0);

				final Comparison comparison = compareModels(original, loadedCopy);
				return comparison.getDifferences().isEmpty();
			});
		} finally {
			f.delete();
		}
	}

	public static Comparison compareModels(final EObject left, final EObject right) {
		final IEObjectMatcher matcher = DefaultMatchEngine.createDefaultEObjectMatcher(UseIdentifiers.NEVER);
		final IComparisonFactory comparisonFactory = new DefaultComparisonFactory(new DefaultEqualityHelperFactory());

		final IMatchEngine.Factory matchEngineFactory = new MatchEngineFactoryImpl(matcher, comparisonFactory);
		matchEngineFactory.setRanking(20);
		final IMatchEngine.Factory.Registry matchEngineRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineRegistry.add(matchEngineFactory);

		final EMFCompare comparator = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineRegistry).build();
		// Compare the two models
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		return comparator.compare(scope);
	}
}
