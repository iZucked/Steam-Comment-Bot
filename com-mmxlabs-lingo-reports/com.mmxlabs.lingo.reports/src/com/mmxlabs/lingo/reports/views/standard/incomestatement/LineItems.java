/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

enum LineItems {
	Revenues("Revenues"),
	SalesVolume("Sales Volume"),
	Costs("Costs"), 
	PurchaseVolume("Purchase Volume"),
	BOG("BOG"), GrossMargin("Gross Margin"), ShippingCosts("Shipping Costs"), Hire("Hire"), VariableCosts("Variable Costs"), PortCharges("Port Charges"), Ebitda(
			"Ebitda")
	
	;
	private final String name;
	private final String tooltip;

	private LineItems(String name) {
		this(name, name);
	}

	private LineItems(String name, String tooltip) {
		this.name = name;
		this.tooltip = tooltip;
	}

	public String getName() {
		return name;
	}

	public String getTooltip() {
		return tooltip;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}