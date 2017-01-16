/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#getElements <em>Elements</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#isArchived <em>Archived</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#isHidden <em>Hidden</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ContainerImpl extends EObjectImpl implements Container {
	/**
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
	protected EList<Container> elements;

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
	 * The default value of the '{@link #isHidden() <em>Hidden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHidden()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HIDDEN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isHidden() <em>Hidden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHidden()
	 * @generated
	 * @ordered
	 */
	protected boolean hidden = HIDDEN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Container getParent() {
		if (eContainerFeatureID() != ScenarioServicePackage.CONTAINER__PARENT)
			return null;
		return (Container) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParent(Container newParent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newParent, ScenarioServicePackage.CONTAINER__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParent(Container newParent) {
		if (newParent != eInternalContainer() || (eContainerFeatureID() != ScenarioServicePackage.CONTAINER__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, newParent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject) newParent).eInverseAdd(this, ScenarioServicePackage.CONTAINER__ELEMENTS, Container.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.CONTAINER__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Container> getElements() {
		if (elements == null) {
			elements = new EObjectContainmentWithInverseEList<Container>(Container.class, this, ScenarioServicePackage.CONTAINER__ELEMENTS, ScenarioServicePackage.CONTAINER__PARENT);
		}
		return elements;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.CONTAINER__ARCHIVED, oldArchived, archived));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.CONTAINER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHidden(boolean newHidden) {
		boolean oldHidden = hidden;
		hidden = newHidden;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.CONTAINER__HIDDEN, oldHidden, hidden));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IScenarioService getScenarioService() {
		EObject up = this;
		while (up != null && !(up instanceof ScenarioService)) {
			up = up.eContainer();
		}

		if (up instanceof ScenarioService) {
			return ((ScenarioService) up).getServiceRef();
		}

		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getContainedInstanceCount() {
		int accumulator = 0;
		for (final Container container : getElements()) {
			accumulator += container.getContainedInstanceCount();
		}
		return accumulator;
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
		case ScenarioServicePackage.CONTAINER__PARENT:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetParent((Container) otherEnd, msgs);
		case ScenarioServicePackage.CONTAINER__ELEMENTS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getElements()).basicAdd(otherEnd, msgs);
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
		case ScenarioServicePackage.CONTAINER__PARENT:
			return basicSetParent(null, msgs);
		case ScenarioServicePackage.CONTAINER__ELEMENTS:
			return ((InternalEList<?>) getElements()).basicRemove(otherEnd, msgs);
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
		case ScenarioServicePackage.CONTAINER__PARENT:
			return eInternalContainer().eInverseRemove(this, ScenarioServicePackage.CONTAINER__ELEMENTS, Container.class, msgs);
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
		case ScenarioServicePackage.CONTAINER__PARENT:
			return getParent();
		case ScenarioServicePackage.CONTAINER__ELEMENTS:
			return getElements();
		case ScenarioServicePackage.CONTAINER__ARCHIVED:
			return isArchived();
		case ScenarioServicePackage.CONTAINER__NAME:
			return getName();
		case ScenarioServicePackage.CONTAINER__HIDDEN:
			return isHidden();
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
		case ScenarioServicePackage.CONTAINER__PARENT:
			setParent((Container) newValue);
			return;
		case ScenarioServicePackage.CONTAINER__ELEMENTS:
			getElements().clear();
			getElements().addAll((Collection<? extends Container>) newValue);
			return;
		case ScenarioServicePackage.CONTAINER__ARCHIVED:
			setArchived((Boolean) newValue);
			return;
		case ScenarioServicePackage.CONTAINER__NAME:
			setName((String) newValue);
			return;
		case ScenarioServicePackage.CONTAINER__HIDDEN:
			setHidden((Boolean) newValue);
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
		case ScenarioServicePackage.CONTAINER__PARENT:
			setParent((Container) null);
			return;
		case ScenarioServicePackage.CONTAINER__ELEMENTS:
			getElements().clear();
			return;
		case ScenarioServicePackage.CONTAINER__ARCHIVED:
			setArchived(ARCHIVED_EDEFAULT);
			return;
		case ScenarioServicePackage.CONTAINER__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ScenarioServicePackage.CONTAINER__HIDDEN:
			setHidden(HIDDEN_EDEFAULT);
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
		case ScenarioServicePackage.CONTAINER__PARENT:
			return getParent() != null;
		case ScenarioServicePackage.CONTAINER__ELEMENTS:
			return elements != null && !elements.isEmpty();
		case ScenarioServicePackage.CONTAINER__ARCHIVED:
			return archived != ARCHIVED_EDEFAULT;
		case ScenarioServicePackage.CONTAINER__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case ScenarioServicePackage.CONTAINER__HIDDEN:
			return hidden != HIDDEN_EDEFAULT;
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
		result.append(" (archived: ");
		result.append(archived);
		result.append(", name: ");
		result.append(name);
		result.append(", hidden: ");
		result.append(hidden);
		result.append(')');
		return result.toString();
	}

} //ContainerImpl
