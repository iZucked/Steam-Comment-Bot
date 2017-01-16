/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group Profit And Loss</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLoss <em>Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLossPreTax <em>Profit And Loss Pre Tax</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getTaxValue <em>Tax Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getEntityProfitAndLosses <em>Entity Profit And Losses</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGroupProfitAndLoss()
 * @model
 * @generated
 */
public interface GroupProfitAndLoss extends EObject {
	/**
	 * Returns the value of the '<em><b>Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profit And Loss</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profit And Loss</em>' attribute.
	 * @see #setProfitAndLoss(long)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGroupProfitAndLoss_ProfitAndLoss()
	 * @model
	 * @generated
	 */
	long getProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLoss <em>Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profit And Loss</em>' attribute.
	 * @see #getProfitAndLoss()
	 * @generated
	 */
	void setProfitAndLoss(long value);

	/**
	 * Returns the value of the '<em><b>Profit And Loss Pre Tax</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profit And Loss Pre Tax</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profit And Loss Pre Tax</em>' attribute.
	 * @see #setProfitAndLossPreTax(long)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGroupProfitAndLoss_ProfitAndLossPreTax()
	 * @model
	 * @generated
	 */
	long getProfitAndLossPreTax();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLossPreTax <em>Profit And Loss Pre Tax</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profit And Loss Pre Tax</em>' attribute.
	 * @see #getProfitAndLossPreTax()
	 * @generated
	 */
	void setProfitAndLossPreTax(long value);

	/**
	 * Returns the value of the '<em><b>Tax Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tax Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tax Value</em>' attribute.
	 * @see #setTaxValue(long)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGroupProfitAndLoss_TaxValue()
	 * @model
	 * @generated
	 */
	long getTaxValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getTaxValue <em>Tax Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tax Value</em>' attribute.
	 * @see #getTaxValue()
	 * @generated
	 */
	void setTaxValue(long value);

	/**
	 * Returns the value of the '<em><b>Entity Profit And Losses</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity Profit And Losses</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity Profit And Losses</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGroupProfitAndLoss_EntityProfitAndLosses()
	 * @model containment="true"
	 * @generated
	 */
	EList<EntityProfitAndLoss> getEntityProfitAndLosses();

} // GroupProfitAndLoss
