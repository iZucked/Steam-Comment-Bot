/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.LegalEntity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity Profit And Loss</b></em>'.
 * @since 4.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLoss <em>Profit And Loss</em>}</li>
 * </ul>
 * </p>
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
	 * @see #setEntity(LegalEntity)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEntityProfitAndLoss_Entity()
	 * @model
	 * @generated
	 */
	LegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(LegalEntity value);

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

} // EntityProfitAndLoss
