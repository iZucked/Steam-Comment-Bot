/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelEmissionReference;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Fuel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl#getEquivalenceFactor <em>Equivalence Factor</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl#getEmissionReference <em>Emission Reference</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BaseFuelImpl extends UUIDObjectImpl implements BaseFuel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getEquivalenceFactor() <em>Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEquivalenceFactor()
	 * @generated
	 * @ordered
	 */
	protected static final double EQUIVALENCE_FACTOR_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getEquivalenceFactor() <em>Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEquivalenceFactor()
	 * @generated
	 * @ordered
	 */
	protected double equivalenceFactor = EQUIVALENCE_FACTOR_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEmissionReference() <em>Emission Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmissionReference()
	 * @generated
	 * @ordered
	 */
	protected FuelEmissionReference emissionReference;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseFuelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.BASE_FUEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.BASE_FUEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getEquivalenceFactor() {
		return equivalenceFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEquivalenceFactor(double newEquivalenceFactor) {
		double oldEquivalenceFactor = equivalenceFactor;
		equivalenceFactor = newEquivalenceFactor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.BASE_FUEL__EQUIVALENCE_FACTOR, oldEquivalenceFactor, equivalenceFactor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FuelEmissionReference getEmissionReference() {
		if (emissionReference != null && emissionReference.eIsProxy()) {
			InternalEObject oldEmissionReference = (InternalEObject)emissionReference;
			emissionReference = (FuelEmissionReference)eResolveProxy(oldEmissionReference);
			if (emissionReference != oldEmissionReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.BASE_FUEL__EMISSION_REFERENCE, oldEmissionReference, emissionReference));
			}
		}
		return emissionReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FuelEmissionReference basicGetEmissionReference() {
		return emissionReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEmissionReference(FuelEmissionReference newEmissionReference) {
		FuelEmissionReference oldEmissionReference = emissionReference;
		emissionReference = newEmissionReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.BASE_FUEL__EMISSION_REFERENCE, oldEmissionReference, emissionReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.BASE_FUEL__NAME:
				return getName();
			case FleetPackage.BASE_FUEL__EQUIVALENCE_FACTOR:
				return getEquivalenceFactor();
			case FleetPackage.BASE_FUEL__EMISSION_REFERENCE:
				if (resolve) return getEmissionReference();
				return basicGetEmissionReference();
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
			case FleetPackage.BASE_FUEL__NAME:
				setName((String)newValue);
				return;
			case FleetPackage.BASE_FUEL__EQUIVALENCE_FACTOR:
				setEquivalenceFactor((Double)newValue);
				return;
			case FleetPackage.BASE_FUEL__EMISSION_REFERENCE:
				setEmissionReference((FuelEmissionReference)newValue);
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
			case FleetPackage.BASE_FUEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case FleetPackage.BASE_FUEL__EQUIVALENCE_FACTOR:
				setEquivalenceFactor(EQUIVALENCE_FACTOR_EDEFAULT);
				return;
			case FleetPackage.BASE_FUEL__EMISSION_REFERENCE:
				setEmissionReference((FuelEmissionReference)null);
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
			case FleetPackage.BASE_FUEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case FleetPackage.BASE_FUEL__EQUIVALENCE_FACTOR:
				return equivalenceFactor != EQUIVALENCE_FACTOR_EDEFAULT;
			case FleetPackage.BASE_FUEL__EMISSION_REFERENCE:
				return emissionReference != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case FleetPackage.BASE_FUEL__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return FleetPackage.BASE_FUEL__NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", equivalenceFactor: ");
		result.append(equivalenceFactor);
		result.append(')');
		return result.toString();
	}

} // end of BaseFuelImpl

// finish type fixing
