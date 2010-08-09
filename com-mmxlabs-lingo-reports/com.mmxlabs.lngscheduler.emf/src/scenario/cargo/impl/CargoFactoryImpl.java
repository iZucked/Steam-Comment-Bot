/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.CargoModel;
import scenario.cargo.CargoPackage;
import scenario.cargo.DischargeSlot;
import scenario.cargo.LoadSlot;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoFactoryImpl extends EFactoryImpl implements CargoFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CargoFactory init() {
		try {
			CargoFactory theCargoFactory = (CargoFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf/cargo"); 
			if (theCargoFactory != null) {
				return theCargoFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CargoFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case CargoPackage.CARGO_MODEL: return createCargoModel();
			case CargoPackage.CARGO: return createCargo();
			case CargoPackage.LOAD_SLOT: return createLoadSlot();
			case CargoPackage.DISCHARGE_SLOT: return createDischargeSlot();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoModel createCargoModel() {
		CargoModelImpl cargoModel = new CargoModelImpl();
		return cargoModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Cargo createCargo() {
		CargoImpl cargo = new CargoImpl();
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot createLoadSlot() {
		LoadSlotImpl loadSlot = new LoadSlotImpl();
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot createDischargeSlot() {
		DischargeSlotImpl dischargeSlot = new DischargeSlotImpl();
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoPackage getCargoPackage() {
		return (CargoPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CargoPackage getPackage() {
		return CargoPackage.eINSTANCE;
	}

} //CargoFactoryImpl
