/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#getElements <em>Elements</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#isArchived <em>Archived</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl#isHidden <em>Hidden</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ContainerImpl extends EObjectImpl implements Container {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Container getParent() {
		return (Container) eGet(ScenarioServicePackage.eINSTANCE.getContainer_Parent(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParent(Container newParent) {
		eSet(ScenarioServicePackage.eINSTANCE.getContainer_Parent(), newParent);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EList<Container> getElements() {
		return (EList<Container>) eGet(ScenarioServicePackage.eINSTANCE.getContainer_Elements(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isArchived() {
		return (Boolean) eGet(ScenarioServicePackage.eINSTANCE.getContainer_Archived(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setArchived(boolean newArchived) {
		eSet(ScenarioServicePackage.eINSTANCE.getContainer_Archived(), newArchived);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getContainer_Name(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		eSet(ScenarioServicePackage.eINSTANCE.getContainer_Name(), newName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isHidden() {
		return (Boolean) eGet(ScenarioServicePackage.eINSTANCE.getContainer_Hidden(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHidden(boolean newHidden) {
		eSet(ScenarioServicePackage.eINSTANCE.getContainer_Hidden(), newHidden);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getContainedInstanceCount() {
		int accumulator = 0;
		for (final Container container : getElements()) {
			accumulator += container.getContainedInstanceCount();
		}
		return accumulator;
	}

} //ContainerImpl
