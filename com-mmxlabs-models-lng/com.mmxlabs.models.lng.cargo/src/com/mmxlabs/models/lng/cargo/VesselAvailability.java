/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.mmxcore.UUIDObject;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEntity <em>Entity</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability()
 * @model
 * @generated
 */
public interface VesselAvailability extends UUIDObject {
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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

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
	 * @see #setTimeCharterRate(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_TimeCharterRate()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	String getTimeCharterRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Charter Rate</em>' attribute.
	 * @see #isSetTimeCharterRate()
	 * @see #unsetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @generated
	 */
	void setTimeCharterRate(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @see #setTimeCharterRate(String)
	 * @generated
	 */
	void unsetTimeCharterRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Time Charter Rate</em>' attribute is set.
	 * @see #unsetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @see #setTimeCharterRate(String)
	 * @generated
	 */
	boolean isSetTimeCharterRate();

	/**
	 * Returns the value of the '<em><b>Start At</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start At</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start At</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_StartAt()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getStartAt();

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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_StartAfter()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getStartAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartAfter()
	 * @see #getStartAfter()
	 * @see #setStartAfter(Date)
	 * @generated
	 */
	void unsetStartAfter();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start By</em>' attribute.
	 * @see #isSetStartBy()
	 * @see #unsetStartBy()
	 * @see #setStartBy(Date)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_StartBy()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getStartBy();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start By</em>' attribute.
	 * @see #isSetStartBy()
	 * @see #unsetStartBy()
	 * @see #getStartBy()
	 * @generated
	 */
	void setStartBy(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartBy()
	 * @see #getStartBy()
	 * @see #setStartBy(Date)
	 * @generated
	 */
	void unsetStartBy();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start By</em>' attribute is set.
	 * @see #unsetStartBy()
	 * @see #getStartBy()
	 * @see #setStartBy(Date)
	 * @generated
	 */
	boolean isSetStartBy();

	/**
	 * Returns the value of the '<em><b>End At</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End At</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End At</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_EndAt()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getEndAt();

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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_EndAfter()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getEndAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndAfter()
	 * @see #getEndAfter()
	 * @see #setEndAfter(Date)
	 * @generated
	 */
	void unsetEndAfter();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>End By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End By</em>' attribute.
	 * @see #isSetEndBy()
	 * @see #unsetEndBy()
	 * @see #setEndBy(Date)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_EndBy()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getEndBy();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End By</em>' attribute.
	 * @see #isSetEndBy()
	 * @see #unsetEndBy()
	 * @see #getEndBy()
	 * @generated
	 */
	void setEndBy(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndBy()
	 * @see #getEndBy()
	 * @see #setEndBy(Date)
	 * @generated
	 */
	void unsetEndBy();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End By</em>' attribute is set.
	 * @see #unsetEndBy()
	 * @see #getEndBy()
	 * @see #setEndBy(Date)
	 * @generated
	 */
	boolean isSetEndBy();

	/**
	 * Returns the value of the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Heel</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Heel</em>' containment reference.
	 * @see #setStartHeel(HeelOptions)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_StartHeel()
	 * @model containment="true" resolveProxies="true" required="true"
	 * @generated
	 */
	HeelOptions getStartHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartHeel <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Heel</em>' containment reference.
	 * @see #getStartHeel()
	 * @generated
	 */
	void setStartHeel(HeelOptions value);

	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

} // VesselAvailability
