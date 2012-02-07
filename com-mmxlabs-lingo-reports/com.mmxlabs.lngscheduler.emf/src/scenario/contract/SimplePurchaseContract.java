/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract;

import org.eclipse.emf.common.util.EList;

import scenario.port.PortSelection;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Simple Purchase Contract</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.contract.SimplePurchaseContract#getCooldownPorts <em>Cooldown Ports</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.contract.ContractPackage#getSimplePurchaseContract()
 * @model abstract="true"
 * @generated
 */
public interface SimplePurchaseContract extends PurchaseContract {
	/**
	 * Returns the value of the '<em><b>Cooldown Ports</b></em>' reference list. The list contents are of type {@link scenario.port.PortSelection}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooldown Ports</em>' reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Cooldown Ports</em>' reference list.
	 * @see scenario.contract.ContractPackage#getSimplePurchaseContract_CooldownPorts()
	 * @model
	 * @generated
	 */
	EList<PortSelection> getCooldownPorts();

} // SimplePurchaseContract
