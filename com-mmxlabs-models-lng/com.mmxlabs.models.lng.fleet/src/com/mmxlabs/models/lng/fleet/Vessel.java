/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getShortName <em>Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel()
 * @model
 * @generated
 */
public interface Vessel extends AVesselSet<Vessel> {
	/**
	 * Returns the value of the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Short Name</em>' attribute.
	 * @see #setShortName(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_ShortName()
	 * @model
	 * @generated
	 */
	String getShortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getShortName <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Short Name</em>' attribute.
	 * @see #getShortName()
	 * @generated
	 */
	void setShortName(String value);

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
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
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
	EList<APortSet<Port>> getInaccessiblePorts();

	/**
	 * Returns the value of the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity</em>' attribute.
	 * @see #isSetCapacity()
	 * @see #unsetCapacity()
	 * @see #setCapacity(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Capacity()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0'"
	 * @generated
	 */
	int getCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity</em>' attribute.
	 * @see #isSetCapacity()
	 * @see #unsetCapacity()
	 * @see #getCapacity()
	 * @generated
	 */
	void setCapacity(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCapacity()
	 * @see #getCapacity()
	 * @see #setCapacity(int)
	 * @generated
	 */
	void unsetCapacity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Capacity</em>' attribute is set.
	 * @see #unsetCapacity()
	 * @see #getCapacity()
	 * @see #setCapacity(int)
	 * @generated
	 */
	boolean isSetCapacity();

	/**
	 * Returns the value of the '<em><b>Fill Capacity</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fill Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fill Capacity</em>' attribute.
	 * @see #isSetFillCapacity()
	 * @see #unsetFillCapacity()
	 * @see #setFillCapacity(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_FillCapacity()
	 * @model default="1" unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='###.#' unit='%' exportFormatString='#.###'"
	 * @generated
	 */
	double getFillCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fill Capacity</em>' attribute.
	 * @see #isSetFillCapacity()
	 * @see #unsetFillCapacity()
	 * @see #getFillCapacity()
	 * @generated
	 */
	void setFillCapacity(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFillCapacity()
	 * @see #getFillCapacity()
	 * @see #setFillCapacity(double)
	 * @generated
	 */
	void unsetFillCapacity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fill Capacity</em>' attribute is set.
	 * @see #unsetFillCapacity()
	 * @see #getFillCapacity()
	 * @see #setFillCapacity(double)
	 * @generated
	 */
	boolean isSetFillCapacity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getVesselOrVesselClassCapacity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	double getVesselOrVesselClassFillCapacity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getShortenedName();

} // end of  Vessel

// finish type fixing
