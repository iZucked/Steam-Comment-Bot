/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EmissionsCoveredTable;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Emissions Covered Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EmissionsCoveredTableImpl#getStartYear <em>Start Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EmissionsCoveredTableImpl#getEmissionsCovered <em>Emissions Covered</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EmissionsCoveredTableImpl extends EObjectImpl implements EmissionsCoveredTable {
	/**
	 * The default value of the '{@link #getStartYear() <em>Start Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartYear()
	 * @generated
	 * @ordered
	 */
	protected static final int START_YEAR_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStartYear() <em>Start Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartYear()
	 * @generated
	 * @ordered
	 */
	protected int startYear = START_YEAR_EDEFAULT;

	/**
	 * The default value of the '{@link #getEmissionsCovered() <em>Emissions Covered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmissionsCovered()
	 * @generated
	 * @ordered
	 */
	protected static final int EMISSIONS_COVERED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getEmissionsCovered() <em>Emissions Covered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmissionsCovered()
	 * @generated
	 * @ordered
	 */
	protected int emissionsCovered = EMISSIONS_COVERED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EmissionsCoveredTableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.EMISSIONS_COVERED_TABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getStartYear() {
		return startYear;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartYear(int newStartYear) {
		int oldStartYear = startYear;
		startYear = newStartYear;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.EMISSIONS_COVERED_TABLE__START_YEAR, oldStartYear, startYear));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getEmissionsCovered() {
		return emissionsCovered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEmissionsCovered(int newEmissionsCovered) {
		int oldEmissionsCovered = emissionsCovered;
		emissionsCovered = newEmissionsCovered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.EMISSIONS_COVERED_TABLE__EMISSIONS_COVERED, oldEmissionsCovered, emissionsCovered));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.EMISSIONS_COVERED_TABLE__START_YEAR:
				return getStartYear();
			case CargoPackage.EMISSIONS_COVERED_TABLE__EMISSIONS_COVERED:
				return getEmissionsCovered();
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
			case CargoPackage.EMISSIONS_COVERED_TABLE__START_YEAR:
				setStartYear((Integer)newValue);
				return;
			case CargoPackage.EMISSIONS_COVERED_TABLE__EMISSIONS_COVERED:
				setEmissionsCovered((Integer)newValue);
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
			case CargoPackage.EMISSIONS_COVERED_TABLE__START_YEAR:
				setStartYear(START_YEAR_EDEFAULT);
				return;
			case CargoPackage.EMISSIONS_COVERED_TABLE__EMISSIONS_COVERED:
				setEmissionsCovered(EMISSIONS_COVERED_EDEFAULT);
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
			case CargoPackage.EMISSIONS_COVERED_TABLE__START_YEAR:
				return startYear != START_YEAR_EDEFAULT;
			case CargoPackage.EMISSIONS_COVERED_TABLE__EMISSIONS_COVERED:
				return emissionsCovered != EMISSIONS_COVERED_EDEFAULT;
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
		result.append(" (startYear: ");
		result.append(startYear);
		result.append(", emissionsCovered: ");
		result.append(emissionsCovered);
		result.append(')');
		return result.toString();
	}

} //EmissionsCoveredTableImpl
