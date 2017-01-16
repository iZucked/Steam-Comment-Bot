/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

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

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getMetricsToBase <em>Metrics To Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getMetricsToPrevious <em>Metrics To Previous</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getBaseScenarioRef <em>Base Scenario Ref</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getPrevScenarioRef <em>Prev Scenario Ref</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getCurrentScenarioRef <em>Current Scenario Ref</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getBaseScenario <em>Base Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getPrevScenario <em>Prev Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getCurrentScenario <em>Current Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getChangeSetRowsToBase <em>Change Set Rows To Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getChangeSetRowsToPrevious <em>Change Set Rows To Previous</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getCurrentMetrics <em>Current Metrics</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetImpl extends MinimalEObjectImpl.Container implements ChangeSet {
	/**
	 * The cached value of the '{@link #getMetricsToBase() <em>Metrics To Base</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetricsToBase()
	 * @generated
	 * @ordered
	 */
	protected DeltaMetrics metricsToBase;

	/**
	 * The cached value of the '{@link #getMetricsToPrevious() <em>Metrics To Previous</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetricsToPrevious()
	 * @generated
	 * @ordered
	 */
	protected DeltaMetrics metricsToPrevious;

	/**
	 * The cached value of the '{@link #getBaseScenarioRef() <em>Base Scenario Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseScenarioRef()
	 * @generated
	 * @ordered
	 */
	protected ModelReference baseScenarioRef;

	/**
	 * The cached value of the '{@link #getPrevScenarioRef() <em>Prev Scenario Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrevScenarioRef()
	 * @generated
	 * @ordered
	 */
	protected ModelReference prevScenarioRef;

	/**
	 * The cached value of the '{@link #getCurrentScenarioRef() <em>Current Scenario Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentScenarioRef()
	 * @generated
	 * @ordered
	 */
	protected ModelReference currentScenarioRef;

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
	 * The default value of the '{@link #getPrevScenario() <em>Prev Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrevScenario()
	 * @generated
	 * @ordered
	 */
	protected static final ScenarioResult PREV_SCENARIO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrevScenario() <em>Prev Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrevScenario()
	 * @generated
	 * @ordered
	 */
	protected ScenarioResult prevScenario = PREV_SCENARIO_EDEFAULT;

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
	 * The cached value of the '{@link #getChangeSetRowsToBase() <em>Change Set Rows To Base</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeSetRowsToBase()
	 * @generated
	 * @ordered
	 */
	protected EList<ChangeSetRow> changeSetRowsToBase;

	/**
	 * The cached value of the '{@link #getChangeSetRowsToPrevious() <em>Change Set Rows To Previous</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeSetRowsToPrevious()
	 * @generated
	 * @ordered
	 */
	protected EList<ChangeSetRow> changeSetRowsToPrevious;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.CHANGE_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeltaMetrics getMetricsToBase() {
		return metricsToBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetricsToBase(DeltaMetrics newMetricsToBase, NotificationChain msgs) {
		DeltaMetrics oldMetricsToBase = metricsToBase;
		metricsToBase = newMetricsToBase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__METRICS_TO_BASE, oldMetricsToBase, newMetricsToBase);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMetricsToBase(DeltaMetrics newMetricsToBase) {
		if (newMetricsToBase != metricsToBase) {
			NotificationChain msgs = null;
			if (metricsToBase != null)
				msgs = ((InternalEObject)metricsToBase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__METRICS_TO_BASE, null, msgs);
			if (newMetricsToBase != null)
				msgs = ((InternalEObject)newMetricsToBase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__METRICS_TO_BASE, null, msgs);
			msgs = basicSetMetricsToBase(newMetricsToBase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__METRICS_TO_BASE, newMetricsToBase, newMetricsToBase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeltaMetrics getMetricsToPrevious() {
		return metricsToPrevious;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetricsToPrevious(DeltaMetrics newMetricsToPrevious, NotificationChain msgs) {
		DeltaMetrics oldMetricsToPrevious = metricsToPrevious;
		metricsToPrevious = newMetricsToPrevious;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS, oldMetricsToPrevious, newMetricsToPrevious);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMetricsToPrevious(DeltaMetrics newMetricsToPrevious) {
		if (newMetricsToPrevious != metricsToPrevious) {
			NotificationChain msgs = null;
			if (metricsToPrevious != null)
				msgs = ((InternalEObject)metricsToPrevious).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS, null, msgs);
			if (newMetricsToPrevious != null)
				msgs = ((InternalEObject)newMetricsToPrevious).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS, null, msgs);
			msgs = basicSetMetricsToPrevious(newMetricsToPrevious, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS, newMetricsToPrevious, newMetricsToPrevious));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference getBaseScenarioRef() {
		if (baseScenarioRef != null && baseScenarioRef.eIsProxy()) {
			InternalEObject oldBaseScenarioRef = (InternalEObject)baseScenarioRef;
			baseScenarioRef = (ModelReference)eResolveProxy(oldBaseScenarioRef);
			if (baseScenarioRef != oldBaseScenarioRef) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET__BASE_SCENARIO_REF, oldBaseScenarioRef, baseScenarioRef));
			}
		}
		return baseScenarioRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference basicGetBaseScenarioRef() {
		return baseScenarioRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseScenarioRef(ModelReference newBaseScenarioRef) {
		ModelReference oldBaseScenarioRef = baseScenarioRef;
		baseScenarioRef = newBaseScenarioRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__BASE_SCENARIO_REF, oldBaseScenarioRef, baseScenarioRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference getPrevScenarioRef() {
		if (prevScenarioRef != null && prevScenarioRef.eIsProxy()) {
			InternalEObject oldPrevScenarioRef = (InternalEObject)prevScenarioRef;
			prevScenarioRef = (ModelReference)eResolveProxy(oldPrevScenarioRef);
			if (prevScenarioRef != oldPrevScenarioRef) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET__PREV_SCENARIO_REF, oldPrevScenarioRef, prevScenarioRef));
			}
		}
		return prevScenarioRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference basicGetPrevScenarioRef() {
		return prevScenarioRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrevScenarioRef(ModelReference newPrevScenarioRef) {
		ModelReference oldPrevScenarioRef = prevScenarioRef;
		prevScenarioRef = newPrevScenarioRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__PREV_SCENARIO_REF, oldPrevScenarioRef, prevScenarioRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference getCurrentScenarioRef() {
		if (currentScenarioRef != null && currentScenarioRef.eIsProxy()) {
			InternalEObject oldCurrentScenarioRef = (InternalEObject)currentScenarioRef;
			currentScenarioRef = (ModelReference)eResolveProxy(oldCurrentScenarioRef);
			if (currentScenarioRef != oldCurrentScenarioRef) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO_REF, oldCurrentScenarioRef, currentScenarioRef));
			}
		}
		return currentScenarioRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference basicGetCurrentScenarioRef() {
		return currentScenarioRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentScenarioRef(ModelReference newCurrentScenarioRef) {
		ModelReference oldCurrentScenarioRef = currentScenarioRef;
		currentScenarioRef = newCurrentScenarioRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO_REF, oldCurrentScenarioRef, currentScenarioRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioResult getBaseScenario() {
		return baseScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseScenario(ScenarioResult newBaseScenario) {
		ScenarioResult oldBaseScenario = baseScenario;
		baseScenario = newBaseScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__BASE_SCENARIO, oldBaseScenario, baseScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioResult getPrevScenario() {
		return prevScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrevScenario(ScenarioResult newPrevScenario) {
		ScenarioResult oldPrevScenario = prevScenario;
		prevScenario = newPrevScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__PREV_SCENARIO, oldPrevScenario, prevScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioResult getCurrentScenario() {
		return currentScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentScenario(ScenarioResult newCurrentScenario) {
		ScenarioResult oldCurrentScenario = currentScenario;
		currentScenario = newCurrentScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO, oldCurrentScenario, currentScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ChangeSetRow> getChangeSetRowsToBase() {
		if (changeSetRowsToBase == null) {
			changeSetRowsToBase = new EObjectContainmentEList<ChangeSetRow>(ChangeSetRow.class, this, ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE);
		}
		return changeSetRowsToBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ChangeSetRow> getChangeSetRowsToPrevious() {
		if (changeSetRowsToPrevious == null) {
			changeSetRowsToPrevious = new EObjectContainmentEList<ChangeSetRow>(ChangeSetRow.class, this, ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS);
		}
		return changeSetRowsToPrevious;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__CURRENT_METRICS, oldCurrentMetrics, newCurrentMetrics);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentMetrics(Metrics newCurrentMetrics) {
		if (newCurrentMetrics != currentMetrics) {
			NotificationChain msgs = null;
			if (currentMetrics != null)
				msgs = ((InternalEObject)currentMetrics).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__CURRENT_METRICS, null, msgs);
			if (newCurrentMetrics != null)
				msgs = ((InternalEObject)newCurrentMetrics).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__CURRENT_METRICS, null, msgs);
			msgs = basicSetCurrentMetrics(newCurrentMetrics, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__CURRENT_METRICS, newCurrentMetrics, newCurrentMetrics));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET__METRICS_TO_BASE:
				return basicSetMetricsToBase(null, msgs);
			case ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS:
				return basicSetMetricsToPrevious(null, msgs);
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				return ((InternalEList<?>)getChangeSetRowsToBase()).basicRemove(otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				return ((InternalEList<?>)getChangeSetRowsToPrevious()).basicRemove(otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
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
			case ChangesetPackage.CHANGE_SET__METRICS_TO_BASE:
				return getMetricsToBase();
			case ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS:
				return getMetricsToPrevious();
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO_REF:
				if (resolve) return getBaseScenarioRef();
				return basicGetBaseScenarioRef();
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO_REF:
				if (resolve) return getPrevScenarioRef();
				return basicGetPrevScenarioRef();
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO_REF:
				if (resolve) return getCurrentScenarioRef();
				return basicGetCurrentScenarioRef();
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO:
				return getBaseScenario();
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO:
				return getPrevScenario();
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				return getCurrentScenario();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				return getChangeSetRowsToBase();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				return getChangeSetRowsToPrevious();
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
				return getCurrentMetrics();
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
			case ChangesetPackage.CHANGE_SET__METRICS_TO_BASE:
				setMetricsToBase((DeltaMetrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS:
				setMetricsToPrevious((DeltaMetrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO_REF:
				setBaseScenarioRef((ModelReference)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO_REF:
				setPrevScenarioRef((ModelReference)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO_REF:
				setCurrentScenarioRef((ModelReference)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO:
				setBaseScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO:
				setPrevScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				setCurrentScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				getChangeSetRowsToBase().clear();
				getChangeSetRowsToBase().addAll((Collection<? extends ChangeSetRow>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				getChangeSetRowsToPrevious().clear();
				getChangeSetRowsToPrevious().addAll((Collection<? extends ChangeSetRow>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
				setCurrentMetrics((Metrics)newValue);
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
			case ChangesetPackage.CHANGE_SET__METRICS_TO_BASE:
				setMetricsToBase((DeltaMetrics)null);
				return;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS:
				setMetricsToPrevious((DeltaMetrics)null);
				return;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO_REF:
				setBaseScenarioRef((ModelReference)null);
				return;
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO_REF:
				setPrevScenarioRef((ModelReference)null);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO_REF:
				setCurrentScenarioRef((ModelReference)null);
				return;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO:
				setBaseScenario(BASE_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO:
				setPrevScenario(PREV_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				setCurrentScenario(CURRENT_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				getChangeSetRowsToBase().clear();
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				getChangeSetRowsToPrevious().clear();
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
				setCurrentMetrics((Metrics)null);
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
			case ChangesetPackage.CHANGE_SET__METRICS_TO_BASE:
				return metricsToBase != null;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS:
				return metricsToPrevious != null;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO_REF:
				return baseScenarioRef != null;
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO_REF:
				return prevScenarioRef != null;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO_REF:
				return currentScenarioRef != null;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO:
				return BASE_SCENARIO_EDEFAULT == null ? baseScenario != null : !BASE_SCENARIO_EDEFAULT.equals(baseScenario);
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO:
				return PREV_SCENARIO_EDEFAULT == null ? prevScenario != null : !PREV_SCENARIO_EDEFAULT.equals(prevScenario);
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				return CURRENT_SCENARIO_EDEFAULT == null ? currentScenario != null : !CURRENT_SCENARIO_EDEFAULT.equals(currentScenario);
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				return changeSetRowsToBase != null && !changeSetRowsToBase.isEmpty();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				return changeSetRowsToPrevious != null && !changeSetRowsToPrevious.isEmpty();
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
				return currentMetrics != null;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (baseScenario: ");
		result.append(baseScenario);
		result.append(", prevScenario: ");
		result.append(prevScenario);
		result.append(", currentScenario: ");
		result.append(currentScenario);
		result.append(')');
		return result.toString();
	}

} //ChangeSetImpl
