

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.mmxcore.MMXObject;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Availablility</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartBefore <em>Start Before</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndAfter <em>End After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndBefore <em>End Before</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselAvailablility()
 * @model
 * @generated
 */
public interface VesselAvailablility extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Start At</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start At</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start At</em>' reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselAvailablility_StartAt()
	 * @model
	 * @generated
	 */
	EList<APortSet> getStartAt();

	/**
	 * Returns the value of the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start After</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start After</em>' attribute.
	 * @see #isSetStartAfter()
	 * @see #unsetStartAfter()
	 * @see #setStartAfter(Date)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselAvailablility_StartAfter()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getStartAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartAfter <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start After</em>' attribute.
	 * @see #isSetStartAfter()
	 * @see #unsetStartAfter()
	 * @see #getStartAfter()
	 * @generated
	 */
	void setStartAfter(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartAfter <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartAfter()
	 * @see #getStartAfter()
	 * @see #setStartAfter(Date)
	 * @generated
	 */
	void unsetStartAfter();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartAfter <em>Start After</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start After</em>' attribute is set.
	 * @see #unsetStartAfter()
	 * @see #getStartAfter()
	 * @see #setStartAfter(Date)
	 * @generated
	 */
	boolean isSetStartAfter();

	/**
	 * Returns the value of the '<em><b>Start Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Before</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Before</em>' attribute.
	 * @see #isSetStartBefore()
	 * @see #unsetStartBefore()
	 * @see #setStartBefore(Date)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselAvailablility_StartBefore()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getStartBefore();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartBefore <em>Start Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Before</em>' attribute.
	 * @see #isSetStartBefore()
	 * @see #unsetStartBefore()
	 * @see #getStartBefore()
	 * @generated
	 */
	void setStartBefore(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartBefore <em>Start Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartBefore()
	 * @see #getStartBefore()
	 * @see #setStartBefore(Date)
	 * @generated
	 */
	void unsetStartBefore();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getStartBefore <em>Start Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Before</em>' attribute is set.
	 * @see #unsetStartBefore()
	 * @see #getStartBefore()
	 * @see #setStartBefore(Date)
	 * @generated
	 */
	boolean isSetStartBefore();

	/**
	 * Returns the value of the '<em><b>End At</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End At</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End At</em>' reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselAvailablility_EndAt()
	 * @model
	 * @generated
	 */
	EList<APortSet> getEndAt();

	/**
	 * Returns the value of the '<em><b>End After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End After</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End After</em>' attribute.
	 * @see #isSetEndAfter()
	 * @see #unsetEndAfter()
	 * @see #setEndAfter(Date)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselAvailablility_EndAfter()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getEndAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndAfter <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End After</em>' attribute.
	 * @see #isSetEndAfter()
	 * @see #unsetEndAfter()
	 * @see #getEndAfter()
	 * @generated
	 */
	void setEndAfter(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndAfter <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndAfter()
	 * @see #getEndAfter()
	 * @see #setEndAfter(Date)
	 * @generated
	 */
	void unsetEndAfter();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndAfter <em>End After</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End After</em>' attribute is set.
	 * @see #unsetEndAfter()
	 * @see #getEndAfter()
	 * @see #setEndAfter(Date)
	 * @generated
	 */
	boolean isSetEndAfter();

	/**
	 * Returns the value of the '<em><b>End Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Before</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Before</em>' attribute.
	 * @see #isSetEndBefore()
	 * @see #unsetEndBefore()
	 * @see #setEndBefore(Date)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselAvailablility_EndBefore()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getEndBefore();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndBefore <em>End Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Before</em>' attribute.
	 * @see #isSetEndBefore()
	 * @see #unsetEndBefore()
	 * @see #getEndBefore()
	 * @generated
	 */
	void setEndBefore(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndBefore <em>End Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndBefore()
	 * @see #getEndBefore()
	 * @see #setEndBefore(Date)
	 * @generated
	 */
	void unsetEndBefore();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.VesselAvailablility#getEndBefore <em>End Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Before</em>' attribute is set.
	 * @see #unsetEndBefore()
	 * @see #getEndBefore()
	 * @see #setEndBefore(Date)
	 * @generated
	 */
	boolean isSetEndBefore();

} // end of  VesselAvailablility

// finish type fixing
