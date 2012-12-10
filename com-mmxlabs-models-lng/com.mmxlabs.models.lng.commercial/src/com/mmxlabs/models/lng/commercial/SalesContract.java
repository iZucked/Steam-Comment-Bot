/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.types.ASalesContract;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sales Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.SalesContract#getMinCvValue <em>Min Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.SalesContract#getMaxCvValue <em>Max Cv Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getSalesContract()
 * @model abstract="true"
 * @generated
 */
public interface SalesContract extends Contract, ASalesContract {

	/**
	 * Returns the value of the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Cv Value</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Cv Value</em>' attribute.
	 * @see #isSetMinCvValue()
	 * @see #unsetMinCvValue()
	 * @see #setMinCvValue(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getSalesContract_MinCvValue()
	 * @model unsettable="true"
	 * @generated
	 */
	double getMinCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.SalesContract#getMinCvValue <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Cv Value</em>' attribute.
	 * @see #isSetMinCvValue()
	 * @see #unsetMinCvValue()
	 * @see #getMinCvValue()
	 * @generated
	 */
	void setMinCvValue(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.SalesContract#getMinCvValue <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #isSetMinCvValue()
	 * @see #getMinCvValue()
	 * @see #setMinCvValue(double)
	 * @generated
	 */
	void unsetMinCvValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.SalesContract#getMinCvValue <em>Min Cv Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Cv Value</em>' attribute is set.
	 * @see #unsetMinCvValue()
	 * @see #getMinCvValue()
	 * @see #setMinCvValue(double)
	 * @generated
	 */
	boolean isSetMinCvValue();

	/**
	 * Returns the value of the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Cv Value</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Cv Value</em>' attribute.
	 * @see #isSetMaxCvValue()
	 * @see #unsetMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getSalesContract_MaxCvValue()
	 * @model unsettable="true"
	 * @generated
	 */
	double getMaxCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.SalesContract#getMaxCvValue <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Cv Value</em>' attribute.
	 * @see #isSetMaxCvValue()
	 * @see #unsetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @generated
	 */
	void setMaxCvValue(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.SalesContract#getMaxCvValue <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #isSetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @generated
	 */
	void unsetMaxCvValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.SalesContract#getMaxCvValue <em>Max Cv Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Cv Value</em>' attribute is set.
	 * @see #unsetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @generated
	 */
	boolean isSetMaxCvValue();
} // end of  SalesContract

// finish type fixing
