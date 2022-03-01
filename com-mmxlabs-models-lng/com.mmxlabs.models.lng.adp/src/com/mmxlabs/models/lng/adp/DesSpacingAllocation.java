/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import com.mmxlabs.models.lng.commercial.SalesContract;

import com.mmxlabs.models.lng.port.Port;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Des Spacing Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.DesSpacingAllocation#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.DesSpacingAllocation#getDesSpacingRows <em>Des Spacing Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.DesSpacingAllocation#getPort <em>Port</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDesSpacingAllocation()
 * @model
 * @generated
 */
public interface DesSpacingAllocation extends SpacingAllocation {
	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(VesselAvailability)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDesSpacingAllocation_Vessel()
	 * @model
	 * @generated
	 */
	VesselAvailability getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DesSpacingAllocation#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(VesselAvailability value);

	/**
	 * Returns the value of the '<em><b>Des Spacing Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.DesSpacingRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Spacing Rows</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDesSpacingAllocation_DesSpacingRows()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<DesSpacingRow> getDesSpacingRows();

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDesSpacingAllocation_Port()
	 * @model
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DesSpacingAllocation#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

} // DesSpacingAllocation
