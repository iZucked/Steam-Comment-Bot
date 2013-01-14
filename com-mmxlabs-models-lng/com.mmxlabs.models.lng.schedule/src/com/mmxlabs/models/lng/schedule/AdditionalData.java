/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Additional Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.AdditionalData#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.AdditionalData#getValue <em>Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.AdditionalData#getRender <em>Render</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getAdditionalData()
 * @model annotation="http://www.mmxlabs.com/models/mmxcore/validation/NamedObject nonUnique='true'"
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
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #isSetValue()
	 * @see #unsetValue()
	 * @see #setValue(Object)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getAdditionalData_Value()
	 * @model unsettable="true" dataType="com.mmxlabs.models.lng.schedule.Object" required="true"
	 * @generated
	 */
	Object getValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.AdditionalData#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #isSetValue()
	 * @see #unsetValue()
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Object value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.AdditionalData#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetValue()
	 * @see #getValue()
	 * @see #setValue(Object)
	 * @generated
	 */
	void unsetValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.AdditionalData#getValue <em>Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Value</em>' attribute is set.
	 * @see #unsetValue()
	 * @see #getValue()
	 * @see #setValue(Object)
	 * @generated
	 */
	boolean isSetValue();

	/**
	 * Returns the value of the '<em><b>Render</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Render</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Render</em>' attribute.
	 * @see #setRender(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getAdditionalData_Render()
	 * @model required="true"
	 * @generated
	 */
	String getRender();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.AdditionalData#getRender <em>Render</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Render</em>' attribute.
	 * @see #getRender()
	 * @generated
	 */
	void setRender(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final Object value = getValue();\n\nif (value instanceof Number) {\n\treturn ((Number)value).intValue();\n}\n\nthrow new IllegalStateException(\"Not an instanceof Number: \" + value);'"
	 * @generated
	 */
	int getIntValue();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final Object value = getValue();\nfinal String render = getRender();\n\nif (render != null && !render.isEmpty()) {\n\treturn String.format(render, value);\n} else {\n\treturn \"\" + value;\n}'"
	 * @generated
	 */
	String format();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final Object value = getValue();\n\nif (value instanceof Number) {\n\treturn ((Number)value).intValue();\n}\n\nreturn null;'"
	 * @generated
	 */
	Integer getIntegerValue();

} // end of  AdditionalData

// finish type fixing
