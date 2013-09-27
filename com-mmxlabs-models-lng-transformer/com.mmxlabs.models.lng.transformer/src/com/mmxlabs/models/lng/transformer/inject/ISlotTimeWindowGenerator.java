package com.mmxlabs.models.lng.transformer.inject;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;

/**
 * The {@link ISlotTimeWindowGenerator} is an optional component of the {@link LNGScenarioTransformer} which, if present, can be used to customise how a {@link ITimeWindow} object is generated for a
 * given slot.
 * 
 * @author Simon Goodall
 * @since 6.2
 * 
 */
public interface ISlotTimeWindowGenerator {

	/**
	 * Generate the {@link ITimeWindow} to use for this model slot. The builder and earliestTime are also passed in for date conversion.
	 * 
	 * @param builder
	 * 
	 * @param slot
	 * @param earliestTime
	 * @param defaultWindow
	 *            The time window to return if not customised.
	 * @return Custom {@link ITimeWindow} or defaultWindow
	 */
	@NonNull
	ITimeWindow generateTimeWindow(@NonNull ISchedulerBuilder builder, @NonNull Slot slot, @NonNull Date earliestTime, @NonNull ITimeWindow defaultWindow);
}
