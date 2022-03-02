/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.scenario.service.ScenarioResult;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToDefaultBase <em>Metrics To Default Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToAlternativeBase <em>Metrics To Alternative Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenario <em>Base Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenario <em>Current Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getAltBaseScenario <em>Alt Base Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getAltCurrentScenario <em>Alt Current Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToDefaultBase <em>Change Set Rows To Default Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToAlternativeBase <em>Change Set Rows To Alternative Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentMetrics <em>Current Metrics</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeDescription <em>Change Description</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getUserSettings <em>User Settings</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet()
 * @model
 * @generated
 */
public interface ChangeSet extends EObject {
	/**
	 * Returns the value of the '<em><b>Metrics To Default Base</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metrics To Default Base</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metrics To Default Base</em>' containment reference.
	 * @see #setMetricsToDefaultBase(DeltaMetrics)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_MetricsToDefaultBase()
	 * @model containment="true"
	 * @generated
	 */
	DeltaMetrics getMetricsToDefaultBase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToDefaultBase <em>Metrics To Default Base</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metrics To Default Base</em>' containment reference.
	 * @see #getMetricsToDefaultBase()
	 * @generated
	 */
	void setMetricsToDefaultBase(DeltaMetrics value);

	/**
	 * Returns the value of the '<em><b>Metrics To Alternative Base</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metrics To Alternative Base</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metrics To Alternative Base</em>' containment reference.
	 * @see #setMetricsToAlternativeBase(DeltaMetrics)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_MetricsToAlternativeBase()
	 * @model containment="true"
	 * @generated
	 */
	DeltaMetrics getMetricsToAlternativeBase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToAlternativeBase <em>Metrics To Alternative Base</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metrics To Alternative Base</em>' containment reference.
	 * @see #getMetricsToAlternativeBase()
	 * @generated
	 */
	void setMetricsToAlternativeBase(DeltaMetrics value);

	/**
	 * Returns the value of the '<em><b>Base Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Scenario</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Scenario</em>' attribute.
	 * @see #setBaseScenario(ScenarioResult)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_BaseScenario()
	 * @model dataType="com.mmxlabs.lingo.reports.views.changeset.model.ScenarioResult"
	 * @generated
	 */
	ScenarioResult getBaseScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenario <em>Base Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Scenario</em>' attribute.
	 * @see #getBaseScenario()
	 * @generated
	 */
	void setBaseScenario(ScenarioResult value);

	/**
	 * Returns the value of the '<em><b>Current Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Scenario</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Scenario</em>' attribute.
	 * @see #setCurrentScenario(ScenarioResult)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_CurrentScenario()
	 * @model dataType="com.mmxlabs.lingo.reports.views.changeset.model.ScenarioResult"
	 * @generated
	 */
	ScenarioResult getCurrentScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenario <em>Current Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Scenario</em>' attribute.
	 * @see #getCurrentScenario()
	 * @generated
	 */
	void setCurrentScenario(ScenarioResult value);

	/**
	 * Returns the value of the '<em><b>Alt Base Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alt Base Scenario</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alt Base Scenario</em>' attribute.
	 * @see #setAltBaseScenario(ScenarioResult)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_AltBaseScenario()
	 * @model dataType="com.mmxlabs.lingo.reports.views.changeset.model.ScenarioResult"
	 * @generated
	 */
	ScenarioResult getAltBaseScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getAltBaseScenario <em>Alt Base Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alt Base Scenario</em>' attribute.
	 * @see #getAltBaseScenario()
	 * @generated
	 */
	void setAltBaseScenario(ScenarioResult value);

	/**
	 * Returns the value of the '<em><b>Alt Current Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alt Current Scenario</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alt Current Scenario</em>' attribute.
	 * @see #setAltCurrentScenario(ScenarioResult)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_AltCurrentScenario()
	 * @model dataType="com.mmxlabs.lingo.reports.views.changeset.model.ScenarioResult"
	 * @generated
	 */
	ScenarioResult getAltCurrentScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getAltCurrentScenario <em>Alt Current Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alt Current Scenario</em>' attribute.
	 * @see #getAltCurrentScenario()
	 * @generated
	 */
	void setAltCurrentScenario(ScenarioResult value);

	/**
	 * Returns the value of the '<em><b>Change Set Rows To Default Base</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Set Rows To Default Base</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Set Rows To Default Base</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_ChangeSetRowsToDefaultBase()
	 * @model containment="true"
	 * @generated
	 */
	EList<ChangeSetRow> getChangeSetRowsToDefaultBase();

	/**
	 * Returns the value of the '<em><b>Change Set Rows To Alternative Base</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Set Rows To Alternative Base</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Set Rows To Alternative Base</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_ChangeSetRowsToAlternativeBase()
	 * @model containment="true"
	 * @generated
	 */
	EList<ChangeSetRow> getChangeSetRowsToAlternativeBase();

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Change Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Description</em>' attribute.
	 * @see #setChangeDescription(ChangeDescription)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_ChangeDescription()
	 * @model dataType="com.mmxlabs.lingo.reports.views.changeset.model.ChangeDescription"
	 * @generated
	 */
	ChangeDescription getChangeDescription();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeDescription <em>Change Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Change Description</em>' attribute.
	 * @see #getChangeDescription()
	 * @generated
	 */
	void setChangeDescription(ChangeDescription value);

	/**
	 * Returns the value of the '<em><b>User Settings</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Settings</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Settings</em>' attribute.
	 * @see #setUserSettings(UserSettings)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSet_UserSettings()
	 * @model dataType="com.mmxlabs.lingo.reports.views.changeset.model.UserSettings"
	 * @generated
	 */
	UserSettings getUserSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getUserSettings <em>User Settings</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User Settings</em>' attribute.
	 * @see #getUserSettings()
	 * @generated
	 */
	void setUserSettings(UserSettings value);

} // ChangeSet
