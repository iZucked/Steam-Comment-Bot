/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Year Month Curve</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getCurrencyUnit <em>Currency Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getVolumeUnit <em>Volume Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getExpression <em>Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getAbstractYearMonthCurve()
 * @model abstract="true"
 * @generated
 */
public interface AbstractYearMonthCurve extends YearMonthPointContainer {
	/**
	 * Returns the value of the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Currency Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Currency Unit</em>' attribute.
	 * @see #setCurrencyUnit(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getAbstractYearMonthCurve_CurrencyUnit()
	 * @model
	 * @generated
	 */
	String getCurrencyUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getCurrencyUnit <em>Currency Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Currency Unit</em>' attribute.
	 * @see #getCurrencyUnit()
	 * @generated
	 */
	void setCurrencyUnit(String value);

	/**
	 * Returns the value of the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Unit</em>' attribute.
	 * @see #setVolumeUnit(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getAbstractYearMonthCurve_VolumeUnit()
	 * @model
	 * @generated
	 */
	String getVolumeUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getVolumeUnit <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Unit</em>' attribute.
	 * @see #getVolumeUnit()
	 * @generated
	 */
	void setVolumeUnit(String value);

	/**
	 * Returns the value of the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' attribute.
	 * @see #isSetExpression()
	 * @see #unsetExpression()
	 * @see #setExpression(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getAbstractYearMonthCurve_Expression()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	String getExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getExpression <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' attribute.
	 * @see #isSetExpression()
	 * @see #unsetExpression()
	 * @see #getExpression()
	 * @generated
	 */
	void setExpression(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getExpression <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetExpression()
	 * @see #getExpression()
	 * @see #setExpression(String)
	 * @generated
	 */
	void unsetExpression();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getExpression <em>Expression</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Expression</em>' attribute is set.
	 * @see #unsetExpression()
	 * @see #getExpression()
	 * @see #setExpression(String)
	 * @generated
	 */
	boolean isSetExpression();

} // AbstractYearMonthCurve
