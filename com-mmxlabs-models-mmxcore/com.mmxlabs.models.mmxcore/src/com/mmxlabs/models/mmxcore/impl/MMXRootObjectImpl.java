/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import java.lang.reflect.InvocationTargetException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MMX Root Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl#getOtherNames <em>Other Names</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl#getSubModels <em>Sub Models</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MMXRootObjectImpl extends UUIDObjectImpl implements MMXRootObject {
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
	 * The cached value of the '{@link #getOtherNames() <em>Other Names</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.2
	 * <!-- end-user-doc -->
	 * @see #getOtherNames()
	 * @generated
	 * @ordered
	 */
	protected EList<String> otherNames;

	/**
	 * The cached value of the '{@link #getSubModels() <em>Sub Models</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubModels()
	 * @generated
	 * @ordered
	 */
	protected EList<MMXSubModel> subModels;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MMXRootObjectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MMXCorePackage.Literals.MMX_ROOT_OBJECT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_ROOT_OBJECT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.2
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getOtherNames() {
		if (otherNames == null) {
			otherNames = new EDataTypeUniqueEList<String>(String.class, this, MMXCorePackage.MMX_ROOT_OBJECT__OTHER_NAMES);
		}
		return otherNames;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MMXSubModel> getSubModels() {
		if (subModels == null) {
			subModels = new EObjectContainmentEList<MMXSubModel>(MMXSubModel.class, this, MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS);
		}
		return subModels;
	}

	/**
	 * Add the given model as a submodel in here
	 * 
	 * TODO this will always change the container of the submodel; if the submodel is shared with other instances,
	 * this would cause a problem. However, if we always want full containment on our model, we have to do this, so think about
	 * how this ought to work (primarily for things like validation)
	 * 
	 * @generated NO
	 */
	public void addSubModel(UUIDObject subModel) {
		assert(subModel.eContainer() == null);
		final Resource resource = subModel.eResource();
		final MMXSubModel container = MMXCoreFactory.eINSTANCE.createMMXSubModel();
		container.setSubModelInstance(subModel);
		getSubModels().add(container);
	}

	/**
	 * Restore submodels to their proper resources and destroy list.
	 * @generated NO
	 */
	public void restoreSubModels() {
//		for (final MMXSubModel sub : getSubModels()) {
//			sub.getOriginalResource().getContents().add(sub.getSubModelInstance());
//		}
		getSubModels().clear();
	}

	/**
	 * Get a submodel which implements the given class, or null if there isn't one
	 * @generated NO
	 */
	public <T> T getSubModel(final Class<T> subModelClass) {
		for (final MMXSubModel subModel : getSubModels()) {
			if (subModelClass.isInstance(subModel.getSubModelInstance())) {
				return subModelClass.cast(subModel.getSubModelInstance());
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				return ((InternalEList<?>)getSubModels()).basicRemove(otherEnd, msgs);
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
			case MMXCorePackage.MMX_ROOT_OBJECT__NAME:
				return getName();
			case MMXCorePackage.MMX_ROOT_OBJECT__OTHER_NAMES:
				return getOtherNames();
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				return getSubModels();
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
			case MMXCorePackage.MMX_ROOT_OBJECT__NAME:
				setName((String)newValue);
				return;
			case MMXCorePackage.MMX_ROOT_OBJECT__OTHER_NAMES:
				getOtherNames().clear();
				getOtherNames().addAll((Collection<? extends String>)newValue);
				return;
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				getSubModels().clear();
				getSubModels().addAll((Collection<? extends MMXSubModel>)newValue);
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
			case MMXCorePackage.MMX_ROOT_OBJECT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case MMXCorePackage.MMX_ROOT_OBJECT__OTHER_NAMES:
				getOtherNames().clear();
				return;
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				getSubModels().clear();
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
			case MMXCorePackage.MMX_ROOT_OBJECT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case MMXCorePackage.MMX_ROOT_OBJECT__OTHER_NAMES:
				return otherNames != null && !otherNames.isEmpty();
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				return subModels != null && !subModels.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case MMXCorePackage.MMX_ROOT_OBJECT__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				case MMXCorePackage.MMX_ROOT_OBJECT__OTHER_NAMES: return MMXCorePackage.NAMED_OBJECT__OTHER_NAMES;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return MMXCorePackage.MMX_ROOT_OBJECT__NAME;
				case MMXCorePackage.NAMED_OBJECT__OTHER_NAMES: return MMXCorePackage.MMX_ROOT_OBJECT__OTHER_NAMES;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings({"rawtypes", "unchecked" })
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case MMXCorePackage.MMX_ROOT_OBJECT___ADD_SUB_MODEL__UUIDOBJECT:
				addSubModel((UUIDObject)arguments.get(0));
				return null;
			case MMXCorePackage.MMX_ROOT_OBJECT___GET_SUB_MODEL__CLASS:
				return getSubModel((Class)arguments.get(0));
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
		result.append(", otherNames: ");
		result.append(otherNames);
		result.append(')');
		return result.toString();
	}

} //MMXRootObjectImpl
