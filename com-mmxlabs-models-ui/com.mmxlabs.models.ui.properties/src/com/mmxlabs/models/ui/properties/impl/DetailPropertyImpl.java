/**
 */
package com.mmxlabs.models.ui.properties.impl;

import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesPackage;

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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Detail Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.properties.impl.DetailPropertyImpl#getUnits <em>Units</em>}</li>
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
	 * The default value of the '{@link #getUnits() <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnits()
	 * @generated
	 * @ordered
	 */
	protected static final String UNITS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnits() <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnits()
	 * @generated
	 * @ordered
	 */
	protected String units = UNITS_EDEFAULT;

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
	public DetailProperty getParent() {
		if (eContainerFeatureID() != PropertiesPackage.DETAIL_PROPERTY__PARENT) return null;
		return (DetailProperty)eInternalContainer();
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
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public String getUnits() {
		return units;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnits(String newUnits) {
		String oldUnits = units;
		units = newUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.DETAIL_PROPERTY__UNITS, oldUnits, units));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
			case PropertiesPackage.DETAIL_PROPERTY__NAME:
				return getName();
			case PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION:
				return getDescription();
			case PropertiesPackage.DETAIL_PROPERTY__UNITS:
				return getUnits();
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
			case PropertiesPackage.DETAIL_PROPERTY__NAME:
				setName((String)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__UNITS:
				setUnits((String)newValue);
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
			case PropertiesPackage.DETAIL_PROPERTY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case PropertiesPackage.DETAIL_PROPERTY__UNITS:
				setUnits(UNITS_EDEFAULT);
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
			case PropertiesPackage.DETAIL_PROPERTY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PropertiesPackage.DETAIL_PROPERTY__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case PropertiesPackage.DETAIL_PROPERTY__UNITS:
				return UNITS_EDEFAULT == null ? units != null : !UNITS_EDEFAULT.equals(units);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(", units: ");
		result.append(units);
		result.append(", object: ");
		result.append(object);
		result.append(", labelProvider: ");
		result.append(labelProvider);
		result.append(')');
		return result.toString();
	}

} //DetailPropertyImpl
