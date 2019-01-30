package com.mmxlabs.lngdataserver.integration.general.model.financial.settled;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class SettledPricesVersion {

	private String identifier;

	private Instant createdAt;

	private String createdBy;

	private List<SettledPriceCurve> curves = new LinkedList<>();

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public List<SettledPriceCurve> getCurves() {
		return curves;
	}

	public void setCurves(List<SettledPriceCurve> curves) {
		this.curves = curves;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
