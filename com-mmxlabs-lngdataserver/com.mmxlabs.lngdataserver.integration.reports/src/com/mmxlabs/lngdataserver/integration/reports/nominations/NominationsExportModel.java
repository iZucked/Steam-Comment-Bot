/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.util.List;

public class NominationsExportModel {
	List<Nominations> nominations = null;
	
	public List<Nominations> getNominations() {
		return nominations;
	}
	public void setNominations(List<Nominations> nominations) {
		this.nominations = nominations;
	}
}
