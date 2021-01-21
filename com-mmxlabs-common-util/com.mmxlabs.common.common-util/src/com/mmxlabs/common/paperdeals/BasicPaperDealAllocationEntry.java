/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.paperdeals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.common.exposures.BasicExposureRecord;

/**
 * Class to keep a paper deal allocation and exposure
 * @author farukh
 *
 */
public class BasicPaperDealAllocationEntry {
	private LocalDate date;
	private long quantity;
	private int price;
	private long value;
	private boolean settled;
	private List<BasicExposureRecord> exposures;
	
	public BasicPaperDealAllocationEntry(LocalDate date, long quantity, int price, long value, boolean settled) {
		super();
		this.date = date;
		this.quantity = quantity;
		this.price = price;
		this.value = value;
		this.settled = settled;
		this.exposures = new ArrayList<>();
	}
	
	public LocalDate getDate() {
		return date;
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
	
	public List<BasicExposureRecord> getExposures() {
		return exposures;
	}
}
