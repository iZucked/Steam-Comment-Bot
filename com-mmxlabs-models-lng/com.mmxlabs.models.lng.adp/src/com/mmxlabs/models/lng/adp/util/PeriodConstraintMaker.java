/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.util;

import java.time.YearMonth;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;

public class PeriodConstraintMaker<T> {
	private ContractProfile<?, ?> contractProfile;
	private PeriodDistributionProfileConstraint constraint;

	private T builder;

	public PeriodConstraintMaker(ContractProfile<?, ?> model, PeriodDistributionProfileConstraint constraint, T builder) {
		this.contractProfile = model;
		this.constraint = constraint;
		this.builder = builder;
	}

	public static <T> PeriodConstraintMaker<T> makePeriodDistributionProfileConstraint(ContractProfile<?, ?> model, T builder) {
		PeriodConstraintMaker<T> maker = new PeriodConstraintMaker<>(model, ADPFactory.eINSTANCE.createPeriodDistributionProfileConstraint(), builder);

		return maker;
	}

	public PeriodConstraintMaker<T> withRow(Integer minCargoes, Integer maxCargoes, YearMonth... months) {
		PeriodDistribution distribution = ADPFactory.eINSTANCE.createPeriodDistribution();
		if (minCargoes != null) {
			distribution.setMinCargoes(minCargoes);
		}
		if (maxCargoes != null) {
			distribution.setMaxCargoes(maxCargoes);
		}
		for (YearMonth m : months) {
			distribution.getRange().add(m);
		}
		constraint.getDistributions().add(distribution);
		return this;
	}

	public T build() {
		contractProfile.getConstraints().add(constraint);
		return builder;
	}
}
