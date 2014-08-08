/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ITimezoneProvider;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselEvent#getDurationInDays <em>Duration In Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselEvent#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselEvent#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselEvent#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselEvent#getStartBy <em>Start By</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEvent()
 * @model abstract="true"
 * @generated
 */
public interface VesselEvent extends UUIDObject, NamedObject, ITimezoneProvider, AssignableElement {
	/**
	 * Returns the value of the '<em><b>Duration In Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration In Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration In Days</em>' attribute.
	 * @see #setDurationInDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEvent_DurationInDays()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getDurationInDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getDurationInDays <em>Duration In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration In Days</em>' attribute.
	 * @see #getDurationInDays()
	 * @generated
	 */
	void setDurationInDays(int value);

	/**
	 * Returns the value of the '<em><b>Allowed Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}&lt;com.mmxlabs.models.lng.fleet.Vessel>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEvent_AllowedVessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet<Vessel>> getAllowedVessels();

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEvent_Port()
	 * @model required="true"
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start After</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start After</em>' attribute.
	 * @see #setStartAfter(Date)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEvent_StartAfter()
	 * @model required="true"
	 * @generated
	 */
	Date getStartAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getStartAfter <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start After</em>' attribute.
	 * @see #getStartAfter()
	 * @generated
	 */
	void setStartAfter(Date value);

	/**
	 * Returns the value of the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start By</em>' attribute.
	 * @see #setStartBy(Date)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEvent_StartBy()
	 * @model required="true"
	 * @generated
	 */
	Date getStartBy();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getStartBy <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start By</em>' attribute.
	 * @see #getStartBy()
	 * @generated
	 */
	void setStartBy(Date value);

} // VesselEvent
