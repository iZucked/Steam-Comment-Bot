/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.Schedule;

import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Swap Value Matrix Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPnlMinusBasePnl <em>Swap Pnl Minus Base Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBasePrecedingPnl <em>Base Preceding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseSucceedingPnl <em>Base Succeeding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPrecedingPnl <em>Swap Preceding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapSucceedingPnl <em>Swap Succeeding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseCargo <em>Base Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapDiversionCargo <em>Swap Diversion Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillCargo <em>Swap Backfill Cargo</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult()
 * @model
 * @generated
 */
public interface SwapValueMatrixResult extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Swap Pnl Minus Base Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Pnl Minus Base Pnl</em>' attribute.
	 * @see #setSwapPnlMinusBasePnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapPnlMinusBasePnl()
	 * @model
	 * @generated
	 */
	long getSwapPnlMinusBasePnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPnlMinusBasePnl <em>Swap Pnl Minus Base Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Pnl Minus Base Pnl</em>' attribute.
	 * @see #getSwapPnlMinusBasePnl()
	 * @generated
	 */
	void setSwapPnlMinusBasePnl(long value);

	/**
	 * Returns the value of the '<em><b>Base Preceding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Preceding Pnl</em>' attribute.
	 * @see #setBasePrecedingPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BasePrecedingPnl()
	 * @model
	 * @generated
	 */
	long getBasePrecedingPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBasePrecedingPnl <em>Base Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Preceding Pnl</em>' attribute.
	 * @see #getBasePrecedingPnl()
	 * @generated
	 */
	void setBasePrecedingPnl(long value);

	/**
	 * Returns the value of the '<em><b>Base Succeeding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Succeeding Pnl</em>' attribute.
	 * @see #setBaseSucceedingPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseSucceedingPnl()
	 * @model
	 * @generated
	 */
	long getBaseSucceedingPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseSucceedingPnl <em>Base Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Succeeding Pnl</em>' attribute.
	 * @see #getBaseSucceedingPnl()
	 * @generated
	 */
	void setBaseSucceedingPnl(long value);

	/**
	 * Returns the value of the '<em><b>Swap Preceding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Preceding Pnl</em>' attribute.
	 * @see #setSwapPrecedingPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapPrecedingPnl()
	 * @model
	 * @generated
	 */
	long getSwapPrecedingPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPrecedingPnl <em>Swap Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Preceding Pnl</em>' attribute.
	 * @see #getSwapPrecedingPnl()
	 * @generated
	 */
	void setSwapPrecedingPnl(long value);

	/**
	 * Returns the value of the '<em><b>Swap Succeeding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Succeeding Pnl</em>' attribute.
	 * @see #setSwapSucceedingPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapSucceedingPnl()
	 * @model
	 * @generated
	 */
	long getSwapSucceedingPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapSucceedingPnl <em>Swap Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Succeeding Pnl</em>' attribute.
	 * @see #getSwapSucceedingPnl()
	 * @generated
	 */
	void setSwapSucceedingPnl(long value);

	/**
	 * Returns the value of the '<em><b>Base Cargo</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Cargo</em>' containment reference.
	 * @see #setBaseCargo(SwapValueMatrixShippedCargoResult)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseCargo()
	 * @model containment="true"
	 * @generated
	 */
	SwapValueMatrixShippedCargoResult getBaseCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseCargo <em>Base Cargo</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Cargo</em>' containment reference.
	 * @see #getBaseCargo()
	 * @generated
	 */
	void setBaseCargo(SwapValueMatrixShippedCargoResult value);

	/**
	 * Returns the value of the '<em><b>Swap Diversion Cargo</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Diversion Cargo</em>' containment reference.
	 * @see #setSwapDiversionCargo(SwapValueMatrixShippedCargoResult)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapDiversionCargo()
	 * @model containment="true"
	 * @generated
	 */
	SwapValueMatrixShippedCargoResult getSwapDiversionCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapDiversionCargo <em>Swap Diversion Cargo</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Diversion Cargo</em>' containment reference.
	 * @see #getSwapDiversionCargo()
	 * @generated
	 */
	void setSwapDiversionCargo(SwapValueMatrixShippedCargoResult value);

	/**
	 * Returns the value of the '<em><b>Swap Backfill Cargo</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Backfill Cargo</em>' containment reference.
	 * @see #setSwapBackfillCargo(SwapValueMatrixNonShippedCargoResult)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapBackfillCargo()
	 * @model containment="true"
	 * @generated
	 */
	SwapValueMatrixNonShippedCargoResult getSwapBackfillCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillCargo <em>Swap Backfill Cargo</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Backfill Cargo</em>' containment reference.
	 * @see #getSwapBackfillCargo()
	 * @generated
	 */
	void setSwapBackfillCargo(SwapValueMatrixNonShippedCargoResult value);

} // SwapValueMatrixResult
