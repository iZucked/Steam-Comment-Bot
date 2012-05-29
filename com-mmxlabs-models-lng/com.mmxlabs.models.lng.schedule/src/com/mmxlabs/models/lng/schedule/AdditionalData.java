

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.mmxcore.NamedObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Additional Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.AdditionalData#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.AdditionalData#getIntegerValue <em>Integer Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getAdditionalData()
 * @model
 * @generated
 */
public interface AdditionalData extends NamedObject, AdditionalDataHolder {
	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getAdditionalData_Key()
	 * @model required="true"
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.AdditionalData#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Integer Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Integer Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Integer Value</em>' attribute.
	 * @see #isSetIntegerValue()
	 * @see #unsetIntegerValue()
	 * @see #setIntegerValue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getAdditionalData_IntegerValue()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getIntegerValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.AdditionalData#getIntegerValue <em>Integer Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Integer Value</em>' attribute.
	 * @see #isSetIntegerValue()
	 * @see #unsetIntegerValue()
	 * @see #getIntegerValue()
	 * @generated
	 */
	void setIntegerValue(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.AdditionalData#getIntegerValue <em>Integer Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIntegerValue()
	 * @see #getIntegerValue()
	 * @see #setIntegerValue(int)
	 * @generated
	 */
	void unsetIntegerValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.AdditionalData#getIntegerValue <em>Integer Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Integer Value</em>' attribute is set.
	 * @see #unsetIntegerValue()
	 * @see #getIntegerValue()
	 * @see #setIntegerValue(int)
	 * @generated
	 */
	boolean isSetIntegerValue();

} // end of  AdditionalData

// finish type fixing
