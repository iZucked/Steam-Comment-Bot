/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.port.Canal;
import scenario.port.CanalModel;
import scenario.port.PortPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Canal Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.port.impl.CanalModelImpl#getCanals <em>Canals</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CanalModelImpl extends EObjectImpl implements CanalModel {
	/**
	 * The cached value of the '{@link #getCanals() <em>Canals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanals()
	 * @generated
	 * @ordered
	 */
	protected EList<Canal> canals;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.CANAL_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Canal> getCanals() {
		if (canals == null) {
			canals = new EObjectContainmentEList<Canal>(Canal.class, this, PortPackage.CANAL_MODEL__CANALS);
		}
		return canals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.CANAL_MODEL__CANALS:
				return ((InternalEList<?>)getCanals()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PortPackage.CANAL_MODEL__CANALS:
				return getCanals();
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
			case PortPackage.CANAL_MODEL__CANALS:
				getCanals().clear();
				getCanals().addAll((Collection<? extends Canal>)newValue);
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
			case PortPackage.CANAL_MODEL__CANALS:
				getCanals().clear();
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
			case PortPackage.CANAL_MODEL__CANALS:
				return canals != null && !canals.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CanalModelImpl
