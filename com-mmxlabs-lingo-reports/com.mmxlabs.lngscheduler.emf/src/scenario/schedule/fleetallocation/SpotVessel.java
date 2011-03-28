/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleetallocation;

import scenario.fleet.VesselClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Vessel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.fleetallocation.SpotVessel#getIndex <em>Index</em>}</li>
 *   <li>{@link scenario.schedule.fleetallocation.SpotVessel#getVesselClass <em>Vessel Class</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.fleetallocation.FleetallocationPackage#getSpotVessel()
 * @model
 * @generated
 */
public interface SpotVessel extends AllocatedVessel {
	/**
	 * Returns the value of the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' attribute.
	 * @see #setIndex(int)
	 * @see scenario.schedule.fleetallocation.FleetallocationPackage#getSpotVessel_Index()
	 * @model required="true"
	 * @generated
	 */
	int getIndex();

	/**
	 * Sets the value of the '{@link scenario.schedule.fleetallocation.SpotVessel#getIndex <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' attribute.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(int value);

	/**
	 * Returns the value of the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Class</em>' reference.
	 * @see #setVesselClass(VesselClass)
	 * @see scenario.schedule.fleetallocation.FleetallocationPackage#getSpotVessel_VesselClass()
	 * @model required="true"
	 * @generated
	 */
	VesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link scenario.schedule.fleetallocation.SpotVessel#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Class</em>' reference.
	 * @see #getVesselClass()
	 * @generated
	 */
	void setVesselClass(VesselClass value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return \r\n((getVesselClass().getDailyCharterPrice() * 1000) / 24) / 1000.0;'"
	 * @generated
	 */
	double getHourlyCharterPrice();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getVesselClass().getName() + \" \" + getIndex();'"
	 * @generated
	 */
	String getName();

} // SpotVessel
