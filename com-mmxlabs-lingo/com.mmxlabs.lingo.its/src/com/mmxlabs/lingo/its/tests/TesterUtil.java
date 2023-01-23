/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
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
import org.junit.jupiter.api.Assertions;

import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.util.ResourceHelper;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

public class TesterUtil {

	private TesterUtil() {

	}

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
	public static Properties getProperties(final URL propertiesURL, final boolean create) throws IOException {
		if (create) {
			/**
			 * Extend to save properties in a sorted order for ease of reading Copy and
			 * paste so much due to private methods. :(
			 */
			return new Properties() {

				private void writeComments(BufferedWriter bw, String comments) throws IOException {
					bw.write("#");
					int len = comments.length();
					int current = 0;
					int last = 0;
					char[] uu = new char[6];
					uu[0] = '\\';
					uu[1] = 'u';
					while (current < len) {
						char c = comments.charAt(current);
						if (c > '\u00ff' || c == '\n' || c == '\r') {
							if (last != current)
								bw.write(comments.substring(last, current));
							if (c > '\u00ff') {
								uu[2] = toHex((c >> 12) & 0xf);
								uu[3] = toHex((c >> 8) & 0xf);
								uu[4] = toHex((c >> 4) & 0xf);
								uu[5] = toHex(c & 0xf);
								bw.write(new String(uu));
							} else {
								bw.newLine();
								if (c == '\r' && current != len - 1 && comments.charAt(current + 1) == '\n') {
									current++;
								}
								if (current == len - 1 || (comments.charAt(current + 1) != '#' && comments.charAt(current + 1) != '!'))
									bw.write("#");
							}
							last = current + 1;
						}
						current++;
					}
					if (last != current)
						bw.write(comments.substring(last, current));
					bw.newLine();
				}

				private void store0(BufferedWriter bw, String comments, boolean escUnicode) throws IOException {
					if (comments != null) {
						writeComments(bw, comments);
					}
					bw.write("#" + new Date().toString());
					bw.newLine();
					synchronized (this) {
						for (Object e : new TreeSet<Object>(super.keySet())) {
							// for (Map.Entry<Object, Object> e : entrySet()) {
							String key = (String) e;
							String val = getProperty(key);
							key = saveConvert(key, true, escUnicode);
							/*
							 * No need to escape embedded and trailing spaces for value, hence pass false to
							 * flag.
							 */
							val = saveConvert(val, false, escUnicode);
							bw.write(key + "=" + val);
							bw.newLine();
						}
					}
					bw.flush();
				}

				/*
				 * Converts unicodes to encoded &#92;uxxxx and escapes special characters with a
				 * preceding slash
				 */
				private String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode) {
					int len = theString.length();
					int bufLen = len * 2;
					if (bufLen < 0) {
						bufLen = Integer.MAX_VALUE;
					}
					StringBuilder outBuffer = new StringBuilder(bufLen);

					for (int x = 0; x < len; x++) {
						char aChar = theString.charAt(x);
						// Handle common case first, selecting largest block that
						// avoids the specials below
						if ((aChar > 61) && (aChar < 127)) {
							if (aChar == '\\') {
								outBuffer.append('\\');
								outBuffer.append('\\');
								continue;
							}
							outBuffer.append(aChar);
							continue;
						}
						switch (aChar) {
						case ' ':
							if (x == 0 || escapeSpace)
								outBuffer.append('\\');
							outBuffer.append(' ');
							break;
						case '\t':
							outBuffer.append('\\');
							outBuffer.append('t');
							break;
						case '\n':
							outBuffer.append('\\');
							outBuffer.append('n');
							break;
						case '\r':
							outBuffer.append('\\');
							outBuffer.append('r');
							break;
						case '\f':
							outBuffer.append('\\');
							outBuffer.append('f');
							break;
						case '=': // Fall through
						case ':': // Fall through
						case '#': // Fall through
						case '!':
							outBuffer.append('\\');
							outBuffer.append(aChar);
							break;
						default:
							if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
								outBuffer.append('\\');
								outBuffer.append('u');
								outBuffer.append(toHex((aChar >> 12) & 0xF));
								outBuffer.append(toHex((aChar >> 8) & 0xF));
								outBuffer.append(toHex((aChar >> 4) & 0xF));
								outBuffer.append(toHex(aChar & 0xF));
							} else {
								outBuffer.append(aChar);
							}
						}
					}
					return outBuffer.toString();
				}

				/**
				 * Convert a nibble to a hex character
				 * 
				 * @param nibble the nibble to convert.
				 */
				private char toHex(int nibble) {
					return hexDigit[(nibble & 0xF)];
				}

				/** A table of hex digits */
				private final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void store(OutputStream out, String comments) throws IOException {
					store0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")), comments, true);
				}

				@Override
				public Set<Object> keySet() {
					return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
				}

				@Override
				public Set<java.util.Map.Entry<Object, Object>> entrySet() {
					TreeSet<java.util.Map.Entry<Object, Object>> treeSet = new TreeSet<>((a, b) -> {
						return ((Comparable) a.getKey()).compareTo((Comparable) b.getKey());
					});
					treeSet.addAll(super.entrySet());
					return Collections.unmodifiableSet(treeSet);
				}

				@Override
				public synchronized Enumeration<Object> keys() {
					return Collections.enumeration(new TreeSet<Object>(super.keySet()));
				}
			};
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
	 * Test the original (previously generated) fitnesses against the current. Also
	 * test that the total of the original and current are equal.
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
		final Set<String> seenFitnesses = new HashSet<>();
		for (final Fitness f : currentFitnesses) {

			// get the values
			final long originalFitnessValue = Long.parseLong(props.getProperty(mapName + "." + f.getName(), "0"));
			final long currentFitness = f.getFitnessValue();

			// test they are equal
			Assertions.assertEquals(originalFitnessValue, currentFitness, f.getName() + " - Previous fitness matches current fitness");

			// add to total
			totalCurrentFitness += currentFitness;

			seenFitnesses.add(mapName + "." + f.getName());
		}

		// test totals are equal
		Assertions.assertEquals(totalOriginalFitness, totalCurrentFitness, "Total original fitnesses equal current fitnesses");

		// Check that the expected fitness names appeared in the output
		for (final Object fName : props.keySet()) {
			final String str = fName.toString();
			if (str.startsWith(mapName + ".")) {
				if (fName.equals("originalFitnesses.SimilarityFitnessCore")) {
					continue;
				}
				if (fName.equals("endFitnesses.SimilarityFitnessCore")) {
					continue;
				}
				boolean b = seenFitnesses.contains(fName);
				Assertions.assertTrue(b);
			}
		}
	}

	public static boolean validateReloadedState(final IScenarioDataProvider original) throws Exception {

		final File f = File.createTempFile("TesterUtil", ".xmi");
		f.deleteOnExit();
		try {
			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {

				final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
				final Resource resource = resourceSet.createResource(URI.createFileURI(f.getAbsolutePath()));
				resource.getContents().add(EcoreUtil.copy(original.getScenario()));
				ResourceHelper.saveResource(resource);
			});
			return ServiceHelper.withCheckedOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

				final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
				final Resource resource = ResourceHelper.loadResource(resourceSet, URI.createFileURI(f.getAbsolutePath()));
				final EObject loadedCopy = resource.getContents().get(0);

				final Comparison comparison = compareModels(original.getScenario(), loadedCopy);
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
