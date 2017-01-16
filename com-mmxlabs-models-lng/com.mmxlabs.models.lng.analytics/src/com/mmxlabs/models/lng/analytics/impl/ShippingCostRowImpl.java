/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Shipping Cost Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl#getCargoPrice <em>Cargo Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl#getDestinationType <em>Destination Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl#getHeelVolume <em>Heel Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl#isIncludePortCosts <em>Include Port Costs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ShippingCostRowImpl extends MMXObjectImpl implements ShippingCostRow {
	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected Date date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCargoPrice() <em>Cargo Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double CARGO_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCargoPrice() <em>Cargo Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoPrice()
	 * @generated
	 * @ordered
	 */
	protected double cargoPrice = CARGO_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCvValue() <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCvValue()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCvValue() <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCvValue()
	 * @generated
	 * @ordered
	 */
	protected double cvValue = CV_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDestinationType() <em>Destination Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationType()
	 * @generated
	 * @ordered
	 */
	protected static final DestinationType DESTINATION_TYPE_EDEFAULT = DestinationType.START;

	/**
	 * The cached value of the '{@link #getDestinationType() <em>Destination Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationType()
	 * @generated
	 * @ordered
	 */
	protected DestinationType destinationType = DESTINATION_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeelVolume() <em>Heel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int HEEL_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHeelVolume() <em>Heel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelVolume()
	 * @generated
	 * @ordered
	 */
	protected int heelVolume = HEEL_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #isIncludePortCosts() <em>Include Port Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludePortCosts()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_PORT_COSTS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIncludePortCosts() <em>Include Port Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludePortCosts()
	 * @generated
	 * @ordered
	 */
	protected boolean includePortCosts = INCLUDE_PORT_COSTS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ShippingCostRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SHIPPING_COST_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.SHIPPING_COST_ROW__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_ROW__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDate(Date newDate) {
		Date oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_ROW__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCargoPrice() {
		return cargoPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoPrice(double newCargoPrice) {
		double oldCargoPrice = cargoPrice;
		cargoPrice = newCargoPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_ROW__CARGO_PRICE, oldCargoPrice, cargoPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCvValue() {
		return cvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCvValue(double newCvValue) {
		double oldCvValue = cvValue;
		cvValue = newCvValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_ROW__CV_VALUE, oldCvValue, cvValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DestinationType getDestinationType() {
		return destinationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDestinationType(DestinationType newDestinationType) {
		DestinationType oldDestinationType = destinationType;
		destinationType = newDestinationType == null ? DESTINATION_TYPE_EDEFAULT : newDestinationType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_ROW__DESTINATION_TYPE, oldDestinationType, destinationType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getHeelVolume() {
		return heelVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHeelVolume(int newHeelVolume) {
		int oldHeelVolume = heelVolume;
		heelVolume = newHeelVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_ROW__HEEL_VOLUME, oldHeelVolume, heelVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIncludePortCosts() {
		return includePortCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIncludePortCosts(boolean newIncludePortCosts) {
		boolean oldIncludePortCosts = includePortCosts;
		includePortCosts = newIncludePortCosts;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SHIPPING_COST_ROW__INCLUDE_PORT_COSTS, oldIncludePortCosts, includePortCosts));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.SHIPPING_COST_ROW__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case AnalyticsPackage.SHIPPING_COST_ROW__DATE:
				return getDate();
			case AnalyticsPackage.SHIPPING_COST_ROW__CARGO_PRICE:
				return getCargoPrice();
			case AnalyticsPackage.SHIPPING_COST_ROW__CV_VALUE:
				return getCvValue();
			case AnalyticsPackage.SHIPPING_COST_ROW__DESTINATION_TYPE:
				return getDestinationType();
			case AnalyticsPackage.SHIPPING_COST_ROW__HEEL_VOLUME:
				return getHeelVolume();
			case AnalyticsPackage.SHIPPING_COST_ROW__INCLUDE_PORT_COSTS:
				return isIncludePortCosts();
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
			case AnalyticsPackage.SHIPPING_COST_ROW__PORT:
				setPort((Port)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__DATE:
				setDate((Date)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__CARGO_PRICE:
				setCargoPrice((Double)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__CV_VALUE:
				setCvValue((Double)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__DESTINATION_TYPE:
				setDestinationType((DestinationType)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__HEEL_VOLUME:
				setHeelVolume((Integer)newValue);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__INCLUDE_PORT_COSTS:
				setIncludePortCosts((Boolean)newValue);
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
			case AnalyticsPackage.SHIPPING_COST_ROW__PORT:
				setPort((Port)null);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__CARGO_PRICE:
				setCargoPrice(CARGO_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__CV_VALUE:
				setCvValue(CV_VALUE_EDEFAULT);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__DESTINATION_TYPE:
				setDestinationType(DESTINATION_TYPE_EDEFAULT);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__HEEL_VOLUME:
				setHeelVolume(HEEL_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.SHIPPING_COST_ROW__INCLUDE_PORT_COSTS:
				setIncludePortCosts(INCLUDE_PORT_COSTS_EDEFAULT);
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
			case AnalyticsPackage.SHIPPING_COST_ROW__PORT:
				return port != null;
			case AnalyticsPackage.SHIPPING_COST_ROW__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case AnalyticsPackage.SHIPPING_COST_ROW__CARGO_PRICE:
				return cargoPrice != CARGO_PRICE_EDEFAULT;
			case AnalyticsPackage.SHIPPING_COST_ROW__CV_VALUE:
				return cvValue != CV_VALUE_EDEFAULT;
			case AnalyticsPackage.SHIPPING_COST_ROW__DESTINATION_TYPE:
				return destinationType != DESTINATION_TYPE_EDEFAULT;
			case AnalyticsPackage.SHIPPING_COST_ROW__HEEL_VOLUME:
				return heelVolume != HEEL_VOLUME_EDEFAULT;
			case AnalyticsPackage.SHIPPING_COST_ROW__INCLUDE_PORT_COSTS:
				return includePortCosts != INCLUDE_PORT_COSTS_EDEFAULT;
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
		result.append(", cargoPrice: ");
		result.append(cargoPrice);
		result.append(", cvValue: ");
		result.append(cvValue);
		result.append(", destinationType: ");
		result.append(destinationType);
		result.append(", heelVolume: ");
		result.append(heelVolume);
		result.append(", includePortCosts: ");
		result.append(includePortCosts);
		result.append(')');
		return result.toString();
	}

} // end of ShippingCostRowImpl

// finish type fixing
