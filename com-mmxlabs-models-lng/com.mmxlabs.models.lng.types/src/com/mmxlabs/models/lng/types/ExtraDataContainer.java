/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import java.io.Serializable;
import java.lang.Iterable;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extra Data Container</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.types.ExtraDataContainer#getExtraData <em>Extra Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraDataContainer()
 * @model
 * @generated
 */
public interface ExtraDataContainer extends EObject {
	/**
	 * Returns the value of the '<em><b>Extra Data</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.ExtraData}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extra Data</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extra Data</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.types.TypesPackage#getExtraDataContainer_ExtraData()
	 * @model containment="true"
	 * @generated
	 */
	EList<ExtraData> getExtraData();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" keysDataType="com.mmxlabs.models.lng.types.Iterable<org.eclipse.emf.ecore.EString>" keysRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='java.util.Iterator<String> iterator = keys.iterator();\n\t\tif (iterator.hasNext() == false) return null;\n\t\tExtraData edc = getDataWithKey(iterator.next());\n\t\twhile (edc != null && iterator.hasNext()) {\n\t\t\tedc = edc.getDataWithKey(iterator.next());\n\t\t}\n\t\treturn edc;'"
	 * @generated
	 */
	ExtraData getDataWithPath(Iterable<String> keys);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" keyRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='for (final ExtraData e : getExtraData()) {\n\tif (e.getKey().equals(key)) return e;\n}\nreturn null;'"
	 * @generated
	 */
	ExtraData getDataWithKey(String key);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" keyRequired="true" nameRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final ExtraData result = TypesFactory.eINSTANCE.createExtraData();\nresult.setKey(key);\nresult.setName(name);\ngetExtraData().add(result);\nreturn result;'"
	 * @generated
	 */
	ExtraData addExtraData(String key, String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" keyRequired="true" nameRequired="true" valueDataType="com.mmxlabs.models.lng.types.SerializableObject" valueRequired="true" formatRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final ExtraData result = addExtraData(key, name);\nresult.setValue(value);\nresult.setFormatType(format);\nreturn result;'"
	 * @generated
	 */
	ExtraData addExtraData(String key, String name, Serializable value, ExtraDataFormatType format);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" pathDataType="com.mmxlabs.models.lng.types.Iterable<org.eclipse.emf.ecore.EString>" pathRequired="true" clazzRequired="true" defaultValueRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final ExtraData ed = getDataWithPath(path);\nif (ed == null) return defaultValue;\nfinal T value = ed.getValueAs(clazz);\nif (value == null) return defaultValue;\nreturn value;'"
	 * @generated
	 */
	<T> T getValueWithPathAs(Iterable<String> path, Class<T> clazz, T defaultValue);

} // end of  ExtraDataContainer

// finish type fixing
