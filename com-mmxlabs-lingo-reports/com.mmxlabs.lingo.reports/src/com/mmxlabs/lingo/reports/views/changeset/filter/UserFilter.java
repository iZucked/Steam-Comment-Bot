/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.filter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType;
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

	public FilterSlotType rhsType;
	public String rhsKey;

	public FilterVesselType vesselType;
	public String vesselKey;
	private final String label;

	public boolean negate;

	@Override
	public int hashCode() {
		return Objects.hash(label, lhsType, lhsKey, rhsType, rhsKey, vesselType, vesselKey, negate);
	}

	public String getLabel() {
		if (negate) {
			return label + " (exclude)";
		}
		return label;

	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof UserFilter) {
			final UserFilter other = (UserFilter) obj;
			return //
			negate == other.negate //
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

	public UserFilter(final String label) {
		this.label = label;
	}

	public boolean include(final ChangeSetTableGroup group) {
		if (group == null) {
			return false;
		}

		if (lhsType == FilterSlotType.ANY && rhsType == FilterSlotType.ANY && vesselType == FilterVesselType.ANY) {
			// Negate does not apply here
			return true;
		}

		final List<ChangeSetTableRow> rows = new LinkedList<>(group.getRows());
		final Iterator<ChangeSetTableRow> itr = rows.iterator();
		while (itr.hasNext()) {
			final ChangeSetTableRow row = itr.next();
//			Boolean match = null;
//			if (lhsType != FilterSlotType.ANY) {
//				if (match == null) {
//					match = doesLHSMatch(row);
//				} else {
//					match |= doesLHSMatch(row);
//				}
//			}
//			if (lhsType != FilterSlotType.ANY) {
//				if (match == null) {
//					match = doesLHSMatch(row);
//				} else {
//					match |= doesLHSMatch(row);
//				}
//			}
//			if (rhsType != FilterSlotType.ANY) {
//				if (match == null) {
//					match = doesRHSMatch(row);
//				} else {
//					match |= doesRHSMatch(row);
//				}
//			}
//			if (vesselType != FilterVesselType.ANY) {
//				if (match == null) {
//					match = doesVesselMatch(row);
//				} else {
//					match |= doesVesselMatch(row);
//				}
//			}
//
//			if (match != null) {
//				if (match == Boolean.TRUE) {
//					return !negate;
//				}
//			}
			
			final boolean match = doesLHSMatch(row) //
					&& doesRHSMatch(row) //
					&& doesVesselMatch(row);
			if (match) {
				return !negate;
			}
		}
		return negate;

	}

	private boolean doesLHSMatch(final ChangeSetTableRow row) {
		if (lhsType == FilterSlotType.ANY) {
			return true;
		}
		if (lhsType == FilterSlotType.BY_OPEN) {
			return !row.isLhsSlot();
		}
		if (lhsType == FilterSlotType.BY_ID) {
			return (lhsKey.equalsIgnoreCase(row.getLhsName()));
		}
		if (lhsType == FilterSlotType.BY_CONTRACT) {
			return (row.isLhsSlot() && row.getLhsAfter() != null && row.getLhsAfter().getLoadSlot() != null && row.getLhsAfter().getLoadSlot().getContract() != null
					&& lhsKey.equalsIgnoreCase(row.getLhsAfter().getLoadSlot().getContract().getName()));
		}
		try {
			if (lhsType == FilterSlotType.BY_SPOT_MARKET) {
				return (row.isLhsSpot() && row.getLhsAfter() != null && row.getLhsAfter().getLoadSlot() != null && (lhsKey == null
						|| ((SpotSlot) row.getLhsAfter().getLoadSlot()).getMarket() != null && lhsKey.equalsIgnoreCase(((SpotSlot) row.getLhsAfter().getLoadSlot()).getMarket().getName())));
			}
		} catch (final Exception e) {
			if (lhsType == FilterSlotType.BY_SPOT_MARKET) {
				return (row.isLhsSpot() && (lhsKey == null
						|| ((SpotSlot) row.getLhsAfter().getLoadSlot()).getMarket() != null && lhsKey.equalsIgnoreCase(((SpotSlot) row.getLhsAfter().getLoadSlot()).getMarket().getName())));
			}
		}

		return true;
	}

	private boolean doesRHSMatch(final ChangeSetTableRow row) {
		if (rhsType == FilterSlotType.ANY) {
			return true;
		}
		if (rhsType == FilterSlotType.BY_OPEN) {
			return !row.isCurrentRhsSlot();
		}
		if (rhsType == FilterSlotType.BY_ID) {
			return (rhsKey.equalsIgnoreCase(row.getCurrentRhsName()));
		}
		if (rhsType == FilterSlotType.BY_CONTRACT) {
			return (row.isCurrentRhsSlot() && row.getCurrentRhsAfter() != null && row.getCurrentRhsAfter().getDischargeSlot().getContract() != null
					&& rhsKey.equalsIgnoreCase(row.getCurrentRhsAfter().getDischargeSlot().getContract().getName()));
		}
		if (rhsType == FilterSlotType.BY_SPOT_MARKET) {
			return (row.isCurrentRhsSpot() && (rhsKey == null //
					|| ((SpotSlot) row.getCurrentRhsAfter().getDischargeSlot()).getMarket() != null //
							&& rhsKey.equalsIgnoreCase(((SpotSlot) row.getCurrentRhsAfter().getDischargeSlot()).getMarket().getName())));
		}

		return true;
	}

	private boolean doesVesselMatch(final ChangeSetTableRow row) {
		if (vesselType == FilterVesselType.ANY) {
			return true;
		}
		if (vesselType == FilterVesselType.BY_NAME) {
			return vesselKey.equalsIgnoreCase(row.getAfterVesselName());
		}
		if (vesselType == FilterVesselType.BY_SPOT_MARKET) {
			return row.getAfterVesselType() == ChangeSetVesselType.MARKET;
		}
		if (vesselType == FilterVesselType.BY_FLEET) {
			return row.getAfterVesselType() == ChangeSetVesselType.FLEET;
		}
		if (vesselType == FilterVesselType.BY_NOMINAL) {
			return row.getAfterVesselType() == ChangeSetVesselType.NOMINAL;
		}

		return true;
	}

}
