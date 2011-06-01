/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.market.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.market.MarketPackage;
import scenario.market.StepwisePrice;
import scenario.market.StepwisePriceCurve;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Stepwise Price Curve</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.market.impl.StepwisePriceCurveImpl#getPrices <em>Prices</em>}</li>
 *   <li>{@link scenario.market.impl.StepwisePriceCurveImpl#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StepwisePriceCurveImpl extends EObjectImpl implements StepwisePriceCurve {
	/**
	 * The cached value of the '{@link #getPrices() <em>Prices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrices()
	 * @generated
	 * @ordered
	 */
	protected EList<StepwisePrice> prices;

	/**
	 * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected static final float DEFAULT_VALUE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected float defaultValue = DEFAULT_VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StepwisePriceCurveImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MarketPackage.Literals.STEPWISE_PRICE_CURVE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<StepwisePrice> getPrices() {
		if (prices == null) {
			prices = new EObjectContainmentEList<StepwisePrice>(StepwisePrice.class, this, MarketPackage.STEPWISE_PRICE_CURVE__PRICES);
		}
		return prices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getDefaultValue() {
		return defaultValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultValue(float newDefaultValue) {
		float oldDefaultValue = defaultValue;
		defaultValue = newDefaultValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MarketPackage.STEPWISE_PRICE_CURVE__DEFAULT_VALUE, oldDefaultValue, defaultValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getValueAtDate(Date date) {
		final EList<StepwisePrice> prices = getPrices();
		
		java.util.Collections.sort(prices,
		new java.util.Comparator<StepwisePrice>() {
			public int compare(StepwisePrice a, StepwisePrice b) {
				return a.getDate().compareTo(b.getDate());
			}
		});
		
		float previousPrice = getDefaultValue();
		for (final StepwisePrice p : prices) {
			if (p.getDate().after(date)) {
				return previousPrice;
			}
			previousPrice = p.getPriceFromDate();
		}
		return previousPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MarketPackage.STEPWISE_PRICE_CURVE__PRICES:
				return ((InternalEList<?>)getPrices()).basicRemove(otherEnd, msgs);
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
			case MarketPackage.STEPWISE_PRICE_CURVE__PRICES:
				return getPrices();
			case MarketPackage.STEPWISE_PRICE_CURVE__DEFAULT_VALUE:
				return getDefaultValue();
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
			case MarketPackage.STEPWISE_PRICE_CURVE__PRICES:
				getPrices().clear();
				getPrices().addAll((Collection<? extends StepwisePrice>)newValue);
				return;
			case MarketPackage.STEPWISE_PRICE_CURVE__DEFAULT_VALUE:
				setDefaultValue((Float)newValue);
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
			case MarketPackage.STEPWISE_PRICE_CURVE__PRICES:
				getPrices().clear();
				return;
			case MarketPackage.STEPWISE_PRICE_CURVE__DEFAULT_VALUE:
				setDefaultValue(DEFAULT_VALUE_EDEFAULT);
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
			case MarketPackage.STEPWISE_PRICE_CURVE__PRICES:
				return prices != null && !prices.isEmpty();
			case MarketPackage.STEPWISE_PRICE_CURVE__DEFAULT_VALUE:
				return defaultValue != DEFAULT_VALUE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case MarketPackage.STEPWISE_PRICE_CURVE___GET_VALUE_AT_DATE__DATE:
				return getValueAtDate((Date)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (defaultValue: ");
		result.append(defaultValue);
		result.append(')');
		return result.toString();
	}

} //StepwisePriceCurveImpl
