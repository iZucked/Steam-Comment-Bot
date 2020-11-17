/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory ADP Entity Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow#getInitialAllocation <em>Initial Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow#getRelativeEntitlement <em>Relative Entitlement</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryADPEntityRow()
 * @model
 * @generated
 */
public interface InventoryADPEntityRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryADPEntityRow_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>Initial Allocation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Allocation</em>' attribute.
	 * @see #setInitialAllocation(String)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryADPEntityRow_InitialAllocation()
	 * @model
	 * @generated
	 */
	String getInitialAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow#getInitialAllocation <em>Initial Allocation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Allocation</em>' attribute.
	 * @see #getInitialAllocation()
	 * @generated
	 */
	void setInitialAllocation(String value);

	/**
	 * Returns the value of the '<em><b>Relative Entitlement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relative Entitlement</em>' attribute.
	 * @see #setRelativeEntitlement(double)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryADPEntityRow_RelativeEntitlement()
	 * @model
	 * @generated
	 */
	double getRelativeEntitlement();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow#getRelativeEntitlement <em>Relative Entitlement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Relative Entitlement</em>' attribute.
	 * @see #getRelativeEntitlement()
	 * @generated
	 */
	void setRelativeEntitlement(double value);

} // InventoryADPEntityRow
