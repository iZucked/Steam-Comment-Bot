/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Position Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PositionDescriptor#getAfter <em>After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PositionDescriptor#getBefore <em>Before</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPositionDescriptor()
 * @model
 * @generated
 */
public interface PositionDescriptor extends EObject {
	/**
	 * Returns the value of the '<em><b>After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>After</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>After</em>' attribute.
	 * @see #setAfter(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPositionDescriptor_After()
	 * @model
	 * @generated
	 */
	String getAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.PositionDescriptor#getAfter <em>After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>After</em>' attribute.
	 * @see #getAfter()
	 * @generated
	 */
	void setAfter(String value);

	/**
	 * Returns the value of the '<em><b>Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Before</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Before</em>' attribute.
	 * @see #setBefore(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPositionDescriptor_Before()
	 * @model
	 * @generated
	 */
	String getBefore();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.PositionDescriptor#getBefore <em>Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Before</em>' attribute.
	 * @see #getBefore()
	 * @generated
	 */
	void setBefore(String value);

} // PositionDescriptor
