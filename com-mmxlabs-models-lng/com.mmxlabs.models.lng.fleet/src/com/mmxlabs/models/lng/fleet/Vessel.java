/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselSet;
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
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getAvailability <em>Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getStartHeel <em>Start Heel</em>}</li>
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
	 * Returns the value of the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Heel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Heel</em>' containment reference.
	 * @see #setStartHeel(HeelOptions)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_StartHeel()
	 * @model containment="true" required="true"
	 * @generated
	 */
	HeelOptions getStartHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getStartHeel <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Heel</em>' containment reference.
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
	 * @see #setAvailability(VesselAvailability)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Availability()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VesselAvailability getAvailability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getAvailability <em>Availability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Availability</em>' containment reference.
	 * @see #getAvailability()
	 * @generated
	 */
	void setAvailability(VesselAvailability value);

	/**
	 * Returns the value of the '<em><b>Time Charter Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Charter Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Charter Rate</em>' attribute.
	 * @see #isSetTimeCharterRate()
	 * @see #unsetTimeCharterRate()
	 * @see #setTimeCharterRate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_TimeCharterRate()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getTimeCharterRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getTimeCharterRate <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Charter Rate</em>' attribute.
	 * @see #isSetTimeCharterRate()
	 * @see #unsetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @generated
	 */
	void setTimeCharterRate(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getTimeCharterRate <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @see #setTimeCharterRate(int)
	 * @generated
	 */
	void unsetTimeCharterRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getTimeCharterRate <em>Time Charter Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Time Charter Rate</em>' attribute is set.
	 * @see #unsetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @see #setTimeCharterRate(int)
	 * @generated
	 */
	boolean isSetTimeCharterRate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model markedMany="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (marked.contains(this)) return org.eclipse.emf.common.util.ECollections.emptyEList();\nfinal org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.AVessel> result = new org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.AVessel>();\nmarked.add(this);\nresult.add(this);\nreturn result;'"
	 * @generated
	 */
	EList<AVessel> collect(EList<AVesselSet> marked);

} // end of  Vessel

// finish type fixing
