/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;

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
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStart <em>Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodEnd <em>Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isShippingOnly <em>Shipping Only</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isGenerateCharterOuts <em>Generate Charter Outs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isWithSpotCargoMarkets <em>With Spot Cargo Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isBuildActionSets <em>Build Action Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getSimilarityMode <em>Similarity Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#isCleanStateOptimisation <em>Clean State Optimisation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.UserSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings()
 * @model
 * @generated
 */
public interface UserSettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Period Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Period Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period Start</em>' attribute.
	 * @see #isSetPeriodStart()
	 * @see #unsetPeriodStart()
	 * @see #setPeriodStart(YearMonth)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getUserSettings_PeriodStart()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getPeriodStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStart <em>Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period Start</em>' attribute.
	 * @see #isSetPeriodStart()
	 * @see #unsetPeriodStart()
	 * @see #getPeriodStart()
	 * @generated
	 */
	void setPeriodStart(YearMonth value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStart <em>Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPeriodStart()
	 * @see #getPeriodStart()
	 * @see #setPeriodStart(YearMonth)
	 * @generated
	 */
	void unsetPeriodStart();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStart <em>Period Start</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Period Start</em>' attribute is set.
	 * @see #unsetPeriodStart()
	 * @see #getPeriodStart()
	 * @see #setPeriodStart(YearMonth)
	 * @generated
	 */
	boolean isSetPeriodStart();

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

} // UserSettings
