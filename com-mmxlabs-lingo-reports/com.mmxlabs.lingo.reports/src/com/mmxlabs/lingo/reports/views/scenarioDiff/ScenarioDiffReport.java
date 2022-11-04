/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.scenarioDiff;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.commercial.Contract;

public class ScenarioDiffReport extends AbstractScenarioDiff<String> {

	public ScenarioDiffReport() {
		super("com.mmxlabs.lingo.reports.Reports_IncomeStatementByContract");
	}
	
	private Set<String> seenContracts = new HashSet<>();
	private static final String SPOT_CONTRACT = "<Spot>";

	@Override
	protected String getSubType(final DischargeSlot dischargeSlot) {
		
		Contract contract = dischargeSlot.getContract();
		final String name;
		if (contract != null) {
			name = contract.getName();
		} else {
			name = SPOT_CONTRACT;
		}
		seenContracts.add(name);
		return name;
	}

	@Override
	protected Collection<String> getSubTypes() {
		
		return seenContracts;
	}

	@Override
	protected void resetData() {
		
		seenContracts.clear();
	}

	@Override
	protected int doCompare(ScenarioDiffData o1, ScenarioDiffData o2) {
		return o1.type.compareTo(o2.type);
	}

	@Override
	protected String doGetColumnText(ScenarioDiffData data) {
		
		return data.type;
	}
}