/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.OptimisationMode;
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
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isNominalOnly <em>Nominal Only</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getPeriodStartDate <em>Period Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getPeriodEnd <em>Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isDualMode <em>Dual Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getSimilarityMode <em>Similarity Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isShippingOnly <em>Shipping Only</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isGenerateCharterOuts <em>Generate Charter Outs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isWithCharterLength <em>With Charter Length</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getCharterLengthDays <em>Charter Length Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isWithSpotCargoMarkets <em>With Spot Cargo Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#getFloatingDaysLimit <em>Floating Days Limit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isCleanSlateOptimisation <em>Clean Slate Optimisation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl#isGeneratedPapersInPNL <em>Generated Papers In PNL</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UserSettingsImpl extends EObjectImpl implements UserSettings {
	/**
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected static final OptimisationMode MODE_EDEFAULT = OptimisationMode.SHORT_TERM;

	/**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected OptimisationMode mode = MODE_EDEFAULT;

	/**
	 * The default value of the '{@link #isNominalOnly() <em>Nominal Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNominalOnly()
	 * @generated
	 * @ordered
	 */
	protected static final boolean NOMINAL_ONLY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isNominalOnly() <em>Nominal Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNominalOnly()
	 * @generated
	 * @ordered
	 */
	protected boolean nominalOnly = NOMINAL_ONLY_EDEFAULT;

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
	 * The default value of the '{@link #isDualMode() <em>Dual Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDualMode()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DUAL_MODE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDualMode() <em>Dual Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDualMode()
	 * @generated
	 * @ordered
	 */
	protected boolean dualMode = DUAL_MODE_EDEFAULT;

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
	 * The default value of the '{@link #isWithCharterLength() <em>With Charter Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWithCharterLength()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WITH_CHARTER_LENGTH_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWithCharterLength() <em>With Charter Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWithCharterLength()
	 * @generated
	 * @ordered
	 */
	protected boolean withCharterLength = WITH_CHARTER_LENGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getCharterLengthDays() <em>Charter Length Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterLengthDays()
	 * @generated
	 * @ordered
	 */
	protected static final int CHARTER_LENGTH_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCharterLengthDays() <em>Charter Length Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterLengthDays()
	 * @generated
	 * @ordered
	 */
	protected int charterLengthDays = CHARTER_LENGTH_DAYS_EDEFAULT;

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
	 * The default value of the '{@link #isCleanSlateOptimisation() <em>Clean Slate Optimisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCleanSlateOptimisation()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CLEAN_SLATE_OPTIMISATION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCleanSlateOptimisation() <em>Clean Slate Optimisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCleanSlateOptimisation()
	 * @generated
	 * @ordered
	 */
	protected boolean cleanSlateOptimisation = CLEAN_SLATE_OPTIMISATION_EDEFAULT;

	/**
	 * The default value of the '{@link #isGeneratedPapersInPNL() <em>Generated Papers In PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGeneratedPapersInPNL()
	 * @generated
	 * @ordered
	 */
	protected static final boolean GENERATED_PAPERS_IN_PNL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isGeneratedPapersInPNL() <em>Generated Papers In PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGeneratedPapersInPNL()
	 * @generated
	 * @ordered
	 */
	protected boolean generatedPapersInPNL = GENERATED_PAPERS_IN_PNL_EDEFAULT;

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
	@Override
	public OptimisationMode getMode() {
		return mode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMode(OptimisationMode newMode) {
		OptimisationMode oldMode = mode;
		mode = newMode == null ? MODE_EDEFAULT : newMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__MODE, oldMode, mode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isNominalOnly() {
		return nominalOnly;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNominalOnly(boolean newNominalOnly) {
		boolean oldNominalOnly = nominalOnly;
		nominalOnly = newNominalOnly;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__NOMINAL_ONLY, oldNominalOnly, nominalOnly));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPeriodStartDate() {
		return periodStartDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
	public boolean isWithCharterLength() {
		return withCharterLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWithCharterLength(boolean newWithCharterLength) {
		boolean oldWithCharterLength = withCharterLength;
		withCharterLength = newWithCharterLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__WITH_CHARTER_LENGTH, oldWithCharterLength, withCharterLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCharterLengthDays() {
		return charterLengthDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterLengthDays(int newCharterLengthDays) {
		int oldCharterLengthDays = charterLengthDays;
		charterLengthDays = newCharterLengthDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__CHARTER_LENGTH_DAYS, oldCharterLengthDays, charterLengthDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isWithSpotCargoMarkets() {
		return withSpotCargoMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public int getFloatingDaysLimit() {
		return floatingDaysLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public boolean isCleanSlateOptimisation() {
		return cleanSlateOptimisation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCleanSlateOptimisation(boolean newCleanSlateOptimisation) {
		boolean oldCleanSlateOptimisation = cleanSlateOptimisation;
		cleanSlateOptimisation = newCleanSlateOptimisation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__CLEAN_SLATE_OPTIMISATION, oldCleanSlateOptimisation, cleanSlateOptimisation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isGeneratedPapersInPNL() {
		return generatedPapersInPNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGeneratedPapersInPNL(boolean newGeneratedPapersInPNL) {
		boolean oldGeneratedPapersInPNL = generatedPapersInPNL;
		generatedPapersInPNL = newGeneratedPapersInPNL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__GENERATED_PAPERS_IN_PNL, oldGeneratedPapersInPNL, generatedPapersInPNL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isDualMode() {
		return dualMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDualMode(boolean newDualMode) {
		boolean oldDualMode = dualMode;
		dualMode = newDualMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.USER_SETTINGS__DUAL_MODE, oldDualMode, dualMode));
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParametersPackage.USER_SETTINGS__MODE:
				return getMode();
			case ParametersPackage.USER_SETTINGS__NOMINAL_ONLY:
				return isNominalOnly();
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
				return getPeriodStartDate();
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
				return getPeriodEnd();
			case ParametersPackage.USER_SETTINGS__DUAL_MODE:
				return isDualMode();
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
				return getSimilarityMode();
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
				return isShippingOnly();
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
				return isGenerateCharterOuts();
			case ParametersPackage.USER_SETTINGS__WITH_CHARTER_LENGTH:
				return isWithCharterLength();
			case ParametersPackage.USER_SETTINGS__CHARTER_LENGTH_DAYS:
				return getCharterLengthDays();
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
				return isWithSpotCargoMarkets();
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
				return getFloatingDaysLimit();
			case ParametersPackage.USER_SETTINGS__CLEAN_SLATE_OPTIMISATION:
				return isCleanSlateOptimisation();
			case ParametersPackage.USER_SETTINGS__GENERATED_PAPERS_IN_PNL:
				return isGeneratedPapersInPNL();
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
			case ParametersPackage.USER_SETTINGS__MODE:
				setMode((OptimisationMode)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__NOMINAL_ONLY:
				setNominalOnly((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
				setPeriodStartDate((LocalDate)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
				setPeriodEnd((YearMonth)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__DUAL_MODE:
				setDualMode((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
				setSimilarityMode((SimilarityMode)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
				setShippingOnly((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
				setGenerateCharterOuts((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__WITH_CHARTER_LENGTH:
				setWithCharterLength((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__CHARTER_LENGTH_DAYS:
				setCharterLengthDays((Integer)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
				setWithSpotCargoMarkets((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
				setFloatingDaysLimit((Integer)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__CLEAN_SLATE_OPTIMISATION:
				setCleanSlateOptimisation((Boolean)newValue);
				return;
			case ParametersPackage.USER_SETTINGS__GENERATED_PAPERS_IN_PNL:
				setGeneratedPapersInPNL((Boolean)newValue);
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
			case ParametersPackage.USER_SETTINGS__MODE:
				setMode(MODE_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__NOMINAL_ONLY:
				setNominalOnly(NOMINAL_ONLY_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
				unsetPeriodStartDate();
				return;
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
				unsetPeriodEnd();
				return;
			case ParametersPackage.USER_SETTINGS__DUAL_MODE:
				setDualMode(DUAL_MODE_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
				setSimilarityMode(SIMILARITY_MODE_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
				setShippingOnly(SHIPPING_ONLY_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
				setGenerateCharterOuts(GENERATE_CHARTER_OUTS_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__WITH_CHARTER_LENGTH:
				setWithCharterLength(WITH_CHARTER_LENGTH_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__CHARTER_LENGTH_DAYS:
				setCharterLengthDays(CHARTER_LENGTH_DAYS_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
				setWithSpotCargoMarkets(WITH_SPOT_CARGO_MARKETS_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
				setFloatingDaysLimit(FLOATING_DAYS_LIMIT_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__CLEAN_SLATE_OPTIMISATION:
				setCleanSlateOptimisation(CLEAN_SLATE_OPTIMISATION_EDEFAULT);
				return;
			case ParametersPackage.USER_SETTINGS__GENERATED_PAPERS_IN_PNL:
				setGeneratedPapersInPNL(GENERATED_PAPERS_IN_PNL_EDEFAULT);
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
			case ParametersPackage.USER_SETTINGS__MODE:
				return mode != MODE_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__NOMINAL_ONLY:
				return nominalOnly != NOMINAL_ONLY_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
				return isSetPeriodStartDate();
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
				return isSetPeriodEnd();
			case ParametersPackage.USER_SETTINGS__DUAL_MODE:
				return dualMode != DUAL_MODE_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
				return similarityMode != SIMILARITY_MODE_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
				return shippingOnly != SHIPPING_ONLY_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
				return generateCharterOuts != GENERATE_CHARTER_OUTS_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__WITH_CHARTER_LENGTH:
				return withCharterLength != WITH_CHARTER_LENGTH_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__CHARTER_LENGTH_DAYS:
				return charterLengthDays != CHARTER_LENGTH_DAYS_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
				return withSpotCargoMarkets != WITH_SPOT_CARGO_MARKETS_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
				return floatingDaysLimit != FLOATING_DAYS_LIMIT_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__CLEAN_SLATE_OPTIMISATION:
				return cleanSlateOptimisation != CLEAN_SLATE_OPTIMISATION_EDEFAULT;
			case ParametersPackage.USER_SETTINGS__GENERATED_PAPERS_IN_PNL:
				return generatedPapersInPNL != GENERATED_PAPERS_IN_PNL_EDEFAULT;
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
		result.append(" (mode: ");
		result.append(mode);
		result.append(", nominalOnly: ");
		result.append(nominalOnly);
		result.append(", periodStartDate: ");
		if (periodStartDateESet) result.append(periodStartDate); else result.append("<unset>");
		result.append(", periodEnd: ");
		if (periodEndESet) result.append(periodEnd); else result.append("<unset>");
		result.append(", dualMode: ");
		result.append(dualMode);
		result.append(", similarityMode: ");
		result.append(similarityMode);
		result.append(", shippingOnly: ");
		result.append(shippingOnly);
		result.append(", generateCharterOuts: ");
		result.append(generateCharterOuts);
		result.append(", withCharterLength: ");
		result.append(withCharterLength);
		result.append(", charterLengthDays: ");
		result.append(charterLengthDays);
		result.append(", withSpotCargoMarkets: ");
		result.append(withSpotCargoMarkets);
		result.append(", floatingDaysLimit: ");
		result.append(floatingDaysLimit);
		result.append(", cleanSlateOptimisation: ");
		result.append(cleanSlateOptimisation);
		result.append(", generatedPapersInPNL: ");
		result.append(generatedPapersInPNL);
		result.append(')');
		return result.toString();
	}

} //UserSettingsImpl
