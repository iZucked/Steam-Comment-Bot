/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CII Start Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CIIStartOptions#getYearToDateEmissions <em>Year To Date Emissions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CIIStartOptions#getYearToDateDistance <em>Year To Date Distance</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCIIStartOptions()
 * @model
 * @generated
 */
public interface CIIStartOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Year To Date Emissions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year To Date Emissions</em>' attribute.
	 * @see #setYearToDateEmissions(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCIIStartOptions_YearToDateEmissions()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#######'"
	 * @generated
	 */
	int getYearToDateEmissions();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CIIStartOptions#getYearToDateEmissions <em>Year To Date Emissions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year To Date Emissions</em>' attribute.
	 * @see #getYearToDateEmissions()
	 * @generated
	 */
	void setYearToDateEmissions(int value);

	/**
	 * Returns the value of the '<em><b>Year To Date Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year To Date Distance</em>' attribute.
	 * @see #setYearToDateDistance(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCIIStartOptions_YearToDateDistance()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#####'"
	 * @generated
	 */
	int getYearToDateDistance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CIIStartOptions#getYearToDateDistance <em>Year To Date Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year To Date Distance</em>' attribute.
	 * @see #getYearToDateDistance()
	 * @generated
	 */
	void setYearToDateDistance(int value);

} // CIIStartOptions
