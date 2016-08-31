/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
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
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.SlotContractHelper;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossSlotDetailsAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class ExporterExtensionUtils {

	@Inject
	private IPortSlotProvider slotProvider;

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

		if (slot instanceof ILoadOption || slot instanceof IDischargeOption) {

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

//				OpenSlotAllocation openSlotAllocation = null;
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

			if (slot instanceof IVesselEventPortSlot) {
				if (slot instanceof IGeneratedCharterOutVesselEventPortSlot) {
					final GeneratedCharterOut gco = portSlotEventProvider.getEventFromPortSlot(slot, GeneratedCharterOut.class);
					return gco;
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
			}

		} else if (slot instanceof StartPortSlot) {
			final StartEvent startEvent = findStartEvent(element, modelEntityMap, outputSchedule, annotatedSolution);
			return startEvent;
		} else if (slot instanceof EndPortSlot) {
			final EndEvent endEvent = findEndEvent(element, modelEntityMap, outputSchedule, annotatedSolution);
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

	private EndEvent findEndEvent(final @NonNull ISequenceElement element, @NonNull final ModelEntityMap modelEntityMap, @NonNull final Schedule outputSchedule,
			@NonNull final IAnnotatedSolution annotatedSolution) {
		EndEvent endEvent = null;
		//
		// for (int i = 0; i < annotatedSolution.getFullSequences().size(); ++i) {
		LOOP_OUTER: for (final Map.Entry<IResource, ISequence> e : annotatedSolution.getFullSequences().getSequences().entrySet()) {
			final IResource res = e.getKey();
			final ISequence seq = e.getValue();
			if (seq.size() == 0) {
				continue;
			}
			if (seq.get(0) == element) {
				for (final Sequence sequence : outputSchedule.getSequences()) {
					final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
					if (vesselAvailability == null) {
						continue;
					}

					// Find the matching
					final IVesselAvailability iVesselAvailability = modelEntityMap.getOptimiserObject(vesselAvailability, IVesselAvailability.class);

					if (iVesselAvailability == vesselProvider.getVesselAvailability(res)) {
						if (sequence.getEvents().size() > 0) {
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

	private StartEvent findStartEvent(final ISequenceElement element, @NonNull final ModelEntityMap modelEntityMap, @NonNull final Schedule outputSchedule,
			@NonNull final IAnnotatedSolution annotatedSolution) {
		StartEvent startEvent = null;
		//
		// Find the optimiser sequence for the start element
		LOOP_OUTER: for (final Map.Entry<IResource, ISequence> e : annotatedSolution.getFullSequences().getSequences().entrySet()) {
			final IResource res = e.getKey();
			final ISequence seq = e.getValue();
			if (seq.size() == 0) {
				continue;
			}
			if (seq.get(0) == element) {
				// Found the sequence, so no find the matching EMF sequence
				for (final Sequence sequence : outputSchedule.getSequences()) {
					// Get the EMF Vessel
					final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
					if (vesselAvailability == null) {
						continue;
					}
					// Find the matching
					final IVesselAvailability iVesselAvailability = modelEntityMap.getOptimiserObject(vesselAvailability, IVesselAvailability.class);

					// Look up correct instance (NOTE: Even though IVessel extends IResource, they seem to be different instances.
					if (iVesselAvailability == vesselProvider.getVesselAvailability(res)) {
						if (sequence.getEvents().size() > 0) {
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

}
