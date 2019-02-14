/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import java.time.LocalDate;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Paper Deal Allocation Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PaperDealAllocationEntryImpl#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PaperDealAllocationEntryImpl#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PaperDealAllocationEntryImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PaperDealAllocationEntryImpl#getValue <em>Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PaperDealAllocationEntryImpl#isSettled <em>Settled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PaperDealAllocationEntryImpl#getExposures <em>Exposures</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PaperDealAllocationEntryImpl extends EObjectImpl implements PaperDealAllocationEntry {
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
	 * The default value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final double QUANTITY_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected double quantity = QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected double price = PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final double VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected double value = VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #isSettled() <em>Settled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSettled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SETTLED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSettled() <em>Settled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSettled()
	 * @generated
	 * @ordered
	 */
	protected boolean settled = SETTLED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getExposures() <em>Exposures</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExposures()
	 * @generated
	 * @ordered
	 */
	protected EList<ExposureDetail> exposures;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PaperDealAllocationEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.PAPER_DEAL_ALLOCATION_ENTRY;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getQuantity() {
		return quantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuantity(double newQuantity) {
		double oldQuantity = quantity;
		quantity = newQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__QUANTITY, oldQuantity, quantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrice(double newPrice) {
		double oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(double newValue) {
		double oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSettled() {
		return settled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSettled(boolean newSettled) {
		boolean oldSettled = settled;
		settled = newSettled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__SETTLED, oldSettled, settled));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExposureDetail> getExposures() {
		if (exposures == null) {
			exposures = new EObjectContainmentEList<ExposureDetail>(ExposureDetail.class, this, SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__EXPOSURES);
		}
		return exposures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__EXPOSURES:
				return ((InternalEList<?>)getExposures()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__DATE:
				return getDate();
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__QUANTITY:
				return getQuantity();
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__PRICE:
				return getPrice();
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__VALUE:
				return getValue();
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__SETTLED:
				return isSettled();
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__EXPOSURES:
				return getExposures();
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
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__DATE:
				setDate((LocalDate)newValue);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__QUANTITY:
				setQuantity((Double)newValue);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__PRICE:
				setPrice((Double)newValue);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__VALUE:
				setValue((Double)newValue);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__SETTLED:
				setSettled((Boolean)newValue);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__EXPOSURES:
				getExposures().clear();
				getExposures().addAll((Collection<? extends ExposureDetail>)newValue);
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
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__QUANTITY:
				setQuantity(QUANTITY_EDEFAULT);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__SETTLED:
				setSettled(SETTLED_EDEFAULT);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__EXPOSURES:
				getExposures().clear();
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
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__QUANTITY:
				return quantity != QUANTITY_EDEFAULT;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__PRICE:
				return price != PRICE_EDEFAULT;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__VALUE:
				return value != VALUE_EDEFAULT;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__SETTLED:
				return settled != SETTLED_EDEFAULT;
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY__EXPOSURES:
				return exposures != null && !exposures.isEmpty();
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
		result.append(" (date: ");
		result.append(date);
		result.append(", quantity: ");
		result.append(quantity);
		result.append(", price: ");
		result.append(price);
		result.append(", value: ");
		result.append(value);
		result.append(", settled: ");
		result.append(settled);
		result.append(')');
		return result.toString();
	}

} //PaperDealAllocationEntryImpl
