/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.commercial.SalesContract;

import com.mmxlabs.models.lng.fleet.Vessel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contract Allocation Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractAllocationRow#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractAllocationRow#getWeight <em>Weight</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractAllocationRow#getVessels <em>Vessels</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractAllocationRow()
 * @model
 * @generated
 */
public interface ContractAllocationRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(SalesContract)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractAllocationRow_Contract()
	 * @model
	 * @generated
	 */
	SalesContract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractAllocationRow#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(SalesContract value);

	/**
	 * Returns the value of the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Weight</em>' attribute.
	 * @see #setWeight(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractAllocationRow_Weight()
	 * @model
	 * @generated
	 */
	int getWeight();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractAllocationRow#getWeight <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Weight</em>' attribute.
	 * @see #getWeight()
	 * @generated
	 */
	void setWeight(int value);

	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.Vessel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractAllocationRow_Vessels()
	 * @model
	 * @generated
	 */
	EList<Vessel> getVessels();

} // ContractAllocationRow
