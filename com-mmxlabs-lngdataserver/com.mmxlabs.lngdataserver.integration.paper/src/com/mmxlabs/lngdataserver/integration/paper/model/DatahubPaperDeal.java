/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.paper.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@name")
public class DatahubPaperDeal {
	Double price;
	Double quantity;
	Integer year;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate pricingPeriodStart;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate pricingPeriodEnd;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate hedgingPeriodStart;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate hedgingPeriodEnd;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate pricingMonth;
	@JsonDeserialize(using = StringDeserializer.class)
	@JsonSerialize(using = StringSerializer.class)
	String name;
	@JsonDeserialize(using = StringDeserializer.class)
	@JsonSerialize(using = StringSerializer.class)
	String entity;
	@JsonDeserialize(using = StringDeserializer.class)
	@JsonSerialize(using = StringSerializer.class)
	String index;
	@JsonDeserialize(using = StringDeserializer.class)
	@JsonSerialize(using = StringSerializer.class)
	String comment;
	// @JsonDeserialize(using = StringDeserializer.class)
	// @JsonSerialize(using = StringSerializer.class)
	// String entity;
	@JsonDeserialize()
	@JsonSerialize()
	PricingType pricingType;
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public LocalDate getPricingPeriodStart() {
		return pricingPeriodStart;
	}

	public void setPricingPeriodStart(LocalDate pricingPeriodStart) {
		this.pricingPeriodStart = pricingPeriodStart;
	}

	public LocalDate getPricingPeriodEnd() {
		return pricingPeriodEnd;
	}

	public void setPricingPeriodEnd(LocalDate pricingPeriodEnd) {
		this.pricingPeriodEnd = pricingPeriodEnd;
	}

	public LocalDate getHedgingPeriodStart() {
		return hedgingPeriodStart;
	}

	public void setHedgingPeriodStart(LocalDate hedgingPeriodStart) {
		this.hedgingPeriodStart = hedgingPeriodStart;
	}

	public LocalDate getHedgingPeriodEnd() {
		return hedgingPeriodEnd;
	}

	public void setHedgingPeriodEnd(LocalDate hedgingPeriodEnd) {
		this.hedgingPeriodEnd = hedgingPeriodEnd;
	}

	public LocalDate getPricingMonth() {
		return pricingMonth;
	}

	public void setPricingMonth(LocalDate pricingMonth) {
		this.pricingMonth = pricingMonth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public PricingType getPricingType() {
		return pricingType;
	}

	public void setPricingType(PricingType pricingType) {
		this.pricingType = pricingType;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	@JsonDeserialize()
	@JsonSerialize()
	Kind kind;

	public DatahubPaperDeal(
			double price,
			double quantity,
			int year,
			LocalDate pricingMonth,
			LocalDate pricingPeriodStart,
			LocalDate pricingPeriodEnd,
			LocalDate hedgingPeriodStart,
			LocalDate hedgingPeriodEnd,
			String name,
			String entity,
			String index,
			String comment,
			//String entity,
			PricingType pricingType,
			Kind kind)
	{
		this.price=price;
		this.quantity=quantity;
		this.year=year;
		this.pricingMonth=pricingMonth;
		this.pricingPeriodStart=pricingPeriodStart;
		this.pricingPeriodEnd=pricingPeriodEnd;
		this.hedgingPeriodStart=hedgingPeriodStart;
		this.hedgingPeriodEnd=hedgingPeriodEnd;
		this.name=name;
		this.index=index;
		this.comment=comment;
		this.entity=entity;
		this.pricingType=pricingType;
		this.kind = kind;
	}
	
	public DatahubPaperDeal (){
		
	}

	public enum PricingType {
		PERIOD_AVG,
		CALENDAR,
		INSTRUMENT;
		
		private static Map<String, PricingType> namesMap = new HashMap<>(3);

	    static {
	        namesMap.put("PERIOD_AVG", PERIOD_AVG);
	        namesMap.put("CALENDAR", CALENDAR);
	        namesMap.put("INSTRUMENT", INSTRUMENT);
	    }

		@JsonCreator
	    public static PricingType forValue(String value) {
	        return namesMap.get(value);
	    }

	    @JsonValue
	    public String toValue() {
	        for (Entry<String, PricingType> entry : namesMap.entrySet()) {
	            if (entry.getValue() == this)
	                return entry.getKey();
	        }
	        return null; // or fail
	    }
	}

	public enum Kind {
		SELL_PAPER_DEAL,
		BUY_PAPER_DEAL;
		
		private static Map<String, Kind> namesMap = new HashMap<>(3);

	    static {
	        namesMap.put("SellPaperDeal", SELL_PAPER_DEAL);
	        namesMap.put("BuyPaperDeal", BUY_PAPER_DEAL);
	    }

		@JsonCreator
	    public static Kind forValue(String value) {
	        return namesMap.get(value);
	    }

	    @JsonValue
	    public String toValue() {
	        for (Entry<String, Kind> entry : namesMap.entrySet()) {
	            if (entry.getValue() == this)
	                return entry.getKey();
	        }
	        return null;
	    }
	}
}
