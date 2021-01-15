/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import java.time.LocalDate;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Paper Deal Allocation Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getValue <em>Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#isSettled <em>Settled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getExposures <em>Exposures</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocationEntry()
 * @model
 * @generated
 */
public interface PaperDealAllocationEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(LocalDate)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocationEntry_Date()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantity</em>' attribute.
	 * @see #setQuantity(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocationEntry_Quantity()
	 * @model
	 * @generated
	 */
	double getQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getQuantity <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity</em>' attribute.
	 * @see #getQuantity()
	 * @generated
	 */
	void setQuantity(double value);

	/**
	 * Returns the value of the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' attribute.
	 * @see #setPrice(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocationEntry_Price()
	 * @model
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(double value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocationEntry_Value()
	 * @model
	 * @generated
	 */
	double getValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(double value);

	/**
	 * Returns the value of the '<em><b>Settled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settled</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settled</em>' attribute.
	 * @see #setSettled(boolean)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocationEntry_Settled()
	 * @model
	 * @generated
	 */
	boolean isSettled();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry#isSettled <em>Settled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Settled</em>' attribute.
	 * @see #isSettled()
	 * @generated
	 */
	void setSettled(boolean value);

	/**
	 * Returns the value of the '<em><b>Exposures</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.ExposureDetail}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exposures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exposures</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocationEntry_Exposures()
	 * @model containment="true"
	 * @generated
	 */
	EList<ExposureDetail> getExposures();

} // PaperDealAllocationEntry
