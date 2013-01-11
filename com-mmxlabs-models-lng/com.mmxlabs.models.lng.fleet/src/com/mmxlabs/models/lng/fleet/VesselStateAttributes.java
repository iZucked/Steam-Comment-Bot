/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel State Attributes</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate <em>Idle Base Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate <em>In Port Base Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getFuelConsumption <em>Fuel Consumption</em>}</li>
 * </ul>
 * </p>
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
	 * @see #setNboRate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_NboRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m3/d'"
	 * @generated
	 */
	int getNboRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nbo Rate</em>' attribute.
	 * @see #getNboRate()
	 * @generated
	 */
	void setNboRate(int value);

	/**
	 * Returns the value of the '<em><b>Idle NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle NBO Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle NBO Rate</em>' attribute.
	 * @see #setIdleNBORate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_IdleNBORate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m3/d'"
	 * @generated
	 */
	int getIdleNBORate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle NBO Rate</em>' attribute.
	 * @see #getIdleNBORate()
	 * @generated
	 */
	void setIdleNBORate(int value);

	/**
	 * Returns the value of the '<em><b>Idle Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle Base Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle Base Rate</em>' attribute.
	 * @see #setIdleBaseRate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_IdleBaseRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/d'"
	 * @generated
	 */
	int getIdleBaseRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate <em>Idle Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle Base Rate</em>' attribute.
	 * @see #getIdleBaseRate()
	 * @generated
	 */
	void setIdleBaseRate(int value);

	/**
	 * Returns the value of the '<em><b>In Port Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Port Base Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Port Base Rate</em>' attribute.
	 * @see #setInPortBaseRate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselStateAttributes_InPortBaseRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/d'"
	 * @generated
	 */
	int getInPortBaseRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate <em>In Port Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Port Base Rate</em>' attribute.
	 * @see #getInPortBaseRate()
	 * @generated
	 */
	void setInPortBaseRate(int value);

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

} // end of  VesselStateAttributes

// finish type fixing
