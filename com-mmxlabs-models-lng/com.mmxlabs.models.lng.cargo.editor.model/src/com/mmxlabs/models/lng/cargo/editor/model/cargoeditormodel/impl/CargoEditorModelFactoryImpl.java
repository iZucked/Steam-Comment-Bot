/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoEditorModelFactoryImpl extends EFactoryImpl implements CargoEditorModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CargoEditorModelFactory init() {
		try {
			CargoEditorModelFactory theCargoEditorModelFactory = (CargoEditorModelFactory)EPackage.Registry.INSTANCE.getEFactory(CargoEditorModelPackage.eNS_URI);
			if (theCargoEditorModelFactory != null) {
				return theCargoEditorModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CargoEditorModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoEditorModelFactoryImpl() {
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
			case CargoEditorModelPackage.TRADES_ROW: return createTradesRow();
			case CargoEditorModelPackage.TRADES_TABLE_ROOT: return createTradesTableRoot();
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
	public TradesRow createTradesRow() {
		TradesRowImpl tradesRow = new TradesRowImpl();
		return tradesRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TradesTableRoot createTradesTableRoot() {
		TradesTableRootImpl tradesTableRoot = new TradesTableRootImpl();
		return tradesTableRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoEditorModelPackage getCargoEditorModelPackage() {
		return (CargoEditorModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CargoEditorModelPackage getPackage() {
		return CargoEditorModelPackage.eINSTANCE;
	}

} //CargoEditorModelFactoryImpl
