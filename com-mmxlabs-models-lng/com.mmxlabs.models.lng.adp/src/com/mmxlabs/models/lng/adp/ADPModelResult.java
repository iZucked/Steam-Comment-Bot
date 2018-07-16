/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModelResult#getExtraSlots <em>Extra Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModelResult#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ADPModelResult#getExtraSpotCharterMarkets <em>Extra Spot Charter Markets</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModelResult()
 * @model annotation="http://www.mmxlabs.com/models/mmxcore/validation/NamedObject nonUniqueChildren='true'"
 * @generated
 */
public interface ADPModelResult extends EObject {
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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModelResult_ExtraSlots()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Slot> getExtraSlots();

	/**
	 * Returns the value of the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schedule Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schedule Model</em>' containment reference.
	 * @see #setScheduleModel(ScheduleModel)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModelResult_ScheduleModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ScheduleModel getScheduleModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ADPModelResult#getScheduleModel <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Model</em>' containment reference.
	 * @see #getScheduleModel()
	 * @generated
	 */
	void setScheduleModel(ScheduleModel value);

	/**
	 * Returns the value of the '<em><b>Extra Spot Charter Markets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extra Spot Charter Markets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extra Spot Charter Markets</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getADPModelResult_ExtraSpotCharterMarkets()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<CharterInMarket> getExtraSpotCharterMarkets();

} // ADPModelResult
