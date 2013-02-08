/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.types.AIndex;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>AIndex Price Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getIndex <em>AIndex</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getMultiplier <em>Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getConstant <em>Constant</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getIndexPriceContract()
 * @model
 * @generated
 */
public interface IndexPriceContract extends SalesContract, PurchaseContract {
	/**
	 * Returns the value of the '<em><b>AIndex</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AIndex</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AIndex</em>' reference.
	 * @see #setIndex(AIndex)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getIndexPriceContract_Index()
	 * @model required="true"
	 * @generated
	 */
	AIndex getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getIndex <em>AIndex</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>AIndex</em>' reference.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getIndexPriceContract_Constant()
	 * @model default="0" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='-#,##0.##'"
	 * @generated
	 */
	double getConstant();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getConstant <em>Constant</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getIndexPriceContract_Multiplier()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getMultiplier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getMultiplier <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplier</em>' attribute.
	 * @see #getMultiplier()
	 * @generated
	 */
	void setMultiplier(double value);

} // end of  IndexPriceContract

// finish type fixing
