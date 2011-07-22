/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
	protected static final Double TAX_RATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTaxRate() <em>Tax Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaxRate()
	 * @generated
	 * @ordered
	 */
	protected Double taxRate = TAX_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOwnership() <em>Ownership</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnership()
	 * @generated
	 * @ordered
	 */
	protected static final Double OWNERSHIP_EDEFAULT = new Double(1.0);

	/**
	 * The cached value of the '{@link #getOwnership() <em>Ownership</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnership()
	 * @generated
	 * @ordered
	 */
	protected Double ownership = OWNERSHIP_EDEFAULT;

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
	public Double getTaxRate() {
		return taxRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaxRate(Double newTaxRate) {
		Double oldTaxRate = taxRate;
		taxRate = newTaxRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.ENTITY__TAX_RATE, oldTaxRate, taxRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getOwnership() {
		return ownership;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwnership(Double newOwnership) {
		Double oldOwnership = ownership;
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
				setTaxRate((Double)newValue);
				return;
			case ContractPackage.ENTITY__OWNERSHIP:
				setOwnership((Double)newValue);
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
				return TAX_RATE_EDEFAULT == null ? taxRate != null : !TAX_RATE_EDEFAULT.equals(taxRate);
			case ContractPackage.ENTITY__OWNERSHIP:
				return OWNERSHIP_EDEFAULT == null ? ownership != null : !OWNERSHIP_EDEFAULT.equals(ownership);
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
