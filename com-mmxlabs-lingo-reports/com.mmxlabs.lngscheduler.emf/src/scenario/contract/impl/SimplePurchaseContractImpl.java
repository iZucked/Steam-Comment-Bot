/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import scenario.contract.ContractPackage;
import scenario.contract.SimplePurchaseContract;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.SimplePurchaseContractImpl#getCooldownPorts <em>Cooldown Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class SimplePurchaseContractImpl extends PurchaseContractImpl implements SimplePurchaseContract {
	/**
	 * The cached value of the '{@link #getCooldownPorts() <em>Cooldown Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooldownPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> cooldownPorts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimplePurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.SIMPLE_PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Port> getCooldownPorts() {
		if (cooldownPorts == null) {
			cooldownPorts = new EObjectResolvingEList<Port>(Port.class, this, ContractPackage.SIMPLE_PURCHASE_CONTRACT__COOLDOWN_PORTS);
		}
		return cooldownPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.SIMPLE_PURCHASE_CONTRACT__COOLDOWN_PORTS:
				return getCooldownPorts();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ContractPackage.SIMPLE_PURCHASE_CONTRACT__COOLDOWN_PORTS:
				getCooldownPorts().clear();
				getCooldownPorts().addAll((Collection<? extends Port>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ContractPackage.SIMPLE_PURCHASE_CONTRACT__COOLDOWN_PORTS:
				getCooldownPorts().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ContractPackage.SIMPLE_PURCHASE_CONTRACT__COOLDOWN_PORTS:
				return cooldownPorts != null && !cooldownPorts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SimplePurchaseContractImpl
