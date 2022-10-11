/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.types.VolumeUnits;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Volume Tier Price Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters#getLowExpression <em>Low Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters#getHighExpression <em>High Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters#getVolumeLimitsUnit <em>Volume Limits Unit</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getVolumeTierPriceParameters()
 * @model annotation="http://minimaxlabs.com/models/commercial/slot/parameters"
 *        annotation="http://minimaxlabs.com/models/commercial/slot/expression allowExpressionOverride='false'"
 * @generated
 */
public interface VolumeTierPriceParameters extends LNGPriceCalculatorParameters {
	/**
	 * Returns the value of the '<em><b>Low Expression</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Low Expression</em>' attribute.
	 * @see #setLowExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getVolumeTierPriceParameters_LowExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getLowExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters#getLowExpression <em>Low Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Low Expression</em>' attribute.
	 * @see #getLowExpression()
	 * @generated
	 */
	void setLowExpression(String value);

	/**
	 * Returns the value of the '<em><b>High Expression</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>High Expression</em>' attribute.
	 * @see #setHighExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getVolumeTierPriceParameters_HighExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getHighExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters#getHighExpression <em>High Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>High Expression</em>' attribute.
	 * @see #getHighExpression()
	 * @generated
	 */
	void setHighExpression(String value);

	/**
	 * Returns the value of the '<em><b>Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Threshold</em>' attribute.
	 * @see #setThreshold(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getVolumeTierPriceParameters_Threshold()
	 * @model
	 * @generated
	 */
	double getThreshold();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters#getThreshold <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Threshold</em>' attribute.
	 * @see #getThreshold()
	 * @generated
	 */
	void setThreshold(double value);

	/**
	 * Returns the value of the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.VolumeUnits}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Limits Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #setVolumeLimitsUnit(VolumeUnits)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getVolumeTierPriceParameters_VolumeLimitsUnit()
	 * @model required="true"
	 * @generated
	 */
	VolumeUnits getVolumeLimitsUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters#getVolumeLimitsUnit <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Limits Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #getVolumeLimitsUnit()
	 * @generated
	 */
	void setVolumeLimitsUnit(VolumeUnits value);

} // VolumeTierPriceParameters
