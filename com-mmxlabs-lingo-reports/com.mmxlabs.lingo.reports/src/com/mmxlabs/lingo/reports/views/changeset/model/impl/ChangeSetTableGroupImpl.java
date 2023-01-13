/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;

import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.scenario.service.ScenarioResult;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Set Table Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getDeltaMetrics <em>Delta Metrics</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getCurrentMetrics <em>Current Metrics</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getChangeSet <em>Change Set</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getBaseScenario <em>Base Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getCurrentScenario <em>Current Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getLinkedGroup <em>Linked Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getComplexity <em>Complexity</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getSortValue <em>Sort Value</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getGroupSortValue <em>Group Sort Value</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getGroupObject <em>Group Object</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#isGroupAlternative <em>Group Alternative</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetTableGroupImpl extends MinimalEObjectImpl.Container implements ChangeSetTableGroup {
	/**
	 * The cached value of the '{@link #getRows() <em>Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRows()
	 * @generated
	 * @ordered
	 */
	protected EList<ChangeSetTableRow> rows;

	/**
	 * The cached value of the '{@link #getDeltaMetrics() <em>Delta Metrics</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeltaMetrics()
	 * @generated
	 * @ordered
	 */
	protected DeltaMetrics deltaMetrics;

	/**
	 * The cached value of the '{@link #getCurrentMetrics() <em>Current Metrics</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentMetrics()
	 * @generated
	 * @ordered
	 */
	protected Metrics currentMetrics;

	/**
	 * The cached value of the '{@link #getChangeSet() <em>Change Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeSet()
	 * @generated
	 * @ordered
	 */
	protected ChangeSet changeSet;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseScenario() <em>Base Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseScenario()
	 * @generated
	 * @ordered
	 */
	protected static final ScenarioResult BASE_SCENARIO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBaseScenario() <em>Base Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseScenario()
	 * @generated
	 * @ordered
	 */
	protected ScenarioResult baseScenario = BASE_SCENARIO_EDEFAULT;

	/**
	 * The default value of the '{@link #getCurrentScenario() <em>Current Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentScenario()
	 * @generated
	 * @ordered
	 */
	protected static final ScenarioResult CURRENT_SCENARIO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCurrentScenario() <em>Current Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentScenario()
	 * @generated
	 * @ordered
	 */
	protected ScenarioResult currentScenario = CURRENT_SCENARIO_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLinkedGroup() <em>Linked Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkedGroup()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetTableGroup linkedGroup;

	/**
	 * The default value of the '{@link #getComplexity() <em>Complexity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComplexity()
	 * @generated
	 * @ordered
	 */
	protected static final int COMPLEXITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getComplexity() <em>Complexity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComplexity()
	 * @generated
	 * @ordered
	 */
	protected int complexity = COMPLEXITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getSortValue() <em>Sort Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortValue()
	 * @generated
	 * @ordered
	 */
	protected static final double SORT_VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSortValue() <em>Sort Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortValue()
	 * @generated
	 * @ordered
	 */
	protected double sortValue = SORT_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getGroupSortValue() <em>Group Sort Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupSortValue()
	 * @generated
	 * @ordered
	 */
	protected static final double GROUP_SORT_VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getGroupSortValue() <em>Group Sort Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupSortValue()
	 * @generated
	 * @ordered
	 */
	protected double groupSortValue = GROUP_SORT_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getGroupObject() <em>Group Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupObject()
	 * @generated
	 * @ordered
	 */
	protected static final Object GROUP_OBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGroupObject() <em>Group Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupObject()
	 * @generated
	 * @ordered
	 */
	protected Object groupObject = GROUP_OBJECT_EDEFAULT;

	/**
	 * The default value of the '{@link #isGroupAlternative() <em>Group Alternative</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGroupAlternative()
	 * @generated
	 * @ordered
	 */
	protected static final boolean GROUP_ALTERNATIVE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isGroupAlternative() <em>Group Alternative</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGroupAlternative()
	 * @generated
	 * @ordered
	 */
	protected boolean groupAlternative = GROUP_ALTERNATIVE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeSetTableGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.CHANGE_SET_TABLE_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ChangeSetTableRow> getRows() {
		if (rows == null) {
			rows = new EObjectContainmentEList<ChangeSetTableRow>(ChangeSetTableRow.class, this, ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeltaMetrics getDeltaMetrics() {
		return deltaMetrics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDeltaMetrics(DeltaMetrics newDeltaMetrics, NotificationChain msgs) {
		DeltaMetrics oldDeltaMetrics = deltaMetrics;
		deltaMetrics = newDeltaMetrics;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS, oldDeltaMetrics, newDeltaMetrics);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDeltaMetrics(DeltaMetrics newDeltaMetrics) {
		if (newDeltaMetrics != deltaMetrics) {
			NotificationChain msgs = null;
			if (deltaMetrics != null)
				msgs = ((InternalEObject)deltaMetrics).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS, null, msgs);
			if (newDeltaMetrics != null)
				msgs = ((InternalEObject)newDeltaMetrics).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS, null, msgs);
			msgs = basicSetDeltaMetrics(newDeltaMetrics, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS, newDeltaMetrics, newDeltaMetrics));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Metrics getCurrentMetrics() {
		return currentMetrics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCurrentMetrics(Metrics newCurrentMetrics, NotificationChain msgs) {
		Metrics oldCurrentMetrics = currentMetrics;
		currentMetrics = newCurrentMetrics;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS, oldCurrentMetrics, newCurrentMetrics);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentMetrics(Metrics newCurrentMetrics) {
		if (newCurrentMetrics != currentMetrics) {
			NotificationChain msgs = null;
			if (currentMetrics != null)
				msgs = ((InternalEObject)currentMetrics).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS, null, msgs);
			if (newCurrentMetrics != null)
				msgs = ((InternalEObject)newCurrentMetrics).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS, null, msgs);
			msgs = basicSetCurrentMetrics(newCurrentMetrics, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS, newCurrentMetrics, newCurrentMetrics));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSet getChangeSet() {
		if (changeSet != null && changeSet.eIsProxy()) {
			InternalEObject oldChangeSet = (InternalEObject)changeSet;
			changeSet = (ChangeSet)eResolveProxy(oldChangeSet);
			if (changeSet != oldChangeSet) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET, oldChangeSet, changeSet));
			}
		}
		return changeSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSet basicGetChangeSet() {
		return changeSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setChangeSet(ChangeSet newChangeSet) {
		ChangeSet oldChangeSet = changeSet;
		changeSet = newChangeSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET, oldChangeSet, changeSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScenarioResult getBaseScenario() {
		return baseScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseScenario(ScenarioResult newBaseScenario) {
		ScenarioResult oldBaseScenario = baseScenario;
		baseScenario = newBaseScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__BASE_SCENARIO, oldBaseScenario, baseScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScenarioResult getCurrentScenario() {
		return currentScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentScenario(ScenarioResult newCurrentScenario) {
		ScenarioResult oldCurrentScenario = currentScenario;
		currentScenario = newCurrentScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_SCENARIO, oldCurrentScenario, currentScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetTableGroup getLinkedGroup() {
		if (linkedGroup != null && linkedGroup.eIsProxy()) {
			InternalEObject oldLinkedGroup = (InternalEObject)linkedGroup;
			linkedGroup = (ChangeSetTableGroup)eResolveProxy(oldLinkedGroup);
			if (linkedGroup != oldLinkedGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_GROUP__LINKED_GROUP, oldLinkedGroup, linkedGroup));
			}
		}
		return linkedGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetTableGroup basicGetLinkedGroup() {
		return linkedGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLinkedGroup(ChangeSetTableGroup newLinkedGroup) {
		ChangeSetTableGroup oldLinkedGroup = linkedGroup;
		linkedGroup = newLinkedGroup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__LINKED_GROUP, oldLinkedGroup, linkedGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getComplexity() {
		return complexity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setComplexity(int newComplexity) {
		int oldComplexity = complexity;
		complexity = newComplexity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__COMPLEXITY, oldComplexity, complexity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getSortValue() {
		return sortValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSortValue(double newSortValue) {
		double oldSortValue = sortValue;
		sortValue = newSortValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__SORT_VALUE, oldSortValue, sortValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getGroupSortValue() {
		return groupSortValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGroupSortValue(double newGroupSortValue) {
		double oldGroupSortValue = groupSortValue;
		groupSortValue = newGroupSortValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_SORT_VALUE, oldGroupSortValue, groupSortValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getGroupObject() {
		return groupObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGroupObject(Object newGroupObject) {
		Object oldGroupObject = groupObject;
		groupObject = newGroupObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_OBJECT, oldGroupObject, groupObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isGroupAlternative() {
		return groupAlternative;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGroupAlternative(boolean newGroupAlternative) {
		boolean oldGroupAlternative = groupAlternative;
		groupAlternative = newGroupAlternative;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_ALTERNATIVE, oldGroupAlternative, groupAlternative));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				return ((InternalEList<?>)getRows()).basicRemove(otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				return basicSetDeltaMetrics(null, msgs);
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				return basicSetCurrentMetrics(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				return getRows();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				return getDeltaMetrics();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				return getCurrentMetrics();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET:
				if (resolve) return getChangeSet();
				return basicGetChangeSet();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION:
				return getDescription();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__BASE_SCENARIO:
				return getBaseScenario();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_SCENARIO:
				return getCurrentScenario();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__LINKED_GROUP:
				if (resolve) return getLinkedGroup();
				return basicGetLinkedGroup();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__COMPLEXITY:
				return getComplexity();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__SORT_VALUE:
				return getSortValue();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_SORT_VALUE:
				return getGroupSortValue();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_OBJECT:
				return getGroupObject();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_ALTERNATIVE:
				return isGroupAlternative();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				getRows().clear();
				getRows().addAll((Collection<? extends ChangeSetTableRow>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				setDeltaMetrics((DeltaMetrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				setCurrentMetrics((Metrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET:
				setChangeSet((ChangeSet)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__BASE_SCENARIO:
				setBaseScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_SCENARIO:
				setCurrentScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__LINKED_GROUP:
				setLinkedGroup((ChangeSetTableGroup)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__COMPLEXITY:
				setComplexity((Integer)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__SORT_VALUE:
				setSortValue((Double)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_SORT_VALUE:
				setGroupSortValue((Double)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_OBJECT:
				setGroupObject(newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_ALTERNATIVE:
				setGroupAlternative((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				getRows().clear();
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				setDeltaMetrics((DeltaMetrics)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				setCurrentMetrics((Metrics)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET:
				setChangeSet((ChangeSet)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__BASE_SCENARIO:
				setBaseScenario(BASE_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_SCENARIO:
				setCurrentScenario(CURRENT_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__LINKED_GROUP:
				setLinkedGroup((ChangeSetTableGroup)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__COMPLEXITY:
				setComplexity(COMPLEXITY_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__SORT_VALUE:
				setSortValue(SORT_VALUE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_SORT_VALUE:
				setGroupSortValue(GROUP_SORT_VALUE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_OBJECT:
				setGroupObject(GROUP_OBJECT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_ALTERNATIVE:
				setGroupAlternative(GROUP_ALTERNATIVE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				return rows != null && !rows.isEmpty();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				return deltaMetrics != null;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				return currentMetrics != null;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET:
				return changeSet != null;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__BASE_SCENARIO:
				return BASE_SCENARIO_EDEFAULT == null ? baseScenario != null : !BASE_SCENARIO_EDEFAULT.equals(baseScenario);
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_SCENARIO:
				return CURRENT_SCENARIO_EDEFAULT == null ? currentScenario != null : !CURRENT_SCENARIO_EDEFAULT.equals(currentScenario);
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__LINKED_GROUP:
				return linkedGroup != null;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__COMPLEXITY:
				return complexity != COMPLEXITY_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__SORT_VALUE:
				return sortValue != SORT_VALUE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_SORT_VALUE:
				return groupSortValue != GROUP_SORT_VALUE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_OBJECT:
				return GROUP_OBJECT_EDEFAULT == null ? groupObject != null : !GROUP_OBJECT_EDEFAULT.equals(groupObject);
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__GROUP_ALTERNATIVE:
				return groupAlternative != GROUP_ALTERNATIVE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (description: ");
		result.append(description);
		result.append(", baseScenario: ");
		result.append(baseScenario);
		result.append(", currentScenario: ");
		result.append(currentScenario);
		result.append(", complexity: ");
		result.append(complexity);
		result.append(", sortValue: ");
		result.append(sortValue);
		result.append(", groupSortValue: ");
		result.append(groupSortValue);
		result.append(", groupObject: ");
		result.append(groupObject);
		result.append(", groupAlternative: ");
		result.append(groupAlternative);
		result.append(')');
		return result.toString();
	}

} //ChangeSetTableGroupImpl
