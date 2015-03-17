/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IGeneratedCharterOutSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;

/**
 * Implementation of Charter Out Port Slot Editor
 * @author achurchill
 *
 */
public final class HashMapGeneratedCharterOutPortSlotEditor implements IGeneratedCharterOutSlotProviderEditor {

	@Inject
	private IPortSlotProviderEditor portSlotProvider;

	private final Map<ISequenceElement, IGeneratedCharterOutVesselEventPortSlot> portSlotMap = new HashMap<ISequenceElement, IGeneratedCharterOutVesselEventPortSlot>();
	private final Map<IVesselEventPortSlot, ISequenceElement> elementMap = new HashMap<IVesselEventPortSlot, ISequenceElement>();
	private final Map<ISequenceElement, IGeneratedCharterOutVesselEventPortSlot> generatedPortSlotMap = new HashMap<ISequenceElement, IGeneratedCharterOutVesselEventPortSlot>();
	private final Map<IGeneratedCharterOutVesselEventPortSlot, ISequenceElement> generatedElementMap = new HashMap<IGeneratedCharterOutVesselEventPortSlot, ISequenceElement>();

	
	@Override
	public void setPortSlot(ISequenceElement element, IGeneratedCharterOutVesselEventPortSlot portSlot) {
		setPortSlot(element, portSlot, true);
	}
	public void setPortSlot(ISequenceElement element, IGeneratedCharterOutVesselEventPortSlot portSlot, boolean setElement) {
		portSlotMap.put(element, portSlot);
		if (setElement) {
			setSequenceElement(portSlot, element, false);
		}
	}

	@Override
	public void setSequenceElement(IGeneratedCharterOutVesselEventPortSlot portSlot, ISequenceElement element) {
		setSequenceElement(portSlot, element, true);
	}
	
	public void setSequenceElement(IGeneratedCharterOutVesselEventPortSlot portSlot, ISequenceElement element, boolean setPortSlot) {
		elementMap.put(portSlot, element);
		if (setPortSlot) {
			setPortSlot(element, portSlot, false);
		}
	}

	@Override
	public void setPortSlotGeneratedByElement(ISequenceElement element, IGeneratedCharterOutVesselEventPortSlot portSlot) {
		setPortSlotGeneratedByElement(element, portSlot, true);
	}
	
	public void setPortSlotGeneratedByElement(ISequenceElement element, IGeneratedCharterOutVesselEventPortSlot portSlot, boolean setSequenceElement) {
		generatedPortSlotMap.put(element, portSlot);
		if (setSequenceElement) {
			setSequenceElementForGeneratedPortSlot(portSlot, element, false);
		}
	}

	@Override
	public void setSequenceElementForGeneratedPortSlot(IGeneratedCharterOutVesselEventPortSlot portSlot, ISequenceElement element) {
		setSequenceElementForGeneratedPortSlot(portSlot, element, true);
	}
	
	public void setSequenceElementForGeneratedPortSlot(IGeneratedCharterOutVesselEventPortSlot portSlot, ISequenceElement element, boolean setPortSlot) {
		generatedElementMap.put(portSlot, element);
		if (setPortSlot) {
			setPortSlotGeneratedByElement(element, portSlot, false);
		}
	}

	@Override
	public IGeneratedCharterOutVesselEventPortSlot getPortSlot(ISequenceElement element) {
		return portSlotMap.get(element);
	}

	@Override
	public ISequenceElement getSequenceElement(IGeneratedCharterOutVesselEventPortSlot portSlot) {
		return elementMap.get(portSlot);
	}

	@Override
	public IGeneratedCharterOutVesselEventPortSlot getPortSlotGeneratedByElement(ISequenceElement element) {
		return generatedPortSlotMap.get(element);
	}

	@Override
	public ISequenceElement getSequenceElementFromGeneratedPortSlot(IGeneratedCharterOutVesselEventPortSlot portSlot) {
		return generatedElementMap.get(portSlot);
	}

	@Override
	public IGeneratedCharterOutVesselEventPortSlot getPortSlotGeneratedByPortSlot(IPortSlot existingPortSlot) {
		ISequenceElement element = portSlotProvider.getElement(existingPortSlot);
		if (element != null) {
			return getPortSlotGeneratedByElement(element);
		}
		return null;
	}
}
