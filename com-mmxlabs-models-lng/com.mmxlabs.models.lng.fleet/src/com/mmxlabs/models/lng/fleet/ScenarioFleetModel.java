

/**
 */
package com.mmxlabs.models.lng.fleet;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Fleet Model</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.ScenarioFleetModel#getVesselAvailabilities <em>Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.ScenarioFleetModel#getVesselEvents <em>Vessel Events</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getScenarioFleetModel()
 * @model
 * @generated
 */
public interface ScenarioFleetModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Vessel Availabilities</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselAvailability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Availabilities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Availabilities</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getScenarioFleetModel_VesselAvailabilities()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselAvailability> getVesselAvailabilities();

	/**
	 * Returns the value of the '<em><b>Vessel Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getScenarioFleetModel_VesselEvents()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselEvent> getVesselEvents();

} // ScenarioFleetModel

// finish type fixing

