/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import org.eclipse.emf.common.notify.Notification;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cooldown Price</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl#isLumpsum <em>Lumpsum</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CooldownPriceImpl extends PortsExpressionMapImpl implements CooldownPrice {
	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected CommodityIndex index;
	/**
	 * The default value of the '{@link #isLumpsum() <em>Lumpsum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLumpsum()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LUMPSUM_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isLumpsum() <em>Lumpsum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLumpsum()
	 * @generated
	 * @ordered
	 */
	protected boolean lumpsum = LUMPSUM_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CooldownPriceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.COOLDOWN_PRICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex getIndex() {
		if (index != null && index.eIsProxy()) {
			InternalEObject oldIndex = (InternalEObject)index;
			index = (CommodityIndex)eResolveProxy(oldIndex);
			if (index != oldIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.COOLDOWN_PRICE__INDEX, oldIndex, index));
			}
		}
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex basicGetIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(CommodityIndex newIndex) {
		CommodityIndex oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.COOLDOWN_PRICE__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLumpsum() {
		return lumpsum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLumpsum(boolean newLumpsum) {
		boolean oldLumpsum = lumpsum;
		lumpsum = newLumpsum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.COOLDOWN_PRICE__LUMPSUM, oldLumpsum, lumpsum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.COOLDOWN_PRICE__INDEX:
				if (resolve) return getIndex();
				return basicGetIndex();
			case PricingPackage.COOLDOWN_PRICE__LUMPSUM:
				return isLumpsum();
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
			case PricingPackage.COOLDOWN_PRICE__INDEX:
				setIndex((CommodityIndex)newValue);
				return;
			case PricingPackage.COOLDOWN_PRICE__LUMPSUM:
				setLumpsum((Boolean)newValue);
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
			case PricingPackage.COOLDOWN_PRICE__INDEX:
				setIndex((CommodityIndex)null);
				return;
			case PricingPackage.COOLDOWN_PRICE__LUMPSUM:
				setLumpsum(LUMPSUM_EDEFAULT);
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
			case PricingPackage.COOLDOWN_PRICE__INDEX:
				return index != null;
			case PricingPackage.COOLDOWN_PRICE__LUMPSUM:
				return lumpsum != LUMPSUM_EDEFAULT;
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
		result.append(" (lumpsum: ");
		result.append(lumpsum);
		result.append(')');
		return result.toString();
	}

} // end of CooldownPriceImpl

// finish type fixing
