/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Solution Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isHasDualModeSolutions <em>Has Dual Mode Solutions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isPortfolioBreakEvenMode <em>Portfolio Break Even Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getUserSettings <em>User Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraSlots <em>Extra Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getBaseOption <em>Base Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getOptions <em>Options</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSolutionSet()
 * @model abstract="true"
 *        annotation="http://www.mmxlabs.com/models/mmxcore/validation/NamedObject nonUniqueChildren='true'"
 * @generated
 */
public interface AbstractSolutionSet extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Has Dual Mode Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Has Dual Mode Solutions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Dual Mode Solutions</em>' attribute.
	 * @see #setHasDualModeSolutions(boolean)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSolutionSet_HasDualModeSolutions()
	 * @model
	 * @generated
	 */
	boolean isHasDualModeSolutions();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isHasDualModeSolutions <em>Has Dual Mode Solutions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Has Dual Mode Solutions</em>' attribute.
	 * @see #isHasDualModeSolutions()
	 * @generated
	 */
	void setHasDualModeSolutions(boolean value);

	/**
	 * Returns the value of the '<em><b>Portfolio Break Even Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Portfolio Break Even Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Portfolio Break Even Mode</em>' attribute.
	 * @see #setPortfolioBreakEvenMode(boolean)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSolutionSet_PortfolioBreakEvenMode()
	 * @model
	 * @generated
	 */
	boolean isPortfolioBreakEvenMode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isPortfolioBreakEvenMode <em>Portfolio Break Even Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Portfolio Break Even Mode</em>' attribute.
	 * @see #isPortfolioBreakEvenMode()
	 * @generated
	 */
	void setPortfolioBreakEvenMode(boolean value);

	/**
	 * Returns the value of the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Settings</em>' containment reference.
	 * @see #setUserSettings(UserSettings)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSolutionSet_UserSettings()
	 * @model containment="true"
	 * @generated
	 */
	UserSettings getUserSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getUserSettings <em>User Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User Settings</em>' containment reference.
	 * @see #getUserSettings()
	 * @generated
	 */
	void setUserSettings(UserSettings value);

	/**
	 * Returns the value of the '<em><b>Options</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.SolutionOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Options</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Options</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSolutionSet_Options()
	 * @model containment="true"
	 * @generated
	 */
	EList<SolutionOption> getOptions();

	/**
	 * Returns the value of the '<em><b>Extra Slots</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Slot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extra Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extra Slots</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSolutionSet_ExtraSlots()
	 * @model containment="true"
	 *        annotation="http://www.mmxlabs.com/models/mmxcore/validation/NamedObject nonUniqueChildren='true'"
	 * @generated
	 */
	EList<Slot> getExtraSlots();

	/**
	 * Returns the value of the '<em><b>Base Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Option</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Option</em>' containment reference.
	 * @see #setBaseOption(SolutionOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractSolutionSet_BaseOption()
	 * @model containment="true"
	 * @generated
	 */
	SolutionOption getBaseOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getBaseOption <em>Base Option</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Option</em>' containment reference.
	 * @see #getBaseOption()
	 * @generated
	 */
	void setBaseOption(SolutionOption value);

} // AbstractSolutionSet
