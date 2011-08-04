package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing MoveGeneratorSettings instances
 * @generated
 */
public  class MoveGeneratorSettingsComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

  /**
   * Call superclass constructor
     * @generated
   */
  public MoveGeneratorSettingsComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

  public MoveGeneratorSettingsComposite(final Composite container, final int style, final boolean validate) {
    this(container, style, "Move Generator Settings", validate);
  }

  /**
	 * Call superclass constructor
     * @generated
	 */
	public MoveGeneratorSettingsComposite(final Composite container, final int style) {
    this(container, style, "Move Generator Settings", true);
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
    createMoveGeneratorSettingsFields(composite, mainGroup);
  }

  /**
   * Create fields belonging to all the supertypes of MoveGeneratorSettings.
   * @generated
   */
  protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

  /**
   * Create fields belonging directly to MoveGeneratorSettings
   * @generated
   */
  protected static void createMoveGeneratorSettingsFields(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

}
