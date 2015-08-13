/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.Change;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;

import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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
 * An implementation of the model object '<em><b>Change Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getChangesToBase <em>Changes To Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getChangesToPrevious <em>Changes To Previous</em>}</li>
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
 * </ul>
 *
 * @generated
 */
public class ChangeSetImpl extends MinimalEObjectImpl.Container implements ChangeSet {
	/**
	 * The cached value of the '{@link #getChangesToBase() <em>Changes To Base</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangesToBase()
	 * @generated
	 * @ordered
	 */
	protected EList<Change> changesToBase;

	/**
	 * The cached value of the '{@link #getChangesToPrevious() <em>Changes To Previous</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangesToPrevious()
	 * @generated
	 * @ordered
	 */
	protected EList<Change> changesToPrevious;

	/**
	 * The cached value of the '{@link #getMetricsToBase() <em>Metrics To Base</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetricsToBase()
	 * @generated
	 * @ordered
	 */
	protected Metrics metricsToBase;

	/**
	 * The cached value of the '{@link #getMetricsToPrevious() <em>Metrics To Previous</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetricsToPrevious()
	 * @generated
	 * @ordered
	 */
	protected Metrics metricsToPrevious;

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
	 * The cached value of the '{@link #getBaseScenario() <em>Base Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseScenario()
	 * @generated
	 * @ordered
	 */
	protected ScenarioInstance baseScenario;

	/**
	 * The cached value of the '{@link #getPrevScenario() <em>Prev Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrevScenario()
	 * @generated
	 * @ordered
	 */
	protected ScenarioInstance prevScenario;

