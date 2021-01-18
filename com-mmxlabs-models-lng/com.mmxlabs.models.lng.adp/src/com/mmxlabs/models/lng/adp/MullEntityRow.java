/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import com.mmxlabs.models.lng.port.Port;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mull Entity Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.MullEntityRow#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MullEntityRow#getInitialAllocation <em>Initial Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MullEntityRow#getRelativeEntitlement <em>Relative Entitlement</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MullEntityRow#getDesSalesMarketAllocationRows <em>Des Sales Market Allocation Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MullEntityRow#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MullEntityRow#getSalesContractAllocationRows <em>Sales Contract Allocation Rows</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullEntityRow()
 * @model
 * @generated
 */
public interface MullEntityRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullEntityRow_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MullEntityRow#getEntity <em>Entity</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullEntityRow_InitialAllocation()
	 * @model
	 * @generated
	 */
	String getInitialAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MullEntityRow#getInitialAllocation <em>Initial Allocation</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullEntityRow_RelativeEntitlement()
	 * @model
	 * @generated
	 */
	double getRelativeEntitlement();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MullEntityRow#getRelativeEntitlement <em>Relative Entitlement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Relative Entitlement</em>' attribute.
	 * @see #getRelativeEntitlement()
	 * @generated
	 */
	void setRelativeEntitlement(double value);

	/**
	 * Returns the value of the '<em><b>Des Sales Market Allocation Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Sales Market Allocation Rows</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullEntityRow_DesSalesMarketAllocationRows()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<DESSalesMarketAllocationRow> getDesSalesMarketAllocationRows();

	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.Port}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullEntityRow_Ports()
	 * @model
	 * @generated
	 */
	EList<Port> getPorts();

	/**
	 * Returns the value of the '<em><b>Sales Contract Allocation Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.SalesContractAllocationRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sales Contract Allocation Rows</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMullEntityRow_SalesContractAllocationRows()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<SalesContractAllocationRow> getSalesContractAllocationRows();

} // MullEntityRow
