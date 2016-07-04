/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity Profit And Loss</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntityBook <em>Entity Book</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLoss <em>Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLossPreTax <em>Profit And Loss Pre Tax</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityProfitAndLoss()
 * @model
 * @generated
 */
public interface EntityProfitAndLoss extends EObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityProfitAndLoss_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>Entity Book</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity Book</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity Book</em>' reference.
	 * @see #setEntityBook(BaseEntityBook)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityProfitAndLoss_EntityBook()
	 * @model
	 * @generated
	 */
	BaseEntityBook getEntityBook();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntityBook <em>Entity Book</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity Book</em>' reference.
	 * @see #getEntityBook()
	 * @generated
	 */
	void setEntityBook(BaseEntityBook value);

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
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityProfitAndLoss_ProfitAndLoss()
	 * @model
	 * @generated
	 */
	long getProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLoss <em>Profit And Loss</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityProfitAndLoss_ProfitAndLossPreTax()
	 * @model
	 * @generated
	 */
	long getProfitAndLossPreTax();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLossPreTax <em>Profit And Loss Pre Tax</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profit And Loss Pre Tax</em>' attribute.
	 * @see #getProfitAndLossPreTax()
	 * @generated
	 */
	void setProfitAndLossPreTax(long value);

} // EntityProfitAndLoss
