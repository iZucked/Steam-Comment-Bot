

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.port.Port;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getDailyHireRate <em>Daily Hire Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getInitialPort <em>Initial Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getFinalPort <em>Final Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getAvailability <em>Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isTimeChartered <em>Time Chartered</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getTimeCharterRate <em>Time Charter Rate</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel()
 * @model
 * @generated
 */
public interface Vessel extends AVessel {
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
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_VesselClass()
	 * @model required="true"
	 * @generated
	 */
	VesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Class</em>' reference.
	 * @see #getVesselClass()
	 * @generated
	 */
	void setVesselClass(VesselClass value);

	/**
	 * Returns the value of the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_InaccessiblePorts()
	 * @model
	 * @generated
	 */
	EList<APortSet> getInaccessiblePorts();

	/**
	 * Returns the value of the '<em><b>Daily Hire Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Daily Hire Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Daily Hire Rate</em>' attribute.
	 * @see #isSetDailyHireRate()
	 * @see #unsetDailyHireRate()
	 * @see #setDailyHireRate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_DailyHireRate()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getDailyHireRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getDailyHireRate <em>Daily Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Daily Hire Rate</em>' attribute.
	 * @see #isSetDailyHireRate()
	 * @see #unsetDailyHireRate()
	 * @see #getDailyHireRate()
	 * @generated
	 */
	void setDailyHireRate(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getDailyHireRate <em>Daily Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDailyHireRate()
	 * @see #getDailyHireRate()
	 * @see #setDailyHireRate(int)
	 * @generated
	 */
	void unsetDailyHireRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getDailyHireRate <em>Daily Hire Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Daily Hire Rate</em>' attribute is set.
	 * @see #unsetDailyHireRate()
	 * @see #getDailyHireRate()
	 * @see #setDailyHireRate(int)
	 * @generated
	 */
	boolean isSetDailyHireRate();

	/**
	 * Returns the value of the '<em><b>Initial Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Port</em>' reference.
	 * @see #isSetInitialPort()
	 * @see #unsetInitialPort()
	 * @see #setInitialPort(Port)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_InitialPort()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Port getInitialPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getInitialPort <em>Initial Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Port</em>' reference.
	 * @see #isSetInitialPort()
	 * @see #unsetInitialPort()
	 * @see #getInitialPort()
	 * @generated
	 */
	void setInitialPort(Port value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getInitialPort <em>Initial Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInitialPort()
	 * @see #getInitialPort()
	 * @see #setInitialPort(Port)
	 * @generated
	 */
	void unsetInitialPort();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getInitialPort <em>Initial Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Initial Port</em>' reference is set.
	 * @see #unsetInitialPort()
	 * @see #getInitialPort()
	 * @see #setInitialPort(Port)
	 * @generated
	 */
	boolean isSetInitialPort();

	/**
	 * Returns the value of the '<em><b>Final Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Final Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Final Port</em>' reference.
	 * @see #isSetFinalPort()
	 * @see #unsetFinalPort()
	 * @see #setFinalPort(Port)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_FinalPort()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Port getFinalPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFinalPort <em>Final Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Final Port</em>' reference.
	 * @see #isSetFinalPort()
	 * @see #unsetFinalPort()
	 * @see #getFinalPort()
	 * @generated
	 */
	void setFinalPort(Port value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFinalPort <em>Final Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFinalPort()
	 * @see #getFinalPort()
	 * @see #setFinalPort(Port)
	 * @generated
	 */
	void unsetFinalPort();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFinalPort <em>Final Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Final Port</em>' reference is set.
	 * @see #unsetFinalPort()
	 * @see #getFinalPort()
	 * @see #setFinalPort(Port)
	 * @generated
	 */
	boolean isSetFinalPort();

	/**
	 * Returns the value of the '<em><b>Start Heel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Heel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Heel</em>' reference.
	 * @see #setStartHeel(HeelOptions)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_StartHeel()
	 * @model required="true"
	 * @generated
	 */
	HeelOptions getStartHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getStartHeel <em>Start Heel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Heel</em>' reference.
	 * @see #getStartHeel()
	 * @generated
	 */
	void setStartHeel(HeelOptions value);

	/**
	 * Returns the value of the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Availability</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Availability</em>' containment reference.
	 * @see #setAvailability(VesselAvailablility)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Availability()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VesselAvailablility getAvailability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getAvailability <em>Availability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Availability</em>' containment reference.
	 * @see #getAvailability()
	 * @generated
	 */
	void setAvailability(VesselAvailablility value);

	/**
	 * Returns the value of the '<em><b>Time Chartered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Chartered</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Chartered</em>' attribute.
	 * @see #setTimeChartered(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_TimeChartered()
	 * @model required="true"
	 * @generated
	 */
	boolean isTimeChartered();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isTimeChartered <em>Time Chartered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Chartered</em>' attribute.
	 * @see #isTimeChartered()
	 * @generated
	 */
	void setTimeChartered(boolean value);

	/**
	 * Returns the value of the '<em><b>Time Charter Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Charter Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Charter Rate</em>' attribute.
	 * @see #setTimeCharterRate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_TimeCharterRate()
	 * @model required="true"
	 * @generated
	 */
	int getTimeCharterRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getTimeCharterRate <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Charter Rate</em>' attribute.
	 * @see #getTimeCharterRate()
	 * @generated
	 */
	void setTimeCharterRate(int value);

} // end of  Vessel

// finish type fixing
