package com.mmxlabs.models.lng.transformer.export;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityPNLDetails;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.SlotContractHelper;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossSlotDetailsAnnotation;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class ExporterExtensionUtils {

	public static double convertHoursToDays(final int hours) {
		final int days = hours / 24;
		final double fraction = ((double) hours - days * 24) / 24.0;
		return (double) days + fraction;
	}

	@Nullable
	public static ProfitAndLossContainer findProfitAndLossContainer(@NonNull final IPortSlot slot, @NonNull final ModelEntityMap modelEntityMap, @NonNull final Schedule outputSchedule) {
		ProfitAndLossContainer profitAndLossContainer = null;

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

			for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
				if (allocation.getSlot() == modelSlot) {
					profitAndLossContainer = allocation;
					break;
				}
			}
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
	public static <T> T findLoadOptionAnnotation(@NonNull final ISequenceElement element, @NonNull final ILoadOption slot, @NonNull final ModelEntityMap modelEntityMap,
			@NonNull final Schedule outputSchedule, @NonNull final IAnnotatedSolution annotatedSolution, @NonNull final String annotationKey, @NonNull final Class<T> annotationClass) {

		if (slot instanceof ILoadOption) {
			final ProfitAndLossContainer profitAndLossContainer = findProfitAndLossContainer(slot, modelEntityMap, outputSchedule);
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
}
