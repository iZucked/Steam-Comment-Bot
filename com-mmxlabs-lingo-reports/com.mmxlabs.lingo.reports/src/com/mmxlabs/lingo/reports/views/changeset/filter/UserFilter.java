/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.filter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.models.lng.cargo.SpotSlot;

public class UserFilter {
	public enum FilterSlotType {
		ANY, BY_CONTRACT, BY_SPOT_MARKET, BY_SHIPPED_STATUS, BY_ID, BY_OPEN
	}

	public enum FilterVesselType {
		ANY, BY_FLEET, BY_SPOT_MARKET, BY_NOMINAL, BY_NAME
	}

	public FilterSlotType lhsType;
	public String lhsKey;
	public boolean lhsNegate;

	public FilterSlotType rhsType;
	public String rhsKey;
	public boolean rhsNegate;

	public FilterVesselType vesselType;
	public String vesselKey;
	public boolean vesselNegate;
	private String label;

	@Override
	public int hashCode() {
		return Objects.hash(label, lhsType, lhsKey, lhsNegate, rhsType, rhsKey, rhsNegate, vesselType, vesselKey, vesselNegate);
	}

	public String getLabel() {
		if (lhsNegate || rhsNegate || vesselNegate) {
			return label + " (exclude)";
		}
		return label;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof UserFilter) {
			UserFilter other = (UserFilter) obj;
			return //
			lhsNegate == other.lhsNegate //
					&& rhsNegate == other.rhsNegate //
					&& vesselNegate == other.vesselNegate //
					&& Objects.equals(lhsType, other.lhsType) //
					&& Objects.equals(rhsType, other.rhsType) //
					&& Objects.equals(vesselType, other.vesselType) //
					&& Objects.equals(label, other.label) //
					&& Objects.equals(lhsKey, other.lhsKey) //
					&& Objects.equals(rhsKey, other.rhsKey) //
					&& Objects.equals(vesselKey, other.vesselKey);

		}
		return super.equals(obj);
	}

	public UserFilter(String label) {
		this.label = label;
	}

	public boolean include(ChangeSetTableGroup group) {
		if (lhsType == FilterSlotType.ANY && rhsType == FilterSlotType.ANY && vesselType == FilterVesselType.ANY) {
			// Negate does not apply here
			return true;
		}

		List<ChangeSetTableRow> rows = new LinkedList<>(group.getRows());
		Iterator<ChangeSetTableRow> itr = rows.iterator();
		while (itr.hasNext()) {
			ChangeSetTableRow row = itr.next();
			if (!doesLHSMatch(row)) {
				itr.remove();
				continue;
			}
			if (!doesRHSMatch(row)) {
				itr.remove();
				continue;
			}
			if (!doesVesselMatch(row)) {
				itr.remove();
				continue;
			}
		}
		return !rows.isEmpty();
	}

	private boolean doesLHSMatch(ChangeSetTableRow row) {
		if (lhsType == FilterSlotType.ANY) {
			return true;
		}
		if (lhsType == FilterSlotType.BY_OPEN) {
			return !lhsNegate == !row.isLhsSlot();
		}
		if (lhsType == FilterSlotType.BY_ID) {
			return !lhsNegate == (lhsKey.equalsIgnoreCase(row.getLhsName()));
		}
		if (lhsType == FilterSlotType.BY_CONTRACT) {
			return !lhsNegate == (row.isLhsSlot() && row.getLhsAfter().getLoadSlot() != null && row.getLhsAfter().getLoadSlot().getContract() != null
					&& lhsKey.equalsIgnoreCase(row.getLhsAfter().getLoadSlot().getContract().getName()));
		}
		if (lhsType == FilterSlotType.BY_SPOT_MARKET) {
			return !lhsNegate == (row.isLhsSpot() && (lhsKey == null
					|| ((SpotSlot) row.getLhsAfter().getLoadSlot()).getMarket() != null && lhsKey.equalsIgnoreCase(((SpotSlot) row.getLhsAfter().getLoadSlot()).getMarket().getName())));
		}

		return true;
	}

	private boolean doesRHSMatch(ChangeSetTableRow row) {
		if (rhsType == FilterSlotType.ANY) {
			return true;
		}
		if (rhsType == FilterSlotType.BY_OPEN) {
			return !rhsNegate == !row.isRhsSlot();
		}
		if (rhsType == FilterSlotType.BY_ID) {
			return !rhsNegate == (rhsKey.equalsIgnoreCase(row.getRhsName()));
		}
		if (rhsType == FilterSlotType.BY_CONTRACT) {
			return !rhsNegate == (row.isRhsSlot() && row.getRhsAfter().getDischargeSlot().getContract() != null
					&& rhsKey.equalsIgnoreCase(row.getRhsAfter().getDischargeSlot().getContract().getName()));
		}
		if (rhsType == FilterSlotType.BY_SPOT_MARKET) {
			return !rhsNegate == (row.isRhsSpot() && (rhsKey == null
					|| ((SpotSlot) row.getRhsAfter().getDischargeSlot()).getMarket() != null && rhsKey.equalsIgnoreCase(((SpotSlot) row.getRhsAfter().getDischargeSlot()).getMarket().getName())));
		}

		return true;
	}

	private boolean doesVesselMatch(ChangeSetTableRow row) {
		if (vesselType == FilterVesselType.ANY) {
			return true;
		}
		if (vesselType == FilterVesselType.BY_NAME) {
			return !vesselNegate == vesselKey.equalsIgnoreCase(row.getAfterVesselName());
		}

		return true;
	}

}
