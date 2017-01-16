/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class ChangeChecker {
	SimilarityState target;
	SimilarityState base;
	ISequences baseRawSequences;
	List<Difference> fullDifferences;

	@Inject
	private IPortTypeProvider portTypeProvider;

	public ChangeChecker() {

	}

	public ChangeChecker(final SimilarityState base, final SimilarityState target, final ISequences baseRawSequences) {
		init(base, target, baseRawSequences);
	}

	public void init(final SimilarityState base, final SimilarityState target, final ISequences baseRawSequences) {
		this.base = base;
		this.target = target;
		this.baseRawSequences = baseRawSequences;
		setDifferences(baseRawSequences);
	}

	public List<Difference> getFullDifferences() {
		return fullDifferences;
	}

	private void setDifferences(final ISequences rawSequences) {
		fullDifferences = findAllDifferences(rawSequences);
	}

	private List<Difference> findAllDifferences(final ISequences rawSequences) {
		final List<Difference> differences = new ArrayList<Difference>();
		int differenceCount = 0;
		boolean different = false;
		for (final IResource resource : rawSequences.getResources()) {
			final ISequence sequence = rawSequences.getSequence(resource);
			ISequenceElement prev = null;
			int currIdx = -1;
			for (final ISequenceElement current : sequence) {
				currIdx++;
				if (prev != null) {
					// Currently only looking at LD style cargoes
					if (portTypeProvider.getPortType(prev) == PortType.Load && portTypeProvider.getPortType(current) == PortType.Discharge) {
						// Wiring Change
						final boolean wiringChange = false;
						final Integer matchedDischarge = target.getDischargeForLoad(prev);
						final Integer matchedLoad = target.getLoadForDischarge(current);
						if (matchedDischarge == null && matchedLoad == null) {
							differences.add(new Difference(DifferenceType.CARGO_NOT_IN_TARGET, prev, current, resource));
							prev = current;
							continue;
						} else if (matchedLoad == null && matchedDischarge != null) {
							differences.add(new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, prev, current, resource));
							prev = current;
							continue;
						} else if (matchedDischarge == null && matchedLoad != null) {
							differences.add(new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, prev, current, resource));
							prev = current;
							continue;
						} else if (matchedDischarge != current.getIndex()) {
							differences.add(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, current, resource));
							// Has the load moved vessel?
							final IResource matchedLoadResource = target.getResourceForElement(prev);
							final IResource matchedDischargeResource = target.getResourceForElement(current);
							if (matchedLoadResource != resource) {
								// Hash the discharge moved vessel?
								if (matchedDischargeResource != resource) {
									// Both load and discharge have moved
									differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
									differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
									prev = current;
									continue;
								} else {
									differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
									prev = current;
									continue;
								}
							} else if (matchedDischargeResource != resource) {
								differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
								prev = current;
								continue;
							}
							prev = current;
							continue;
						}
						// Vessel Change
						if (!wiringChange) {
							assert prev != null;
							if (target.getResourceForElement(prev) == null || target.getResourceForElement(prev) != resource) {
								different = true;
								differenceCount++;
								// System.out.println("prev:"+prev);
								// System.out.println("current:"+current);
								// System.out.println("md:"+matchedDischarge);
								// System.out.println("ml:"+matchedLoad);
								// Current Cargo load and discharge can move as a pair.
								if (target.getResourceForElement(prev).equals(target.getResourceForElement(current))) {
									// if (prev.getName().contains("N210")) {
									// int z = 0;
									// }
									differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, current, resource));
									prev = current;
									continue;
								} else {
									// Part of a wiring change.
									// ^^^ above
								}
							}
						}
					}
				}
				prev = current;
			}
		}

		final Deque<ISequenceElement> unusedElements = new LinkedList<ISequenceElement>(rawSequences.getUnusedElements());
		while (unusedElements.size() > 0) {
			final ISequenceElement element = unusedElements.pop();
			if (portTypeProvider.getPortType(element).equals(PortType.Load) && target.getDischargeForLoad(element) != null) {
				differences.add(new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, element, null, null));
			} else if (portTypeProvider.getPortType(element).equals(PortType.Discharge) && target.getLoadForDischarge(element) != null) {
				differences.add(new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, element, null));
			}
		}
		// for (Difference d : differences) {
		// // if (d.toString().contains("CARGO_WRONG_VESSEL N210-Bonny STA03-Bonny Resource virtual-N158-Bonny")) {
		// // int z = 0;
		// // }
		// System.out.println(d);
		// }
		// System.out.println("------------------");
		return differences;
	}

	public ChangeChecker(final ChangeChecker original) {
		this(original.base, original.target, original.baseRawSequences);
		this.fullDifferences = new ArrayList<Difference>();
		for (final Difference d : original.fullDifferences) {
			this.fullDifferences.add(new Difference(d.move, d.load, d.discharge, d.currentResource));
		}
	}

	public static List<Difference> copyDifferenceList(final List<Difference> differences) {
		final List<Difference> copy = new ArrayList<>();
		for (final Difference d : differences) {
			copy.add(new Difference(d.move, d.load, d.discharge, d.currentResource));
		}
		return copy;
	}

	public static enum DifferenceType {
		CARGO_WRONG_VESSEL, DISCHARGE_WRONG_VESSEL, LOAD_WRONG_VESSEL, CARGO_WRONG_WIRING, CARGO_NOT_IN_TARGET, UNUSED_DISCHARGE_IN_TARGET, UNUSED_LOAD_IN_TARGET, DISCHARGE_UNUSED_IN_BASE, LOAD_UNUSED_IN_BASE,
	}
}
