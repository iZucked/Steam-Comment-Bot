/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselSet;

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
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getTimeCharterRate <em>Time Charter Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}</li>
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
	 * Returns the value of the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity</em>' attribute.
	 * @see #isSetCapacity()
	 * @see #unsetCapacity()
	 * @see #setCapacity(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Capacity()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m3'"
	 * @generated
	 */
	int getCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
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
	 * @since 3.0
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
	 * @since 3.0
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
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fill Capacity</em>' attribute.
	 * @see #isSetFillCapacity()
	 * @see #unsetFillCapacity()
	 * @see #setFillCapacity(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_FillCapacity()
	 * @model default="1" unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='###.#' unit='%'"
	 * @generated
	 */
	double getFillCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
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
	 * @since 3.0
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
	 * @since 3.0
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
	 * @model markedMany="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (marked.contains(this)) return org.eclipse.emf.common.util.ECollections.emptyEList();\nfinal org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.AVessel> result = new org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.AVessel>();\nmarked.add(this);\nresult.add(this);\nreturn result;'"
	 * @generated
	 */
	EList<AVessel> collect(EList<AVesselSet> marked);

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getVesselOrVesselClassCapacity();

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	double getVesselOrVesselClassFillCapacity();

} // end of  Vessel

// finish type fixing
