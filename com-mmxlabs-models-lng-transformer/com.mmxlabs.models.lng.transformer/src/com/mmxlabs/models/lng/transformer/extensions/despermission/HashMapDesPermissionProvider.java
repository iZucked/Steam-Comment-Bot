package com.mmxlabs.models.lng.transformer.extensions.despermission;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class HashMapDesPermissionProvider implements IDesPermissionProviderEditor {
	final String name;
	
	final HashSet<ISequenceElement> desProhibitedSalesSlots = new HashSet<ISequenceElement>();
	final HashSet<ISequenceElement> desPurchaseSlots = new HashSet<ISequenceElement>();

	public HashMapDesPermissionProvider(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		desProhibitedSalesSlots.clear();
		desPurchaseSlots.clear();		
	}

	@Override
	public Collection<ISequenceElement> getRestrictedFollowerElements(
			ISequenceElement element) {
		if (desPurchaseSlots.contains(element)) {
			return desProhibitedSalesSlots;
		}
		return Collections.emptySet();
	}

	@Override
	public Collection<ISequenceElement> getRestrictedPrecedingElements(
			ISequenceElement element) {
		if (desProhibitedSalesSlots.contains(element)) {
			return desProhibitedSalesSlots;
		}
		return Collections.emptySet();
	}
	
	public void addDesPurchaseSlot(ISequenceElement element) {
		desPurchaseSlots.add(element);
	}

	public void addDesProhibitedSalesSlot(ISequenceElement element) {
		desProhibitedSalesSlots.add(element);
	}

}
