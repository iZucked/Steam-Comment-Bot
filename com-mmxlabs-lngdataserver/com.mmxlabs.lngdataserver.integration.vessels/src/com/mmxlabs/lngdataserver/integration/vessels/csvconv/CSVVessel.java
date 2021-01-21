/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.csvconv;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "kind", "routeParameters", //
		"ladenAttributes.fuelConsumption", "ballastAttributes.fuelConsumption", //
		"ladenAttributes.kind", "ballastAttributes.kind", //
		"SUEZ.parameters.kind", "PANAMA.parameters.kind" })
public class CSVVessel {
	public String name;
	public String shortName;
	public String IMO;

	public String type;
	public Integer capacity;
	public Double fillCapacity;
	public Integer scnt;

	public Double pilotLightRate;
	public Integer safetyHeel;
	public Integer warmingTime;
	public Integer coolingVolume;
	public Integer coolingTime;
	public Integer purgeVolume;
	public Integer purgeTime;
	public Double minSpeed;
	public Double maxSpeed;

	public boolean inaccessiblePortsOverride;
	public List<String> inaccessiblePorts;

	public boolean inaccessibleRoutesOverride;
	public List<String> inaccessibleRoutes;

	public double minBaseFuelConsumption;
	public boolean hasReliqCapabilityOverride;
	public boolean hasReliqCapability;
	public String notes;
	public String mmxId;
	public String reference;

	public String baseFuel;
	public String inPortBaseFuel;
	public String pilotLightBaseFuel;
	public String idleBaseFuel;

	@JsonProperty("ladenAttributes.nboRate")
	public Double ladenAttributes_nboRate;

	@JsonProperty("ladenAttributes.idleNBORate")
	public Double ladenAttributes_idleNBORate;

	@JsonProperty("ladenAttributes.idleBaseRate")
	public Double ladenAttributes_idleBaseRate;

	@JsonProperty("ladenAttributes.inPortBaseRate")
	public Double ladenAttributes_inPortBaseRate;

	@JsonProperty("ladenAttributes.serviceSpeed")
	public Double ladenAttributes_serviceSpeed;

	@JsonProperty("ladenAttributes.inPortNBORate")
	public Double ladenAttributes_inPortNBORate;

	@JsonProperty("ladenAttributes.fuelConsumptionOverride")
	public boolean ladenAttributes_fuelConsumptionOverride;

	@JsonProperty("ballastAttributes.nboRate")
	public Double ballastAttributes_nboRate;

	@JsonProperty("ballastAttributes.idleNBORate")
	public Double ballastAttributes_idleNBORate;

	@JsonProperty("ballastAttributes.idleBaseRate")
	public Double ballastAttributes_idleBaseRate;

	@JsonProperty("ballastAttributes.inPortBaseRate")
	public Double ballastAttributes_inPortBaseRate;

	@JsonProperty("ballastAttributes.serviceSpeed")
	public Double ballastAttributes_serviceSpeed;

	@JsonProperty("ballastAttributes.inPortNBORate")
	public Double ballastAttributes_inPortNBORate;

	@JsonProperty("ballastAttributes.fuelConsumptionOverride")
	public boolean ballastAttributes_fuelConsumptionOverride;

	public boolean routeParametersOverride;

	@JsonProperty("SUEZ.parameters.extraTransitTime")
	public int suez_extraTransitTime;

	@JsonProperty("SUEZ.parameters.ladenConsumptionRate")
	public double suez_ladenConsumptionRate;

	@JsonProperty("SUEZ.parameters.ladenNBORate")
	public double suez_ladenNBORate;

	@JsonProperty("SUEZ.parameters.ballastConsumptionRate")
	public double suez_ballastConsumptionRate;

	@JsonProperty("SUEZ.parameters.ballastNBORate")
	public double suez_ballastNBORate;

	@JsonProperty("PANAMA.parameters.extraTransitTime")
	public int panama_extraTransitTime;

	@JsonProperty("PANAMA.parameters.ladenConsumptionRate")
	public double panama_ladenConsumptionRate;

	@JsonProperty("PANAMA.parameters.ladenNBORate")
	public double panama_ladenNBORate;

	@JsonProperty("PANAMA.parameters.ballastConsumptionRate")
	public double panama_ballastConsumptionRate;

	@JsonProperty("PANAMA.parameters.ballastNBORate")
	public double panama_ballastNBORate;

}
