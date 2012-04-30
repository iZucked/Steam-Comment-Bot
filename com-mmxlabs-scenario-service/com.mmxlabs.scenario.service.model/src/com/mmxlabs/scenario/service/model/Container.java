/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.Container#getParent <em>Parent</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.Container#getElements <em>Elements</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.Container#isArchived <em>Archived</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getContainer()
 * @model abstract="true"
 * @generated
 */
public interface Container extends EObject {
	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.Container#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(Container)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getContainer_Parent()
	 * @see com.mmxlabs.scenario.service.model.Container#getElements
	 * @model opposite="elements" transient="false"
	 * @generated
	 */
	Container getParent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.Container#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(Container value);

	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.scenario.service.model.Container}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.Container#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getContainer_Elements()
	 * @see com.mmxlabs.scenario.service.model.Container#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<Container> getElements();

	/**
	 * Returns the value of the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Archived</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Archived</em>' attribute.
	 * @see #setArchived(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getContainer_Archived()
	 * @model
	 * @generated
	 */
	boolean isArchived();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.Container#isArchived <em>Archived</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Archived</em>' attribute.
	 * @see #isArchived()
	 * @generated
	 */
	void setArchived(boolean value);

} // Container
