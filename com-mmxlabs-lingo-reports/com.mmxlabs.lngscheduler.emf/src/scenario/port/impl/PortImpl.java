/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port.impl;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.ScenarioPackage;
import scenario.contract.Contract;

import scenario.impl.UUIDObjectImpl;
import scenario.impl.NamedObjectImpl;

import scenario.market.Index;

import scenario.port.Port;
import scenario.port.PortPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.port.impl.PortImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.port.impl.PortImpl#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link scenario.port.impl.PortImpl#getRegasEfficiency <em>Regas Efficiency</em>}</li>
 *   <li>{@link scenario.port.impl.PortImpl#getDefaultContract <em>Default Contract</em>}</li>
 *   <li>{@link scenario.port.impl.PortImpl#getDefaultIndex <em>Default Index</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PortImpl extends UUIDObjectImpl implements Port {
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
	 * The default value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected static final String TIME_ZONE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected String timeZone = TIME_ZONE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRegasEfficiency() <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegasEfficiency()
	 * @generated
	 * @ordered
	 */
	protected static final float REGAS_EFFICIENCY_EDEFAULT = 1.0F;

	/**
	 * The cached value of the '{@link #getRegasEfficiency() <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegasEfficiency()
	 * @generated
	 * @ordered
	 */
	protected float regasEfficiency = REGAS_EFFICIENCY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDefaultContract() <em>Default Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultContract()
	 * @generated
	 * @ordered
	 */
	protected Contract defaultContract;

	/**
	 * The cached value of the '{@link #getDefaultIndex() <em>Default Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultIndex()
	 * @generated
	 * @ordered
	 */
	protected Index defaultIndex;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.PORT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index getDefaultIndex() {
		if (defaultIndex != null && defaultIndex.eIsProxy()) {
			InternalEObject oldDefaultIndex = (InternalEObject)defaultIndex;
			defaultIndex = (Index)eResolveProxy(oldDefaultIndex);
			if (defaultIndex != oldDefaultIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.PORT__DEFAULT_INDEX, oldDefaultIndex, defaultIndex));
			}
		}
		return defaultIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index basicGetDefaultIndex() {
		return defaultIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultIndex(Index newDefaultIndex) {
		Index oldDefaultIndex = defaultIndex;
		defaultIndex = newDefaultIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_INDEX, oldDefaultIndex, defaultIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getContainer() {
		return eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeZone(String newTimeZone) {
		String oldTimeZone = timeZone;
		timeZone = newTimeZone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__TIME_ZONE, oldTimeZone, timeZone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Contract getDefaultContract() {
		if (defaultContract != null && defaultContract.eIsProxy()) {
			InternalEObject oldDefaultContract = (InternalEObject)defaultContract;
			defaultContract = (Contract)eResolveProxy(oldDefaultContract);
			if (defaultContract != oldDefaultContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.PORT__DEFAULT_CONTRACT, oldDefaultContract, defaultContract));
			}
		}
		return defaultContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Contract basicGetDefaultContract() {
		return defaultContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultContract(Contract newDefaultContract) {
		Contract oldDefaultContract = defaultContract;
		defaultContract = newDefaultContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_CONTRACT, oldDefaultContract, defaultContract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getRegasEfficiency() {
		return regasEfficiency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRegasEfficiency(float newRegasEfficiency) {
		float oldRegasEfficiency = regasEfficiency;
		regasEfficiency = newRegasEfficiency;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__REGAS_EFFICIENCY, oldRegasEfficiency, regasEfficiency));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PortPackage.PORT__NAME:
				return getName();
			case PortPackage.PORT__TIME_ZONE:
				return getTimeZone();
			case PortPackage.PORT__REGAS_EFFICIENCY:
				return getRegasEfficiency();
			case PortPackage.PORT__DEFAULT_CONTRACT:
				if (resolve) return getDefaultContract();
				return basicGetDefaultContract();
			case PortPackage.PORT__DEFAULT_INDEX:
				if (resolve) return getDefaultIndex();
				return basicGetDefaultIndex();
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
			case PortPackage.PORT__NAME:
				setName((String)newValue);
				return;
			case PortPackage.PORT__TIME_ZONE:
				setTimeZone((String)newValue);
				return;
			case PortPackage.PORT__REGAS_EFFICIENCY:
				setRegasEfficiency((Float)newValue);
				return;
			case PortPackage.PORT__DEFAULT_CONTRACT:
				setDefaultContract((Contract)newValue);
				return;
			case PortPackage.PORT__DEFAULT_INDEX:
				setDefaultIndex((Index)newValue);
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
			case PortPackage.PORT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PortPackage.PORT__TIME_ZONE:
				setTimeZone(TIME_ZONE_EDEFAULT);
				return;
			case PortPackage.PORT__REGAS_EFFICIENCY:
				setRegasEfficiency(REGAS_EFFICIENCY_EDEFAULT);
				return;
			case PortPackage.PORT__DEFAULT_CONTRACT:
				setDefaultContract((Contract)null);
				return;
			case PortPackage.PORT__DEFAULT_INDEX:
				setDefaultIndex((Index)null);
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
			case PortPackage.PORT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PortPackage.PORT__TIME_ZONE:
				return TIME_ZONE_EDEFAULT == null ? timeZone != null : !TIME_ZONE_EDEFAULT.equals(timeZone);
			case PortPackage.PORT__REGAS_EFFICIENCY:
				return regasEfficiency != REGAS_EFFICIENCY_EDEFAULT;
			case PortPackage.PORT__DEFAULT_CONTRACT:
				return defaultContract != null;
			case PortPackage.PORT__DEFAULT_INDEX:
				return defaultIndex != null;
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
		if (baseClass == ScenarioObject.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case PortPackage.PORT__NAME: return ScenarioPackage.NAMED_OBJECT__NAME;
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
		if (baseClass == ScenarioObject.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case ScenarioPackage.NAMED_OBJECT__NAME: return PortPackage.PORT__NAME;
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
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == ScenarioObject.class) {
			switch (baseOperationID) {
				case ScenarioPackage.SCENARIO_OBJECT___GET_CONTAINER: return PortPackage.PORT___GET_CONTAINER;
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case PortPackage.PORT___GET_CONTAINER:
				return getContainer();
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
		result.append(", timeZone: ");
		result.append(timeZone);
		result.append(", regasEfficiency: ");
		result.append(regasEfficiency);
		result.append(')');
		return result.toString();
	}

} //PortImpl
