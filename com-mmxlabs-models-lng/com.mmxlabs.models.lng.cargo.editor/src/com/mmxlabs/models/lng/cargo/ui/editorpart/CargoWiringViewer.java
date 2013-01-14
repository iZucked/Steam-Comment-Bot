/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.rcp.common.actions.LockableAction;

public class CargoWiringViewer extends Composite {
	protected IScenarioEditingLocation location;
	protected ToolBarManager toolBarManager;
	private final CargoWiringComposite wiringComposite;
	private final ToolBar actionBar;

	public CargoWiringViewer(final Composite parent, final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(parent, SWT.NONE);
		this.location = location;
		actionBar = new ToolBar(this, SWT.FLAT | SWT.WRAP);

		final GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 6;
		this.setLayout(gl_shell);

		final ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// scrolledComposite.setBounds(0, 0, 400 ,400);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(true);

		// Enabling the mouse wheel to scroll
		// http://www.richclient2.de/2006_10_03/scrolledcomposite-and-the-mouse-wheel/
		scrolledComposite.addListener(SWT.Activate, new Listener() {
			public void handleEvent(final Event e) {
				scrolledComposite.setFocus();
			}
		});
		wiringComposite = new CargoWiringComposite(scrolledComposite, SWT.NONE, part.getSite()) {
			@Override
			public void layout() {
				super.layout();
				scrolledComposite.setMinSize(wiringComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}

			/**
			 * Handle new scroll positions while dragging. Coords are display coords
			 */
			@Override
			protected void requestScrollTo(final int newXpos, int newYPos) {

				// Convert into scrolledComposite local co-ordinates
				newYPos = scrolledComposite.toControl(newXpos, newYPos).y;

				final ScrollBar vScroll = scrolledComposite.getVerticalBar();
				final int increment = vScroll.getIncrement();
				if (newYPos < 5) {

					int newPos = vScroll.getSelection() - increment;
					if (newPos < vScroll.getMinimum()) {
						newPos = vScroll.getMinimum();
					}
					vScroll.setSelection(newPos);
					for (final Listener l : vScroll.getListeners((SWT.Selection))) {
						l.handleEvent(new Event());
					}
				} else {
					final int height = scrolledComposite.getClientArea().height;

					if (newYPos > height - 5) {
						int newPos = vScroll.getSelection() + increment;
						if (newPos > vScroll.getMaximum()) {
							newPos = vScroll.getMaximum();
						}
						vScroll.setSelection(newPos);
						for (final Listener l : vScroll.getListeners((SWT.Selection))) {
							l.handleEvent(new Event());
						}

					}
				}

			}
		};

		scrolledComposite.setContent(wiringComposite);
		scrolledComposite.setMinSize(wiringComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setLayout(new FillLayout());
		wiringComposite.setLocation(location);

		this.addControlListener(new ControlAdapter() {
			public void controlResized(final ControlEvent e) {
				scrolledComposite.setMinSize(wiringComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);

		wiringComposite.setCargoes(cargoModel.getCargoes(), cargoModel.getLoadSlots(), cargoModel.getDischargeSlots());

		{
			final Action repackAction = new Action("Flatten Wiring") {
				@Override
				public void run() {
					wiringComposite.setCargoes(cargoModel.getCargoes(), cargoModel.getLoadSlots(), cargoModel.getDischargeSlots());
				}
			};
			repackAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "/icons/rewire_icon.png"));
			getToolBarManager().add(repackAction);
		}

		final Action addAction = new AddAction("Add");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		getToolBarManager().add(addAction);

		getToolBarManager().update(true);
	}

	public void setLocked(final boolean locked) {
		wiringComposite.setLocked(locked);

		for (final IContributionItem item : getToolBarManager().getItems()) {
			if (item instanceof ActionContributionItem) {
				final IAction action = ((ActionContributionItem) item).getAction();
				if (action instanceof LockableAction) {
					((LockableAction) action).setLocked(locked);
				}
			}
		}
	}

	public ToolBarManager getToolBarManager() {
		if (toolBarManager == null) {
			toolBarManager = new ToolBarManager(actionBar);
		}
		return toolBarManager;
	}

	@Override
	public void dispose() {

		super.dispose();
	}

	private <T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);

		final Collection<? extends ISetting> settings = factory.createInstance(location.getRootObject(), container, reference, StructuredSelection.EMPTY);
		if (settings.isEmpty() == false) {

			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return null;
	}

	private class AddAction extends Action implements IMenuCreator {

		private Menu lastMenu;

		public AddAction(final String label) {
			super(label, IAction.AS_DROP_DOWN_MENU);
		}

		@Override
		public void dispose() {
			if ((lastMenu != null) && (lastMenu.isDisposed() == false)) {
				lastMenu.dispose();
			}
			lastMenu = null;
		}

		@Override
		public IMenuCreator getMenuCreator() {
			return this;
		}

		@Override
		public Menu getMenu(final Control parent) {
			if (lastMenu != null) {
				lastMenu.dispose();
			}
			lastMenu = new Menu(parent);

			populate(lastMenu);

			return lastMenu;
		}

		protected void addActionToMenu(final Action a, final Menu m) {
			final ActionContributionItem aci = new ActionContributionItem(a);
			aci.fill(m, -1);
		}

		/**
		 * Subclasses should fill their menu with actions here.
		 * 
		 * @param menu
		 *            the menu which is about to be displayed
		 */
		protected void populate(final Menu menu) {
			final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);

			{
				final Action newDESPurchase = new Action("New DES Purchase") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New DES Purchase");

						final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
						newLoad.setDESPurchase(true);
						newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newLoad.setOptional(true);
						newLoad.setName("");
						cmd.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

						location.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newDESPurchase, menu);
			}
			{
				final Action newLoad = new Action("New Load Slot") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New Load Slot");

						final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
						newLoad.setDESPurchase(false);
						newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newLoad.setOptional(true);
						newLoad.setName("");
						cmd.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

						location.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newFOBSale = new Action("New FOB Sale") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New FOB Sale");

						final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
						newDischarge.setFOBSale(true);
						newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newDischarge.setOptional(true);
						newDischarge.setName("");
						cmd.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));

						location.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newFOBSale, menu);

			}
			{
				final Action newDischarge = new Action("New Discharge") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New Discharge Slot");

						final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
						newDischarge.setFOBSale(false);
						newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newDischarge.setOptional(true);
						newDischarge.setName("");
						cmd.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));

						location.getEditingDomain().getCommandStack().execute(cmd);
					}
				};

				addActionToMenu(newDischarge, menu);
			}

		}

		@Override
		public Menu getMenu(final Menu parent) {
			if (lastMenu != null) {
				lastMenu.dispose();
			}
			lastMenu = new Menu(parent);

			populate(lastMenu);

			return lastMenu;
		}

	}
}
