/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.types.impl.ALegalEntityImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Legal Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.LegalEntityImpl#getTaxRates <em>Tax Rates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.LegalEntityImpl#getTransferPrice <em>Transfer Price</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LegalEntityImpl extends ALegalEntityImpl implements LegalEntity {
	/**
	 * The cached value of the '{@link #getTaxRates() <em>Tax Rates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getTaxRates()
	 * @generated
	 * @ordered
	 */
	protected EList<TaxRate> taxRates;

	/**
	 * The default value of the '{@link #getTransferPrice() <em>Transfer Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getTransferPrice()
	 * @generated
	 * @ordered
	 */
	protected static final float TRANSFER_PRICE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getTransferPrice() <em>Transfer Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getTransferPrice()
	 * @generated
	 * @ordered
	 */
	protected float transferPrice = TRANSFER_PRICE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LegalEntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.LEGAL_ENTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TaxRate> getTaxRates() {
		if (taxRates == null) {
			taxRates = new EObjectContainmentEList<TaxRate>(TaxRate.class, this, CommercialPackage.LEGAL_ENTITY__TAX_RATES);
		}
		return taxRates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getTransferPrice() {
		return transferPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransferPrice(float newTransferPrice) {
		float oldTransferPrice = transferPrice;
		transferPrice = newTransferPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.LEGAL_ENTITY__TRANSFER_PRICE, oldTransferPrice, transferPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.LEGAL_ENTITY__TAX_RATES:
				return ((InternalEList<?>)getTaxRates()).basicRemove(otherEnd, msgs);
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
			case CommercialPackage.LEGAL_ENTITY__TAX_RATES:
				return getTaxRates();
			case CommercialPackage.LEGAL_ENTITY__TRANSFER_PRICE:
				return getTransferPrice();
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
			case CommercialPackage.LEGAL_ENTITY__TAX_RATES:
				getTaxRates().clear();
				getTaxRates().addAll((Collection<? extends TaxRate>)newValue);
				return;
			case CommercialPackage.LEGAL_ENTITY__TRANSFER_PRICE:
				setTransferPrice((Float)newValue);
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
			case CommercialPackage.LEGAL_ENTITY__TAX_RATES:
				getTaxRates().clear();
				return;
			case CommercialPackage.LEGAL_ENTITY__TRANSFER_PRICE:
				setTransferPrice(TRANSFER_PRICE_EDEFAULT);
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
			case CommercialPackage.LEGAL_ENTITY__TAX_RATES:
				return taxRates != null && !taxRates.isEmpty();
			case CommercialPackage.LEGAL_ENTITY__TRANSFER_PRICE:
				return transferPrice != TRANSFER_PRICE_EDEFAULT;
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
		result.append(" (transferPrice: ");
		result.append(transferPrice);
		result.append(')');
		return result.toString();
	}

} // end of LegalEntityImpl

// finish type fixing
