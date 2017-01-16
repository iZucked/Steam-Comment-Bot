/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel State Attributes</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate <em>Idle Base Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate <em>In Port Base Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getFuelConsumption <em>Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getServiceSpeed <em>Service Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortNBORate <em>In Port NBO Rate</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes()
 * @model
 * @generated
 */
public interface VesselStateAttributes extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Nbo Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nbo Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nbo Rate</em>' attribute.
	 * @see #setNboRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_NboRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263/day' formatString='##0.###'"
	 * @generated
	 */
	double getNboRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nbo Rate</em>' attribute.
	 * @see #getNboRate()
	 * @generated
	 */
	void setNboRate(double value);

	/**
	 * Returns the value of the '<em><b>Idle NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle NBO Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle NBO Rate</em>' attribute.
	 * @see #setIdleNBORate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_IdleNBORate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263/day' formatString='##0.###'"
	 * @generated
	 */
	double getIdleNBORate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle NBO Rate</em>' attribute.
	 * @see #getIdleNBORate()
	 * @generated
	 */
	void setIdleNBORate(double value);

	/**
	 * Returns the value of the '<em><b>Idle Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle Base Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle Base Rate</em>' attribute.
	 * @see #setIdleBaseRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_IdleBaseRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getIdleBaseRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate <em>Idle Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle Base Rate</em>' attribute.
	 * @see #getIdleBaseRate()
	 * @generated
	 */
	void setIdleBaseRate(double value);

	/**
	 * Returns the value of the '<em><b>In Port Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Port Base Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Port Base Rate</em>' attribute.
	 * @see #setInPortBaseRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_InPortBaseRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getInPortBaseRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate <em>In Port Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Port Base Rate</em>' attribute.
	 * @see #getInPortBaseRate()
	 * @generated
	 */
	void setInPortBaseRate(double value);

	/**
	 * Returns the value of the '<em><b>Fuel Consumption</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.FuelConsumption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Consumption</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Consumption</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_FuelConsumption()
	 * @model containment="true"
	 * @generated
	 */
	EList<FuelConsumption> getFuelConsumption();

	/**
	 * Returns the value of the '<em><b>Service Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Speed</em>' attribute.
	 * @see #setServiceSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_ServiceSpeed()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kts' formatString='#0.###'"
	 * @generated
	 */
	double getServiceSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getServiceSpeed <em>Service Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Speed</em>' attribute.
	 * @see #getServiceSpeed()
	 * @generated
	 */
	void setServiceSpeed(double value);

	/**
	 * Returns the value of the '<em><b>In Port NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Port NBO Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Port NBO Rate</em>' attribute.
	 * @see #setInPortNBORate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_InPortNBORate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263/day' formatString='##0.###'"
	 * @generated
	 */
	double getInPortNBORate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortNBORate <em>In Port NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Port NBO Rate</em>' attribute.
	 * @see #getInPortNBORate()
	 * @generated
	 */
	void setInPortNBORate(double value);

} // end of  VesselStateAttributes

// finish type fixing
