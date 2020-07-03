/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.model;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Vessel {

	private String mmxId;

	private String name;
	private String shortName;
	// unique vessel id
	private String imo;
	private Optional<String> type = Optional.empty();

	private Optional<String> referenceVessel = Optional.empty();

	private OptionalInt capacity = OptionalInt.empty();
	private OptionalDouble fillCapacity = OptionalDouble.empty();
	private OptionalInt scnt = OptionalInt.empty();

	private String notes;

	private Optional<List<String>> inaccessiblePorts = Optional.empty();
	private Optional<List<String>> inaccessibleRoutes = Optional.empty();

	private Optional<String> travelBaseFuel = Optional.empty();
	private Optional<String> idleBaseFuel = Optional.empty();
	private Optional<String> inPortBaseFuel = Optional.empty();
	private Optional<String> pilotLightBaseFuel = Optional.empty();

	private VesselTravelAttributes ladenAttributes = new VesselTravelAttributes();

	private VesselTravelAttributes ballastAttributes = new VesselTravelAttributes();

	private VesselPortAttributes loadAttributes = new VesselPortAttributes();
	private VesselPortAttributes dischargeAttributes = new VesselPortAttributes();

	private Optional<List<VesselRouteParameters>> routeParameters = Optional.empty();

	private OptionalDouble minSpeed = OptionalDouble.empty();
	private OptionalDouble maxSpeed = OptionalDouble.empty();
	private OptionalInt safetyHeel = OptionalInt.empty();
	private OptionalInt warmingTime = OptionalInt.empty();
	private OptionalInt purgeTime = OptionalInt.empty();
	private OptionalInt coolingVolume = OptionalInt.empty();
	private OptionalDouble pilotLightRate = OptionalDouble.empty();
	private Optional<Boolean> hasReliqCapacity = Optional.empty();

	public String getMmxId() {
		return mmxId;
	}

	public void setMmxId(String mmxId) {
		this.mmxId = mmxId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getImo() {
		return imo;
	}

	public void setImo(String imo) {
		this.imo = imo;
	}

	public Optional<String> getType() {
		return type;
	}

	public void setType(Optional<String> type) {
		this.type = type;
	}

	public Optional<String> getReferenceVessel() {
		return referenceVessel;
	}

	public void setReferenceVessel(Optional<String> referenceVessel) {
		this.referenceVessel = referenceVessel;
	}

	public OptionalInt getCapacity() {
		return capacity;
	}

	public void setCapacity(OptionalInt capacity) {
		this.capacity = capacity;
	}

	public OptionalDouble getFillCapacity() {
		return fillCapacity;
	}

	public void setFillCapacity(OptionalDouble fillCapacity) {
		this.fillCapacity = fillCapacity;
	}

	public OptionalInt getScnt() {
		return scnt;
	}

	public void setScnt(OptionalInt scnt) {
		this.scnt = scnt;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Optional<List<String>> getInaccessiblePorts() {
		return inaccessiblePorts;
	}

	public void setInaccessiblePorts(Optional<List<String>> inaccessiblePorts) {
		this.inaccessiblePorts = inaccessiblePorts;
	}

	public Optional<List<String>> getInaccessibleRoutes() {
		return inaccessibleRoutes;
	}

	public void setInaccessibleRoutes(Optional<List<String>> inaccessibleRoutes) {
		this.inaccessibleRoutes = inaccessibleRoutes;
	}

	public Optional<String> getTravelBaseFuel() {
		return travelBaseFuel;
	}

	public void setTravelBaseFuel(Optional<String> travelBaseFuel) {
		this.travelBaseFuel = travelBaseFuel;
	}

	public Optional<String> getIdleBaseFuel() {
		return idleBaseFuel;
	}

	public void setIdleBaseFuel(Optional<String> idleBaseFuel) {
		this.idleBaseFuel = idleBaseFuel;
	}

	public Optional<String> getInPortBaseFuel() {
		return inPortBaseFuel;
	}

	public void setInPortBaseFuel(Optional<String> inPortBaseFuel) {
		this.inPortBaseFuel = inPortBaseFuel;
	}

	public Optional<String> getPilotLightBaseFuel() {
		return pilotLightBaseFuel;
	}

	public void setPilotLightBaseFuel(Optional<String> pilotLightBaseFuel) {
		this.pilotLightBaseFuel = pilotLightBaseFuel;
	}

	public VesselTravelAttributes getLadenAttributes() {
		return ladenAttributes;
	}

	public void setLadenAttributes(VesselTravelAttributes ladenAttributes) {
		this.ladenAttributes = ladenAttributes;
	}

	public VesselTravelAttributes getBallastAttributes() {
		return ballastAttributes;
	}

	public void setBallastAttributes(VesselTravelAttributes ballastAttributes) {
		this.ballastAttributes = ballastAttributes;
	}

	public VesselPortAttributes getLoadAttributes() {
		return loadAttributes;
	}

	public void setLoadAttributes(VesselPortAttributes loadAttributes) {
		this.loadAttributes = loadAttributes;
	}

	public VesselPortAttributes getDischargeAttributes() {
		return dischargeAttributes;
	}

	public void setDischargeAttributes(VesselPortAttributes dischargeAttributes) {
		this.dischargeAttributes = dischargeAttributes;
	}

	public OptionalDouble getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(OptionalDouble minSpeed) {
		this.minSpeed = minSpeed;
	}

	public OptionalDouble getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(OptionalDouble maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public OptionalInt getSafetyHeel() {
		return safetyHeel;
	}

	public void setSafetyHeel(OptionalInt safetyHeel) {
		this.safetyHeel = safetyHeel;
	}

	public OptionalInt getWarmingTime() {
		return warmingTime;
	}

	public void setWarmingTime(OptionalInt warmingTime) {
		this.warmingTime = warmingTime;
	}

	public OptionalInt getPurgeTime() {
		return purgeTime;
	}
	
	public void setPurgeTime(OptionalInt purgeTime) {
		this.purgeTime = purgeTime;
	}

	public OptionalInt getCoolingVolume() {
		return coolingVolume;
	}

	public void setCoolingVolume(OptionalInt coolingVolume) {
		this.coolingVolume = coolingVolume;
	}

	public OptionalDouble getPilotLightRate() {
		return pilotLightRate;
	}

	public void setPilotLightRate(OptionalDouble pilotLightRate) {
		this.pilotLightRate = pilotLightRate;
	}

	public Optional<Boolean> getHasReliqCapacity() {
		return hasReliqCapacity;
	}

	public void setHasReliqCapacity(Optional<Boolean> hasReliqCapacity) {
		this.hasReliqCapacity = hasReliqCapacity;
	}

	public Optional<List<VesselRouteParameters>> getRouteParameters() {
		return routeParameters;
	}

	public void setRouteParameters(Optional<List<VesselRouteParameters>> routeParameters) {
		this.routeParameters = routeParameters;
	}
}
