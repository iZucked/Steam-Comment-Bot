/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CommercialPackage;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ballast Bonus Contract Line</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.BallastBonusContractLineImpl#getRedeliveryPorts <em>Redelivery Ports</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class BallastBonusContractLineImpl extends EObjectImpl implements BallastBonusContractLine {
	/**
	 * The cached value of the '{@link #getRedeliveryPorts() <em>Redelivery Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRedeliveryPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> redeliveryPorts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BallastBonusContractLineImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.BALLAST_BONUS_CONTRACT_LINE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getRedeliveryPorts() {
		if (redeliveryPorts == null) {
			redeliveryPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.BALLAST_BONUS_CONTRACT_LINE__REDELIVERY_PORTS);
		}
		return redeliveryPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.BALLAST_BONUS_CONTRACT_LINE__REDELIVERY_PORTS:
				return getRedeliveryPorts();
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
			case CommercialPackage.BALLAST_BONUS_CONTRACT_LINE__REDELIVERY_PORTS:
				getRedeliveryPorts().clear();
				getRedeliveryPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
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
			case CommercialPackage.BALLAST_BONUS_CONTRACT_LINE__REDELIVERY_PORTS:
				getRedeliveryPorts().clear();
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
			case CommercialPackage.BALLAST_BONUS_CONTRACT_LINE__REDELIVERY_PORTS:
				return redeliveryPorts != null && !redeliveryPorts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //BallastBonusContractLineImpl
