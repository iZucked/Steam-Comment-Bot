/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fuel Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.FuelUsage#getFuels <em>Fuels</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelUsage()
 * @model
 * @generated
 */
public interface FuelUsage extends EObject {
	/**
	 * Returns the value of the '<em><b>Fuels</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.FuelQuantity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuels</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuels</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelUsage_Fuels()
	 * @model containment="true"
	 * @generated
	 */
	EList<FuelQuantity> getFuels();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='int sum = 0;\nfor (final FuelQuantity fq : getFuels()) {\n\tsum += fq.getCost();\n}\nreturn sum;'"
	 * @generated
	 */
	int getFuelCost();

} // end of  FuelUsage

// finish type fixing
