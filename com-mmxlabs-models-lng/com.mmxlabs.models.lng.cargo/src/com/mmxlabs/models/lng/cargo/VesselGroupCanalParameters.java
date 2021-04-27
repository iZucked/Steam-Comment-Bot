/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Group Canal Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters#getNorthboundWaitingDays <em>Northbound Waiting Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters#getSouthboundWaitingDays <em>Southbound Waiting Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters#getVesselGroup <em>Vessel Group</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselGroupCanalParameters()
 * @model
 * @generated
 */
public interface VesselGroupCanalParameters extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Northbound Waiting Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Northbound Waiting Days</em>' attribute.
	 * @see #setNorthboundWaitingDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselGroupCanalParameters_NorthboundWaitingDays()
	 * @model
	 * @generated
	 */
	int getNorthboundWaitingDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters#getNorthboundWaitingDays <em>Northbound Waiting Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Northbound Waiting Days</em>' attribute.
	 * @see #getNorthboundWaitingDays()
	 * @generated
	 */
	void setNorthboundWaitingDays(int value);

	/**
	 * Returns the value of the '<em><b>Southbound Waiting Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Southbound Waiting Days</em>' attribute.
	 * @see #setSouthboundWaitingDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselGroupCanalParameters_SouthboundWaitingDays()
	 * @model
	 * @generated
	 */
	int getSouthboundWaitingDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters#getSouthboundWaitingDays <em>Southbound Waiting Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Southbound Waiting Days</em>' attribute.
	 * @see #getSouthboundWaitingDays()
	 * @generated
	 */
	void setSouthboundWaitingDays(int value);

	/**
	 * Returns the value of the '<em><b>Vessel Group</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}<code>&lt;com.mmxlabs.models.lng.fleet.Vessel&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Group</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselGroupCanalParameters_VesselGroup()
	 * @model
	 * @generated
	 */
	EList<AVesselSet<Vessel>> getVesselGroup();

} // VesselGroupCanalParameters
