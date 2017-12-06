package com.mmxlabs.models.lng.transformer.ui.common;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.CargoChange;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.OpenSlotChange;
import com.mmxlabs.models.lng.analytics.PositionDescriptor;
import com.mmxlabs.models.lng.analytics.RealSlotDescriptor;
import com.mmxlabs.models.lng.analytics.SlotType;
import com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor;
import com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.VesselEventChange;
import com.mmxlabs.models.lng.analytics.VesselEventDescriptor;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class SequencesToChangeDescriptionTransformer {
	final ScheduleSpecificationHelper scheduleSpecificationHelper;
	private @NonNull final LNGDataTransformer dataTransformer;
	private Function<ISequences, ChangeDescription> generateChangeDescription;

	public SequencesToChangeDescriptionTransformer(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		this.dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		this.scheduleSpecificationHelper = new ScheduleSpecificationHelper(scenarioToOptimiserBridge.getScenarioDataProvider());
	}

	private Function<ISequences, ChangeDescription> createChangeDescriptionFunction(final ISequences baseSolution) {

		// Create a small injector to check hold of the IMoveHandlerHelper instance.
		final Injector injector = dataTransformer.getInjector().createChildInjector(new SequencesManipulatorModule(), new AbstractModule() {

			@Override
			protected void configure() {
				bind(MoveHelper.class).in(Singleton.class);
				bind(IMoveHelper.class).to(MoveHelper.class);

				bind(boolean.class).annotatedWith(Names.named(MoveHelper.LEGACY_CHECK_RESOURCE)).toInstance(Boolean.FALSE);

				bind(MoveHandlerHelper.class).in(Singleton.class);
				bind(IMoveHandlerHelper.class).to(MoveHandlerHelper.class);
			}
		});

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IPortTypeProvider portTypeProvider = injector.getInstance(IPortTypeProvider.class);
		final ISpotMarketSlotsProvider spotMarketSlotsProvider = injector.getInstance(ISpotMarketSlotsProvider.class);
		// May need to get a thread based hook
		final IMoveHandlerHelper moveHandlerHelper = injector.getInstance(IMoveHandlerHelper.class);

		final Map<ISequenceElement, Pair<IResource, List<ISequenceElement>>> elementToSegment = new HashMap<>();
		// Build map of elements to cargo/event sequences for base case.
		for (final IResource r : baseSolution.getResources()) {
			final ISequence seq = baseSolution.getSequence(r);
			for (int idx = 0; idx < seq.size(); ++idx) {
				final ISequenceElement e = seq.get(idx);
				if (portTypeProvider.getPortType(e) == PortType.Load //
						|| portTypeProvider.getPortType(e) == PortType.Discharge //
						|| portTypeProvider.getPortType(e) == PortType.DryDock //
						|| portTypeProvider.getPortType(e) == PortType.Maintenance //
						|| portTypeProvider.getPortType(e) == PortType.CharterOut //
				) {
					final List<ISequenceElement> elementList = moveHandlerHelper.extractSegment(seq, e);
					for (final ISequenceElement e2 : elementList) {
						elementToSegment.put(e2, new Pair<>(r, elementList));
					}
					// Increment by list size minus one to skip past this segment. Also take into account element position in list.
					idx += elementList.size() - elementList.indexOf(e) - 1;
				}
			}
		}

		return (option) -> {
			final Set<ISequenceElement> seenOptionElements = new HashSet<>(); // Changed elements in the option
			final Set<ISequenceElement> seenBaseElements = new HashSet<>(); // Changes elements in the base

			final ChangeDescription changeDescription = AnalyticsFactory.eINSTANCE.createChangeDescription();

			for (final IResource r : option.getResources()) {

				PositionDescriptor lastPositionDescriptor = null;
				String lastID = null;
				final ISequence seq = option.getSequence(r);
				for (int idx = 0; idx < seq.size(); ++idx) {
					final ISequenceElement seq_e = seq.get(idx);

					final PortType seq_e_portType = portTypeProvider.getPortType(seq_e);
					if (seq_e_portType == PortType.Load //
							|| seq_e_portType == PortType.Discharge //
							|| seq_e_portType == PortType.DryDock //
							|| seq_e_portType == PortType.Maintenance //
							|| seq_e_portType == PortType.CharterOut //
					) {
						String currentLastID = lastID;
						IPortSlot seq_e_portSlot = portSlotProvider.getPortSlot(seq_e);
						if (seq_e_portType == PortType.Load //
								|| seq_e_portType == PortType.Discharge) {
							Slot slot = dataTransformer.getModelEntityMap().getModelObjectNullChecked(seq_e_portSlot, Slot.class);
							lastID = slot.getName();
						} else {
							VesselEvent event = dataTransformer.getModelEntityMap().getModelObjectNullChecked(seq_e_portSlot, VesselEvent.class);
							lastID = event.getName();
						}

						if (seenOptionElements.contains(seq_e)) {
							continue;
						}

						if (lastPositionDescriptor != null) {
							lastPositionDescriptor.setBefore(lastID);
							lastPositionDescriptor = null;
						}
						final List<ISequenceElement> elementList = moveHandlerHelper.extractSegment(seq, seq_e);
						seenOptionElements.addAll(elementList);

						final Pair<IResource, List<ISequenceElement>> p = elementToSegment.get(seq_e);

						// TODO: Record positional information and diff. - tricky! It is only a change if nothing else has changed. Position can change indirectly due to other cargoes moving about
						if (p == null || p.getFirst() != r || !p.getSecond().equals(elementList)) {

							final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(r);
							final ISpotCharterInMarket o_spotCharterInMarket = vesselAvailability.getSpotCharterInMarket();
							VesselAllocationDescriptor vesselDescriptor = null;
							if (o_spotCharterInMarket != null) {
								final MarketVesselAllocationDescriptor marketVesselDescriptor = AnalyticsFactory.eINSTANCE.createMarketVesselAllocationDescriptor();
								// TODO: Check this is the correct string. Use
								final CharterInMarket charterInMarket = dataTransformer.getModelEntityMap().getModelObject(o_spotCharterInMarket, CharterInMarket.class);
								marketVesselDescriptor.setMarketName(charterInMarket.getName());
								marketVesselDescriptor.setSpotIndex(vesselAvailability.getSpotIndex());
								vesselDescriptor = marketVesselDescriptor;
							} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER) {
								final FleetVesselAllocationDescriptor fleetVesselDescriptor = AnalyticsFactory.eINSTANCE.createFleetVesselAllocationDescriptor();
								final VesselAvailability e_vesselAvailability = dataTransformer.getModelEntityMap().getModelObject(vesselAvailability, VesselAvailability.class);
								fleetVesselDescriptor.setName(e_vesselAvailability.getVessel().getName());
								fleetVesselDescriptor.setCharterIndex(e_vesselAvailability.getCharterNumber());
								vesselDescriptor = fleetVesselDescriptor;
							}

							// There has been some kind of change.
							if (seq_e_portType == PortType.Load //
									|| seq_e_portType == PortType.Discharge //
							) {
								// Cargo change
								final CargoChange change = AnalyticsFactory.eINSTANCE.createCargoChange();
								for (final ISequenceElement element : elementList) {

									final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
									final Slot slot = dataTransformer.getModelEntityMap().getModelObject(portSlot, Slot.class);
									SlotType slotType = null;
									if (slot instanceof LoadSlot) {
										final LoadSlot loadSlot = (LoadSlot) slot;
										slotType = loadSlot.isDESPurchase() ? SlotType.DES_PURCHASE : SlotType.FOB_PURCHASE;
									} else if (slot instanceof DischargeSlot) {
										final DischargeSlot dischargeSlot = (DischargeSlot) slot;
										slotType = dischargeSlot.isFOBSale() ? SlotType.FOB_SALE : SlotType.DES_SALE;
									} else {
										assert false;
									}
									assert slotType != null;

									if (slot instanceof SpotSlot) {
										final SpotMarketSlotDescriptor slotDescriptor = AnalyticsFactory.eINSTANCE.createSpotMarketSlotDescriptor();
										slotDescriptor.setMarketName(((SpotSlot) slot).getMarket().getName());
										slotDescriptor.setDate(YearMonth.from(slot.getWindowStart()));
										slotDescriptor.setSlotType(slotType);
										// TODO: This may not always be correct! Really need the PortVisit!
										slotDescriptor.setPortName(slot.getPort().getName());
										change.getSlotDescriptors().add(slotDescriptor);
									} else {
										final RealSlotDescriptor slotDescriptor = AnalyticsFactory.eINSTANCE.createRealSlotDescriptor();
										slotDescriptor.setSlotName(slot.getName());
										slotDescriptor.setSlotType(slotType);
										change.getSlotDescriptors().add(slotDescriptor);
									}
								}

								change.setVesselAllocation(vesselDescriptor);

								// TODO: Fill in details
								PositionDescriptor positionDescriptor = AnalyticsFactory.eINSTANCE.createPositionDescriptor();
								positionDescriptor.setAfter(currentLastID);
								change.setPosition(positionDescriptor); //
								lastPositionDescriptor = positionDescriptor;
								changeDescription.getChanges().add(change);
							} else {
								final VesselEventChange change = AnalyticsFactory.eINSTANCE.createVesselEventChange();
								// Vessel event change
								final IPortSlot portSlot = portSlotProvider.getPortSlot(seq_e);
								final VesselEvent vesselEvent = dataTransformer.getModelEntityMap().getModelObject(portSlot, VesselEvent.class);
								final VesselEventDescriptor eventDescriptor = AnalyticsFactory.eINSTANCE.createVesselEventDescriptor();
								eventDescriptor.setEventName(vesselEvent.getName());
								change.setVesselEventDescriptor(eventDescriptor);

								change.setVesselAllocation(vesselDescriptor);

								// TODO: Fill in details
								PositionDescriptor positionDescriptor = AnalyticsFactory.eINSTANCE.createPositionDescriptor();
								positionDescriptor.setAfter(currentLastID);
								change.setPosition(positionDescriptor); //
								lastPositionDescriptor = positionDescriptor;

								changeDescription.getChanges().add(change);
							}
						}
					}
				}
			}

			for (final ISequenceElement e : option.getUnusedElements()) {
				if (spotMarketSlotsProvider.isSpotMarketSlot(e)) {
					continue;
				}

				if (baseSolution.getUnusedElements().contains(e)) {
					continue;
				}
				final IPortSlot portSlot = portSlotProvider.getPortSlot(e);
				final Slot slot = dataTransformer.getModelEntityMap().getModelObject(portSlot, Slot.class);
				final SlotType slotType = ScheduleSpecificationHelper.getSlotType(slot);
				final OpenSlotChange change = AnalyticsFactory.eINSTANCE.createOpenSlotChange();
				final RealSlotDescriptor slotDescriptor = AnalyticsFactory.eINSTANCE.createRealSlotDescriptor();
				slotDescriptor.setSlotName(slot.getName());
				slotDescriptor.setSlotType(slotType);
				change.setSlotDescriptor(slotDescriptor);

				changeDescription.getChanges().add(change);
			}
			return changeDescription;
		};
	}

	public void prepareFromBase(@NonNull final ISequences baseRawSequences) {
		generateChangeDescription = createChangeDescriptionFunction(baseRawSequences);
	}

	public ChangeDescription generateChangeDescription(@NonNull final ISequences rawSequences) {

		if (generateChangeDescription == null) {
			throw new IllegalStateException();
		}

		final ChangeDescription changeDescription = generateChangeDescription.apply(rawSequences);

		return changeDescription;
	}
}
