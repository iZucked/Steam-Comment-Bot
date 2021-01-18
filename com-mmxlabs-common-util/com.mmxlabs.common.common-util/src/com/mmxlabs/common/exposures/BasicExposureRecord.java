/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.exposures;

import java.time.LocalDate;

public class BasicExposureRecord {
	
	private long volumeMMBTU;
	private long volumeNative;
	private long volumeValueNative;
	private int unitPrice;
	private String priceExpression;
	private LocalDate time;
	private String indexName;
	private String volumeUnit;
	private String currencyUnit;
	
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
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getPriceExpression() {
		return priceExpression;
	}
	public void setPriceExpression(String priceExpression) {
		this.priceExpression = priceExpression;
	}
	public LocalDate getTime() {
		return time;
	}
	public void setTime(LocalDate time) {
		this.time = time;
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
