/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.port.RouteOption;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Partial Case Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getBuyOptions <em>Buy Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getSellOptions <em>Sell Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getVesselEventOptions <em>Vessel Event Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getOptions <em>Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getCommodityCurveOptions <em>Commodity Curve Options</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRow()
 * @model
 * @generated
 */
public interface PartialCaseRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Buy Options</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.BuyOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Buy Options</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy Options</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRow_BuyOptions()
	 * @model
	 * @generated
	 */
	EList<BuyOption> getBuyOptions();

	/**
	 * Returns the value of the '<em><b>Sell Options</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.SellOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sell Options</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell Options</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRow_SellOptions()
	 * @model
	 * @generated
	 */
	EList<SellOption> getSellOptions();

	/**
	 * Returns the value of the '<em><b>Vessel Event Options</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.VesselEventOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Event Options</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Event Options</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRow_VesselEventOptions()
	 * @model
	 * @generated
	 */
	EList<VesselEventOption> getVesselEventOptions();

	/**
	 * Returns the value of the '<em><b>Shipping</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ShippingOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRow_Shipping()
	 * @model
	 * @generated
	 */
	EList<ShippingOption> getShipping();

	/**
	 * Returns the value of the '<em><b>Options</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Options</em>' containment reference.
	 * @see #setOptions(PartialCaseRowOptions)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRow_Options()
	 * @model containment="true"
	 * @generated
	 */
	PartialCaseRowOptions getOptions();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getOptions <em>Options</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Options</em>' containment reference.
	 * @see #getOptions()
	 * @generated
	 */
	void setOptions(PartialCaseRowOptions value);

	/**
	 * Returns the value of the '<em><b>Commodity Curve Options</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.CommodityCurveOption}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commodity Curve Options</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRow_CommodityCurveOptions()
	 * @model
	 * @generated
	 */
	EList<CommodityCurveOption> getCommodityCurveOptions();

} // PartialCaseRow
