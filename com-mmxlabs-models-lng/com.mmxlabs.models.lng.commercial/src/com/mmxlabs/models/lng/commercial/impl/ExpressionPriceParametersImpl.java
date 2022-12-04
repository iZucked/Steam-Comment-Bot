/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.PreferredPricingBasesWrapper;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expression Price Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ExpressionPriceParametersImpl#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ExpressionPriceParametersImpl#getPricingBasis <em>Pricing Basis</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ExpressionPriceParametersImpl#getPreferredPBs <em>Preferred PBs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExpressionPriceParametersImpl extends LNGPriceCalculatorParametersImpl implements ExpressionPriceParameters {
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
	 * The default value of the '{@link #getPricingBasis() <em>Pricing Basis</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingBasis()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICING_BASIS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPricingBasis() <em>Pricing Basis</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingBasis()
	 * @generated
	 * @ordered
	 */
	protected String pricingBasis = PRICING_BASIS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPreferredPBs() <em>Preferred PBs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreferredPBs()
	 * @generated
	 * @ordered
	 */
	protected EList<PreferredPricingBasesWrapper> preferredPBs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExpressionPriceParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION, oldPriceExpression, priceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPricingBasis() {
		return pricingBasis;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingBasis(String newPricingBasis) {
		String oldPricingBasis = pricingBasis;
		pricingBasis = newPricingBasis;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICING_BASIS, oldPricingBasis, pricingBasis));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PreferredPricingBasesWrapper> getPreferredPBs() {
		if (preferredPBs == null) {
			preferredPBs = new EObjectContainmentEList<PreferredPricingBasesWrapper>(PreferredPricingBasesWrapper.class, this, CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PREFERRED_PBS);
		}
		return preferredPBs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PREFERRED_PBS:
				return ((InternalEList<?>)getPreferredPBs()).basicRemove(otherEnd, msgs);
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
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION:
				return getPriceExpression();
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICING_BASIS:
				return getPricingBasis();
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PREFERRED_PBS:
				return getPreferredPBs();
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
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
				return;
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICING_BASIS:
				setPricingBasis((String)newValue);
				return;
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PREFERRED_PBS:
				getPreferredPBs().clear();
				getPreferredPBs().addAll((Collection<? extends PreferredPricingBasesWrapper>)newValue);
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
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION:
				setPriceExpression(PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICING_BASIS:
				setPricingBasis(PRICING_BASIS_EDEFAULT);
				return;
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PREFERRED_PBS:
				getPreferredPBs().clear();
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
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION:
				return PRICE_EXPRESSION_EDEFAULT == null ? priceExpression != null : !PRICE_EXPRESSION_EDEFAULT.equals(priceExpression);
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PRICING_BASIS:
				return PRICING_BASIS_EDEFAULT == null ? pricingBasis != null : !PRICING_BASIS_EDEFAULT.equals(pricingBasis);
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS__PREFERRED_PBS:
				return preferredPBs != null && !preferredPBs.isEmpty();
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (priceExpression: ");
		result.append(priceExpression);
		result.append(", pricingBasis: ");
		result.append(pricingBasis);
		result.append(')');
		return result.toString();
	}

} // end of ExpressionPriceParametersImpl

// finish type fixing
