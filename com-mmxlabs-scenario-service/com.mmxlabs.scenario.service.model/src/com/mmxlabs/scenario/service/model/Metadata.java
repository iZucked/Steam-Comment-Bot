/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.Metadata#getCreator <em>Creator</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.Metadata#getCreated <em>Created</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.Metadata#getLastModified <em>Last Modified</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.Metadata#getComment <em>Comment</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.Metadata#getLastModifiedBy <em>Last Modified By</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.Metadata#getContentType <em>Content Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getMetadata()
 * @model
 * @generated
 */
public interface Metadata extends EObject {
	/**
	 * Returns the value of the '<em><b>Creator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Creator</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Creator</em>' attribute.
	 * @see #setCreator(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getMetadata_Creator()
	 * @model required="true"
	 * @generated
	 */
	String getCreator();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.Metadata#getCreator <em>Creator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Creator</em>' attribute.
	 * @see #getCreator()
	 * @generated
	 */
	void setCreator(String value);

	/**
	 * Returns the value of the '<em><b>Created</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Created</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Created</em>' attribute.
	 * @see #setCreated(Date)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getMetadata_Created()
	 * @model required="true"
	 * @generated
	 */
	Date getCreated();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.Metadata#getCreated <em>Created</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Created</em>' attribute.
	 * @see #getCreated()
	 * @generated
	 */
	void setCreated(Date value);

	/**
	 * Returns the value of the '<em><b>Last Modified</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Modified</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Modified</em>' attribute.
	 * @see #setLastModified(Date)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getMetadata_LastModified()
	 * @model
	 * @generated
	 */
	Date getLastModified();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.Metadata#getLastModified <em>Last Modified</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Modified</em>' attribute.
	 * @see #getLastModified()
	 * @generated
	 */
	void setLastModified(Date value);

	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getMetadata_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.Metadata#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * Returns the value of the '<em><b>Last Modified By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Modified By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Modified By</em>' attribute.
	 * @see #setLastModifiedBy(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getMetadata_LastModifiedBy()
	 * @model
	 * @generated
	 */
	String getLastModifiedBy();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.Metadata#getLastModifiedBy <em>Last Modified By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Modified By</em>' attribute.
	 * @see #getLastModifiedBy()
	 * @generated
	 */
	void setLastModifiedBy(String value);

	/**
	 * Returns the value of the '<em><b>Content Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content Type</em>' attribute.
	 * @see #setContentType(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getMetadata_ContentType()
	 * @model
	 * @generated
	 */
	String getContentType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.Metadata#getContentType <em>Content Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content Type</em>' attribute.
	 * @see #getContentType()
	 * @generated
	 */
	void setContentType(String value);

} // Metadata
