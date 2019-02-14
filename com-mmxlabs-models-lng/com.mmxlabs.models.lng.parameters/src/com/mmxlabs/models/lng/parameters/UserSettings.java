/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;

import java.time.LocalDate;
import java.time.YearMonth;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStartDate <em>Period Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodEnd <em>Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isShippingOnly <em>Shipping Only</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isGenerateCharterOuts <em>Generate Charter Outs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isWithCharterLength <em>With Charter Length</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getCharterLengthDays <em>Charter Length Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isWithSpotCargoMarkets <em>With Spot Cargo Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isBuildActionSets <em>Build Action Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getSimilarityMode <em>Similarity Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isNominalADP <em>Nominal ADP</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isCleanStateOptimisation <em>Clean State Optimisation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isAdpOptimisation <em>Adp Optimisation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isDualMode <em>Dual Mode</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings()
 * @model
 * @generated
 */
public interface UserSettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Period Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Period Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period Start Date</em>' attribute.
	 * @see #isSetPeriodStartDate()
	 * @see #unsetPeriodStartDate()
	 * @see #setPeriodStartDate(LocalDate)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_PeriodStartDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPeriodStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStartDate <em>Period Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period Start Date</em>' attribute.
	 * @see #isSetPeriodStartDate()
	 * @see #unsetPeriodStartDate()
	 * @see #getPeriodStartDate()
	 * @generated
	 */
	void setPeriodStartDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStartDate <em>Period Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPeriodStartDate()
	 * @see #getPeriodStartDate()
	 * @see #setPeriodStartDate(LocalDate)
	 * @generated
	 */
	void unsetPeriodStartDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStartDate <em>Period Start Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Period Start Date</em>' attribute is set.
	 * @see #unsetPeriodStartDate()
	 * @see #getPeriodStartDate()
	 * @see #setPeriodStartDate(LocalDate)
	 * @generated
	 */
	boolean isSetPeriodStartDate();

	/**
	 * Returns the value of the '<em><b>Period End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Period End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period End</em>' attribute.
	 * @see #isSetPeriodEnd()
	 * @see #unsetPeriodEnd()
	 * @see #setPeriodEnd(YearMonth)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_PeriodEnd()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getPeriodEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodEnd <em>Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period End</em>' attribute.
	 * @see #isSetPeriodEnd()
	 * @see #unsetPeriodEnd()
	 * @see #getPeriodEnd()
	 * @generated
	 */
	void setPeriodEnd(YearMonth value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodEnd <em>Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPeriodEnd()
	 * @see #getPeriodEnd()
	 * @see #setPeriodEnd(YearMonth)
	 * @generated
	 */
	void unsetPeriodEnd();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodEnd <em>Period End</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Period End</em>' attribute is set.
	 * @see #unsetPeriodEnd()
	 * @see #getPeriodEnd()
	 * @see #setPeriodEnd(YearMonth)
	 * @generated
	 */
	boolean isSetPeriodEnd();

	/**
	 * Returns the value of the '<em><b>Shipping Only</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Only</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Only</em>' attribute.
	 * @see #setShippingOnly(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_ShippingOnly()
	 * @model default="false"
	 * @generated
	 */
	boolean isShippingOnly();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isShippingOnly <em>Shipping Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Only</em>' attribute.
	 * @see #isShippingOnly()
	 * @generated
	 */
	void setShippingOnly(boolean value);

	/**
	 * Returns the value of the '<em><b>Generate Charter Outs</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generate Charter Outs</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generate Charter Outs</em>' attribute.
	 * @see #setGenerateCharterOuts(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_GenerateCharterOuts()
	 * @model default="false"
	 * @generated
	 */
	boolean isGenerateCharterOuts();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isGenerateCharterOuts <em>Generate Charter Outs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Charter Outs</em>' attribute.
	 * @see #isGenerateCharterOuts()
	 * @generated
	 */
	void setGenerateCharterOuts(boolean value);

	/**
	 * Returns the value of the '<em><b>With Charter Length</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>With Charter Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>With Charter Length</em>' attribute.
	 * @see #setWithCharterLength(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_WithCharterLength()
	 * @model default="false"
	 * @generated
	 */
	boolean isWithCharterLength();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isWithCharterLength <em>With Charter Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>With Charter Length</em>' attribute.
	 * @see #isWithCharterLength()
	 * @generated
	 */
	void setWithCharterLength(boolean value);

	/**
	 * Returns the value of the '<em><b>Charter Length Days</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Length Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Length Days</em>' attribute.
	 * @see #setCharterLengthDays(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_CharterLengthDays()
	 * @model default="0"
	 * @generated
	 */
	int getCharterLengthDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getCharterLengthDays <em>Charter Length Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Length Days</em>' attribute.
	 * @see #getCharterLengthDays()
	 * @generated
	 */
	void setCharterLengthDays(int value);

	/**
	 * Returns the value of the '<em><b>With Spot Cargo Markets</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>With Spot Cargo Markets</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>With Spot Cargo Markets</em>' attribute.
	 * @see #setWithSpotCargoMarkets(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_WithSpotCargoMarkets()
	 * @model default="false"
	 * @generated
	 */
	boolean isWithSpotCargoMarkets();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isWithSpotCargoMarkets <em>With Spot Cargo Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>With Spot Cargo Markets</em>' attribute.
	 * @see #isWithSpotCargoMarkets()
	 * @generated
	 */
	void setWithSpotCargoMarkets(boolean value);

	/**
	 * Returns the value of the '<em><b>Build Action Sets</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Build Action Sets</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Build Action Sets</em>' attribute.
	 * @see #setBuildActionSets(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_BuildActionSets()
	 * @model default="false"
	 *        annotation="http://www.minimaxlabs.com/license/features/required module='actionplan'"
	 * @generated
	 */
	boolean isBuildActionSets();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isBuildActionSets <em>Build Action Sets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Build Action Sets</em>' attribute.
	 * @see #isBuildActionSets()
	 * @generated
	 */
	void setBuildActionSets(boolean value);

	/**
	 * Returns the value of the '<em><b>Similarity Mode</b></em>' attribute.
	 * The default value is <code>"OFF"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.parameters.SimilarityMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Similarity Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Similarity Mode</em>' attribute.
	 * @see com.mmxlabs.models.lng.parameters.SimilarityMode
	 * @see #setSimilarityMode(SimilarityMode)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_SimilarityMode()
	 * @model default="OFF"
	 * @generated
	 */
	SimilarityMode getSimilarityMode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getSimilarityMode <em>Similarity Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Similarity Mode</em>' attribute.
	 * @see com.mmxlabs.models.lng.parameters.SimilarityMode
	 * @see #getSimilarityMode()
	 * @generated
	 */
	void setSimilarityMode(SimilarityMode value);

	/**
	 * Returns the value of the '<em><b>Nominal ADP</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nominal ADP</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nominal ADP</em>' attribute.
	 * @see #setNominalADP(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_NominalADP()
	 * @model default="false"
	 * @generated
	 */
	boolean isNominalADP();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isNominalADP <em>Nominal ADP</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nominal ADP</em>' attribute.
	 * @see #isNominalADP()
	 * @generated
	 */
	void setNominalADP(boolean value);

	/**
	 * Returns the value of the '<em><b>Clean State Optimisation</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Clean State Optimisation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Clean State Optimisation</em>' attribute.
	 * @see #setCleanStateOptimisation(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_CleanStateOptimisation()
	 * @model default="false"
	 * @generated
	 */
	boolean isCleanStateOptimisation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isCleanStateOptimisation <em>Clean State Optimisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Clean State Optimisation</em>' attribute.
	 * @see #isCleanStateOptimisation()
	 * @generated
	 */
	void setCleanStateOptimisation(boolean value);

	/**
	 * Returns the value of the '<em><b>Floating Days Limit</b></em>' attribute.
	 * The default value is <code>"15"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Floating Days Limit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Floating Days Limit</em>' attribute.
	 * @see #setFloatingDaysLimit(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_FloatingDaysLimit()
	 * @model default="15"
	 * @generated
	 */
	int getFloatingDaysLimit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Floating Days Limit</em>' attribute.
	 * @see #getFloatingDaysLimit()
	 * @generated
	 */
	void setFloatingDaysLimit(int value);

	/**
	 * Returns the value of the '<em><b>Adp Optimisation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adp Optimisation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Adp Optimisation</em>' attribute.
	 * @see #setAdpOptimisation(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_AdpOptimisation()
	 * @model
	 * @generated
	 */
	boolean isAdpOptimisation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isAdpOptimisation <em>Adp Optimisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Adp Optimisation</em>' attribute.
	 * @see #isAdpOptimisation()
	 * @generated
	 */
	void setAdpOptimisation(boolean value);

	/**
	 * Returns the value of the '<em><b>Dual Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dual Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dual Mode</em>' attribute.
	 * @see #setDualMode(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_DualMode()
	 * @model
	 * @generated
	 */
	boolean isDualMode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#isDualMode <em>Dual Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dual Mode</em>' attribute.
	 * @see #isDualMode()
	 * @generated
	 */
	void setDualMode(boolean value);

} // UserSettings
