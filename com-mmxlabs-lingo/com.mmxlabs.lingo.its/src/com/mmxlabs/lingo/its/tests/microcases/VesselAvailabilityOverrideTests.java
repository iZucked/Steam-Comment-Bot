/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
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
		GenericCharterContract bbc = CommercialFactory.eINSTANCE.createGenericCharterContract();
		va.setGenericCharterContract(bbc);
		assertEquals(TEST_ENTITY, va.getCharterOrDelegateEntity().getName());
	}

}