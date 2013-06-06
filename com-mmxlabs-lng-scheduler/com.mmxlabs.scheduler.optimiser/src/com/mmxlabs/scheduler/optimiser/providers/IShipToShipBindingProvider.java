package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @since 5.0
 */
public interface IShipToShipBindingProvider extends IDataComponentProvider {

	IPortSlot getConverseTransferElement(IPortSlot slot);

}
