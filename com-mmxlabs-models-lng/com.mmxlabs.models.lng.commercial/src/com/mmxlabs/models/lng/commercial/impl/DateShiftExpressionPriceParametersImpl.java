/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Date Shift Expression Price Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.DateShiftExpressionPriceParametersImpl#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.DateShiftExpressionPriceParametersImpl#isSpecificDay <em>Specific Day</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.DateShiftExpressionPriceParametersImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DateShiftExpressionPriceParametersImpl extends LNGPriceCalculatorParametersImpl implements DateShiftExpressionPriceParameters {
	/**
	 * The default value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_EXPRESSION_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String priceExpression = PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #isSpecificDay() <em>Specific Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSpecificDay()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SPECIFIC_DAY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSpecificDay() <em>Specific Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSpecificDay()
	 * @generated
	 * @ordered
	 */
	protected boolean specificDay = SPECIFIC_DAY_EDEFAULT;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final int VALUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected int value = VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DateShiftExpressionPriceParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION, oldPriceExpression, priceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSpecificDay() {
		return specificDay;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpecificDay(boolean newSpecificDay) {
		boolean oldSpecificDay = specificDay;
		specificDay = newSpecificDay;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY, oldSpecificDay, specificDay));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(int newValue) {
		int oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION:
				return getPriceExpression();
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY:
				return isSpecificDay();
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE:
				return getValue();
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
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
				return;
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY:
				setSpecificDay((Boolean)newValue);
				return;
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE:
				setValue((Integer)newValue);
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
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION:
				setPriceExpression(PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY:
				setSpecificDay(SPECIFIC_DAY_EDEFAULT);
				return;
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE:
				setValue(VALUE_EDEFAULT);
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
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION:
				return PRICE_EXPRESSION_EDEFAULT == null ? priceExpression != null : !PRICE_EXPRESSION_EDEFAULT.equals(priceExpression);
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY:
				return specificDay != SPECIFIC_DAY_EDEFAULT;
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE:
				return value != VALUE_EDEFAULT;
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
		result.append(" (priceExpression: ");
		result.append(priceExpression);
		result.append(", specificDay: ");
		result.append(specificDay);
		result.append(", value: ");
		result.append(value);
		result.append(')');
		return result.toString();
	}

} //DateShiftExpressionPriceParametersImpl
