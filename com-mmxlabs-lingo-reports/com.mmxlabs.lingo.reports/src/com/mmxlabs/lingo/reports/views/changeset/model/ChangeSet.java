/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToBase <em>Metrics To Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToPrevious <em>Metrics To Previous</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenarioRef <em>Base Scenario Ref</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenarioRef <em>Prev Scenario Ref</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenarioRef <em>Current Scenario Ref</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenario <em>Base Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenario <em>Prev Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenario <em>Current Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToBase <em>Change Set Rows To Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToPrevious <em>Change Set Rows To Previous</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentMetrics <em>Current Metrics</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet()
 * @model
 * @generated
 */
public interface ChangeSet extends EObject {
	/**
	 * Returns the value of the '<em><b>Metrics To Base</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metrics To Base</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metrics To Base</em>' containment reference.
	 * @see #setMetricsToBase(DeltaMetrics)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_MetricsToBase()
	 * @model containment="true"
	 * @generated
	 */
	DeltaMetrics getMetricsToBase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToBase <em>Metrics To Base</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metrics To Base</em>' containment reference.
	 * @see #getMetricsToBase()
	 * @generated
	 */
	void setMetricsToBase(DeltaMetrics value);

	/**
	 * Returns the value of the '<em><b>Metrics To Previous</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metrics To Previous</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metrics To Previous</em>' containment reference.
	 * @see #setMetricsToPrevious(DeltaMetrics)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_MetricsToPrevious()
	 * @model containment="true"
	 * @generated
	 */
	DeltaMetrics getMetricsToPrevious();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToPrevious <em>Metrics To Previous</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metrics To Previous</em>' containment reference.
	 * @see #getMetricsToPrevious()
	 * @generated
	 */
	void setMetricsToPrevious(DeltaMetrics value);

	/**
	 * Returns the value of the '<em><b>Base Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Scenario Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Scenario Ref</em>' reference.
	 * @see #setBaseScenarioRef(ModelReference)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_BaseScenarioRef()
	 * @model
	 * @generated
	 */
	ModelReference getBaseScenarioRef();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenarioRef <em>Base Scenario Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Scenario Ref</em>' reference.
	 * @see #getBaseScenarioRef()
	 * @generated
	 */
	void setBaseScenarioRef(ModelReference value);

	/**
	 * Returns the value of the '<em><b>Prev Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prev Scenario Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prev Scenario Ref</em>' reference.
	 * @see #setPrevScenarioRef(ModelReference)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_PrevScenarioRef()
	 * @model
	 * @generated
	 */
	ModelReference getPrevScenarioRef();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenarioRef <em>Prev Scenario Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prev Scenario Ref</em>' reference.
	 * @see #getPrevScenarioRef()
	 * @generated
	 */
	void setPrevScenarioRef(ModelReference value);

	/**
	 * Returns the value of the '<em><b>Current Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Scenario Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Scenario Ref</em>' reference.
	 * @see #setCurrentScenarioRef(ModelReference)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_CurrentScenarioRef()
	 * @model
	 * @generated
	 */
	ModelReference getCurrentScenarioRef();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenarioRef <em>Current Scenario Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Scenario Ref</em>' reference.
	 * @see #getCurrentScenarioRef()
	 * @generated
	 */
	void setCurrentScenarioRef(ModelReference value);

	/**
	 * Returns the value of the '<em><b>Base Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Scenario</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Scenario</em>' reference.
	 * @see #setBaseScenario(ScenarioInstance)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_BaseScenario()
	 * @model
	 * @generated
	 */
	ScenarioInstance getBaseScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenario <em>Base Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Scenario</em>' reference.
	 * @see #getBaseScenario()
	 * @generated
	 */
	void setBaseScenario(ScenarioInstance value);

	/**
	 * Returns the value of the '<em><b>Prev Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prev Scenario</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prev Scenario</em>' reference.
	 * @see #setPrevScenario(ScenarioInstance)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_PrevScenario()
	 * @model
	 * @generated
	 */
	ScenarioInstance getPrevScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenario <em>Prev Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prev Scenario</em>' reference.
	 * @see #getPrevScenario()
	 * @generated
	 */
	void setPrevScenario(ScenarioInstance value);

	/**
	 * Returns the value of the '<em><b>Current Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Scenario</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Scenario</em>' reference.
	 * @see #setCurrentScenario(ScenarioInstance)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_CurrentScenario()
	 * @model
	 * @generated
	 */
	ScenarioInstance getCurrentScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenario <em>Current Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Scenario</em>' reference.
	 * @see #getCurrentScenario()
	 * @generated
	 */
	void setCurrentScenario(ScenarioInstance value);

	/**
	 * Returns the value of the '<em><b>Change Set Rows To Base</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Set Rows To Base</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Set Rows To Base</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_ChangeSetRowsToBase()
	 * @model containment="true"
	 * @generated
	 */
	EList<ChangeSetRow> getChangeSetRowsToBase();

	/**
	 * Returns the value of the '<em><b>Change Set Rows To Previous</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Set Rows To Previous</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Set Rows To Previous</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_ChangeSetRowsToPrevious()
	 * @model containment="true"
	 * @generated
	 */
	EList<ChangeSetRow> getChangeSetRowsToPrevious();

	/**
	 * Returns the value of the '<em><b>Current Metrics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Metrics</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Metrics</em>' containment reference.
	 * @see #setCurrentMetrics(Metrics)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_CurrentMetrics()
	 * @model containment="true"
	 * @generated
	 */
	Metrics getCurrentMetrics();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentMetrics <em>Current Metrics</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Metrics</em>' containment reference.
	 * @see #getCurrentMetrics()
	 * @generated
	 */
	void setCurrentMetrics(Metrics value);

} // ChangeSet
