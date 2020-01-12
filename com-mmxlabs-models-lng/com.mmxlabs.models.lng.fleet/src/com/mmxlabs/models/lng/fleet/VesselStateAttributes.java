/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#isFuelConsumptionOverride <em>Fuel Consumption Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getFuelConsumption <em>Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getServiceSpeed <em>Service Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortNBORate <em>In Port NBO Rate</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes()
 * @model annotation="http://www.mmxlabs.com/models/featureOverrideByContainer"
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
	 * @see #isSetNboRate()
	 * @see #unsetNboRate()
	 * @see #setNboRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_NboRate()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263/day' formatString='##0.###'"
	 * @generated
	 */
	double getNboRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nbo Rate</em>' attribute.
	 * @see #isSetNboRate()
	 * @see #unsetNboRate()
	 * @see #getNboRate()
	 * @generated
	 */
	void setNboRate(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetNboRate()
	 * @see #getNboRate()
	 * @see #setNboRate(double)
	 * @generated
	 */
	void unsetNboRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Nbo Rate</em>' attribute is set.
	 * @see #unsetNboRate()
	 * @see #getNboRate()
	 * @see #setNboRate(double)
	 * @generated
	 */
	boolean isSetNboRate();

	/**
	 * Returns the value of the '<em><b>Idle NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle NBO Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle NBO Rate</em>' attribute.
	 * @see #isSetIdleNBORate()
	 * @see #unsetIdleNBORate()
	 * @see #setIdleNBORate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_IdleNBORate()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263/day' formatString='##0.###'"
	 * @generated
	 */
	double getIdleNBORate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle NBO Rate</em>' attribute.
	 * @see #isSetIdleNBORate()
	 * @see #unsetIdleNBORate()
	 * @see #getIdleNBORate()
	 * @generated
	 */
	void setIdleNBORate(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIdleNBORate()
	 * @see #getIdleNBORate()
	 * @see #setIdleNBORate(double)
	 * @generated
	 */
	void unsetIdleNBORate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Idle NBO Rate</em>' attribute is set.
	 * @see #unsetIdleNBORate()
	 * @see #getIdleNBORate()
	 * @see #setIdleNBORate(double)
	 * @generated
	 */
	boolean isSetIdleNBORate();

	/**
	 * Returns the value of the '<em><b>Idle Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle Base Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle Base Rate</em>' attribute.
	 * @see #isSetIdleBaseRate()
	 * @see #unsetIdleBaseRate()
	 * @see #setIdleBaseRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_IdleBaseRate()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getIdleBaseRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate <em>Idle Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle Base Rate</em>' attribute.
	 * @see #isSetIdleBaseRate()
	 * @see #unsetIdleBaseRate()
	 * @see #getIdleBaseRate()
	 * @generated
	 */
	void setIdleBaseRate(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate <em>Idle Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIdleBaseRate()
	 * @see #getIdleBaseRate()
	 * @see #setIdleBaseRate(double)
	 * @generated
	 */
	void unsetIdleBaseRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate <em>Idle Base Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Idle Base Rate</em>' attribute is set.
	 * @see #unsetIdleBaseRate()
	 * @see #getIdleBaseRate()
	 * @see #setIdleBaseRate(double)
	 * @generated
	 */
	boolean isSetIdleBaseRate();

	/**
	 * Returns the value of the '<em><b>In Port Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Port Base Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Port Base Rate</em>' attribute.
	 * @see #isSetInPortBaseRate()
	 * @see #unsetInPortBaseRate()
	 * @see #setInPortBaseRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_InPortBaseRate()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getInPortBaseRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate <em>In Port Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Port Base Rate</em>' attribute.
	 * @see #isSetInPortBaseRate()
	 * @see #unsetInPortBaseRate()
	 * @see #getInPortBaseRate()
	 * @generated
	 */
	void setInPortBaseRate(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate <em>In Port Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInPortBaseRate()
	 * @see #getInPortBaseRate()
	 * @see #setInPortBaseRate(double)
	 * @generated
	 */
	void unsetInPortBaseRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate <em>In Port Base Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>In Port Base Rate</em>' attribute is set.
	 * @see #unsetInPortBaseRate()
	 * @see #getInPortBaseRate()
	 * @see #setInPortBaseRate(double)
	 * @generated
	 */
	boolean isSetInPortBaseRate();

	/**
	 * Returns the value of the '<em><b>Fuel Consumption Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Consumption Override</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Consumption Override</em>' attribute.
	 * @see #setFuelConsumptionOverride(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_FuelConsumptionOverride()
	 * @model
	 * @generated
	 */
	boolean isFuelConsumptionOverride();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#isFuelConsumptionOverride <em>Fuel Consumption Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Consumption Override</em>' attribute.
	 * @see #isFuelConsumptionOverride()
	 * @generated
	 */
	void setFuelConsumptionOverride(boolean value);

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
	 * @see #isSetServiceSpeed()
	 * @see #unsetServiceSpeed()
	 * @see #setServiceSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_ServiceSpeed()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kts' formatString='#0.###'"
	 * @generated
	 */
	double getServiceSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getServiceSpeed <em>Service Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Speed</em>' attribute.
	 * @see #isSetServiceSpeed()
	 * @see #unsetServiceSpeed()
	 * @see #getServiceSpeed()
	 * @generated
	 */
	void setServiceSpeed(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getServiceSpeed <em>Service Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetServiceSpeed()
	 * @see #getServiceSpeed()
	 * @see #setServiceSpeed(double)
	 * @generated
	 */
	void unsetServiceSpeed();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getServiceSpeed <em>Service Speed</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Service Speed</em>' attribute is set.
	 * @see #unsetServiceSpeed()
	 * @see #getServiceSpeed()
	 * @see #setServiceSpeed(double)
	 * @generated
	 */
	boolean isSetServiceSpeed();

	/**
	 * Returns the value of the '<em><b>In Port NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Port NBO Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Port NBO Rate</em>' attribute.
	 * @see #isSetInPortNBORate()
	 * @see #unsetInPortNBORate()
	 * @see #setInPortNBORate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_InPortNBORate()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263/day' formatString='##0.###'"
	 * @generated
	 */
	double getInPortNBORate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortNBORate <em>In Port NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Port NBO Rate</em>' attribute.
	 * @see #isSetInPortNBORate()
	 * @see #unsetInPortNBORate()
	 * @see #getInPortNBORate()
	 * @generated
	 */
	void setInPortNBORate(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortNBORate <em>In Port NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInPortNBORate()
	 * @see #getInPortNBORate()
	 * @see #setInPortNBORate(double)
	 * @generated
	 */
	void unsetInPortNBORate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortNBORate <em>In Port NBO Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>In Port NBO Rate</em>' attribute is set.
	 * @see #unsetInPortNBORate()
	 * @see #getInPortNBORate()
	 * @see #setInPortNBORate(double)
	 * @generated
	 */
	boolean isSetInPortNBORate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateNBORate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateIdleNBORate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateIdleBaseRate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateInPortBaseRate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateServiceSpeed();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateInPortNBORate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	EList<FuelConsumption> getVesselOrDelegateFuelConsumption();

} // end of  VesselStateAttributes

// finish type fixing
