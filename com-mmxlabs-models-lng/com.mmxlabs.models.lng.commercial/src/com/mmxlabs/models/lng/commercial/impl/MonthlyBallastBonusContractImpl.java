/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Monthly Ballast Bonus Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusContractImpl#getHubs <em>Hubs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MonthlyBallastBonusContractImpl extends RuleBasedBallastBonusContractImpl implements MonthlyBallastBonusContract {
	/**
	 * The cached value of the '{@link #getHubs() <em>Hubs</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHubs()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> hubs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonthlyBallastBonusContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getHubs() {
		if (hubs == null) {
			hubs = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.MONTHLY_BALLAST_BONUS_CONTRACT__HUBS);
		}
		return hubs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTRACT__HUBS:
				return getHubs();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTRACT__HUBS:
				getHubs().clear();
				getHubs().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTRACT__HUBS:
				getHubs().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTRACT__HUBS:
				return hubs != null && !hubs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MonthlyBallastBonusContractImpl
