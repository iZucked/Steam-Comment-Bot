package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPartSite;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.presentation.composites.SlotInlineEditorWrapper;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;

public class PortAndDateComposite extends Composite implements IDisplayComposite, IInlineEditorContainer {

	private Slot slot;
	protected final LinkedList<IInlineEditor> editors = new LinkedList<IInlineEditor>();
	private ICommandHandler commandHandler;
	private MenuManager menuManager;

	public PortAndDateComposite(final Composite parent, final int style, IWorkbenchPartSite site, boolean isLoad) {
		super(parent, style);

		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.marginHeight = 0;
//		gridLayout.marginBottom = 0;
//		gridLayout.marginTop = 0;
		gridLayout.horizontalSpacing = 1;
		gridLayout.verticalSpacing = 0;
		if(isLoad){
			gridLayout.marginRight = 5;
		} else{
			gridLayout.marginLeft = 5;
		}
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		addInlineEditor(ComponentHelperUtils.createDefaultEditor(MMXCorePackage.eINSTANCE.getNamedObject(), MMXCorePackage.Literals.NAMED_OBJECT__NAME));
		addInlineEditor(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__CONTRACT));
		addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__PORT)));
		addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__WINDOW_START)));

		// Create a context menu for this control. Menu items will be populated using the IMenuListeners add using #addMenuListener
		menuManager = new MenuManager("#PopupMenu");
		site.registerContextMenu(menuManager, site.getSelectionProvider());
		menuManager.setRemoveAllWhenShown(true);
		Menu m = menuManager.createContextMenu(this);
		this.setMenu(m);
	}

	@Override
	public void dispose() {

		slot = null;

		super.dispose();
	}

	public void addMenuListener(IMenuListener l) {
		menuManager.addMenuListener(l);
	}

	public void removeMenuListener(IMenuListener l) {
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

	public Contract getContract() {
		if (slot == null) {
			return null;
		}
		return slot.getContract();
	}

	@Override
	public void addInlineEditor(final IInlineEditor editor) {
		editors.add(editor);
		editor.setCommandHandler(commandHandler);
		Control control = editor.createControl(this);
		int column = editors.size();
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd.verticalIndent = 0;
		switch(column){
			case 0:
				gd.widthHint = 120; // ID
			case 1:
				gd.widthHint = 120; // contract
			case 2:
				gd.widthHint = 120; // port
			case 3:
				gd.widthHint = 120; // date
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
		if (value instanceof Slot) {
			slot = (Slot) value;
			this.setVisible(true);
		} else {
			slot = null;
			this.setVisible(false);
		}
		for (final IInlineEditor editor : editors) {
			editor.setCommandHandler(commandHandler);
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
		for (final IInlineEditor editor : editors) {
			editor.processValidation(status);
		}
	}

}