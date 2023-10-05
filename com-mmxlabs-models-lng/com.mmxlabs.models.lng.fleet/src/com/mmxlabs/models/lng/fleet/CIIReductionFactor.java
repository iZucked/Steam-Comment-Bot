/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.fleet;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CII Reduction Factor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIReductionFactor#getYear <em>Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIReductionFactor#getPercentage <em>Percentage</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIReductionFactor#getRemark <em>Remark</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIReductionFactor()
 * @model
 * @generated
 */
public interface CIIReductionFactor extends EObject {
	/**
	 * Returns the value of the '<em><b>Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year</em>' attribute.
	 * @see #setYear(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIReductionFactor_Year()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='####'"
	 * @generated
	 */
	int getYear();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CIIReductionFactor#getYear <em>Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year</em>' attribute.
	 * @see #getYear()
	 * @generated
	 */
	void setYear(int value);

	/**
	 * Returns the value of the '<em><b>Percentage</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Percentage</em>' attribute.
	 * @see #setPercentage(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIReductionFactor_Percentage()
	 * @model
	 * @generated
	 */
	int getPercentage();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CIIReductionFactor#getPercentage <em>Percentage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Percentage</em>' attribute.
	 * @see #getPercentage()
	 * @generated
	 */
	void setPercentage(int value);

	/**
	 * Returns the value of the '<em><b>Remark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Remark</em>' attribute.
	 * @see #setRemark(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIReductionFactor_Remark()
	 * @model
	 * @generated
	 */
	String getRemark();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CIIReductionFactor#getRemark <em>Remark</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remark</em>' attribute.
	 * @see #getRemark()
	 * @generated
	 */
	void setRemark(String value);

} // CIIReductionFactor
