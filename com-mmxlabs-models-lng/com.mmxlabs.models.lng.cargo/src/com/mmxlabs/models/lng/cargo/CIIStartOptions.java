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
 *   <li>{@link com.mmxlabs.models.lng.cargo.CIIStartOptions#getYearTodayEmissions <em>Year Today Emissions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CIIStartOptions#getYearTodayDistance <em>Year Today Distance</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCIIStartOptions()
 * @model
 * @generated
 */
public interface CIIStartOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Year Today Emissions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year Today Emissions</em>' attribute.
	 * @see #setYearTodayEmissions(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCIIStartOptions_YearTodayEmissions()
	 * @model
	 * @generated
	 */
	int getYearTodayEmissions();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CIIStartOptions#getYearTodayEmissions <em>Year Today Emissions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year Today Emissions</em>' attribute.
	 * @see #getYearTodayEmissions()
	 * @generated
	 */
	void setYearTodayEmissions(int value);

	/**
	 * Returns the value of the '<em><b>Year Today Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year Today Distance</em>' attribute.
	 * @see #setYearTodayDistance(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCIIStartOptions_YearTodayDistance()
	 * @model
	 * @generated
	 */
	int getYearTodayDistance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CIIStartOptions#getYearTodayDistance <em>Year Today Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year Today Distance</em>' attribute.
	 * @see #getYearTodayDistance()
	 * @generated
	 */
	void setYearTodayDistance(int value);

} // CIIStartOptions
