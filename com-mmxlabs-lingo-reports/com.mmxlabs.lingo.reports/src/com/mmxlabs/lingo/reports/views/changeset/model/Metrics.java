/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metrics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getPnl <em>Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getLateness <em>Lateness</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getCapacity <em>Capacity</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getMetrics()
 * @model
 * @generated
 */
public interface Metrics extends EObject {
	/**
	 * Returns the value of the '<em><b>Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pnl</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pnl</em>' attribute.
	 * @see #setPnl(int)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getMetrics_Pnl()
	 * @model
	 * @generated
	 */
	int getPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getPnl <em>Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pnl</em>' attribute.
	 * @see #getPnl()
	 * @generated
	 */
	void setPnl(int value);

	/**
	 * Returns the value of the '<em><b>Lateness</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lateness</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lateness</em>' attribute.
	 * @see #setLateness(int)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getMetrics_Lateness()
	 * @model
	 * @generated
	 */
	int getLateness();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getLateness <em>Lateness</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lateness</em>' attribute.
	 * @see #getLateness()
	 * @generated
	 */
	void setLateness(int value);

	/**
	 * Returns the value of the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity</em>' attribute.
	 * @see #setCapacity(int)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getMetrics_Capacity()
	 * @model
	 * @generated
	 */
	int getCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity</em>' attribute.
	 * @see #getCapacity()
	 * @generated
	 */
	void setCapacity(int value);

} // Metrics
