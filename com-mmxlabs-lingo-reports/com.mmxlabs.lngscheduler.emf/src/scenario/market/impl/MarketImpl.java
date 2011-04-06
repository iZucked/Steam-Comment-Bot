/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.market.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import scenario.impl.NamedObjectImpl;
import scenario.market.Market;
import scenario.market.MarketPackage;
import scenario.market.StepwisePriceCurve;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.market.impl.MarketImpl#getPriceCurve <em>Price Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MarketImpl extends NamedObjectImpl implements Market {
	/**
	 * The cached value of the '{@link #getPriceCurve() <em>Price Curve</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceCurve()
	 * @generated
	 * @ordered
	 */
	protected StepwisePriceCurve priceCurve;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MarketPackage.Literals.MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StepwisePriceCurve getPriceCurve() {
		return priceCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPriceCurve(StepwisePriceCurve newPriceCurve, NotificationChain msgs) {
		StepwisePriceCurve oldPriceCurve = priceCurve;
		priceCurve = newPriceCurve;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MarketPackage.MARKET__PRICE_CURVE, oldPriceCurve, newPriceCurve);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceCurve(StepwisePriceCurve newPriceCurve) {
		if (newPriceCurve != priceCurve) {
			NotificationChain msgs = null;
			if (priceCurve != null)
				msgs = ((InternalEObject)priceCurve).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MarketPackage.MARKET__PRICE_CURVE, null, msgs);
			if (newPriceCurve != null)
				msgs = ((InternalEObject)newPriceCurve).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MarketPackage.MARKET__PRICE_CURVE, null, msgs);
			msgs = basicSetPriceCurve(newPriceCurve, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MarketPackage.MARKET__PRICE_CURVE, newPriceCurve, newPriceCurve));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MarketPackage.MARKET__PRICE_CURVE:
				return basicSetPriceCurve(null, msgs);
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
			case MarketPackage.MARKET__PRICE_CURVE:
				return getPriceCurve();
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
			case MarketPackage.MARKET__PRICE_CURVE:
				setPriceCurve((StepwisePriceCurve)newValue);
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
			case MarketPackage.MARKET__PRICE_CURVE:
				setPriceCurve((StepwisePriceCurve)null);
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
			case MarketPackage.MARKET__PRICE_CURVE:
				return priceCurve != null;
		}
		return super.eIsSet(featureID);
	}

} //MarketImpl
