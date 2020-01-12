/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.scenario.service.manifest.impl;

import com.mmxlabs.scenario.service.manifest.ManifestPackage;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.manifest.StorageType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl#getStorageType <em>Storage Type</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl#getPath <em>Path</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl#getDataVersion <em>Data Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl#getDisplayName <em>Display Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelArtifactImpl extends EObjectImpl implements ModelArtifact {
	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * The default value of the '{@link #getStorageType() <em>Storage Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorageType()
	 * @generated
	 * @ordered
	 */
	protected static final StorageType STORAGE_TYPE_EDEFAULT = StorageType.COLOCATED;

	/**
	 * The cached value of the '{@link #getStorageType() <em>Storage Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorageType()
	 * @generated
	 * @ordered
	 */
	protected StorageType storageType = STORAGE_TYPE_EDEFAULT;

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
	 * The default value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected static final String PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected String path = PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getDataVersion() <em>Data Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataVersion() <em>Data Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataVersion()
	 * @generated
	 * @ordered
	 */
	protected String dataVersion = DATA_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getDisplayName() <em>Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayName()
	 * @generated
	 * @ordered
	 */
	protected static final String DISPLAY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDisplayName() <em>Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayName()
	 * @generated
	 * @ordered
	 */
	protected String displayName = DISPLAY_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ManifestPackage.Literals.MODEL_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MODEL_ARTIFACT__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StorageType getStorageType() {
		return storageType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStorageType(StorageType newStorageType) {
		StorageType oldStorageType = storageType;
		storageType = newStorageType == null ? STORAGE_TYPE_EDEFAULT : newStorageType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MODEL_ARTIFACT__STORAGE_TYPE, oldStorageType, storageType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MODEL_ARTIFACT__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPath(String newPath) {
		String oldPath = path;
		path = newPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MODEL_ARTIFACT__PATH, oldPath, path));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDataVersion() {
		return dataVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDataVersion(String newDataVersion) {
		String oldDataVersion = dataVersion;
		dataVersion = newDataVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MODEL_ARTIFACT__DATA_VERSION, oldDataVersion, dataVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDisplayName(String newDisplayName) {
		String oldDisplayName = displayName;
		displayName = newDisplayName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MODEL_ARTIFACT__DISPLAY_NAME, oldDisplayName, displayName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ManifestPackage.MODEL_ARTIFACT__KEY:
				return getKey();
			case ManifestPackage.MODEL_ARTIFACT__STORAGE_TYPE:
				return getStorageType();
			case ManifestPackage.MODEL_ARTIFACT__TYPE:
				return getType();
			case ManifestPackage.MODEL_ARTIFACT__PATH:
				return getPath();
			case ManifestPackage.MODEL_ARTIFACT__DATA_VERSION:
				return getDataVersion();
			case ManifestPackage.MODEL_ARTIFACT__DISPLAY_NAME:
				return getDisplayName();
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
			case ManifestPackage.MODEL_ARTIFACT__KEY:
				setKey((String)newValue);
				return;
			case ManifestPackage.MODEL_ARTIFACT__STORAGE_TYPE:
				setStorageType((StorageType)newValue);
				return;
			case ManifestPackage.MODEL_ARTIFACT__TYPE:
				setType((String)newValue);
				return;
			case ManifestPackage.MODEL_ARTIFACT__PATH:
				setPath((String)newValue);
				return;
			case ManifestPackage.MODEL_ARTIFACT__DATA_VERSION:
				setDataVersion((String)newValue);
				return;
			case ManifestPackage.MODEL_ARTIFACT__DISPLAY_NAME:
				setDisplayName((String)newValue);
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
			case ManifestPackage.MODEL_ARTIFACT__KEY:
				setKey(KEY_EDEFAULT);
				return;
			case ManifestPackage.MODEL_ARTIFACT__STORAGE_TYPE:
				setStorageType(STORAGE_TYPE_EDEFAULT);
				return;
			case ManifestPackage.MODEL_ARTIFACT__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case ManifestPackage.MODEL_ARTIFACT__PATH:
				setPath(PATH_EDEFAULT);
				return;
			case ManifestPackage.MODEL_ARTIFACT__DATA_VERSION:
				setDataVersion(DATA_VERSION_EDEFAULT);
				return;
			case ManifestPackage.MODEL_ARTIFACT__DISPLAY_NAME:
				setDisplayName(DISPLAY_NAME_EDEFAULT);
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
			case ManifestPackage.MODEL_ARTIFACT__KEY:
				return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
			case ManifestPackage.MODEL_ARTIFACT__STORAGE_TYPE:
				return storageType != STORAGE_TYPE_EDEFAULT;
			case ManifestPackage.MODEL_ARTIFACT__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case ManifestPackage.MODEL_ARTIFACT__PATH:
				return PATH_EDEFAULT == null ? path != null : !PATH_EDEFAULT.equals(path);
			case ManifestPackage.MODEL_ARTIFACT__DATA_VERSION:
				return DATA_VERSION_EDEFAULT == null ? dataVersion != null : !DATA_VERSION_EDEFAULT.equals(dataVersion);
			case ManifestPackage.MODEL_ARTIFACT__DISPLAY_NAME:
				return DISPLAY_NAME_EDEFAULT == null ? displayName != null : !DISPLAY_NAME_EDEFAULT.equals(displayName);
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
		result.append(" (key: ");
		result.append(key);
		result.append(", storageType: ");
		result.append(storageType);
		result.append(", type: ");
		result.append(type);
		result.append(", path: ");
		result.append(path);
		result.append(", dataVersion: ");
		result.append(dataVersion);
		result.append(", displayName: ");
		result.append(displayName);
		result.append(')');
		return result.toString();
	}

} //ModelArtifactImpl
