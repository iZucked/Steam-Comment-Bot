/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Emissions Covered Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EmissionsCoveredTable#getStartYear <em>Start Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EmissionsCoveredTable#getEmissionsCovered <em>Emissions Covered</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEmissionsCoveredTable()
 * @model
 * @generated
 */
public interface EmissionsCoveredTable extends EObject {
	/**
	 * Returns the value of the '<em><b>Start Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Year</em>' attribute.
	 * @see #setStartYear(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEmissionsCoveredTable_StartYear()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='####'"
	 * @generated
	 */
	int getStartYear();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EmissionsCoveredTable#getStartYear <em>Start Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Year</em>' attribute.
	 * @see #getStartYear()
	 * @generated
	 */
	void setStartYear(int value);

	/**
	 * Returns the value of the '<em><b>Emissions Covered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Emissions Covered</em>' attribute.
	 * @see #setEmissionsCovered(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEmissionsCoveredTable_EmissionsCovered()
	 * @model
	 * @generated
	 */
	int getEmissionsCovered();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EmissionsCoveredTable#getEmissionsCovered <em>Emissions Covered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Emissions Covered</em>' attribute.
	 * @see #getEmissionsCovered()
	 * @generated
	 */
	void setEmissionsCovered(int value);

} // EmissionsCoveredTable
