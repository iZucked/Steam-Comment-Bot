/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;

import com.mmxlabs.models.lng.cargo.LoadSlot;

import com.mmxlabs.models.lng.commercial.PurchaseContract;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Purchase Contract Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class PurchaseContractProfileImpl extends ContractProfileImpl<LoadSlot, PurchaseContract> implements PurchaseContractProfile {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PurchaseContractProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.PURCHASE_CONTRACT_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific type known in this context.
	 * @generated
	 */
	@Override
	public void setContract(PurchaseContract newContract) {
		super.setContract(newContract);
	}

} //PurchaseContractProfileImpl
