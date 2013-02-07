

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.pricing.Index;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LNG Index Price Parameters</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters#getMultiplier <em>Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters#getConstant <em>Constant</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getLNGIndexPriceParameters()
 * @model
 * @generated
 */
public interface LNGIndexPriceParameters extends LNGPriceCalculatorParameters {
	/**
	 * Returns the value of the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' reference.
	 * @see #setIndex(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getLNGIndexPriceParameters_Index()
	 * @model required="true"
	 * @generated
	 */
	Index getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(Index value);

	/**
	 * Returns the value of the '<em><b>Multiplier</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiplier</em>' attribute.
	 * @see #setMultiplier(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getLNGIndexPriceParameters_Multiplier()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getMultiplier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters#getMultiplier <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplier</em>' attribute.
	 * @see #getMultiplier()
	 * @generated
	 */
	void setMultiplier(double value);

	/**
	 * Returns the value of the '<em><b>Constant</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constant</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constant</em>' attribute.
	 * @see #setConstant(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getLNGIndexPriceParameters_Constant()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getConstant();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters#getConstant <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constant</em>' attribute.
	 * @see #getConstant()
	 * @generated
	 */
	void setConstant(double value);

} // end of  LNGIndexPriceParameters

// finish type fixing
