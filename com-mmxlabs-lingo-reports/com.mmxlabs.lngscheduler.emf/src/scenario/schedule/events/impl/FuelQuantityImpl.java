/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelPurpose;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.FuelUnit;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Fuel Quantity</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.schedule.events.impl.FuelQuantityImpl#getFuelType <em>Fuel Type</em>}</li>
 * <li>{@link scenario.schedule.events.impl.FuelQuantityImpl#getQuantity <em>Quantity</em>}</li>
 * <li>{@link scenario.schedule.events.impl.FuelQuantityImpl#getUnitPrice <em>Unit Price</em>}</li>
 * <li>{@link scenario.schedule.events.impl.FuelQuantityImpl#getTotalPrice <em>Total Price</em>}</li>
 * <li>{@link scenario.schedule.events.impl.FuelQuantityImpl#getFuelUnit <em>Fuel Unit</em>}</li>
 * <li>{@link scenario.schedule.events.impl.FuelQuantityImpl#getPurpose <em>Purpose</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class FuelQuantityImpl extends EObjectImpl implements FuelQuantity {
	/**
	 * The default value of the '{@link #getFuelType() <em>Fuel Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFuelType()
	 * @generated
	 * @ordered
	 */
	protected static final FuelType FUEL_TYPE_EDEFAULT = FuelType.FBO;

	/**
	 * The cached value of the '{@link #getFuelType() <em>Fuel Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFuelType()
	 * @generated
	 * @ordered
	 */
	protected FuelType fuelType = FUEL_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getQuantity() <em>Quantity</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final long QUANTITY_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getQuantity() <em>Quantity</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected long quantity = QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnitPrice() <em>Unit Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected static final long UNIT_PRICE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getUnitPrice() <em>Unit Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected long unitPrice = UNIT_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalPrice() <em>Total Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTotalPrice()
	 * @generated
	 * @ordered
	 */
	protected static final long TOTAL_PRICE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getTotalPrice() <em>Total Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTotalPrice()
	 * @generated
	 * @ordered
	 */
	protected long totalPrice = TOTAL_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFuelUnit() <em>Fuel Unit</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFuelUnit()
	 * @generated
	 * @ordered
	 */
	protected static final FuelUnit FUEL_UNIT_EDEFAULT = FuelUnit.MT;

	/**
	 * The cached value of the '{@link #getFuelUnit() <em>Fuel Unit</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFuelUnit()
	 * @generated
	 * @ordered
	 */
	protected FuelUnit fuelUnit = FUEL_UNIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getPurpose() <em>Purpose</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPurpose()
	 * @generated
	 * @ordered
	 */
	protected static final FuelPurpose PURPOSE_EDEFAULT = FuelPurpose.TRAVEL;

	/**
	 * The cached value of the '{@link #getPurpose() <em>Purpose</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPurpose()
	 * @generated
	 * @ordered
	 */
	protected FuelPurpose purpose = PURPOSE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected FuelQuantityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EventsPackage.Literals.FUEL_QUANTITY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public FuelType getFuelType() {
		return fuelType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setFuelType(final FuelType newFuelType) {
		final FuelType oldFuelType = fuelType;
		fuelType = newFuelType == null ? FUEL_TYPE_EDEFAULT : newFuelType;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.FUEL_QUANTITY__FUEL_TYPE, oldFuelType, fuelType));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public long getQuantity() {
		return quantity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setQuantity(final long newQuantity) {
		final long oldQuantity = quantity;
		quantity = newQuantity;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.FUEL_QUANTITY__QUANTITY, oldQuantity, quantity));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public long getUnitPrice() {
		return unitPrice;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setUnitPrice(final long newUnitPrice) {
		final long oldUnitPrice = unitPrice;
		unitPrice = newUnitPrice;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.FUEL_QUANTITY__UNIT_PRICE, oldUnitPrice, unitPrice));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public long getTotalPrice() {
		return totalPrice;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTotalPrice(final long newTotalPrice) {
		final long oldTotalPrice = totalPrice;
		totalPrice = newTotalPrice;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.FUEL_QUANTITY__TOTAL_PRICE, oldTotalPrice, totalPrice));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public FuelUnit getFuelUnit() {
		return fuelUnit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setFuelUnit(final FuelUnit newFuelUnit) {
		final FuelUnit oldFuelUnit = fuelUnit;
		fuelUnit = newFuelUnit == null ? FUEL_UNIT_EDEFAULT : newFuelUnit;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.FUEL_QUANTITY__FUEL_UNIT, oldFuelUnit, fuelUnit));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public FuelPurpose getPurpose() {
		return purpose;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setPurpose(final FuelPurpose newPurpose) {
		final FuelPurpose oldPurpose = purpose;
		purpose = newPurpose == null ? PURPOSE_EDEFAULT : newPurpose;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.FUEL_QUANTITY__PURPOSE, oldPurpose, purpose));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case EventsPackage.FUEL_QUANTITY__FUEL_TYPE:
			return getFuelType();
		case EventsPackage.FUEL_QUANTITY__QUANTITY:
			return getQuantity();
		case EventsPackage.FUEL_QUANTITY__UNIT_PRICE:
			return getUnitPrice();
		case EventsPackage.FUEL_QUANTITY__TOTAL_PRICE:
			return getTotalPrice();
		case EventsPackage.FUEL_QUANTITY__FUEL_UNIT:
			return getFuelUnit();
		case EventsPackage.FUEL_QUANTITY__PURPOSE:
			return getPurpose();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case EventsPackage.FUEL_QUANTITY__FUEL_TYPE:
			setFuelType((FuelType) newValue);
			return;
		case EventsPackage.FUEL_QUANTITY__QUANTITY:
			setQuantity((Long) newValue);
			return;
		case EventsPackage.FUEL_QUANTITY__UNIT_PRICE:
			setUnitPrice((Long) newValue);
			return;
		case EventsPackage.FUEL_QUANTITY__TOTAL_PRICE:
			setTotalPrice((Long) newValue);
			return;
		case EventsPackage.FUEL_QUANTITY__FUEL_UNIT:
			setFuelUnit((FuelUnit) newValue);
			return;
		case EventsPackage.FUEL_QUANTITY__PURPOSE:
			setPurpose((FuelPurpose) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case EventsPackage.FUEL_QUANTITY__FUEL_TYPE:
			setFuelType(FUEL_TYPE_EDEFAULT);
			return;
		case EventsPackage.FUEL_QUANTITY__QUANTITY:
			setQuantity(QUANTITY_EDEFAULT);
			return;
		case EventsPackage.FUEL_QUANTITY__UNIT_PRICE:
			setUnitPrice(UNIT_PRICE_EDEFAULT);
			return;
		case EventsPackage.FUEL_QUANTITY__TOTAL_PRICE:
			setTotalPrice(TOTAL_PRICE_EDEFAULT);
			return;
		case EventsPackage.FUEL_QUANTITY__FUEL_UNIT:
			setFuelUnit(FUEL_UNIT_EDEFAULT);
			return;
		case EventsPackage.FUEL_QUANTITY__PURPOSE:
			setPurpose(PURPOSE_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case EventsPackage.FUEL_QUANTITY__FUEL_TYPE:
			return fuelType != FUEL_TYPE_EDEFAULT;
		case EventsPackage.FUEL_QUANTITY__QUANTITY:
			return quantity != QUANTITY_EDEFAULT;
		case EventsPackage.FUEL_QUANTITY__UNIT_PRICE:
			return unitPrice != UNIT_PRICE_EDEFAULT;
		case EventsPackage.FUEL_QUANTITY__TOTAL_PRICE:
			return totalPrice != TOTAL_PRICE_EDEFAULT;
		case EventsPackage.FUEL_QUANTITY__FUEL_UNIT:
			return fuelUnit != FUEL_UNIT_EDEFAULT;
		case EventsPackage.FUEL_QUANTITY__PURPOSE:
			return purpose != PURPOSE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (fuelType: ");
		result.append(fuelType);
		result.append(", quantity: ");
		result.append(quantity);
		result.append(", unitPrice: ");
		result.append(unitPrice);
		result.append(", totalPrice: ");
		result.append(totalPrice);
		result.append(", fuelUnit: ");
		result.append(fuelUnit);
		result.append(", purpose: ");
		result.append(purpose);
		result.append(')');
		return result.toString();
	}

} // FuelQuantityImpl
