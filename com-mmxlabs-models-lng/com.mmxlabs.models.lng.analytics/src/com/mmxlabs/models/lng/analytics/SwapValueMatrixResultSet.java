/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Swap Value Matrix Result Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getResults <em>Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getSwapFee <em>Swap Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getGeneratedSpotLoadSlot <em>Generated Spot Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getGeneratedSpotDischargeSlot <em>Generated Spot Discharge Slot</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResultSet()
 * @model
 * @generated
 */
public interface SwapValueMatrixResultSet extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Results</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Results</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResultSet_Results()
	 * @model containment="true"
	 * @generated
	 */
	EList<SwapValueMatrixResult> getResults();

	/**
	 * Returns the value of the '<em><b>Swap Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Fee</em>' attribute.
	 * @see #setSwapFee(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResultSet_SwapFee()
	 * @model
	 * @generated
	 */
	double getSwapFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getSwapFee <em>Swap Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Fee</em>' attribute.
	 * @see #getSwapFee()
	 * @generated
	 */
	void setSwapFee(double value);

	/**
	 * Returns the value of the '<em><b>Generated Spot Load Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generated Spot Load Slot</em>' containment reference.
	 * @see #setGeneratedSpotLoadSlot(SpotLoadSlot)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResultSet_GeneratedSpotLoadSlot()
	 * @model containment="true"
	 * @generated
	 */
	SpotLoadSlot getGeneratedSpotLoadSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getGeneratedSpotLoadSlot <em>Generated Spot Load Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generated Spot Load Slot</em>' containment reference.
	 * @see #getGeneratedSpotLoadSlot()
	 * @generated
	 */
	void setGeneratedSpotLoadSlot(SpotLoadSlot value);

	/**
	 * Returns the value of the '<em><b>Generated Spot Discharge Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generated Spot Discharge Slot</em>' containment reference.
	 * @see #setGeneratedSpotDischargeSlot(SpotDischargeSlot)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResultSet_GeneratedSpotDischargeSlot()
	 * @model containment="true"
	 * @generated
	 */
	SpotDischargeSlot getGeneratedSpotDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getGeneratedSpotDischargeSlot <em>Generated Spot Discharge Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generated Spot Discharge Slot</em>' containment reference.
	 * @see #getGeneratedSpotDischargeSlot()
	 * @generated
	 */
	void setGeneratedSpotDischargeSlot(SpotDischargeSlot value);

} // SwapValueMatrixResultSet
