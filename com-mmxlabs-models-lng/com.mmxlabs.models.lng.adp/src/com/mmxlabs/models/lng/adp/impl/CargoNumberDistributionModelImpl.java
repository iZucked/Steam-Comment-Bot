/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Number Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl#getNumberOfCargoes <em>Number Of Cargoes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoNumberDistributionModelImpl extends DistributionModelImpl implements CargoNumberDistributionModel {
	/**
	 * The default value of the '{@link #getNumberOfCargoes() <em>Number Of Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfCargoes()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_CARGOES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfCargoes() <em>Number Of Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfCargoes()
	 * @generated
	 * @ordered
	 */
	protected int numberOfCargoes = NUMBER_OF_CARGOES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoNumberDistributionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.CARGO_NUMBER_DISTRIBUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getNumberOfCargoes() {
		return numberOfCargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNumberOfCargoes(int newNumberOfCargoes) {
		int oldNumberOfCargoes = numberOfCargoes;
		numberOfCargoes = newNumberOfCargoes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES, oldNumberOfCargoes, numberOfCargoes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES:
				return getNumberOfCargoes();
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
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES:
				setNumberOfCargoes((Integer)newValue);
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
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES:
				setNumberOfCargoes(NUMBER_OF_CARGOES_EDEFAULT);
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
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES:
				return numberOfCargoes != NUMBER_OF_CARGOES_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (numberOfCargoes: ");
		result.append(numberOfCargoes);
		result.append(')');
		return result.toString();
	}

} //CargoNumberDistributionModelImpl
