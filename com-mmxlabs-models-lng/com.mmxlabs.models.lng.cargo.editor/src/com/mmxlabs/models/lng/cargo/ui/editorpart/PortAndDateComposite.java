/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPartSite;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.presentation.composites.SlotInlineEditorWrapper;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.dates.DateInlineEditor;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;

public class PortAndDateComposite extends Composite implements IDisplayComposite, IInlineEditorContainer {

	private Slot slot;
	protected final LinkedList<IInlineEditor> editors = new LinkedList<IInlineEditor>();
	private ICommandHandler commandHandler;
	private final MenuManager menuManager;
	private Button ctxButton;
	private boolean isLoad;

	public PortAndDateComposite(final Composite parent, final int style, final IWorkbenchPartSite site, final boolean isLoad) {
		super(parent, style);
		this.isLoad = isLoad;

		final GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginHeight = 0;
		// gridLayout.marginBottom = 0;
		// gridLayout.marginTop = 0;
		this.isLoad = isLoad;

		// 7 for validation markers
		gridLayout.horizontalSpacing = 7;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginLeft = 7;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		if (!isLoad) {
			ctxButton = new Button(this, SWT.PUSH);
			ctxButton.setText("<");
		}
		if (isLoad) {
			addInlineEditor(ComponentHelperUtils.createDefaultEditor(MMXCorePackage.eINSTANCE.getNamedObject(), MMXCorePackage.Literals.NAMED_OBJECT__NAME));
			addInlineEditor(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__CONTRACT));
//			addInlineEditor(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__MAX_QUANTITY));
			addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__PORT)));
			addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__WINDOW_START)));
			ctxButton = new Button(this, SWT.PUSH);
			ctxButton.setText(">");
		}
		else{
			addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__WINDOW_START)));			
			addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__PORT)));
//			addInlineEditor(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__MAX_QUANTITY));
			addInlineEditor(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__CONTRACT));
			addInlineEditor(ComponentHelperUtils.createDefaultEditor(MMXCorePackage.eINSTANCE.getNamedObject(), MMXCorePackage.Literals.NAMED_OBJECT__NAME));
		}

		// Create a context menu for this control. Menu items will be populated using the IMenuListeners add using #addMenuListener
		menuManager = new MenuManager("#PopupMenu");
		site.registerContextMenu(menuManager, site.getSelectionProvider());
		menuManager.setRemoveAllWhenShown(true);
		final Menu m = menuManager.createContextMenu(this);
		this.setMenu(m);
		ctxButton.setMenu(m);
		ctxButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				m.setLocation(ctxButton.toDisplay(e.x, e.y));
				m.setVisible(true);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});
	}

	@Override
	public void dispose() {

		slot = null;
		commandHandler = null;

		editors.clear();
		super.dispose();
	}

	public void addMenuListener(final IMenuListener l) {
		menuManager.addMenuListener(l);
	}

	public void removeMenuListener(final IMenuListener l) {
		menuManager.removeMenuListener(l);
	}

	public Date getDate() {
		if (slot == null) {
			return null;
		}
		return slot.getWindowStart();
	}

	public Port getPort() {
		if (slot == null) {
			return null;
		}
		return slot.getPort();
	}

//	public Contract getContract() {
//		if (slot == null) {
//			return null;
//		}
//		return slot.getContract();
//	}

	@Override
	public void addInlineEditor(final IInlineEditor editor) {
		editors.add(editor);
		editor.setCommandHandler(commandHandler);
		final Control control = editor.createControl(this);
		final int column = editors.size();

		int hAlignment = isLoad ? SWT.LEFT : SWT.RIGHT;

		final GridData gd = new GridData(hAlignment, SWT.FILL, false, false);
		gd.verticalIndent = 0;
		if(isLoad){
			switch (column) {
			case 0:
				gd.widthHint = 60; // ID
			case 1:
				gd.widthHint = 60; // contract
			case 2:
				gd.widthHint = 60; // volume				
			case 3:
				gd.widthHint = 60; // port
			case 4:
				gd.widthHint = 60; // date
				
			}
		} else {
			switch (column) {
			case 0:
				gd.widthHint = 60; // date
			case 1:
				gd.widthHint = 60; // port
			case 2:
				gd.widthHint = 60; // volume
			case 3:
				gd.widthHint = 60; // contract
			case 4:
				gd.widthHint = 60; // ID
			}			
		}
		control.setLayoutData(gd);
		control.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject value, final Collection<EObject> range) {

		if (isDisposed()) {
			return;
		}
		if (value instanceof Slot) {
			slot = (Slot) value;
			this.setVisible(true);
		} else {
			slot = null;
			this.setVisible(false);
		}

		for (final IInlineEditor editor : editors) {
			editor.setCommandHandler(commandHandler);

			// For spot slots, show a slightly different date format.
			IInlineEditor instance = editor;
			if (editor instanceof SlotInlineEditorWrapper) {
				instance = ((SlotInlineEditorWrapper) editor).getWrapped();
			}

			if (instance instanceof DateInlineEditor) {
				final DateInlineEditor inlineEditor = (DateInlineEditor) instance;
				// FIXME: Locale!
				if (slot instanceof SpotSlot) {
					inlineEditor.setDateFormat("MM/yyyy");
				} else {
					inlineEditor.setDateFormat("dd/MM/yyyy");
				}
			}
			editor.display(location, root, value, range);
		}

	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {

	}

	@Override
	public void displayValidationStatus(final IStatus status) {

		if (isDisposed()) {
			return;
		}

		for (final IInlineEditor editor : editors) {
			editor.processValidation(status);
		}
	}

	public void setEditorEnabled(final boolean enabled) {
		for (final IInlineEditor editor : editors) {
			editor.setEditorEnabled(enabled);
		}
	}

	public void setEditorLocked(final boolean enabled) {
		for (final IInlineEditor editor : editors) {
			editor.setEditorLocked(enabled);
		}
	}

	public Slot getSlot() {
		return slot;
	}

}