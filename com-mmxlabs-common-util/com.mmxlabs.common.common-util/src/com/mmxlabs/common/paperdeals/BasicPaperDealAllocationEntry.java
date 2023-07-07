/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.paperdeals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.common.exposures.BasicExposureRecord;

/**
 * Class to keep a paper deal allocation and exposure
 * @author FM
 *
 */
public class BasicPaperDealAllocationEntry {
	private LocalDate pricingDate;
	private LocalDate hedgingStart;
	private LocalDate hedgingEnd;
	private long quantity;
	private int price;
	private long value;
	private boolean settled;
	private List<BasicExposureRecord> exposures;
	
	public BasicPaperDealAllocationEntry(LocalDate pricingDate, LocalDate hedgingStart, LocalDate hedgingEnd, long quantity, int price, long value, boolean settled) {
		this.pricingDate = pricingDate;
		this.hedgingStart = hedgingStart;
		this.hedgingEnd = hedgingEnd;
		this.quantity = quantity;
		this.price = price;
		this.value = value;
		this.settled = settled;
		this.exposures = new ArrayList<>();
	}

	public LocalDate getDate() {
		return pricingDate;
	}
	
	public long getQuantity() {
		return quantity;
	}
	
	public int getPrice() {
		return price;
	}
	
	public long getValue() {
		return value;
	}
	
	public boolean isSettled() {
		return settled;
	}
	
	public LocalDate getHedingStart() {
		return hedgingStart;
	}

	public LocalDate getHedgingEnd() {
		return hedgingEnd;
	}

	public List<BasicExposureRecord> getExposures() {
		return exposures;
	}
}
