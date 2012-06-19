

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.types.AIndex;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Netback Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getNotionalBallastParameters <em>Notional Ballast Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getConstant <em>Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getMultiplier <em>Multiplier</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract()
 * @model
 * @generated
 */
public interface NetbackPurchaseContract extends PurchaseContract {
	/**
	 * Returns the value of the '<em><b>Notional Ballast Parameters</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notional Ballast Parameters</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notional Ballast Parameters</em>' containment reference.
	 * @see #setNotionalBallastParameters(NotionalBallastParameters)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract_NotionalBallastParameters()
	 * @model containment="true" required="true"
	 * @generated
	 */
	NotionalBallastParameters getNotionalBallastParameters();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getNotionalBallastParameters <em>Notional Ballast Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notional Ballast Parameters</em>' containment reference.
	 * @see #getNotionalBallastParameters()
	 * @generated
	 */
	void setNotionalBallastParameters(NotionalBallastParameters value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract_Index()
	 * @model required="true"
	 * @generated
	 */
	AIndex getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(AIndex value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract_Constant()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getConstant();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getConstant <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constant</em>' attribute.
	 * @see #getConstant()
	 * @generated
	 */
	void setConstant(double value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract_Multiplier()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getMultiplier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getMultiplier <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplier</em>' attribute.
	 * @see #getMultiplier()
	 * @generated
	 */
	void setMultiplier(double value);

} // end of  NetbackPurchaseContract

// finish type fixing
