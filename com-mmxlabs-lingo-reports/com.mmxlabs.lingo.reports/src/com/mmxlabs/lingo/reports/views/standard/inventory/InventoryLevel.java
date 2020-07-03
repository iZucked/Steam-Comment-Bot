/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

import java.time.LocalDate;

import com.mmxlabs.models.lng.cargo.InventoryFrequency;

public class InventoryLevel {


	public final LocalDate date;
	public int changeInM3; //FM - have to make it reset-able
	public final String type;
	public String vessel;
	public String dischargeId;
	public String dischargePort;
	public String loadId;
	public String loadPort;
	public int feedIn = 0; //FM - adding a required field
	public int feedOut = 0; //FM - adding a required field
	public int cargoOut = 0; //FM - adding a required field
	public int cargoIn = 0;
	public int volumeLow = 0;
	public int volumeHigh = 0;

	public int runningTotal = 0;
	public int ttlLow = 0;
	public int ttlHigh = 0;
	public boolean breach = false;
	public String salesContract;
	public String purchaseContract;
	public LocalDate salesDate;
	public LocalDate purchaseDate;
	
	public void merge(final InventoryLevel lvl) {
		this.changeInM3 += lvl.changeInM3;
		this.feedIn += lvl.feedIn;
		this.volumeLow += lvl.volumeLow;
		this.volumeHigh += lvl.volumeHigh;
		this.feedOut += lvl.feedOut;
		this.cargoOut += lvl.cargoOut;
		this.cargoIn += lvl.cargoIn;
		this.vessel = lvl.vessel != null ? lvl.vessel : this.vessel;
		this.dischargeId = lvl.dischargeId != null ? lvl.dischargeId : this.dischargeId;
		this.dischargePort = lvl.dischargePort != null ? lvl.dischargePort : this.dischargePort;
		this.salesContract = lvl.vessel != null ? lvl.salesContract : this.salesContract;
		this.salesDate = lvl.vessel != null ? lvl.salesDate : this.salesDate;
		this.loadId = lvl.loadId != null ? lvl.loadId : this.loadId;
		this.loadPort = lvl.loadPort != null ? lvl.loadPort : this.loadPort;
		this.purchaseContract = lvl.vessel != null ? lvl.purchaseContract : this.purchaseContract;
		this.purchaseDate = lvl.vessel != null ? lvl.purchaseDate : this.purchaseDate;
		this.breach = lvl.breach || this.breach;
	}

	public InventoryLevel(final LocalDate date, final InventoryFrequency type, final int changeInM3, final String vessel, final String dischargeId, //
			final String dischargePort, final String salesContract, final String loadId, final String loadPort, final String purchaseContract, //
			LocalDate salesDate, LocalDate purchaseDate) {
		this(date, type.toString(), changeInM3, vessel, dischargeId, dischargePort, salesContract, loadId, loadPort, purchaseContract, salesDate, purchaseDate);
	}
	
	public InventoryLevel(final LocalDate date, final String type, final int changeInM3, final String vessel, final String dischargeId, //
			final String dischargePort, final String salesContract, final String loadId, final String loadPort, final String purchaseContract, //
			LocalDate salesDate, LocalDate purchaseDate) {
		this.date = date;
		this.type = type;
		this.changeInM3 = changeInM3;
		this.vessel = vessel;
		this.dischargeId = dischargeId;
		this.dischargePort = dischargePort;
		this.salesContract = salesContract;
		this.salesDate = salesDate;
		this.loadId = loadId;
		this.loadPort = loadPort;
		this.purchaseContract = purchaseContract;
		this.purchaseDate = purchaseDate;
	}

}