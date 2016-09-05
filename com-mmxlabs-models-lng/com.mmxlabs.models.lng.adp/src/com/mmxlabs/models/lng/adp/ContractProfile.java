/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

import com.mmxlabs.models.lng.types.VolumeUnits;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contract Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractProfile#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractProfile#getContractCode <em>Contract Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractProfile#isCustom <em>Custom</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractProfile#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractProfile#getTotalVolume <em>Total Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractProfile#getVolumeUnit <em>Volume Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractProfile#getDistributionModel <em>Distribution Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ContractProfile#getSubProfiles <em>Sub Profiles</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile()
 * @model
 * @generated
 */
public interface ContractProfile<T extends Slot> extends EObject {
	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(Contract)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile_Contract()
	 * @model
	 * @generated
	 */
	Contract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractProfile#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(Contract value);

	/**
	 * Returns the value of the '<em><b>Contract Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Code</em>' attribute.
	 * @see #setContractCode(String)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile_ContractCode()
	 * @model
	 * @generated
	 */
	String getContractCode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractProfile#getContractCode <em>Contract Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract Code</em>' attribute.
	 * @see #getContractCode()
	 * @generated
	 */
	void setContractCode(String value);

	/**
	 * Returns the value of the '<em><b>Custom</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Custom</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Custom</em>' attribute.
	 * @see #setCustom(boolean)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile_Custom()
	 * @model
	 * @generated
	 */
	boolean isCustom();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractProfile#isCustom <em>Custom</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Custom</em>' attribute.
	 * @see #isCustom()
	 * @generated
	 */
	void setCustom(boolean value);

	/**
	 * Returns the value of the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enabled</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enabled</em>' attribute.
	 * @see #setEnabled(boolean)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile_Enabled()
	 * @model
	 * @generated
	 */
	boolean isEnabled();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractProfile#isEnabled <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enabled</em>' attribute.
	 * @see #isEnabled()
	 * @generated
	 */
	void setEnabled(boolean value);

	/**
	 * Returns the value of the '<em><b>Total Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Total Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Total Volume</em>' attribute.
	 * @see #setTotalVolume(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile_TotalVolume()
	 * @model
	 * @generated
	 */
	int getTotalVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractProfile#getTotalVolume <em>Total Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Total Volume</em>' attribute.
	 * @see #getTotalVolume()
	 * @generated
	 */
	void setTotalVolume(int value);

	/**
	 * Returns the value of the '<em><b>Volume Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.VolumeUnits}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #setVolumeUnit(VolumeUnits)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile_VolumeUnit()
	 * @model
	 * @generated
	 */
	VolumeUnits getVolumeUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractProfile#getVolumeUnit <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #getVolumeUnit()
	 * @generated
	 */
	void setVolumeUnit(VolumeUnits value);

	/**
	 * Returns the value of the '<em><b>Distribution Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distribution Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distribution Model</em>' containment reference.
	 * @see #setDistributionModel(DistributionModel)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile_DistributionModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	DistributionModel getDistributionModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ContractProfile#getDistributionModel <em>Distribution Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distribution Model</em>' containment reference.
	 * @see #getDistributionModel()
	 * @generated
	 */
	void setDistributionModel(DistributionModel value);

	/**
	 * Returns the value of the '<em><b>Sub Profiles</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.SubContractProfile}&lt;T>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Profiles</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Profiles</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getContractProfile_SubProfiles()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<SubContractProfile<T>> getSubProfiles();

} // ContractProfile
