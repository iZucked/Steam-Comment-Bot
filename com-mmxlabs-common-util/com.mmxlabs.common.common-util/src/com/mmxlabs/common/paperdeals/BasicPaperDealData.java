/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.paperdeals;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Basic data equivalent for PaperDeal
 * @author FM
 *
 */
public class BasicPaperDealData {
	

	private String name;
	private boolean isBuy; //side of the paper buy or sell
	private long paperVolume; // always MMBTU
	private int paperUnitPrice; // fixed cost per mmbtu
	private String type; //PERIOD_AVG, CALENDAR, INSTRUMENT
	private BasicInstrumentData instrument; //instrument which is used for settling
	private final @NonNull String indexName; //name of the curve
	private String entity; //name of the legal entity
	private int year; //year of the paper
	private String notes; //notes
	private boolean virtual;
	
	private LocalDate contractMonth;
	private LocalDate pricingStart;
	private LocalDate pricingEnd;
	private LocalDate hedgingStart;
	private LocalDate hedgingEnd;
	
	public BasicPaperDealData(String name, boolean isBuy, long paperVolume, int paperUnitPrice, String type, BasicInstrumentData instrument, @NonNull String indexName, String entity, int year,
			String notes, boolean virtual, LocalDate contractMonth, LocalDate pricingStart, LocalDate pricingEnd, LocalDate hedgingStart, LocalDate hedgingEnd) {
		super();
		this.name = name;
		this.isBuy = isBuy;
		this.paperVolume = paperVolume;
		this.paperUnitPrice = paperUnitPrice;
		this.type = type;
		this.instrument = instrument;
		this.indexName = indexName;
		this.entity = entity;
		this.year = year;
		this.notes = notes;
		this.virtual = virtual;
		this.contractMonth = contractMonth;
		this.pricingStart = pricingStart;
		this.pricingEnd = pricingEnd;
		this.hedgingStart = hedgingStart;
		this.hedgingEnd = hedgingEnd;
	}

	public String getName() {
		return name;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public long getPaperVolume() {
		return paperVolume;
	}

	public int getPaperUnitPrice() {
		return paperUnitPrice;
	}

	public String getType() {
		return type;
	}

	public BasicInstrumentData getInstrument() {
		return instrument;
	}

	public String getIndexName() {
		return indexName;
	}

	public String getEntity() {
		return entity;
	}

	public int getYear() {
		return year;
	}

	public String getNotes() {
		return notes;
	}
	
	public boolean isSetIntstrument() {
		return instrument != null;
	}
	
	public boolean isVirtual() {
		return virtual;
	}

	public LocalDate getContractMonth() {
		return contractMonth;
	}

	public LocalDate getPricingStart() {
		return pricingStart;
	}

	public LocalDate getPricingEnd() {
		return pricingEnd;
	}

	public LocalDate getHedgingStart() {
		return hedgingStart;
	}

	public LocalDate getHedgingEnd() {
		return hedgingEnd;
	}
}
