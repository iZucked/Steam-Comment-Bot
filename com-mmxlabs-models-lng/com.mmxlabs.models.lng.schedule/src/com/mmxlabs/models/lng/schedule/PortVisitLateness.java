/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Visit Lateness</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PortVisitLateness#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PortVisitLateness#getLatenessInHours <em>Lateness In Hours</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisitLateness()
 * @model
 * @generated
 */
public interface PortVisitLateness extends EObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.schedule.PortVisitLatenessType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.schedule.PortVisitLatenessType
	 * @see #setType(PortVisitLatenessType)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisitLateness_Type()
	 * @model
	 * @generated
	 */
	PortVisitLatenessType getType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PortVisitLateness#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.schedule.PortVisitLatenessType
	 * @see #getType()
	 * @generated
	 */
	void setType(PortVisitLatenessType value);

	/**
	 * Returns the value of the '<em><b>Lateness In Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lateness In Hours</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lateness In Hours</em>' attribute.
	 * @see #setLatenessInHours(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisitLateness_LatenessInHours()
	 * @model
	 * @generated
	 */
	int getLatenessInHours();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PortVisitLateness#getLatenessInHours <em>Lateness In Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lateness In Hours</em>' attribute.
	 * @see #getLatenessInHours()
	 * @generated
	 */
	void setLatenessInHours(int value);

} // PortVisitLateness
