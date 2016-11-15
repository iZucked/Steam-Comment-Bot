/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Break Even Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getPriceString <em>Price String</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getCargoPNL <em>Cargo PNL</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBreakEvenResult()
 * @model
 * @generated
 */
public interface BreakEvenResult extends AnalysisResultDetail {
	/**
	 * Returns the value of the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' attribute.
	 * @see #setPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBreakEvenResult_Price()
	 * @model
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(double value);

	/**
	 * Returns the value of the '<em><b>Price String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price String</em>' attribute.
	 * @see #setPriceString(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBreakEvenResult_PriceString()
	 * @model
	 * @generated
	 */
	String getPriceString();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getPriceString <em>Price String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price String</em>' attribute.
	 * @see #getPriceString()
	 * @generated
	 */
	void setPriceString(String value);

	/**
	 * Returns the value of the '<em><b>Cargo PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo PNL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo PNL</em>' attribute.
	 * @see #setCargoPNL(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBreakEvenResult_CargoPNL()
	 * @model
	 * @generated
	 */
	double getCargoPNL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getCargoPNL <em>Cargo PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo PNL</em>' attribute.
	 * @see #getCargoPNL()
	 * @generated
	 */
	void setCargoPNL(double value);

} // BreakEvenResult
