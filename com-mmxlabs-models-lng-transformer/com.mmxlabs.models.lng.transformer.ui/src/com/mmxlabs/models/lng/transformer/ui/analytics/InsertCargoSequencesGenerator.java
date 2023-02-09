/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ui.analytics.marketability.MarketabilityWindowTrimmer;
import com.mmxlabs.models.lng.transformer.ui.analytics.viability.ViabilityWindowTrimmer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.DisconnectedSegment;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.SequencesAttributesProviderImpl;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.impl.EndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.IVoyageSpecificationProvider;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.VoyageSpecificationProviderImpl;

public class InsertCargoSequencesGenerator {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Inject
	private @NonNull IVesselProviderEditor vesselProvider;

	@Inject
	private @NonNull IPortSlotProviderEditor portSlotProvider;

	@Inject
	private @NonNull IStartEndRequirementProviderEditor startEndRequirementProvider;

	@Inject
	private @NonNull ModelEntityMap modelEntityMap;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	private @NonNull IReturnElementProvider returnElementProvider;

	public void generateOptions(final ISequences sequences, final List<ISequenceElement> orderedCargoElements, final IResource targetResource, final ViabilityWindowTrimmer trimmer,
			final IPortSlot portSlot, final ToBooleanFunction<ISequences> action) {

		for (final ISequenceElement e : orderedCargoElements) {
			if (!helper.checkResource(e, targetResource)) {
				return;
			}
		}

		// Get list of preceders for first element
		final Followers<ISequenceElement> preceders = followersAndPreceders.getValidPreceders(orderedCargoElements.get(0));

		// Get list of followers for last element.
		final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(orderedCargoElements.get(orderedCargoElements.size() - 1));

		final ISequence seq = sequences.getSequence(targetResource);
		for (int i = 0; i < seq.size() - 1; ++i) {
			final ISequenceElement before = seq.get(i);
			final ISequenceElement after = seq.get(i + 1);
			if (preceders.contains(before) && followers.contains(after)) {
				// Valid insertion point
				final ModifiableSequences newSequences = new ModifiableSequences(sequences);
				final IModifiableSequence modifiableSequence = newSequences.getModifiableSequence(targetResource);
				modifiableSequence.insert(i + 1, new DisconnectedSegment(orderedCargoElements));
				trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.EARLIEST, 0);
				final boolean earliestValid = action.accept(newSequences);
				trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.LATEST, 0);
				final boolean latestValid = action.accept(newSequences);
				if (earliestValid && !latestValid) {
					for (int j = 0; j < 31; j++) {
						trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.SHIFT, j * 24);
						final boolean newValid = action.accept(newSequences);
						if (newValid)
							break;
					}
				}
			}
		}
	}

	// TODO: CHANGE NAME
	private boolean equalsTest(IPortSlot slot, Slot<?> slot2) {
		IPortSlot oSlot2 = modelEntityMap.getOptimiserObjectNullChecked(slot2, IPortSlot.class);
		return slot == oSlot2;
	}

	private IModifiableSequence createDiversionSequence(final Schedule schedule, final IResource targetResource, final ISequenceElement load, final ISequenceElement saleMarket,
			final ISequence sequence, MarketabilityWindowTrimmer trimmer, final VoyageSpecificationProviderImpl voyageProvider, final Integer vesselSpeed) {

		IVesselCharter vesselCharter = vesselProvider.getVesselCharter(targetResource);

		ISequenceElement nextEvent = null;
		for (int i = 0; i < sequence.size(); i++) {
			if (sequence.get(i) == load) {
				nextEvent = sequence.get(i + 2);
				break;
			}
		}
		assert nextEvent != null;
		if(vesselSpeed != null) {
			clampVesselSpeed(vesselCharter, OptimiserUnitConvertor.convertToInternalSpeed(vesselSpeed));
		}
		ISequenceElement start = startEndRequirementProvider.getStartElement(targetResource);
		ISequenceElement end = startEndRequirementProvider.getEndElement(targetResource);

		IPortSlot nextEventSlot = portSlotProvider.getPortSlot(nextEvent);
		Optional<SlotAllocation> nextEventAllocation = schedule.getSlotAllocations().stream().filter(x -> equalsTest(nextEventSlot, x.getSlot())).findFirst();
		assert load != null;
		{
			IPortSlot loadSlot = portSlotProvider.getPortSlot(load);
			SlotVisit visit = schedule.getSlotAllocations().stream().filter(x -> equalsTest(loadSlot, x.getSlot())).map(SlotAllocation::getSlotVisit).findFirst().orElseThrow();
			int startTime = dateHelper.convertTime(visit.getStart());
			IStartRequirement newStartRequirement = new StartRequirement(loadSlot.getPort(), true, false, new TimeWindow(startTime, startTime + 1),
					vesselCharter.getStartRequirement().getHeelOptions());
			IEndRequirement newEndRequirement = null;
			IStartRequirement threadStartRequirement = startEndRequirementProvider.getStartRequirement(targetResource);
			IEndRequirement threadEndRequirement = startEndRequirementProvider.getEndRequirement(targetResource);

			if (nextEventAllocation.isPresent()) {
				// Next event is not the end event
				SlotVisit nextEventVisit = nextEventAllocation.get().getSlotVisit();

				int endTime = dateHelper.convertTime(nextEventVisit.getStart());
				TimeWindow endWindow = new TimeWindow(endTime, endTime + 1);
				ISequenceElement returnElement = returnElementProvider.getReturnElement(targetResource, nextEventSlot.getPort());
				newEndRequirement = new EndRequirement(List.of(nextEventSlot.getPort()), true, true, endWindow, (vesselCharter.getEndRequirement().getHeelOptions()));
				end = returnElement;
				assert end != null;
			} else {
				newEndRequirement = null;
			}

			if (threadStartRequirement instanceof ThreadLocalStartRequirement sRequirement && threadEndRequirement instanceof ThreadLocalEndRequirement eRequirement) {
				sRequirement.setStartRequirement(newStartRequirement);
				eRequirement.setEndRequirment(newEndRequirement);
			}
		}
		
		IPortSlot portSlot = portSlotProvider.getPortSlot(load);
		SlotVisit visit = schedule.getSlotAllocations().stream().filter(x -> equalsTest(portSlot, x.getSlot())).map(SlotAllocation::getSlotVisit).findFirst().orElseThrow();
		int loadTime = dateHelper.convertTime(visit.getStart());
		voyageProvider.setArrivalTime(portSlot, loadTime);
		return new ListModifiableSequence(List.of(start, load, saleMarket, end));

	}

	private void clampVesselSpeed(IVesselCharter vc, int maxSpeed) {
		IVessel vessel = vc.getVessel();
		if (!(vessel instanceof ThreadLocalVessel)) {
			throw new IllegalArgumentException();
		}
		ThreadLocalVessel oVessel = (ThreadLocalVessel) vessel;
		oVessel.setMinSpeed(maxSpeed);
		oVessel.setMaxSpeed(maxSpeed);
	}

	// TODO: CHANGE THIS NAME
	@NonNullByDefault
	public void generateOptionsTemp(final Schedule schedule, final ISequences sequences, final List<@NonNull ISequenceElement> orderedCargoElements, final IResource targetResource,
			MarketabilityWindowTrimmer trimmer, final IPortSlot portSlot, final Integer vesselSpeed, final ToBooleanFunction<ISequences> action) {

		for (final ISequenceElement e : orderedCargoElements) {
			if (!helper.checkResource(e, targetResource)) {
				return;
			}
		}
		final ISequence seq = sequences.getSequence(targetResource);
		final ISequenceElement load = orderedCargoElements.get(0);
		final ISequenceElement salesMarket = orderedCargoElements.get(orderedCargoElements.size() - 1);
		assert salesMarket != null;
		final VoyageSpecificationProviderImpl voyageSpecificationProvider = new VoyageSpecificationProviderImpl();

		final IModifiableSequence modifiableSequence = createDiversionSequence(schedule, targetResource, load, salesMarket, seq, trimmer, voyageSpecificationProvider, vesselSpeed);
		final Map<IResource, IModifiableSequence> sequenceMap = new HashMap<>();
		sequenceMap.put(targetResource, modifiableSequence);
		final SequencesAttributesProviderImpl providers = new SequencesAttributesProviderImpl();
		providers.addProvider(IVoyageSpecificationProvider.class, voyageSpecificationProvider);

		final IModifiableSequences newSequences = new ModifiableSequences(List.of(targetResource), sequenceMap, new ArrayList<ISequenceElement>(), providers);

		trimmer.setTrim(portSlot, MarketabilityWindowTrimmer.Mode.EARLIEST, 0);
		final boolean earliestValid = action.accept(newSequences);
		trimmer.setTrim(portSlot, MarketabilityWindowTrimmer.Mode.LATEST, 0);
		final boolean latestValid = action.accept(newSequences);
		//FIXME: ONLY FINDS RESULT TO THE NEAREST DAY
		if (earliestValid && !latestValid) {
			for (int j = 0; j < 31; j++) {
				trimmer.setTrim(portSlot, MarketabilityWindowTrimmer.Mode.SHIFT, j * 24);
				final boolean newValid = action.accept(newSequences);
				if (newValid)
					break;
			}
		}
		trimmer.resetTrim();
	}
}
