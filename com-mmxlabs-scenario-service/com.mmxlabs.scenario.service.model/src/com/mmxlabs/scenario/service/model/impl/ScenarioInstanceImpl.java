/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getInstance <em>Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getAdapters <em>Adapters</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getSubModelURIs <em>Sub Model UR Is</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getDependencyUUIDs <em>Dependency UUI Ds</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioInstanceImpl extends ContainerImpl implements
		ScenarioInstance {
	/**
	 * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected static final String UUID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected String uuid = UUID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
	protected Metadata metadata;

	/**
	 * The default value of the '{@link #isLocked() <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLocked()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LOCKED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLocked() <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLocked()
	 * @generated
	 * @ordered
	 */
	protected boolean locked = LOCKED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInstance() <em>Instance</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstance()
	 * @generated
	 * @ordered
	 */
	protected EObject instance;

	/**
	 * The cached value of the '{@link #getAdapters() <em>Adapters</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdapters()
	 * @generated
	 * @ordered
	 */
	protected Map<Class<?>, Object> adapters;

	/**
	 * The cached value of the '{@link #getSubModelURIs() <em>Sub Model UR Is</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubModelURIs()
	 * @generated
	 * @ordered
	 */
	protected EList<String> subModelURIs;

	/**
	 * The cached value of the '{@link #getDependencyUUIDs() <em>Dependency UUI Ds</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencyUUIDs()
	 * @generated
	 * @ordered
	 */
	protected EList<String> dependencyUUIDs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.Literals.SCENARIO_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUuid(String newUuid) {
		String oldUuid = uuid;
		uuid = newUuid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ScenarioServicePackage.SCENARIO_INSTANCE__UUID, oldUuid,
					uuid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetadata(Metadata newMetadata,
			NotificationChain msgs) {
		Metadata oldMetadata = metadata;
		metadata = newMetadata;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this,
					Notification.SET,
					ScenarioServicePackage.SCENARIO_INSTANCE__METADATA,
					oldMetadata, newMetadata);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMetadata(Metadata newMetadata) {
		if (newMetadata != metadata) {
			NotificationChain msgs = null;
			if (metadata != null)
				msgs = ((InternalEObject) metadata)
						.eInverseRemove(
								this,
								EOPPOSITE_FEATURE_BASE
										- ScenarioServicePackage.SCENARIO_INSTANCE__METADATA,
								null, msgs);
			if (newMetadata != null)
				msgs = ((InternalEObject) newMetadata)
						.eInverseAdd(
								this,
								EOPPOSITE_FEATURE_BASE
										- ScenarioServicePackage.SCENARIO_INSTANCE__METADATA,
								null, msgs);
			msgs = basicSetMetadata(newMetadata, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ScenarioServicePackage.SCENARIO_INSTANCE__METADATA,
					newMetadata, newMetadata));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocked(boolean newLocked) {
		boolean oldLocked = locked;
		locked = newLocked;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED,
					oldLocked, locked));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getInstance() {
		if (instance != null && instance.eIsProxy()) {
			InternalEObject oldInstance = (InternalEObject) instance;
			instance = eResolveProxy(oldInstance);
			if (instance != oldInstance) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE,
							oldInstance, instance));
			}
		}
		return instance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetInstance() {
		return instance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInstance(EObject newInstance) {
		EObject oldInstance = instance;
		instance = newInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE,
					oldInstance, instance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map<Class<?>, Object> getAdapters() {
		return adapters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAdapters(Map<Class<?>, Object> newAdapters) {
		Map<Class<?>, Object> oldAdapters = adapters;
		adapters = newAdapters;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS,
					oldAdapters, adapters));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getSubModelURIs() {
		if (subModelURIs == null) {
			subModelURIs = new EDataTypeUniqueEList<String>(String.class, this,
					ScenarioServicePackage.SCENARIO_INSTANCE__SUB_MODEL_UR_IS);
		}
		return subModelURIs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getDependencyUUIDs() {
		if (dependencyUUIDs == null) {
			dependencyUUIDs = new EDataTypeUniqueEList<String>(String.class,
					this,
					ScenarioServicePackage.SCENARIO_INSTANCE__DEPENDENCY_UUI_DS);
		}
		return dependencyUUIDs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getContainedInstanceCount() {
		return super.getContainedInstanceCount() + 1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return basicSetMetadata(null, msgs);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
			return getUuid();
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return getMetadata();
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			return isLocked();
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			if (resolve)
				return getInstance();
			return basicGetInstance();
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			return getAdapters();
		case ScenarioServicePackage.SCENARIO_INSTANCE__SUB_MODEL_UR_IS:
			return getSubModelURIs();
		case ScenarioServicePackage.SCENARIO_INSTANCE__DEPENDENCY_UUI_DS:
			return getDependencyUUIDs();
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
			setUuid((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			setMetadata((Metadata) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			setLocked((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			setInstance((EObject) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			setAdapters((Map<Class<?>, Object>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SUB_MODEL_UR_IS:
			getSubModelURIs().clear();
			getSubModelURIs().addAll((Collection<? extends String>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__DEPENDENCY_UUI_DS:
			getDependencyUUIDs().clear();
			getDependencyUUIDs()
					.addAll((Collection<? extends String>) newValue);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
			setUuid(UUID_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			setMetadata((Metadata) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			setLocked(LOCKED_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			setInstance((EObject) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			setAdapters((Map<Class<?>, Object>) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SUB_MODEL_UR_IS:
			getSubModelURIs().clear();
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__DEPENDENCY_UUI_DS:
			getDependencyUUIDs().clear();
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
			return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT
					.equals(uuid);
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return metadata != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			return locked != LOCKED_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			return instance != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			return adapters != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SUB_MODEL_UR_IS:
			return subModelURIs != null && !subModelURIs.isEmpty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__DEPENDENCY_UUI_DS:
			return dependencyUUIDs != null && !dependencyUUIDs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (uuid: ");
		result.append(uuid);
		result.append(", locked: ");
		result.append(locked);
		result.append(", adapters: ");
		result.append(adapters);
		result.append(", subModelURIs: ");
		result.append(subModelURIs);
		result.append(", dependencyUUIDs: ");
		result.append(dependencyUUIDs);
		result.append(')');
		return result.toString();
	}

} //ScenarioInstanceImpl
