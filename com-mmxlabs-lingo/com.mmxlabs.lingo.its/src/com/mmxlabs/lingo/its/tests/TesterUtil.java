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

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;

import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.optimiser.core.OptimiserConstants;

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
	public static Properties getProperties(URL propertiesURL, boolean create) throws MalformedURLException, IOException {
		if (create) {
			/**
			 * Extend to save properties in a sorted order for ease of reading
			 */
			Properties props = new Properties() {
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

			Properties props = new Properties();
			props.load(propertiesURL.openStream());
			return props;
		}
	}

	public static void saveProperties(@NonNull Properties props, @NonNull File file) throws IOException {
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
				if ( fName.equals("originalFitnesses.SimilarityFitnessCore")) {
					continue;
				}
				Assert.assertTrue(seenFitnesses.contains(fName));
			}
		}
	}
}
