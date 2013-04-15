/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extra Data</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getValue <em>Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getFormat <em>Format</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraData#getFormatType <em>Format Type</em>}</li>
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
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #isSetValue()
	 * @see #unsetValue()
	 * @see #setValue(Object)
	 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraData_Value()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Object getValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #isSetValue()
	 * @see #unsetValue()
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Object value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetValue()
	 * @see #getValue()
	 * @see #setValue(Object)
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
	 * @see #setValue(Object)
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
	 * Returns the value of the '<em><b>Format Type</b></em>' attribute.
	 * The default value is <code>"AUTO"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.ExtraDataFormatType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Format Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Format Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.ExtraDataFormatType
	 * @see #setFormatType(ExtraDataFormatType)
	 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraData_FormatType()
	 * @model default="AUTO" required="true"
	 * @generated
	 */
	ExtraDataFormatType getFormatType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.types.ExtraData#getFormatType <em>Format Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Format Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.ExtraDataFormatType
	 * @see #getFormatType()
	 * @generated
	 */
	void setFormatType(ExtraDataFormatType value);

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
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetValue()) {\n\t\t\tfinal Object o = getValue();\n\t\t\tfinal String format = getFormat();\n\t\t\tExtraDataFormatType f = getFormatType();\n\t\t\tif (f == ExtraDataFormatType.AUTO) {\n\t\t\t\tif (o instanceof Integer)\n\t\t\t\t\tf = ExtraDataFormatType.INTEGER;\n\t\t\t\telse if (o instanceof Date)\n\t\t\t\t\tf = ExtraDataFormatType.DATE;\n\t\t\t}\n\t\t\tswitch (getFormatType()) {\n\t\t\tcase AUTO:\n\t\t\t\treturn \"\" + o;\n\t\t\tcase INTEGER:\n\t\t\t\treturn String.format(\"%,d\", o);\n\t\t\tcase DURATION:\n\t\t\t\tif (o instanceof Integer) {\n\t\t\t\t\tfinal int totalHours = (Integer) o;\n\t\t\t\t\tfinal int hrs = totalHours % 24;\n\t\t\t\t\tfinal int days = totalHours / 24;\n\t\t\t\t\tif (days > 0) {\n\t\t\t\t\t\treturn String.format(\"%d:%d\", days, hrs);\n\t\t\t\t\t} else {\n\t\t\t\t\t\treturn String.format(\"%d\", hrs);\n\t\t\t\t\t}\n\t\t\t\t}\n\t\t\t\tbreak;\n\t\t\tcase CURRENCY:\n\t\t\t\treturn String.format(\"$%,d\", o);\n\t\t\tcase DATE:\n\t\t\t\tDateFormat dateFormat;\n\t\t\t\tif (format != null && !format.isEmpty()) {\n\t\t\t\t\tdateFormat = new SimpleDateFormat(format);\n\t\t\t\t} else {\n\t\t\t\t\tdateFormat = DateFormat.getDateTimeInstance();\n\t\t\t\t}\n\t\t\t\treturn dateFormat.format(o);\n\t\t\tcase STRING_FORMAT:\n\t\t\t\treturn String.format(format, o);\n\t\t\t}\n\t\t}\n\t\treturn \"\";'"
	 * @generated
	 */
	String formatValue();

} // end of  ExtraData

// finish type fixing
