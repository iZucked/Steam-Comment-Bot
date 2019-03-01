/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.minimaxlabs.rnd.representation;

import java.io.Serializable;

public class ShipmentData implements Serializable {
	private static final long serialVersionUID = 5517835927614289514L;

	private int m = 0; // cargo count - includes end index
	private int n = 0; // ship count
	private double[] pnl;
	private double[] vesselCapacities;
	private int[][] cargoTransit; // cargo delivery time (port to port)
	private int[][][] cargoToCargoTime; // size: m x m x n
	private double[][][] cargoToCargoCost;
	private boolean[][] cargoVesselRestrictions;
	private Interval[] load;
	private Interval[] discharge;

	public ShipmentData(int m, int n, double[] pnl, int[][][] cargoToCargoTime, double[][][] cargoToCargoCost, Interval[] load) {
		this.m = m;
		this.n = n;
		this.pnl = pnl;
		this.cargoToCargoTime = cargoToCargoTime;
		this.cargoToCargoCost = cargoToCargoCost;
		this.load = load;
	}

	public ShipmentData(int m, int n, double[] cargoPNL, double[] vesselCapacities, int[][][] cargoToCargoTime, double[][][] cargoToCargoCost, int[][] cargoTransit, Interval[] load,
			Interval[] discharge) {
		this.m = m;
		this.n = n;
		this.pnl = cargoPNL;
		this.vesselCapacities = vesselCapacities;
		this.cargoTransit = cargoTransit;
		this.cargoToCargoTime = cargoToCargoTime;
		this.cargoToCargoCost = cargoToCargoCost;
		this.load = load;
		this.discharge = discharge;
	}

	public ShipmentData(int m, int n, double[] cargoPNL, double[] vesselCapacities, int[][][] cargoToCargoTime, boolean[][] cargoVesselRestrictions, double[][][] cargoToCargoCost,
			int[][] cargoTransit, Interval[] load, Interval[] discharge) {
		this(m, n, cargoPNL, vesselCapacities, cargoToCargoTime, cargoToCargoCost, cargoTransit, load, discharge);
		this.setCargoVesselRestrictions(cargoVesselRestrictions);
	}

	public int[][] getCargoTransit() {
		return cargoTransit;
	}

	public void setCargoTransit(int[][] minCargoStartToEndSlotTravelTimesPerVessel) {
		this.cargoTransit = minCargoStartToEndSlotTravelTimesPerVessel;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double[] getPnl() {
		return pnl;
	}

	public void setPnl(double[] cargoPNL) {
		this.pnl = cargoPNL;
	}

	public int[][][] getCargoToCargoTime() {
		return cargoToCargoTime;
	}

	public void setCargoToCargoTime(int[][][] cargoToCargoTime) {
		this.cargoToCargoTime = cargoToCargoTime;
	}

	public double[][][] getCargoToCargoCost() {
		return cargoToCargoCost;
	}

	public void setCargoToCargoCost(double[][][] cargoToCargoCostsOnAvailability) {
		this.cargoToCargoCost = cargoToCargoCostsOnAvailability;
	}

	public Interval[] getLoad() {
		return load;
	}

	public void setLoad(Interval[] load) {
		this.load = load;
	}

	public Interval[] getDischarge() {
		return discharge;
	}

	public void setDischarge(Interval[] discharge) {
		this.discharge = discharge;
	}

	public double[] getVesselCapacities() {
		return vesselCapacities;
	}

	public void setVesselCapacities(double[] vesselCapacities) {
		this.vesselCapacities = vesselCapacities;
	}

	public boolean[][] getCargoVesselRestrictions() {
		return cargoVesselRestrictions;
	}

	public void setCargoVesselRestrictions(boolean[][] cargoVesselRestrictions) {
		this.cargoVesselRestrictions = cargoVesselRestrictions;
	}
}