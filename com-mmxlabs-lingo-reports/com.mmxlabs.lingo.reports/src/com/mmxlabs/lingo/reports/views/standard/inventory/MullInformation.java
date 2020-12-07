package com.mmxlabs.lingo.reports.views.standard.inventory;

import java.time.YearMonth;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

public class MullInformation {
	public YearMonth ym;
	public BaseLegalEntity entity;
	public int overlift;
	public int lifted;
	public int monthlyRE;
	public int monthEndEntitlement;
	public int cargoCount;
	
	public YearMonth getYM() {
		return this.ym;
	}
	
	public BaseLegalEntity getEntity() {
		return this.entity;
	}
	
	public int getOverlift() {
		return this.overlift;
	}
	
	public int getLifted() {
		return this.lifted;
	}
	
	public int getMonthlyRE() {
		return this.monthlyRE;
	}
	
	public int getMonthEndEntitlement() {
		return this.monthEndEntitlement;
	}
	
	public int getCargoCount() {
		return this.cargoCount;
	}
}
