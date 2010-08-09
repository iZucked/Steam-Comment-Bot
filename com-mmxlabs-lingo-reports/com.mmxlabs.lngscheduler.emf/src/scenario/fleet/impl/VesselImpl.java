/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.fleet.FleetPackage;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.VesselImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselImpl#getClass_ <em>Class</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselImpl#getStartPort <em>Start Port</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselImpl#getEndPort <em>End Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselImpl extends EObjectImpl implements Vessel {
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
	 * The cached value of the '{@link #getClass_() <em>Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClass_()
	 * @generated
	 * @ordered
	 */
	protected VesselClass class_;

	/**
	 * The cached value of the '{@link #getStartPort() <em>Start Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartPort()
	 * @generated
	 * @ordered
	 */
	protected Port startPort;

	/**
	 * The cached value of the '{@link #getEndPort() <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndPort()
	 * @generated
	 * @ordered
	 */
	protected Port endPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselClass getClass_() {
		if (class_ != null && class_.eIsProxy()) {
			InternalEObject oldClass = (InternalEObject)class_;
			class_ = (VesselClass)eResolveProxy(oldClass);
			if (class_ != oldClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__CLASS, oldClass, class_));
			}
		}
		return class_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetClass() {
		return class_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setClass(VesselClass newClass) {
		VesselClass oldClass = class_;
		class_ = newClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__CLASS, oldClass, class_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getStartPort() {
		if (startPort != null && startPort.eIsProxy()) {
			InternalEObject oldStartPort = (InternalEObject)startPort;
			startPort = (Port)eResolveProxy(oldStartPort);
			if (startPort != oldStartPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__START_PORT, oldStartPort, startPort));
			}
		}
		return startPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetStartPort() {
		return startPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartPort(Port newStartPort) {
		Port oldStartPort = startPort;
		startPort = newStartPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__START_PORT, oldStartPort, startPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getEndPort() {
		if (endPort != null && endPort.eIsProxy()) {
			InternalEObject oldEndPort = (InternalEObject)endPort;
			endPort = (Port)eResolveProxy(oldEndPort);
			if (endPort != oldEndPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__END_PORT, oldEndPort, endPort));
			}
		}
		return endPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetEndPort() {
		return endPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndPort(Port newEndPort) {
		Port oldEndPort = endPort;
		endPort = newEndPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__END_PORT, oldEndPort, endPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL__NAME:
				return getName();
			case FleetPackage.VESSEL__CLASS:
				if (resolve) return getClass_();
				return basicGetClass();
			case FleetPackage.VESSEL__START_PORT:
				if (resolve) return getStartPort();
				return basicGetStartPort();
			case FleetPackage.VESSEL__END_PORT:
				if (resolve) return getEndPort();
				return basicGetEndPort();
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
			case FleetPackage.VESSEL__NAME:
				setName((String)newValue);
				return;
			case FleetPackage.VESSEL__CLASS:
				setClass((VesselClass)newValue);
				return;
			case FleetPackage.VESSEL__START_PORT:
				setStartPort((Port)newValue);
				return;
			case FleetPackage.VESSEL__END_PORT:
				setEndPort((Port)newValue);
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
			case FleetPackage.VESSEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case FleetPackage.VESSEL__CLASS:
				setClass((VesselClass)null);
				return;
			case FleetPackage.VESSEL__START_PORT:
				setStartPort((Port)null);
				return;
			case FleetPackage.VESSEL__END_PORT:
				setEndPort((Port)null);
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
			case FleetPackage.VESSEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case FleetPackage.VESSEL__CLASS:
				return class_ != null;
			case FleetPackage.VESSEL__START_PORT:
				return startPort != null;
			case FleetPackage.VESSEL__END_PORT:
				return endPort != null;
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
		result.append(')');
		return result.toString();
	}

} //VesselImpl
