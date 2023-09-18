package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mmxlabs.common.Pair;

public class CommodityCurveData {
	private String name;
	private Map<LocalDate, List<Double>> dateMap;
	private String currency;
	private String unit;
	
	public CommodityCurveData(String name, String currency, String unit) {
		this.name = name;
		this.currency = currency;
		this.unit = unit;
		dateMap = new HashMap<>();
	}
	

	/**
	 * Adds date and price to map, If date is already found it adds the price to the list of prices found for that day
	 * @param date
	 * @param price
	 */
	public void addDateAndPrice(LocalDate date, double price) {
		// Check if date is already in data points
		if(dateMap.containsKey(date))
			dateMap.get(date).add(price);
		else {
			List<Double> prices = new ArrayList<>();
			prices.add(price);
			dateMap.put(date, prices);
		}
	}
	
	/**
	 * Returns the list of dates with the average MTM price for that date 
	 * @return
	 */
	public List<Pair<LocalDate, Double>> getDatesAndPrices() {
		List<Pair<LocalDate, Double>> datesAndPrices = new ArrayList<>();
		
		for(Entry<LocalDate, List<Double>> entry : dateMap.entrySet()) {
			Pair<LocalDate, Double> dateAndPrice = new Pair<>();
			dateAndPrice.setBoth(entry.getKey(), getAveragePrice(entry.getValue()));
			datesAndPrices.add(dateAndPrice);
		}
		
		return datesAndPrices;
	}
	
	private double getAveragePrice(List<Double> prices) {
		if(prices.isEmpty())
			return 0.0;
		
		if(prices.size() == 1)
			return prices.get(0);
		
		double sum = 0.0;
		for(double price : prices)
			sum += price;
		
		return sum / prices.size();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
