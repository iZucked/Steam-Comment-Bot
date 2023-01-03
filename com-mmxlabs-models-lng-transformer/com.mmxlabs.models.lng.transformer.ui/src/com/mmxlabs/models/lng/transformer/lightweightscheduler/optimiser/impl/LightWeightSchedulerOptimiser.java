/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.minimaxlabs.rnd.representation.LightWeightOutputData;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.common.AbstractOptimiserHelper;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
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
	@Named(OptimiserConstants.DEFAULT_INTERNAL_VESSEL)
	private IVesselCharter pnlVesselCharter;

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
	public Pair<ISequences, Long> optimise(final IVesselCharter pnlVessel, @NonNull final IProgressMonitor monitor, JobExecutor jobExecutor) {
		// Get optimised sequences from our injected sequences optimiser
		final List<List<Integer>> sequences = lightWeightSequenceOptimiser.optimise(lightWeightOptimisationData, constraintCheckers, fitnessFunctions, jobExecutor, monitor);

		// Export the pairings matrix to the raw sequences:

		final ModifiableSequences rawSequences1 = new ModifiableSequences(initialSequences);
		AbstractOptimiserHelper.moveElementsToUnusedList(rawSequences1, portSlotProvider);
		final ModifiableSequences rawSequences = new ModifiableSequences(initialSequences.getResources());
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
	private void updateSequences(@NonNull final IModifiableSequences rawSequences, final List<List<Integer>> sequences, final List<List<IPortSlot>> cargoes, final List<IVesselCharter> vessels) {
		final List<ISequenceElement> unusedElements = rawSequences.getModifiableUnusedElements();

		final Set<ISequenceElement> usedElements = new LinkedHashSet<>();
		for (int vesselIndex = 0; vesselIndex < vessels.size(); vesselIndex++) {
			final IVesselCharter vesselCharter = vessels.get(vesselIndex);
			final IResource o_resource = vesselProvider.getResource(vesselCharter);

			final IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(o_resource);
			modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

			final List<Integer> cargoIndexes = sequences.get(vesselIndex);
			for (final int cargoIndex : cargoIndexes) {
				final List<IPortSlot> cargo = cargoes.get(cargoIndex);

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
				IVesselCharter va = null;
				for (final IPortSlot e : cargo) {
					va = virtualVesselSlotProvider.getVesselCharterForElement(portSlotProvider.getElement(e));
					if (va != null) {
						break;
					}
				}

				assert va != null;
				final IResource o_resource = vesselProvider.getResource(va);
				final IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(o_resource);
				modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

				for (final IPortSlot e : cargo) {
					modifiableSequence.add(portSlotProvider.getElement(e));
					usedElements.add(portSlotProvider.getElement(e));
				}

				modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));
			}
		}

		// Add paired, but unscheduled and put on nominal
		final Map<ILoadOption, IDischargeOption> unscheduledMap = lightWeightOptimisationData.getPairingsMap();
		{
			final IResource o_resource = vesselProvider.getResource(pnlVesselCharter);
			final IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(o_resource);

			if (modifiableSequence.size() == 0) {
				modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));
			} else {
				// Pop last element off, to re-add later
				modifiableSequence.remove(modifiableSequence.get(modifiableSequence.size() - 1));
			}

			for (final Map.Entry<ILoadOption, IDischargeOption> e : unscheduledMap.entrySet()) {
				final ISequenceElement loadElement = portSlotProvider.getElement(e.getKey());

				if (e.getValue() != null && !usedElements.contains(loadElement)) {
					final ISequenceElement dischargeElement = portSlotProvider.getElement(e.getValue());
					modifiableSequence.add(loadElement);
					modifiableSequence.add(dischargeElement);

					usedElements.add(loadElement);
					usedElements.add(dischargeElement);
				}
			}
			modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));
		}

		unusedElements.removeAll(usedElements);

		// Make sure any untouched resources have start/end elements
		for (final IResource o_resource : rawSequences.getResources()) {
			final IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(o_resource);
			if (modifiableSequence.size() == 0) {
				modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));
				modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));
			}
		}
	}

	private static List<List<Integer>> getStoredSequences(final String path) throws IOException {
		LightWeightOutputData readCase = null;
		try (final FileInputStream streamIn = new FileInputStream(path)) {
			try (ObjectInputStream objectinputstream = new ObjectInputStream(streamIn)) {
				readCase = (LightWeightOutputData) objectinputstream.readObject();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return readCase.sequences;
	}

	private void printSequencesToFile(final ISequences sequences) {
		final LocalDateTime date = LocalDateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/sequence-%s-%s-%s.txt", date.getHour(), date.getMinute(), date.getSecond()), "UTF-8");
			for (final IResource iResource : sequences.getResources()) {
				final ISequence sequence = sequences.getSequence(iResource);
				final String seqString = StreamSupport.stream(sequence.spliterator(), false).map(ISequenceElement::getName).collect(Collectors.joining("-"));
				writer.println(seqString);
			}
			final String seqString = StreamSupport.stream(sequences.getUnusedElements().spliterator(), false).sorted((a, b) -> a.getName().compareTo(b.getName())).map(s -> s.getName())
					.collect(Collectors.joining("-"));
			writer.println(seqString);
			writer.close();
		} catch (final FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
