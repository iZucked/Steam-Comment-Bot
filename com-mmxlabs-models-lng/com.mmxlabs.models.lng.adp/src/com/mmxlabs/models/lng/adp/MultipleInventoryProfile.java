/**
 */
package com.mmxlabs.models.lng.adp;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multiple Inventory Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.MultipleInventoryProfile#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MultipleInventoryProfile#getVolumeFlex <em>Volume Flex</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MultipleInventoryProfile#getInventories <em>Inventories</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MultipleInventoryProfile#getFullCargoLotValue <em>Full Cargo Lot Value</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMultipleInventoryProfile()
 * @model
 * @generated
 */
public interface MultipleInventoryProfile extends EObject {
	/**
	 * Returns the value of the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Size</em>' attribute.
	 * @see #setWindowSize(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMultipleInventoryProfile_WindowSize()
	 * @model
	 * @generated
	 */
	int getWindowSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MultipleInventoryProfile#getWindowSize <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Size</em>' attribute.
	 * @see #getWindowSize()
	 * @generated
	 */
	void setWindowSize(int value);

	/**
	 * Returns the value of the '<em><b>Volume Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Flex</em>' attribute.
	 * @see #setVolumeFlex(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMultipleInventoryProfile_VolumeFlex()
	 * @model
	 * @generated
	 */
	int getVolumeFlex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MultipleInventoryProfile#getVolumeFlex <em>Volume Flex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Flex</em>' attribute.
	 * @see #getVolumeFlex()
	 * @generated
	 */
	void setVolumeFlex(int value);

	/**
	 * Returns the value of the '<em><b>Inventories</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.InventorySubprofile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inventories</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMultipleInventoryProfile_Inventories()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<InventorySubprofile> getInventories();

	/**
	 * Returns the value of the '<em><b>Full Cargo Lot Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Full Cargo Lot Value</em>' attribute.
	 * @see #setFullCargoLotValue(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMultipleInventoryProfile_FullCargoLotValue()
	 * @model
	 * @generated
	 */
	int getFullCargoLotValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MultipleInventoryProfile#getFullCargoLotValue <em>Full Cargo Lot Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Full Cargo Lot Value</em>' attribute.
	 * @see #getFullCargoLotValue()
	 * @generated
	 */
	void setFullCargoLotValue(int value);

} // MultipleInventoryProfile
