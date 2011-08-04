/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import java.util.Date;

import org.eclipse.emf.common.util.EList;
import scenario.AnnotatedObject;
import org.eclipse.emf.ecore.EObject;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.VesselEvent#getId <em>Id</em>}</li>
 *   <li>{@link scenario.fleet.VesselEvent#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link scenario.fleet.VesselEvent#getEndDate <em>End Date</em>}</li>
 *   <li>{@link scenario.fleet.VesselEvent#getDuration <em>Duration</em>}</li>
 *   <li>{@link scenario.fleet.VesselEvent#getVessels <em>Vessels</em>}</li>
 *   <li>{@link scenario.fleet.VesselEvent#getVesselClasses <em>Vessel Classes</em>}</li>
 *   <li>{@link scenario.fleet.VesselEvent#getStartPort <em>Start Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getVesselEvent()
 * @model abstract="true"
 * @generated
 */
public interface VesselEvent extends AnnotatedObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see scenario.fleet.FleetPackage#getVesselEvent_Id()
	 * @model required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselEvent#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #setStartDate(Date)
	 * @see scenario.fleet.FleetPackage#getVesselEvent_StartDate()
	 * @model
	 * @generated
	 */
	Date getStartDate();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselEvent#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(Date value);

	/**
	 * Returns the value of the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Date</em>' attribute.
	 * @see #setEndDate(Date)
	 * @see scenario.fleet.FleetPackage#getVesselEvent_EndDate()
	 * @model
	 * @generated
	 */
	Date getEndDate();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselEvent#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Date</em>' attribute.
	 * @see #getEndDate()
	 * @generated
	 */
	void setEndDate(Date value);

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
	 * @see scenario.fleet.FleetPackage#getVesselEvent_Duration()
	 * @model
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselEvent#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

	/**
	 * Returns the value of the '<em><b>Start Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Port</em>' reference.
	 * @see #setStartPort(Port)
	 * @see scenario.fleet.FleetPackage#getVesselEvent_StartPort()
	 * @model required="true"
	 * @generated
	 */
	Port getStartPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselEvent#getStartPort <em>Start Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Port</em>' reference.
	 * @see #getStartPort()
	 * @generated
	 */
	void setStartPort(Port value);

	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link scenario.fleet.Vessel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see scenario.fleet.FleetPackage#getVesselEvent_Vessels()
	 * @model
	 * @generated
	 */
	EList<Vessel> getVessels();

	/**
	 * Returns the value of the '<em><b>Vessel Classes</b></em>' reference list.
	 * The list contents are of type {@link scenario.fleet.VesselClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Classes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Classes</em>' reference list.
	 * @see scenario.fleet.FleetPackage#getVesselEvent_VesselClasses()
	 * @model
	 * @generated
	 */
	EList<VesselClass> getVesselClasses();

} // VesselEvent
