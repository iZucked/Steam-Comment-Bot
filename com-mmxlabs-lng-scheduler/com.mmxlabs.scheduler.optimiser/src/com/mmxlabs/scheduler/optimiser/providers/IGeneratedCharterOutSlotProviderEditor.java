/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;

/**
 * {@link IDataComponentProvider} interface to provide {@link IPortSlot} or {@link ISequenceElement} information for the given sequence elements or slots. Slots and sequence elements have been generated by existing sequence events.
 * 
 * @author achurchill
 */
public interface IGeneratedCharterOutSlotProviderEditor extends IGeneratedCharterOutSlotProvider {

	/**
	 * set the {@link IVesselEventPortSlot} for the given element.
	 * 
	 * @param element
	 * @return
	 */
	void setPortSlot(ISequenceElement element, IGeneratedCharterOutVesselEventPortSlot portSlot);

	/**
	 * sets the sequence element for the given {@link IVesselEventPortSlot}.
	 * 
	 * @param portSlot
	 * @return
	 */
	void setSequenceElement(IGeneratedCharterOutVesselEventPortSlot portSlot, ISequenceElement element);

	/**
	 * sets the {@link IVesselEventPortSlot} generated from the given element.
	 * 
	 * @param element
	 * @return
	 */
	void setPortSlotGeneratedByElement(ISequenceElement element, IGeneratedCharterOutVesselEventPortSlot portSlot);

	/**
	 * sets the sequence element used to generate the given {@link IVesselEventPortSlot}.
	 * 
	 * @param portSlot
	 * @return
	 */
	void setSequenceElementForGeneratedPortSlot(IGeneratedCharterOutVesselEventPortSlot portSlot, ISequenceElement element);
}
