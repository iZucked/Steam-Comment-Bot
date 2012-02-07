/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.port.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.ScenarioPackage;
import scenario.impl.UUIDObjectImpl;
import scenario.port.Canal;
import scenario.port.DistanceModel;
import scenario.port.PortPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Canal</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.port.impl.CanalImpl#getName <em>Name</em>}</li>
 * <li>{@link scenario.port.impl.CanalImpl#getDistanceModel <em>Distance Model</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class CanalImpl extends UUIDObjectImpl implements Canal {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDistanceModel() <em>Distance Model</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDistanceModel()
	 * @generated
	 * @ordered
	 */
	protected DistanceModel distanceModel;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CanalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.CANAL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setName(final String newName) {
		final String oldName = name;
		name = newName;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__NAME, oldName, name));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject getContainer() {
		return eContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public DistanceModel getDistanceModel() {
		if ((distanceModel != null) && distanceModel.eIsProxy()) {
			final InternalEObject oldDistanceModel = (InternalEObject) distanceModel;
			distanceModel = (DistanceModel) eResolveProxy(oldDistanceModel);
			if (distanceModel != oldDistanceModel) {
				final InternalEObject newDistanceModel = (InternalEObject) distanceModel;
				NotificationChain msgs = oldDistanceModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.CANAL__DISTANCE_MODEL, null, null);
				if (newDistanceModel.eInternalContainer() == null) {
					msgs = newDistanceModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.CANAL__DISTANCE_MODEL, null, msgs);
				}
				if (msgs != null) {
					msgs.dispatch();
				}
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.CANAL__DISTANCE_MODEL, oldDistanceModel, distanceModel));
				}
			}
		}
		return distanceModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DistanceModel basicGetDistanceModel() {
		return distanceModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDistanceModel(final DistanceModel newDistanceModel, NotificationChain msgs) {
		final DistanceModel oldDistanceModel = distanceModel;
		distanceModel = newDistanceModel;
		if (eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__DISTANCE_MODEL, oldDistanceModel, newDistanceModel);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDistanceModel(final DistanceModel newDistanceModel) {
		if (newDistanceModel != distanceModel) {
			NotificationChain msgs = null;
			if (distanceModel != null) {
				msgs = ((InternalEObject) distanceModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.CANAL__DISTANCE_MODEL, null, msgs);
			}
			if (newDistanceModel != null) {
				msgs = ((InternalEObject) newDistanceModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.CANAL__DISTANCE_MODEL, null, msgs);
			}
			msgs = basicSetDistanceModel(newDistanceModel, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__DISTANCE_MODEL, newDistanceModel, newDistanceModel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case PortPackage.CANAL__DISTANCE_MODEL:
			return basicSetDistanceModel(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case PortPackage.CANAL__NAME:
			return getName();
		case PortPackage.CANAL__DISTANCE_MODEL:
			if (resolve) {
				return getDistanceModel();
			}
			return basicGetDistanceModel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case PortPackage.CANAL__NAME:
			setName((String) newValue);
			return;
		case PortPackage.CANAL__DISTANCE_MODEL:
			setDistanceModel((DistanceModel) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case PortPackage.CANAL__NAME:
			setName(NAME_EDEFAULT);
			return;
		case PortPackage.CANAL__DISTANCE_MODEL:
			setDistanceModel((DistanceModel) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case PortPackage.CANAL__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case PortPackage.CANAL__DISTANCE_MODEL:
			return distanceModel != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(final int derivedFeatureID, final Class<?> baseClass) {
		if (baseClass == ScenarioObject.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
			case PortPackage.CANAL__NAME:
				return ScenarioPackage.NAMED_OBJECT__NAME;
			default:
				return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(final int baseFeatureID, final Class<?> baseClass) {
		if (baseClass == ScenarioObject.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
			case ScenarioPackage.NAMED_OBJECT__NAME:
				return PortPackage.CANAL__NAME;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(final int baseOperationID, final Class<?> baseClass) {
		if (baseClass == ScenarioObject.class) {
			switch (baseOperationID) {
			case ScenarioPackage.SCENARIO_OBJECT___GET_CONTAINER:
				return PortPackage.CANAL___GET_CONTAINER;
			default:
				return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseOperationID) {
			default:
				return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eInvoke(final int operationID, final EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case PortPackage.CANAL___GET_CONTAINER:
			return getContainer();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} // CanalImpl
