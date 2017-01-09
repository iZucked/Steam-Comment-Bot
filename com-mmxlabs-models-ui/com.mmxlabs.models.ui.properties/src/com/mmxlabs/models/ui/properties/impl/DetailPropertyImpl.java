/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.properties.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jface.viewers.ILabelProvider;

import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Detail Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getId <em>Id</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getUnitsPrefix <em>Units Prefix</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getUnitsSuffix <em>Units Suffix</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getObject <em>Object</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getLabelProvider <em>Label Provider</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DetailPropertyImpl extends MinimalEObjectImpl.Container implements DetailProperty {
	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<DetailProperty> children;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

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
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnitsPrefix() <em>Units Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitsPrefix()
	 * @generated
	 * @ordered
	 */
	protected static final String UNITS_PREFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnitsPrefix() <em>Units Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitsPrefix()
	 * @generated
	 * @ordered
	 */
	protected String unitsPrefix = UNITS_PREFIX_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnitsSuffix() <em>Units Suffix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitsSuffix()
	 * @generated
	 * @ordered
	 */
	protected static final String UNITS_SUFFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnitsSuffix() <em>Units Suffix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitsSuffix()
	 * @generated
	 * @ordered
	 */
	protected String unitsSuffix = UNITS_SUFFIX_EDEFAULT;

	/**
	 * The default value of the '{@link #getObject() <em>Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObject()
	 * @generated
	 * @ordered
	 */
	protected static final Object OBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getObject() <em>Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObject()
	 * @generated
	 * @ordered
	 */
	protected Object object = OBJECT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLabelProvider() <em>Label Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabelProvider()
	 * @generated
	 * @ordered
	 */
	protected static final ILabelProvider LABEL_PROVIDER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabelProvider() <em>Label Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabelProvider()
	 * @generated
	 * @ordered
	 */
	protected ILabelProvider labelProvider = LABEL_PROVIDER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DetailPropertyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PropertiesPackage.Literals.DETAIL_PROPERTY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DetailProperty getParent() {
		if (eContainerFeatureID() != PropertiesPackage.DETAIL_PROPERTY__PARENT) return null;
		return (DetailProperty)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParent(DetailProperty newParent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newParent, PropertiesPackage.DETAIL_PROPERTY__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParent(DetailProperty newParent) {
		if (newParent != eInternalContainer() || (eContainerFeatureID() != PropertiesPackage.DETAIL_PROPERTY__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, newParent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseAdd(this, PropertiesPackage.DETAIL_PROPERTY__CHILDREN, DetailProperty.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DetailProperty> getChildren() {
		if (children == null) {
			children = new EObjectContainmentWithInverseEList<DetailProperty>(DetailProperty.class, this, PropertiesPackage.DETAIL_PROPERTY__CHILDREN, PropertiesPackage.DETAIL_PROPERTY__PARENT);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getUnitsPrefix() {
		return unitsPrefix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnitsPrefix(String newUnitsPrefix) {
		String oldUnitsPrefix = unitsPrefix;
		unitsPrefix = newUnitsPrefix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__UNITS_PREFIX, oldUnitsPrefix, unitsPrefix));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getUnitsSuffix() {
		return unitsSuffix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnitsSuffix(String newUnitsSuffix) {
		String oldUnitsSuffix = unitsSuffix;
		unitsSuffix = newUnitsSuffix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__UNITS_SUFFIX, oldUnitsSuffix, unitsSuffix));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getObject() {
		return object;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setObject(Object newObject) {
		Object oldObject = object;
		object = newObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__OBJECT, oldObject, object));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLabelProvider(ILabelProvider newLabelProvider) {
		ILabelProvider oldLabelProvider = labelProvider;
		labelProvider = newLabelProvider;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__LABEL_PROVIDER, oldLabelProvider, labelProvider));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String format() {
		
		final Object obj = getObject();
		final ILabelProvider lp = getLabelProvider();
		if (lp != null) {
			return lp.getText(obj);
		} else if (obj != null) {
			return obj.toString();
		} else {
			return null;
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
			case PropertiesPackage.DETAIL_PROPERTY__PARENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetParent((DetailProperty)otherEnd, msgs);
			case PropertiesPackage.DETAIL_PROPERTY__CHILDREN:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
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
			case PropertiesPackage.DETAIL_PROPERTY__PARENT:
				return basicSetParent(null, msgs);
			case PropertiesPackage.DETAIL_PROPERTY__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
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
			case PropertiesPackage.DETAIL_PROPERTY__PARENT:
				return eInternalContainer().eInverseRemove(this, PropertiesPackage.DETAIL_PROPERTY__CHILDREN, DetailProperty.class, msgs);
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
			case PropertiesPackage.DETAIL_PROPERTY__PARENT:
				return getParent();
			case PropertiesPackage.DETAIL_PROPERTY__CHILDREN:
				return getChildren();
			case PropertiesPackage.DETAIL_PROPERTY__ID:
				return getId();
			case PropertiesPackage.DETAIL_PROPERTY__NAME:
				return getName();
			case PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION:
				return getDescription();
			case PropertiesPackage.DETAIL_PROPERTY__UNITS_PREFIX:
				return getUnitsPrefix();
			case PropertiesPackage.DETAIL_PROPERTY__UNITS_SUFFIX:
				return getUnitsSuffix();
			case PropertiesPackage.DETAIL_PROPERTY__OBJECT:
				return getObject();
			case PropertiesPackage.DETAIL_PROPERTY__LABEL_PROVIDER:
				return getLabelProvider();
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
			case PropertiesPackage.DETAIL_PROPERTY__PARENT:
				setParent((DetailProperty)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends DetailProperty>)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__ID:
				setId((String)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__NAME:
				setName((String)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__UNITS_PREFIX:
				setUnitsPrefix((String)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__UNITS_SUFFIX:
				setUnitsSuffix((String)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__OBJECT:
				setObject(newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__LABEL_PROVIDER:
				setLabelProvider((ILabelProvider)newValue);
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
			case PropertiesPackage.DETAIL_PROPERTY__PARENT:
				setParent((DetailProperty)null);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__CHILDREN:
				getChildren().clear();
				return;
			case PropertiesPackage.DETAIL_PROPERTY__ID:
				setId(ID_EDEFAULT);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__UNITS_PREFIX:
				setUnitsPrefix(UNITS_PREFIX_EDEFAULT);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__UNITS_SUFFIX:
				setUnitsSuffix(UNITS_SUFFIX_EDEFAULT);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__OBJECT:
				setObject(OBJECT_EDEFAULT);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__LABEL_PROVIDER:
				setLabelProvider(LABEL_PROVIDER_EDEFAULT);
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
			case PropertiesPackage.DETAIL_PROPERTY__PARENT:
				return getParent() != null;
			case PropertiesPackage.DETAIL_PROPERTY__CHILDREN:
				return children != null && !children.isEmpty();
			case PropertiesPackage.DETAIL_PROPERTY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PropertiesPackage.DETAIL_PROPERTY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case PropertiesPackage.DETAIL_PROPERTY__UNITS_PREFIX:
				return UNITS_PREFIX_EDEFAULT == null ? unitsPrefix != null : !UNITS_PREFIX_EDEFAULT.equals(unitsPrefix);
			case PropertiesPackage.DETAIL_PROPERTY__UNITS_SUFFIX:
				return UNITS_SUFFIX_EDEFAULT == null ? unitsSuffix != null : !UNITS_SUFFIX_EDEFAULT.equals(unitsSuffix);
			case PropertiesPackage.DETAIL_PROPERTY__OBJECT:
				return OBJECT_EDEFAULT == null ? object != null : !OBJECT_EDEFAULT.equals(object);
			case PropertiesPackage.DETAIL_PROPERTY__LABEL_PROVIDER:
				return LABEL_PROVIDER_EDEFAULT == null ? labelProvider != null : !LABEL_PROVIDER_EDEFAULT.equals(labelProvider);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case PropertiesPackage.DETAIL_PROPERTY___FORMAT:
				return format();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (id: ");
		result.append(id);
		result.append(", name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(", unitsPrefix: ");
		result.append(unitsPrefix);
		result.append(", unitsSuffix: ");
		result.append(unitsSuffix);
		result.append(", object: ");
		result.append(object);
		result.append(", labelProvider: ");
		result.append(labelProvider);
		result.append(')');
		return result.toString();
	}

} //DetailPropertyImpl
