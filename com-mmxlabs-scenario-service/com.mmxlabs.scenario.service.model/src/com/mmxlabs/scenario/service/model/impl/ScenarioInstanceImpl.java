/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
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
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isDirty <em>Dirty</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getLocks <em>Locks</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getValidationStatusCode <em>Validation Status Code</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getScenarioVersion <em>Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getVersionContext <em>Version Context</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioInstanceImpl extends ContainerImpl implements ScenarioInstance {
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
	 * The default value of the '{@link #isDirty() <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDirty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DIRTY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDirty() <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDirty()
	 * @generated
	 * @ordered
	 */
	protected boolean dirty = DIRTY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLocks() <em>Locks</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocks()
	 * @generated
	 * @ordered
	 */
	protected EList<ScenarioLock> locks;

	/**
	 * The default value of the '{@link #getValidationStatusCode() <em>Validation Status Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValidationStatusCode()
	 * @generated
	 * @ordered
	 */
	protected static final int VALIDATION_STATUS_CODE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getValidationStatusCode() <em>Validation Status Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValidationStatusCode()
	 * @generated
	 * @ordered
	 */
	protected int validationStatusCode = VALIDATION_STATUS_CODE_EDEFAULT;

	/**
	 * The default value of the '{@link #getScenarioVersion() <em>Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int SCENARIO_VERSION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getScenarioVersion() <em>Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected int scenarioVersion = SCENARIO_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersionContext() <em>Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getVersionContext()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_CONTEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersionContext() <em>Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getVersionContext()
	 * @generated
	 * @ordered
	 */
	protected String versionContext = VERSION_CONTEXT_EDEFAULT;

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
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__UUID, oldUuid, uuid));
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
	public NotificationChain basicSetMetadata(Metadata newMetadata, NotificationChain msgs) {
		Metadata oldMetadata = metadata;
		metadata = newMetadata;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__METADATA, oldMetadata, newMetadata);
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
				msgs = ((InternalEObject) metadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioServicePackage.SCENARIO_INSTANCE__METADATA, null, msgs);
			if (newMetadata != null)
				msgs = ((InternalEObject) newMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioServicePackage.SCENARIO_INSTANCE__METADATA, null, msgs);
			msgs = basicSetMetadata(newMetadata, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__METADATA, newMetadata, newMetadata));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED, oldLocked, locked));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE, oldInstance, instance));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE, oldInstance, instance));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS, oldAdapters, adapters));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getSubModelURIs() {
		if (subModelURIs == null) {
			subModelURIs = new EDataTypeUniqueEList<String>(String.class, this, ScenarioServicePackage.SCENARIO_INSTANCE__SUB_MODEL_UR_IS);
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
			dependencyUUIDs = new EDataTypeUniqueEList<String>(String.class, this, ScenarioServicePackage.SCENARIO_INSTANCE__DEPENDENCY_UUI_DS);
		}
		return dependencyUUIDs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getScenarioVersion() {
		return scenarioVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScenarioVersion(int newScenarioVersion) {
		int oldScenarioVersion = scenarioVersion;
		scenarioVersion = newScenarioVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION, oldScenarioVersion, scenarioVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersionContext() {
		return versionContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersionContext(String newVersionContext) {
		String oldVersionContext = versionContext;
		versionContext = newVersionContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT, oldVersionContext, versionContext));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDirty(boolean newDirty) {
		boolean oldDirty = dirty;
		dirty = newDirty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY, oldDirty, dirty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ScenarioLock> getLocks() {
		if (locks == null) {
			locks = new EObjectContainmentWithInverseEList<ScenarioLock>(ScenarioLock.class, this, ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS, ScenarioServicePackage.SCENARIO_LOCK__INSTANCE);
		}
		return locks;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValidationStatusCode() {
		return validationStatusCode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValidationStatusCode(int newValidationStatusCode) {
		int oldValidationStatusCode = validationStatusCode;
		validationStatusCode = newValidationStatusCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE, oldValidationStatusCode, validationStatusCode));
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
	public ScenarioLock getLock(String key) {
		final List<ScenarioLock> locks = getLocks();
		synchronized (locks) {
			for (final ScenarioLock lock : locks) {
				if (lock.getKey().equals(key)) {
					return lock;
				}
			}

			final ScenarioLock newLock = ScenarioServiceFactory.eINSTANCE.createScenarioLock();
			newLock.setKey(key);
			locks.add(newLock);
			newLock.init();
			return newLock;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getLocks()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return basicSetMetadata(null, msgs);
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			return ((InternalEList<?>) getLocks()).basicRemove(otherEnd, msgs);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
			return isDirty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			return getLocks();
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
			return getValidationStatusCode();
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
			return getScenarioVersion();
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			return getVersionContext();
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
			getDependencyUUIDs().addAll((Collection<? extends String>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
			setDirty((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			getLocks().clear();
			getLocks().addAll((Collection<? extends ScenarioLock>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
			setValidationStatusCode((Integer) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
			setScenarioVersion((Integer) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			setVersionContext((String) newValue);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
			setDirty(DIRTY_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			getLocks().clear();
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
			setValidationStatusCode(VALIDATION_STATUS_CODE_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
			setScenarioVersion(SCENARIO_VERSION_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			setVersionContext(VERSION_CONTEXT_EDEFAULT);
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
			return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
			return dirty != DIRTY_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			return locks != null && !locks.isEmpty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
			return validationStatusCode != VALIDATION_STATUS_CODE_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
			return scenarioVersion != SCENARIO_VERSION_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			return VERSION_CONTEXT_EDEFAULT == null ? versionContext != null : !VERSION_CONTEXT_EDEFAULT.equals(versionContext);
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
		result.append(", dirty: ");
		result.append(dirty);
		result.append(", validationStatusCode: ");
		result.append(validationStatusCode);
		result.append(", scenarioVersion: ");
		result.append(scenarioVersion);
		result.append(", versionContext: ");
		result.append(versionContext);
		result.append(')');
		return result.toString();
	}

} //ScenarioInstanceImpl
