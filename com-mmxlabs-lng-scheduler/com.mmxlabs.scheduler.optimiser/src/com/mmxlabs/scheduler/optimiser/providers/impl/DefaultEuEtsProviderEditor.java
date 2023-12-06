package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Set;

import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.impl.EuEtsSeasonalityCurve;
import com.mmxlabs.scheduler.optimiser.providers.IEuEtsProviderEditor;

public class DefaultEuEtsProviderEditor implements IEuEtsProviderEditor {

	private Set<IPort> ports;
	private EuEtsSeasonalityCurve seasonalityCurve;
	private IParameterisedCurve priceCurve;
	
	@Override
	public Set<IPort> getEuPorts() {
		return ports;
	}

	@Override
	public EuEtsSeasonalityCurve getSeasonalityCurve() {
		return seasonalityCurve;
	}

	@Override
	public IParameterisedCurve getPriceCurve() {
		return priceCurve;
	}

	@Override
	public void setEuPorts(Set<IPort> ports) {
		this.ports = ports;
	}

	@Override
	public void setSeasonalityCurve(EuEtsSeasonalityCurve curve) {
		this.seasonalityCurve = curve;
	}

	@Override
	public void setPriceCurve(IParameterisedCurve curve) {
		this.priceCurve = curve;
	}

}
