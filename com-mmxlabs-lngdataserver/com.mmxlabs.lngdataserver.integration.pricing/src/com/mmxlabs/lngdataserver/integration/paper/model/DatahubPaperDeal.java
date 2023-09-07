package com.mmxlabs.lngdataserver.integration.paper.model;

import java.time.LocalDate;
import java.time.YearMonth;
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

// This file is a 1 to 1 mapping to the Paper representation on datahub

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
			String index,
			String comment,
			// String entity,
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
		// this.entity=entity;
		this.pricingType=pricingType;
		this.kind = kind;
	}
	
	public DatahubPaperDeal(
			double price,
			double quantity,
			int year,
			YearMonth pricingMonth,
			LocalDate pricingPeriodStart,
			LocalDate pricingPeriodEnd,
			LocalDate hedgingPeriodStart,
			LocalDate hedgingPeriodEnd,
			String name,
			String index,
			String comment,
			// String entity,
			PricingType pricingType,
			Kind kind)
	{
		this(
			price, quantity, year,
			pricingMonth.atDay(1),
			pricingPeriodStart, pricingPeriodEnd, hedgingPeriodStart, hedgingPeriodEnd,
			name, index, comment, pricingType, kind
		);
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
