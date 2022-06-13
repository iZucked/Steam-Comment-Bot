/**
 */
package com.mmxlabs.models.lng.adp;

import java.time.LocalDate;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Allowed Arrival Time Record</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.AllowedArrivalTimeRecord#getPeriodStart <em>Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.AllowedArrivalTimeRecord#getAllowedTimes <em>Allowed Times</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getAllowedArrivalTimeRecord()
 * @model
 * @generated
 */
public interface AllowedArrivalTimeRecord extends EObject {
	/**
	 * Returns the value of the '<em><b>Period Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period Start</em>' attribute.
	 * @see #setPeriodStart(LocalDate)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getAllowedArrivalTimeRecord_PeriodStart()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPeriodStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.AllowedArrivalTimeRecord#getPeriodStart <em>Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period Start</em>' attribute.
	 * @see #getPeriodStart()
	 * @generated
	 */
	void setPeriodStart(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Allowed Times</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Times</em>' attribute list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getAllowedArrivalTimeRecord_AllowedTimes()
	 * @model
	 * @generated
	 */
	EList<Integer> getAllowedTimes();

} // AllowedArrivalTimeRecord
