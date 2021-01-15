/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.SalesContractProfile;

import com.mmxlabs.models.lng.cargo.DischargeSlot;

import com.mmxlabs.models.lng.commercial.SalesContract;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sales Contract Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class SalesContractProfileImpl extends ContractProfileImpl<DischargeSlot, SalesContract> implements SalesContractProfile {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SalesContractProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.SALES_CONTRACT_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific type known in this context.
	 * @generated
	 */
	@Override
	public void setContract(SalesContract newContract) {
		super.setContract(newContract);
	}

} //SalesContractProfileImpl
