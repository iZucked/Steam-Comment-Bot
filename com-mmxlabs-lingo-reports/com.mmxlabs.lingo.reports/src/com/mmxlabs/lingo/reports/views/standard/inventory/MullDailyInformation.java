package com.mmxlabs.lingo.reports.views.standard.inventory;

import java.time.LocalDate;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

public class MullDailyInformation {
	public LocalDate date;
	public BaseLegalEntity entity;
	public int allocatedEntitlement;
	public int actualEntitlement;
	public boolean grey;
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public int getAllocatedEntitlement() {
		return this.allocatedEntitlement;
	}
	
	public int getActualEntitlement() {
		return this.actualEntitlement;
	}
}
