/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;

import java.time.LocalDate;
import java.time.YearMonth;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>User Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getPeriodStartDate <em>Period Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getPeriodEnd <em>Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isShippingOnly <em>Shipping Only</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isGenerateCharterOuts <em>Generate Charter Outs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isWithSpotCargoMarkets <em>With Spot Cargo Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isBuildActionSets <em>Build Action Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getSimilarityMode <em>Similarity Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isCleanStateOptimisation <em>Clean State Optimisation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getFloatingDaysLimit <em>Floating Days Limit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UserSettingsImpl extends EObjectImpl implements UserSettings {
	/**
	 * The default value of the '{@link #getPeriodStartDate() <em>Period Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PERIOD_START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPeriodStartDate() <em>Period Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodStartDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate periodStartDate = PERIOD_START_DATE_EDEFAULT;

	/**
	 * This is true if the Period Start Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean periodStartDateESet;

	/**
	 * The default value of the '{@link #getPeriodEnd() <em>Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodEnd()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth PERIOD_END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPeriodEnd() <em>Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodEnd()
	 * @generated
	 * @ordered
	 */
	protected YearMonth periodEnd = PERIOD_END_EDEFAULT;

	/**
	 * This is true if the Period End attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean periodEndESet;

	/**
	 * The default value of the '{@link #isShippingOnly() <em>Shipping Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShippingOnly()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHIPPING_ONLY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShippingOnly() <em>Shipping Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShippingOnly()
	 * @generated
	 * @ordered
	 */
	protected boolean shippingOnly = SHIPPING_ONLY_EDEFAULT;

	/**
	 * The default value of the '{@link #isGenerateCharterOuts() <em>Generate Charter Outs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGenerateCharterOuts()
	 * @generated
	 * @ordered
	 */
	protected static final boolean GENERATE_CHARTER_OUTS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isGenerateCharterOuts() <em>Generate Charter Outs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGenerateCharterOuts()
	 * @generated
	 * @ordered
	 */
	protected boolean generateCharterOuts = GENERATE_CHARTER_OUTS_EDEFAULT;

	/**
	 * The default value of the '{@link #isWithSpotCargoMarkets() <em>With Spot Cargo Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWithSpotCargoMarkets()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WITH_SPOT_CARGO_MARKETS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWithSpotCargoMarkets() <em>With Spot Cargo Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWithSpotCargoMarkets()
	 * @generated
	 * @ordered
	 */
	protected boolean withSpotCargoMarkets = WITH_SPOT_CARGO_MARKETS_EDEFAULT;

	/**
	 * The default value of the '{@link #isBuildActionSets() <em>Build Action Sets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBuildActionSets()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BUILD_ACTION_SETS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBuildActionSets() <em>Build Action Sets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBuildActionSets()
	 * @generated
	 * @ordered
	 */
	protected boolean buildActionSets = BUILD_ACTION_SETS_EDEFAULT;

	/**
	 * The default value of the '{@link #getSimilarityMode() <em>Similarity Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimilarityMode()
	 * @generated
	 * @ordered
	 */
	protected static final SimilarityMode SIMILARITY_MODE_EDEFAULT = SimilarityMode.OFF;

	/**
	 * The cached value of the '{@link #getSimilarityMode() <em>Similarity Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimilarityMode()
	 * @generated
	 * @ordered
	 */
	protected SimilarityMode similarityMode = SIMILARITY_MODE_EDEFAULT;

	/**
	 * The default value of the '{@link #isCleanStateOptimisation() <em>Clean State Optimisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCleanStateOptimisation()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CLEAN_STATE_OPTIMISATION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCleanStateOptimisation() <em>Clean State Optimisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCleanStateOptimisation()
	 * @generated
	 * @ordered
	 */
	protected boolean cleanStateOptimisation = CLEAN_STATE_OPTIMISATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getFloatingDaysLimit() <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloatingDaysLimit()
	 * @generated
	 * @ordered
	 */
	protected static final int FLOATING_DAYS_LIMIT_EDEFAULT = 15;

	/**
	 * The cached value of the '{@link #getFloatingDaysLimit() <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloatingDaysLimit()
	 * @generated
	 * @ordered
	 */
	protected int floatingDaysLimit = FLOATING_DAYS_LIMIT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UserSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.USER_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getPeriodStartDate() {
		return periodStartDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPeriodStartDate(LocalDate newPeriodStartDate) {
		LocalDate oldPeriodStartDate = periodStartDate;
		periodStartDate = newPeriodStartDate;
		boolean oldPeriodStartDateESet = periodStartDateESet;
		periodStartDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__PERIOD_START_DATE, oldPeriodStartDate, periodStartDate, !oldPeriodStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPeriodStartDate() {
		LocalDate oldPeriodStartDate = periodStartDate;
		boolean oldPeriodStartDateESet = periodStartDateESet;
		periodStartDate = PERIOD_START_DATE_EDEFAULT;
		periodStartDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ParametersPackage.USER_SETTINGS__PERIOD_START_DATE, oldPeriodStartDate, PERIOD_START_DATE_EDEFAULT, oldPeriodStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPeriodStartDate() {
		return periodStartDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth getPeriodEnd() {
		return periodEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPeriodEnd(YearMonth newPeriodEnd) {
		YearMonth oldPeriodEnd = periodEnd;
		periodEnd = newPeriodEnd;
		boolean oldPeriodEndESet = periodEndESet;
		periodEndESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__PERIOD_END, oldPeriodEnd, periodEnd, !oldPeriodEndESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPeriodEnd() {
		YearMonth oldPeriodEnd = periodEnd;
		boolean oldPeriodEndESet = periodEndESet;
		periodEnd = PERIOD_END_EDEFAULT;
		periodEndESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ParametersPackage.USER_SETTINGS__PERIOD_END, oldPeriodEnd, PERIOD_END_EDEFAULT, oldPeriodEndESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPeriodEnd() {
		return periodEndESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isShippingOnly() {
		return shippingOnly;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShippingOnly(boolean newShippingOnly) {
		boolean oldShippingOnly = shippingOnly;
		shippingOnly = newShippingOnly;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__SHIPPING_ONLY, oldShippingOnly, shippingOnly));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isGenerateCharterOuts() {
		return generateCharterOuts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGenerateCharterOuts(boolean newGenerateCharterOuts) {
		boolean oldGenerateCharterOuts = generateCharterOuts;
		generateCharterOuts = newGenerateCharterOuts;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS, oldGenerateCharterOuts, generateCharterOuts));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isWithSpotCargoMarkets() {
		return withSpotCargoMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWithSpotCargoMarkets(boolean newWithSpotCargoMarkets) {
		boolean oldWithSpotCargoMarkets = withSpotCargoMarkets;
		withSpotCargoMarkets = newWithSpotCargoMarkets;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS, oldWithSpotCargoMarkets, withSpotCargoMarkets));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isBuildActionSets() {
		return buildActionSets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBuildActionSets(boolean newBuildActionSets) {
		boolean oldBuildActionSets = buildActionSets;
		buildActionSets = newBuildActionSets;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__BUILD_ACTION_SETS, oldBuildActionSets, buildActionSets));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimilarityMode getSimilarityMode() {
		return similarityMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSimilarityMode(SimilarityMode newSimilarityMode) {
		SimilarityMode oldSimilarityMode = similarityMode;
		similarityMode = newSimilarityMode == null ? SIMILARITY_MODE_EDEFAULT : newSimilarityMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__SIMILARITY_MODE, oldSimilarityMode, similarityMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isCleanStateOptimisation() {
		return cleanStateOptimisation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCleanStateOptimisation(boolean newCleanStateOptimisation) {
		boolean oldCleanStateOptimisation = cleanStateOptimisation;
		cleanStateOptimisation = newCleanStateOptimisation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__CLEAN_STATE_OPTIMISATION, oldCleanStateOptimisation, cleanStateOptimisation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getFloatingDaysLimit() {
		return floatingDaysLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFloatingDaysLimit(int newFloatingDaysLimit) {
		int oldFloatingDaysLimit = floatingDaysLimit;
		floatingDaysLimit = newFloatingDaysLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT, oldFloatingDaysLimit, floatingDaysLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
				return getPeriodStartDate();
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
				return getPeriodEnd();
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
				return isShippingOnly();
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
				return isGenerateCharterOuts();
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
				return isWithSpotCargoMarkets();
			case ParametersPackage.USER_SETTINGS__BUILD_ACTION_SETS:
				return isBuildActionSets();
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
				return getSimilarityMode();
			case ParametersPackage.USER_SETTINGS__CLEAN_STATE_OPTIMISATION:
				return isCleanStateOptimisation();
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
				return getFloatingDaysLimit();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
				setPeriodStartDate((LocalDate)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
				setPeriodEnd((YearMonth)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
				setShippingOnly((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
				setGenerateCharterOuts((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
				setWithSpotCargoMarkets((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__BUILD_ACTION_SETS:
				setBuildActionSets((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
				setSimilarityMode((SimilarityMode)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__CLEAN_STATE_OPTIMISATION:
				setCleanStateOptimisation((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
				setFloatingDaysLimit((Integer)newValue);
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
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
				unsetPeriodStartDate();
				return;
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
				unsetPeriodEnd();
				return;
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
				setShippingOnly(SHIPPING_ONLY_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
				setGenerateCharterOuts(GENERATE_CHARTER_OUTS_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
				setWithSpotCargoMarkets(WITH_SPOT_CARGO_MARKETS_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__BUILD_ACTION_SETS:
				setBuildActionSets(BUILD_ACTION_SETS_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
				setSimilarityMode(SIMILARITY_MODE_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__CLEAN_STATE_OPTIMISATION:
				setCleanStateOptimisation(CLEAN_STATE_OPTIMISATION_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
				setFloatingDaysLimit(FLOATING_DAYS_LIMIT_EDEFAULT);
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
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
				return isSetPeriodStartDate();
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
				return isSetPeriodEnd();
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
				return shippingOnly != SHIPPING_ONLY_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
				return generateCharterOuts != GENERATE_CHARTER_OUTS_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
				return withSpotCargoMarkets != WITH_SPOT_CARGO_MARKETS_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__BUILD_ACTION_SETS:
				return buildActionSets != BUILD_ACTION_SETS_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
				return similarityMode != SIMILARITY_MODE_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__CLEAN_STATE_OPTIMISATION:
				return cleanStateOptimisation != CLEAN_STATE_OPTIMISATION_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
				return floatingDaysLimit != FLOATING_DAYS_LIMIT_EDEFAULT;
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
		result.append(" (periodStartDate: ");
		if (periodStartDateESet) result.append(periodStartDate); else result.append("<unset>");
		result.append(", periodEnd: ");
		if (periodEndESet) result.append(periodEnd); else result.append("<unset>");
		result.append(", shippingOnly: ");
		result.append(shippingOnly);
		result.append(", generateCharterOuts: ");
		result.append(generateCharterOuts);
		result.append(", withSpotCargoMarkets: ");
		result.append(withSpotCargoMarkets);
		result.append(", buildActionSets: ");
		result.append(buildActionSets);
		result.append(", similarityMode: ");
		result.append(similarityMode);
		result.append(", cleanStateOptimisation: ");
		result.append(cleanStateOptimisation);
		result.append(", floatingDaysLimit: ");
		result.append(floatingDaysLimit);
		result.append(')');
		return result.toString();
	}

} //UserSettingsImpl
