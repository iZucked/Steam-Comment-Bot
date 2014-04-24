/**
 */
package com.mmxlabs.models.lng.spotmarkets;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out Start Date</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate#getCharterOutStartDate <em>Charter Out Start Date</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutStartDate()
 * @model
 * @generated
 */
public interface CharterOutStartDate extends EObject {
	/**
	 * Returns the value of the '<em><b>Charter Out Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out Start Date</em>' attribute.
	 * @see #setCharterOutStartDate(Date)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutStartDate_CharterOutStartDate()
	 * @model annotation="http://www.mmxlabs.com/models/lng/ui/datetime showTime='false'"
	 * @generated
	 */
	Date getCharterOutStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate#getCharterOutStartDate <em>Charter Out Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out Start Date</em>' attribute.
	 * @see #getCharterOutStartDate()
	 * @generated
	 */
	void setCharterOutStartDate(Date value);

} // CharterOutStartDate
