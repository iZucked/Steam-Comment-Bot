/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.filter;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.mmxlabs.lingo.reports.views.changeset.filter.UserFilter.FilterSlotType;
import com.mmxlabs.lingo.reports.views.changeset.filter.UserFilter.FilterVesselType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;

public class UserFilterTests {

	@Test
	public void testNull() {

		final UserFilter filter = new UserFilter("Filter");

		Assertions.assertFalse(filter.include(null));

	}

	@Test
	public void testMatchAnything() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;
		filter.rhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

		Assertions.assertTrue(filter.include(group));

	}

	@TestFactory
	List<DynamicNode> testMatchLoad_ID() {

		final UserFilter filter = new UserFilter("Filter");
		filter.rhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		filter.lhsType = FilterSlotType.BY_ID;
		filter.lhsKey = "slotid";

		final List<DynamicNode> cases = new LinkedList<>();

		// Matching slot id
		cases.add(DynamicTest.dynamicTest("Matching slot id", () -> {

			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, false, rd -> rd.setLhsName("slotid"));
			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));
		// Matching slot id - multiple rows
		cases.add(DynamicTest.dynamicTest(" Matching slot id - multiple rows", () -> {

			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final ChangeSetTableRow row1 = createAfterRow(true, false, rd -> rd.setLhsName("notslotid"));
			group.getRows().add(row1);

			final ChangeSetTableRow row2 = createAfterRow(true, false, rd -> rd.setLhsName("slotid"));
			group.getRows().add(row2);

			final ChangeSetTableRow row3 = createAfterRow(true, false, rd -> rd.setLhsName("notslotid3"));
			group.getRows().add(row3);

			Assertions.assertTrue(filter.include(group));
		}));

		// Mismatching slot id
		cases.add(DynamicTest.dynamicTest("Mismatching slot id", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, false, rd -> rd.setLhsName("notslotid"));

			group.getRows().add(row);

			Assertions.assertFalse(filter.include(group));
		}));
		// No slot/row at all
		cases.add(DynamicTest.dynamicTest("No slot/row at all", () -> {

			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			Assertions.assertFalse(filter.include(group));
		}));
		// Matching slot id - wrong slot
		cases.add(DynamicTest.dynamicTest("Matching slot id - wrong slot", () -> {

			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(false, true, rd -> rd.setRhsName("slotid"));

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));
		return cases;
	}

	private ChangeSetTableRow createAfterRow(boolean asLHS, boolean asRHS, Consumer<ChangeSetRowData> action) {
		final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

		final ChangeSetRowData rd = ChangesetFactory.eINSTANCE.createChangeSetRowData();
		final ChangeSetRowDataGroup rdg = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
		rd.setRowDataGroup(rdg);
		action.accept(rd);

		if (asLHS) {
			row.setLhsAfter(rd);
		}
		if (asRHS) {
			row.setCurrentRhsAfter(rd);
		}

		return row;
	}

	private ChangeSetTableRow createBeforeRow(boolean asLHS, boolean asRHS, Consumer<ChangeSetRowData> action) {
		final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

		final ChangeSetRowData rd = ChangesetFactory.eINSTANCE.createChangeSetRowData();
		final ChangeSetRowDataGroup rdg = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
		rd.setRowDataGroup(rdg);
		action.accept(rd);

		if (asLHS) {
			row.setLhsBefore(rd);
		}
		if (asRHS) {
			row.setPreviousRhsBefore(rd);
		}

		return row;
	}

	@TestFactory
	List<DynamicNode> testMatchDischarge_ID() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		filter.rhsType = FilterSlotType.BY_ID;
		filter.rhsKey = "slotid";

		final List<DynamicNode> cases = new LinkedList<>();

		// Matching slot id
		cases.add(DynamicTest.dynamicTest(" Matching slot id", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(false, true, rd -> rd.setRhsName("slotid"));

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));
		// Matching slot id - multiple rows
		cases.add(DynamicTest.dynamicTest(" Matching slot id - multiple rows", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row1 = createAfterRow(false, true, rd -> rd.setRhsName("notslotid"));
			group.getRows().add(row1);
			final ChangeSetTableRow row2 = createAfterRow(false, true, rd -> rd.setRhsName("slotid"));
			group.getRows().add(row2);
			final ChangeSetTableRow row3 = createAfterRow(false, true, rd -> rd.setRhsName("notslotid3"));
			group.getRows().add(row3);

			Assertions.assertTrue(filter.include(group));
		}));
		// Mismatching slot id
		cases.add(DynamicTest.dynamicTest("Mismatching slot id", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(false, true, rd -> rd.setRhsName("notslotid"));
			group.getRows().add(row);

			Assertions.assertFalse(filter.include(group));
		}));
		// No slot/row at all
		cases.add(DynamicTest.dynamicTest("No slot/row at all", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			Assertions.assertFalse(filter.include(group));
		}));
		// Matching slot id - wrong slot
		cases.add(DynamicTest.dynamicTest(" Matching slot id - wrong slot", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, false, rd -> rd.setLhsName("slotid"));

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));

		return cases;
	}

	@TestFactory
	List<DynamicNode> testMatchLoad_Contract() {

		final UserFilter filter = new UserFilter("Filter");
		filter.rhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		filter.lhsType = FilterSlotType.BY_CONTRACT;
		filter.lhsKey = "contractid";

		final List<DynamicNode> cases = new LinkedList<>();

		// Matching contract id - after
		cases.add(DynamicTest.dynamicTest("Matching contract id - after", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
			contract.setName("contractid");
			slot.setContract(contract);

			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setLoadSlot(slot);
				rd.setLhsSlot(true);
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));

		// Matching contract id - before
		cases.add(DynamicTest.dynamicTest("Matching contract id - before", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
			contract.setName("contractid");
			slot.setContract(contract);

			final ChangeSetTableRow row = createBeforeRow(true, false, rd -> {
				rd.setLoadSlot(slot);
				rd.setLhsSlot(true);
			});

			group.getRows().add(row);
			// Filter only applies to after
			Assertions.assertFalse(filter.include(group));
		}));

		// Mis-Matching contract id - after
		cases.add(DynamicTest.dynamicTest("Mis-Matching contract id - after", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
			contract.setName("notcontractid");
			slot.setContract(contract);

			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setLoadSlot(slot);
				rd.setLhsSlot(true);
			});

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));
		// Null contract name
		cases.add(DynamicTest.dynamicTest("Null contract name", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
			contract.setName(null);
			slot.setContract(contract);
			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setLoadSlot(slot);
				rd.setLhsSlot(true);
			});
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));

		// No contract - after
		cases.add(DynamicTest.dynamicTest("No contract - after", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();

			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setLoadSlot(slot);
				rd.setLhsSlot(true);
			});

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));

		return cases;
	}

	@TestFactory
	List<DynamicNode> testMatchDischarge_Contract() {

		final UserFilter filter = new UserFilter("Filter");
		filter.rhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		filter.rhsType = FilterSlotType.BY_CONTRACT;
		filter.rhsKey = "contractid";

		final List<DynamicNode> cases = new LinkedList<>();

		// Matching contract id - after
		cases.add(DynamicTest.dynamicTest("Matching contract id - after", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
			contract.setName("contractid");
			slot.setContract(contract);

			final ChangeSetTableRow row = createAfterRow(false, true, rd -> {
				rd.setDischargeSlot(slot);
				rd.setRhsSlot(true);
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));

		// Matching contract id - before
		cases.add(DynamicTest.dynamicTest("Matching contract id - before", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
			contract.setName("contractid");
			slot.setContract(contract);

			final ChangeSetTableRow row = createBeforeRow(false, true, rd -> {
				rd.setDischargeSlot(slot);
				rd.setRhsSlot(true);
			});

			group.getRows().add(row);
			// Filter only applies to after
			Assertions.assertFalse(filter.include(group));
		}));

		//
		cases.add(DynamicTest.dynamicTest("Mis-Matching contract id - after", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
			contract.setName("notcontractid");
			slot.setContract(contract);
			final ChangeSetTableRow row = createAfterRow(false, true, rd -> {
				rd.setDischargeSlot(slot);
				rd.setRhsSlot(true);
			});
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));

		//
		cases.add(DynamicTest.dynamicTest("Null contract name", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
			contract.setName(null);
			slot.setContract(contract);
			final ChangeSetTableRow row = createAfterRow(false, true, rd -> {
				rd.setDischargeSlot(slot);
				rd.setRhsSlot(true);
			});
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));

		// No contract - after
		cases.add(DynamicTest.dynamicTest("No contract - after", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final ChangeSetTableRow row = createAfterRow(false, true, rd -> {
				rd.setDischargeSlot(slot);
				rd.setRhsSlot(true);
			});
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));

		return cases;
	}

	@TestFactory
	List<DynamicNode> testMatchVessel_ID() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;
		filter.rhsType = FilterSlotType.ANY;

		filter.vesselType = FilterVesselType.BY_NAME;
		filter.vesselKey = "vesselname";

		final List<DynamicNode> cases = new LinkedList<>();

		// Matching vessel id
		cases.add(DynamicTest.dynamicTest("Matching vessel id", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setVesselName("vesselname");
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));
		// Mis-Matching vessel id
		cases.add(DynamicTest.dynamicTest("Mis-Matching vessel id", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setVesselName("notvesselname");
			});

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));

		return cases;

	}

	@TestFactory
	List<DynamicNode> testMatchVessel_NotID() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;
		filter.rhsType = FilterSlotType.ANY;

		filter.vesselType = FilterVesselType.BY_NAME;
		filter.vesselKey = "vesselname";
		filter.negate = true;

		final List<DynamicNode> cases = new LinkedList<>();

		// Matching vessel id
		cases.add(DynamicTest.dynamicTest("Matching vessel id", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setVesselName("vesselname");
			});

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));
		// Mis-Matching vessel id
		cases.add(DynamicTest.dynamicTest("Matching vessel id", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setVesselName("notvesselname");
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));
		// null
		cases.add(DynamicTest.dynamicTest("null vessel", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, false, rd -> {
				rd.setVesselName(null);
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));

		return cases;

	}

	@TestFactory
	List<DynamicNode> testMatchSlotAndVessel_SalesAndVessel() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;

		filter.rhsType = FilterSlotType.BY_ID;
		filter.rhsKey = "slotid";

		filter.vesselType = FilterVesselType.BY_NAME;
		filter.vesselKey = "vesselname";

		final List<DynamicNode> cases = new LinkedList<>();

		// Matching
		cases.add(DynamicTest.dynamicTest("Matching", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
				rd.setLhsName("lhsslot");
				rd.setRhsName("slotid");
				rd.setVesselName("vesselname");
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));
		cases.add(DynamicTest.dynamicTest("Slot mismatch", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
				rd.setLhsName("lhsslot");
				rd.setRhsName("notslotid");
				rd.setVesselName("vesselname");
			});

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));
		cases.add(DynamicTest.dynamicTest("Vessel mismatch", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
				rd.setLhsName("lhsslot");
				rd.setRhsName("slotid");
				rd.setVesselName("");
			});

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));

		return cases;
	}

	@TestFactory
	List<DynamicNode> testMatchSlotAndVessel_Negate_SalesAndVessel() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;

		filter.negate = true;
		
		filter.rhsType = FilterSlotType.BY_ID;
		filter.rhsKey = "slotid";

		filter.vesselType = FilterVesselType.BY_NAME;
		filter.vesselKey = "vesselname";

		final List<DynamicNode> cases = new LinkedList<>();

		// Matching
		cases.add(DynamicTest.dynamicTest("Matching", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
				rd.setLhsName("lhsslot");
				rd.setRhsName("slotid");
				rd.setVesselName("vesselname");
			});

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}));
		// Not matching
		cases.add(DynamicTest.dynamicTest("Not matching #1", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
				rd.setLhsName("lhsslot");
				rd.setRhsName("notslotid");
				rd.setVesselName("vesselname");
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));
		cases.add(DynamicTest.dynamicTest("Not matching #2", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
				rd.setLhsName("lhsslot");
				rd.setRhsName("slotid");
				rd.setVesselName("notvesselname");
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));
		cases.add(DynamicTest.dynamicTest("Not matching #3", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
				rd.setLhsName("lhsslot");
				rd.setRhsName("notslotid");
				rd.setVesselName("notvesselname");
			});

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}));
		// Multiple rows
		cases.add(DynamicTest.dynamicTest("Multiple rows #1", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			{
				final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
					rd.setLhsName("lhsslot");
					rd.setRhsName("notslotid");
					rd.setVesselName("notvesselname");
				});

				group.getRows().add(row);
			}
			{
				final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
					rd.setLhsName("lhsslot");
					rd.setRhsName("slotid");
					rd.setVesselName("vesselname");
				});

				group.getRows().add(row);
			}
			Assertions.assertFalse(filter.include(group));
		}));
		// Multiple rows
		cases.add(DynamicTest.dynamicTest("Multiple rows #2", () -> {
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			{
				final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
					rd.setLhsName("lhsslot");
					rd.setRhsName("notslotid");
					rd.setVesselName("notvesselname");
				});

				group.getRows().add(row);
			}
			{

				final ChangeSetTableRow row = createAfterRow(true, true, rd -> {
					rd.setLhsName("lhsslot2");
					rd.setRhsName("not2slotid");
					rd.setVesselName("vesselname");
				});

				group.getRows().add(row);
			}
			Assertions.assertTrue(filter.include(group));
		}));

		return cases;
	}
}
