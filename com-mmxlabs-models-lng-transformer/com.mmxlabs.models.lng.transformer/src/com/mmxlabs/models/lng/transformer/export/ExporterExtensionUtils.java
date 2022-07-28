/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EntityPNLDetails;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.SlotContractHelper;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossSlotDetailsAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class ExporterExtensionUtils {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotEventProvider portSlotEventProvider;

	public static double convertHoursToDays(final int hours) {
		final int days = hours / 24;
		final double fraction = ((double) hours - days * 24) / 24.0;
		return (double) days + fraction;
	}

	@Nullable
	public ProfitAndLossContainer findProfitAndLossContainer(@NonNull final ISequenceElement element, @NonNull final IPortSlot slot, @NonNull final ModelEntityMap modelEntityMap,
			@NonNull final Schedule outputSchedule, @NonNull final IAnnotatedSolution annotatedSolution) {
		ProfitAndLossContainer profitAndLossContainer = null;

		if (slot.getPortType() == PortType.Load || slot.getPortType() == PortType.Discharge) {

			final Slot modelSlot = modelEntityMap.getModelObject(slot, Slot.class);
			for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
				for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
					if (slotAllocation.getSlot() == modelSlot) {
						profitAndLossContainer = allocation;
						break;
					}
				}
			}
			if (profitAndLossContainer == null) {

				// OpenSlotAllocation openSlotAllocation = null;
				for (final OpenSlotAllocation allocation : outputSchedule.getOpenSlotAllocations()) {
					if (allocation.getSlot() == modelSlot) {
						profitAndLossContainer = allocation;
						break;
					}
				}

				if (profitAndLossContainer == null) {

					for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
						if (allocation.getSlot() == modelSlot) {
							profitAndLossContainer = allocation;
							break;
						}
					}
				}
			}
		} else if (slot instanceof IVesselEventPortSlot) {

			if (slot instanceof IGeneratedCharterOutVesselEventPortSlot) {
				final GeneratedCharterOut gco = portSlotEventProvider.getEventFromPortSlot(slot, GeneratedCharterOut.class);
				return gco;
			} else if (slot instanceof IGeneratedCharterLengthEventPortSlot) {
				return portSlotEventProvider.getEventFromPortSlot(slot, CharterLengthEvent.class);
			} else {
				final com.mmxlabs.models.lng.cargo.VesselEvent modelEvent = modelEntityMap.getModelObject(slot, com.mmxlabs.models.lng.cargo.VesselEvent.class);
				VesselEventVisit visit = null;
				//
				for (final Sequence sequence : outputSchedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof VesselEventVisit) {
							if (((VesselEventVisit) event).getVesselEvent() == modelEvent) {
								visit = (VesselEventVisit) event;
							}
						}
					}
				}
				return visit;
			}
		} else if (slot.getPortType() == PortType.Start) {
			final StartEvent startEvent = findStartEvent(element, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
			return startEvent;
		} else if (slot.getPortType() == PortType.End) {
			final EndEvent endEvent = findEndEvent(element, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
			return endEvent;
		}
		return profitAndLossContainer;
	}

	@Nullable
	public static <T> T findElementAnnotation(@NonNull final ISequenceElement element, @NonNull final ProfitAndLossContainer profitAndLossContainer,
			@NonNull final IAnnotatedSolution annotatedSolution, @NonNull final String annotationKey, @NonNull final Class<T> annotationClass) {

		final IProfitAndLossSlotDetailsAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLossSlotDetails,
				IProfitAndLossSlotDetailsAnnotation.class);
		return SlotContractHelper.findDetailsAnnotation(profitAndLoss, annotationKey, annotationClass);
	}

	@Nullable
	public <T> T findLoadOptionAnnotation(@NonNull final ISequenceElement element, @NonNull final ILoadOption slot, @NonNull final ModelEntityMap modelEntityMap,
			@NonNull final Schedule outputSchedule, @NonNull final IAnnotatedSolution annotatedSolution, @NonNull final String annotationKey, @NonNull final Class<T> annotationClass) {

		if (slot instanceof ILoadOption) {
			final ProfitAndLossContainer profitAndLossContainer = findProfitAndLossContainer(element, slot, modelEntityMap, outputSchedule, annotatedSolution);
			if (profitAndLossContainer != null) {
				return findElementAnnotation(element, profitAndLossContainer, annotatedSolution, annotationKey, annotationClass);
			}
		}
		return null;
	}

	public static void addSlotPNLDetails(@NonNull final ProfitAndLossContainer profitAndLossContainer, @NonNull final Slot modelSlot, @NonNull final GeneralPNLDetails details) {
		SlotPNLDetails slotDetails = null;
		for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
			if (generalPNLDetails instanceof SlotPNLDetails) {
				final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
				if (slotPNLDetails.getSlot() == modelSlot) {
					slotDetails = slotPNLDetails;
				}
			}
		}
		if (slotDetails == null) {
			slotDetails = ScheduleFactory.eINSTANCE.createSlotPNLDetails();
			slotDetails.setSlot(modelSlot);
			profitAndLossContainer.getGeneralPNLDetails().add(slotDetails);
		}

		slotDetails.getGeneralPNLDetails().add(details);
	}

	public static void addEntityPNLDetails(@NonNull final ProfitAndLossContainer profitAndLossContainer, @NonNull final BaseLegalEntity modelEntity, @NonNull final GeneralPNLDetails details) {
		EntityPNLDetails entityDetails = null;
		for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
			if (generalPNLDetails instanceof EntityPNLDetails) {
				final EntityPNLDetails slotPNLDetails = (EntityPNLDetails) generalPNLDetails;
				if (slotPNLDetails.getEntity() == modelEntity) {
					entityDetails = slotPNLDetails;
				}
			}
		}
		if (entityDetails == null) {
			entityDetails = ScheduleFactory.eINSTANCE.createEntityPNLDetails();
			entityDetails.setEntity(modelEntity);
			profitAndLossContainer.getGeneralPNLDetails().add(entityDetails);
		}

		entityDetails.getGeneralPNLDetails().add(details);
	}

	public static EndEvent findEndEvent(final ISequenceElement element, @NonNull ModelEntityMap modelEntityMap, @NonNull Schedule outputSchedule, @NonNull IAnnotatedSolution annotatedSolution,
			IVesselProvider vesselProvider) {
		EndEvent endEvent = null;
		//
		// for (int i = 0; i < annotatedSolution.getFullSequences().size(); ++i) {
		LOOP_OUTER: for (final Map.Entry<IResource, ISequence> e : annotatedSolution.getFullSequences().getSequences().entrySet()) {
			final IResource res = e.getKey();
			final ISequence seq = e.getValue();
			if (seq.size() == 0) {
				continue;
			}
			@NonNull
			final ISequenceElement endElement = seq.get(seq.size() - 1);
			if (endElement == element) {
				for (final Sequence sequence : outputSchedule.getSequences()) {
					CharterInMarket charterInMarket = sequence.getCharterInMarket();
					final VesselCharter vesselCharter = sequence.getVesselCharter();
					if (charterInMarket == null && vesselCharter == null) {
						continue;
					}

					boolean correctVessel = false;
					if (charterInMarket != null) {
						@Nullable
						ISpotCharterInMarket iSpotCharterInMarket = modelEntityMap.getOptimiserObject(charterInMarket, ISpotCharterInMarket.class);
						if (iSpotCharterInMarket == null) {
							continue;
						}
						IVesselCharter iVesselCharter = vesselProvider.getVesselCharter(res);
						@Nullable
						ISpotCharterInMarket oSpotCharterInMarket = iVesselCharter.getSpotCharterInMarket();
						if (oSpotCharterInMarket == iSpotCharterInMarket && iVesselCharter.getSpotIndex() == sequence.getSpotIndex()) {
							correctVessel = true;
						}
					} else {
						// non-spot case
						// Find the matching
						final IVesselCharter o_VesselCharter = modelEntityMap.getOptimiserObject(vesselCharter, IVesselCharter.class);
						if (o_VesselCharter == vesselProvider.getVesselCharter(res)) {
							correctVessel = true;
						}
					}

					if (correctVessel) {
						if (!sequence.getEvents().isEmpty()) {
							final Event evt = sequence.getEvents().get(sequence.getEvents().size() - 1);
							if (evt instanceof EndEvent) {
								endEvent = (EndEvent) evt;
								break LOOP_OUTER;
							}
						}
					}
				}
			}
		}
		return endEvent;
	}

	public static StartEvent findStartEvent(final ISequenceElement element, @NonNull final ModelEntityMap modelEntityMap, @NonNull final Schedule outputSchedule,
			@NonNull final IAnnotatedSolution annotatedSolution, IVesselProvider vesselProvider) {
		StartEvent startEvent = null;
		//
		// Find the optimiser sequence for the start element
		LOOP_OUTER: for (final Map.Entry<IResource, ISequence> e : annotatedSolution.getFullSequences().getSequences().entrySet()) {
			final IResource res = e.getKey();
			final ISequence seq = e.getValue();
			if (seq.size() == 0) {
				continue;
			}
			@NonNull
			ISequenceElement startElement = seq.get(0);
			if (startElement == element) {
				// Found the sequence, so no find the matching EMF sequence
				for (final Sequence sequence : outputSchedule.getSequences()) {
					CharterInMarket charterInMarket = sequence.getCharterInMarket();
					final VesselCharter vesselCharter = sequence.getVesselCharter();
					if (charterInMarket == null && vesselCharter == null) {
						continue;
					}
					boolean correctVessel = false;
					if (charterInMarket != null) {
						@Nullable
						ISpotCharterInMarket iSpotCharterInMarket = modelEntityMap.getOptimiserObject(charterInMarket, ISpotCharterInMarket.class);
						if (iSpotCharterInMarket == null) {
							continue;
						}
						IVesselCharter iVesselCharter = vesselProvider.getVesselCharter(res);
						@Nullable
						ISpotCharterInMarket oSpotCharterInMarket = iVesselCharter.getSpotCharterInMarket();
						if (oSpotCharterInMarket == iSpotCharterInMarket && iVesselCharter.getSpotIndex() == sequence.getSpotIndex()) {
							correctVessel = true;
						}
					} else {
						// non-spot case
						// Find the matching
						final IVesselCharter o_VesselCharter = modelEntityMap.getOptimiserObject(vesselCharter, IVesselCharter.class);
						if (o_VesselCharter == vesselProvider.getVesselCharter(res)) {
							correctVessel = true;
						}
					}

					// Look up correct instance (NOTE: Even though IVessel extends IResource, they seem to be different instances.
					if (correctVessel) {
						if (!sequence.getEvents().isEmpty()) {
							final Event evt = sequence.getEvents().get(0);
							if (evt instanceof StartEvent) {
								startEvent = (StartEvent) evt;
								break LOOP_OUTER;
							}
						}
					}
				}
			}
		}
		return startEvent;
	}

	@NonNull
	public static RouteOption mapRouteOption(@NonNull final ERouteOption routeOption) {
		switch (routeOption) {
		case DIRECT:
			return RouteOption.DIRECT;
		case PANAMA:
			return RouteOption.PANAMA;
		case SUEZ:
			return RouteOption.SUEZ;
		}
		throw new IllegalStateException();
	}

	@NonNull
	public static CanalEntry mapCanalEntry(@NonNull final ECanalEntry canalEntry) {
		switch (canalEntry) {
		case NorthSide:
			return CanalEntry.NORTHSIDE;
		case SouthSide:
			return CanalEntry.SOUTHSIDE;
		}
		throw new IllegalStateException();
	}
}
