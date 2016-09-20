/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getRootObjectURI <em>Root Object URI</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getScenarioVersion <em>Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getVersionContext <em>Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getClientScenarioVersion <em>Client Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getClientVersionContext <em>Client Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getInstance <em>Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getFragments <em>Fragments</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getModelReferences <em>Model References</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getAdapters <em>Adapters</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getLocks <em>Locks</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isReadonly <em>Readonly</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isDirty <em>Dirty</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getValidationStatusCode <em>Validation Status Code</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isLoadFailure <em>Load Failure</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getLoadException <em>Load Exception</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioInstanceImpl extends ContainerImpl implements ScenarioInstance {

	/**
	 * @generated NOT
	 */
	private static final Logger log = LoggerFactory.getLogger(ScenarioInstanceImpl.class);

	/**
	 * Object used as a lock when performing IO operations {@link #load()}, {@link #save()} and {@link #unload()};
	 * @generated NOT
	 */
	private Object ioLock = new Object();

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
	 * The default value of the '{@link #getRootObjectURI() <em>Root Object URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootObjectURI()
	 * @generated
	 * @ordered
	 */
	protected static final String ROOT_OBJECT_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRootObjectURI() <em>Root Object URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootObjectURI()
	 * @generated
	 * @ordered
	 */
	protected String rootObjectURI = ROOT_OBJECT_URI_EDEFAULT;

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
	 * The default value of the '{@link #getScenarioVersion() <em>Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int SCENARIO_VERSION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getScenarioVersion() <em>Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected int scenarioVersion = SCENARIO_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersionContext() <em>Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersionContext()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_CONTEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersionContext() <em>Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersionContext()
	 * @generated
	 * @ordered
	 */
	protected String versionContext = VERSION_CONTEXT_EDEFAULT;

	/**
	 * The default value of the '{@link #getClientScenarioVersion() <em>Client Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int CLIENT_SCENARIO_VERSION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getClientScenarioVersion() <em>Client Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected int clientScenarioVersion = CLIENT_SCENARIO_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getClientVersionContext() <em>Client Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientVersionContext()
	 * @generated
	 * @ordered
	 */
	protected static final String CLIENT_VERSION_CONTEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getClientVersionContext() <em>Client Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientVersionContext()
	 * @generated
	 * @ordered
	 */
	protected String clientVersionContext = CLIENT_VERSION_CONTEXT_EDEFAULT;

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
	 * The cached value of the '{@link #getFragments() <em>Fragments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getFragments()
	 * @generated
	 * @ordered
	 */
	protected EList<ScenarioFragment> fragments;

	/**
	 * The cached value of the '{@link #getModelReferences() <em>Model References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelReference> modelReferences;

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
	 * The cached value of the '{@link #getLocks() <em>Locks</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocks()
	 * @generated
	 * @ordered
	 */
	protected EList<ScenarioLock> locks;

	/**
	 * The default value of the '{@link #isReadonly() <em>Readonly</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @see #isReadonly()
	 * @generated
	 * @ordered
	 */
	protected static final boolean READONLY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isReadonly() <em>Readonly</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @see #isReadonly()
	 * @generated
	 * @ordered
	 */
	protected boolean readonly = READONLY_EDEFAULT;

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
	 * The default value of the '{@link #isLoadFailure() <em>Load Failure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLoadFailure()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LOAD_FAILURE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLoadFailure() <em>Load Failure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLoadFailure()
	 * @generated
	 * @ordered
	 */
	protected boolean loadFailure = LOAD_FAILURE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLoadException() <em>Load Exception</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadException()
	 * @generated
	 * @ordered
	 */
	protected static final Exception LOAD_EXCEPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLoadException() <em>Load Exception</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadException()
	 * @generated
	 * @ordered
	 */
	protected Exception loadException = LOAD_EXCEPTION_EDEFAULT;

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
		return ScenarioServicePackage.eINSTANCE.getScenarioInstance();
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
	public String getRootObjectURI() {
		return rootObjectURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRootObjectURI(String newRootObjectURI) {
		String oldRootObjectURI = rootObjectURI;
		rootObjectURI = newRootObjectURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__ROOT_OBJECT_URI, oldRootObjectURI, rootObjectURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getScenarioVersion() {
		return scenarioVersion;
	}

	/**
	 * <!-- begin-user-doc -->
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersionContext() {
		return versionContext;
	}

	/**
	 * <!-- begin-user-doc -->
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
	public EList<ScenarioFragment> getFragments() {
		if (fragments == null) {
			fragments = new EObjectContainmentWithInverseEList<ScenarioFragment>(ScenarioFragment.class, this, ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS,
					ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE);
		}
		return fragments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelReference> getModelReferences() {
		if (modelReferences == null) {
			modelReferences = new EObjectContainmentWithInverseEList<ModelReference>(ModelReference.class, this, ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES,
					ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE);
		}
		return modelReferences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReadonly(boolean newReadonly) {
		boolean oldReadonly = readonly;
		readonly = newReadonly;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__READONLY, oldReadonly, readonly));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getClientScenarioVersion() {
		return clientScenarioVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setClientScenarioVersion(int newClientScenarioVersion) {
		int oldClientScenarioVersion = clientScenarioVersion;
		clientScenarioVersion = newClientScenarioVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_SCENARIO_VERSION, oldClientScenarioVersion, clientScenarioVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getClientVersionContext() {
		return clientVersionContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setClientVersionContext(String newClientVersionContext) {
		String oldClientVersionContext = clientVersionContext;
		clientVersionContext = newClientVersionContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_VERSION_CONTEXT, oldClientVersionContext, clientVersionContext));
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
	public boolean isLoadFailure() {
		return loadFailure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadFailure(boolean newLoadFailure) {
		boolean oldLoadFailure = loadFailure;
		loadFailure = newLoadFailure;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_FAILURE, oldLoadFailure, loadFailure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Exception getLoadException() {
		return loadException;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadException(Exception newLoadException) {
		Exception oldLoadException = loadException;
		loadException = newLoadException;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_EXCEPTION, oldLoadException, loadException));
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
	public ScenarioLock getLock(final String key) {
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
	 * @generated NOT
	 */
	public synchronized ModelReference getReference(final String referenceID) {

		final ModelReference modelReference = ScenarioServiceFactory.eINSTANCE.createModelReference();
		synchronized (this.getModelReferences()) {
			// Concurrent calls to ModelReference#setScenarioInstance can lead to issues in the references list of the scenario instance, thus we "lock" the scenario instance reference list.
			modelReference.setScenarioInstance(this);
			modelReference.setReferenceId(referenceID);
		}

		return modelReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Load (or return existing) model instance. This should not be called directly, but rather as a result of a call to {@link ModelReference#getInstance()}.
	 * Requires the {@link ScenarioInstance} to be hooked into a {@link ScenarioService}.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EObject load() throws IOException {
		final IScenarioService scenarioService = getScenarioService();
		if (scenarioService == null) {
			throw new RuntimeException("Request #load() on a ScenarioInstance with no ScenarioService");
		}
		// Do not attempt to reload if a previous attempt failed
		if (isLoadFailure()) {
			return null;
		}
		synchronized (ioLock) {
			// Already unloaded?
			if (getInstance() != null) {
				return getInstance();
			}
			try {
				return scenarioService.load(this);
			} catch (final Exception e) {
				// Error on loading, record failure and do not attempt to reload.
				setLoadFailure(true);
				setLoadException(e);
				log.error("Failed to load scenario " + getName() + ". Reload will not be attempted again until the application is relaunched.");

				// Re-throw exception
				throw e;
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * Unload the model instance. This should not be called directly, but rather as a result of all {@link ModelReference}s being released.
	 * Requires the {@link ScenarioInstance} to be hooked into a {@link ScenarioService}.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void unload() {

		final IScenarioService scenarioService = getScenarioService();
		if (scenarioService == null) {
			if (instance != null) {
				log.error("Request #unload() on a ScenarioInstance with no ScenarioService");
			}
			// setInstance(null);
			return;
		}
		synchronized (ioLock) {
			// Already unloaded?
			if (getInstance() == null) {
				return;
			}

			// Final checks - if still no references here, trigger unload
			List<ModelReference> refs = getModelReferences();
			synchronized (refs) {
				if (refs.isEmpty()) {
					scenarioService.unload(this);
				}
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 *  Save the model instance if loaded.
	 * Requires the {@link ScenarioInstance} to be hooked into a {@link ScenarioService}.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void save() throws IOException {
		final IScenarioService scenarioService = getScenarioService();
		if (scenarioService == null) {
			throw new RuntimeException("Request #save() on a ScenarioInstance with no ScenarioService");
		}
		synchronized (ioLock) {
			// Already unloaded?
			if (getInstance() == null) {
				return;
			}

			scenarioService.save(this);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getFragments()).basicAdd(otherEnd, msgs);
		case ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getModelReferences()).basicAdd(otherEnd, msgs);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS:
			return ((InternalEList<?>) getFragments()).basicRemove(otherEnd, msgs);
		case ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES:
			return ((InternalEList<?>) getModelReferences()).basicRemove(otherEnd, msgs);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__ROOT_OBJECT_URI:
			return getRootObjectURI();
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return getMetadata();
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
			return getScenarioVersion();
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			return getVersionContext();
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_SCENARIO_VERSION:
			return getClientScenarioVersion();
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_VERSION_CONTEXT:
			return getClientVersionContext();
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			if (resolve)
				return getInstance();
			return basicGetInstance();
		case ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS:
			return getFragments();
		case ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES:
			return getModelReferences();
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			return getAdapters();
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			return isLocked();
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			return getLocks();
		case ScenarioServicePackage.SCENARIO_INSTANCE__READONLY:
			return isReadonly();
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
			return isDirty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
			return getValidationStatusCode();
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_FAILURE:
			return isLoadFailure();
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_EXCEPTION:
			return getLoadException();
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__ROOT_OBJECT_URI:
			setRootObjectURI((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			setMetadata((Metadata) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
			setScenarioVersion((Integer) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			setVersionContext((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_SCENARIO_VERSION:
			setClientScenarioVersion((Integer) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_VERSION_CONTEXT:
			setClientVersionContext((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			setInstance((EObject) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS:
			getFragments().clear();
			getFragments().addAll((Collection<? extends ScenarioFragment>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES:
			getModelReferences().clear();
			getModelReferences().addAll((Collection<? extends ModelReference>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			setAdapters((Map<Class<?>, Object>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			setLocked((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			getLocks().clear();
			getLocks().addAll((Collection<? extends ScenarioLock>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__READONLY:
			setReadonly((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
			setDirty((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
			setValidationStatusCode((Integer) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_FAILURE:
			setLoadFailure((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_EXCEPTION:
			setLoadException((Exception) newValue);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__ROOT_OBJECT_URI:
			setRootObjectURI(ROOT_OBJECT_URI_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			setMetadata((Metadata) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
			setScenarioVersion(SCENARIO_VERSION_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			setVersionContext(VERSION_CONTEXT_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_SCENARIO_VERSION:
			setClientScenarioVersion(CLIENT_SCENARIO_VERSION_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_VERSION_CONTEXT:
			setClientVersionContext(CLIENT_VERSION_CONTEXT_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			setInstance((EObject) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS:
			getFragments().clear();
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES:
			getModelReferences().clear();
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			setAdapters((Map<Class<?>, Object>) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			setLocked(LOCKED_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			getLocks().clear();
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__READONLY:
			setReadonly(READONLY_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
			setDirty(DIRTY_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
			setValidationStatusCode(VALIDATION_STATUS_CODE_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_FAILURE:
			setLoadFailure(LOAD_FAILURE_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_EXCEPTION:
			setLoadException(LOAD_EXCEPTION_EDEFAULT);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__ROOT_OBJECT_URI:
			return ROOT_OBJECT_URI_EDEFAULT == null ? rootObjectURI != null : !ROOT_OBJECT_URI_EDEFAULT.equals(rootObjectURI);
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return metadata != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
			return scenarioVersion != SCENARIO_VERSION_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			return VERSION_CONTEXT_EDEFAULT == null ? versionContext != null : !VERSION_CONTEXT_EDEFAULT.equals(versionContext);
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_SCENARIO_VERSION:
			return clientScenarioVersion != CLIENT_SCENARIO_VERSION_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_VERSION_CONTEXT:
			return CLIENT_VERSION_CONTEXT_EDEFAULT == null ? clientVersionContext != null : !CLIENT_VERSION_CONTEXT_EDEFAULT.equals(clientVersionContext);
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			return instance != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS:
			return fragments != null && !fragments.isEmpty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES:
			return modelReferences != null && !modelReferences.isEmpty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			return adapters != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			return locked != LOCKED_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
			return locks != null && !locks.isEmpty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__READONLY:
			return readonly != READONLY_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
			return dirty != DIRTY_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
			return validationStatusCode != VALIDATION_STATUS_CODE_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_FAILURE:
			return loadFailure != LOAD_FAILURE_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOAD_EXCEPTION:
			return LOAD_EXCEPTION_EDEFAULT == null ? loadException != null : !LOAD_EXCEPTION_EDEFAULT.equals(loadException);
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
		result.append(", rootObjectURI: ");
		result.append(rootObjectURI);
		result.append(", scenarioVersion: ");
		result.append(scenarioVersion);
		result.append(", versionContext: ");
		result.append(versionContext);
		result.append(", clientScenarioVersion: ");
		result.append(clientScenarioVersion);
		result.append(", clientVersionContext: ");
		result.append(clientVersionContext);
		result.append(", adapters: ");
		result.append(adapters);
		result.append(", locked: ");
		result.append(locked);
		result.append(", readonly: ");
		result.append(readonly);
		result.append(", dirty: ");
		result.append(dirty);
		result.append(", validationStatusCode: ");
		result.append(validationStatusCode);
		result.append(", loadFailure: ");
		result.append(loadFailure);
		result.append(", loadException: ");
		result.append(loadException);
		result.append(')');
		return result.toString();
	}

	@Override
	public void eNotify(Notification notification) {
		super.eNotify(notification);

		// Trigger model unload when reference count reaches zero AND the scenario is clean. Saving should alutomatically trigger a reference count increase/decrease
		if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_ModelReferences()) {

			if (notification.getEventType() == Notification.REMOVE || notification.getEventType() == Notification.REMOVE_MANY) {
				if (getModelReferences().isEmpty()) {
					if (!isDirty()) {
						// TODO: Delay unloading
						unload();
					}
				}
			}
			if (notification.getEventType() == Notification.ADD || notification.getEventType() == Notification.ADD_MANY) {
				// TODO: cancel unload request
			}
		}

	}
} //ScenarioInstanceImpl
