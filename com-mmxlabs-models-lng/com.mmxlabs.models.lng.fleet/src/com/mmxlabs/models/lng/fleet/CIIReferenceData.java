/**
 */
package com.mmxlabs.models.lng.fleet;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CII Reference Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIReferenceData#getCiiGradeBoundaries <em>Cii Grade Boundaries</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIReferenceData#getFuelEmissions <em>Fuel Emissions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CIIReferenceData#getReductionFactors <em>Reduction Factors</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIReferenceData()
 * @model
 * @generated
 */
public interface CIIReferenceData extends EObject {
	/**
	 * Returns the value of the '<em><b>Cii Grade Boundaries</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.CIIGradeBoundary}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cii Grade Boundaries</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIReferenceData_CiiGradeBoundaries()
	 * @model containment="true"
	 * @generated
	 */
	EList<CIIGradeBoundary> getCiiGradeBoundaries();

	/**
	 * Returns the value of the '<em><b>Fuel Emissions</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.FuelEmissionReference}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Emissions</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIReferenceData_FuelEmissions()
	 * @model containment="true"
	 * @generated
	 */
	EList<FuelEmissionReference> getFuelEmissions();

	/**
	 * Returns the value of the '<em><b>Reduction Factors</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.CIIReductionFactor}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reduction Factors</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCIIReferenceData_ReductionFactors()
	 * @model containment="true"
	 * @generated
	 */
	EList<CIIReductionFactor> getReductionFactors();

} // CIIReferenceData
