/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetTransformerUtil.MappingModel;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
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
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

public class ChangeSetTransformerUtilTest {

	@Test
	public void testNoChange() {

		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		final MappingModel afterMapping;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, sequence, newLoadSlotAllocation, newDischargeSlotAllocation);
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Collections.singleton(newCargoAllocation));

			// ChangeSetTransformerUtil.createMOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, newLoadSlotAllocation.getSlotVisit(), loadSlot, true, false);
		}

		// Base

		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		final MappingModel beforeMapping;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			// ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, oldLoadSlotAllocation.getSlotVisit(), loadSlot, false, false);

			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Collections.singleton(oldCargoAllocation));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(1, changeSetTableRows.size());

		final ChangeSetTableRow row = changeSetTableRows.get(0);
		Assert.assertEquals("load", row.getLhsName());
		Assert.assertEquals("discharge", row.getRhsName());
		Assert.assertEquals("sequence", row.getBeforeVesselName());
		Assert.assertEquals("sequence", row.getAfterVesselName());

		// TODO: WIP
		// Assert.assertNull(row.getNextLHS());
		// Assert.assertNull(row.getPreviousRHS());

		Assert.assertSame(newCargoAllocation, row.getLhsAfter().getLhsGroupProfitAndLoss());
		Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getLhsGroupProfitAndLoss());

		Assert.assertSame(newCargoAllocation, row.getLhsAfter().getEventGrouping());
		Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getEventGrouping());

		Assert.assertSame(newLoadSlotAllocation, row.getLhsAfter().getLoadAllocation());
		Assert.assertSame(oldLoadSlotAllocation, row.getLhsBefore().getLoadAllocation());

		Assert.assertSame(newDischargeSlotAllocation, row.getRhsAfter().getDischargeAllocation());
		Assert.assertSame(oldDischargeSlotAllocation, row.getRhsBefore().getDischargeAllocation());
	}

	@Test
	public void testNoChange_OpenLoadSlotAllocation() {

		// Target

		final OpenSlotAllocation newOpenSlotAllocation;
		final MappingModel afterMapping;
		{
			final LoadSlot loadSlot = createLoadSlot("load");

			newOpenSlotAllocation = createOpenSlotAllocation(loadSlot);
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Collections.singleton(newOpenSlotAllocation));
		}

		// Base

		final OpenSlotAllocation oldOpenSlotAllocation;
		final MappingModel beforeMapping;

		{
			final LoadSlot loadSlot = createLoadSlot("load");
			oldOpenSlotAllocation = createOpenSlotAllocation(loadSlot);
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Collections.singleton(oldOpenSlotAllocation));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(1, changeSetTableRows.size());

		final ChangeSetTableRow row = changeSetTableRows.get(0);
		Assert.assertEquals("load", row.getLhsName());
		Assert.assertNull(row.getRhsName());
		Assert.assertNull(row.getBeforeVesselName());
		Assert.assertNull(row.getAfterVesselName());
		// Assert.assertNull(row.getNextLHS());
		// Assert.assertNull(row.getPreviousRHS());
		//
		Assert.assertSame(newOpenSlotAllocation, row.getLhsAfter().getOpenLoadAllocation());
		Assert.assertSame(oldOpenSlotAllocation, row.getLhsBefore().getOpenLoadAllocation());
	}

	@Test
	public void testNoChange_OpenDischargeSlotAllocation() {

		// Target

		final OpenSlotAllocation newOpenSlotAllocation;
		final MappingModel afterMapping;
		{
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");

			newOpenSlotAllocation = createOpenSlotAllocation(dischargeSlot);
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Collections.singleton(newOpenSlotAllocation));
		}

		// Base

		final OpenSlotAllocation oldOpenSlotAllocation;
		final MappingModel beforeMapping;

		{
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			oldOpenSlotAllocation = createOpenSlotAllocation(dischargeSlot);
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Collections.singleton(oldOpenSlotAllocation));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(1, changeSetTableRows.size());

		final ChangeSetTableRow row = changeSetTableRows.get(0);
		Assert.assertNull(row.getLhsName());
		Assert.assertEquals("discharge", row.getRhsName());
		Assert.assertNull(row.getBeforeVesselName());
		Assert.assertNull(row.getAfterVesselName());
		// Assert.assertNull(row.getNextLHS());
		// Assert.assertNull(row.getPreviousRHS());
		//
		Assert.assertSame(newOpenSlotAllocation, row.getRhsAfter().getOpenDischargeAllocation());
		Assert.assertSame(oldOpenSlotAllocation, row.getRhsBefore().getOpenDischargeAllocation());
	}

	@Test
	public void testSimpleWiringChange_WithMissingSlots() {

		// Configure example case

		// Target
		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		final MappingModel afterMapping;

		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge2");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, sequence, newLoadSlotAllocation, newDischargeSlotAllocation);

			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Collections.singleton(newCargoAllocation));
		}

		// Base
		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		final MappingModel beforeMapping;

		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Collections.singleton(oldCargoAllocation));
		}
		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(2, changeSetTableRows.size());
		{
			final ChangeSetTableRow row = changeSetTableRows.get(0);
			Assert.assertEquals("load", row.getLhsName());
			Assert.assertEquals("discharge2", row.getRhsName());
			Assert.assertEquals("sequence", row.getBeforeVesselName());
			Assert.assertEquals("sequence", row.getAfterVesselName());
			Assert.assertNull(row.getNextLHS());
			Assert.assertSame(changeSetTableRows.get(1), row.getPreviousRHS());

			Assert.assertSame(newCargoAllocation, row.getLhsAfter().getLhsGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getLhsGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation, row.getLhsAfter().getEventGrouping());
			Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getEventGrouping());

			Assert.assertSame(newLoadSlotAllocation, row.getLhsAfter().getLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation, row.getLhsBefore().getLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation, row.getRhsAfter().getDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation, row.getLhsBefore().getDischargeAllocation());
		}
		// Open "discharge"
		{
			final ChangeSetTableRow row = changeSetTableRows.get(1);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("discharge", row.getRhsName());
			Assert.assertNull(row.getAfterVesselName());
			Assert.assertNull(row.getBeforeVesselName());
			// Assert.assertSame(changeSetTableRows.get(0), row.getNextLHS());
			Assert.assertNull(row.getPreviousRHS());

			Assert.assertSame(oldDischargeSlotAllocation, row.getRhsBefore().getDischargeAllocation());
			// Assert.assertNull(row.getLhsBefore().getLhsGroupProfitAndLoss());
			//
			// Assert.assertNull(row.getLhsAfter().getEventGrouping());
			// Assert.assertNull(row.getLhsBefore().getEventGrouping());
			//
			// Assert.assertNull(row.getLhsAfter().getLoadAllocation());
			// Assert.assertNull(row.getLhsBefore().getLoadAllocation());
			//
			// Assert.assertNull(row.getRhsAfter().getDischargeAllocation());
			// Assert.assertNull(row.getOriginalDischargeAllocation());
		}
	}

	@Test
	public void testWiringChange_SplitIntoOpenSlotAllocations() {

		// Configure example case

		// Target
		final OpenSlotAllocation newOpenAllocation1;
		final OpenSlotAllocation newOpenAllocation2;
		final MappingModel afterMapping;

		{
			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			newOpenAllocation1 = createOpenSlotAllocation(loadSlot);
			newOpenAllocation2 = createOpenSlotAllocation(dischargeSlot);
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newOpenAllocation1, newOpenAllocation2));
		}

		// Base
		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		final MappingModel beforeMapping;

		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation, oldDischargeSlotAllocation);

			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldCargoAllocation));

		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(2, changeSetTableRows.size());
		{
			final ChangeSetTableRow row = changeSetTableRows.get(0);
			Assert.assertEquals("load", row.getLhsName());
			Assert.assertNull(row.getRhsName());
			Assert.assertEquals("sequence", row.getBeforeVesselName());
			Assert.assertNull(row.getAfterVesselName());
			Assert.assertNull(row.getNextLHS());
			Assert.assertSame(changeSetTableRows.get(1), row.getPreviousRHS());

			Assert.assertSame(newOpenAllocation1, row.getLhsAfter().getOpenLoadAllocation());
			Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getLhsGroupProfitAndLoss());
			// Assert.assertNull(row.getLhsAfter().getEventGrouping());
			Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getEventGrouping());

			// Assert.assertNull(row.getLhsAfter().getLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation, row.getLhsBefore().getLoadAllocation());

			// Assert.assertNull(row.getRhsAfter().getDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation, row.getLhsBefore().getDischargeAllocation());
		}
		// Open "discharge"
		{
			final ChangeSetTableRow row = changeSetTableRows.get(1);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("discharge", row.getRhsName());
			Assert.assertNull(row.getBeforeVesselName());
			Assert.assertNull(row.getAfterVesselName());
			Assert.assertSame(changeSetTableRows.get(0), row.getNextLHS());
			Assert.assertNull(row.getPreviousRHS());

			Assert.assertSame(newOpenAllocation2, row.getRhsAfter().getOpenDischargeAllocation());
			// Assert.assertNull(row.getLhsBefore().getLhsGroupProfitAndLoss());
			//
			// Assert.assertNull(row.getLhsAfter().getEventGrouping());
			// Assert.assertNull(row.getLhsBefore().getEventGrouping());
			//
			// Assert.assertNull(row.getLhsAfter().getLoadAllocation());
			// Assert.assertNull(row.getLhsBefore().getLoadAllocation());
			//
			// Assert.assertNull(row.getRhsAfter().getDischargeAllocation());
			// Assert.assertNull(row.getOriginalDischargeAllocation());
		}
	}

	@Test
	public void testWiringChange_CombineOpenSlotAllocations() {

		// Configure example case

		// New
		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		final MappingModel afterMapping;
		{
			final Sequence sequence = createSequence("sequence");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, sequence, newLoadSlotAllocation, newDischargeSlotAllocation);
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newCargoAllocation));

		}

		// Original
		final OpenSlotAllocation oldOpenAllocation1;
		final OpenSlotAllocation oldOpenAllocation2;
		final MappingModel beforeMapping;
		{
			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createDischargeSlot("discharge");
			oldOpenAllocation1 = createOpenSlotAllocation(loadSlot);
			oldOpenAllocation2 = createOpenSlotAllocation(dischargeSlot);
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldOpenAllocation1, oldOpenAllocation2));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(1, changeSetTableRows.size());
		{
			final ChangeSetTableRow row = changeSetTableRows.get(0);
			Assert.assertEquals("load", row.getLhsName());
			Assert.assertEquals("discharge", row.getRhsName());
			Assert.assertNull(row.getBeforeVesselName());
			Assert.assertEquals("sequence", row.getAfterVesselName());
			Assert.assertNull(row.getNextLHS());
			Assert.assertNull(row.getPreviousRHS());

			Assert.assertSame(newCargoAllocation, row.getLhsAfter().getLhsGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation, row.getLhsAfter().getEventGrouping());

			Assert.assertSame(oldOpenAllocation1, row.getLhsBefore().getOpenLoadAllocation());
			Assert.assertSame(oldOpenAllocation2, row.getRhsBefore().getOpenDischargeAllocation());

			Assert.assertSame(oldOpenAllocation1, row.getLhsBefore().getLhsGroupProfitAndLoss());
			Assert.assertSame(oldOpenAllocation2, row.getRhsBefore().getRhsGroupProfitAndLoss());

			Assert.assertNull(row.getLhsBefore().getEventGrouping());

			Assert.assertSame(newLoadSlotAllocation, row.getLhsAfter().getLoadAllocation());
			Assert.assertNull(row.getLhsBefore().getLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation, row.getRhsAfter().getDischargeAllocation());
			Assert.assertNull(row.getLhsBefore().getDischargeAllocation());
		}
	}

	@Test
	public void testSpotFOBSaleRewire_A() {
		//
		// I saw this case in a G scenario. In this case the FOB sale was not hooked up properly. (Split into two lines and no slot icon).

		// Configure example case

		// Target
		final CargoAllocation new_Load2_To_SpotFOB;
		final OpenSlotAllocation new_Load1;
		final OpenSlotAllocation new_RealSlot;

		final SlotAllocation new_Load2_SlotAllocation;
		final SlotAllocation new_SpotFOB_SlotAllocation;
		final MappingModel afterMapping;
		{
			final LoadSlot slot_Load1 = createLoadSlot("load1");
			final LoadSlot slot_Load2 = createLoadSlot("load2");
			final DischargeSlot slot_RealSlot = createDischargeSlot("RealSlot");

			final Sequence sequence = createSequence("FOBSequence");
			final SpotMarket dischargeMarket = createFOBSaleSpotMarket("SPOT");
			final DischargeSlot slot_SpotFOB = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			Mockito.when(slot_SpotFOB.isFOBSale()).thenReturn(true);

			final Cargo cargo = createCargo("load2", slot_Load2, slot_SpotFOB);

			new_Load2_SlotAllocation = createSlotAllocation(slot_Load2, sequence);
			new_SpotFOB_SlotAllocation = createSlotAllocation(slot_SpotFOB, sequence);

			new_Load1 = createOpenSlotAllocation(slot_Load1);
			new_RealSlot = createOpenSlotAllocation(slot_RealSlot);

			new_Load2_To_SpotFOB = createCargoAllocation(cargo, sequence, new_Load2_SlotAllocation, new_SpotFOB_SlotAllocation);

			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(new_Load2_To_SpotFOB, new_Load1, new_RealSlot));

		}

		// Base
		final CargoAllocation old_Load1_To_SpotFOB;
		final CargoAllocation old_Load2_To_RealSlot;

		final SlotAllocation old_Load1_SlotAllocation;
		final SlotAllocation old_Load2_SlotAllocation;
		final SlotAllocation old_SpotFOB_SlotAllocation;
		final SlotAllocation old_RealSlot_SlotAllocation;

		final MappingModel beforeMapping;
		{

			final LoadSlot slot_Load1 = createLoadSlot("load1");
			final LoadSlot slot_Load2 = createLoadSlot("load2");
			final DischargeSlot slot_RealSlot = createDischargeSlot("RealSlot");

			final Sequence sequence_fob = createSequence("FOBSequence");
			final Sequence sequence_cargo = createSequence("Cargo");
			final SpotMarket dischargeMarket = createFOBSaleSpotMarket("SPOT");
			final DischargeSlot slot_SpotFOB = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			Mockito.when(slot_SpotFOB.isFOBSale()).thenReturn(true);

			final Cargo cargo1 = createCargo("load1", slot_Load1, slot_SpotFOB);
			final Cargo cargo2 = createCargo("load2", slot_Load2, slot_RealSlot);

			old_Load1_SlotAllocation = createSlotAllocation(slot_Load1, sequence_fob);
			old_SpotFOB_SlotAllocation = createSlotAllocation(slot_SpotFOB, sequence_fob);

			old_Load2_SlotAllocation = createSlotAllocation(slot_Load2, sequence_cargo);
			old_RealSlot_SlotAllocation = createSlotAllocation(slot_RealSlot, sequence_cargo);

			old_Load1_To_SpotFOB = createCargoAllocation(cargo1, sequence_fob, old_Load1_SlotAllocation, old_SpotFOB_SlotAllocation);
			old_Load2_To_RealSlot = createCargoAllocation(cargo2, sequence_cargo, old_Load2_SlotAllocation, old_RealSlot_SlotAllocation);

			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(old_Load1_To_SpotFOB, old_Load2_To_RealSlot));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(3, changeSetTableRows.size());
		{
			final ChangeSetTableRow row = changeSetTableRows.get(0);
			Assert.assertEquals("load2", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("Cargo", row.getBeforeVesselName());
			Assert.assertEquals("FOBSequence", row.getAfterVesselName());
			// Assert.assertNull(row.getNextLHS());
			// Assert.assertSame(changeSetTableRows.get(1), row.getPreviousRHS());

			// Assert.assertSame(newCargoAllocation, row.getLhsAfter().getLhsGroupProfitAndLoss());
			// Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getLhsGroupProfitAndLoss());
			// Assert.assertSame(newCargoAllocation, row.getLhsAfter().getEventGrouping());
			// Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getEventGrouping());
			//
			// Assert.assertSame(newLoadSlotAllocation, row.getLhsAfter().getLoadAllocation());
			// Assert.assertSame(oldLoadSlotAllocation, row.getLhsBefore().getLoadAllocation());
			//
			// Assert.assertSame(newDischargeSlotAllocation, row.getRhsAfter().getDischargeAllocation());
			// Assert.assertNull(row.getRhsBefore().getDischargeAllocation());
		}
		{
			final ChangeSetTableRow row = changeSetTableRows.get(1);
			Assert.assertEquals("load1", row.getLhsName());
			Assert.assertNull(row.getRhsName());
			Assert.assertEquals("FOBSequence", row.getBeforeVesselName());
			Assert.assertNull(row.getAfterVesselName());
			// Assert.assertNull(row.getNextLHS());
			// Assert.assertSame(changeSetTableRows.get(1), row.getPreviousRHS());

			// Assert.assertSame(newCargoAllocation, row.getLhsAfter().getLhsGroupProfitAndLoss());
			// Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getLhsGroupProfitAndLoss());
			// Assert.assertSame(newCargoAllocation, row.getLhsAfter().getEventGrouping());
			// Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getEventGrouping());
			//
			// Assert.assertSame(newLoadSlotAllocation, row.getLhsAfter().getLoadAllocation());
			// Assert.assertSame(oldLoadSlotAllocation, row.getLhsBefore().getLoadAllocation());
			//
			// Assert.assertSame(newDischargeSlotAllocation, row.getRhsAfter().getDischargeAllocation());
			// Assert.assertNull(row.getRhsBefore().getDischargeAllocation());
		}

		// Different market type caused second row to be created
		{
			final ChangeSetTableRow row = changeSetTableRows.get(2);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("RealSlot", row.getRhsName());
			Assert.assertNull(row.getBeforeVesselName());
			Assert.assertNull(row.getAfterVesselName());
//			Assert.assertSame(changeSetTableRows.get(0), row.getNextLHS());
//			Assert.assertNull(row.getPreviousRHS());
//
//			Assert.assertNull(row.getLhsBefore());
//			Assert.assertNull(row.getLhsAfter().getLhsGroupProfitAndLoss());
//
//			Assert.assertNull(row.getLhsAfter().getEventGrouping());
//
//			Assert.assertNull(row.getLhsAfter().getLoadAllocation());
//
//			Assert.assertNull(row.getRhsAfter().getDischargeAllocation());
			// Assert.assertSame(oldDischargeSlotAllocation, row.getRhsBefore().getDischargeAllocation());
		}
	}

	@Test
	public void testSpotDischargeSwapWiringChange_DifferentMarketMarketType() {
		//
		// FIX ME NEXT;
		// Should not be too different to #testSimpleWiringChange_WithMissingSlots();
		// Why are rows not linked?

		// Configure example case

		// Target
		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		final MappingModel afterMapping;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");
			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, sequence, newLoadSlotAllocation, newDischargeSlotAllocation);
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newCargoAllocation));

		}

		// Base
		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		final MappingModel beforeMapping;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createFOBSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldCargoAllocation));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(2, changeSetTableRows.size());
		{
			final ChangeSetTableRow row = changeSetTableRows.get(0);
			Assert.assertEquals("load", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getBeforeVesselName());
			Assert.assertEquals("sequence", row.getAfterVesselName());
			Assert.assertNull(row.getNextLHS());
			Assert.assertSame(changeSetTableRows.get(1), row.getPreviousRHS());

			Assert.assertSame(newCargoAllocation, row.getLhsAfter().getLhsGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getLhsGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation, row.getLhsAfter().getEventGrouping());
			Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getEventGrouping());

			Assert.assertSame(newLoadSlotAllocation, row.getLhsAfter().getLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation, row.getLhsBefore().getLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation, row.getRhsAfter().getDischargeAllocation());
			Assert.assertNull(row.getRhsBefore().getDischargeAllocation());
		}

		// Different market type caused second row to be created
		{
			final ChangeSetTableRow row = changeSetTableRows.get(1);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertNull(row.getBeforeVesselName());
			Assert.assertNull(row.getAfterVesselName());
			Assert.assertSame(changeSetTableRows.get(0), row.getNextLHS());
			Assert.assertNull(row.getPreviousRHS());

			Assert.assertNull(row.getLhsBefore());
			Assert.assertNull(row.getLhsAfter().getLhsGroupProfitAndLoss());

			Assert.assertNull(row.getLhsAfter().getEventGrouping());

			Assert.assertNull(row.getLhsAfter().getLoadAllocation());

			Assert.assertNull(row.getRhsAfter().getDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation, row.getRhsBefore().getDischargeAllocation());
		}
	}

	@Test
	public void testSpotDischargeSwapWiringChange_DifferentMarketInstance() {

		// Configure example case

		// Target
		final CargoAllocation newCargoAllocation;
		final SlotAllocation newLoadSlotAllocation;
		final SlotAllocation newDischargeSlotAllocation;
		final MappingModel afterMapping;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");
			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-3", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			newLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation = createCargoAllocation(cargo, sequence, newLoadSlotAllocation, newDischargeSlotAllocation);
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newCargoAllocation));
		}

		// Base
		final CargoAllocation oldCargoAllocation;
		final SlotAllocation oldLoadSlotAllocation;
		final SlotAllocation oldDischargeSlotAllocation;
		final MappingModel beforeMapping;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load", loadSlot, dischargeSlot);

			oldLoadSlotAllocation = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation, oldDischargeSlotAllocation);
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldCargoAllocation));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results - same market type, same market name, same month, same as no change.
		Assert.assertEquals(1, changeSetTableRows.size());

		final ChangeSetTableRow row = changeSetTableRows.get(0);
		Assert.assertEquals("load", row.getLhsName());
		Assert.assertEquals("SPOT-2015-11", row.getRhsName());
		Assert.assertEquals("sequence", row.getBeforeVesselName());
		Assert.assertEquals("sequence", row.getAfterVesselName());
		Assert.assertNull(row.getNextLHS());
		Assert.assertNull(row.getPreviousRHS());

		Assert.assertSame(newCargoAllocation, row.getLhsAfter().getLhsGroupProfitAndLoss());
		Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getLhsGroupProfitAndLoss());

		Assert.assertSame(newCargoAllocation, row.getLhsAfter().getEventGrouping());
		Assert.assertSame(oldCargoAllocation, row.getLhsBefore().getEventGrouping());

		Assert.assertSame(newLoadSlotAllocation, row.getLhsAfter().getLoadAllocation());
		Assert.assertSame(oldLoadSlotAllocation, row.getLhsBefore().getLoadAllocation());

		Assert.assertSame(newDischargeSlotAllocation, row.getLhsAfter().getDischargeAllocation());
		Assert.assertSame(oldDischargeSlotAllocation, row.getLhsBefore().getDischargeAllocation());
	}

	@Test
	public void testSpotDischargeSwapWiringChange_DifferentMarketInstance_MultipleOptions() {

		// Configure example case

		// Target - 1
		final CargoAllocation newCargoAllocation1;
		final SlotAllocation newLoadSlotAllocation1;
		final SlotAllocation newDischargeSlotAllocation1;
		final MappingModel afterMapping;
		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");
			final LoadSlot loadSlot = createLoadSlot("load1");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-2", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load1", loadSlot, dischargeSlot);

			newLoadSlotAllocation1 = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation1 = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation1 = createCargoAllocation(cargo, sequence, newLoadSlotAllocation1, newDischargeSlotAllocation1);
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

			newCargoAllocation2 = createCargoAllocation(cargo, sequence, newLoadSlotAllocation2, newDischargeSlotAllocation2);

		}
		afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newCargoAllocation1, newCargoAllocation2));

		// Base - 1
		final CargoAllocation oldCargoAllocation1;
		final SlotAllocation oldLoadSlotAllocation1;
		final SlotAllocation oldDischargeSlotAllocation1;
		final MappingModel beforeMapping;

		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load1");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load1", loadSlot, dischargeSlot);

			oldLoadSlotAllocation1 = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation1 = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation1 = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation1, oldDischargeSlotAllocation1);
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

			oldCargoAllocation2 = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation2, oldDischargeSlotAllocation2);
		}

		beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldCargoAllocation1, oldCargoAllocation2));

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(3, changeSetTableRows.size());

		{
			final ChangeSetTableRow row = changeSetTableRows.get(1);
			Assert.assertEquals("load1", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getBeforeVesselName());
			Assert.assertEquals("sequence", row.getAfterVesselName());
			Assert.assertNull(row.getNextLHS());
			Assert.assertNull(row.getPreviousRHS());

			Assert.assertSame(newCargoAllocation1, row.getLhsAfter().getLhsGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation1, row.getLhsBefore().getLhsGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation1, row.getLhsAfter().getEventGrouping());
			Assert.assertSame(oldCargoAllocation1, row.getLhsBefore().getEventGrouping());

			Assert.assertSame(newLoadSlotAllocation1, row.getLhsAfter().getLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation1, row.getLhsBefore().getLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation1, row.getRhsAfter().getDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation1, row.getRhsBefore().getDischargeAllocation());
		}
		{
			final ChangeSetTableRow row = changeSetTableRows.get(0);
			Assert.assertEquals("load2", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getBeforeVesselName());
			Assert.assertEquals("sequence", row.getAfterVesselName());
			Assert.assertNull(row.getNextLHS());
			Assert.assertSame(changeSetTableRows.get(2), row.getPreviousRHS());

			Assert.assertSame(newCargoAllocation2, row.getLhsAfter().getLhsGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation2, row.getLhsBefore().getLhsGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation2, row.getLhsAfter().getEventGrouping());
			Assert.assertSame(oldCargoAllocation2, row.getLhsBefore().getEventGrouping());

			Assert.assertSame(newLoadSlotAllocation2, row.getLhsAfter().getLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation2, row.getLhsBefore().getLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation2, row.getRhsAfter().getDischargeAllocation());
			Assert.assertNull(row.getRhsBefore().getDischargeAllocation());
		}
		{
			final ChangeSetTableRow row = changeSetTableRows.get(2);
			Assert.assertNull(row.getLhsName());
			Assert.assertEquals("discharge", row.getRhsName());
			Assert.assertNull(row.getBeforeVesselName());
			Assert.assertNull(row.getAfterVesselName());
			Assert.assertSame(changeSetTableRows.get(0), row.getNextLHS());
			Assert.assertNull(row.getPreviousRHS());

			Assert.assertNull(row.getLhsBefore());
			Assert.assertNull(row.getLhsAfter().getLhsGroupProfitAndLoss());

			Assert.assertNull(row.getLhsAfter().getEventGrouping());

			Assert.assertNull(row.getLhsAfter().getLoadAllocation());

			Assert.assertNull(row.getRhsAfter().getDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation2, row.getRhsBefore().getDischargeAllocation());
		}
	}

	@Test
	public void testSpotDischargeSwapWiringChange_DifferentMarketInstance_WiringSwap() {

		// Configure example case

		// Target - 1
		final CargoAllocation newCargoAllocation1;
		final SlotAllocation newLoadSlotAllocation1;
		final SlotAllocation newDischargeSlotAllocation1;
		final MappingModel afterMapping;

		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");
			final LoadSlot loadSlot = createLoadSlot("load1");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-2", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load1", loadSlot, dischargeSlot);

			newLoadSlotAllocation1 = createSlotAllocation(loadSlot, sequence);
			newDischargeSlotAllocation1 = createSlotAllocation(dischargeSlot, sequence);

			newCargoAllocation1 = createCargoAllocation(cargo, sequence, newLoadSlotAllocation1, newDischargeSlotAllocation1);

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

			newCargoAllocation2 = createCargoAllocation(cargo, sequence, newLoadSlotAllocation2, newDischargeSlotAllocation2);

		}
		afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newCargoAllocation1, newCargoAllocation2));

		// Base - 1
		final CargoAllocation oldCargoAllocation1;
		final SlotAllocation oldLoadSlotAllocation1;
		final SlotAllocation oldDischargeSlotAllocation1;
		final MappingModel beforeMapping;

		{
			final Sequence sequence = createSequence("sequence");
			final SpotMarket dischargeMarket = createDESSaleSpotMarket("SPOT");

			final LoadSlot loadSlot = createLoadSlot("load1");
			final DischargeSlot dischargeSlot = createSpotDischargeSlot("SPOT-2015-11-1", LocalDate.of(2015, 11, 1), dischargeMarket);
			final Cargo cargo = createCargo("load1", loadSlot, dischargeSlot);

			oldLoadSlotAllocation1 = createSlotAllocation(loadSlot, sequence);
			oldDischargeSlotAllocation1 = createSlotAllocation(dischargeSlot, sequence);

			oldCargoAllocation1 = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation1, oldDischargeSlotAllocation1);
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

			oldCargoAllocation2 = createCargoAllocation(cargo, sequence, oldLoadSlotAllocation2, oldDischargeSlotAllocation2);
		}
		beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldCargoAllocation1, oldCargoAllocation2));

		// Verify results
		// Expect two independent rows as spot slots are equivalent

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		Assert.assertEquals(2, changeSetTableRows.size());

		{
			final ChangeSetTableRow row = changeSetTableRows.get(1);
			Assert.assertEquals("load1", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getBeforeVesselName());
			Assert.assertEquals("sequence", row.getAfterVesselName());
			Assert.assertNull(row.getNextLHS());
			Assert.assertNull(row.getPreviousRHS());

			Assert.assertSame(newCargoAllocation1, row.getLhsAfter().getLhsGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation1, row.getLhsBefore().getLhsGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation1, row.getLhsAfter().getEventGrouping());
			Assert.assertSame(oldCargoAllocation1, row.getLhsBefore().getEventGrouping());

			Assert.assertSame(newLoadSlotAllocation1, row.getLhsAfter().getLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation1, row.getLhsBefore().getLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation1, row.getRhsAfter().getDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation1, row.getRhsBefore().getDischargeAllocation());
		}
		{
			final ChangeSetTableRow row = changeSetTableRows.get(0);
			Assert.assertEquals("load2", row.getLhsName());
			Assert.assertEquals("SPOT-2015-11", row.getRhsName());
			Assert.assertEquals("sequence", row.getBeforeVesselName());
			Assert.assertEquals("sequence", row.getAfterVesselName());
			Assert.assertNull(row.getNextLHS());
			Assert.assertNull(row.getPreviousRHS());

			Assert.assertSame(newCargoAllocation2, row.getLhsAfter().getLhsGroupProfitAndLoss());
			Assert.assertSame(oldCargoAllocation2, row.getLhsBefore().getLhsGroupProfitAndLoss());
			Assert.assertSame(newCargoAllocation2, row.getLhsAfter().getEventGrouping());
			Assert.assertSame(oldCargoAllocation2, row.getLhsBefore().getEventGrouping());

			Assert.assertSame(newLoadSlotAllocation2, row.getLhsAfter().getLoadAllocation());
			Assert.assertSame(oldLoadSlotAllocation2, row.getLhsBefore().getLoadAllocation());

			Assert.assertSame(newDischargeSlotAllocation2, row.getRhsAfter().getDischargeAllocation());
			Assert.assertSame(oldDischargeSlotAllocation2, row.getRhsBefore().getDischargeAllocation());
		}
	}

	@Test
	public void testNoChange_StartEvent() {

		// Configure example case

		// New

		final StartEvent newStartEvent;
		final MappingModel afterMapping;

		{
			final Sequence sequence = createSequence("sequence");
			newStartEvent = createStartEvent(sequence, "Start sequence");
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newStartEvent));
		}

		// Original

		final StartEvent oldStartEvent;
		final MappingModel beforeMapping;

		{
			final Sequence sequence = createSequence("sequence");
			oldStartEvent = createStartEvent(sequence, "Start sequence");
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldStartEvent));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(1, changeSetTableRows.size());

		final ChangeSetTableRow row = changeSetTableRows.get(0);
		Assert.assertEquals("Start sequence", row.getLhsName());
		Assert.assertNull(row.getRhsName());
		Assert.assertEquals("sequence", row.getBeforeVesselName());
		Assert.assertEquals("sequence", row.getAfterVesselName());
		Assert.assertNull(row.getNextLHS());
		Assert.assertNull(row.getPreviousRHS());

		Assert.assertSame(newStartEvent, row.getLhsAfter().getEventGrouping());
		Assert.assertSame(oldStartEvent, row.getLhsBefore().getEventGrouping());
		Assert.assertSame(newStartEvent, row.getLhsAfter().getLhsGroupProfitAndLoss());
		Assert.assertSame(oldStartEvent, row.getLhsBefore().getLhsGroupProfitAndLoss());
	}

	@Test
	public void testNoChange_DryDockEvent() {

		// Configure example case

		// New

		final VesselEventVisit newEvent;
		final MappingModel afterMapping;

		{
			final Sequence sequence = createSequence("sequence");
			newEvent = createDryDockEvent(sequence, "Drydock-1");
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newEvent));
		}

		// Original

		final VesselEventVisit oldEvent;
		final MappingModel beforeMapping;

		{
			final Sequence sequence = createSequence("sequence");
			oldEvent = createDryDockEvent(sequence, "Drydock-1");
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldEvent));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(1, changeSetTableRows.size());

		final ChangeSetTableRow row = changeSetTableRows.get(0);
		Assert.assertEquals("Drydock-1", row.getLhsName());
		Assert.assertNull(row.getRhsName());
		Assert.assertEquals("sequence", row.getBeforeVesselName());
		Assert.assertEquals("sequence", row.getAfterVesselName());
		Assert.assertNull(row.getNextLHS());
		Assert.assertNull(row.getPreviousRHS());

		Assert.assertSame(newEvent, row.getLhsAfter().getEventGrouping());
		Assert.assertSame(oldEvent, row.getLhsBefore().getEventGrouping());
		Assert.assertSame(newEvent, row.getLhsAfter().getLhsGroupProfitAndLoss());
		Assert.assertSame(oldEvent, row.getLhsBefore().getLhsGroupProfitAndLoss());
	}

	@Test
	public void testNoChange_EndEvent() {

		// Configure example case

		// New

		final EndEvent newEndEvent;
		final MappingModel afterMapping;

		{
			final Sequence sequence = createSequence("sequence");
			newEndEvent = createEndEvent(sequence, "End sequence");
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newEndEvent));
		}

		// Original

		final EndEvent oldEndEvent;
		final MappingModel beforeMapping;

		{
			final Sequence sequence = createSequence("sequence");
			oldEndEvent = createEndEvent(sequence, "End sequence");
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldEndEvent));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results
		Assert.assertEquals(1, changeSetTableRows.size());

		final ChangeSetTableRow row = changeSetTableRows.get(0);
		Assert.assertEquals("End sequence", row.getLhsName());
		Assert.assertNull(row.getRhsName());
		Assert.assertEquals("sequence", row.getBeforeVesselName());
		Assert.assertEquals("sequence", row.getAfterVesselName());
		Assert.assertNull(row.getNextLHS());
		Assert.assertNull(row.getPreviousRHS());

		Assert.assertSame(newEndEvent, row.getLhsAfter().getEventGrouping());
		Assert.assertSame(oldEndEvent, row.getLhsBefore().getEventGrouping());
		Assert.assertSame(newEndEvent, row.getLhsAfter().getLhsGroupProfitAndLoss());
		Assert.assertSame(oldEndEvent, row.getLhsBefore().getLhsGroupProfitAndLoss());
	}

	@Ignore("This test no longer works as defined, generateMappingModel needs the inputs filtered")
	@Test
	public void testNoChange_CharterIn_EndEvent() {

		// Configure example case

		// New

		final EndEvent newEndEvent;
		final MappingModel afterMapping;

		{
			final CharterInMarket charterInMarket = Mockito.mock(CharterInMarket.class);
			Mockito.when(charterInMarket.getName()).thenReturn("charter");

			final Sequence sequence = createSequence("sequence");
			Mockito.when(sequence.isSetCharterInMarket()).thenReturn(Boolean.TRUE);
			Mockito.when(sequence.getCharterInMarket()).thenReturn(charterInMarket);

			newEndEvent = createEndEvent(sequence, "End sequence");
			afterMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(newEndEvent));
		}

		// Original

		final EndEvent oldEndEvent;
		final MappingModel beforeMapping;

		{
			final CharterInMarket charterInMarket = Mockito.mock(CharterInMarket.class);
			Mockito.when(charterInMarket.getName()).thenReturn("charter");

			final Sequence sequence = createSequence("sequence");
			Mockito.when(sequence.isSetCharterInMarket()).thenReturn(Boolean.TRUE);
			Mockito.when(sequence.getCharterInMarket()).thenReturn(charterInMarket);

			oldEndEvent = createEndEvent(sequence, "End sequence");
			beforeMapping = ChangeSetTransformerUtil.generateMappingModel(Lists.newArrayList(oldEndEvent));
		}

		final List<ChangeSetRow> _rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeMapping, afterMapping);
		ChangeSetTransformerUtil.setRowFlags(_rows);
		final List<ChangeSetTableRow> changeSetTableRows = new ChangeSetToTableTransformer().convertRows(_rows);

		// Verify results -- Charter in end events filtered out.
		Assert.assertEquals(0, changeSetTableRows.size());
	}

	private @NonNull VesselEventVisit createDryDockEvent(final Sequence sequence, final String name) {

		final VesselEventVisit visit = Mockito.mock(VesselEventVisit.class);
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
		final SlotAllocationType type = slot instanceof LoadSlot ? SlotAllocationType.PURCHASE : SlotAllocationType.SALE;
		Mockito.when(slotAllocation.getSlotAllocationType()).thenReturn(type);

		return slotAllocation;
	}

	@NonNull
	private OpenSlotAllocation createOpenSlotAllocation(final Slot slot) {

		final OpenSlotAllocation slotAllocation = Mockito.mock(OpenSlotAllocation.class);
		Mockito.when(slotAllocation.getSlot()).thenReturn(slot);

		return slotAllocation;
	}

	@NonNull
	private CargoAllocation createCargoAllocation(final Cargo cargo, final Sequence sequence, final SlotAllocation... slotsAllocations) {
		final CargoAllocation cargoAllocation = Mockito.mock(CargoAllocation.class);
		// Mockito.when(cargoAllocation.getInputCargo()).thenReturn(cargo);

		final EList<SlotAllocation> slotList = new BasicEList<>();
		final EList<Event> eventList = new BasicEList<>();
		for (final SlotAllocation slotsAllocation : slotsAllocations) {
			Mockito.when(slotsAllocation.getCargoAllocation()).thenReturn(cargoAllocation);
			slotList.add(slotsAllocation);
		}
		Mockito.when(cargoAllocation.getSlotAllocations()).thenReturn(slotList);
		Mockito.when(cargoAllocation.getEvents()).thenReturn(eventList);
		Mockito.when(cargoAllocation.getSequence()).thenReturn(sequence);

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
