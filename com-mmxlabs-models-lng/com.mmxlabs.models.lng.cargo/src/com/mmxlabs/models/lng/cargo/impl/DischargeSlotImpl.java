/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Discharge Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DischargeSlotImpl extends SlotImpl implements DischargeSlot {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DischargeSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.DISCHARGE_SLOT;
	}

	@Override
	public Object getUnsetValue(EStructuralFeature feature) {
		if (feature == CargoPackage.Literals.SLOT__DURATION) {
			if (getPort() == null) return 12;
			return getPort().getDischargeDuration();
		}
		return super.getUnsetValue(feature);
	}
	
} // end of DischargeSlotImpl

// finish type fixing
