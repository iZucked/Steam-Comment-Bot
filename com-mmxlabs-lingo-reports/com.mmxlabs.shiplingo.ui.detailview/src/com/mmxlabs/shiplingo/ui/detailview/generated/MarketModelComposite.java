/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.market.MarketPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing MarketModel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class MarketModelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public MarketModelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public MarketModelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Market Model", validate);
	}

	public MarketModelComposite(final Composite container, final int style) {
		this(container, style, "Market Model", true);
	}

	/**
	 * Create the main contents
	 * @generated
	 */
	protected void createContents(final Composite group) {
    final Composite mainGroup;

    if (group == null) {
      mainGroup = createGroup(this, mainGroupTitle);
    } else {
      mainGroup = group;
    }
    
    super.createContents(mainGroup);		

    createFields(this, mainGroup);
  }

	/**
	 * @generated
	 */
	protected static void createFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFieldsFromSupers(composite, mainGroup);
    createMarketModelFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of MarketModel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to MarketModel
	 * @generated
	 */
	protected static void createMarketModelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createIndicesEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the indices feature on MarketModel
	 * @generated
	 */
	protected static void createIndicesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(MarketPackage.eINSTANCE.getMarketModel_Indices()),
      "Indices");
  }
}
