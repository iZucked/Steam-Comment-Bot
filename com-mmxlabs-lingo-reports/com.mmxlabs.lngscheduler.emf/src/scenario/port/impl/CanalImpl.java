/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.port.Canal;
import scenario.port.PartialDistance;
import scenario.port.DistanceLine;
import scenario.port.Port;
import scenario.port.PortPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Canal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.port.impl.CanalImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.port.impl.CanalImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link scenario.port.impl.CanalImpl#getEntryDistances <em>Entry Distances</em>}</li>
 *   <li>{@link scenario.port.impl.CanalImpl#getExitDistances <em>Exit Distances</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CanalImpl extends EObjectImpl implements Canal {
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
	 * The default value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected static final int DISTANCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected int distance = DISTANCE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEntryDistances() <em>Entry Distances</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryDistances()
	 * @generated
	 * @ordered
	 */
	protected EList<PartialDistance> entryDistances;

	/**
	 * The cached value of the '{@link #getExitDistances() <em>Exit Distances</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExitDistances()
	 * @generated
	 * @ordered
	 */
	protected EList<PartialDistance> exitDistances;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.CANAL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDistance(int newDistance) {
		int oldDistance = distance;
		distance = newDistance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__DISTANCE, oldDistance, distance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PartialDistance> getEntryDistances() {
		if (entryDistances == null) {
			entryDistances = new EObjectContainmentEList<PartialDistance>(PartialDistance.class, this, PortPackage.CANAL__ENTRY_DISTANCES);
		}
		return entryDistances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PartialDistance> getExitDistances() {
		if (exitDistances == null) {
			exitDistances = new EObjectContainmentEList<PartialDistance>(PartialDistance.class, this, PortPackage.CANAL__EXIT_DISTANCES);
		}
		return exitDistances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.CANAL__ENTRY_DISTANCES:
				return ((InternalEList<?>)getEntryDistances()).basicRemove(otherEnd, msgs);
			case PortPackage.CANAL__EXIT_DISTANCES:
				return ((InternalEList<?>)getExitDistances()).basicRemove(otherEnd, msgs);
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
			case PortPackage.CANAL__NAME:
				return getName();
			case PortPackage.CANAL__DISTANCE:
				return getDistance();
			case PortPackage.CANAL__ENTRY_DISTANCES:
				return getEntryDistances();
			case PortPackage.CANAL__EXIT_DISTANCES:
				return getExitDistances();
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
			case PortPackage.CANAL__NAME:
				setName((String)newValue);
				return;
			case PortPackage.CANAL__DISTANCE:
				setDistance((Integer)newValue);
				return;
			case PortPackage.CANAL__ENTRY_DISTANCES:
				getEntryDistances().clear();
				getEntryDistances().addAll((Collection<? extends PartialDistance>)newValue);
				return;
			case PortPackage.CANAL__EXIT_DISTANCES:
				getExitDistances().clear();
				getExitDistances().addAll((Collection<? extends PartialDistance>)newValue);
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
			case PortPackage.CANAL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PortPackage.CANAL__DISTANCE:
				setDistance(DISTANCE_EDEFAULT);
				return;
			case PortPackage.CANAL__ENTRY_DISTANCES:
				getEntryDistances().clear();
				return;
			case PortPackage.CANAL__EXIT_DISTANCES:
				getExitDistances().clear();
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
			case PortPackage.CANAL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PortPackage.CANAL__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
			case PortPackage.CANAL__ENTRY_DISTANCES:
				return entryDistances != null && !entryDistances.isEmpty();
			case PortPackage.CANAL__EXIT_DISTANCES:
				return exitDistances != null && !exitDistances.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", distance: ");
		result.append(distance);
		result.append(')');
		return result.toString();
	}

} //CanalImpl
