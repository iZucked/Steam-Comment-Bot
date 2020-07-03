/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargocontract;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */


import java.util.List;


/**
 * @author josephpallamidessi
 *
 */
public class CargoesPerContractReportModel {
	
	int year;
	int month;
	List<ContractReportModel> contracts;
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	
	public List<ContractReportModel> getContracts() {
		return contracts;
	}
	public void setContracts(List<ContractReportModel> contracts) {
		this.contracts = contracts;
	}
    	
}
