/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.minimaxlabs.rnd.representation.LightWeightOutputData;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightSequenceOptimiser;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

/**
 * Light weight scheduling optimisation.
 * 
 * @author achurchill
 *
 */
public class LightWeightSchedulerOptimiser {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private ILightWeightSequenceOptimiser lightWeightSequenceOptimiser;

	@Inject
	private ILightWeightOptimisationData lightWeightOptimisationData;

	@Inject
	private List<ILightWeightConstraintChecker> constraintCheckers;

	@Inject
	private List<ILightWeightFitnessFunction> fitnessFunctions;

	@Inject
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	private ISequences initialSequences;

	@Inject
	@Named(OptimiserConstants.DEFAULT_VESSEL)
	private IVesselAvailability pnlVesselAvailability;

	/**
	 * Perform a long term (nominal) trading optimisation
	 * 
	 * @param monitor
	 * 
	 * @param executorService
	 * @param dataTransformer
	 * @param charterInMarket
	 * @return
	 */
	public Pair<ISequences, Long> optimise(final IVesselAvailability pnlVessel, @NonNull IProgressMonitor monitor) {
		// Get optimised sequences from our injected sequences optimiser
		List<List<Integer>> sequences = lightWeightSequenceOptimiser.optimise(lightWeightOptimisationData, constraintCheckers, fitnessFunctions, monitor);

		// Export the pairings matrix to the raw sequences:

		ModifiableSequences rawSequences1 = new ModifiableSequences(initialSequences);
		LightWeightOptimiserHelper.moveElementsToUnusedList(rawSequences1, portSlotProvider);
		ModifiableSequences rawSequences = new ModifiableSequences(initialSequences.getResources());
		rawSequences.getModifiableUnusedElements().addAll(rawSequences1.getUnusedElements());

		// (a) update shipped
		updateSequences(rawSequences, sequences, lightWeightOptimisationData.getShippedCargoes(), lightWeightOptimisationData.getVessels());

		return new Pair<>(rawSequences, 0L);
	}

	/**
	 * Updates the raw sequences given an allocations matrix
	 * 
	 * @param rawSequences
	 * @param sequences
	 * @param cargoes
	 * @param vessels
	 * @param pairingsMap
	 * @param nominal
	 */
	private void updateSequences(@NonNull IModifiableSequences rawSequences, List<List<Integer>> sequences, List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels) {
		List<ISequenceElement> unusedElements = rawSequences.getModifiableUnusedElements();

		Set<ISequenceElement> usedElements = new HashSet<>();
		for (int vesselIndex = 0; vesselIndex < vessels.size(); vesselIndex++) {
			IVesselAvailability vesselAvailability = vessels.get(vesselIndex);
			IResource o_resource = vesselProvider.getResource(vesselAvailability);

			IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(o_resource);
			modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

			List<Integer> cargoIndexes = sequences.get(vesselIndex);
			for (int cargoIndex : cargoIndexes) {
				List<IPortSlot> cargo = cargoes.get(cargoIndex);

				for (final IPortSlot e : cargo) {
					modifiableSequence.add(portSlotProvider.getElement(e));
					usedElements.add(portSlotProvider.getElement(e));
				}
			}
			modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));
		}
		{
			for (final List<IPortSlot> cargo : lightWeightOptimisationData.getNonShippedCargoes()) {
				// Grab FOB/DES vessel
				IVesselAvailability va = null;
				for (final IPortSlot e : cargo) {
					va = virtualVesselSlotProvider.getVesselAvailabilityForElement(portSlotProvider.getElement(e));
					if (va != null) {
						break;
					}
				}

				assert va != null;
				IResource o_resource = vesselProvider.getResource(va);
				final IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(o_resource);
				modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

				for (final IPortSlot e : cargo) {
					modifiableSequence.add(portSlotProvider.getElement(e));
					usedElements.add(portSlotProvider.getElement(e));
				}

				modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));
			}
		}

		unusedElements.removeAll(usedElements);

		// Make sure any untouched resources have start/end elements
		for (IResource o_resource : rawSequences.getResources()) {
			final IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(o_resource);
			if (modifiableSequence.size() == 0) {
				modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));
				modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));
			}
		}
	}

	private static List<List<Integer>> getStoredSequences(String path) throws IOException {
		ObjectInputStream objectinputstream = null;
		LightWeightOutputData readCase = null;
		try {
			FileInputStream streamIn = new FileInputStream(path);
			objectinputstream = new ObjectInputStream(streamIn);
			readCase = (LightWeightOutputData) objectinputstream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectinputstream != null) {
				objectinputstream.close();
			}
		}
		return readCase.sequences;
	}

	private void printSequencesToFile(ISequences sequences) {
		DateTime date = DateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/sequence-%s-%s-%s.txt", date.getHourOfDay(), date.getMinuteOfHour(), date.getSecondOfMinute()), "UTF-8");
			for (IResource iResource : sequences.getResources()) {
				ISequence sequence = sequences.getSequence(iResource);
				String seqString = StreamSupport.stream(sequence.spliterator(), false).map(s -> s.getName()).collect(Collectors.joining("-"));
				writer.println(seqString);
			}
			String seqString = StreamSupport.stream(sequences.getUnusedElements().spliterator(), false).sorted((a, b) -> a.getName().compareTo(b.getName())).map(s -> s.getName())
					.collect(Collectors.joining("-"));
			writer.println(seqString);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
