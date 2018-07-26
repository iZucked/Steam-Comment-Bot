package com.mmxlabs.lngdataserver.integration.reports.cargocontract;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.List;


/**
 * @author josephpallamidessi
 *
 */
public class ContractReportModel {
		private String name;
		private int nb;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getNb() {
			return nb;
		}
		public void setNb(int nb) {
			this.nb = nb;
		}
}
