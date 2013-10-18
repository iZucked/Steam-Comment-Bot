package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Date;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.Port;

/**
 * Interface to pass into generic DES redirection contract API to obtain common details from the EMF data model.
 * 
 * API Subject to change
 * 
 * @author Simon Goodall
 * 
 */
public interface IRedirectionContractDetailsProvider {

	/**
	 * Returns the orignal loading date
	 * 
	 * @param loadSlot
	 * @return
	 */
	Date getOriginalDate(LoadSlot loadSlot);

	/**
	 * Returns the window size from the original date;
	 * 
	 * @param loadSlot
	 * @return
	 */
	int getWindow(LoadSlot loadSlot);

	Port getBaseLoadPort(LoadSlot loadSlot);

	Port getBaseDestinationPort(LoadSlot loadSlot);
}
