/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
	private LocalDate start; //also pricing month if type is instrument or calendar
	private LocalDate end;
	private String type; //PERIOD_AVG, CALENDAR, INSTRUMENT
	private BasicInstrumentData instrument; //instrument which is used for settling
	private final @NonNull String indexName; //name of the curve
	private String entity; //name of the legal entity
	private int year; //year of the paper
	private String notes; //notes
	private boolean virtual;
	
	public BasicPaperDealData(String name, boolean isBuy, long paperVolume, int paperUnitPrice, LocalDate start, LocalDate end, 
			String type, BasicInstrumentData instrument, String indexName, String entity, int year, String notes, boolean virtual) {
		super();
		this.name = name;
		this.isBuy = isBuy;
		this.paperVolume = paperVolume;
		this.paperUnitPrice = paperUnitPrice;
		this.start = start;
		this.end = end;
		this.type = type;
		this.instrument = instrument;
		this.indexName = indexName;
		this.entity = entity;
		this.year = year;
		this.notes = notes;
		this.virtual = virtual;
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

	public LocalDate getStart() {
		return start;
	}

	public LocalDate getEnd() {
		return end;
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
}
