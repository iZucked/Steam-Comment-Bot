/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Calendar;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class PeriodExporterTests {
//
//	/**
//	 * Cargo out of scope of period - expect no change
//	 */
//	@Test
//	public void updateOriginalTest_OutOfScopeCargo() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		// Populate initial data
//		final LoadSlot load1 = PeriodTestUtils.createLoadSlot(originalScenario, "load");
//		final DischargeSlot discharge1 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge");
//		final Cargo cargo1 = PeriodTestUtils.createCargo(originalScenario, load1, discharge1);
//
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- remove all data in period scenario.
//		{
//			mapping.registerRemovedOriginal(load1);
//			mapping.registerRemovedOriginal(discharge1);
//			mapping.registerRemovedOriginal(cargo1);
//
//			periodScenario.getCargoModel().getCargoes().clear();
//			periodScenario.getCargoModel().getLoadSlots().clear();
//			periodScenario.getCargoModel().getDischargeSlots().clear();
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//
//		}
//		// Check original scenario state
//		{
//			Assert.assertSame(cargo1, originalScenario.getCargoModel().getCargoes().get(0));
//			Assert.assertSame(load1, originalScenario.getCargoModel().getLoadSlots().get(0));
//			Assert.assertSame(discharge1, originalScenario.getCargoModel().getDischargeSlots().get(0));
//			Assert.assertSame(cargo1, load1.getCargo());
//			Assert.assertSame(cargo1, discharge1.getCargo());
//		}
//	}
//
//	/**
//	 * Cargo in scope of period - expect no change
//	 */
//	@Test
//	public void updateOriginalTest_InScopeCargo() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		// Populate initial data
//		final LoadSlot load1 = PeriodTestUtils.createLoadSlot(originalScenario, "load");
//		final DischargeSlot discharge1 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge");
//		final Cargo cargo1 = PeriodTestUtils.createCargo(originalScenario, load1, discharge1);
//
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- no change.
//		{
//			// ...
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//
//		}
//		// Check original scenario state
//		{
//			Assert.assertSame(cargo1, originalScenario.getCargoModel().getCargoes().get(0));
//			Assert.assertSame(load1, originalScenario.getCargoModel().getLoadSlots().get(0));
//			Assert.assertSame(discharge1, originalScenario.getCargoModel().getDischargeSlots().get(0));
//		}
//	}
//
//	/**
//	 * Cargo assignment change
//	 */
//	@Test
//	public void updateOriginalTest_CargoAssignmentChange() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		// Populate initial data
//		final LoadSlot load1 = PeriodTestUtils.createLoadSlot(originalScenario, "load");
//		final DischargeSlot discharge1 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge");
//		final Cargo cargo1 = PeriodTestUtils.createCargo(originalScenario, load1, discharge1);
//
//		final Vessel vessel1 = PeriodTestUtils.createVessel(originalScenario, "vessel1");
//		final Vessel vessel2 = PeriodTestUtils.createVessel(originalScenario, "vessel2");
//
//		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(originalScenario, vessel1);
//		final VesselAvailability vesselAvailability2 = PeriodTestUtils.createVesselAvailability(originalScenario, vessel2);
//
//		cargo1.setVesselAssignmentType(vesselAvailability1);
//		cargo1.setSequenceHint(1);
//
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- change assignment
//		{
//			final Cargo copyCargo1 = mapping.getCopyFromOriginal(cargo1);
//			assert copyCargo1 != null;
//			final VesselAvailability copyVesselAvailability2 = mapping.getCopyFromOriginal(vesselAvailability2);
//			copyCargo1.setVesselAssignmentType(copyVesselAvailability2);
//			copyCargo1.setSequenceHint(2);
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//
//		}
//
//		// Check original scenario state
//		{
//			Assert.assertSame(vesselAvailability2, cargo1.getVesselAssignmentType());
//			Assert.assertEquals(2, cargo1.getSequenceHint());
//		}
//	}
//
//	/**
//	 * Two cargoes with discharge slot swap
//	 */
//	@Test
//	public void updateOriginalTest_CargoSwapDischarge() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		// Populate initial data
//		final LoadSlot load1 = PeriodTestUtils.createLoadSlot(originalScenario, "load1");
//		final DischargeSlot discharge1 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge1");
//		final Cargo cargo1 = PeriodTestUtils.createCargo(originalScenario, load1, discharge1);
//
//		final LoadSlot load2 = PeriodTestUtils.createLoadSlot(originalScenario, "load2");
//		final DischargeSlot discharge2 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge2");
//		final Cargo cargo2 = PeriodTestUtils.createCargo(originalScenario, load2, discharge2);
//
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- swap discharge slots
//		{
//			// ...
//			final Cargo copyCargo1 = mapping.getCopyFromOriginal(cargo1);
//			final Cargo copyCargo2 = mapping.getCopyFromOriginal(cargo2);
//
//			final DischargeSlot copyDischarge1 = mapping.getCopyFromOriginal(discharge1);
//			assert copyDischarge1 != null;
//
//			final DischargeSlot copyDischarge2 = mapping.getCopyFromOriginal(discharge2);
//			assert copyDischarge2 != null;
//
//			copyDischarge1.setCargo(copyCargo2);
//			copyDischarge2.setCargo(copyCargo1);
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//
//		}
//
//		// Check original scenario state
//		{
//			Assert.assertEquals(2, cargo1.getSlots().size());
//			Assert.assertTrue(cargo1.getSlots().contains(load1));
//			Assert.assertTrue(cargo1.getSlots().contains(discharge2));
//
//			Assert.assertEquals(2, cargo2.getSlots().size());
//			Assert.assertTrue(cargo2.getSlots().contains(load2));
//			Assert.assertTrue(cargo2.getSlots().contains(discharge1));
//		}
//	}
//
//	/**
//	 * One cargo, unpaired
//	 */
//	@Test
//	public void updateOriginalTest_UnpairCargo() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		// Populate initial data
//		final LoadSlot load1 = PeriodTestUtils.createLoadSlot(originalScenario, "load1");
//		final DischargeSlot discharge1 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge1");
//		final Cargo cargo1 = PeriodTestUtils.createCargo(originalScenario, load1, discharge1);
//
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- swap discharge slots
//		{
//			// ...
//			final Cargo copyCargo1 = mapping.getCopyFromOriginal(cargo1);
//
//			final LoadSlot copyLoad1 = mapping.getCopyFromOriginal(load1);
//			final DischargeSlot copyDischarge1 = mapping.getCopyFromOriginal(discharge1);
//
//			assert copyCargo1 != null;
//			assert copyLoad1 != null;
//			assert copyDischarge1 != null;
//
//			copyLoad1.setCargo(null);
//			copyDischarge1.setCargo(null);
//
//			periodScenario.getCargoModel().getCargoes().remove(copyCargo1);
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//		}
//
//		// Check original scenario state
//		{
//
//			Assert.assertSame(load1, originalScenario.getCargoModel().getLoadSlots().get(0));
//			Assert.assertSame(discharge1, originalScenario.getCargoModel().getDischargeSlots().get(0));
//
//			Assert.assertNull(load1.getCargo());
//			Assert.assertNull(discharge1.getCargo());
//
//			Assert.assertTrue(originalScenario.getCargoModel().getCargoes().isEmpty());
//		}
//	}
//
//	/**
//	 * One cargo, unpaired, spot slot deleted
//	 */
//	@Test
//	public void updateOriginalTest_UnpairCargoWithSpotSlot() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		// Populate initial data
//		final LoadSlot load1 = PeriodTestUtils.createLoadSlot(originalScenario, "load1");
//		final SpotDischargeSlot discharge1 = PeriodTestUtils.createSpotDischargeSlot(originalScenario, "spot-discharge1");
//		final Cargo cargo1 = PeriodTestUtils.createCargo(originalScenario, load1, discharge1);
//
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- swap discharge slots
//		{
//			// ...
//			final Cargo copyCargo1 = mapping.getCopyFromOriginal(cargo1);
//
//			final LoadSlot copyLoad1 = mapping.getCopyFromOriginal(load1);
//			final DischargeSlot copyDischarge1 = mapping.getCopyFromOriginal(discharge1);
//
//			assert copyCargo1 != null;
//			assert copyLoad1 != null;
//			assert copyDischarge1 != null;
//
//			copyLoad1.setCargo(null);
//			copyDischarge1.setCargo(null);
//
//			periodScenario.getCargoModel().getCargoes().remove(copyCargo1);
//			periodScenario.getCargoModel().getDischargeSlots().remove(copyDischarge1);
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//		}
//
//		// Check original scenario state
//		{
//
//			Assert.assertSame(load1, originalScenario.getCargoModel().getLoadSlots().get(0));
//			Assert.assertNull(load1.getCargo());
//
//			Assert.assertTrue(originalScenario.getCargoModel().getCargoes().isEmpty());
//			Assert.assertTrue(originalScenario.getCargoModel().getDischargeSlots().isEmpty());
//		}
//	}
//
//	/**
//	 * One cargo, created in period opt
//	 */
//	@Test
//	public void updateOriginalTest_PairCargo() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		// Populate initial data
//		final LoadSlot load1 = PeriodTestUtils.createLoadSlot(originalScenario, "load1");
//		final DischargeSlot discharge1 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge1");
//
//		final Vessel vessel1 = PeriodTestUtils.createVessel(originalScenario, "vessel1");
//		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(originalScenario, vessel1);
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- swap discharge slots
//		{
//			// ...
//
//			final LoadSlot copyLoad1 = mapping.getCopyFromOriginal(load1);
//			final DischargeSlot copyDischarge1 = mapping.getCopyFromOriginal(discharge1);
//
//			// Create our new wiring
//			final Cargo copyCargo = PeriodTestUtils.createCargo(periodScenario, copyLoad1, copyDischarge1);
//
//			final VesselAvailability copyVesselAvailability1 = mapping.getCopyFromOriginal(vesselAvailability1);
//			copyCargo.setVesselAssignmentType(copyVesselAvailability1);
//			copyCargo.setSequenceHint(2);
//
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//
//		}
//
//		// Check original scenario state
//		{
//
//			Assert.assertFalse(originalScenario.getCargoModel().getCargoes().isEmpty());
//			final Cargo newCargo = originalScenario.getCargoModel().getCargoes().get(0);
//
//			Assert.assertSame(vesselAvailability1, newCargo.getVesselAssignmentType());
//
//			Assert.assertSame(load1, originalScenario.getCargoModel().getLoadSlots().get(0));
//			Assert.assertSame(discharge1, originalScenario.getCargoModel().getDischargeSlots().get(0));
//
//			Assert.assertSame(newCargo, load1.getCargo());
//			Assert.assertSame(newCargo, discharge1.getCargo());
//		}
//	}
//
//	/**
//	 * One cargo, created in period opt with spot slot generated
//	 */
//	@Test
//	public void updateOriginalTest_PairCargoCreateSpotSlot() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		Port originalPort1 = PeriodTestUtils.createPort(originalScenario, "port1");
//		Port originalPort2 = PeriodTestUtils.createPort(originalScenario, "port2");
//
//		// Populate initial data
//		final LoadSlot load1 = PeriodTestUtils.createLoadSlot(originalScenario, "load1");
//		load1.setPort(originalPort1);
//
//		final Vessel vessel1 = PeriodTestUtils.createVessel(originalScenario, "vessel1");
//		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(originalScenario, vessel1);
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- swap discharge slots
//		{
//			// ...
//
//			final LoadSlot copyLoad1 = mapping.getCopyFromOriginal(load1);
//			final DischargeSlot copySpotDischarge1 = PeriodTestUtils.createSpotDischargeSlot(periodScenario, "spot-discharge1");
//			copySpotDischarge1.setPort(mapping.getCopyFromOriginal(originalPort2));
//			copySpotDischarge1.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 1));
//
//			// Create our new wiring
//			final Cargo copyCargo = PeriodTestUtils.createCargo(periodScenario, copyLoad1, copySpotDischarge1);
//
//			final VesselAvailability copyVesselAvailability1 = mapping.getCopyFromOriginal(vesselAvailability1);
//			copyCargo.setVesselAssignmentType(copyVesselAvailability1);
//			copyCargo.setSequenceHint(2);
//
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//
//		}
//
//		// Check original scenario state
//		{
//
//			Assert.assertFalse(originalScenario.getCargoModel().getCargoes().isEmpty());
//			final Cargo newCargo = originalScenario.getCargoModel().getCargoes().get(0);
//
//			Assert.assertFalse(originalScenario.getCargoModel().getDischargeSlots().isEmpty());
//			final DischargeSlot newDischarge = originalScenario.getCargoModel().getDischargeSlots().get(0);
//			Assert.assertTrue(newDischarge instanceof SpotSlot);
//
//			Assert.assertSame(vesselAvailability1, newCargo.getVesselAssignmentType());
//
//			Assert.assertSame(load1, originalScenario.getCargoModel().getLoadSlots().get(0));
//
//			Assert.assertSame(newCargo, load1.getCargo());
//			Assert.assertSame(newCargo, newDischarge.getCargo());
//
//		}
//	}
//
//	/**
//	 * One cargoes with discharge slot swap. Load slot is spot - expect port,date etc attributes to change.
//	 */
//	@Test
//	public void updateOriginalTest_CargoSwapDischargeUpdateSpotDetails() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		final Port port1 = PeriodTestUtils.createPort(originalScenario, "port1");
//		final Port port2 = PeriodTestUtils.createPort(originalScenario, "port1");
//
//		// Populate initial data
//		final SpotLoadSlot load1 = PeriodTestUtils.createSpotLoadSlot(originalScenario, "load1");
//		load1.setDESPurchase(true);
//		load1.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 8));
//		load1.setPort(port1);
//
//		final DischargeSlot discharge1 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge1");
//		discharge1.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 8));
//		discharge1.setPort(port1);
//
//		final Cargo cargo1 = PeriodTestUtils.createCargo(originalScenario, load1, discharge1);
//
//		final DischargeSlot discharge2 = PeriodTestUtils.createDischargeSlot(originalScenario, "discharge2");
//		discharge2.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 8));
//		discharge2.setPort(port2);
//
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- swap discharge slots
//		{
//			// ...
//			final Cargo copyCargo1 = mapping.getCopyFromOriginal(cargo1);
//
//			final LoadSlot copyLoad1 = mapping.getCopyFromOriginal(load1);
//			final DischargeSlot copyDischarge1 = mapping.getCopyFromOriginal(discharge1);
//			final DischargeSlot copyDischarge2 = mapping.getCopyFromOriginal(discharge2);
//
//			assert copyCargo1 != null;
//			assert copyLoad1 != null;
//			assert copyDischarge1 != null;
//			assert copyDischarge2 != null;
//
//			copyLoad1.setPort(mapping.getCopyFromOriginal(port2));
//			copyDischarge1.setCargo(null);
//			copyDischarge2.setCargo(copyCargo1);
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//
//		}
//
//		// Check original scenario state
//		{
//			Assert.assertEquals(2, cargo1.getSlots().size());
//			Assert.assertTrue(cargo1.getSlots().contains(load1));
//			Assert.assertTrue(cargo1.getSlots().contains(discharge2));
//			Assert.assertSame(port2, load1.getPort());
//		}
//	}
//
//	/**
//	 * Charter out assignment change
//	 */
//	@Test
//	public void updateOriginalTest_CharterOutEventAssignmentChange() {
//
//		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();
//
//		// Create an original scenario, a period scenario and mapping data.
//		final LNGScenarioModel originalScenario = PeriodTestUtils.createBasicScenario();
//		LNGScenarioModel periodScenario;
//
//		// Populate initial data
//		final CharterOutEvent event1 = PeriodTestUtils.createCharterOutEvent(originalScenario, "charter1");
//
//		final Vessel vessel1 = PeriodTestUtils.createVessel(originalScenario, "vessel1");
//		final Vessel vessel2 = PeriodTestUtils.createVessel(originalScenario, "vessel2");
//
//		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(originalScenario, vessel1);
//		final VesselAvailability vesselAvailability2 = PeriodTestUtils.createVesselAvailability(originalScenario, vessel2);
//
//		event1.setVesselAssignmentType(vesselAvailability1);
//		event1.setSequenceHint(1);
//
//		// Create period copy
//		{
//			final Copier copier = new Copier();
//			periodScenario = (LNGScenarioModel) copier.copy(originalScenario);
//			copier.copyReferences();
//
//			mapping.createMappings(copier);
//		}
//
//		// Perform changes -- change assignment
//		{
//			final CharterOutEvent copyEvent1 = mapping.getCopyFromOriginal(event1);
//			assert copyEvent1 != null;
//
//			final VesselAvailability copyVesselAvailability2 = mapping.getCopyFromOriginal(vesselAvailability2);
//			assert copyVesselAvailability2 != null;
//
//			copyEvent1.setVesselAssignmentType(copyVesselAvailability2);
//			copyEvent1.setSequenceHint(2);
//		}
//
//		// Execute update
//		{
//			executeUpdate(mapping, originalScenario, periodScenario);
//
//		}
//
//		// Check original scenario state
//		{
//			Assert.assertSame(vesselAvailability2, event1.getVesselAssignmentType());
//			Assert.assertEquals(2, event1.getSequenceHint());
//		}
//	}
//
//	protected void executeUpdate(final IScenarioEntityMapping mapping, final LNGScenarioModel originalScenario, final LNGScenarioModel periodScenario) {
//		final EditingDomain editingDomain = PeriodTestUtils.createEditingDomain(originalScenario);
//
//		final PeriodExporter exporter = new PeriodExporter();
//
//		final Command cmd = exporter.updateOriginal(editingDomain, originalScenario, periodScenario, mapping);
//		Assert.assertTrue(cmd.canExecute());
//		editingDomain.getCommandStack().execute(cmd);
//	}
}
