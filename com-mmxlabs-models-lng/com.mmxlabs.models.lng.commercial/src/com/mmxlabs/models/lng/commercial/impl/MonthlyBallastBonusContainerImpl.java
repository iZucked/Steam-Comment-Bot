/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Monthly Ballast Bonus Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusContainerImpl#getHubs <em>Hubs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusContainerImpl#getTerms <em>Terms</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MonthlyBallastBonusContainerImpl extends EObjectImpl implements MonthlyBallastBonusContainer {
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
	 * The cached value of the '{@link #getTerms() <em>Terms</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTerms()
	 * @generated
	 * @ordered
	 */
	protected EList<MonthlyBallastBonusTerm> terms;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonthlyBallastBonusContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getHubs() {
		if (hubs == null) {
			hubs = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__HUBS);
		}
		return hubs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MonthlyBallastBonusTerm> getTerms() {
		if (terms == null) {
			terms = new EObjectContainmentEList<MonthlyBallastBonusTerm>(MonthlyBallastBonusTerm.class, this, CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS);
		}
		return terms;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS:
				return ((InternalEList<?>)getTerms()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__HUBS:
				return getHubs();
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS:
				return getTerms();
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
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__HUBS:
				getHubs().clear();
				getHubs().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS:
				getTerms().clear();
				getTerms().addAll((Collection<? extends MonthlyBallastBonusTerm>)newValue);
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
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__HUBS:
				getHubs().clear();
				return;
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS:
				getTerms().clear();
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
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__HUBS:
				return hubs != null && !hubs.isEmpty();
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS:
				return terms != null && !terms.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MonthlyBallastBonusContainerImpl
