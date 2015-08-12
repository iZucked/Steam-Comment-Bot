package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class ChangeChecker {
//	SimilarityState target;
//	SimilarityState base;
//	ISequences baseFullSequences;
//	List<Difference> fullDifferences;
//	
//	@Inject
//	private IPortTypeProvider portTypeProvider;
//
//	public ChangeChecker() {
//		
//	}
//	
//	public ChangeChecker(SimilarityState base, SimilarityState target, ISequences baseFullSequences) {
//		init(base, target, baseFullSequences);
//	}
//
//	public void init(SimilarityState base, SimilarityState target, ISequences baseFullSequences) {
//		this.base = base;
//		this.target = target;
//		this.baseFullSequences = baseFullSequences;
//		setFullDifferences(baseFullSequences);
//	}
//	
//	public List<Difference> getFullDifferences() {
//		return fullDifferences;
//	}
//	private void setFullDifferences(ISequences sequences) {
//		fullDifferences = findAllDifferences(sequences);
//	}
//	
//	private List<Difference> findAllDifferences(ISequences fullSequences) {
//		List<Difference> differences = new ArrayList<Difference>();
//		int differenceCount = 0;
//		boolean different = false;
//		for (final IResource resource : fullSequences.getResources()) {
//			final ISequence sequence = fullSequences.getSequence(resource.getIndex());
//			ISequenceElement prev = null;
//			int prevIdx = -1;
//			int currIdx = -1;
//			for (final ISequenceElement current : sequence) {
//				currIdx++;
//				if (prev != null) {
//					// Currently only looking at LD style cargoes
//					if (portTypeProvider.getPortType(prev) == PortType.Load && portTypeProvider.getPortType(current) == PortType.Discharge) {
//						// Wiring Change
//						boolean wiringChange = false;
//						final Integer matchedDischarge = target.getDischargeForLoad(prev);
//						final Integer matchedLoad = target.getLoadForDischarge(current);
//						if (matchedDischarge == null && matchedLoad == null) {
//							differences.add(new Difference(DifferenceType.CARGO_NOT_IN_TARGET, prev, current, resource));
//						} else if (matchedLoad == null && matchedDischarge != null) {
//							differences.add(new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, prev, current, resource));
//						} else if (matchedDischarge == null && matchedLoad != null) {
//							differences.add(new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, prev, current, resource));
//						} else if (matchedDischarge != current.getIndex()) {
//							differences.add(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, current, resource));
//							// Has the load moved vessel?
//							int matchedLoadResource = target.getResourceIdxForElement(prev).intValue();
//							int matchedDischargeResource = target.getResourceIdxForElement(current).intValue();
//							if (matchedLoadResource != resource.getIndex()) {
//								// Hash the discharge moved vessel?
//								if (matchedDischargeResource != resource.getIndex()) {
//									// Both load and discharge have moved
//									differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
//									differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
//								} else {
//									differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
//								}
//							} else if (matchedDischargeResource != resource.getIndex()){
//								differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
//							}
//						}
//						// Vessel Change
//						if (!wiringChange) {
//							assert prev != null;
//							if (target.getResourceIdxForElement(prev) == null || target.getResourceIdxForElement(prev).intValue() != resource.getIndex()) {
//								different = true;
//								differenceCount++;
//								// Current Cargo load and discharge can move as a pair.
//								if (target.getResourceIdxForElement(prev).equals(target.getResourceIdxForElement(current))) {
//									differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, current, resource));
//								} else {
//									// Part of a wiring change.
//									// ^^^ above
//								}
//							}
//						}
//					}
//				}
//				prev = current;
//				prevIdx = currIdx;
//			}
//		}
//
//		Deque<ISequenceElement> unusedElements = new LinkedList<ISequenceElement>(fullSequences.getUnusedElements());
//		while (unusedElements.size() > 0) {
//			ISequenceElement element = unusedElements.pop();
//			if (portTypeProvider.getPortType(element).equals(PortType.Load)) {
//				differences.add(new Difference(DifferenceType.ELEMENT_UNUSED_IN_BASE, element, null, null));
//			} else if (portTypeProvider.getPortType(element).equals(PortType.Discharge)){
//				differences.add(new Difference(DifferenceType.ELEMENT_UNUSED_IN_BASE, null, element, null));
//			}
//		}
//		return differences;
//	}
//
//	
//	public ChangeChecker(ChangeChecker original) {
//		this(original.base, original.target, original.baseFullSequences);
//		this.fullDifferences = new ArrayList<Difference>();
//		for (Difference d : original.fullDifferences) {
//			this.fullDifferences.add(new Difference(d.move, d.load, d.discharge, d.resource));
//		}
//	}
//	
//	public static List<Difference> copyDifferenceList(List<Difference> differences) {
//		List<Difference> copy = new ArrayList<Difference>();
//		for (Difference d : differences) {
//			copy.add(new Difference(d.move, d.load, d.discharge, d.resource));
//		}
//		return copy;
//	}
//	
	public enum DifferenceType{
		CARGO_WRONG_VESSEL,
		DISCHARGE_WRONG_VESSEL,
		LOAD_WRONG_VESSEL,
		CARGO_WRONG_WIRING,
		CARGO_NOT_IN_TARGET,
		UNUSED_DISCHARGE_IN_TARGET,
		UNUSED_LOAD_IN_TARGET,
		ELEMENT_UNUSED_IN_BASE
	}
}
