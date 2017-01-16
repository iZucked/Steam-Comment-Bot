/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unit Cost Line</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getUnitCost <em>Unit Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getMmbtuDelivered <em>Mmbtu Delivered</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getFrom <em>From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getTo <em>To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getVolumeLoaded <em>Volume Loaded</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getVolumeDischarged <em>Volume Discharged</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getFuelCost <em>Fuel Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getCanalCost <em>Canal Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getCostComponents <em>Cost Components</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getPortCost <em>Port Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getProfit <em>Profit</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine()
 * @model
 * @generated
 */
public interface UnitCostLine extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Unit Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit Cost</em>' attribute.
	 * @see #setUnitCost(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_UnitCost()
	 * @model required="true"
	 * @generated
	 */
	double getUnitCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getUnitCost <em>Unit Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Cost</em>' attribute.
	 * @see #getUnitCost()
	 * @generated
	 */
	void setUnitCost(double value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Total Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getHireCost() + getFuelCost() + getCanalCost() + getPortCost();'"
	 * @generated
	 */
	int getTotalCost();

	/**
	 * Returns the value of the '<em><b>Mmbtu Delivered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mmbtu Delivered</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mmbtu Delivered</em>' attribute.
	 * @see #setMmbtuDelivered(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_MmbtuDelivered()
	 * @model required="true"
	 * @generated
	 */
	int getMmbtuDelivered();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getMmbtuDelivered <em>Mmbtu Delivered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mmbtu Delivered</em>' attribute.
	 * @see #getMmbtuDelivered()
	 * @generated
	 */
	void setMmbtuDelivered(int value);

	/**
	 * Returns the value of the '<em><b>From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' reference.
	 * @see #setFrom(Port)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_From()
	 * @model required="true"
	 * @generated
	 */
	Port getFrom();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getFrom <em>From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From</em>' reference.
	 * @see #getFrom()
	 * @generated
	 */
	void setFrom(Port value);

	/**
	 * Returns the value of the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' reference.
	 * @see #setTo(Port)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_To()
	 * @model required="true"
	 * @generated
	 */
	Port getTo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getTo <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To</em>' reference.
	 * @see #getTo()
	 * @generated
	 */
	void setTo(Port value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_Duration()
	 * @model required="true"
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

	/**
	 * Returns the value of the '<em><b>Volume Loaded</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Loaded</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Loaded</em>' attribute.
	 * @see #setVolumeLoaded(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_VolumeLoaded()
	 * @model required="true"
	 * @generated
	 */
	int getVolumeLoaded();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getVolumeLoaded <em>Volume Loaded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Loaded</em>' attribute.
	 * @see #getVolumeLoaded()
	 * @generated
	 */
	void setVolumeLoaded(int value);

	/**
	 * Returns the value of the '<em><b>Volume Discharged</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Discharged</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Discharged</em>' attribute.
	 * @see #setVolumeDischarged(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_VolumeDischarged()
	 * @model required="true"
	 * @generated
	 */
	int getVolumeDischarged();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getVolumeDischarged <em>Volume Discharged</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Discharged</em>' attribute.
	 * @see #getVolumeDischarged()
	 * @generated
	 */
	void setVolumeDischarged(int value);

	/**
	 * Returns the value of the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hire Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Cost</em>' attribute.
	 * @see #setHireCost(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_HireCost()
	 * @model required="true"
	 * @generated
	 */
	int getHireCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getHireCost <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Cost</em>' attribute.
	 * @see #getHireCost()
	 * @generated
	 */
	void setHireCost(int value);

	/**
	 * Returns the value of the '<em><b>Fuel Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Cost</em>' attribute.
	 * @see #setFuelCost(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_FuelCost()
	 * @model required="true"
	 * @generated
	 */
	int getFuelCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getFuelCost <em>Fuel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Cost</em>' attribute.
	 * @see #getFuelCost()
	 * @generated
	 */
	void setFuelCost(int value);

	/**
	 * Returns the value of the '<em><b>Canal Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Cost</em>' attribute.
	 * @see #setCanalCost(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_CanalCost()
	 * @model required="true"
	 * @generated
	 */
	int getCanalCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getCanalCost <em>Canal Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Cost</em>' attribute.
	 * @see #getCanalCost()
	 * @generated
	 */
	void setCanalCost(int value);

	/**
	 * Returns the value of the '<em><b>Cost Components</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.CostComponent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cost Components</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cost Components</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_CostComponents()
	 * @model containment="true"
	 * @generated
	 */
	EList<CostComponent> getCostComponents();

	/**
	 * Returns the value of the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Cost</em>' attribute.
	 * @see #setPortCost(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_PortCost()
	 * @model required="true"
	 * @generated
	 */
	int getPortCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getPortCost <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Cost</em>' attribute.
	 * @see #getPortCost()
	 * @generated
	 */
	void setPortCost(int value);

	/**
	 * Returns the value of the '<em><b>Profit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profit</em>' attribute.
	 * @see #setProfit(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostLine_Profit()
	 * @model required="true"
	 * @generated
	 */
	int getProfit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getProfit <em>Profit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profit</em>' attribute.
	 * @see #getProfit()
	 * @generated
	 */
	void setProfit(int value);

} // end of  UnitCostLine

// finish type fixing
