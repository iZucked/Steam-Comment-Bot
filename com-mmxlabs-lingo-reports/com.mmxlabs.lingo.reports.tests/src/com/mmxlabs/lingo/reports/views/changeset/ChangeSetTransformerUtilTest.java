/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

public class ChangeSetTransformerUtilTest {

	@Test
	public void testNoChange() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Target

		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, newLoadSlotAllocation, newDischargeSlotAllocation);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation.getSlotVisit(), newLoadSlotAllocation, true, false);
		}

		// Base

		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation.getSlotVisit(), oldLoadSlotAllocation, false,
					false);
		}

		// Verify results
		Assert.assertEquals(1, rows.size());

		final ChangeSetRow row = rows.get(0);
		Assert.assertEquals("load", row.getLhsName());
		Assert.assertEquals("discharge", row.getRhsName());
		Assert.assertEquals("sequence", row.getOriginalVesselName());
		Assert.assertEquals("sequence", row.getNewVesselName());
		Assert.assertNull(row.getLhsWiringLink());
		Assert.assertNull(row.getRhsWiringLink());

		Assert.assertSame(newCargoAllocation, row.getNewGroupProfitAndLoss());
		Assert.assertSame(oldCargoAllocation, row.getOriginalGroupProfitAndLoss());

		Assert.assertSame(newCargoAllocation, row.getNewEventGrouping());
		Assert.assertSame(oldCargoAllocation, row.getOriginalEventGrouping());

		Assert.assertSame(newLoadSlotAllocation, row.getNewLoadAllocation());
		Assert.assertSame(oldLoadSlotAllocation, row.getOriginalLoadAllocation());

		Assert.assertSame(newDischargeSlotAllocation, row.getNewDischargeAllocation());
		Assert.assertSame(oldDischargeSlotAllocation, row.getOriginalDischargeAllocation());
	}

	@Test
	public void testNoChange_OpenSlotAllocation() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Target

		final OpenSlotAllocation newOpenSlotAllocation;
		{
			final LoadSlot loadSlot = createLoadSlot("load");

			newOpenSlotAllocation = createOpenSlotAllocation(loadSlot);
			ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, newOpenSlotAllocation, true);
		}

		// Base

		final OpenSlotAllocation oldOpenSlotAllocation;
		{
			final LoadSlot loadSlot = createLoadSlot("load");
			oldOpenSlotAllocation = createOpenSlotAllocation(loadSlot);
			ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, oldOpenSlotAllocation, false);
		}

		// Verify results
		Assert.assertEquals(1, rows.size());

		final ChangeSetRow row = rows.get(0);
		Assert.assertEquals("load", row.getLhsName());
		Assert.assertNull(row.getRhsName());
		Assert.assertNull(row.getOriginalVesselName());
		Assert.assertNull(row.getNewVesselName());
		Assert.assertNull(row.getLhsWiringLink());
		Assert.assertNull(row.getRhsWiringLink());

		Assert.assertSame(newOpenSlotAllocation, row.getNewOpenLoadAllocation());
		Assert.assertSame(oldOpenSlotAllocation, row.getOriginalOpenLoadAllocation());
	}

	@Test
	public void testSimpleWiringChange() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Target
		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge2");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, newLoadSlotAllocation, newDischargeSlotAllocation);

			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation.getSlotVisit(), newLoadSlotAllocation, true, false);
		}

		// Base
		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation.getSlotVisit(), oldLoadSlotAllocation, false,
					false);
		}

		// Verify results
		Assert.assertEquals(2, rows.size());
		{
			final ChangeSetRow row = rows.get(0);
			Assert.assertEquals("load", row.getLhsName());
			Assert.assertEquals("discharge2", row.getRhsName());
			Assert.assertEquals("sequence", row.getOriginalVesselName());
			Assert.assertEquals("sequence", row.getNewVesselName());
			Assert.assertNull(row.getLhsWiringLink());
			Assert.assertSame(rows.get(1), row.getRhsWiringLink());

			Assert.assertSame(newCargoAllocation, row.getNewGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation, row.getOriginalGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation, row.getNewEventGrouping());
			Assert.assertSame(oldCargoAllocation, row.getOriginalEventGrouping());

			Assert.assertSame(newLoadSlotAllocation, row.getNewLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation, row.getOriginalLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation, row.getNewDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation, row.getOriginalDischargeAllocation());
		}
		// Open "discharge"
		{
			final ChangeSetRow row = rows.get(1);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("discharge", row.getRhsName());
			Assert.assertNull(row.getOriginalVesselName());
			Assert.assertNull(row.getNewVesselName());
			Assert.assertSame(rows.get(0), row.getLhsWiringLink());
			Assert.assertNull(row.getRhsWiringLink());

			Assert.assertNull(row.getNewGroupProfitAndLoss());
			Assert.assertNull(row.getOriginalGroupProfitAndLoss());

			Assert.assertNull(row.getNewEventGrouping());
			Assert.assertNull(row.getOriginalEventGrouping());

			Assert.assertNull(row.getNewLoadAllocation());
			Assert.assertNull(row.getOriginalLoadAllocation());

			Assert.assertNull(row.getNewDischargeAllocation());
			Assert.assertNull(row.getOriginalDischargeAllocation());
		}
	}

	@Test
	public void testWiringChange_SplitIntoOpenSlotAllocations() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Target
		final OpenSlotAllocation newOpenAllocation1;
		final OpenSlotAllocation newOpenAllocation2;
		{
			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			newOpenAllocation1 = createOpenSlotAllocation(loadSlot);
			newOpenAllocation2 = createOpenSlotAllocation(dischargeSlot);
			ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, newOpenAllocation1, true);
			ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, newOpenAllocation2, true);
		}

		// Base
		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation.getSlotVisit(), oldLoadSlotAllocation, false,
					false);
		}

		// Verify results
		Assert.assertEquals(2, rows.size());
		{
			final ChangeSetRow row = rows.get(0);
			Assert.assertEquals("load", row.getLhsName());
			Assert.assertNull(row.getRhsName());
			Assert.assertEquals("sequence", row.getOriginalVesselName());
			Assert.assertNull(row.getNewVesselName());
			Assert.assertNull(row.getLhsWiringLink());
			Assert.assertSame(rows.get(1), row.getRhsWiringLink());

			Assert.assertSame(newOpenAllocation1, row.getNewOpenLoadAllocation());
			Assert.assertSame(oldCargoAllocation, row.getOriginalGroupProfitAndLoss());
			Assert.assertNull(row.getNewEventGrouping());
			Assert.assertSame(oldCargoAllocation, row.getOriginalEventGrouping());

			Assert.assertNull(row.getNewLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation, row.getOriginalLoadAllocation());

			Assert.assertNull(row.getNewDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation, row.getOriginalDischargeAllocation());
		}
		// Open "discharge"
		{
			final ChangeSetRow row = rows.get(1);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("discharge", row.getRhsName());
			Assert.assertNull(row.getOriginalVesselName());
			Assert.assertNull(row.getNewVesselName());
			Assert.assertSame(rows.get(0), row.getLhsWiringLink());
			Assert.assertNull(row.getRhsWiringLink());

			Assert.assertSame(newOpenAllocation2, row.getNewOpenDischargeAllocation());
			Assert.assertNull(row.getOriginalGroupProfitAndLoss());

			Assert.assertNull(row.getNewEventGrouping());
			Assert.assertNull(row.getOriginalEventGrouping());

			Assert.assertNull(row.getNewLoadAllocation());
			Assert.assertNull(row.getOriginalLoadAllocation());

			Assert.assertNull(row.getNewDischargeAllocation());
			Assert.assertNull(row.getOriginalDischargeAllocation());
		}
	}

	@Test
	public void testWiringChange_CombineOpenSlotAllocations() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// New
		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, newLoadSlotAllocation, newDischargeSlotAllocation);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation.getSlotVisit(), newLoadSlotAllocation, true, false);
		}

		// Original
		final OpenSlotAllocation oldOpenAllocation1;
		final OpenSlotAllocation oldOpenAllocation2;
		{
			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			oldOpenAllocation1 = createOpenSlotAllocation(loadSlot);
			oldOpenAllocation2 = createOpenSlotAllocation(dischargeSlot);
			ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, oldOpenAllocation1, false);
			ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, oldOpenAllocation2, false);
		}

		// Verify results
		Assert.assertEquals(1, rows.size());
		{
			final ChangeSetRow row = rows.get(0);
			Assert.assertEquals("load", row.getLhsName());
			Assert.assertEquals("discharge", row.getRhsName());
			Assert.assertNull(row.getOriginalVesselName());
			Assert.assertEquals("sequence", row.getNewVesselName());
			Assert.assertNull(row.getLhsWiringLink());
			Assert.assertNull(row.getRhsWiringLink());

			Assert.assertSame(newCargoAllocation, row.getNewGroupProfitAndLoss());
			// Arg, what about oldOpenAllocation1?
			Assert.assertSame(oldOpenAllocation1, row.getOriginalOpenLoadAllocation());
			Assert.assertSame(oldOpenAllocation2, row.getOriginalOpenDischargeAllocation());
			Assert.assertNull(row.getOriginalGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation, row.getNewEventGrouping());
			Assert.assertNull(row.getOriginalEventGrouping());

			Assert.assertSame(newLoadSlotAllocation, row.getNewLoadAllocation());
			Assert.assertNull(row.getOriginalLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation, row.getNewDischargeAllocation());
			Assert.assertNull(row.getOriginalDischargeAllocation());
		}
	}

	@Test
	public void testSpotDischargeSwapWiringChange_DifferentMarketMarketType() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Target
		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");
			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, newLoadSlotAllocation, newDischargeSlotAllocation);

			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation.getSlotVisit(), newLoadSlotAllocation, true, false);
		}

		// Base
		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createFOBSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation.getSlotVisit(), oldLoadSlotAllocation, false,
					false);
		}

		// Verify results
		Assert.assertEquals(2, rows.size());
		{
			final ChangeSetRow row = rows.get(0);
			Assert.assertEquals("load", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getOriginalVesselName());
			Assert.assertEquals("sequence", row.getNewVesselName());
			Assert.assertNull(row.getLhsWiringLink());
			Assert.assertSame(rows.get(1), row.getRhsWiringLink());

			Assert.assertSame(newCargoAllocation, row.getNewGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation, row.getOriginalGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation, row.getNewEventGrouping());
			Assert.assertSame(oldCargoAllocation, row.getOriginalEventGrouping());

			Assert.assertSame(newLoadSlotAllocation, row.getNewLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation, row.getOriginalLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation, row.getNewDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation, row.getOriginalDischargeAllocation());
		}

		// Different market type caused second row to be created
		{
			final ChangeSetRow row = rows.get(1);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertNull(row.getOriginalVesselName());
			Assert.assertNull(row.getNewVesselName());
			Assert.assertSame(rows.get(0), row.getLhsWiringLink());
			Assert.assertNull(row.getRhsWiringLink());

			Assert.assertNull(row.getNewGroupProfitAndLoss());
			Assert.assertNull(row.getOriginalGroupProfitAndLoss());

			Assert.assertNull(row.getNewEventGrouping());
			Assert.assertNull(row.getOriginalEventGrouping());

			Assert.assertNull(row.getNewLoadAllocation());
			Assert.assertNull(row.getOriginalLoadAllocation());

			Assert.assertNull(row.getNewDischargeAllocation());
			Assert.assertNull(row.getOriginalDischargeAllocation());
		}
	}

	@Test
	public void testSpotDischargeSwapWiringChange_DifferentMarketInstance() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Target
		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");
			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-3", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, newLoadSlotAllocation, newDischargeSlotAllocation);

			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation.getSlotVisit(), newLoadSlotAllocation, true, false);
		}

		// Base
		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation.getSlotVisit(), oldLoadSlotAllocation, false,
					false);
		}

		// Verify results - same market type, same market name, same month, same as no change.
		Assert.assertEquals(1, rows.size());

		final ChangeSetRow row = rows.get(0);
		Assert.assertEquals("load", row.getLhsName());
		Assert.assertEquals("SPOT-2015-11", row.getRhsName());
		Assert.assertEquals("sequence", row.getOriginalVesselName());
		Assert.assertEquals("sequence", row.getNewVesselName());
		Assert.assertNull(row.getLhsWiringLink());
		Assert.assertNull(row.getRhsWiringLink());

		Assert.assertSame(newCargoAllocation, row.getNewGroupProfitAndLoss());
		Assert.assertSame(oldCargoAllocation, row.getOriginalGroupProfitAndLoss());

		Assert.assertSame(newCargoAllocation, row.getNewEventGrouping());
		Assert.assertSame(oldCargoAllocation, row.getOriginalEventGrouping());

		Assert.assertSame(newLoadSlotAllocation, row.getNewLoadAllocation());
		Assert.assertSame(oldLoadSlotAllocation, row.getOriginalLoadAllocation());

		Assert.assertSame(newDischargeSlotAllocation, row.getNewDischargeAllocation());
		Assert.assertSame(oldDischargeSlotAllocation, row.getOriginalDischargeAllocation());
	}

	@Test
	public void testSpotDischargeSwapWiringChange_DifferentMarketInstance_MultipleOptions() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Target - 1
		final CargoAllocation newCargoAllocation1;
		final SlotAllocation newLoadSlotAllocation1;
		final SlotAllocation newDischargeSlotAllocation1;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");
			final LoadSlot loadSlot = createLoadSlot("load1");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-2", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load1", loadSlot, dischargeSlot);

			newLoadSlotAllocation1 = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation1 = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation1 = createCargoAllocation(cargo, newLoadSlotAllocation1, newDischargeSlotAllocation1);

			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation1.getSlotVisit(), newLoadSlotAllocation1, true,
					false);
		}
		// Target - 2
		final CargoAllocation newCargoAllocation2;
		final SlotAllocation newLoadSlotAllocation2;
		final SlotAllocation newDischargeSlotAllocation2;
		{
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final Sequence sequence = createSequence("sequence");
			final LoadSlot loadSlot = createLoadSlot("load2");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load2", loadSlot, dischargeSlot);

			newLoadSlotAllocation2 = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation2 = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation2 = createCargoAllocation(cargo, newLoadSlotAllocation2, newDischargeSlotAllocation2);

			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation2.getSlotVisit(), newLoadSlotAllocation2, true,
					false);
		}

		// Base - 1
		final CargoAllocation oldCargoAllocation1;
		final SlotAllocation oldLoadSlotAllocation1;
		final SlotAllocation oldDischargeSlotAllocation1;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load1");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load1", loadSlot, dischargeSlot);

			oldLoadSlotAllocation1 = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation1 = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation1 = createCargoAllocation(cargo, oldLoadSlotAllocation1, oldDischargeSlotAllocation1);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation1.getSlotVisit(), oldLoadSlotAllocation1, false,
					false);
		}

		// Base - 2
		final CargoAllocation oldCargoAllocation2;
		final SlotAllocation oldLoadSlotAllocation2;
		final SlotAllocation oldDischargeSlotAllocation2;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load2");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");

			final Cargo cargo = createCargo("load2", loadSlot, dischargeSlot);

			oldLoadSlotAllocation2 = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation2 = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation2 = createCargoAllocation(cargo, oldLoadSlotAllocation2, oldDischargeSlotAllocation2);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation2.getSlotVisit(), oldLoadSlotAllocation2, false,
					false);
		}

		// Verify results
		Assert.assertEquals(3, rows.size());

		{
			final ChangeSetRow row = rows.get(0);
			Assert.assertEquals("load1", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getOriginalVesselName());
			Assert.assertEquals("sequence", row.getNewVesselName());
			Assert.assertNull(row.getLhsWiringLink());
			Assert.assertNull(row.getRhsWiringLink());

			Assert.assertSame(newCargoAllocation1, row.getNewGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation1, row.getOriginalGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation1, row.getNewEventGrouping());
			Assert.assertSame(oldCargoAllocation1, row.getOriginalEventGrouping());

			Assert.assertSame(newLoadSlotAllocation1, row.getNewLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation1, row.getOriginalLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation1, row.getNewDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation1, row.getOriginalDischargeAllocation());
		}
		{
			final ChangeSetRow row = rows.get(1);
			Assert.assertEquals("load2", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getOriginalVesselName());
			Assert.assertEquals("sequence", row.getNewVesselName());
			Assert.assertNull(row.getLhsWiringLink());
			Assert.assertSame(rows.get(2), row.getRhsWiringLink());

			Assert.assertSame(newCargoAllocation2, row.getNewGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation2, row.getOriginalGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation2, row.getNewEventGrouping());
			Assert.assertSame(oldCargoAllocation2, row.getOriginalEventGrouping());

			Assert.assertSame(newLoadSlotAllocation2, row.getNewLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation2, row.getOriginalLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation2, row.getNewDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation2, row.getOriginalDischargeAllocation());
		}
		{
			final ChangeSetRow row = rows.get(2);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("discharge", row.getRhsName());
			Assert.assertNull(row.getOriginalVesselName());
			Assert.assertNull(row.getNewVesselName());
			Assert.assertSame(rows.get(1), row.getLhsWiringLink());
			Assert.assertNull(row.getRhsWiringLink());

			Assert.assertNull(row.getNewGroupProfitAndLoss());
			Assert.assertNull(row.getOriginalGroupProfitAndLoss());

			Assert.assertNull(row.getNewEventGrouping());
			Assert.assertNull(row.getOriginalEventGrouping());

			Assert.assertNull(row.getNewLoadAllocation());
			Assert.assertNull(row.getOriginalLoadAllocation());

			Assert.assertNull(row.getNewDischargeAllocation());
			Assert.assertNull(row.getOriginalDischargeAllocation());
		}
	}

	@Test
	public void testSpotDischargeSwapWiringChange_DifferentMarketInstance_WiringSwap() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Target - 1
		final CargoAllocation newCargoAllocation1;
		final SlotAllocation newLoadSlotAllocation1;
		final SlotAllocation newDischargeSlotAllocation1;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");
			final LoadSlot loadSlot = createLoadSlot("load1");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-2", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load1", loadSlot, dischargeSlot);

			newLoadSlotAllocation1 = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation1 = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation1 = createCargoAllocation(cargo, newLoadSlotAllocation1, newDischargeSlotAllocation1);

			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation1.getSlotVisit(), newLoadSlotAllocation1, true,
					false);
		}
		// Target - 2
		final CargoAllocation newCargoAllocation2;
		final SlotAllocation newLoadSlotAllocation2;
		final SlotAllocation newDischargeSlotAllocation2;
		{
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final Sequence sequence = createSequence("sequence");
			final LoadSlot loadSlot = createLoadSlot("load2");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load2", loadSlot, dischargeSlot);

			newLoadSlotAllocation2 = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation2 = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation2 = createCargoAllocation(cargo, newLoadSlotAllocation2, newDischargeSlotAllocation2);

			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation2.getSlotVisit(), newLoadSlotAllocation2, true,
					false);
		}

		// Base - 1
		final CargoAllocation oldCargoAllocation1;
		final SlotAllocation oldLoadSlotAllocation1;
		final SlotAllocation oldDischargeSlotAllocation1;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load1");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load1", loadSlot, dischargeSlot);

			oldLoadSlotAllocation1 = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation1 = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation1 = createCargoAllocation(cargo, oldLoadSlotAllocation1, oldDischargeSlotAllocation1);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation1.getSlotVisit(), oldLoadSlotAllocation1, false,
					false);
		}

		// Base - 2
		final CargoAllocation oldCargoAllocation2;
		final SlotAllocation oldLoadSlotAllocation2;
		final SlotAllocation oldDischargeSlotAllocation2;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load2");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-2", LocalDate.of(2015, 11, 1), dischargeMarket);

			final Cargo cargo = createCargo("load2", loadSlot, dischargeSlot);

			oldLoadSlotAllocation2 = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation2 = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation2 = createCargoAllocation(cargo, oldLoadSlotAllocation2, oldDischargeSlotAllocation2);
			ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation2.getSlotVisit(), oldLoadSlotAllocation2, false,
					false);
		}

		// Verify results
		// Expect two independent rows as spot slots are equivalent

		Assert.assertEquals(2, rows.size());

		{
			final ChangeSetRow row = rows.get(0);
			Assert.assertEquals("load1", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getOriginalVesselName());
			Assert.assertEquals("sequence", row.getNewVesselName());
			Assert.assertNull(row.getLhsWiringLink());
			Assert.assertNull(row.getRhsWiringLink());

			Assert.assertSame(newCargoAllocation1, row.getNewGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation1, row.getOriginalGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation1, row.getNewEventGrouping());
			Assert.assertSame(oldCargoAllocation1, row.getOriginalEventGrouping());

			Assert.assertSame(newLoadSlotAllocation1, row.getNewLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation1, row.getOriginalLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation1, row.getNewDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation1, row.getOriginalDischargeAllocation());
		}
		{
			final ChangeSetRow row = rows.get(1);
			Assert.assertEquals("load2", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getOriginalVesselName());
			Assert.assertEquals("sequence", row.getNewVesselName());
			Assert.assertNull(row.getLhsWiringLink());
			Assert.assertNull(row.getRhsWiringLink());

			Assert.assertSame(newCargoAllocation2, row.getNewGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation2, row.getOriginalGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation2, row.getNewEventGrouping());
			Assert.assertSame(oldCargoAllocation2, row.getOriginalEventGrouping());

			Assert.assertSame(newLoadSlotAllocation2, row.getNewLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation2, row.getOriginalLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation2, row.getNewDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation2, row.getOriginalDischargeAllocation());
		}
	}

	@Test
	public void testNoChange_StartEvent() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// New

		final StartEvent newStartEvent;
		{
			final Sequence sequence = createSequence("sequence");
			newStartEvent = createStartEvent(sequence, "Start sequence");
			ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, newStartEvent, true);
		}

		// Original

		final StartEvent oldStartEvent;
		{
			final Sequence sequence = createSequence("sequence");
			oldStartEvent = createStartEvent(sequence, "Start sequence");
			ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, oldStartEvent, false);
		}

		// Verify results
		Assert.assertEquals(1, rows.size());

		final ChangeSetRow row = rows.get(0);
		Assert.assertEquals("Start sequence", row.getLhsName());
		Assert.assertNull(row.getRhsName());
		Assert.assertEquals("sequence", row.getOriginalVesselName());
		Assert.assertEquals("sequence", row.getNewVesselName());
		Assert.assertNull(row.getLhsWiringLink());
		Assert.assertNull(row.getRhsWiringLink());

		Assert.assertSame(newStartEvent, row.getNewEventGrouping());
		Assert.assertSame(oldStartEvent, row.getOriginalEventGrouping());
		Assert.assertSame(newStartEvent, row.getNewGroupProfitAndLoss());
		Assert.assertSame(oldStartEvent, row.getOriginalGroupProfitAndLoss());
	}

	@Test
	public void testNoChange_DryDockEvent() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// New

		final VesselEventVisit newEvent;
		{
			final Sequence sequence = createSequence("sequence");
			newEvent = createDryDockEvent(sequence, "Drydock-1");
			ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, newEvent, true);
		}

		// Original

		final VesselEventVisit oldEvent;
		{
			final Sequence sequence = createSequence("sequence");
			oldEvent = createDryDockEvent(sequence, "Drydock-1");
			ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, oldEvent, false);
		}

		// Verify results
		Assert.assertEquals(1, rows.size());

		final ChangeSetRow row = rows.get(0);
		Assert.assertEquals("Drydock-1", row.getLhsName());
		Assert.assertNull(row.getRhsName());
		Assert.assertEquals("sequence", row.getOriginalVesselName());
		Assert.assertEquals("sequence", row.getNewVesselName());
		Assert.assertNull(row.getLhsWiringLink());
		Assert.assertNull(row.getRhsWiringLink());

		Assert.assertSame(newEvent, row.getNewEventGrouping());
		Assert.assertSame(oldEvent, row.getOriginalEventGrouping());
		Assert.assertSame(newEvent, row.getNewGroupProfitAndLoss());
		Assert.assertSame(oldEvent, row.getOriginalGroupProfitAndLoss());
	}

	@Test
	public void testNoChange_EndEvent() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// New

		final EndEvent newEndEvent;
		{
			final Sequence sequence = createSequence("sequence");
			newEndEvent = createEndEvent(sequence, "End sequence");
			ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, newEndEvent, true);
		}

		// Original

		final EndEvent oldEndEvent;
		{
			final Sequence sequence = createSequence("sequence");
			oldEndEvent = createEndEvent(sequence, "End sequence");
			ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, oldEndEvent, false);
		}

		// Verify results
		Assert.assertEquals(1, rows.size());

		final ChangeSetRow row = rows.get(0);
		Assert.assertEquals("End sequence", row.getLhsName());
		Assert.assertNull(row.getRhsName());
		Assert.assertEquals("sequence", row.getOriginalVesselName());
		Assert.assertEquals("sequence", row.getNewVesselName());
		Assert.assertNull(row.getLhsWiringLink());
		Assert.assertNull(row.getRhsWiringLink());

		Assert.assertSame(newEndEvent, row.getNewEventGrouping());
		Assert.assertSame(oldEndEvent, row.getOriginalEventGrouping());
		Assert.assertSame(newEndEvent, row.getNewGroupProfitAndLoss());
		Assert.assertSame(oldEndEvent, row.getOriginalGroupProfitAndLoss());
	}

	@Test
	public void testNoChange_CharterIn_EndEvent() {

		// Configure example case

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// New

		final EndEvent newEndEvent;
		{
			final Sequence sequence = createSequence("sequence");
			Mockito.when(sequence.isSetCharterInMarket()).thenReturn(Boolean.TRUE);
			newEndEvent = createEndEvent(sequence, "End sequence");
			ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, newEndEvent, true);
		}

		// Original

		final EndEvent oldEndEvent;
		{
			final Sequence sequence = createSequence("sequence");
			Mockito.when(sequence.isSetCharterInMarket()).thenReturn(Boolean.TRUE);
			oldEndEvent = createEndEvent(sequence, "End sequence");
			ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, oldEndEvent, false);
		}

		// Verify results -- Charter in end events filtered out.
		Assert.assertEquals(0, rows.size());
	}

	private @NonNull VesselEventVisit createDryDockEvent(final Sequence sequence, final String name) {

		VesselEventVisit visit = Mockito.mock(VesselEventVisit.class);
		final DryDockEvent event = Mockito.mock(DryDockEvent.class);
		Mockito.when(visit.name()).thenReturn(name);
		Mockito.when(visit.getSequence()).thenReturn(sequence);

		Mockito.when(visit.getVesselEvent()).thenReturn(event);

		Mockito.when(visit.eClass()).thenReturn(CargoPackage.Literals.DRY_DOCK_EVENT);

		return visit;
	}

	private @NonNull StartEvent createStartEvent(final Sequence sequence, final String name) {

		final StartEvent startEvent = Mockito.mock(StartEvent.class);
		Mockito.when(startEvent.name()).thenReturn(name);
		Mockito.when(startEvent.getSequence()).thenReturn(sequence);

		return startEvent;
	}

	private @NonNull EndEvent createEndEvent(final Sequence sequence, final String name) {

		final EndEvent endEvent = Mockito.mock(EndEvent.class);
		Mockito.when(endEvent.name()).thenReturn(name);
		Mockito.when(endEvent.getSequence()).thenReturn(sequence);

		return endEvent;
	}

	private @NonNull Sequence createSequence(final String name) {

		final Sequence sequence = Mockito.mock(Sequence.class);
		final EList<Event> events = new BasicEList<>();
		Mockito.when(sequence.getName()).thenReturn(name);
		Mockito.when(sequence.getEvents()).thenReturn(events);

		return sequence;
	}

	@NonNull
	private SlotAllocation createSlotAllocation(final Slot slot, final Sequence sequence) {

		final SlotAllocation slotAllocation = Mockito.mock(SlotAllocation.class);
		final SlotVisit slotVisit = Mockito.mock(SlotVisit.class);

		Mockito.when(slotVisit.getSlotAllocation()).thenReturn(slotAllocation);
		Mockito.when(slotAllocation.getSlot()).thenReturn(slot);
		Mockito.when(slotAllocation.getSlotVisit()).thenReturn(slotVisit);

		Mockito.when(slotVisit.getSequence()).thenReturn(sequence);

		return slotAllocation;
	}

	@NonNull
	private OpenSlotAllocation createOpenSlotAllocation(final Slot slot) {

		final OpenSlotAllocation slotAllocation = Mockito.mock(OpenSlotAllocation.class);
		Mockito.when(slotAllocation.getSlot()).thenReturn(slot);

		return slotAllocation;
	}

	@NonNull
	private CargoAllocation createCargoAllocation(final Cargo cargo, final SlotAllocation... slotsAllocations) {
		final CargoAllocation cargoAllocation = Mockito.mock(CargoAllocation.class);
		// Mockito.when(cargoAllocation.getInputCargo()).thenReturn(cargo);

		final EList<SlotAllocation> slotList = new BasicEList<>();
		for (final SlotAllocation slotsAllocation : slotsAllocations) {
			Mockito.when(slotsAllocation.getCargoAllocation()).thenReturn(cargoAllocation);
			slotList.add(slotsAllocation);
		}
		Mockito.when(cargoAllocation.getSlotAllocations()).thenReturn(slotList);

		return cargoAllocation;
	}

	@NonNull
	private SpotMarket createDESSaleSpotMarket(final String name) {
		final DESSalesMarket spotMarket = Mockito.mock(DESSalesMarket.class);

		Mockito.when(spotMarket.eClass()).thenReturn(SpotMarketsPackage.eINSTANCE.getDESSalesMarket());
		Mockito.when(spotMarket.getName()).thenReturn(name);

		return spotMarket;

	}

	@NonNull
	private SpotMarket createFOBSaleSpotMarket(final String name) {
		final FOBSalesMarket spotMarket = Mockito.mock(FOBSalesMarket.class);

		Mockito.when(spotMarket.eClass()).thenReturn(SpotMarketsPackage.eINSTANCE.getFOBSalesMarket());
		Mockito.when(spotMarket.getName()).thenReturn(name);

		return spotMarket;

	}

	@NonNull
	private LoadSlot createLoadSlot(final String name) {
		final LoadSlot slot = Mockito.mock(LoadSlot.class);
		Mockito.when(slot.getName()).thenReturn(name);
		return slot;
	}

	@NonNull
	private DischargeSlot createDischargeSlot(final String name) {
		final DischargeSlot slot = Mockito.mock(DischargeSlot.class);
		Mockito.when(slot.getName()).thenReturn(name);
		return slot;
	}

	@NonNull
	private SpotLoadSlot createSpotLoadSlot(final String name, final LocalDate window, final SpotMarket market) {
		final SpotLoadSlot slot = Mockito.mock(SpotLoadSlot.class);
		Mockito.when(slot.getName()).thenReturn(name);
		Mockito.when(slot.getMarket()).thenReturn(market);
		Mockito.when(slot.getWindowStart()).thenReturn(window);
		return slot;
	}

	@NonNull
	private SpotDischargeSlot createSpotDischargeSlot(final String name, final LocalDate window, final SpotMarket market) {
		final SpotDischargeSlot slot = Mockito.mock(SpotDischargeSlot.class);
		Mockito.when(slot.getName()).thenReturn(name);
		Mockito.when(slot.getMarket()).thenReturn(market);
		Mockito.when(slot.getWindowStart()).thenReturn(window);
		return slot;
	}

	@NonNull
	private Cargo createCargo(final String name, final Slot... slots) {
		final Cargo cargo = Mockito.mock(Cargo.class);
		Mockito.when(cargo.getLoadName()).thenReturn(name);

		final EList<Slot> slotList = new BasicEList<>();
		for (final Slot slot : slots) {
			Mockito.when(slot.getCargo()).thenReturn(cargo);
			slotList.add(slot);
		}
		Mockito.when(cargo.getSlots()).thenReturn(slotList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotList);

		return cargo;
	}
}
