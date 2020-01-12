/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class CargoReportModel {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime loadScheduledDate;
	
	public String loadName;
	public double loadVolumeM3;
	public double loadVolumeMMBTU;
	public double loadPrice;
	public String loadPortName;
	public String purchaseContract;
	public String purchaseCounterparty; 
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public LocalDateTime dischargeScheduledDate;
	
	public String dischargeName;
	public double dischargeVolumeM3; // M3
	public double dischargeVolumeMMBTU; // MMMBtu
	public double dischargePrice;
	public String dischargePortName;
	public String saleContract;
	public String saleCounterparty; 
	
	public String vesselName;
	
	public String loadComment;
	public String dischargeComment;
	
	public String cargoType;
	
	public long profitAndLoss;

	public LocalDateTime getLoadScheduledDate() {
		return loadScheduledDate;
	}

	public void setLoadScheduledDate(LocalDateTime loadScheduledDate) {
		this.loadScheduledDate = loadScheduledDate;
	}

	public String getLoadName() {
		return loadName;
	}

	public void setLoadName(String loadName) {
		this.loadName = loadName;
	}

	public double getLoadVolumeM3() {
		return loadVolumeM3;
	}

	public void setLoadVolumeM3(double loadVolumeM3) {
		this.loadVolumeM3 = loadVolumeM3;
	}

	public double getLoadVolumeMMBTU() {
		return loadVolumeMMBTU;
	}

	public void setLoadVolumeMMBTU(double loadVolumeMMBTU) {
		this.loadVolumeMMBTU = loadVolumeMMBTU;
	}

	public double getLoadPrice() {
		return loadPrice;
	}

	public void setLoadPrice(double loadPrice) {
		this.loadPrice = loadPrice;
	}

	public String getLoadPortName() {
		return loadPortName;
	}

	public void setLoadPortName(String loadPortName) {
		this.loadPortName = loadPortName;
	}

	public String getPurchaseContract() {
		return purchaseContract;
	}

	public void setPurchaseContract(String purchaseContract) {
		this.purchaseContract = purchaseContract;
	}

	public String getPurchaseCounterparty() {
		return purchaseCounterparty;
	}

	public void setPurchaseCounterparty(String purchaseCounterparty) {
		this.purchaseCounterparty = purchaseCounterparty;
	}

	public LocalDateTime getDischargeScheduledDate() {
		return dischargeScheduledDate;
	}

	public void setDischargeScheduledDate(LocalDateTime dischargeScheduledDate) {
		this.dischargeScheduledDate = dischargeScheduledDate;
	}

	public String getDischargeName() {
		return dischargeName;
	}

	public void setDischargeName(String dischargeName) {
		this.dischargeName = dischargeName;
	}

	public double getDischargeVolumeM3() {
		return dischargeVolumeM3;
	}

	public void setDischargeVolumeM3(double dischargeVolumeM3) {
		this.dischargeVolumeM3 = dischargeVolumeM3;
	}

	public double getDischargeVolumeMMBTU() {
		return dischargeVolumeMMBTU;
	}

	public void setDischargeVolumeMMBTU(double dischargeVolumeMMBTU) {
		this.dischargeVolumeMMBTU = dischargeVolumeMMBTU;
	}

	public double getDischargePrice() {
		return dischargePrice;
	}

	public void setDischargePrice(double dischargePrice) {
		this.dischargePrice = dischargePrice;
	}

	public String getDischargePortName() {
		return dischargePortName;
	}

	public void setDischargePortName(String dischargePortName) {
		this.dischargePortName = dischargePortName;
	}

	public String getSaleContract() {
		return saleContract;
	}

	public void setSaleContract(String saleContract) {
		this.saleContract = saleContract;
	}

	public String getSaleCounterparty() {
		return saleCounterparty;
	}

	public void setSaleCounterparty(String saleCounterparty) {
		this.saleCounterparty = saleCounterparty;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getLoadComment() {
		return loadComment;
	}

	public void setLoadComment(String loadComment) {
		this.loadComment = loadComment;
	}

	public String getDischargeComment() {
		return dischargeComment;
	}

	public void setDischargeComment(String dischargeComment) {
		this.dischargeComment = dischargeComment;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public long getProfitAndLoss() {
		return profitAndLoss;
	}

	public void setProfitAndLoss(long profitAndLoss) {
		this.profitAndLoss = profitAndLoss;
	}
}
