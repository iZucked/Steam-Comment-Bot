/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Break Even Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenResultImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenResultImpl#getPriceString <em>Price String</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenResultImpl#getCargoPNL <em>Cargo PNL</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BreakEvenResultImpl extends AnalysisResultDetailImpl implements BreakEvenResult {
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
	 * The default value of the '{@link #getPriceString() <em>Price String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceString()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_STRING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriceString() <em>Price String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceString()
	 * @generated
	 * @ordered
	 */
	protected String priceString = PRICE_STRING_EDEFAULT;

	/**
	 * The default value of the '{@link #getCargoPNL() <em>Cargo PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoPNL()
	 * @generated
	 * @ordered
	 */
	protected static final double CARGO_PNL_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCargoPNL() <em>Cargo PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoPNL()
	 * @generated
	 * @ordered
	 */
	protected double cargoPNL = CARGO_PNL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BreakEvenResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BREAK_EVEN_RESULT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_RESULT__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPriceString() {
		return priceString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriceString(String newPriceString) {
		String oldPriceString = priceString;
		priceString = newPriceString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_RESULT__PRICE_STRING, oldPriceString, priceString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCargoPNL() {
		return cargoPNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoPNL(double newCargoPNL) {
		double oldCargoPNL = cargoPNL;
		cargoPNL = newCargoPNL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_RESULT__CARGO_PNL, oldCargoPNL, cargoPNL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.BREAK_EVEN_RESULT__PRICE:
				return getPrice();
			case AnalyticsPackage.BREAK_EVEN_RESULT__PRICE_STRING:
				return getPriceString();
			case AnalyticsPackage.BREAK_EVEN_RESULT__CARGO_PNL:
				return getCargoPNL();
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
			case AnalyticsPackage.BREAK_EVEN_RESULT__PRICE:
				setPrice((Double)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_RESULT__PRICE_STRING:
				setPriceString((String)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_RESULT__CARGO_PNL:
				setCargoPNL((Double)newValue);
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
			case AnalyticsPackage.BREAK_EVEN_RESULT__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.BREAK_EVEN_RESULT__PRICE_STRING:
				setPriceString(PRICE_STRING_EDEFAULT);
				return;
			case AnalyticsPackage.BREAK_EVEN_RESULT__CARGO_PNL:
				setCargoPNL(CARGO_PNL_EDEFAULT);
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
			case AnalyticsPackage.BREAK_EVEN_RESULT__PRICE:
				return price != PRICE_EDEFAULT;
			case AnalyticsPackage.BREAK_EVEN_RESULT__PRICE_STRING:
				return PRICE_STRING_EDEFAULT == null ? priceString != null : !PRICE_STRING_EDEFAULT.equals(priceString);
			case AnalyticsPackage.BREAK_EVEN_RESULT__CARGO_PNL:
				return cargoPNL != CARGO_PNL_EDEFAULT;
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
		result.append(" (price: ");
		result.append(price);
		result.append(", priceString: ");
		result.append(priceString);
		result.append(", cargoPNL: ");
		result.append(cargoPNL);
		result.append(')');
		return result.toString();
	}

} //BreakEvenResultImpl
