/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Analysis Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getBuys <em>Buys</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getSells <em>Sells</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getShippingTemplates <em>Shipping Templates</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractAnalysisModel()
 * @model abstract="true"
 * @generated
 */
public interface AbstractAnalysisModel extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Buys</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.BuyOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Buys</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buys</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractAnalysisModel_Buys()
	 * @model containment="true"
	 * @generated
	 */
	EList<BuyOption> getBuys();

	/**
	 * Returns the value of the '<em><b>Sells</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.SellOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sells</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sells</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractAnalysisModel_Sells()
	 * @model containment="true"
	 * @generated
	 */
	EList<SellOption> getSells();

	/**
	 * Returns the value of the '<em><b>Vessel Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.VesselEventOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractAnalysisModel_VesselEvents()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselEventOption> getVesselEvents();

	/**
	 * Returns the value of the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ShippingOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Templates</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Templates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAbstractAnalysisModel_ShippingTemplates()
	 * @model containment="true"
	 * @generated
	 */
	EList<ShippingOption> getShippingTemplates();

} // AbstractAnalysisModel
