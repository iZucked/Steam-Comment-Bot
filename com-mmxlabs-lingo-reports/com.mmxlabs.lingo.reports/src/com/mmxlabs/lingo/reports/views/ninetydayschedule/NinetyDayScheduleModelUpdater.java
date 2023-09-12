package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleChartModelUpdater;

public class NinetyDayScheduleModelUpdater implements IScheduleChartModelUpdater {
	
	private final Supplier<ICommandHandler> commandHandlerProvider;
	
	public NinetyDayScheduleModelUpdater(Supplier<ICommandHandler> commandHandlerProvider) {
		this.commandHandlerProvider = commandHandlerProvider;
	}

	@Override
	public void annotationEdited(Object scheduleEventData, Object annotationData, LocalDateTime windowStart, LocalDateTime windowEnd) {
		if (annotationData.equals(NinetyDayScheduleEventAnnotationType.SLOT_WINDOW) && scheduleEventData instanceof SlotVisit sv) {
			slotWindowRezised(sv, windowStart, windowEnd);
		}
	}
	
	private void slotWindowRezised(SlotVisit sv, LocalDateTime windowStart, LocalDateTime windowEnd) {
			final Slot<?> slot = sv.getSlotAllocation().getSlot();
			final LocalDate newWindowStart = LocalDate.from(windowStart);
			final int newWindowStartHour = windowStart.getHour();
			final CompoundCommand cmd = new CompoundCommand("Update start window");
			
			boolean changedStart = false;
			final ICommandHandler commandHandler = commandHandlerProvider.get();
			final LocalDate oldWindowStart = slot.getWindowStart();
			if (!oldWindowStart.equals(newWindowStart)) {
				changedStart = true;
				cmd.append(SetCommand.create(commandHandler.getEditingDomain(), slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), newWindowStart));
			}
			final int oldWindowHour = slot.getWindowStartTime();
			if (oldWindowHour != newWindowStartHour) {
				cmd.append(SetCommand.create(commandHandler.getEditingDomain(), slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), newWindowStartHour));
			}
			
			LocalDateTime windowEndDate = windowEnd;
			final LocalDateTime oldWindowEnd = slot.getSchedulingTimeWindow().getEnd().toLocalDateTime();
			if (!oldWindowEnd.equals(windowEndDate) || changedStart) {
				final int hoursDifference = Hours.between(windowStart, windowEndDate);
				cmd.append(SetCommand.create(commandHandler.getEditingDomain(), slot, CargoPackage.eINSTANCE.getSlot_WindowSize(), hoursDifference));
				if (slot.getWindowSizeUnits() != TimePeriod.HOURS) {
					cmd.append(SetCommand.create(commandHandler.getEditingDomain(), slot, CargoPackage.eINSTANCE.getSlot_WindowSizeUnits(), TimePeriod.HOURS));
				}
			}
			
			if (!cmd.isEmpty()) {
				commandHandler.handleCommand(cmd);
			}
	}

}
