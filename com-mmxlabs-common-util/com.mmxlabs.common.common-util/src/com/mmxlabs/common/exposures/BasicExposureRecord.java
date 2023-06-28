/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.exposures;

import java.time.LocalDate;

public class BasicExposureRecord {
	
	private String portSlotName;
	private long volumeMMBTU;
	private long volumeNative;
	private long volumeValueNative;
	private long unitPrice;
	private String priceExpression;
	private LocalDate pricingDate;
	private LocalDate hedgingStart;
	private LocalDate hedgingEnd;
	private String indexName;
	private String volumeUnit;
	private String currencyUnit;
	private int adjustment;
	
	public int getAdjustment() {
		return adjustment;
	}
	public void setAdjustment(int adjustment) {
		this.adjustment = adjustment;
	}
	public String getPortSlotName() {
		return portSlotName;
	}
	public void setPortSlotName(String portSlotName) {
		this.portSlotName = portSlotName;
	}
	public long getVolumeMMBTU() {
		return volumeMMBTU;
	}
	public void setVolumeMMBTU(long volumeMMBTU) {
		this.volumeMMBTU = volumeMMBTU;
	}
	public long getVolumeNative() {
		return volumeNative;
	}
	public void setVolumeNative(long volumeNative) {
		this.volumeNative = volumeNative;
	}
	public long getVolumeValueNative() {
		return volumeValueNative;
	}
	public void setVolumeValueNative(long volumeValue) {
		this.volumeValueNative = volumeValue;
	}
	public long getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(long unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getPriceExpression() {
		return priceExpression;
	}
	public void setPriceExpression(String priceExpression) {
		this.priceExpression = priceExpression;
	}
	public LocalDate getTime() {
		return pricingDate;
	}
	public void setTime(LocalDate time) {
		this.pricingDate = time;
	}
	public LocalDate getHedingStart() {
		return hedgingStart;
	}
	public void setHedgingStart(LocalDate hedgingStart) {
		this.hedgingStart = hedgingStart;
	}
	public LocalDate getHedgingEnd() {
		return hedgingEnd;
	}
	public void setHedgingEnd(LocalDate hedgingEnd) {
		this.hedgingEnd = hedgingEnd;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getVolumeUnit() {
		return volumeUnit;
	}
	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}
	public String getCurrencyUnit() {
		return currencyUnit;
	}
	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}
}
