/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations;

import java.time.LocalDate;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Nomination</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getNomineeId <em>Nominee Id</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getDueDate <em>Due Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNomination#isDone <em>Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getAlertDate <em>Alert Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getSpecUuid <em>Spec Uuid</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNomination()
 * @model abstract="true"
 * @generated
 */
public interface AbstractNomination extends AbstractNominationSpec {
	/**
	 * Returns the value of the '<em><b>Nominee Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nominee Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nominee Id</em>' attribute.
	 * @see #setNomineeId(String)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNomination_NomineeId()
	 * @model
	 * @generated
	 */
	String getNomineeId();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getNomineeId <em>Nominee Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nominee Id</em>' attribute.
	 * @see #getNomineeId()
	 * @generated
	 */
	void setNomineeId(String value);

	/**
	 * Returns the value of the '<em><b>Due Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Due Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Due Date</em>' attribute.
	 * @see #isSetDueDate()
	 * @see #unsetDueDate()
	 * @see #setDueDate(LocalDate)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNomination_DueDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getDueDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getDueDate <em>Due Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Due Date</em>' attribute.
	 * @see #isSetDueDate()
	 * @see #unsetDueDate()
	 * @see #getDueDate()
	 * @generated
	 */
	void setDueDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getDueDate <em>Due Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDueDate()
	 * @see #getDueDate()
	 * @see #setDueDate(LocalDate)
	 * @generated
	 */
	void unsetDueDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getDueDate <em>Due Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Due Date</em>' attribute is set.
	 * @see #unsetDueDate()
	 * @see #getDueDate()
	 * @see #setDueDate(LocalDate)
	 * @generated
	 */
	boolean isSetDueDate();

	/**
	 * Returns the value of the '<em><b>Done</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Done</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Done</em>' attribute.
	 * @see #setDone(boolean)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNomination_Done()
	 * @model
	 * @generated
	 */
	boolean isDone();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#isDone <em>Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Done</em>' attribute.
	 * @see #isDone()
	 * @generated
	 */
	void setDone(boolean value);

	/**
	 * Returns the value of the '<em><b>Alert Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alert Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alert Date</em>' attribute.
	 * @see #isSetAlertDate()
	 * @see #unsetAlertDate()
	 * @see #setAlertDate(LocalDate)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNomination_AlertDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getAlertDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getAlertDate <em>Alert Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alert Date</em>' attribute.
	 * @see #isSetAlertDate()
	 * @see #unsetAlertDate()
	 * @see #getAlertDate()
	 * @generated
	 */
	void setAlertDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getAlertDate <em>Alert Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAlertDate()
	 * @see #getAlertDate()
	 * @see #setAlertDate(LocalDate)
	 * @generated
	 */
	void unsetAlertDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getAlertDate <em>Alert Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Alert Date</em>' attribute is set.
	 * @see #unsetAlertDate()
	 * @see #getAlertDate()
	 * @see #setAlertDate(LocalDate)
	 * @generated
	 */
	boolean isSetAlertDate();

	/**
	 * Returns the value of the '<em><b>Spec Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spec Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spec Uuid</em>' attribute.
	 * @see #isSetSpecUuid()
	 * @see #unsetSpecUuid()
	 * @see #setSpecUuid(String)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNomination_SpecUuid()
	 * @model unsettable="true"
	 * @generated
	 */
	String getSpecUuid();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getSpecUuid <em>Spec Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spec Uuid</em>' attribute.
	 * @see #isSetSpecUuid()
	 * @see #unsetSpecUuid()
	 * @see #getSpecUuid()
	 * @generated
	 */
	void setSpecUuid(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getSpecUuid <em>Spec Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSpecUuid()
	 * @see #getSpecUuid()
	 * @see #setSpecUuid(String)
	 * @generated
	 */
	void unsetSpecUuid();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNomination#getSpecUuid <em>Spec Uuid</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Spec Uuid</em>' attribute is set.
	 * @see #unsetSpecUuid()
	 * @see #getSpecUuid()
	 * @see #setSpecUuid(String)
	 * @generated
	 */
	boolean isSetSpecUuid();

} // AbstractNomination
