/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargo;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.io.Files;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubSummary;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

@SchemaVersion(1)
public class CargoReportModel {

	@ColumnName("Type")
	public String cargoType;

	@ColumnName("Vessel")
	@HubSummary(name="Vessel", index=4)
	public String vesselName;

	@ColumnName("ID")
	public String loadName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@HubFormat("DD/MM/YYYY hh:mm")
	@ColumnName("Date")
	@HubSummary(name="Date", index=2)
	public LocalDateTime loadScheduledDate;

	@ColumnName("Port")
	@HubSummary(name="Port", index=1)
	public String loadPortName;

	public double loadVolumeM3;
	@ColumnName("Volume")
	@HubFormat("{ \"thousandSeparated\": true }")
	public double loadVolumeMMBTU;

	@ColumnName("Buy at")
	@HubSummary(name="Buy at", index=3)
	public String purchaseContract;
	@ColumnName("C/P")
	public String purchaseCounterparty;

	@ColumnName("Buy Price")
	@HubFormat("{\"thousandSeparated\": true, \"decimals\": 2}")
	public double loadPrice;

	@ColumnName("ID")
	public String dischargeName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	@HubFormat("DD/MM/YYYY hh:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@ColumnName("Date")
	@HubSummary(name="Date", index=6)
	public LocalDateTime dischargeScheduledDate;

	@ColumnName("Port")
	@HubSummary(name="Port", index=5)
	public String dischargePortName;

	public double dischargeVolumeM3; // M3
	
	@ColumnName("Volume")
	@HubFormat("{ \"thousandSeparated\": true }")
	public double dischargeVolumeMMBTU; // MMMBtu

	@ColumnName("Sell at")
	@HubSummary(name="Sell at", index=7)
	public String saleContract;

	@ColumnName("C/P")
	public String saleCounterparty;

	@ColumnName("Sell Price")
	@HubFormat("{\"thousandSeparated\": true, \"decimals\": 2}")
	public double dischargePrice;

	@ColumnName("Buy Notes")
	public String loadComment;

	@ColumnName("Sell Notes")
	public String dischargeComment;

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
	
	
	public static void main(String[] args) throws Exception {
		String schema = new SchemaGenerator().generateHubSchema(CargoReportModel.class, Mode.SUMMARY);
		System.out.println(schema);
		Files.write(schema, new File("target/cargo.json"), StandardCharsets.UTF_8);
	}
}
