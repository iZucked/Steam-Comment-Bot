/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.LiNGOTestDataProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.DatePeriodPrior;
import com.mmxlabs.models.lng.nominations.NominationsFactory;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.Side;
import com.mmxlabs.models.lng.nominations.SlotNomination;
import com.mmxlabs.models.lng.nominations.SlotNominationSpec;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class NominationsTests { 
 
	@Test
	public void testGeneration() {
		final LNGScenarioModel sm = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		final NominationsModel nm = NominationsFactory.eINSTANCE.createNominationsModel();
		sm.setNominationsModel(nm);
		
		//Add a contract.
		final CommercialModel commercialModel = CommercialFactory.eINSTANCE.createCommercialModel();
		final LNGReferenceModel rm = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		sm.setReferenceModel(rm);
		sm.getReferenceModel().setCommercialModel(commercialModel);
		final PurchaseContract pc = CommercialFactory.eINSTANCE.createPurchaseContract();
		pc.setName("PurchaseContract1");
		commercialModel.getPurchaseContracts().add(pc);
		
		//Add a slot.
		final CargoModel cm = CargoFactory.eINSTANCE.createCargoModel();
		sm.setCargoModel(cm);
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cm.getCargoes().add(cargo);
		final LoadSlot ls = CargoFactory.eINSTANCE.createLoadSlot();
		ls.setName("LS1");
		ls.setWindowStart(LocalDate.of(2019, 5, 21));
		ls.setContract(pc);
		cm.getLoadSlots().add(ls);
		cargo.getSlots().add(ls);
		
		//Add specs:
		//Test basic days due date + alert date.
		final SlotNominationSpec sns = NominationsFactory.eINSTANCE.createSlotNominationSpec();
		sns.setType("Buy window");
		sns.setRefererId("PurchaseContract1");
		sns.setSize(10);
		sns.setSizeUnits(DatePeriodPrior.DAYS_PRIOR);
		sns.setSide(Side.BUY);
		sns.setAlertSize(20);
		sns.setAlertSizeUnits(DatePeriodPrior.DAYS_PRIOR);
		sns.setRemark("Remarks about spec1.");
		nm.getNominationSpecs().add(sns);
		
		//Test months due date + alert date.
		final SlotNominationSpec sns2 = NominationsFactory.eINSTANCE.createSlotNominationSpec();
		sns2.setType("Load volume");
		sns2.setRefererId("PurchaseContract1");
		sns2.setSize(1);
		sns2.setSizeUnits(DatePeriodPrior.MONTHS_PRIOR);
		sns2.setSide(Side.BUY);
		sns2.setAlertSize(2);
		sns2.setAlertSizeUnits(DatePeriodPrior.MONTHS_PRIOR);
		sns2.setRemark("Remarks about spec 2");
		nm.getNominationSpecs().add(sns2);
		
		//Test months + day of month due date + months alert date.
		final SlotNominationSpec sns3 = NominationsFactory.eINSTANCE.createSlotNominationSpec();
		sns3.setType("Vessel");
		sns3.setRefererId("PurchaseContract1");
		sns3.setDayOfMonth(3);
		sns3.setSize(1);
		sns3.setSizeUnits(DatePeriodPrior.MONTHS_PRIOR);
		sns3.setSide(Side.BUY);
		sns3.setAlertSize(2);
		sns3.setAlertSizeUnits(DatePeriodPrior.MONTHS_PRIOR);
		sns3.setRemark("Remarks about spec 3");
		nm.getNominationSpecs().add(sns3);			

		//Test sell.
		final SlotNominationSpec sns4 = NominationsFactory.eINSTANCE.createSlotNominationSpec();
		sns4.setType("Sell window");
		sns4.setRefererId("SalesContract1");
		sns4.setSize(10);
		sns4.setSizeUnits(DatePeriodPrior.DAYS_PRIOR);
		sns4.setSide(Side.SELL);
		sns4.setAlertSize(20);
		sns4.setAlertSizeUnits(DatePeriodPrior.DAYS_PRIOR);
		sns4.setRemark("Remarks about spec 4.");
		nm.getNominationSpecs().add(sns4);
		
		//Test no alert size.
		final SlotNominationSpec sns5 = NominationsFactory.eINSTANCE.createSlotNominationSpec();
		sns4.setType("Sell window");
		sns4.setRefererId("SalesContract1");
		sns4.setSize(10);
		sns4.setSizeUnits(DatePeriodPrior.DAYS_PRIOR);
		sns4.setSide(Side.SELL);
		sns4.setRemark("Remarks about spec 5.");
		nm.getNominationSpecs().add(sns4);
		
		//Generate.
		final List<AbstractNomination> nominations = NominationsModelUtils.generateNominationsFromSpecs(sm.getNominationsModel());
		for (final AbstractNomination n : nominations) {
			final String specUuid = n.getSpecUuid();
			if (specUuid.equals(sns.getUuid())) {
				checkNomination(sm, n, Side.BUY, "Buy window",11,5,2019,1,5,2019, sns.getRemark());
			}
			else if (specUuid.equals(sns2.getUuid())) {
				checkNomination(sm, n, Side.BUY, "Load volume",21,4,2019,21,3,2019, sns2.getRemark());
			}
			else if (specUuid.equals(sns3.getUuid())) {
				checkNomination(sm, n, Side.BUY, "Vessel",3,4,2019,21,3,2019, sns3.getRemark());				
			}
			else if (specUuid.equals(sns4.getUuid())) {
				checkNomination(sm, n, Side.SELL, "Sell window",11,5,2019,1,5,2019, sns4.getRemark());				
			}
			else if (specUuid.equals(sns5.getUuid())) {
				checkNomination(sm, n, Side.SELL, "Sell window",11,5,2019,11,5,2019, sns5.getRemark());				
			}
		}
	}
	
	private void checkNomination(LNGScenarioModel sm, AbstractNomination n, Side side, String nominationType, int d, int m, int y, int ad, int am, int ay, String remark) {
		assertEquals(side, n.getSide());
		
		assertEquals(nominationType, n.getType());
		
		final LocalDate dueDate = NominationsModelUtils.getDueDate(sm, n);
		assertNotNull(dueDate);
		assertEquals(d, dueDate.getDayOfMonth());
		assertEquals(m, dueDate.getMonthValue());
		assertEquals(y, dueDate.getYear());
		
		final LocalDate alertDate = NominationsModelUtils.getAlertDate(sm, n);
		assertNotNull(alertDate);
		assertEquals(ad, alertDate.getDayOfMonth());
		assertEquals(am, alertDate.getMonthValue());
		assertEquals(ay, alertDate.getYear());
		
		assertEquals(remark, n.getRemark());
	}
}
