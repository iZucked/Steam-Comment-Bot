/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Heel Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.HeelOptions#getVolumeAvailable <em>Volume Available</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.HeelOptions#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.HeelOptions#getPricePerMMBTU <em>Price Per MMBTU</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getHeelOptions()
 * @model
 * @generated
 */
public interface HeelOptions extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Volume Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Available</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Available</em>' attribute.
	 * @see #isSetVolumeAvailable()
	 * @see #unsetVolumeAvailable()
	 * @see #setVolumeAvailable(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getHeelOptions_VolumeAvailable()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0.###'"
	 * @generated
	 */
	double getVolumeAvailable();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.HeelOptions#getVolumeAvailable <em>Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Available</em>' attribute.
	 * @see #isSetVolumeAvailable()
	 * @see #unsetVolumeAvailable()
	 * @see #getVolumeAvailable()
	 * @generated
	 */
	void setVolumeAvailable(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.HeelOptions#getVolumeAvailable <em>Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVolumeAvailable()
	 * @see #getVolumeAvailable()
	 * @see #setVolumeAvailable(double)
	 * @generated
	 */
	void unsetVolumeAvailable();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.HeelOptions#getVolumeAvailable <em>Volume Available</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Volume Available</em>' attribute is set.
	 * @see #unsetVolumeAvailable()
	 * @see #getVolumeAvailable()
	 * @see #setVolumeAvailable(double)
	 * @generated
	 */
	boolean isSetVolumeAvailable();

	/**
	 * Returns the value of the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv Value</em>' attribute.
	 * @see #setCvValue(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getHeelOptions_CvValue()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu/m\263' formatString='#0.######'"
	 * @generated
	 */
	double getCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.HeelOptions#getCvValue <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv Value</em>' attribute.
	 * @see #getCvValue()
	 * @generated
	 */
	void setCvValue(double value);

	/**
	 * Returns the value of the '<em><b>Price Per MMBTU</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Per MMBTU</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Per MMBTU</em>' attribute.
	 * @see #setPricePerMMBTU(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getHeelOptions_PricePerMMBTU()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/mmBtu' formatString='#0.###'"
	 * @generated
	 */
	double getPricePerMMBTU();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.HeelOptions#getPricePerMMBTU <em>Price Per MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Per MMBTU</em>' attribute.
	 * @see #getPricePerMMBTU()
	 * @generated
	 */
	void setPricePerMMBTU(double value);

} // end of  HeelOptions

// finish type fixing
