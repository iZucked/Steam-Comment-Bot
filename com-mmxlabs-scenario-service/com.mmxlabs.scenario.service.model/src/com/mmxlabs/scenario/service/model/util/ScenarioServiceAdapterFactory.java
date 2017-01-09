/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util;

import com.mmxlabs.scenario.service.model.*;
import java.io.Closeable;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage
 * @generated
 */
public class ScenarioServiceAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ScenarioServicePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioServiceAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ScenarioServicePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioServiceSwitch<Adapter> modelSwitch = new ScenarioServiceSwitch<Adapter>() {
		@Override
		public Adapter caseScenarioModel(ScenarioModel object) {
			return createScenarioModelAdapter();
		}

		@Override
		public Adapter caseContainer(Container object) {
			return createContainerAdapter();
		}

		@Override
		public Adapter caseFolder(Folder object) {
			return createFolderAdapter();
		}

		@Override
		public Adapter caseScenarioService(ScenarioService object) {
			return createScenarioServiceAdapter();
		}

		@Override
		public Adapter caseScenarioInstance(ScenarioInstance object) {
			return createScenarioInstanceAdapter();
		}

		@Override
		public Adapter caseMetadata(Metadata object) {
			return createMetadataAdapter();
		}

		@Override
		public Adapter caseScenarioLock(ScenarioLock object) {
			return createScenarioLockAdapter();
		}

		@Override
		public Adapter caseScenarioFragment(ScenarioFragment object) {
			return createScenarioFragmentAdapter();
		}

		@Override
		public Adapter caseModelReference(ModelReference object) {
			return createModelReferenceAdapter();
		}

		@Override
		public Adapter caseCloseable(Closeable object) {
			return createCloseableAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.ScenarioModel <em>Scenario Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.ScenarioModel
	 * @generated
	 */
	public Adapter createScenarioModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.Container <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.Container
	 * @generated
	 */
	public Adapter createContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.Folder <em>Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.Folder
	 * @generated
	 */
	public Adapter createFolderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.ScenarioService <em>Scenario Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService
	 * @generated
	 */
	public Adapter createScenarioServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.ScenarioInstance <em>Scenario Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance
	 * @generated
	 */
	public Adapter createScenarioInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.Metadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.Metadata
	 * @generated
	 */
	public Adapter createMetadataAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.ScenarioLock <em>Scenario Lock</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.ScenarioLock
	 * @generated
	 */
	public Adapter createScenarioLockAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.ScenarioFragment <em>Scenario Fragment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment
	 * @generated
	 */
	public Adapter createScenarioFragmentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.scenario.service.model.ModelReference <em>Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.scenario.service.model.ModelReference
	 * @generated
	 */
	public Adapter createModelReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.io.Closeable <em>Closeable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see java.io.Closeable
	 * @generated
	 */
	public Adapter createCloseableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //ScenarioServiceAdapterFactory
