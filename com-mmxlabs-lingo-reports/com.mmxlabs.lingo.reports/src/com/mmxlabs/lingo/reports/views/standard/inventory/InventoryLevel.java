/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

import java.time.LocalDate;

import com.mmxlabs.models.lng.cargo.InventoryFrequency;

public class InventoryLevel {


	public LocalDate date;
	public int changeInM3; //FM - have to make it reset-able
	public String type;
	public String vessel;
	public String dischargeId;
	public String dischargePort;
	public int feedIn = 0; //FM - adding a required field
	public int feedOut = 0; //FM - adding a required field
	public int cargoOut = 0; //FM - adding a required field
	public int volumeLow = 0;
	public int volumeHigh = 0;

	public int runningTotal = 0;
	public int ttlLow = 0;
	public int ttlHigh = 0;
	public boolean breach = false;
	public String salesContract;
	public LocalDate salesDate;
	
	public void merge(final InventoryLevel lvl) {
		this.changeInM3 += lvl.changeInM3;
		this.feedIn += lvl.feedIn;
		this.volumeLow += lvl.volumeLow;
		this.volumeHigh += lvl.volumeHigh;
		this.feedOut += lvl.feedOut;
		this.cargoOut += lvl.cargoOut;
		this.vessel = lvl.vessel != null ? lvl.vessel : this.vessel;
		this.dischargeId = lvl.dischargeId != null ? lvl.dischargeId : this.dischargeId;
		this.dischargePort = lvl.dischargePort != null ? lvl.dischargePort : this.dischargePort;
		this.salesContract = lvl.vessel != null ? lvl.salesContract : this.salesContract;
		this.salesDate = lvl.vessel != null ? lvl.salesDate : this.salesDate;
		this.breach = lvl.breach || this.breach;
	}

	public InventoryLevel(final LocalDate date, final InventoryFrequency type, final int changeInM3, final String vessel, final String dischargeId, final String dischargePort, final String salesContract, LocalDate salesDate) {
		this(date, type.toString(), changeInM3, vessel, dischargeId, dischargePort, salesContract, salesDate);
	}

	public InventoryLevel(final LocalDate date, final String type, final int changeInM3, final String vessel, final String dischargeId, final String dischargePort, final String salesContract, LocalDate salesDate) {
		this.date = date;
		this.type = type;
		this.changeInM3 = changeInM3;
		this.vessel = vessel;
		this.dischargeId = dischargeId;
		this.dischargePort = dischargePort;
		this.salesContract = salesContract;
		this.salesDate = salesDate;
	}

}