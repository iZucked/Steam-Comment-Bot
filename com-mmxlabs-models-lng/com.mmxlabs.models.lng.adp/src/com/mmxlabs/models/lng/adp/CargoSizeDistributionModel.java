/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Slot;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Size Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel#getCargoSize <em>Cargo Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel#isExact <em>Exact</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoSizeDistributionModel()
 * @model
 * @generated
 */
public interface CargoSizeDistributionModel extends DistributionModel {
	/**
	 * Returns the value of the '<em><b>Cargo Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Size</em>' attribute.
	 * @see #setCargoSize(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoSizeDistributionModel_CargoSize()
	 * @model
	 * @generated
	 */
	int getCargoSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel#getCargoSize <em>Cargo Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Size</em>' attribute.
	 * @see #getCargoSize()
	 * @generated
	 */
	void setCargoSize(int value);

	/**
	 * Returns the value of the '<em><b>Exact</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exact</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exact</em>' attribute.
	 * @see #setExact(boolean)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoSizeDistributionModel_Exact()
	 * @model
	 * @generated
	 */
	boolean isExact();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel#isExact <em>Exact</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exact</em>' attribute.
	 * @see #isExact()
	 * @generated
	 */
	void setExact(boolean value);

} // CargoSizeDistributionModel
