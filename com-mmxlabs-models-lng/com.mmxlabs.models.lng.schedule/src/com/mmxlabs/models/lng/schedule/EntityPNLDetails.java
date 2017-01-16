/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity PNL Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EntityPNLDetails#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EntityPNLDetails#getGeneralPNLDetails <em>General PNL Details</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityPNLDetails()
 * @model
 * @generated
 */
public interface EntityPNLDetails extends GeneralPNLDetails {

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
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityPNLDetails_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.EntityPNLDetails#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>General PNL Details</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.GeneralPNLDetails}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>General PNL Details</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>General PNL Details</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityPNLDetails_GeneralPNLDetails()
	 * @model containment="true"
	 * @generated
	 */
	EList<GeneralPNLDetails> getGeneralPNLDetails();
} // EntityPNLDetails
