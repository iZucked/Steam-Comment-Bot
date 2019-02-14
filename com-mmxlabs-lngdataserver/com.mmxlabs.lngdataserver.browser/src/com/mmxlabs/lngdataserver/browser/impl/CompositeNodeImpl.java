/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lngdataserver.browser.impl;

import com.mmxlabs.lngdataserver.browser.BrowserPackage;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.impl.CompositeNodeImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.impl.CompositeNodeImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.impl.CompositeNodeImpl#getCurrent <em>Current</em>}</li>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.impl.CompositeNodeImpl#getActionHandler <em>Action Handler</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompositeNodeImpl extends NodeImpl implements CompositeNode {
	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> children;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCurrent() <em>Current</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrent()
	 * @generated
	 * @ordered
	 */
	protected Node current;

	/**
	 * The default value of the '{@link #getActionHandler() <em>Action Handler</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActionHandler()
	 * @generated
	 * @ordered
	 */
	protected static final IDataBrowserActionsHandler ACTION_HANDLER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getActionHandler() <em>Action Handler</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActionHandler()
	 * @generated
	 * @ordered
	 */
	protected IDataBrowserActionsHandler actionHandler = ACTION_HANDLER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CompositeNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BrowserPackage.Literals.COMPOSITE_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList<Node>(Node.class, this, BrowserPackage.COMPOSITE_NODE__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BrowserPackage.COMPOSITE_NODE__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node getCurrent() {
		if (current != null && current.eIsProxy()) {
			InternalEObject oldCurrent = (InternalEObject) current;
			current = (Node) eResolveProxy(oldCurrent);
			if (current != oldCurrent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BrowserPackage.COMPOSITE_NODE__CURRENT, oldCurrent, current));
			}
		}
		return current;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node basicGetCurrent() {
		return current;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrent(Node newCurrent) {
		Node oldCurrent = current;
		current = newCurrent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BrowserPackage.COMPOSITE_NODE__CURRENT, oldCurrent, current));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IDataBrowserActionsHandler getActionHandler() {
		return actionHandler;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActionHandler(IDataBrowserActionsHandler newActionHandler) {
		IDataBrowserActionsHandler oldActionHandler = actionHandler;
		actionHandler = newActionHandler;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BrowserPackage.COMPOSITE_NODE__ACTION_HANDLER, oldActionHandler, actionHandler));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case BrowserPackage.COMPOSITE_NODE__CHILDREN:
			return ((InternalEList<?>) getChildren()).basicRemove(otherEnd, msgs);
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
		case BrowserPackage.COMPOSITE_NODE__CHILDREN:
			return getChildren();
		case BrowserPackage.COMPOSITE_NODE__TYPE:
			return getType();
		case BrowserPackage.COMPOSITE_NODE__CURRENT:
			if (resolve)
				return getCurrent();
			return basicGetCurrent();
		case BrowserPackage.COMPOSITE_NODE__ACTION_HANDLER:
			return getActionHandler();
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
		case BrowserPackage.COMPOSITE_NODE__CHILDREN:
			getChildren().clear();
			getChildren().addAll((Collection<? extends Node>) newValue);
			return;
		case BrowserPackage.COMPOSITE_NODE__TYPE:
			setType((String) newValue);
			return;
		case BrowserPackage.COMPOSITE_NODE__CURRENT:
			setCurrent((Node) newValue);
			return;
		case BrowserPackage.COMPOSITE_NODE__ACTION_HANDLER:
			setActionHandler((IDataBrowserActionsHandler) newValue);
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
		case BrowserPackage.COMPOSITE_NODE__CHILDREN:
			getChildren().clear();
			return;
		case BrowserPackage.COMPOSITE_NODE__TYPE:
			setType(TYPE_EDEFAULT);
			return;
		case BrowserPackage.COMPOSITE_NODE__CURRENT:
			setCurrent((Node) null);
			return;
		case BrowserPackage.COMPOSITE_NODE__ACTION_HANDLER:
			setActionHandler(ACTION_HANDLER_EDEFAULT);
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
		case BrowserPackage.COMPOSITE_NODE__CHILDREN:
			return children != null && !children.isEmpty();
		case BrowserPackage.COMPOSITE_NODE__TYPE:
			return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
		case BrowserPackage.COMPOSITE_NODE__CURRENT:
			return current != null;
		case BrowserPackage.COMPOSITE_NODE__ACTION_HANDLER:
			return ACTION_HANDLER_EDEFAULT == null ? actionHandler != null : !ACTION_HANDLER_EDEFAULT.equals(actionHandler);
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
		result.append(" (type: ");
		result.append(type);
		result.append(", actionHandler: ");
		result.append(actionHandler);
		result.append(')');
		return result.toString();
	}

} //CompositeNodeImpl
