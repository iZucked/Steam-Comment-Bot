/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

public class HeelValueRecord {
	private final long heelRevenue;
	private final long heelCost;
	private final int revenueUnitPrice;
	private final int costUnitPrice;

	public HeelValueRecord(final long cost, final int costUnitPrice, final long revenue, final int revenueUnitPrice) {
		this.heelCost = cost;
		this.costUnitPrice = costUnitPrice;
		this.heelRevenue = revenue;
		this.revenueUnitPrice = revenueUnitPrice;
	}

	public int getRevenueUnitPrice() {
		return revenueUnitPrice;
	}

	public int getCostUnitPrice() {
		return costUnitPrice;
	}

	public long getHeelRevenue() {
		return heelRevenue;
	}

	public long getHeelCost() {
		return heelCost;
	}

	public static HeelValueRecord withRevenue(final long revenue, final int unitPrice) {
		return new HeelValueRecord(0, 0, revenue, unitPrice);

	}

	public static HeelValueRecord withCost(final long cost, final int unitPrice) {
		return new HeelValueRecord(cost, unitPrice, 0, 0);
	}

	public static HeelValueRecord merge(final HeelValueRecord a, final HeelValueRecord b) {
		final long cost = a.getHeelCost() + b.getHeelCost();
		final long revenue = a.getHeelRevenue() + b.getHeelRevenue();
		final int costUnitPrice = a.getCostUnitPrice() + b.getCostUnitPrice();
		final int revenueUnitPrice = a.getRevenueUnitPrice() + b.getRevenueUnitPrice();

		// Only expect one heel price to be present at a time.
		assert a.getCostUnitPrice() == costUnitPrice || b.getCostUnitPrice() == costUnitPrice;
		assert a.getRevenueUnitPrice() == revenueUnitPrice || b.getRevenueUnitPrice() == revenueUnitPrice;

		return new HeelValueRecord(cost, costUnitPrice, revenue, revenueUnitPrice);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof HeelValueRecord) {
			HeelValueRecord other = (HeelValueRecord) obj;
			if (heelCost != other.heelCost) {
				return false;
			}
			if (heelRevenue != other.heelRevenue) {
				return false;
			}
			if (costUnitPrice != other.costUnitPrice) {
				return false;
			}
			if (revenueUnitPrice != other.revenueUnitPrice) {
				return false;
			}
			return true;
		}

		return false;
	}
}