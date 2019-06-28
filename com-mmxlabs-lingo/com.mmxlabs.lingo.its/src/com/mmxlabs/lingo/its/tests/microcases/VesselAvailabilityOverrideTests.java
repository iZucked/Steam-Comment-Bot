/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.LegalEntity;

public class VesselAvailabilityOverrideTests {

	private static final String TEST_ENTITY = "TestEntity";
	private static final String TEST_VA_ENTITY = "TestVAEntity";
	private static final String VA_REPOSITIONING_FEE = "100000";
	private static final String BBC_REPOSITIONING_FEE = "200000";
	
	@Test
	public void testEntityOnVA() {
		VesselAvailability va = CargoFactory.eINSTANCE.createVesselAvailability();
		LegalEntity le = CommercialFactory.eINSTANCE.createLegalEntity();
		le.setName(TEST_ENTITY);
		va.setEntity(le);
		BallastBonusCharterContract bbc = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		va.setCharterContract(bbc);
		assertEquals(TEST_ENTITY, va.getCharterOrDelegateEntity().getName());
	}
	
	@Test
	public void testEntityOnBBCC() {
		VesselAvailability va = CargoFactory.eINSTANCE.createVesselAvailability();
		LegalEntity le = CommercialFactory.eINSTANCE.createLegalEntity();
		le.setName(TEST_ENTITY);
		BallastBonusCharterContract bbc = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		bbc.setEntity(le);
		va.setCharterContract(bbc);
		assertEquals(TEST_ENTITY, va.getCharterOrDelegateEntity().getName());
	}
	
	@Test
	public void testEntityOnBothVABBCC() {
		VesselAvailability va = CargoFactory.eINSTANCE.createVesselAvailability();
		LegalEntity le = CommercialFactory.eINSTANCE.createLegalEntity();
		le.setName(TEST_ENTITY);
		BallastBonusCharterContract bbc = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		bbc.setEntity(le);
		va.setCharterContract(bbc);
		LegalEntity leva = CommercialFactory.eINSTANCE.createLegalEntity();
		leva.setName(TEST_VA_ENTITY);
		va.setEntity(leva);
		assertEquals(TEST_VA_ENTITY, va.getCharterOrDelegateEntity().getName());
	}

	@Test
	public void testRepositioningFeeOnVA() {
		VesselAvailability va = CargoFactory.eINSTANCE.createVesselAvailability();
		BallastBonusCharterContract bbc = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		va.setCharterContract(bbc);
		va.setRepositioningFee(VA_REPOSITIONING_FEE);
		assertEquals(VA_REPOSITIONING_FEE, va.getCharterOrDelegateRepositioningFee());		
	}

	@Test
	public void testRepositioningFeeOnBBCC() {
		VesselAvailability va = CargoFactory.eINSTANCE.createVesselAvailability();
		BallastBonusCharterContract bbc = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		bbc.setRepositioningFee(BBC_REPOSITIONING_FEE);
		va.setCharterContract(bbc);
		assertEquals(BBC_REPOSITIONING_FEE, va.getCharterOrDelegateRepositioningFee());				
	}
	
	@Test
	public void testRepositioningFeeOnBothVABBCC() {
		VesselAvailability va = CargoFactory.eINSTANCE.createVesselAvailability();
		BallastBonusCharterContract bbc = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		bbc.setRepositioningFee(BBC_REPOSITIONING_FEE);
		va.setCharterContract(bbc);
		va.setRepositioningFee(VA_REPOSITIONING_FEE);
		assertEquals(VA_REPOSITIONING_FEE, va.getCharterOrDelegateRepositioningFee());						
	}
}