package com.mmxlabs.models.lng.transformer.extensions.despermission;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An editor interface for {@link IDesPermissionProvider}
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public interface IDesPermissionProviderEditor extends IDesPermissionProvider {
	void addDesPurchaseSlot(ISequenceElement element);
	void addDesProhibitedSalesSlot(ISequenceElement element);
}
