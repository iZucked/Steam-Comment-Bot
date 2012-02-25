/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.mmxcore.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MMX Root Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl#getSubModels <em>Sub Models</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MMXRootObjectImpl extends UUIDObjectImpl implements MMXRootObject {
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
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int VERSION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected int version = VERSION_EDEFAULT;

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
	public EList<MMXSubModel> getSubModels() {
		if (subModels == null) {
			subModels = new EObjectContainmentEList<MMXSubModel>(MMXSubModel.class, this, MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS);
		}
		return subModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(int newVersion) {
		int oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_ROOT_OBJECT__VERSION, oldVersion, version));
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
		container.setOriginalResource(resource);
		container.setSubModelInstance(subModel);
		getSubModels().add(container);
	}

	/**
	 * Restore submodels to their proper resources and destroy list.
	 * @generated NO
	 */
	public void restoreSubModels() {
		for (final MMXSubModel sub : getSubModels()) {
			sub.getOriginalResource().getContents().add(sub.getSubModelInstance());
		}
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
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				return getSubModels();
			case MMXCorePackage.MMX_ROOT_OBJECT__VERSION:
				return getVersion();
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
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				getSubModels().clear();
				getSubModels().addAll((Collection<? extends MMXSubModel>)newValue);
				return;
			case MMXCorePackage.MMX_ROOT_OBJECT__VERSION:
				setVersion((Integer)newValue);
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
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				getSubModels().clear();
				return;
			case MMXCorePackage.MMX_ROOT_OBJECT__VERSION:
				setVersion(VERSION_EDEFAULT);
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
			case MMXCorePackage.MMX_ROOT_OBJECT__SUB_MODELS:
				return subModels != null && !subModels.isEmpty();
			case MMXCorePackage.MMX_ROOT_OBJECT__VERSION:
				return version != VERSION_EDEFAULT;
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
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} //MMXRootObjectImpl
