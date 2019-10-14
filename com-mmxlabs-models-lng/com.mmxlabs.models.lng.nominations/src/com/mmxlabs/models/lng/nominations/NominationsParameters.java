/**
 */
package com.mmxlabs.models.lng.nominations;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationsParameters#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationsParameters#getEndDate <em>End Date</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsParameters()
 * @model
 * @generated
 */
public interface NominationsParameters extends EObject {
	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #setStartDate(LocalDate)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsParameters_StartDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.NominationsParameters#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Date</em>' attribute.
	 * @see #setEndDate(LocalDate)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsParameters_EndDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEndDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.NominationsParameters#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Date</em>' attribute.
	 * @see #getEndDate()
	 * @generated
	 */
	void setEndDate(LocalDate value);

} // NominationsParameters
