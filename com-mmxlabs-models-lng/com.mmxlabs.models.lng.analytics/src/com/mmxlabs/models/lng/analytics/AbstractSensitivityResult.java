/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Sensitivity Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getMinPnL <em>Min Pn L</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getMaxPnL <em>Max Pn L</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getAveragePnL <em>Average Pn L</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getVariance <em>Variance</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSensitivityResult()
 * @model abstract="true"
 * @generated
 */
public interface AbstractSensitivityResult extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Min Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Pn L</em>' attribute.
	 * @see #setMinPnL(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSensitivityResult_MinPnL()
	 * @model
	 * @generated
	 */
	long getMinPnL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getMinPnL <em>Min Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Pn L</em>' attribute.
	 * @see #getMinPnL()
	 * @generated
	 */
	void setMinPnL(long value);

	/**
	 * Returns the value of the '<em><b>Max Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Pn L</em>' attribute.
	 * @see #setMaxPnL(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSensitivityResult_MaxPnL()
	 * @model
	 * @generated
	 */
	long getMaxPnL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getMaxPnL <em>Max Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Pn L</em>' attribute.
	 * @see #getMaxPnL()
	 * @generated
	 */
	void setMaxPnL(long value);

	/**
	 * Returns the value of the '<em><b>Average Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Average Pn L</em>' attribute.
	 * @see #setAveragePnL(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSensitivityResult_AveragePnL()
	 * @model
	 * @generated
	 */
	long getAveragePnL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getAveragePnL <em>Average Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Average Pn L</em>' attribute.
	 * @see #getAveragePnL()
	 * @generated
	 */
	void setAveragePnL(long value);

	/**
	 * Returns the value of the '<em><b>Variance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variance</em>' attribute.
	 * @see #setVariance(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSensitivityResult_Variance()
	 * @model
	 * @generated
	 */
	double getVariance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getVariance <em>Variance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Variance</em>' attribute.
	 * @see #getVariance()
	 * @generated
	 */
	void setVariance(double value);

} // AbstractSensitivityResult
