/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.fleet.impl;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelEmissionReference;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fuel Emission Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FuelEmissionReferenceImpl#getIsoReference <em>Iso Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FuelEmissionReferenceImpl#getCf <em>Cf</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FuelEmissionReferenceImpl extends NamedObjectImpl implements FuelEmissionReference {
	/**
	 * The default value of the '{@link #getIsoReference() <em>Iso Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsoReference()
	 * @generated
	 * @ordered
	 */
	protected static final String ISO_REFERENCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIsoReference() <em>Iso Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsoReference()
	 * @generated
	 * @ordered
	 */
	protected String isoReference = ISO_REFERENCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCf() <em>Cf</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCf()
	 * @generated
	 * @ordered
	 */
	protected static final double CF_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCf() <em>Cf</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCf()
	 * @generated
	 * @ordered
	 */
	protected double cf = CF_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelEmissionReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.FUEL_EMISSION_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getIsoReference() {
		return isoReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsoReference(String newIsoReference) {
		String oldIsoReference = isoReference;
		isoReference = newIsoReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.FUEL_EMISSION_REFERENCE__ISO_REFERENCE, oldIsoReference, isoReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCf() {
		return cf;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCf(double newCf) {
		double oldCf = cf;
		cf = newCf;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.FUEL_EMISSION_REFERENCE__CF, oldCf, cf));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.FUEL_EMISSION_REFERENCE__ISO_REFERENCE:
				return getIsoReference();
			case FleetPackage.FUEL_EMISSION_REFERENCE__CF:
				return getCf();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FleetPackage.FUEL_EMISSION_REFERENCE__ISO_REFERENCE:
				setIsoReference((String)newValue);
				return;
			case FleetPackage.FUEL_EMISSION_REFERENCE__CF:
				setCf((Double)newValue);
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
			case FleetPackage.FUEL_EMISSION_REFERENCE__ISO_REFERENCE:
				setIsoReference(ISO_REFERENCE_EDEFAULT);
				return;
			case FleetPackage.FUEL_EMISSION_REFERENCE__CF:
				setCf(CF_EDEFAULT);
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
			case FleetPackage.FUEL_EMISSION_REFERENCE__ISO_REFERENCE:
				return ISO_REFERENCE_EDEFAULT == null ? isoReference != null : !ISO_REFERENCE_EDEFAULT.equals(isoReference);
			case FleetPackage.FUEL_EMISSION_REFERENCE__CF:
				return cf != CF_EDEFAULT;
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
		result.append(" (isoReference: ");
		result.append(isoReference);
		result.append(", cf: ");
		result.append(cf);
		result.append(')');
		return result.toString();
	}

} //FuelEmissionReferenceImpl
