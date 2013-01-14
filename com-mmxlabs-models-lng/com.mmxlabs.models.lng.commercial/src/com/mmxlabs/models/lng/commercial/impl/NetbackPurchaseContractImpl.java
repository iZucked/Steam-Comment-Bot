/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NetbackPurchaseContract;
import com.mmxlabs.models.lng.commercial.NotionalBallastParameters;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Netback Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl#getNotionalBallastParameters <em>Notional Ballast Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl#getMargin <em>Margin</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl#getFloorPrice <em>Floor Price</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NetbackPurchaseContractImpl extends PurchaseContractImpl implements NetbackPurchaseContract {
	/**
	 * The cached value of the '{@link #getNotionalBallastParameters() <em>Notional Ballast Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalBallastParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<NotionalBallastParameters> notionalBallastParameters;

	/**
	 * The default value of the '{@link #getMargin() <em>Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMargin()
	 * @generated
	 * @ordered
	 */
	protected static final double MARGIN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMargin() <em>Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMargin()
	 * @generated
	 * @ordered
	 */
	protected double margin = MARGIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getFloorPrice() <em>Floor Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloorPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double FLOOR_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getFloorPrice() <em>Floor Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloorPrice()
	 * @generated
	 * @ordered
	 */
	protected double floorPrice = FLOOR_PRICE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NetbackPurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.NETBACK_PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NotionalBallastParameters> getNotionalBallastParameters() {
		if (notionalBallastParameters == null) {
			notionalBallastParameters = new EObjectContainmentEList<NotionalBallastParameters>(NotionalBallastParameters.class, this, CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS);
		}
		return notionalBallastParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMargin() {
		return margin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMargin(double newMargin) {
		double oldMargin = margin;
		margin = newMargin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NETBACK_PURCHASE_CONTRACT__MARGIN, oldMargin, margin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getFloorPrice() {
		return floorPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFloorPrice(double newFloorPrice) {
		double oldFloorPrice = floorPrice;
		floorPrice = newFloorPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NETBACK_PURCHASE_CONTRACT__FLOOR_PRICE, oldFloorPrice, floorPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				return ((InternalEList<?>)getNotionalBallastParameters()).basicRemove(otherEnd, msgs);
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
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				return getNotionalBallastParameters();
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__MARGIN:
				return getMargin();
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__FLOOR_PRICE:
				return getFloorPrice();
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
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				getNotionalBallastParameters().clear();
				getNotionalBallastParameters().addAll((Collection<? extends NotionalBallastParameters>)newValue);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__MARGIN:
				setMargin((Double)newValue);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__FLOOR_PRICE:
				setFloorPrice((Double)newValue);
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
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				getNotionalBallastParameters().clear();
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__MARGIN:
				setMargin(MARGIN_EDEFAULT);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__FLOOR_PRICE:
				setFloorPrice(FLOOR_PRICE_EDEFAULT);
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
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				return notionalBallastParameters != null && !notionalBallastParameters.isEmpty();
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__MARGIN:
				return margin != MARGIN_EDEFAULT;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__FLOOR_PRICE:
				return floorPrice != FLOOR_PRICE_EDEFAULT;
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
		result.append(" (margin: ");
		result.append(margin);
		result.append(", floorPrice: ");
		result.append(floorPrice);
		result.append(')');
		return result.toString();
	}

} // end of NetbackPurchaseContractImpl

// finish type fixing
