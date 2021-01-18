/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.utils;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class OptimiserDebugTools {

	private OptimiserDebugTools() {
	}

	public static void checkLDOnSequence(IModifiableSequences currentRawSequences, String resourceName, String loadName, String DischargeName) {
		List<@NonNull IResource> resources = currentRawSequences.getResources();
		for (IResource resource : resources) {
			if (resource.getName().contains(resourceName)) {
				if (currentRawSequences.getSequence(resource).size() < 4) {
					continue;
				}
				int i = 0;
				for (ISequenceElement sequenceElement : currentRawSequences.getSequence(resource)) {
					if (sequenceElement.getName().contains(loadName) && i < currentRawSequences.getSequence(resource).size() - 1
							&& currentRawSequences.getSequence(resource).get(i + 1).getName().contains(DischargeName)) {
						// debug checkpoint
					}
					i++;
				}
			}

		}
	}
}
