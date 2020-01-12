/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.commercial.Contract;

public class IncomeStatementByContract extends AbstractIncomeStatement<String> {

	public IncomeStatementByContract() {
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
	protected int doCompare(IncomeStatementData o1, IncomeStatementData o2) {
		if (o1.key instanceof Enum && o2.key instanceof Enum) {
			return ((Enum) o1.key).ordinal() - ((Enum) o2.key).ordinal();
		} else {
			return o1.key.toString().compareTo(o2.key.toString());
		}

	}

	@Override
	protected String doGetColumnText(IncomeStatementData data) {
		return data.key.toString();
	}
}