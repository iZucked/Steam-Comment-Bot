/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.insertion.SequencesHitchHikerHelper;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

/***
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public class PeriodExporter {

	private static class OffsetRecord {
		public OffsetRecord(@Nullable final IResource first, final int offset, final int size) {
			this.resource = first;
			this.offset = offset;
			this.size = size;
		}

		@Nullable final IResource resource;
		final int offset; // offset - starting index of segment
		final int size; // size - number of elements in the segment
	}

	private final Map<String, ISequenceElement> completeElementMap = new HashMap<>();
	private final Map<IResource, OffsetRecord> periodToCompleteResourceLength = new HashMap<>();
	private final Map<IResource, Pair<IResource, List<ISequenceElement>>> periodToCompleteNominalResourceElements = new HashMap<>();
	private final LNGDataTransformer completeDataTransformer;
	private final LNGDataTransformer periodDataTransformer;
	private final IScenarioEntityMapping periodMapping;

	public PeriodExporter(final LNGDataTransformer completeDataTransformer, final LNGDataTransformer periodDataTransformer, final IScenarioEntityMapping periodMapping) {
		this.completeDataTransformer = completeDataTransformer;
		this.periodDataTransformer = periodDataTransformer;
		this.periodMapping = periodMapping;

		initSequencesMapping(completeDataTransformer.getInitialSequences(), periodDataTransformer.getInitialSequences());
	}

	private void initSequencesMapping(final ISequences completeSequences, final ISequences periodSequences) {
		final Map<String, Pair<@Nullable IResource, Integer>> completeLookup = new HashMap<>();

		// First part, build a mapping between the name of the original elements and the elements so we can lookup it up as the equivalent from the period sceario.
		{
			for (final IResource r : completeSequences.getResources()) {
				final ISequence s = completeSequences.getSequence(r);
				// Skip start and end elements as the name could be clashing
				for (int i = 1; i < s.size() - 1; ++i) {
					final ISequenceElement e = s.get(i);
					assert e != null;

					assert !completeLookup.containsKey(e.getName());
					completeLookup.put(e.getName(), new Pair<>(r, i));

					assert !completeElementMap.containsKey(e.getName());
					completeElementMap.put(e.getName(), e);
				}
			}

			final List<ISequenceElement> unusedElements = completeSequences.getUnusedElements();
			for (int i = 0; i < unusedElements.size(); ++i) {
				final ISequenceElement e = unusedElements.get(i);
				assert e != null;

				assert !completeLookup.containsKey(e.getName());
				completeLookup.put(e.getName(), new Pair<>(null, i));

				assert !completeElementMap.containsKey(e.getName());
				completeElementMap.put(e.getName(), e);
			}
		}

		// Now go through the period and map back to the original. We need to take into account the various changes in the period transformer such as re-numbering charter in market option counts,
		// re-adding cargoes onto separate vessels for P&L purposes (which should not have been changed by the optimiser).

		// Note A key source of issues is trying to match the optimised segments back to the original segment. Sometimes we have bugs where elements on boundaries are classified as both in and out in
		// different parts of the period transformer leading to assertion errors here.

		final IVesselProvider period_vesselProvider = periodDataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider period_virtualVesselSlotProvider = periodDataTransformer.getInjector().getInstance(IVirtualVesselSlotProvider.class);
		final IVesselProvider complete_vesselProvider = completeDataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider complete_virtualVesselSlotProvider = completeDataTransformer.getInjector().getInstance(IVirtualVesselSlotProvider.class);
		final IPortSlotProvider complete_portSlotProvider = completeDataTransformer.getInjector().getInstance(IPortSlotProvider.class);

		{
			for (final IResource period_resource : periodSequences.getResources()) {
				final ISequence s = periodSequences.getSequence(period_resource);
				final int size = s.size() - 2;

				// We could just do the next section, however I prefer to keep it as a sanity
				// check where possible and use the more complex code more often so we can pick
				// up problems with it earlier
				// (i.e. every scenario rather than specific cases).

				// if (size > 0) {
				// final ISequenceElement e = s.get(1);
				// final Pair<IResource, Integer> pair = lookup.get(e.getName());
				// assert pair != null;
				// assert pair.getFirst() != null;
				// periodToCompleteResourceLength.put(period_resource, new
				// Triple<>(pair.getFirst(), pair.getSecond(), size));
				// } else

				{

					final IVesselAvailability period_vesselAvailability = period_vesselProvider.getVesselAvailability(period_resource);
					if (period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
						if (size > 0) {
							final ISequenceElement e = s.get(1);
							assert e != null;

							final Pair<@Nullable IResource, Integer> pair = completeLookup.get(e.getName());
							assert pair != null;
							assert pair.getFirst() != null;

							assert !periodToCompleteResourceLength.containsKey(period_resource);
							periodToCompleteResourceLength.put(period_resource, new OffsetRecord(pair.getFirst(), pair.getSecond(), size));
						} else {
							final ISequenceElement periodE = period_virtualVesselSlotProvider.getElementForVesselAvailability(period_vesselAvailability);
							final ISequenceElement completeE = completeElementMap.get(periodE.getName());

							final IResource complete_resource = complete_vesselProvider.getResource(complete_virtualVesselSlotProvider.getVesselAvailabilityForElement(completeE));
							assert complete_resource != null;

							assert !periodToCompleteResourceLength.containsKey(period_resource);
							periodToCompleteResourceLength.put(period_resource, new OffsetRecord(complete_resource, 1, 0));
						}
					} else if (period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
						if (size > 0) {
							final ISequenceElement e = s.get(1);
							assert e != null;

							final Pair<@Nullable IResource, Integer> pair = completeLookup.get(e.getName());
							assert pair != null;
							assert pair.getFirst() != null;

							assert !periodToCompleteResourceLength.containsKey(period_resource);
							periodToCompleteResourceLength.put(period_resource, new OffsetRecord(pair.getFirst(), pair.getSecond(), size));
						} else {
							final ISequenceElement periodE = period_virtualVesselSlotProvider.getElementForVesselAvailability(period_vesselAvailability);
							final ISequenceElement completeE = completeElementMap.get(periodE.getName());

							final IResource complete_resource = complete_vesselProvider.getResource(complete_virtualVesselSlotProvider.getVesselAvailabilityForElement(completeE));
							assert complete_resource != null;

							assert !periodToCompleteResourceLength.containsKey(period_resource);
							periodToCompleteResourceLength.put(period_resource, new OffsetRecord(complete_resource, 1, 0));
						}
					} else if (period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
							|| period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER //
							|| period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER//
							|| period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {

						// Step one, map the resources between scenarios.
						IResource complete_resource = null;
						Pair<VesselAssignmentType, Integer> key = null;
						if (period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
								|| period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER) {

							final VesselAvailability period_VA = periodDataTransformer.getModelEntityMap().getModelObjectNullChecked(period_vesselAvailability, VesselAvailability.class);

							final @Nullable VesselAvailability complete_VA = periodMapping.getOriginalFromCopy(period_VA);
							if (complete_VA == null) {
								// Probably a re-introduced cargo with duplicate availability.
								continue;
							}
							final IVesselAvailability complete_vesselAvailability = completeDataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(complete_VA, IVesselAvailability.class);

							complete_resource = complete_vesselProvider.getResource(complete_vesselAvailability);

							key = new Pair<>(period_VA, 0);

						} else if (period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER//
								|| period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
							final @Nullable ISpotCharterInMarket period_spotCharterInMarket = period_vesselAvailability.getSpotCharterInMarket();
							final int period_spotIndex = period_vesselAvailability.getSpotIndex();

							final CharterInMarket period_CharterInMarket = periodDataTransformer.getModelEntityMap().getModelObjectNullChecked(period_spotCharterInMarket, CharterInMarket.class);

							final int complete_spotIndex = periodMapping.getSpotCharterInMappingFromPeriod(period_CharterInMarket, period_spotIndex);

							final CharterInMarket complete_CharterterInMarket = periodMapping.getOriginalFromCopy(period_CharterInMarket);
							assert complete_CharterterInMarket != null;

							final ISpotCharterInMarket complete_spotCharterterInMarket = completeDataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(complete_CharterterInMarket,
									ISpotCharterInMarket.class);

							for (final IResource poss_complete_resource : completeSequences.getResources()) {
								final IVesselAvailability complete_vesselAvailability = complete_vesselProvider.getVesselAvailability(poss_complete_resource);
								if (complete_vesselAvailability.getSpotCharterInMarket() == complete_spotCharterterInMarket) {
									if (complete_vesselAvailability.getSpotIndex() == complete_spotIndex) {
										complete_resource = poss_complete_resource;
										key = new Pair<>(period_CharterInMarket, complete_spotIndex);
										break;
									}
								}
							}
						}
						assert complete_resource != null;
						assert key != null;

						// If it is a round trip, there is no coherent segment to extract, rather a set
						// of elements out of an unsorted list. We need to handle these differently.
						if (period_vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
							final List<ISequenceElement> elements = new LinkedList<>();
							if (size > 0) {
								for (int i = 0; i < size; ++i) {
									final ISequenceElement e = s.get(1 + i);
									assert e != null;
									elements.add(completeElementMap.get(e.getName()));
								}
							}

							assert !periodToCompleteNominalResourceElements.containsKey(period_resource);
							assert !periodToCompleteResourceLength.containsKey(period_resource);

							periodToCompleteNominalResourceElements.put(period_resource, new Pair<>(complete_resource, elements));
							periodToCompleteResourceLength.put(period_resource, new OffsetRecord(complete_resource, 1, 0));
							continue;
						}

						final ISequence complete_sequence = completeSequences.getSequence(complete_resource);
						assert complete_sequence != null;

						int start_idx = 0;
						int end_idx = 0;

						@Nullable
						AssignableElement lastTrimmedBefore = periodMapping.getLastTrimmedBefore(key.getFirst(), key.getSecond());
						if (lastTrimmedBefore != null) {
							lastTrimmedBefore = periodMapping.getOriginalFromCopy(lastTrimmedBefore);
						}

						if (lastTrimmedBefore == null) {
							start_idx = 1;
						} else {
							if (lastTrimmedBefore instanceof VesselEvent) {
								final VesselEvent vesselEvent = (VesselEvent) lastTrimmedBefore;
								final IVesselEventPortSlot o_VesselEvent = completeDataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(vesselEvent, IVesselEventPortSlot.class);

								final List<ISequenceElement> eventSequenceElements = o_VesselEvent.getEventSequenceElements();
								final ISequenceElement e = eventSequenceElements.get(eventSequenceElements.size() - 1);
								assert e != null;

								start_idx = completeLookup.get(e.getName()).getSecond() + 1;
							} else if (lastTrimmedBefore instanceof Cargo) {
								final Cargo cargo = (Cargo) lastTrimmedBefore;
								final Slot<?> slot = cargo.getSortedSlots().get(cargo.getSlots().size() - 1);
								final IPortSlot portSlot = completeDataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(slot, IPortSlot.class);

								final ISequenceElement e = complete_portSlotProvider.getElement(portSlot);
								assert e != null;

								start_idx = completeLookup.get(e.getName()).getSecond() + 1;
							}
						}
						@Nullable
						AssignableElement lastTrimmedAfter = periodMapping.getLastTrimmedAfter(key.getFirst(), key.getSecond());
						if (lastTrimmedAfter != null) {
							lastTrimmedAfter = periodMapping.getOriginalFromCopy(lastTrimmedAfter);
						}
						if (lastTrimmedAfter == null) {
							end_idx = complete_sequence.size() - 1;
						} else {
							end_idx = complete_sequence.size() - 1;
							if (lastTrimmedAfter instanceof VesselEvent) {
								final VesselEvent vesselEvent = (VesselEvent) lastTrimmedAfter;
								final IVesselEventPortSlot o_VesselEvent = completeDataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(vesselEvent, IVesselEventPortSlot.class);

								final List<ISequenceElement> eventSequenceElements = o_VesselEvent.getEventSequenceElements();

								final ISequenceElement e = eventSequenceElements.get(0);
								assert e != null;

								end_idx = completeLookup.get(e.getName()).getSecond();
							} else if (lastTrimmedAfter instanceof Cargo) {
								final Cargo cargo = (Cargo) lastTrimmedAfter;
								final Slot<?> slot = cargo.getSortedSlots().get(0);
								final IPortSlot portSlot = completeDataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(slot, IPortSlot.class);

								final ISequenceElement e = complete_portSlotProvider.getElement(portSlot);
								assert e != null;

								end_idx = completeLookup.get(e.getName()).getSecond();
							}
						}

						if (size > 0) {
							final ISequenceElement e = s.get(1);
							assert e != null;

							final Pair<@Nullable IResource, Integer> pair = completeLookup.get(e.getName());
							assert pair != null;
							assert pair.getFirst() != null;

							// Sanity checking asserts
							assert complete_resource == pair.getFirst();
							assert start_idx == pair.getSecond();
							assert end_idx - start_idx == size;
							// periodToCompleteResourceLength.put(period_resource, new Triple<>(pair.getFirst(), pair.getSecond(), size));
						}

						assert !periodToCompleteResourceLength.containsKey(period_resource);
						periodToCompleteResourceLength.put(period_resource, new OffsetRecord(complete_resource, start_idx, end_idx - start_idx));
					}

					{
						// Check a resource only occurs once
						final Set<IResource> seenCompleteResources = new HashSet<>();
						for (final var e : periodToCompleteResourceLength.entrySet()) {
							final IResource completeResource = e.getValue().resource;
							assert completeResource != null;

							if (seenCompleteResources.contains(completeResource)) {
								throw new IllegalStateException("Resource included twice");
							}
							seenCompleteResources.add(completeResource);
						}
					}

					assert SequencesHitchHikerHelper.checkValidSequences(periodSequences);
					assert SequencesHitchHikerHelper.checkValidSequences(completeSequences);
				}
			}
		}
	}

	public ISequences transform(final ISequences rawSequences) {
		final IModifiableSequences completeSequences = new ModifiableSequences(completeDataTransformer.getInitialSequences());
		{
			final ISequences partialSequences = rawSequences;

			// Elements we have put in
			final Set<ISequenceElement> usedElements = new HashSet<>();
			// Elements we have pulled out. Once we have processed the data, removedElements
			// - usedElements == partialSequences.getUnusedElements()
			final Set<ISequenceElement> removedElements = new HashSet<>();

			// Loop over period solution and modify the complete
			final Set<IResource> seenCompleteResources = new HashSet<>();
			for (final IResource r : partialSequences.getResources()) {
				final ISequence seq = partialSequences.getSequence(r);

				final OffsetRecord completeMapping = periodToCompleteResourceLength.get(r);
				if (completeMapping == null) {
					// Assume linked to re-introduced cargo
					continue;
				}

				// Extract the solution segment that represents the equivalent bit of the period
				final IResource completeResource = completeMapping.resource;
				assert completeResource != null;

				if (seenCompleteResources.contains(completeResource)) {
					assert false;
				}
				seenCompleteResources.add(completeResource);

				final IModifiableSequence completeSeq = completeSequences.getModifiableSequence(completeResource);
				if (completeMapping.size > 0) {

					final ISegment s = completeSeq.getSegment(completeMapping.offset, completeMapping.offset + completeMapping.size);
					// Remove it!
					completeSeq.remove(s);
					// Mark elements as removed.
					s.forEach(removedElements::add);
				} else if (periodToCompleteNominalResourceElements.containsKey(r)) {
					final Pair<IResource, List<ISequenceElement>> pair = periodToCompleteNominalResourceElements.get(r);
					for (final ISequenceElement e : pair.getSecond()) {
						assert e != null;

						completeSeq.remove(e);
						removedElements.add(e);
					}
				}
				// Now if we use the resource, construct a reverse order list of the elements
				// and insert back into the gap. (ignoring start and end elements).
				final int size = seq.size() - 2;
				if (size > 0) {
					final List<ISequenceElement> toInsert = new LinkedList<>();
					for (int i = 1; i < seq.size() - 1; ++i) {
						final ISequenceElement partialE = seq.get(i);
						assert partialE != null;
						final ISequenceElement compeleteE = completeElementMap.get(partialE.getName());

						// Potentially if the original scenario does not contain an element with the same name, then this could be null.
						// One possible cause is due to different spot market slots being generated - but hard to see how this could happen in practise.
						// This *could* be the source of the client T NPE issue seen end of March 2021
						assert compeleteE != null;

						toInsert.add(0, compeleteE);
					}
					if (!toInsert.isEmpty()) {
						for (final ISequenceElement e : toInsert) {
							assert e != null;
							completeSeq.insert(completeMapping.offset, e);
						}
						usedElements.addAll(toInsert);
					}
				}
			}

			// Update the unused elements list.''
			completeSequences.getModifiableUnusedElements().removeAll(usedElements);
			removedElements.removeAll(usedElements);

			completeSequences.getModifiableUnusedElements().addAll(removedElements);

			assert !completeSequences.getModifiableUnusedElements().contains(null);
		}
		return completeSequences;
	}
}
