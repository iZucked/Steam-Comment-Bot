/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ParamSet;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.Solution;

import java.util.Collection;

import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getUri <em>Uri</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isArchived <em>Archived</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getInitialSolution <em>Initial Solution</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getVariations <em>Variations</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getParameterSets <em>Parameter Sets</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getInstance <em>Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getController <em>Controller</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getAdapters <em>Adapters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioInstanceImpl extends EObjectImpl implements ScenarioInstance {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

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
	 * The default value of the '{@link #getUri() <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
	protected static final String URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUri() <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
	protected String uri = URI_EDEFAULT;

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
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
	protected Metadata metadata;

	/**
	 * The default value of the '{@link #isArchived() <em>Archived</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isArchived()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ARCHIVED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isArchived() <em>Archived</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isArchived()
	 * @generated
	 * @ordered
	 */
	protected boolean archived = ARCHIVED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInitialSolution() <em>Initial Solution</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialSolution()
	 * @generated
	 * @ordered
	 */
	protected Solution initialSolution;

	/**
	 * The cached value of the '{@link #getVariations() <em>Variations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariations()
	 * @generated
	 * @ordered
	 */
	protected EList<ScenarioInstance> variations;

	/**
	 * The cached value of the '{@link #getParameterSets() <em>Parameter Sets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterSets()
	 * @generated
	 * @ordered
	 */
	protected EList<ParamSet> parameterSets;

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
	 * The cached value of the '{@link #getController() <em>Controller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getController()
	 * @generated
	 * @ordered
	 */
	protected EObject controller;

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
	public EList<ScenarioInstance> getVariations() {
		if (variations == null) {
			variations = new EObjectContainmentEList<ScenarioInstance>(ScenarioInstance.class, this, ScenarioServicePackage.SCENARIO_INSTANCE__VARIATIONS);
		}
		return variations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ParamSet> getParameterSets() {
		if (parameterSets == null) {
			parameterSets = new EObjectContainmentEList<ParamSet>(ParamSet.class, this, ScenarioServicePackage.SCENARIO_INSTANCE__PARAMETER_SETS);
		}
		return parameterSets;
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
	public EObject getController() {
		if (controller != null && controller.eIsProxy()) {
			InternalEObject oldController = (InternalEObject) controller;
			controller = eResolveProxy(oldController);
			if (controller != oldController) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioServicePackage.SCENARIO_INSTANCE__CONTROLLER, oldController, controller));
			}
		}
		return controller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetController() {
		return controller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setController(EObject newController) {
		EObject oldController = controller;
		controller = newController;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__CONTROLLER, oldController, controller));
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
	public String getUri() {
		return uri;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUri(String newUri) {
		String oldUri = uri;
		uri = newUri;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__URI, oldUri, uri));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Solution getInitialSolution() {
		return initialSolution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInitialSolution(Solution newInitialSolution, NotificationChain msgs) {
		Solution oldInitialSolution = initialSolution;
		initialSolution = newInitialSolution;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION, oldInitialSolution, newInitialSolution);
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
	public void setInitialSolution(Solution newInitialSolution) {
		if (newInitialSolution != initialSolution) {
			NotificationChain msgs = null;
			if (initialSolution != null)
				msgs = ((InternalEObject) initialSolution).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION, null, msgs);
			if (newInitialSolution != null)
				msgs = ((InternalEObject) newInitialSolution).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION, null, msgs);
			msgs = basicSetInitialSolution(newInitialSolution, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION, newInitialSolution, newInitialSolution));
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
	public boolean isArchived() {
		return archived;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArchived(boolean newArchived) {
		boolean oldArchived = archived;
		archived = newArchived;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_INSTANCE__ARCHIVED, oldArchived, archived));
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return basicSetMetadata(null, msgs);
		case ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION:
			return basicSetInitialSolution(null, msgs);
		case ScenarioServicePackage.SCENARIO_INSTANCE__VARIATIONS:
			return ((InternalEList<?>) getVariations()).basicRemove(otherEnd, msgs);
		case ScenarioServicePackage.SCENARIO_INSTANCE__PARAMETER_SETS:
			return ((InternalEList<?>) getParameterSets()).basicRemove(otherEnd, msgs);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__NAME:
			return getName();
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
			return getUuid();
		case ScenarioServicePackage.SCENARIO_INSTANCE__URI:
			return getUri();
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			return isLocked();
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return getMetadata();
		case ScenarioServicePackage.SCENARIO_INSTANCE__ARCHIVED:
			return isArchived();
		case ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION:
			return getInitialSolution();
		case ScenarioServicePackage.SCENARIO_INSTANCE__VARIATIONS:
			return getVariations();
		case ScenarioServicePackage.SCENARIO_INSTANCE__PARAMETER_SETS:
			return getParameterSets();
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			if (resolve)
				return getInstance();
			return basicGetInstance();
		case ScenarioServicePackage.SCENARIO_INSTANCE__CONTROLLER:
			if (resolve)
				return getController();
			return basicGetController();
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			return getAdapters();
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__NAME:
			setName((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
			setUuid((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__URI:
			setUri((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			setLocked((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			setMetadata((Metadata) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ARCHIVED:
			setArchived((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION:
			setInitialSolution((Solution) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VARIATIONS:
			getVariations().clear();
			getVariations().addAll((Collection<? extends ScenarioInstance>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__PARAMETER_SETS:
			getParameterSets().clear();
			getParameterSets().addAll((Collection<? extends ParamSet>) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			setInstance((EObject) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__CONTROLLER:
			setController((EObject) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			setAdapters((Map<Class<?>, Object>) newValue);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
			setUuid(UUID_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__URI:
			setUri(URI_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			setLocked(LOCKED_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			setMetadata((Metadata) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ARCHIVED:
			setArchived(ARCHIVED_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION:
			setInitialSolution((Solution) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VARIATIONS:
			getVariations().clear();
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__PARAMETER_SETS:
			getParameterSets().clear();
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			setInstance((EObject) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__CONTROLLER:
			setController((EObject) null);
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			setAdapters((Map<Class<?>, Object>) null);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
			return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
		case ScenarioServicePackage.SCENARIO_INSTANCE__URI:
			return URI_EDEFAULT == null ? uri != null : !URI_EDEFAULT.equals(uri);
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
			return locked != LOCKED_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
			return metadata != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ARCHIVED:
			return archived != ARCHIVED_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION:
			return initialSolution != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__VARIATIONS:
			return variations != null && !variations.isEmpty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__PARAMETER_SETS:
			return parameterSets != null && !parameterSets.isEmpty();
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
			return instance != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__CONTROLLER:
			return controller != null;
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
			return adapters != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", uuid: ");
		result.append(uuid);
		result.append(", uri: ");
		result.append(uri);
		result.append(", locked: ");
		result.append(locked);
		result.append(", archived: ");
		result.append(archived);
		result.append(", adapters: ");
		result.append(adapters);
		result.append(')');
		return result.toString();
	}

} //ScenarioInstanceImpl
