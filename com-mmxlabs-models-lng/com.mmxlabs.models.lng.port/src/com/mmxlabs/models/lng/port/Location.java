/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Location</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.Location#getCountry <em>Country</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Location#getLat <em>Lat</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Location#getLon <em>Lon</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getLocation()
 * @model
 * @generated
 */
public interface Location extends EObject {
	/**
	 * Returns the value of the '<em><b>Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Country</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Country</em>' attribute.
	 * @see #setCountry(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getLocation_Country()
	 * @model
	 * @generated
	 */
	String getCountry();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Location#getCountry <em>Country</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Country</em>' attribute.
	 * @see #getCountry()
	 * @generated
	 */
	void setCountry(String value);

	/**
	 * Returns the value of the '<em><b>Lat</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lat</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lat</em>' attribute.
	 * @see #setLat(double)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getLocation_Lat()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='-##0.###' exportFormatString='##0.###'"
	 * @generated
	 */
	double getLat();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Location#getLat <em>Lat</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lat</em>' attribute.
	 * @see #getLat()
	 * @generated
	 */
	void setLat(double value);

	/**
	 * Returns the value of the '<em><b>Lon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lon</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lon</em>' attribute.
	 * @see #setLon(double)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getLocation_Lon()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='-##0.###' exportFormatString='##0.###'"
	 * @generated
	 */
	double getLon();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Location#getLon <em>Lon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lon</em>' attribute.
	 * @see #getLon()
	 * @generated
	 */
	void setLon(double value);

} // end of  Location

// finish type fixing
