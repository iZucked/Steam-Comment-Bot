/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period.extensions;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Schedule;

public interface IPeriodTransformerExtension {

	/**
	 * Return any additional dependencies that should be retained in the period scenario to be able to correctly calculate this slot's P&L. If any of the returned slots would otherwise have been
	 * excluded, they will be brought back into the period, locked down (and potentially on their own vessel instance with matching start and end heels, dates, ports etc).
	 * 
	 * @param slot
	 * @return
	 */
	@Nullable
	List<Slot> getExtraDependenciesForSlot(Slot slot);

	/**
	 * A hook for an extension to process the final list of removed cargoes and slots for any final modifications needed.
	 * 
	 * @param cargoModel
	 * @param excludedSlots
	 * @param excludedCargoes
	 */
	void processSlotInclusionsAndExclusions(@NonNull CargoModel cargoModel, @NonNull Schedule schedule, @NonNull Collection<Slot> excludedSlots, @NonNull Collection<Cargo> excludedCargoes);
}
