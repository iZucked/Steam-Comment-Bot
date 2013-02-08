

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fixed Price Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.FixedPriceParameters#getPricePerMMBTU <em>Price Per MMBTU</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getFixedPriceParameters()
 * @model
 * @generated
 */
public interface FixedPriceParameters extends LNGPriceCalculatorParameters {
	/**
	 * Returns the value of the '<em><b>Price Per MMBTU</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Per MMBTU</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Per MMBTU</em>' attribute.
	 * @see #setPricePerMMBTU(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getFixedPriceParameters_PricePerMMBTU()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getPricePerMMBTU();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.FixedPriceParameters#getPricePerMMBTU <em>Price Per MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Per MMBTU</em>' attribute.
	 * @see #getPricePerMMBTU()
	 * @generated
	 */
	void setPricePerMMBTU(double value);

} // end of  FixedPriceParameters

// finish type fixing
