/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.cargo;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see scenario.cargo.CargoFactory
 * @model kind="package"
 * @generated
 */
public interface CargoPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "cargo";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/cargo";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.cargo";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoPackage eINSTANCE = scenario.cargo.impl.CargoPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.cargo.impl.CargoModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.impl.CargoModelImpl
	 * @see scenario.cargo.impl.CargoPackageImpl#getCargoModel()
	 * @generated
	 */
	int CARGO_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Cargoes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CARGOES = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link scenario.cargo.impl.CargoImpl <em>Cargo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.impl.CargoImpl
	 * @see scenario.cargo.impl.CargoPackageImpl#getCargo()
	 * @generated
	 */
	int CARGO = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ID = 0;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__LOAD_SLOT = 1;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__DISCHARGE_SLOT = 2;

	/**
	 * The number of structural features of the '<em>Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_FEATURE_COUNT = 3;


	/**
	 * The meta object id for the '{@link scenario.cargo.impl.SlotImpl <em>Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.impl.SlotImpl
	 * @see scenario.cargo.impl.CargoPackageImpl#getSlot()
	 * @generated
	 */
	int SLOT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__ID = 0;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MIN_QUANTITY = 1;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MAX_QUANTITY = 2;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PORT = 3;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START = 4;

	/**
	 * The feature id for the '<em><b>Window Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_DURATION = 5;

	/**
	 * The feature id for the '<em><b>Slot Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__SLOT_DURATION = 6;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MARKET = 7;

	/**
	 * The number of structural features of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link scenario.cargo.impl.LoadSlotImpl <em>Load Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.impl.LoadSlotImpl
	 * @see scenario.cargo.impl.CargoPackageImpl#getLoadSlot()
	 * @generated
	 */
	int LOAD_SLOT = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__ID = SLOT__ID;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__MIN_QUANTITY = SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__MAX_QUANTITY = SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PORT = SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_START = SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_DURATION = SLOT__WINDOW_DURATION;

	/**
	 * The feature id for the '<em><b>Slot Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__SLOT_DURATION = SLOT__SLOT_DURATION;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__MARKET = SLOT__MARKET;

	/**
	 * The feature id for the '<em><b>Cargo CVvalue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CARGO_CVVALUE = SLOT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 1;

	/**
	 * Returns the meta object for class '{@link scenario.cargo.CargoModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.cargo.CargoModel
	 * @generated
	 */
	EClass getCargoModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.cargo.CargoModel#getCargoes <em>Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargoes</em>'.
	 * @see scenario.cargo.CargoModel#getCargoes()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_Cargoes();

	/**
	 * Returns the meta object for class '{@link scenario.cargo.Cargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo</em>'.
	 * @see scenario.cargo.Cargo
	 * @generated
	 */
	EClass getCargo();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Cargo#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see scenario.cargo.Cargo#getId()
	 * @see #getCargo()
	 * @generated
	 */
	EAttribute getCargo_Id();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.cargo.Cargo#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Load Slot</em>'.
	 * @see scenario.cargo.Cargo#getLoadSlot()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_LoadSlot();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Discharge Slot</em>'.
	 * @see scenario.cargo.Cargo#getDischargeSlot()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_DischargeSlot();

	/**
	 * Returns the meta object for class '{@link scenario.cargo.Slot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot</em>'.
	 * @see scenario.cargo.Slot
	 * @generated
	 */
	EClass getSlot();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see scenario.cargo.Slot#getId()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Id();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getMinQuantity <em>Min Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Quantity</em>'.
	 * @see scenario.cargo.Slot#getMinQuantity()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MinQuantity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Quantity</em>'.
	 * @see scenario.cargo.Slot#getMaxQuantity()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MaxQuantity();

	/**
	 * Returns the meta object for the reference '{@link scenario.cargo.Slot#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see scenario.cargo.Slot#getPort()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Port();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getWindowStart <em>Window Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Start</em>'.
	 * @see scenario.cargo.Slot#getWindowStart()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowStart();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getWindowDuration <em>Window Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Duration</em>'.
	 * @see scenario.cargo.Slot#getWindowDuration()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowDuration();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getSlotDuration <em>Slot Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Slot Duration</em>'.
	 * @see scenario.cargo.Slot#getSlotDuration()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_SlotDuration();

	/**
	 * Returns the meta object for the reference '{@link scenario.cargo.Slot#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see scenario.cargo.Slot#getMarket()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Market();

	/**
	 * Returns the meta object for class '{@link scenario.cargo.LoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Load Slot</em>'.
	 * @see scenario.cargo.LoadSlot
	 * @generated
	 */
	EClass getLoadSlot();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.LoadSlot#getCargoCVvalue <em>Cargo CVvalue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo CVvalue</em>'.
	 * @see scenario.cargo.LoadSlot#getCargoCVvalue()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_CargoCVvalue();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CargoFactory getCargoFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link scenario.cargo.impl.CargoModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.impl.CargoModelImpl
		 * @see scenario.cargo.impl.CargoPackageImpl#getCargoModel()
		 * @generated
		 */
		EClass CARGO_MODEL = eINSTANCE.getCargoModel();

		/**
		 * The meta object literal for the '<em><b>Cargoes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CARGOES = eINSTANCE.getCargoModel_Cargoes();

		/**
		 * The meta object literal for the '{@link scenario.cargo.impl.CargoImpl <em>Cargo</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.impl.CargoImpl
		 * @see scenario.cargo.impl.CargoPackageImpl#getCargo()
		 * @generated
		 */
		EClass CARGO = eINSTANCE.getCargo();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO__ID = eINSTANCE.getCargo_Id();

		/**
		 * The meta object literal for the '<em><b>Load Slot</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__LOAD_SLOT = eINSTANCE.getCargo_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__DISCHARGE_SLOT = eINSTANCE.getCargo_DischargeSlot();

		/**
		 * The meta object literal for the '{@link scenario.cargo.impl.SlotImpl <em>Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.impl.SlotImpl
		 * @see scenario.cargo.impl.CargoPackageImpl#getSlot()
		 * @generated
		 */
		EClass SLOT = eINSTANCE.getSlot();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__ID = eINSTANCE.getSlot_Id();

		/**
		 * The meta object literal for the '<em><b>Min Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__MIN_QUANTITY = eINSTANCE.getSlot_MinQuantity();

		/**
		 * The meta object literal for the '<em><b>Max Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__MAX_QUANTITY = eINSTANCE.getSlot_MaxQuantity();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__PORT = eINSTANCE.getSlot_Port();

		/**
		 * The meta object literal for the '<em><b>Window Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_START = eINSTANCE.getSlot_WindowStart();

		/**
		 * The meta object literal for the '<em><b>Window Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_DURATION = eINSTANCE.getSlot_WindowDuration();

		/**
		 * The meta object literal for the '<em><b>Slot Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__SLOT_DURATION = eINSTANCE.getSlot_SlotDuration();

		/**
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__MARKET = eINSTANCE.getSlot_Market();

		/**
		 * The meta object literal for the '{@link scenario.cargo.impl.LoadSlotImpl <em>Load Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.impl.LoadSlotImpl
		 * @see scenario.cargo.impl.CargoPackageImpl#getLoadSlot()
		 * @generated
		 */
		EClass LOAD_SLOT = eINSTANCE.getLoadSlot();

		/**
		 * The meta object literal for the '<em><b>Cargo CVvalue</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__CARGO_CVVALUE = eINSTANCE.getLoadSlot_CargoCVvalue();

	}

} //CargoPackage
