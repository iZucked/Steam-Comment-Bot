/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Slot;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Number Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.CargoNumberDistributionModel#getNumberOfCargoes <em>Number Of Cargoes</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoNumberDistributionModel()
 * @model
 * @generated
 */
public interface CargoNumberDistributionModel extends DistributionModel {
	/**
	 * Returns the value of the '<em><b>Number Of Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Cargoes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Cargoes</em>' attribute.
	 * @see #setNumberOfCargoes(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoNumberDistributionModel_NumberOfCargoes()
	 * @model
	 * @generated
	 */
	int getNumberOfCargoes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.CargoNumberDistributionModel#getNumberOfCargoes <em>Number Of Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Cargoes</em>' attribute.
	 * @see #getNumberOfCargoes()
	 * @generated
	 */
	void setNumberOfCargoes(int value);

} // CargoNumberDistributionModel
