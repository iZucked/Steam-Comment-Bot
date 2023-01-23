/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.entities.EntityBookType;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;

/**
 * DCP linking IPortSlot instances with IEntity instances. ICalculators are not linked, because of the fixed price override creating a bogus calculator.
 * 
 * TODO: think about this; the fixed price override could be implemented in the calculator with a suitable DCP for checking it. Not sure which is better.
 * 
 * @author hinton
 * 
 */
public interface IEntityProvider extends IDataComponentProvider {

	/**
	 * Get the entity which owns contracts at the given slot.
	 * 
	 * @param calculator
	 *            a calculator
	 * @return the entity which owns the contract at the given slot
	 */
	IEntity getEntityForSlot(@NonNull IPortSlot slot);

	@NonNull
	IEntity getEntityForVesselCharter(@NonNull IVesselCharter vesselCharter);

	@NonNull
	IEntityBook getEntityBook(@NonNull IEntity entity, @NonNull EntityBookType bookType);

	@NonNull
	Collection<@NonNull IEntity> getEntities();
}
