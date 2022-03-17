/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.reports.views.changeset.filter.UserFilter.FilterSlotType;
import com.mmxlabs.lingo.reports.views.changeset.filter.UserFilter.FilterVesselType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
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

	@Test
	public void testMatchLoad_ID() {

		final UserFilter filter = new UserFilter("Filter");
		filter.rhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		filter.lhsType = FilterSlotType.BY_ID;
		filter.lhsKey = "slotid";

		// Matching slot id
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setLhsName("slotid");

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}
		// Matching slot id - multiple rows
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row1 = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row1.setLhsName("notslotid");
			group.getRows().add(row1);
			final ChangeSetTableRow row2 = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row2.setLhsName("slotid");
			group.getRows().add(row2);
			final ChangeSetTableRow row3 = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row3.setLhsName("notslotid3");
			group.getRows().add(row3);

			Assertions.assertTrue(filter.include(group));
		}
		// Mismatching slot id
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setLhsName("notslotid");

			group.getRows().add(row);

			Assertions.assertFalse(filter.include(group));
		}
		// No slot/row at all
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			Assertions.assertFalse(filter.include(group));
		}
		// Matching slot id - wrong slot
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("slotid");

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}
	}

	@Test
	public void testMatchDischarge_ID() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		filter.rhsType = FilterSlotType.BY_ID;
		filter.rhsKey = "slotid";

		// Matching slot id
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("slotid");

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}
		// Matching slot id - multiple rows
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row1 = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row1.setRhsName("notslotid");
			group.getRows().add(row1);
			final ChangeSetTableRow row2 = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row2.setRhsName("slotid");
			group.getRows().add(row2);
			final ChangeSetTableRow row3 = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row3.setRhsName("notslotid3");
			group.getRows().add(row3);

			Assertions.assertTrue(filter.include(group));
		}
		// Mismatching slot id
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("notslotid");

			group.getRows().add(row);

			Assertions.assertFalse(filter.include(group));
		}
		// No slot/row at all
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			Assertions.assertFalse(filter.include(group));
		}
		// Matching slot id - wrong slot
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setLhsName("slotid");

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}
	}

	@Test
	public void testMatchLoad_Contract() {

		final UserFilter filter = new UserFilter("Filter");
		filter.rhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		filter.lhsType = FilterSlotType.BY_CONTRACT;
		filter.lhsKey = "contractid";

		// Matching contract id - after
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
			contract.setName("contractid");
			slot.setContract(contract);
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setLoadSlot(slot);

			row.setLhsAfter(data);
			row.setLhsSlot(true);
			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}

		// Matching contract id - before
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
			contract.setName("contractid");
			slot.setContract(contract);
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setLoadSlot(slot);
			row.setLhsBefore(data);
			row.setLhsSlot(true);

			group.getRows().add(row);
			// Filter only applies to after
			Assertions.assertFalse(filter.include(group));
		}

		// Mis-Matching contract id - after
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
			contract.setName("notcontractid");
			slot.setContract(contract);
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setLoadSlot(slot);

			row.setLhsAfter(data);
			row.setLhsSlot(true);
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}

		// Null contract name
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
			contract.setName(null);
			slot.setContract(contract);
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setLoadSlot(slot);

			row.setLhsAfter(data);
			row.setLhsSlot(true);
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}

		// No contract - after
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setLoadSlot(slot);

			row.setLhsAfter(data);
			row.setLhsSlot(true);
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}
	}

	@Test
	public void testMatchDischarge_Contract() {

		final UserFilter filter = new UserFilter("Filter");
		filter.rhsType = FilterSlotType.ANY;
		filter.vesselType = FilterVesselType.ANY;

		filter.rhsType = FilterSlotType.BY_CONTRACT;
		filter.rhsKey = "contractid";

		// Matching contract id - after
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
			contract.setName("contractid");
			slot.setContract(contract);
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setDischargeSlot(slot);

			row.setRhsAfter(data);
			row.setRhsSlot(true);
			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}

		// Matching contract id - before
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
			contract.setName("contractid");
			slot.setContract(contract);
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setDischargeSlot(slot);
			row.setRhsBefore(data);
			row.setRhsSlot(true);

			group.getRows().add(row);
			// Filter only applies to after
			Assertions.assertFalse(filter.include(group));
		}

		// Mis-Matching contract id - after
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
			contract.setName("notcontractid");
			slot.setContract(contract);
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setDischargeSlot(slot);

			row.setRhsAfter(data);
			row.setRhsSlot(true);
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}

		// Null contract name
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
			contract.setName(null);
			slot.setContract(contract);
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setDischargeSlot(slot);

			row.setRhsAfter(data);
			row.setRhsSlot(true);
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}

		// No contract - after
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			final ChangeSetRowData data = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			data.setDischargeSlot(slot);

			row.setRhsAfter(data);
			row.setRhsSlot(true);
			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}
	}

	@Test
	public void testMatchVessel_ID() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;
		filter.rhsType = FilterSlotType.ANY;

		filter.vesselType = FilterVesselType.BY_NAME;
		filter.vesselKey = "vesselname";

		// Matching vessel id
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setAfterVesselName("vesselname");

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}
		// Mis-Matching vessel id
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setAfterVesselName("notvesselname");

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}

	}

	@Test
	public void testMatchVessel_NotID() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;
		filter.rhsType = FilterSlotType.ANY;

		filter.vesselType = FilterVesselType.BY_NAME;
		filter.vesselKey = "vesselname";
		filter.negate = true;

		// Matching vessel id
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setAfterVesselName("vesselname");

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}
		// Mis-Matching vessel id
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setAfterVesselName("notvesselname");

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}
		// null
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setAfterVesselName(null);

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}

	}

	@Test
	public void testMatchSlotAndVessel_SalesAndVessel() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;

		filter.rhsType = FilterSlotType.BY_ID;
		filter.rhsKey = "slotid";

		filter.vesselType = FilterVesselType.BY_NAME;
		filter.vesselKey = "vesselname";

		// Matching
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("slotid");
			row.setAfterVesselName("vesselname");

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("notslotid");
			row.setAfterVesselName("vesselname");

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("slotid");
			row.setAfterVesselName("notvesselname");

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}
	}

	@Test
	public void testMatchSlotAndVessel_Negate_SalesAndVessel() {

		final UserFilter filter = new UserFilter("Filter");
		filter.lhsType = FilterSlotType.ANY;

		filter.rhsType = FilterSlotType.BY_ID;
		filter.rhsKey = "slotid";
		filter.negate = true;

		filter.vesselType = FilterVesselType.BY_NAME;
		filter.vesselKey = "vesselname";

		// Matching
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("slotid");
			row.setAfterVesselName("vesselname");

			group.getRows().add(row);
			Assertions.assertFalse(filter.include(group));
		}
		// Not matching
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("notslotid");
			row.setAfterVesselName("vesselname");

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("slotid");
			row.setAfterVesselName("notvesselname");

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
			row.setRhsName("notslotid");
			row.setAfterVesselName("notvesselname");

			group.getRows().add(row);
			Assertions.assertTrue(filter.include(group));
		}
		// Multiple rows
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();

			{
				final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
				row.setRhsName("notslotid");
				row.setAfterVesselName("notvesselname");

				group.getRows().add(row);
			}
			{
				final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
				row.setRhsName("slotid");
				row.setAfterVesselName("vesselname");

				group.getRows().add(row);
			}
			Assertions.assertFalse(filter.include(group));
		}
		// Multiple rows
		{
			final ChangeSetTableGroup group = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
			
			{
				final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
				row.setRhsName("notslotid");
				row.setAfterVesselName("notvesselname");
				
				group.getRows().add(row);
			}
			{
				final ChangeSetTableRow row = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
				row.setRhsName("not2slotid");
				row.setAfterVesselName("vesselname");
				
				group.getRows().add(row);
			}
			Assertions.assertTrue(filter.include(group));
		}
	}
}
