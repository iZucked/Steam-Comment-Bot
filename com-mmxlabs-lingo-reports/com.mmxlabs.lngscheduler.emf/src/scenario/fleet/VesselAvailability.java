/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.VesselAvailability#getStartPort <em>Start Port</em>}</li>
 *   <li>{@link scenario.fleet.VesselAvailability#getEndPort <em>End Port</em>}</li>
 *   <li>{@link scenario.fleet.VesselAvailability#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link scenario.fleet.VesselAvailability#getDuration <em>Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getVesselAvailability()
 * @model
 * @generated
 */
public interface VesselAvailability extends EObject {
	/**
	 * Returns the value of the '<em><b>Start Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Port</em>' reference.
	 * @see #isSetStartPort()
	 * @see #unsetStartPort()
	 * @see #setStartPort(Port)
	 * @see scenario.fleet.FleetPackage#getVesselAvailability_StartPort()
	 * @model unsettable="true"
	 * @generated
	 */
	Port getStartPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselAvailability#getStartPort <em>Start Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Port</em>' reference.
	 * @see #isSetStartPort()
	 * @see #unsetStartPort()
	 * @see #getStartPort()
	 * @generated
	 */
	void setStartPort(Port value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.VesselAvailability#getStartPort <em>Start Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartPort()
	 * @see #getStartPort()
	 * @see #setStartPort(Port)
	 * @generated
	 */
	void unsetStartPort();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.VesselAvailability#getStartPort <em>Start Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Port</em>' reference is set.
	 * @see #unsetStartPort()
	 * @see #getStartPort()
	 * @see #setStartPort(Port)
	 * @generated
	 */
	boolean isSetStartPort();

	/**
	 * Returns the value of the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Port</em>' reference.
	 * @see #isSetEndPort()
	 * @see #unsetEndPort()
	 * @see #setEndPort(Port)
	 * @see scenario.fleet.FleetPackage#getVesselAvailability_EndPort()
	 * @model unsettable="true"
	 * @generated
	 */
	Port getEndPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselAvailability#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Port</em>' reference.
	 * @see #isSetEndPort()
	 * @see #unsetEndPort()
	 * @see #getEndPort()
	 * @generated
	 */
	void setEndPort(Port value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.VesselAvailability#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndPort()
	 * @see #getEndPort()
	 * @see #setEndPort(Port)
	 * @generated
	 */
	void unsetEndPort();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.VesselAvailability#getEndPort <em>End Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Port</em>' reference is set.
	 * @see #unsetEndPort()
	 * @see #getEndPort()
	 * @see #setEndPort(Port)
	 * @generated
	 */
	boolean isSetEndPort();

	/**
	 * Returns the value of the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Time</em>' attribute.
	 * @see #isSetStartTime()
	 * @see #unsetStartTime()
	 * @see #setStartTime(Date)
	 * @see scenario.fleet.FleetPackage#getVesselAvailability_StartTime()
	 * @model unsettable="true"
	 * @generated
	 */
	Date getStartTime();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselAvailability#getStartTime <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Time</em>' attribute.
	 * @see #isSetStartTime()
	 * @see #unsetStartTime()
	 * @see #getStartTime()
	 * @generated
	 */
	void setStartTime(Date value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.VesselAvailability#getStartTime <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartTime()
	 * @see #getStartTime()
	 * @see #setStartTime(Date)
	 * @generated
	 */
	void unsetStartTime();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.VesselAvailability#getStartTime <em>Start Time</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Time</em>' attribute is set.
	 * @see #unsetStartTime()
	 * @see #getStartTime()
	 * @see #setStartTime(Date)
	 * @generated
	 */
	boolean isSetStartTime();

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #isSetDuration()
	 * @see #unsetDuration()
	 * @see #setDuration(int)
	 * @see scenario.fleet.FleetPackage#getVesselAvailability_Duration()
	 * @model unsettable="true"
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselAvailability#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #isSetDuration()
	 * @see #unsetDuration()
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.VesselAvailability#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDuration()
	 * @see #getDuration()
	 * @see #setDuration(int)
	 * @generated
	 */
	void unsetDuration();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.VesselAvailability#getDuration <em>Duration</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Duration</em>' attribute is set.
	 * @see #unsetDuration()
	 * @see #getDuration()
	 * @see #setDuration(int)
	 * @generated
	 */
	boolean isSetDuration();

} // VesselAvailability
