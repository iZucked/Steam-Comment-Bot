/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class EENumInlineEditor extends BasicAttributeInlineEditor {
	private final EEnum eenum;
	private Combo combo;

	public EENumInlineEditor(EMFPath path, EAttribute feature,
			EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
		this.eenum = (EEnum) feature.getEAttributeType();	}

	@Override
	public Control createControl(Composite parent) {
		final Combo combo = new Combo(parent, SWT.READ_ONLY);
		for (final EEnumLiteral literal : eenum.getELiterals()) {
			combo.add(literal.getName());
		}
		
		this.combo = combo;
		
		combo.addSelectionListener(
				new SelectionListener() {
					{
						final SelectionListener sl = this;
						combo.addDisposeListener(
								new DisposeListener() {									
									@Override
									public void widgetDisposed(final DisposeEvent e) {
										combo.removeSelectionListener(sl);
									}
								});
					}
					@Override
					public void widgetSelected(SelectionEvent e) {
						doSetValue(eenum.getEEnumLiteral(combo.getText()).getInstance());
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {}
				});
		
		return super.createControl(parent);
	}

	@Override
	protected void updateDisplay(Object value) {
		if (combo.isDisposed()) return;
		combo.setText(((Enumerator) value).getName());
	}

}
