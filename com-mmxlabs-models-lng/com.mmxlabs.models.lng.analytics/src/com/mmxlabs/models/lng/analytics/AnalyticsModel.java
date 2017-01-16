/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getRoundTripMatrices <em>Round Trip Matrices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getSelectedMatrix <em>Selected Matrix</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getShippingCostPlans <em>Shipping Cost Plans</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getCargoSandboxes <em>Cargo Sandboxes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getOptionModels <em>Option Models</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel()
 * @model
 * @generated
 */
public interface AnalyticsModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Round Trip Matrices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.UnitCostMatrix}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Round Trip Matrices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Round Trip Matrices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_RoundTripMatrices()
	 * @model containment="true"
	 * @generated
	 */
	EList<UnitCostMatrix> getRoundTripMatrices();

	/**
	 * Returns the value of the '<em><b>Selected Matrix</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selected Matrix</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selected Matrix</em>' reference.
	 * @see #setSelectedMatrix(UnitCostMatrix)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_SelectedMatrix()
	 * @model required="true"
	 * @generated
	 */
	UnitCostMatrix getSelectedMatrix();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getSelectedMatrix <em>Selected Matrix</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selected Matrix</em>' reference.
	 * @see #getSelectedMatrix()
	 * @generated
	 */
	void setSelectedMatrix(UnitCostMatrix value);

	/**
	 * Returns the value of the '<em><b>Shipping Cost Plans</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ShippingCostPlan}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Cost Plans</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Cost Plans</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_ShippingCostPlans()
	 * @model containment="true"
	 * @generated
	 */
	EList<ShippingCostPlan> getShippingCostPlans();

	/**
	 * Returns the value of the '<em><b>Cargo Sandboxes</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.CargoSandbox}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Sandboxes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Sandboxes</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_CargoSandboxes()
	 * @model containment="true"
	 * @generated
	 */
	EList<CargoSandbox> getCargoSandboxes();

	/**
	 * Returns the value of the '<em><b>Option Models</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Option Models</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Option Models</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_OptionModels()
	 * @model containment="true"
	 * @generated
	 */
	EList<OptionAnalysisModel> getOptionModels();

} // end of  AnalyticsModel

// finish type fixing
