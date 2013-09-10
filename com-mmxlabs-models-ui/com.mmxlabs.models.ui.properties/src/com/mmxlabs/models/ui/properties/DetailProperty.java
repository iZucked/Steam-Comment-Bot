/**
 */
package com.mmxlabs.models.ui.properties;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jface.viewers.ILabelProvider;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Detail Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.properties.DetailProperty#getParent <em>Parent</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.DetailProperty#getChildren <em>Children</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.DetailProperty#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.DetailProperty#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.DetailProperty#getUnits <em>Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.DetailProperty#getObject <em>Object</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.DetailProperty#getLabelProvider <em>Label Provider</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.ui.properties.PropertiesPackage#getDetailProperty()
 * @model abstract="true"
 * @generated
 */
public interface DetailProperty extends EObject {
	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.ui.properties.DetailProperty#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(DetailProperty)
	 * @see com.mmxlabs.models.ui.properties.PropertiesPackage#getDetailProperty_Parent()
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getChildren
	 * @model opposite="children" transient="false"
	 * @generated
	 */
	DetailProperty getParent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.properties.DetailProperty#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(DetailProperty value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.ui.properties.DetailProperty}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.ui.properties.DetailProperty#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see com.mmxlabs.models.ui.properties.PropertiesPackage#getDetailProperty_Children()
	 * @see com.mmxlabs.models.ui.properties.DetailProperty#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<DetailProperty> getChildren();

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
	 * @see com.mmxlabs.models.ui.properties.PropertiesPackage#getDetailProperty_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.properties.DetailProperty#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * @see com.mmxlabs.models.ui.properties.PropertiesPackage#getDetailProperty_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.properties.DetailProperty#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Units</em>' attribute.
	 * @see #setUnits(String)
	 * @see com.mmxlabs.models.ui.properties.PropertiesPackage#getDetailProperty_Units()
	 * @model
	 * @generated
	 */
	String getUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.properties.DetailProperty#getUnits <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Units</em>' attribute.
	 * @see #getUnits()
	 * @generated
	 */
	void setUnits(String value);

	/**
	 * Returns the value of the '<em><b>Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object</em>' attribute.
	 * @see #setObject(Object)
	 * @see com.mmxlabs.models.ui.properties.PropertiesPackage#getDetailProperty_Object()
	 * @model
	 * @generated
	 */
	Object getObject();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.properties.DetailProperty#getObject <em>Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object</em>' attribute.
	 * @see #getObject()
	 * @generated
	 */
	void setObject(Object value);

	/**
	 * Returns the value of the '<em><b>Label Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label Provider</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label Provider</em>' attribute.
	 * @see #setLabelProvider(ILabelProvider)
	 * @see com.mmxlabs.models.ui.properties.PropertiesPackage#getDetailProperty_LabelProvider()
	 * @model dataType="com.mmxlabs.models.ui.properties.ILabelProvider"
	 * @generated
	 */
	ILabelProvider getLabelProvider();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.properties.DetailProperty#getLabelProvider <em>Label Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label Provider</em>' attribute.
	 * @see #getLabelProvider()
	 * @generated
	 */
	void setLabelProvider(ILabelProvider value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	String format();

} // DetailProperty
