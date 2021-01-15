/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Fragment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getScenarioInstance <em>Scenario Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getFragment <em>Fragment</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getContentType <em>Content Type</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#isUseCommandStack <em>Use Command Stack</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl#getTypeHint <em>Type Hint</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioFragmentImpl extends EObjectImpl implements ScenarioFragment {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioFragmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getScenarioFragment();
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
	public ScenarioInstance getScenarioInstance() {
		return (ScenarioInstance) eGet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_ScenarioInstance(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setScenarioInstance(ScenarioInstance newScenarioInstance) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_ScenarioInstance(), newScenarioInstance);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_Name(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_Name(), newName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getFragment() {
		return (EObject) eGet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_Fragment(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFragment(EObject newFragment) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_Fragment(), newFragment);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getContentType() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_ContentType(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContentType(String newContentType) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_ContentType(), newContentType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isUseCommandStack() {
		return (Boolean) eGet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_UseCommandStack(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUseCommandStack(boolean newUseCommandStack) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_UseCommandStack(), newUseCommandStack);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTypeHint() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_TypeHint(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTypeHint(String newTypeHint) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioFragment_TypeHint(), newTypeHint);
	}

} //ScenarioFragmentImpl
