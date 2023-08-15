/**
 */
package com.mmxlabs.models.lng.fleet;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CII Grade Boundary</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIGradeBoundary#getDwtUpperLimit <em>Dwt Upper Limit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIGradeBoundary#getGrade <em>Grade</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIGradeBoundary#getUpperLimit <em>Upper Limit</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIGradeBoundary()
 * @model
 * @generated
 */
public interface CIIGradeBoundary extends EObject {
	/**
	 * Returns the value of the '<em><b>Dwt Upper Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dwt Upper Limit</em>' attribute.
	 * @see #setDwtUpperLimit(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIGradeBoundary_DwtUpperLimit()
	 * @model
	 * @generated
	 */
	double getDwtUpperLimit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CIIGradeBoundary#getDwtUpperLimit <em>Dwt Upper Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dwt Upper Limit</em>' attribute.
	 * @see #getDwtUpperLimit()
	 * @generated
	 */
	void setDwtUpperLimit(double value);

	/**
	 * Returns the value of the '<em><b>Grade</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Grade</em>' attribute.
	 * @see #setGrade(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIGradeBoundary_Grade()
	 * @model
	 * @generated
	 */
	String getGrade();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CIIGradeBoundary#getGrade <em>Grade</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Grade</em>' attribute.
	 * @see #getGrade()
	 * @generated
	 */
	void setGrade(String value);

	/**
	 * Returns the value of the '<em><b>Upper Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Upper Limit</em>' attribute.
	 * @see #setUpperLimit(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIGradeBoundary_UpperLimit()
	 * @model
	 * @generated
	 */
	double getUpperLimit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CIIGradeBoundary#getUpperLimit <em>Upper Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Upper Limit</em>' attribute.
	 * @see #getUpperLimit()
	 * @generated
	 */
	void setUpperLimit(double value);

} // CIIGradeBoundary
