/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;

public class HintManager {

	@Inject
	private IPortTypeProvider portTypeProvider;

	private Collection<ISequenceElement> problemElements = new LinkedHashSet<>();

	private Collection<ISequenceElement> suggestedElements = new LinkedHashSet<>();

	private Collection<ISequenceElement> usedElements = new LinkedHashSet<>();

	public Collection<ISequenceElement> getUsedElements() {
		return usedElements;
	}

	public Collection<ISequenceElement> getProblemElements() {
		return problemElements;
	}

	public Collection<ISequenceElement> getSuggestedElements() {
		return suggestedElements;
	}

	public void chain(@NonNull Hints hints) {
		problemElements.addAll(hints.getProblemElements());
		suggestedElements.addAll(hints.getSuggestedElements());
		usedElements.addAll(hints.getUsedElements());

		// Filter
		{
			Iterator<ISequenceElement> itr = problemElements.iterator();
			while (itr.hasNext()) {
				ISequenceElement e = itr.next();
				if (!GuidedMoveGenerator.validElementTypes.contains(portTypeProvider.getPortType(e))) {
					itr.remove();
				}
			}
		}
		{
			Iterator<ISequenceElement> itr = suggestedElements.iterator();
			while (itr.hasNext()) {
				ISequenceElement e = itr.next();
				if (!GuidedMoveGenerator.validElementTypes.contains(portTypeProvider.getPortType(e))) {
					itr.remove();
				}
			}
		}

	}

	public void reset() {
		problemElements.clear();
		suggestedElements.clear();
		usedElements.clear();
	}
}
