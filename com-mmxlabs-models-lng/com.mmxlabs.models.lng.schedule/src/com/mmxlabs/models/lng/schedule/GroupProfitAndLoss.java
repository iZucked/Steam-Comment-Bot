/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group Profit And Loss</b></em>'.
 * @since 4.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLoss <em>Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getEntityProfitAndLosses <em>Entity Profit And Losses</em>}</li>
 * </ul>
 * </p>
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
