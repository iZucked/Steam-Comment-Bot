/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;

import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inventory Capacity Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryCapacityRowImpl#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryCapacityRowImpl#getMinVolume <em>Min Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryCapacityRowImpl#getMaxVolume <em>Max Volume</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventoryCapacityRowImpl extends EObjectImpl implements InventoryCapacityRow {
	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinVolume() <em>Min Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinVolume() <em>Min Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinVolume()
	 * @generated
	 * @ordered
	 */
	protected int minVolume = MIN_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxVolume() <em>Max Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxVolume() <em>Max Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxVolume()
	 * @generated
	 * @ordered
	 */
	protected int maxVolume = MAX_VOLUME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryCapacityRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.INVENTORY_CAPACITY_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDate(LocalDate newDate) {
		LocalDate oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY_CAPACITY_ROW__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinVolume() {
		return minVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinVolume(int newMinVolume) {
		int oldMinVolume = minVolume;
		minVolume = newMinVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY_CAPACITY_ROW__MIN_VOLUME, oldMinVolume, minVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxVolume() {
		return maxVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxVolume(int newMaxVolume) {
		int oldMaxVolume = maxVolume;
		maxVolume = newMaxVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY_CAPACITY_ROW__MAX_VOLUME, oldMaxVolume, maxVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.INVENTORY_CAPACITY_ROW__DATE:
				return getDate();
			case CargoPackage.INVENTORY_CAPACITY_ROW__MIN_VOLUME:
				return getMinVolume();
			case CargoPackage.INVENTORY_CAPACITY_ROW__MAX_VOLUME:
				return getMaxVolume();
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
			case CargoPackage.INVENTORY_CAPACITY_ROW__DATE:
				setDate((LocalDate)newValue);
				return;
			case CargoPackage.INVENTORY_CAPACITY_ROW__MIN_VOLUME:
				setMinVolume((Integer)newValue);
				return;
			case CargoPackage.INVENTORY_CAPACITY_ROW__MAX_VOLUME:
				setMaxVolume((Integer)newValue);
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
			case CargoPackage.INVENTORY_CAPACITY_ROW__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case CargoPackage.INVENTORY_CAPACITY_ROW__MIN_VOLUME:
				setMinVolume(MIN_VOLUME_EDEFAULT);
				return;
			case CargoPackage.INVENTORY_CAPACITY_ROW__MAX_VOLUME:
				setMaxVolume(MAX_VOLUME_EDEFAULT);
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
			case CargoPackage.INVENTORY_CAPACITY_ROW__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case CargoPackage.INVENTORY_CAPACITY_ROW__MIN_VOLUME:
				return minVolume != MIN_VOLUME_EDEFAULT;
			case CargoPackage.INVENTORY_CAPACITY_ROW__MAX_VOLUME:
				return maxVolume != MAX_VOLUME_EDEFAULT;
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
		result.append(" (date: ");
		result.append(date);
		result.append(", minVolume: ");
		result.append(minVolume);
		result.append(", maxVolume: ");
		result.append(maxVolume);
		result.append(')');
		return result.toString();
	}

} //InventoryCapacityRowImpl