	/**
	 * The cached value of the '{@link #getCurrentScenario() <em>Current Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentScenario()
	 * @generated
	 * @ordered
	 */
	protected ScenarioInstance currentScenario;

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
	public EList<Change> getChangesToBase() {
		if (changesToBase == null) {
			changesToBase = new EObjectContainmentEList<Change>(Change.class, this, ChangesetPackage.CHANGE_SET__CHANGES_TO_BASE);
		}
		return changesToBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Change> getChangesToPrevious() {
		if (changesToPrevious == null) {
			changesToPrevious = new EObjectContainmentEList<Change>(Change.class, this, ChangesetPackage.CHANGE_SET__CHANGES_TO_PREVIOUS);
		}
		return changesToPrevious;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metrics getMetricsToBase() {
		return metricsToBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetricsToBase(Metrics newMetricsToBase, NotificationChain msgs) {
		Metrics oldMetricsToBase = metricsToBase;
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
	public void setMetricsToBase(Metrics newMetricsToBase) {
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
	public Metrics getMetricsToPrevious() {
		return metricsToPrevious;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetricsToPrevious(Metrics newMetricsToPrevious, NotificationChain msgs) {
		Metrics oldMetricsToPrevious = metricsToPrevious;
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
	public void setMetricsToPrevious(Metrics newMetricsToPrevious) {
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
	public ScenarioInstance getBaseScenario() {
		if (baseScenario != null && baseScenario.eIsProxy()) {
			InternalEObject oldBaseScenario = (InternalEObject)baseScenario;
			baseScenario = (ScenarioInstance)eResolveProxy(oldBaseScenario);
			if (baseScenario != oldBaseScenario) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET__BASE_SCENARIO, oldBaseScenario, baseScenario));
			}
		}
		return baseScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance basicGetBaseScenario() {
		return baseScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseScenario(ScenarioInstance newBaseScenario) {
		ScenarioInstance oldBaseScenario = baseScenario;
		baseScenario = newBaseScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__BASE_SCENARIO, oldBaseScenario, baseScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance getPrevScenario() {
		if (prevScenario != null && prevScenario.eIsProxy()) {
			InternalEObject oldPrevScenario = (InternalEObject)prevScenario;
			prevScenario = (ScenarioInstance)eResolveProxy(oldPrevScenario);
			if (prevScenario != oldPrevScenario) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET__PREV_SCENARIO, oldPrevScenario, prevScenario));
			}
		}
		return prevScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance basicGetPrevScenario() {
		return prevScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrevScenario(ScenarioInstance newPrevScenario) {
		ScenarioInstance oldPrevScenario = prevScenario;
		prevScenario = newPrevScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__PREV_SCENARIO, oldPrevScenario, prevScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance getCurrentScenario() {
		if (currentScenario != null && currentScenario.eIsProxy()) {
			InternalEObject oldCurrentScenario = (InternalEObject)currentScenario;
			currentScenario = (ScenarioInstance)eResolveProxy(oldCurrentScenario);
			if (currentScenario != oldCurrentScenario) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO, oldCurrentScenario, currentScenario));
			}
		}
		return currentScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance basicGetCurrentScenario() {
		return currentScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentScenario(ScenarioInstance newCurrentScenario) {
		ScenarioInstance oldCurrentScenario = currentScenario;
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_BASE:
				return ((InternalEList<?>)getChangesToBase()).basicRemove(otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_PREVIOUS:
				return ((InternalEList<?>)getChangesToPrevious()).basicRemove(otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET__METRICS_TO_BASE:
				return basicSetMetricsToBase(null, msgs);
			case ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS:
				return basicSetMetricsToPrevious(null, msgs);
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				return ((InternalEList<?>)getChangeSetRowsToBase()).basicRemove(otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				return ((InternalEList<?>)getChangeSetRowsToPrevious()).basicRemove(otherEnd, msgs);
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
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_BASE:
				return getChangesToBase();
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_PREVIOUS:
				return getChangesToPrevious();
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
				if (resolve) return getBaseScenario();
				return basicGetBaseScenario();
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO:
				if (resolve) return getPrevScenario();
				return basicGetPrevScenario();
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				if (resolve) return getCurrentScenario();
				return basicGetCurrentScenario();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				return getChangeSetRowsToBase();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				return getChangeSetRowsToPrevious();
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
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_BASE:
				getChangesToBase().clear();
				getChangesToBase().addAll((Collection<? extends Change>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_PREVIOUS:
				getChangesToPrevious().clear();
				getChangesToPrevious().addAll((Collection<? extends Change>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_BASE:
				setMetricsToBase((Metrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS:
				setMetricsToPrevious((Metrics)newValue);
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
				setBaseScenario((ScenarioInstance)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO:
				setPrevScenario((ScenarioInstance)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				setCurrentScenario((ScenarioInstance)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				getChangeSetRowsToBase().clear();
				getChangeSetRowsToBase().addAll((Collection<? extends ChangeSetRow>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				getChangeSetRowsToPrevious().clear();
				getChangeSetRowsToPrevious().addAll((Collection<? extends ChangeSetRow>)newValue);
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
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_BASE:
				getChangesToBase().clear();
				return;
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_PREVIOUS:
				getChangesToPrevious().clear();
				return;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_BASE:
				setMetricsToBase((Metrics)null);
				return;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_PREVIOUS:
				setMetricsToPrevious((Metrics)null);
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
				setBaseScenario((ScenarioInstance)null);
				return;
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO:
				setPrevScenario((ScenarioInstance)null);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				setCurrentScenario((ScenarioInstance)null);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				getChangeSetRowsToBase().clear();
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				getChangeSetRowsToPrevious().clear();
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
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_BASE:
				return changesToBase != null && !changesToBase.isEmpty();
			case ChangesetPackage.CHANGE_SET__CHANGES_TO_PREVIOUS:
				return changesToPrevious != null && !changesToPrevious.isEmpty();
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
				return baseScenario != null;
			case ChangesetPackage.CHANGE_SET__PREV_SCENARIO:
				return prevScenario != null;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				return currentScenario != null;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_BASE:
				return changeSetRowsToBase != null && !changeSetRowsToBase.isEmpty();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS:
				return changeSetRowsToPrevious != null && !changeSetRowsToPrevious.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ChangeSetImpl
