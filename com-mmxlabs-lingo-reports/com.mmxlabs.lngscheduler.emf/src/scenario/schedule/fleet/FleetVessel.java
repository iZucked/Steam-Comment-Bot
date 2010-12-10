/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleet;

import scenario.fleet.Vessel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.fleet.FleetVessel#getVessel <em>Vessel</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.fleet.FleetPackage#getFleetVessel()
 * @model
 * @generated
 */
public interface FleetVessel extends AllocatedVessel {
	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(Vessel)
	 * @see scenario.schedule.fleet.FleetPackage#getFleetVessel_Vessel()
	 * @model required="true"
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link scenario.schedule.fleet.FleetVessel#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

} // FleetVessel
