/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.scenario.service.ui.ScenarioResult;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set Table Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getDeltaMetrics <em>Delta Metrics</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getCurrentMetrics <em>Current Metrics</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getChangeSet <em>Change Set</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getBaseScenario <em>Base Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getCurrentScenario <em>Current Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getLinkedGroup <em>Linked Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getComplexity <em>Complexity</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getSortValue <em>Sort Value</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getGroupSortValue <em>Group Sort Value</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getGroupObject <em>Group Object</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#isGroupAlternative <em>Group Alternative</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup()
 * @model
 * @generated
 */
public interface ChangeSetTableGroup extends EObject {
	/**
	 * Returns the value of the '<em><b>Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_Rows()
	 * @model containment="true"
	 * @generated
	 */
	EList<ChangeSetTableRow> getRows();

	/**
	 * Returns the value of the '<em><b>Delta Metrics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Delta Metrics</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Delta Metrics</em>' containment reference.
	 * @see #setDeltaMetrics(DeltaMetrics)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_DeltaMetrics()
	 * @model containment="true"
	 * @generated
	 */
	DeltaMetrics getDeltaMetrics();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getDeltaMetrics <em>Delta Metrics</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delta Metrics</em>' containment reference.
	 * @see #getDeltaMetrics()
	 * @generated
	 */
	void setDeltaMetrics(DeltaMetrics value);

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_CurrentMetrics()
	 * @model containment="true"
	 * @generated
	 */
	Metrics getCurrentMetrics();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getCurrentMetrics <em>Current Metrics</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Metrics</em>' containment reference.
	 * @see #getCurrentMetrics()
	 * @generated
	 */
	void setCurrentMetrics(Metrics value);

	/**
	 * Returns the value of the '<em><b>Change Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Set</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Set</em>' reference.
	 * @see #setChangeSet(ChangeSet)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_ChangeSet()
	 * @model
	 * @generated
	 */
	ChangeSet getChangeSet();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getChangeSet <em>Change Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Change Set</em>' reference.
	 * @see #getChangeSet()
	 * @generated
	 */
	void setChangeSet(ChangeSet value);

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_BaseScenario()
	 * @model dataType="com.mmxlabs.lingo.reports.views.changeset.model.ScenarioResult"
	 * @generated
	 */
	ScenarioResult getBaseScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getBaseScenario <em>Base Scenario</em>}' attribute.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_CurrentScenario()
	 * @model dataType="com.mmxlabs.lingo.reports.views.changeset.model.ScenarioResult"
	 * @generated
	 */
	ScenarioResult getCurrentScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getCurrentScenario <em>Current Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Scenario</em>' attribute.
	 * @see #getCurrentScenario()
	 * @generated
	 */
	void setCurrentScenario(ScenarioResult value);

	/**
	 * Returns the value of the '<em><b>Linked Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Linked Group</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Linked Group</em>' reference.
	 * @see #setLinkedGroup(ChangeSetTableGroup)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_LinkedGroup()
	 * @model
	 * @generated
	 */
	ChangeSetTableGroup getLinkedGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getLinkedGroup <em>Linked Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Linked Group</em>' reference.
	 * @see #getLinkedGroup()
	 * @generated
	 */
	void setLinkedGroup(ChangeSetTableGroup value);

	/**
	 * Returns the value of the '<em><b>Complexity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Complexity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Complexity</em>' attribute.
	 * @see #setComplexity(int)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_Complexity()
	 * @model
	 * @generated
	 */
	int getComplexity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getComplexity <em>Complexity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Complexity</em>' attribute.
	 * @see #getComplexity()
	 * @generated
	 */
	void setComplexity(int value);

	/**
	 * Returns the value of the '<em><b>Sort Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sort Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sort Value</em>' attribute.
	 * @see #setSortValue(double)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_SortValue()
	 * @model
	 * @generated
	 */
	double getSortValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getSortValue <em>Sort Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sort Value</em>' attribute.
	 * @see #getSortValue()
	 * @generated
	 */
	void setSortValue(double value);

	/**
	 * Returns the value of the '<em><b>Group Sort Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Sort Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Sort Value</em>' attribute.
	 * @see #setGroupSortValue(double)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_GroupSortValue()
	 * @model
	 * @generated
	 */
	double getGroupSortValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getGroupSortValue <em>Group Sort Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Sort Value</em>' attribute.
	 * @see #getGroupSortValue()
	 * @generated
	 */
	void setGroupSortValue(double value);

	/**
	 * Returns the value of the '<em><b>Group Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Object</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Object</em>' attribute.
	 * @see #setGroupObject(Object)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_GroupObject()
	 * @model
	 * @generated
	 */
	Object getGroupObject();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getGroupObject <em>Group Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Object</em>' attribute.
	 * @see #getGroupObject()
	 * @generated
	 */
	void setGroupObject(Object value);

	/**
	 * Returns the value of the '<em><b>Group Alternative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Alternative</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Alternative</em>' attribute.
	 * @see #setGroupAlternative(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableGroup_GroupAlternative()
	 * @model
	 * @generated
	 */
	boolean isGroupAlternative();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#isGroupAlternative <em>Group Alternative</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Alternative</em>' attribute.
	 * @see #isGroupAlternative()
	 * @generated
	 */
	void setGroupAlternative(boolean value);

} // ChangeSetTableGroup
