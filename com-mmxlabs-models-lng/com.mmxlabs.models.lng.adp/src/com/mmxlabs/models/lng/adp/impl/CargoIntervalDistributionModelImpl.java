/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.IntervalType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Interval Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl#getIntervalType <em>Interval Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl#getSpacing <em>Spacing</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoIntervalDistributionModelImpl extends EObjectImpl implements CargoIntervalDistributionModel {
	/**
	 * The default value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected int quantity = QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getIntervalType() <em>Interval Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntervalType()
	 * @generated
	 * @ordered
	 */
	protected static final IntervalType INTERVAL_TYPE_EDEFAULT = IntervalType.QUARTERLY;

	/**
	 * The cached value of the '{@link #getIntervalType() <em>Interval Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntervalType()
	 * @generated
	 * @ordered
	 */
	protected IntervalType intervalType = INTERVAL_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSpacing() <em>Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int SPACING_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpacing() <em>Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpacing()
	 * @generated
	 * @ordered
	 */
	protected int spacing = SPACING_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoIntervalDistributionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.CARGO_INTERVAL_DISTRIBUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuantity(int newQuantity) {
		int oldQuantity = quantity;
		quantity = newQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY, oldQuantity, quantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IntervalType getIntervalType() {
		return intervalType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIntervalType(IntervalType newIntervalType) {
		IntervalType oldIntervalType = intervalType;
		intervalType = newIntervalType == null ? INTERVAL_TYPE_EDEFAULT : newIntervalType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE, oldIntervalType, intervalType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpacing(int newSpacing) {
		int oldSpacing = spacing;
		spacing = newSpacing;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING, oldSpacing, spacing));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY:
				return getQuantity();
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE:
				return getIntervalType();
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING:
				return getSpacing();
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
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY:
				setQuantity((Integer)newValue);
				return;
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE:
				setIntervalType((IntervalType)newValue);
				return;
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING:
				setSpacing((Integer)newValue);
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
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY:
				setQuantity(QUANTITY_EDEFAULT);
				return;
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE:
				setIntervalType(INTERVAL_TYPE_EDEFAULT);
				return;
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING:
				setSpacing(SPACING_EDEFAULT);
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
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY:
				return quantity != QUANTITY_EDEFAULT;
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE:
				return intervalType != INTERVAL_TYPE_EDEFAULT;
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING:
				return spacing != SPACING_EDEFAULT;
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
		result.append(" (quantity: ");
		result.append(quantity);
		result.append(", intervalType: ");
		result.append(intervalType);
		result.append(", spacing: ");
		result.append(spacing);
		result.append(')');
		return result.toString();
	}

} //CargoIntervalDistributionModelImpl
