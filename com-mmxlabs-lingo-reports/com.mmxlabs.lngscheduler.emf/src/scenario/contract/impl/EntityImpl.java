/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.contract.ContractPackage;
import scenario.contract.Entity;
import scenario.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.EntityImpl#getTaxRate <em>Tax Rate</em>}</li>
 *   <li>{@link scenario.contract.impl.EntityImpl#getOwnership <em>Ownership</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EntityImpl extends NamedObjectImpl implements Entity {
	/**
	 * The default value of the '{@link #getTaxRate() <em>Tax Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaxRate()
	 * @generated
	 * @ordered
	 */
	protected static final float TAX_RATE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getTaxRate() <em>Tax Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaxRate()
	 * @generated
	 * @ordered
	 */
	protected float taxRate = TAX_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOwnership() <em>Ownership</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnership()
	 * @generated
	 * @ordered
	 */
	protected static final float OWNERSHIP_EDEFAULT = 1.0F;

	/**
	 * The cached value of the '{@link #getOwnership() <em>Ownership</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnership()
	 * @generated
	 * @ordered
	 */
	protected float ownership = OWNERSHIP_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.ENTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getTaxRate() {
		return taxRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTaxRate(float newTaxRate) {
		float oldTaxRate = taxRate;
		taxRate = newTaxRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.ENTITY__TAX_RATE, oldTaxRate, taxRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getOwnership() {
		return ownership;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOwnership(float newOwnership) {
		float oldOwnership = ownership;
		ownership = newOwnership;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.ENTITY__OWNERSHIP, oldOwnership, ownership));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.ENTITY__TAX_RATE:
				return getTaxRate();
			case ContractPackage.ENTITY__OWNERSHIP:
				return getOwnership();
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
			case ContractPackage.ENTITY__TAX_RATE:
				setTaxRate((Float)newValue);
				return;
			case ContractPackage.ENTITY__OWNERSHIP:
				setOwnership((Float)newValue);
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
			case ContractPackage.ENTITY__TAX_RATE:
				setTaxRate(TAX_RATE_EDEFAULT);
				return;
			case ContractPackage.ENTITY__OWNERSHIP:
				setOwnership(OWNERSHIP_EDEFAULT);
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
			case ContractPackage.ENTITY__TAX_RATE:
				return taxRate != TAX_RATE_EDEFAULT;
			case ContractPackage.ENTITY__OWNERSHIP:
				return ownership != OWNERSHIP_EDEFAULT;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (taxRate: ");
		result.append(taxRate);
		result.append(", ownership: ");
		result.append(ownership);
		result.append(')');
		return result.toString();
	}

} //EntityImpl
