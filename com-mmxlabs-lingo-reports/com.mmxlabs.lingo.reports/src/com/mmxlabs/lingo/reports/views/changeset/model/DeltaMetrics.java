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
 * A representation of the model object '<em><b>Delta Metrics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getPnlDelta <em>Pnl Delta</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getLatenessDelta <em>Lateness Delta</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getCapacityDelta <em>Capacity Delta</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getDeltaMetrics()
 * @model
 * @generated
 */
public interface DeltaMetrics extends EObject {
	/**
	 * Returns the value of the '<em><b>Pnl Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pnl Delta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pnl Delta</em>' attribute.
	 * @see #setPnlDelta(int)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getDeltaMetrics_PnlDelta()
	 * @model
	 * @generated
	 */
	int getPnlDelta();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getPnlDelta <em>Pnl Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pnl Delta</em>' attribute.
	 * @see #getPnlDelta()
	 * @generated
	 */
	void setPnlDelta(int value);

	/**
	 * Returns the value of the '<em><b>Lateness Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lateness Delta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lateness Delta</em>' attribute.
	 * @see #setLatenessDelta(int)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getDeltaMetrics_LatenessDelta()
	 * @model
	 * @generated
	 */
	int getLatenessDelta();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getLatenessDelta <em>Lateness Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lateness Delta</em>' attribute.
	 * @see #getLatenessDelta()
	 * @generated
	 */
	void setLatenessDelta(int value);

	/**
	 * Returns the value of the '<em><b>Capacity Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity Delta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity Delta</em>' attribute.
	 * @see #setCapacityDelta(int)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getDeltaMetrics_CapacityDelta()
	 * @model
	 * @generated
	 */
	int getCapacityDelta();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getCapacityDelta <em>Capacity Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity Delta</em>' attribute.
	 * @see #getCapacityDelta()
	 * @generated
	 */
	void setCapacityDelta(int value);

} // DeltaMetrics
