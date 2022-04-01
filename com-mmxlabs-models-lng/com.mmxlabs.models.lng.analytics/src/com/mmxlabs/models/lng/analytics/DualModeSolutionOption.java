/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dual Mode Solution Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.DualModeSolutionOption#getMicroBaseCase <em>Micro Base Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.DualModeSolutionOption#getMicroTargetCase <em>Micro Target Case</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getDualModeSolutionOption()
 * @model
 * @generated
 */
public interface DualModeSolutionOption extends SolutionOption {
	/**
	 * Returns the value of the '<em><b>Micro Base Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Micro Base Case</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Micro Base Case</em>' containment reference.
	 * @see #setMicroBaseCase(SolutionOptionMicroCase)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getDualModeSolutionOption_MicroBaseCase()
	 * @model containment="true"
	 * @generated
	 */
	SolutionOptionMicroCase getMicroBaseCase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.DualModeSolutionOption#getMicroBaseCase <em>Micro Base Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Micro Base Case</em>' containment reference.
	 * @see #getMicroBaseCase()
	 * @generated
	 */
	void setMicroBaseCase(SolutionOptionMicroCase value);

	/**
	 * Returns the value of the '<em><b>Micro Target Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Micro Target Case</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Micro Target Case</em>' containment reference.
	 * @see #setMicroTargetCase(SolutionOptionMicroCase)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getDualModeSolutionOption_MicroTargetCase()
	 * @model containment="true"
	 * @generated
	 */
	SolutionOptionMicroCase getMicroTargetCase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.DualModeSolutionOption#getMicroTargetCase <em>Micro Target Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Micro Target Case</em>' containment reference.
	 * @see #getMicroTargetCase()
	 * @generated
	 */
	void setMicroTargetCase(SolutionOptionMicroCase value);

} // DualModeSolutionOption
