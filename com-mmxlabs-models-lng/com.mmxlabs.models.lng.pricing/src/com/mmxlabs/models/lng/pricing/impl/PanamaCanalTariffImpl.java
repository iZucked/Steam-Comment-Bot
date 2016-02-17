/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PricingPackage;

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
 * An implementation of the model object '<em><b>Panama Canal Tariff</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffImpl#getBands <em>Bands</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffImpl#getAvailableFrom <em>Available From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffImpl#getMarkupRate <em>Markup Rate</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PanamaCanalTariffImpl extends EObjectImpl implements PanamaCanalTariff {
	/**
	 * The cached value of the '{@link #getBands() <em>Bands</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBands()
	 * @generated
	 * @ordered
	 */
	protected EList<PanamaCanalTariffBand> bands;

	/**
	 * The default value of the '{@link #getAvailableFrom() <em>Available From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailableFrom()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate AVAILABLE_FROM_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getAvailableFrom() <em>Available From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailableFrom()
	 * @generated
	 * @ordered
	 */
	protected LocalDate availableFrom = AVAILABLE_FROM_EDEFAULT;

	/**
	 * This is true if the Available From attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean availableFromESet;

	/**
	 * The default value of the '{@link #getMarkupRate() <em>Markup Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarkupRate()
	 * @generated
	 * @ordered
	 */
	protected static final double MARKUP_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMarkupRate() <em>Markup Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarkupRate()
	 * @generated
	 * @ordered
	 */
	protected double markupRate = MARKUP_RATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PanamaCanalTariffImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PANAMA_CANAL_TARIFF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PanamaCanalTariffBand> getBands() {
		if (bands == null) {
			bands = new EObjectContainmentEList<PanamaCanalTariffBand>(PanamaCanalTariffBand.class, this, PricingPackage.PANAMA_CANAL_TARIFF__BANDS);
		}
		return bands;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getAvailableFrom() {
		return availableFrom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAvailableFrom(LocalDate newAvailableFrom) {
		LocalDate oldAvailableFrom = availableFrom;
		availableFrom = newAvailableFrom;
		boolean oldAvailableFromESet = availableFromESet;
		availableFromESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_CANAL_TARIFF__AVAILABLE_FROM, oldAvailableFrom, availableFrom, !oldAvailableFromESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAvailableFrom() {
		LocalDate oldAvailableFrom = availableFrom;
		boolean oldAvailableFromESet = availableFromESet;
		availableFrom = AVAILABLE_FROM_EDEFAULT;
		availableFromESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PricingPackage.PANAMA_CANAL_TARIFF__AVAILABLE_FROM, oldAvailableFrom, AVAILABLE_FROM_EDEFAULT, oldAvailableFromESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAvailableFrom() {
		return availableFromESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMarkupRate() {
		return markupRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarkupRate(double newMarkupRate) {
		double oldMarkupRate = markupRate;
		markupRate = newMarkupRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_CANAL_TARIFF__MARKUP_RATE, oldMarkupRate, markupRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.PANAMA_CANAL_TARIFF__BANDS:
				return ((InternalEList<?>)getBands()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.PANAMA_CANAL_TARIFF__BANDS:
				return getBands();
			case PricingPackage.PANAMA_CANAL_TARIFF__AVAILABLE_FROM:
				return getAvailableFrom();
			case PricingPackage.PANAMA_CANAL_TARIFF__MARKUP_RATE:
				return getMarkupRate();
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
			case PricingPackage.PANAMA_CANAL_TARIFF__BANDS:
				getBands().clear();
				getBands().addAll((Collection<? extends PanamaCanalTariffBand>)newValue);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF__AVAILABLE_FROM:
				setAvailableFrom((LocalDate)newValue);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF__MARKUP_RATE:
				setMarkupRate((Double)newValue);
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
			case PricingPackage.PANAMA_CANAL_TARIFF__BANDS:
				getBands().clear();
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF__AVAILABLE_FROM:
				unsetAvailableFrom();
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF__MARKUP_RATE:
				setMarkupRate(MARKUP_RATE_EDEFAULT);
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
			case PricingPackage.PANAMA_CANAL_TARIFF__BANDS:
				return bands != null && !bands.isEmpty();
			case PricingPackage.PANAMA_CANAL_TARIFF__AVAILABLE_FROM:
				return isSetAvailableFrom();
			case PricingPackage.PANAMA_CANAL_TARIFF__MARKUP_RATE:
				return markupRate != MARKUP_RATE_EDEFAULT;
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
		result.append(" (availableFrom: ");
		if (availableFromESet) result.append(availableFrom); else result.append("<unset>");
		result.append(", markupRate: ");
		result.append(markupRate);
		result.append(')');
		return result.toString();
	}

} //PanamaCanalTariffImpl
