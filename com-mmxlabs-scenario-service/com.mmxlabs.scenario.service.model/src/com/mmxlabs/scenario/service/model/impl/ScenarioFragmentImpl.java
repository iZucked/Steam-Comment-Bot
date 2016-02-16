/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Fragment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getScenarioInstance <em>Scenario Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getFragment <em>Fragment</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getContentType <em>Content Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioFragmentImpl extends EObjectImpl implements ScenarioFragment {
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
	 * The cached value of the '{@link #getFragment() <em>Fragment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFragment()
	 * @generated
	 * @ordered
	 */
	protected EObject fragment;

	/**
	 * The default value of the '{@link #getContentType() <em>Content Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContentType()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTENT_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContentType() <em>Content Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContentType()
	 * @generated
	 * @ordered
	 */
	protected String contentType = CONTENT_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioFragmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getScenarioFragment();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance getScenarioInstance() {
		if (eContainerFeatureID() != ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE)
			return null;
		return (ScenarioInstance) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScenarioInstance(ScenarioInstance newScenarioInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newScenarioInstance, ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScenarioInstance(ScenarioInstance newScenarioInstance) {
		if (newScenarioInstance != eInternalContainer() || (eContainerFeatureID() != ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE && newScenarioInstance != null)) {
			if (EcoreUtil.isAncestor(this, newScenarioInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newScenarioInstance != null)
				msgs = ((InternalEObject) newScenarioInstance).eInverseAdd(this, ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS, ScenarioInstance.class, msgs);
			msgs = basicSetScenarioInstance(newScenarioInstance, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE, newScenarioInstance, newScenarioInstance));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_FRAGMENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getFragment() {
		if (fragment != null && fragment.eIsProxy()) {
			InternalEObject oldFragment = (InternalEObject) fragment;
			fragment = eResolveProxy(oldFragment);
			if (fragment != oldFragment) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioServicePackage.SCENARIO_FRAGMENT__FRAGMENT, oldFragment, fragment));
			}
		}
		return fragment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetFragment() {
		return fragment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFragment(EObject newFragment) {
		EObject oldFragment = fragment;
		fragment = newFragment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_FRAGMENT__FRAGMENT, oldFragment, fragment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContentType(String newContentType) {
		String oldContentType = contentType;
		contentType = newContentType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_FRAGMENT__CONTENT_TYPE, oldContentType, contentType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetScenarioInstance((ScenarioInstance) otherEnd, msgs);
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
		case ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE:
			return basicSetScenarioInstance(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
		case ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE:
			return eInternalContainer().eInverseRemove(this, ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS, ScenarioInstance.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE:
			return getScenarioInstance();
		case ScenarioServicePackage.SCENARIO_FRAGMENT__NAME:
			return getName();
		case ScenarioServicePackage.SCENARIO_FRAGMENT__FRAGMENT:
			if (resolve)
				return getFragment();
			return basicGetFragment();
		case ScenarioServicePackage.SCENARIO_FRAGMENT__CONTENT_TYPE:
			return getContentType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE:
			setScenarioInstance((ScenarioInstance) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_FRAGMENT__NAME:
			setName((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_FRAGMENT__FRAGMENT:
			setFragment((EObject) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_FRAGMENT__CONTENT_TYPE:
			setContentType((String) newValue);
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
		case ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE:
			setScenarioInstance((ScenarioInstance) null);
			return;
		case ScenarioServicePackage.SCENARIO_FRAGMENT__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_FRAGMENT__FRAGMENT:
			setFragment((EObject) null);
			return;
		case ScenarioServicePackage.SCENARIO_FRAGMENT__CONTENT_TYPE:
			setContentType(CONTENT_TYPE_EDEFAULT);
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
		case ScenarioServicePackage.SCENARIO_FRAGMENT__SCENARIO_INSTANCE:
			return getScenarioInstance() != null;
		case ScenarioServicePackage.SCENARIO_FRAGMENT__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case ScenarioServicePackage.SCENARIO_FRAGMENT__FRAGMENT:
			return fragment != null;
		case ScenarioServicePackage.SCENARIO_FRAGMENT__CONTENT_TYPE:
			return CONTENT_TYPE_EDEFAULT == null ? contentType != null : !CONTENT_TYPE_EDEFAULT.equals(contentType);
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
		result.append(", contentType: ");
		result.append(contentType);
		result.append(')');
		return result.toString();
	}

} //ScenarioFragmentImpl
