/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

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
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getShipping <em>Shipping</em>}</li>
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

} // PartialCaseRow
