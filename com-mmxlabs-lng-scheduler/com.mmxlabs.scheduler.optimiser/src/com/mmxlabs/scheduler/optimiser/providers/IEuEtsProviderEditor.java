package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.impl.EuEtsSeasonalityCurve;

/**
 * An editor for {}
 * 
 * @author Isaac
 */
public interface IEuEtsProviderEditor extends IEuEtsProvider {

	/**
	 * Sets the port groups for EU ports, the seasonality curve for emmisions covered and the pricing curve. 
	 */
	
	void setEuPorts(Set<IPort> ports);
	void setSeasonalityCurve(EuEtsSeasonalityCurve curve);
	void setPriceCurve(IParameterisedCurve curve);
}
