/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import java.io.Serializable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extra Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getValue <em>Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraData()
 * @model
 * @generated
 */
public interface ExtraData extends ExtraDataContainer {
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
	 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraData_Key()
	 * @model required="true"
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraData_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * @see #setValue(Serializable)
	 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraData_Value()
	 * @model unsettable="true" dataType="com.mmxlabs.models.lng.types.SerializableObject" required="true"
	 * @generated
	 */
	Serializable getValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #isSetValue()
	 * @see #unsetValue()
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Serializable value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetValue()
	 * @see #getValue()
	 * @see #setValue(Serializable)
	 * @generated
	 */
	void unsetValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getValue <em>Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Value</em>' attribute is set.
	 * @see #unsetValue()
	 * @see #getValue()
	 * @see #setValue(Serializable)
	 * @generated
	 */
	boolean isSetValue();

	/**
	 * Returns the value of the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Format</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Format</em>' attribute.
	 * @see #setFormat(String)
	 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraData_Format()
	 * @model required="true"
	 * @generated
	 */
	String getFormat();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getFormat <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Format</em>' attribute.
	 * @see #getFormat()
	 * @generated
	 */
	void setFormat(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" clazzRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetValue()) {\n\t\t\tif (clazz.isInstance(getValue())) {\n\t\t\t\treturn clazz.cast(getValue());\n\t\t\t}\n\t\t}\n\n\t\treturn null;\n'"
	 * @generated
	 */
	<T> T getValueAs(Class<T> clazz);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetValue()) {\n\tfinal String s = getFormat();\n\tif (s != null && !s.isEmpty()) {\n\t\treturn String.format(s, getValue());\n\t} else {\n\t\treturn getValue() + \"\";\n\t}\n}\nreturn \"\";'"
	 * @generated
	 */
	String formatValue();

} // end of  ExtraData

// finish type fixing
