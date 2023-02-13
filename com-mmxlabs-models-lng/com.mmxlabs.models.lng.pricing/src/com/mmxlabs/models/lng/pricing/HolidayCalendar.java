/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.mmxcore.NamedObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Holiday Calendar</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.HolidayCalendar#getEntries <em>Entries</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.HolidayCalendar#getDescription <em>Description</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getHolidayCalendar()
 * @model
 * @generated
 */
public interface HolidayCalendar extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getHolidayCalendar_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<HolidayCalendarEntry> getEntries();

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getHolidayCalendar_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.HolidayCalendar#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

} // HolidayCalendar
