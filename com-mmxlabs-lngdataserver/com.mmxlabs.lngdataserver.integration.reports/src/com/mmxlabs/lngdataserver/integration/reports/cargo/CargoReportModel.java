/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;

public class CargoReportModel {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate loadingWindowDate;
	int loadingWindowSizeInHours;
	String sourcePortName;
	String type;
	String endBuyer;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate endBuyerWindowDate;
	int endBuyerWindowSizeInHours;
	String receivingPortName;
	String vesselName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate nextLoadPortDate;
	Integer rtv;
	int loadableVolume;
	String comments;
	
	public LocalDate getLoadingWindowDate() {
		return loadingWindowDate;
	}
	public void setLoadingWindowDate(LocalDate loadingWindowDate) {
		this.loadingWindowDate = loadingWindowDate;
	}
	public int getLoadingWindowSizeInHours() {
		return loadingWindowSizeInHours;
	}
	public void setLoadingWindowSizeInHours(int loadingWindowSizeInHours) {
		this.loadingWindowSizeInHours = loadingWindowSizeInHours;
	}
	public String getSourcePortName() {
		return sourcePortName;
	}
	public void setSourcePortName(String sourcePortName) {
		this.sourcePortName = sourcePortName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEndBuyer() {
		return endBuyer;
	}
	public void setEndBuyer(String endBuyer) {
		this.endBuyer = endBuyer;
	}
	public void setEndBuyerWindowDate(LocalDate endBuyerWindowDate) {
		this.endBuyerWindowDate = endBuyerWindowDate;
	}
	public int getEndBuyerWindowSizeInHours() {
		return endBuyerWindowSizeInHours;
	}
	public void setEndBuyerWindowSizeInHours(int endBuyerWindowSizeInHours) {
		this.endBuyerWindowSizeInHours = endBuyerWindowSizeInHours;
	}
	public String getVesselName() {
		return vesselName;
	}
	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	public LocalDate getNextLoadPortDate() {
		return nextLoadPortDate;
	}
	public void setNextLoadPortDate(LocalDate nextLoadPortDate) {
		this.nextLoadPortDate = nextLoadPortDate;
	}
	public Integer getRtv() {
		return rtv;
	}
	public void setRtv(Integer rtv) {
		this.rtv = rtv;
	}
	public int getLoadableVolume() {
		return loadableVolume;
	}
	public void setLoadableVolume(int loadableVolume) {
		this.loadableVolume = loadableVolume;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getReceivingPortName() {
		return receivingPortName;
	}
	public void setReceivingPortName(String receivingPortName) {
		this.receivingPortName = receivingPortName;
	}
}
