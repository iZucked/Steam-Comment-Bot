/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import scenario.contract.Contract;
import scenario.contract.ContractPackage;
import scenario.contract.Entity;

import scenario.impl.NamedObjectImpl;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.ContractImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link scenario.contract.impl.ContractImpl#getDefaultPorts <em>Default Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ContractImpl extends NamedObjectImpl implements Contract {
	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected Entity entity;

	/**
	 * The cached value of the '{@link #getDefaultPorts() <em>Default Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> defaultPorts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (Entity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ContractPackage.CONTRACT__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntity(Entity newEntity) {
		Entity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.CONTRACT__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Port> getDefaultPorts() {
		if (defaultPorts == null) {
			defaultPorts = new EObjectResolvingEList<Port>(Port.class, this, ContractPackage.CONTRACT__DEFAULT_PORTS);
		}
		return defaultPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.CONTRACT__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case ContractPackage.CONTRACT__DEFAULT_PORTS:
				return getDefaultPorts();
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
			case ContractPackage.CONTRACT__ENTITY:
				setEntity((Entity)newValue);
				return;
			case ContractPackage.CONTRACT__DEFAULT_PORTS:
				getDefaultPorts().clear();
				getDefaultPorts().addAll((Collection<? extends Port>)newValue);
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
			case ContractPackage.CONTRACT__ENTITY:
				setEntity((Entity)null);
				return;
			case ContractPackage.CONTRACT__DEFAULT_PORTS:
				getDefaultPorts().clear();
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
			case ContractPackage.CONTRACT__ENTITY:
				return entity != null;
			case ContractPackage.CONTRACT__DEFAULT_PORTS:
				return defaultPorts != null && !defaultPorts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ContractImpl
