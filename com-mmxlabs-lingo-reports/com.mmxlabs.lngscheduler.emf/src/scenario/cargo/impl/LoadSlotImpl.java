package scenario.cargo.impl;


import java.lang.reflect.InvocationTargetException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getCargoCVvalue <em>Cargo CVvalue</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LoadSlotImpl extends SlotImpl implements LoadSlot {
	/**
	 * The default value of the '{@link #getCargoCVvalue() <em>Cargo CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCVvalue()
	 * @generated
	 * @ordered
	 */
	protected static final float CARGO_CVVALUE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getCargoCVvalue() <em>Cargo CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCVvalue()
	 * @generated
	 * @ordered
	 */
	protected float cargoCVvalue = CARGO_CVVALUE_EDEFAULT;

	/**
	 * This is true if the Cargo CVvalue attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cargoCVvalueESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LoadSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.LOAD_SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getCargoCVvalue() {
		return cargoCVvalue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoCVvalue(float newCargoCVvalue) {
		float oldCargoCVvalue = cargoCVvalue;
		cargoCVvalue = newCargoCVvalue;
		boolean oldCargoCVvalueESet = cargoCVvalueESet;
		cargoCVvalueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__CARGO_CVVALUE, oldCargoCVvalue, cargoCVvalue, !oldCargoCVvalueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCargoCVvalue() {
		float oldCargoCVvalue = cargoCVvalue;
		boolean oldCargoCVvalueESet = cargoCVvalueESet;
		cargoCVvalue = CARGO_CVVALUE_EDEFAULT;
		cargoCVvalueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.LOAD_SLOT__CARGO_CVVALUE, oldCargoCVvalue, CARGO_CVVALUE_EDEFAULT, oldCargoCVvalueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCargoCVvalue() {
		return cargoCVvalueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getCargoOrPortCVValue() {
		if (isSetCargoCVvalue()) 
			return getCargoCVvalue();
		else if (getPort()!=null)
			return getPort().getDefaultCVvalue();
		else
			return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.LOAD_SLOT__CARGO_CVVALUE:
				return getCargoCVvalue();
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
			case CargoPackage.LOAD_SLOT__CARGO_CVVALUE:
				setCargoCVvalue((Float)newValue);
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
			case CargoPackage.LOAD_SLOT__CARGO_CVVALUE:
				unsetCargoCVvalue();
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
			case CargoPackage.LOAD_SLOT__CARGO_CVVALUE:
				return isSetCargoCVvalue();
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
			case CargoPackage.LOAD_SLOT___GET_CARGO_OR_PORT_CV_VALUE:
				return getCargoOrPortCVValue();
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
		result.append(" (cargoCVvalue: ");
		if (cargoCVvalueESet) result.append(cargoCVvalue); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //LoadSlotImpl