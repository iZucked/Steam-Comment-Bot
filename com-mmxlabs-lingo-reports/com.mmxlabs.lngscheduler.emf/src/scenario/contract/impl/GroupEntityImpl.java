/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.contract.ContractPackage;
import scenario.contract.GroupEntity;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Group Entity</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.GroupEntityImpl#getTaxRate <em>Tax Rate</em>}</li>
 *   <li>{@link scenario.contract.impl.GroupEntityImpl#getOwnership <em>Ownership</em>}</li>
 *   <li>{@link scenario.contract.impl.GroupEntityImpl#getTransferOffset <em>Transfer Offset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GroupEntityImpl extends EntityImpl implements GroupEntity {
	/**
	 * The default value of the '{@link #getTaxRate() <em>Tax Rate</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTaxRate()
	 * @generated
	 * @ordered
	 */
	protected static final Double TAX_RATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTaxRate() <em>Tax Rate</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTaxRate()
	 * @generated
	 * @ordered
	 */
	protected Double taxRate = TAX_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOwnership() <em>Ownership</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getOwnership()
	 * @generated
	 * @ordered
	 */
	protected static final Double OWNERSHIP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOwnership() <em>Ownership</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getOwnership()
	 * @generated
	 * @ordered
	 */
	protected Double ownership = OWNERSHIP_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransferOffset() <em>Transfer Offset</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTransferOffset()
	 * @generated
	 * @ordered
	 */
	protected static final float TRANSFER_OFFSET_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getTransferOffset() <em>Transfer Offset</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTransferOffset()
	 * @generated
	 * @ordered
	 */
	protected float transferOffset = TRANSFER_OFFSET_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupEntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.GROUP_ENTITY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Double getTaxRate() {
		return taxRate;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTaxRate(Double newTaxRate) {
		Double oldTaxRate = taxRate;
		taxRate = newTaxRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.GROUP_ENTITY__TAX_RATE, oldTaxRate, taxRate));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Double getOwnership() {
		return ownership;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOwnership(Double newOwnership) {
		Double oldOwnership = ownership;
		ownership = newOwnership;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.GROUP_ENTITY__OWNERSHIP, oldOwnership, ownership));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getTransferOffset() {
		return transferOffset;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransferOffset(float newTransferOffset) {
		float oldTransferOffset = transferOffset;
		transferOffset = newTransferOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.GROUP_ENTITY__TRANSFER_OFFSET, oldTransferOffset, transferOffset));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.GROUP_ENTITY__TAX_RATE:
				return getTaxRate();
			case ContractPackage.GROUP_ENTITY__OWNERSHIP:
				return getOwnership();
			case ContractPackage.GROUP_ENTITY__TRANSFER_OFFSET:
				return getTransferOffset();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ContractPackage.GROUP_ENTITY__TAX_RATE:
				setTaxRate((Double)newValue);
				return;
			case ContractPackage.GROUP_ENTITY__OWNERSHIP:
				setOwnership((Double)newValue);
				return;
			case ContractPackage.GROUP_ENTITY__TRANSFER_OFFSET:
				setTransferOffset((Float)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ContractPackage.GROUP_ENTITY__TAX_RATE:
				setTaxRate(TAX_RATE_EDEFAULT);
				return;
			case ContractPackage.GROUP_ENTITY__OWNERSHIP:
				setOwnership(OWNERSHIP_EDEFAULT);
				return;
			case ContractPackage.GROUP_ENTITY__TRANSFER_OFFSET:
				setTransferOffset(TRANSFER_OFFSET_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ContractPackage.GROUP_ENTITY__TAX_RATE:
				return TAX_RATE_EDEFAULT == null ? taxRate != null : !TAX_RATE_EDEFAULT.equals(taxRate);
			case ContractPackage.GROUP_ENTITY__OWNERSHIP:
				return OWNERSHIP_EDEFAULT == null ? ownership != null : !OWNERSHIP_EDEFAULT.equals(ownership);
			case ContractPackage.GROUP_ENTITY__TRANSFER_OFFSET:
				return transferOffset != TRANSFER_OFFSET_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
		result.append(", transferOffset: ");
		result.append(transferOffset);
		result.append(')');
		return result.toString();
	}

} // GroupEntityImpl
