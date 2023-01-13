/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import java.time.LocalDateTime;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Des Spacing Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.DesSpacingRow#getMinDischargeDate <em>Min Discharge Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.DesSpacingRow#getMaxDischargeDate <em>Max Discharge Date</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDesSpacingRow()
 * @model
 * @generated
 */
public interface DesSpacingRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Min Discharge Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Discharge Date</em>' attribute.
	 * @see #isSetMinDischargeDate()
	 * @see #unsetMinDischargeDate()
	 * @see #setMinDischargeDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDesSpacingRow_MinDischargeDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getMinDischargeDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DesSpacingRow#getMinDischargeDate <em>Min Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Discharge Date</em>' attribute.
	 * @see #isSetMinDischargeDate()
	 * @see #unsetMinDischargeDate()
	 * @see #getMinDischargeDate()
	 * @generated
	 */
	void setMinDischargeDate(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.adp.DesSpacingRow#getMinDischargeDate <em>Min Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinDischargeDate()
	 * @see #getMinDischargeDate()
	 * @see #setMinDischargeDate(LocalDateTime)
	 * @generated
	 */
	void unsetMinDischargeDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.adp.DesSpacingRow#getMinDischargeDate <em>Min Discharge Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Discharge Date</em>' attribute is set.
	 * @see #unsetMinDischargeDate()
	 * @see #getMinDischargeDate()
	 * @see #setMinDischargeDate(LocalDateTime)
	 * @generated
	 */
	boolean isSetMinDischargeDate();

	/**
	 * Returns the value of the '<em><b>Max Discharge Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Discharge Date</em>' attribute.
	 * @see #isSetMaxDischargeDate()
	 * @see #unsetMaxDischargeDate()
	 * @see #setMaxDischargeDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDesSpacingRow_MaxDischargeDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getMaxDischargeDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DesSpacingRow#getMaxDischargeDate <em>Max Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Discharge Date</em>' attribute.
	 * @see #isSetMaxDischargeDate()
	 * @see #unsetMaxDischargeDate()
	 * @see #getMaxDischargeDate()
	 * @generated
	 */
	void setMaxDischargeDate(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.adp.DesSpacingRow#getMaxDischargeDate <em>Max Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxDischargeDate()
	 * @see #getMaxDischargeDate()
	 * @see #setMaxDischargeDate(LocalDateTime)
	 * @generated
	 */
	void unsetMaxDischargeDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.adp.DesSpacingRow#getMaxDischargeDate <em>Max Discharge Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Discharge Date</em>' attribute is set.
	 * @see #unsetMaxDischargeDate()
	 * @see #getMaxDischargeDate()
	 * @see #setMaxDischargeDate(LocalDateTime)
	 * @generated
	 */
	boolean isSetMaxDischargeDate();

} // DesSpacingRow
