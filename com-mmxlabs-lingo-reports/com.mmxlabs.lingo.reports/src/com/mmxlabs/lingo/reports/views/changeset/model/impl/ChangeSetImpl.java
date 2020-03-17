/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getMetricsToDefaultBase <em>Metrics To Default Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getMetricsToAlternativeBase <em>Metrics To Alternative Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getBaseScenario <em>Base Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getCurrentScenario <em>Current Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getAltBaseScenario <em>Alt Base Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getAltCurrentScenario <em>Alt Current Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getChangeSetRowsToDefaultBase <em>Change Set Rows To Default Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getChangeSetRowsToAlternativeBase <em>Change Set Rows To Alternative Base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getCurrentMetrics <em>Current Metrics</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getChangeDescription <em>Change Description</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl#getUserSettings <em>User Settings</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetImpl extends MinimalEObjectImpl.Container implements ChangeSet {
	/**
	 * The cached value of the '{@link #getMetricsToDefaultBase() <em>Metrics To Default Base</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetricsToDefaultBase()
	 * @generated
	 * @ordered
	 */
	protected DeltaMetrics metricsToDefaultBase;

	/**
	 * The cached value of the '{@link #getMetricsToAlternativeBase() <em>Metrics To Alternative Base</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetricsToAlternativeBase()
	 * @generated
	 * @ordered
	 */
	protected DeltaMetrics metricsToAlternativeBase;

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
	 * The default value of the '{@link #getAltBaseScenario() <em>Alt Base Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAltBaseScenario()
	 * @generated
	 * @ordered
	 */
	protected static final ScenarioResult ALT_BASE_SCENARIO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAltBaseScenario() <em>Alt Base Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAltBaseScenario()
	 * @generated
	 * @ordered
	 */
	protected ScenarioResult altBaseScenario = ALT_BASE_SCENARIO_EDEFAULT;

	/**
	 * The default value of the '{@link #getAltCurrentScenario() <em>Alt Current Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAltCurrentScenario()
	 * @generated
	 * @ordered
	 */
	protected static final ScenarioResult ALT_CURRENT_SCENARIO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAltCurrentScenario() <em>Alt Current Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAltCurrentScenario()
	 * @generated
	 * @ordered
	 */
	protected ScenarioResult altCurrentScenario = ALT_CURRENT_SCENARIO_EDEFAULT;

	/**
	 * The cached value of the '{@link #getChangeSetRowsToDefaultBase() <em>Change Set Rows To Default Base</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeSetRowsToDefaultBase()
	 * @generated
	 * @ordered
	 */
	protected EList<ChangeSetRow> changeSetRowsToDefaultBase;

	/**
	 * The cached value of the '{@link #getChangeSetRowsToAlternativeBase() <em>Change Set Rows To Alternative Base</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeSetRowsToAlternativeBase()
	 * @generated
	 * @ordered
	 */
	protected EList<ChangeSetRow> changeSetRowsToAlternativeBase;

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
	 * The default value of the '{@link #getChangeDescription() <em>Change Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeDescription()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeDescription CHANGE_DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getChangeDescription() <em>Change Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeDescription()
	 * @generated
	 * @ordered
	 */
	protected ChangeDescription changeDescription = CHANGE_DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getUserSettings() <em>User Settings</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserSettings()
	 * @generated
	 * @ordered
	 */
	protected static final UserSettings USER_SETTINGS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUserSettings() <em>User Settings</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserSettings()
	 * @generated
	 * @ordered
	 */
	protected UserSettings userSettings = USER_SETTINGS_EDEFAULT;

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
	@Override
	public DeltaMetrics getMetricsToDefaultBase() {
		return metricsToDefaultBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetricsToDefaultBase(DeltaMetrics newMetricsToDefaultBase, NotificationChain msgs) {
		DeltaMetrics oldMetricsToDefaultBase = metricsToDefaultBase;
		metricsToDefaultBase = newMetricsToDefaultBase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE, oldMetricsToDefaultBase, newMetricsToDefaultBase);
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
	public void setMetricsToDefaultBase(DeltaMetrics newMetricsToDefaultBase) {
		if (newMetricsToDefaultBase != metricsToDefaultBase) {
			NotificationChain msgs = null;
			if (metricsToDefaultBase != null)
				msgs = ((InternalEObject)metricsToDefaultBase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE, null, msgs);
			if (newMetricsToDefaultBase != null)
				msgs = ((InternalEObject)newMetricsToDefaultBase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE, null, msgs);
			msgs = basicSetMetricsToDefaultBase(newMetricsToDefaultBase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE, newMetricsToDefaultBase, newMetricsToDefaultBase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeltaMetrics getMetricsToAlternativeBase() {
		return metricsToAlternativeBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetricsToAlternativeBase(DeltaMetrics newMetricsToAlternativeBase, NotificationChain msgs) {
		DeltaMetrics oldMetricsToAlternativeBase = metricsToAlternativeBase;
		metricsToAlternativeBase = newMetricsToAlternativeBase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE, oldMetricsToAlternativeBase, newMetricsToAlternativeBase);
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
	public void setMetricsToAlternativeBase(DeltaMetrics newMetricsToAlternativeBase) {
		if (newMetricsToAlternativeBase != metricsToAlternativeBase) {
			NotificationChain msgs = null;
			if (metricsToAlternativeBase != null)
				msgs = ((InternalEObject)metricsToAlternativeBase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE, null, msgs);
			if (newMetricsToAlternativeBase != null)
				msgs = ((InternalEObject)newMetricsToAlternativeBase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE, null, msgs);
			msgs = basicSetMetricsToAlternativeBase(newMetricsToAlternativeBase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE, newMetricsToAlternativeBase, newMetricsToAlternativeBase));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__BASE_SCENARIO, oldBaseScenario, baseScenario));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO, oldCurrentScenario, currentScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScenarioResult getAltBaseScenario() {
		return altBaseScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAltBaseScenario(ScenarioResult newAltBaseScenario) {
		ScenarioResult oldAltBaseScenario = altBaseScenario;
		altBaseScenario = newAltBaseScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__ALT_BASE_SCENARIO, oldAltBaseScenario, altBaseScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScenarioResult getAltCurrentScenario() {
		return altCurrentScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAltCurrentScenario(ScenarioResult newAltCurrentScenario) {
		ScenarioResult oldAltCurrentScenario = altCurrentScenario;
		altCurrentScenario = newAltCurrentScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__ALT_CURRENT_SCENARIO, oldAltCurrentScenario, altCurrentScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ChangeSetRow> getChangeSetRowsToDefaultBase() {
		if (changeSetRowsToDefaultBase == null) {
			changeSetRowsToDefaultBase = new EObjectContainmentEList<ChangeSetRow>(ChangeSetRow.class, this, ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE);
		}
		return changeSetRowsToDefaultBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ChangeSetRow> getChangeSetRowsToAlternativeBase() {
		if (changeSetRowsToAlternativeBase == null) {
			changeSetRowsToAlternativeBase = new EObjectContainmentEList<ChangeSetRow>(ChangeSetRow.class, this, ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE);
		}
		return changeSetRowsToAlternativeBase;
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
	@Override
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeDescription getChangeDescription() {
		return changeDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setChangeDescription(ChangeDescription newChangeDescription) {
		ChangeDescription oldChangeDescription = changeDescription;
		changeDescription = newChangeDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__CHANGE_DESCRIPTION, oldChangeDescription, changeDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UserSettings getUserSettings() {
		return userSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUserSettings(UserSettings newUserSettings) {
		UserSettings oldUserSettings = userSettings;
		userSettings = newUserSettings;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET__USER_SETTINGS, oldUserSettings, userSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE:
				return basicSetMetricsToDefaultBase(null, msgs);
			case ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE:
				return basicSetMetricsToAlternativeBase(null, msgs);
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE:
				return ((InternalEList<?>)getChangeSetRowsToDefaultBase()).basicRemove(otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE:
				return ((InternalEList<?>)getChangeSetRowsToAlternativeBase()).basicRemove(otherEnd, msgs);
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
			case ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE:
				return getMetricsToDefaultBase();
			case ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE:
				return getMetricsToAlternativeBase();
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO:
				return getBaseScenario();
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				return getCurrentScenario();
			case ChangesetPackage.CHANGE_SET__ALT_BASE_SCENARIO:
				return getAltBaseScenario();
			case ChangesetPackage.CHANGE_SET__ALT_CURRENT_SCENARIO:
				return getAltCurrentScenario();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE:
				return getChangeSetRowsToDefaultBase();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE:
				return getChangeSetRowsToAlternativeBase();
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
				return getCurrentMetrics();
			case ChangesetPackage.CHANGE_SET__DESCRIPTION:
				return getDescription();
			case ChangesetPackage.CHANGE_SET__CHANGE_DESCRIPTION:
				return getChangeDescription();
			case ChangesetPackage.CHANGE_SET__USER_SETTINGS:
				return getUserSettings();
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
			case ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE:
				setMetricsToDefaultBase((DeltaMetrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE:
				setMetricsToAlternativeBase((DeltaMetrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO:
				setBaseScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				setCurrentScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__ALT_BASE_SCENARIO:
				setAltBaseScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__ALT_CURRENT_SCENARIO:
				setAltCurrentScenario((ScenarioResult)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE:
				getChangeSetRowsToDefaultBase().clear();
				getChangeSetRowsToDefaultBase().addAll((Collection<? extends ChangeSetRow>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE:
				getChangeSetRowsToAlternativeBase().clear();
				getChangeSetRowsToAlternativeBase().addAll((Collection<? extends ChangeSetRow>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
				setCurrentMetrics((Metrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_DESCRIPTION:
				setChangeDescription((ChangeDescription)newValue);
				return;
			case ChangesetPackage.CHANGE_SET__USER_SETTINGS:
				setUserSettings((UserSettings)newValue);
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
			case ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE:
				setMetricsToDefaultBase((DeltaMetrics)null);
				return;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE:
				setMetricsToAlternativeBase((DeltaMetrics)null);
				return;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO:
				setBaseScenario(BASE_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				setCurrentScenario(CURRENT_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__ALT_BASE_SCENARIO:
				setAltBaseScenario(ALT_BASE_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__ALT_CURRENT_SCENARIO:
				setAltCurrentScenario(ALT_CURRENT_SCENARIO_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE:
				getChangeSetRowsToDefaultBase().clear();
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE:
				getChangeSetRowsToAlternativeBase().clear();
				return;
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
				setCurrentMetrics((Metrics)null);
				return;
			case ChangesetPackage.CHANGE_SET__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__CHANGE_DESCRIPTION:
				setChangeDescription(CHANGE_DESCRIPTION_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET__USER_SETTINGS:
				setUserSettings(USER_SETTINGS_EDEFAULT);
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
			case ChangesetPackage.CHANGE_SET__METRICS_TO_DEFAULT_BASE:
				return metricsToDefaultBase != null;
			case ChangesetPackage.CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE:
				return metricsToAlternativeBase != null;
			case ChangesetPackage.CHANGE_SET__BASE_SCENARIO:
				return BASE_SCENARIO_EDEFAULT == null ? baseScenario != null : !BASE_SCENARIO_EDEFAULT.equals(baseScenario);
			case ChangesetPackage.CHANGE_SET__CURRENT_SCENARIO:
				return CURRENT_SCENARIO_EDEFAULT == null ? currentScenario != null : !CURRENT_SCENARIO_EDEFAULT.equals(currentScenario);
			case ChangesetPackage.CHANGE_SET__ALT_BASE_SCENARIO:
				return ALT_BASE_SCENARIO_EDEFAULT == null ? altBaseScenario != null : !ALT_BASE_SCENARIO_EDEFAULT.equals(altBaseScenario);
			case ChangesetPackage.CHANGE_SET__ALT_CURRENT_SCENARIO:
				return ALT_CURRENT_SCENARIO_EDEFAULT == null ? altCurrentScenario != null : !ALT_CURRENT_SCENARIO_EDEFAULT.equals(altCurrentScenario);
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE:
				return changeSetRowsToDefaultBase != null && !changeSetRowsToDefaultBase.isEmpty();
			case ChangesetPackage.CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE:
				return changeSetRowsToAlternativeBase != null && !changeSetRowsToAlternativeBase.isEmpty();
			case ChangesetPackage.CHANGE_SET__CURRENT_METRICS:
				return currentMetrics != null;
			case ChangesetPackage.CHANGE_SET__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case ChangesetPackage.CHANGE_SET__CHANGE_DESCRIPTION:
				return CHANGE_DESCRIPTION_EDEFAULT == null ? changeDescription != null : !CHANGE_DESCRIPTION_EDEFAULT.equals(changeDescription);
			case ChangesetPackage.CHANGE_SET__USER_SETTINGS:
				return USER_SETTINGS_EDEFAULT == null ? userSettings != null : !USER_SETTINGS_EDEFAULT.equals(userSettings);
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
		result.append(" (baseScenario: ");
		result.append(baseScenario);
		result.append(", currentScenario: ");
		result.append(currentScenario);
		result.append(", altBaseScenario: ");
		result.append(altBaseScenario);
		result.append(", altCurrentScenario: ");
		result.append(altCurrentScenario);
		result.append(", description: ");
		result.append(description);
		result.append(", changeDescription: ");
		result.append(changeDescription);
		result.append(", userSettings: ");
		result.append(userSettings);
		result.append(')');
		return result.toString();
	}

} //ChangeSetImpl
