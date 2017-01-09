/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Shipping Cost Plan</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostPlanImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostPlanImpl#getNotionalDayRate <em>Notional Day Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostPlanImpl#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostPlanImpl#getRows <em>Rows</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ShippingCostPlanImpl extends NamedObjectImpl implements ShippingCostPlan {
	/**
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * The default value of the '{@link #getNotionalDayRate() <em>Notional Day Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalDayRate()
	 * @generated
	 * @ordered
	 */
	protected static final int NOTIONAL_DAY_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNotionalDayRate() <em>Notional Day Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalDayRate()
	 * @generated
	 * @ordered
	 */
	protected int notionalDayRate = NOTIONAL_DAY_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseFuelPrice() <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double BASE_FUEL_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBaseFuelPrice() <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected double baseFuelPrice = BASE_FUEL_PRICE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRows() <em>Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRows()
	 * @generated
	 * @ordered
	 */
	protected EList<ShippingCostRow> rows;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ShippingCostPlanImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SHIPPING_COST_PLAN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.SHIPPING_COST_PLAN__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_PLAN__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getNotionalDayRate() {
		return notionalDayRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNotionalDayRate(int newNotionalDayRate) {
		int oldNotionalDayRate = notionalDayRate;
		notionalDayRate = newNotionalDayRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_PLAN__NOTIONAL_DAY_RATE, oldNotionalDayRate, notionalDayRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getBaseFuelPrice() {
		return baseFuelPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseFuelPrice(double newBaseFuelPrice) {
		double oldBaseFuelPrice = baseFuelPrice;
		baseFuelPrice = newBaseFuelPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_PLAN__BASE_FUEL_PRICE, oldBaseFuelPrice, baseFuelPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ShippingCostRow> getRows() {
		if (rows == null) {
			rows = new EObjectContainmentEList<ShippingCostRow>(ShippingCostRow.class, this, AnalyticsPackage.SHIPPING_COST_PLAN__ROWS);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SHIPPING_COST_PLAN__ROWS:
				return ((InternalEList<?>)getRows()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.SHIPPING_COST_PLAN__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case AnalyticsPackage.SHIPPING_COST_PLAN__NOTIONAL_DAY_RATE:
				return getNotionalDayRate();
			case AnalyticsPackage.SHIPPING_COST_PLAN__BASE_FUEL_PRICE:
				return getBaseFuelPrice();
			case AnalyticsPackage.SHIPPING_COST_PLAN__ROWS:
				return getRows();
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
			case AnalyticsPackage.SHIPPING_COST_PLAN__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_PLAN__NOTIONAL_DAY_RATE:
				setNotionalDayRate((Integer)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_PLAN__BASE_FUEL_PRICE:
				setBaseFuelPrice((Double)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_PLAN__ROWS:
				getRows().clear();
				getRows().addAll((Collection<? extends ShippingCostRow>)newValue);
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
			case AnalyticsPackage.SHIPPING_COST_PLAN__VESSEL:
				setVessel((Vessel)null);
				return;
			case AnalyticsPackage.SHIPPING_COST_PLAN__NOTIONAL_DAY_RATE:
				setNotionalDayRate(NOTIONAL_DAY_RATE_EDEFAULT);
				return;
			case AnalyticsPackage.SHIPPING_COST_PLAN__BASE_FUEL_PRICE:
				setBaseFuelPrice(BASE_FUEL_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SHIPPING_COST_PLAN__ROWS:
				getRows().clear();
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
			case AnalyticsPackage.SHIPPING_COST_PLAN__VESSEL:
				return vessel != null;
			case AnalyticsPackage.SHIPPING_COST_PLAN__NOTIONAL_DAY_RATE:
				return notionalDayRate != NOTIONAL_DAY_RATE_EDEFAULT;
			case AnalyticsPackage.SHIPPING_COST_PLAN__BASE_FUEL_PRICE:
				return baseFuelPrice != BASE_FUEL_PRICE_EDEFAULT;
			case AnalyticsPackage.SHIPPING_COST_PLAN__ROWS:
				return rows != null && !rows.isEmpty();
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
		result.append(" (notionalDayRate: ");
		result.append(notionalDayRate);
		result.append(", baseFuelPrice: ");
		result.append(baseFuelPrice);
		result.append(')');
		return result.toString();
	}

} // end of ShippingCostPlanImpl

// finish type fixing
