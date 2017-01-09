/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Simple text based {@link ISequences} serialiser. Given an equivalent {@link IOptimisationData} instance, use the uniquely identified resources and element names to save & load the sequences. Data
 * is stored simple as line pairs, resource name, followed by element name. A special constant is used for the unused elements. The text file order is the element order.
 * 
 * @author Simon Goodall
 *
 */
public final class SequencesSerialiser {

	private static final @NonNull String UNUSED = "UNUSED";

	public static void save(final @NonNull IOptimisationData optimisationData, final @NonNull ISequences rawSequences, final @NonNull OutputStream out) throws Exception {
		try (PrintWriter pw = new PrintWriter(out)) {

			// Used for sanity checking uniqueness
			Set<String> seenResources = new HashSet<>();
			Set<String> seenElements = new HashSet<>();

			for (final IResource resource : optimisationData.getResources()) {
				assert seenResources.add(resource.getName());
				// Make sure this special constant has not been used
				assert !UNUSED.equals(resource.getName());
				final ISequence s = rawSequences.getSequence(resource);
				for (final ISequenceElement element : s) {
					assert seenElements.add(element.getName());

					pw.println(resource.getName());
					pw.println(element.getName());
				}
			}
			for (final ISequenceElement element : rawSequences.getUnusedElements()) {
				pw.println(UNUSED);
				pw.println(element.getName());
				assert seenElements.add(element.getName());
			}
		}
	}

	public static @NonNull ISequences load(final @NonNull IOptimisationData optimisationData, final @NonNull InputStream in) throws Exception {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

			final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources());

			final Map<String, IResource> resourceMap = new HashMap<>();
			final Map<String, ISequenceElement> sequenceElementMap = new HashMap<>();

			for (final IResource r : optimisationData.getResources()) {
				resourceMap.put(r.getName(), r);
			}

			// Note: This does not include generated charter out elements, but this should be ok
			for (final ISequenceElement e : optimisationData.getSequenceElements()) {
				sequenceElementMap.put(e.getName(), e);
			}

			String line = br.readLine();
			while (line != null && !line.isEmpty()) {
				final String resourceName = line;
				assert resourceName != null;

				final String elementName = br.readLine();
				assert elementName != null;

				final ISequenceElement e = sequenceElementMap.get(elementName);
				assert e != null;
				if (resourceName.equals(UNUSED)) {
					sequences.getModifiableUnusedElements().add(e);
				} else {
					final IResource r = resourceMap.get(resourceName);
					assert r != null;
					sequences.getModifiableSequence(r).add(e);
				}

				line = br.readLine();
			}
			return sequences;
		}
	}

}
