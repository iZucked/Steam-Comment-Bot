/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SolutionOption;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Solution Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#isHasDualModeSolutions <em>Has Dual Mode Solutions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#isPortfolioBreakEvenMode <em>Portfolio Break Even Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getUserSettings <em>User Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getExtraSlots <em>Extra Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getBaseOption <em>Base Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getOptions <em>Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getExtraVesselEvents <em>Extra Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getExtraVesselAvailabilities <em>Extra Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getExtraCharterInMarkets <em>Extra Charter In Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#isUseScenarioBase <em>Use Scenario Base</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractSolutionSetImpl extends UUIDObjectImpl implements AbstractSolutionSet {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isHasDualModeSolutions() <em>Has Dual Mode Solutions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasDualModeSolutions()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HAS_DUAL_MODE_SOLUTIONS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isHasDualModeSolutions() <em>Has Dual Mode Solutions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasDualModeSolutions()
	 * @generated
	 * @ordered
	 */
	protected boolean hasDualModeSolutions = HAS_DUAL_MODE_SOLUTIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #isPortfolioBreakEvenMode() <em>Portfolio Break Even Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortfolioBreakEvenMode()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PORTFOLIO_BREAK_EVEN_MODE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPortfolioBreakEvenMode() <em>Portfolio Break Even Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPortfolioBreakEvenMode()
	 * @generated
	 * @ordered
	 */
	protected boolean portfolioBreakEvenMode = PORTFOLIO_BREAK_EVEN_MODE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getUserSettings() <em>User Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserSettings()
	 * @generated
	 * @ordered
	 */
	protected UserSettings userSettings;

	/**
	 * The cached value of the '{@link #getExtraSlots() <em>Extra Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot> extraSlots;

	/**
	 * The cached value of the '{@link #getBaseOption() <em>Base Option</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseOption()
	 * @generated
	 * @ordered
	 */
	protected SolutionOption baseOption;

	/**
	 * The cached value of the '{@link #getOptions() <em>Options</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<SolutionOption> options;

	/**
	 * The cached value of the '{@link #getExtraVesselEvents() <em>Extra Vessel Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraVesselEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEvent> extraVesselEvents;

	/**
	 * The cached value of the '{@link #getExtraVesselAvailabilities() <em>Extra Vessel Availabilities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraVesselAvailabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselAvailability> extraVesselAvailabilities;

	/**
	 * The cached value of the '{@link #getCharterInMarketOverrides() <em>Charter In Market Overrides</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInMarketOverrides()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarketOverride> charterInMarketOverrides;

	/**
	 * The cached value of the '{@link #getExtraCharterInMarkets() <em>Extra Charter In Markets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraCharterInMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarket> extraCharterInMarkets;

	/**
	 * The default value of the '{@link #isUseScenarioBase() <em>Use Scenario Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseScenarioBase()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_SCENARIO_BASE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isUseScenarioBase() <em>Use Scenario Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseScenarioBase()
	 * @generated
	 * @ordered
	 */
	protected boolean useScenarioBase = USE_SCENARIO_BASE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractSolutionSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isHasDualModeSolutions() {
		return hasDualModeSolutions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHasDualModeSolutions(boolean newHasDualModeSolutions) {
		boolean oldHasDualModeSolutions = hasDualModeSolutions;
		hasDualModeSolutions = newHasDualModeSolutions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS, oldHasDualModeSolutions, hasDualModeSolutions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isPortfolioBreakEvenMode() {
		return portfolioBreakEvenMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortfolioBreakEvenMode(boolean newPortfolioBreakEvenMode) {
		boolean oldPortfolioBreakEvenMode = portfolioBreakEvenMode;
		portfolioBreakEvenMode = newPortfolioBreakEvenMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE, oldPortfolioBreakEvenMode, portfolioBreakEvenMode));
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
	public NotificationChain basicSetUserSettings(UserSettings newUserSettings, NotificationChain msgs) {
		UserSettings oldUserSettings = userSettings;
		userSettings = newUserSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS, oldUserSettings, newUserSettings);
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
	public void setUserSettings(UserSettings newUserSettings) {
		if (newUserSettings != userSettings) {
			NotificationChain msgs = null;
			if (userSettings != null)
				msgs = ((InternalEObject)userSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS, null, msgs);
			if (newUserSettings != null)
				msgs = ((InternalEObject)newUserSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS, null, msgs);
			msgs = basicSetUserSettings(newUserSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS, newUserSettings, newUserSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SolutionOption> getOptions() {
		if (options == null) {
			options = new EObjectContainmentEList<SolutionOption>(SolutionOption.class, this, AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS);
		}
		return options;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselEvent> getExtraVesselEvents() {
		if (extraVesselEvents == null) {
			extraVesselEvents = new EObjectContainmentEList<VesselEvent>(VesselEvent.class, this, AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS);
		}
		return extraVesselEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselAvailability> getExtraVesselAvailabilities() {
		if (extraVesselAvailabilities == null) {
			extraVesselAvailabilities = new EObjectContainmentEList<VesselAvailability>(VesselAvailability.class, this, AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_AVAILABILITIES);
		}
		return extraVesselAvailabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CharterInMarketOverride> getCharterInMarketOverrides() {
		if (charterInMarketOverrides == null) {
			charterInMarketOverrides = new EObjectContainmentEList<CharterInMarketOverride>(CharterInMarketOverride.class, this, AnalyticsPackage.ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES);
		}
		return charterInMarketOverrides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CharterInMarket> getExtraCharterInMarkets() {
		if (extraCharterInMarkets == null) {
			extraCharterInMarkets = new EObjectContainmentEList<CharterInMarket>(CharterInMarket.class, this, AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS);
		}
		return extraCharterInMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isUseScenarioBase() {
		return useScenarioBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUseScenarioBase(boolean newUseScenarioBase) {
		boolean oldUseScenarioBase = useScenarioBase;
		useScenarioBase = newUseScenarioBase;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE, oldUseScenarioBase, useScenarioBase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Slot> getExtraSlots() {
		if (extraSlots == null) {
			extraSlots = new EObjectContainmentEList<Slot>(Slot.class, this, AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS);
		}
		return extraSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SolutionOption getBaseOption() {
		return baseOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseOption(SolutionOption newBaseOption, NotificationChain msgs) {
		SolutionOption oldBaseOption = baseOption;
		baseOption = newBaseOption;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION, oldBaseOption, newBaseOption);
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
	public void setBaseOption(SolutionOption newBaseOption) {
		if (newBaseOption != baseOption) {
			NotificationChain msgs = null;
			if (baseOption != null)
				msgs = ((InternalEObject)baseOption).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION, null, msgs);
			if (newBaseOption != null)
				msgs = ((InternalEObject)newBaseOption).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION, null, msgs);
			msgs = basicSetBaseOption(newBaseOption, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION, newBaseOption, newBaseOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				return basicSetUserSettings(null, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				return ((InternalEList<?>)getExtraSlots()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION:
				return basicSetBaseOption(null, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				return ((InternalEList<?>)getOptions()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS:
				return ((InternalEList<?>)getExtraVesselEvents()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_AVAILABILITIES:
				return ((InternalEList<?>)getExtraVesselAvailabilities()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES:
				return ((InternalEList<?>)getCharterInMarketOverrides()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS:
				return ((InternalEList<?>)getExtraCharterInMarkets()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
				return getName();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS:
				return isHasDualModeSolutions();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE:
				return isPortfolioBreakEvenMode();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				return getUserSettings();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				return getExtraSlots();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION:
				return getBaseOption();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				return getOptions();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS:
				return getExtraVesselEvents();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_AVAILABILITIES:
				return getExtraVesselAvailabilities();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES:
				return getCharterInMarketOverrides();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS:
				return getExtraCharterInMarkets();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE:
				return isUseScenarioBase();
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
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
				setName((String)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS:
				setHasDualModeSolutions((Boolean)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE:
				setPortfolioBreakEvenMode((Boolean)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				setUserSettings((UserSettings)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				getExtraSlots().clear();
				getExtraSlots().addAll((Collection<? extends Slot>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION:
				setBaseOption((SolutionOption)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				getOptions().clear();
				getOptions().addAll((Collection<? extends SolutionOption>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS:
				getExtraVesselEvents().clear();
				getExtraVesselEvents().addAll((Collection<? extends VesselEvent>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_AVAILABILITIES:
				getExtraVesselAvailabilities().clear();
				getExtraVesselAvailabilities().addAll((Collection<? extends VesselAvailability>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES:
				getCharterInMarketOverrides().clear();
				getCharterInMarketOverrides().addAll((Collection<? extends CharterInMarketOverride>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS:
				getExtraCharterInMarkets().clear();
				getExtraCharterInMarkets().addAll((Collection<? extends CharterInMarket>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE:
				setUseScenarioBase((Boolean)newValue);
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
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS:
				setHasDualModeSolutions(HAS_DUAL_MODE_SOLUTIONS_EDEFAULT);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE:
				setPortfolioBreakEvenMode(PORTFOLIO_BREAK_EVEN_MODE_EDEFAULT);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				setUserSettings((UserSettings)null);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				getExtraSlots().clear();
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION:
				setBaseOption((SolutionOption)null);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				getOptions().clear();
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS:
				getExtraVesselEvents().clear();
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_AVAILABILITIES:
				getExtraVesselAvailabilities().clear();
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES:
				getCharterInMarketOverrides().clear();
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS:
				getExtraCharterInMarkets().clear();
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE:
				setUseScenarioBase(USE_SCENARIO_BASE_EDEFAULT);
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
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS:
				return hasDualModeSolutions != HAS_DUAL_MODE_SOLUTIONS_EDEFAULT;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE:
				return portfolioBreakEvenMode != PORTFOLIO_BREAK_EVEN_MODE_EDEFAULT;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				return userSettings != null;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				return extraSlots != null && !extraSlots.isEmpty();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION:
				return baseOption != null;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				return options != null && !options.isEmpty();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS:
				return extraVesselEvents != null && !extraVesselEvents.isEmpty();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_AVAILABILITIES:
				return extraVesselAvailabilities != null && !extraVesselAvailabilities.isEmpty();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES:
				return charterInMarketOverrides != null && !charterInMarketOverrides.isEmpty();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS:
				return extraCharterInMarkets != null && !extraCharterInMarkets.isEmpty();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE:
				return useScenarioBase != USE_SCENARIO_BASE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", hasDualModeSolutions: ");
		result.append(hasDualModeSolutions);
		result.append(", portfolioBreakEvenMode: ");
		result.append(portfolioBreakEvenMode);
		result.append(", useScenarioBase: ");
		result.append(useScenarioBase);
		result.append(')');
		return result.toString();
	}

} //AbstractSolutionSetImpl
