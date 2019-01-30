package com.mmxlabs.lngdataserver.integration.general.model;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class UnitConversionFactorsVersion {

	private String identifier;

	private Instant createdAt;

	private List<UnitConversionFactor> factors = new LinkedList<>();

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

	public List<UnitConversionFactor> getFactors() {
		return factors;
	}

	public void setFactors(List<UnitConversionFactor> factors) {
		this.factors = factors;
	}

}
