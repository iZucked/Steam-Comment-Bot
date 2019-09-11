/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editorpart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.ContractNomination;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.SlotNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanFlagAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.DateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class RelativeDateRangeNominationsViewerPane extends AbstractNominationsViewerPane implements ISelectionListener {

	final class NominationsScenarioTableViewer extends ScenarioTableViewer {
		NominationsScenarioTableViewer(Composite parent, int style, IScenarioEditingLocation part) {
			super(parent, style, part);
		}

		/**
		 * Callback to convert the raw data coming out of the table into something usable externally. 
		 * This is useful when the table data model is custom for the table rather from the real data model.
		 */
		protected List<?> adaptSelectionFromWidget(final List<?> selection) {
			//Ensures Cargo in Trades view gets selected when relevant nomination is selected.
			List<Object> equivalentObjects = new LinkedList<>();

			final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(jointModelEditor.getScenarioDataProvider());

			if (scenarioModel != null) {
				for (final Object e : selection) {
					equivalentObjects.add(e);
					if (e instanceof SlotNomination) {
						Slot<?> slot = NominationsModelUtils.findSlot(scenarioModel, (SlotNomination)e);
						if (slot != null) {
							equivalentObjects.add(slot);
						}
					}
					else if (e instanceof ContractNomination) {
						Contract contract = NominationsModelUtils.findContract(scenarioModel, (ContractNomination)e);
						if (contract != null) {
							equivalentObjects.add(contract);
						}
					}
				}
			}

			return equivalentObjects;
		}

		@Override
		public void setSelection(ISelection selection, boolean reveal) {
			if (selection instanceof IStructuredSelection) {
				List<?> l = ((IStructuredSelection) selection).toList();
				Set<Object> newSelection = new HashSet<>();
				for (Object o : l) {
					for (var e : equivalents.entrySet()) {
						if (Objects.equals(o, e.getKey())) {
							newSelection.addAll(e.getValue());
						}
					}
				}
				newSelection.remove(null);
				StructuredSelection s = new StructuredSelection(new ArrayList<>(newSelection));
				super.setSelection(s, reveal);
			} else {
				super.setSelection(selection, reveal);
			}
		}

		/**
		 * Modify @link {AbstractTreeViewer#getTreePathFromItem(Item)} to adapt items before returning selection object.
		 */
		@Override
		protected TreePath getTreePathFromItem(Item item) {
			final LinkedList<Object> segments = new LinkedList<>();
			while (item != null) {
				final Object segment = item.getData();
				Assert.isNotNull(segment);
				segments.addFirst(segment);
				item = getParentItem(item);
			}
			final List<?> l = adaptSelectionFromWidget(segments);

			return new TreePath(l.toArray());
		}

		public ISelection getOriginalSelection() {
			Control control = getControl();
			if (control == null || control.isDisposed()) {
				return TreeSelection.EMPTY;
			}
			Widget[] items = getSelection(getControl());
			ArrayList<TreePath> list = new ArrayList<>(items.length);
			for (Widget item : items) {
				if (item.getData() != null) {
					list.add(super.getTreePathFromItem((Item) item));
				}
			}
			return new TreeSelection(list.toArray(new TreePath[list.size()]), getComparer());
		}

		@Override
		public void init(final AdapterFactory adapterFactory, final ModelReference modelReference, final EReference... path) {
			super.init(adapterFactory, modelReference, path);

			init(new ITreeContentProvider() {

				@Override
				public void dispose() {
					// Nothing special to do here
				}

				@Override
				public Object[] getElements(final Object inputElement) {
					final List<AbstractNomination> elements = new LinkedList<>();

					clearInputEquivalents();

					if (inputElement instanceof LNGScenarioModel) {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) inputElement;
						final NominationsModel nominationsModel = scenarioModel.getNominationsModel();
						if (nominationsModel != null) {
							elements.addAll(nominationsModel.getNominations());
						}
						setCurrentContainerAndContainment(nominationsModel, null);
					}

					// Add in extra generated nominations
					final LocalDate startDate = getStartDate();
					final LocalDate endDate = getEndDate();
					final Object[] result;

					elements.addAll(NominationsModelUtils.generateNominations(jointModelEditor, startDate, endDate));
					if (RelativeDateRangeNominationsViewerPane.this.viewSelected && !RelativeDateRangeNominationsViewerPane.this.selectedSlots.isEmpty()) {
						result = elements.stream().filter(n -> n.getNomineeId() != null && isSelectedSlot(n.getNomineeId())).toArray();
					}
					else {
						result = elements.toArray();
					}

					final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(jointModelEditor.getScenarioDataProvider());

					if (scenarioModel != null) {
						for (final Object e : result) {

							if (e instanceof SlotNomination) {
								Slot<?> slot = NominationsModelUtils.findSlot(scenarioModel, (SlotNomination)e);
								setInputEquivalents(e, Collections.singleton(slot));
							}
							else if (e instanceof ContractNomination) {
								Contract contract = NominationsModelUtils.findContract(scenarioModel, (ContractNomination)e);
								setInputEquivalents(e, Collections.singleton(contract));
							}
						}
					}

					return result;
				}

				private boolean isSelectedSlot(String nomineeId) {
					for (Slot<?> slot : RelativeDateRangeNominationsViewerPane.this.selectedSlots) {
						if (Objects.equals(slot.getName(), nomineeId)) {
							return true;
						}
					}
					return false;
				}

				@Override
				public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

				}

				@Override
				public Object[] getChildren(final Object parentElement) {
					return null;
					//Below fixes the NullPointerExceptions, but everything else to do with the selection then breaks.
					//return new Object[0];
				}

				@Override
				public Object getParent(final Object element) {
					return null;
				}

				@Override
				public boolean hasChildren(final Object element) {
					return false;
				}

			}, modelReference);
		}

		@Override
		protected void doCommandStackChanged() {
			ViewerHelper.refresh(this, true);
		}
	}
	
	private static final String PLUGIN_ID = "com.mmxlabs.models.lng.nominations.editor";
	
	@NonNull 
	private final IScenarioEditingLocation jointModelEditor;
	
	private NominationDatesToolbarEditor nominationDatesToolbarEditor;
	
	private RelativeDateRangeToolbarEditor relativeDateToolbarEditor;
	
	private boolean includeDone = false;
	private boolean viewSelected = false;
	private final List<Slot<?>> selectedSlots = new ArrayList<>();
	private final HashSet<String> previousNominations = new HashSet<>();
	private final Color colourPink = new Color(Display.getDefault(), 254, 127, 156);
	private final Color colourGreen = new Color(Display.getDefault(), 80, 220, 100);
	private final Color colourLightYellow = new Color(Display.getDefault(), 254, 254, 200);
	
	private final Map<Object, HashSet<Object>> equivalents = new HashMap<>();
	private final Set<Object> contents = new HashSet<>();
	
	public RelativeDateRangeNominationsViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, @NonNull final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
		final ESelectionService service = PlatformUI.getWorkbench().getService(ESelectionService.class);
		service.addPostSelectionListener(this);
	}

	protected Action createAddAction(final EReference containment) {
		super.createAddAction(containment);
		final Action duplicateAction = createDuplicateAction();
		final Action[] extraActions = duplicateAction == null ? new Action[0] : new Action[] { duplicateAction };
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), extraActions, this.getViewer());
	}
	
	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		final ToolBarManager toolbar = getToolBarManager();

		nominationDatesToolbarEditor = new NominationDatesToolbarEditor("nomination_dates_toolbar",
				scenarioEditingLocation.getEditingDomain(), jointModelEditor);
		toolbar.appendToGroup(EDIT_GROUP, nominationDatesToolbarEditor);
		
		relativeDateToolbarEditor = new RelativeDateRangeToolbarEditor(this, "nomination_relative_date_toolbar", jointModelEditor);
		toolbar.appendToGroup(EDIT_GROUP, relativeDateToolbarEditor);
		
		final Action refreshButton = new Action("Refresh", Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				RelativeDateRangeNominationsViewerPane.this.pruneNominationsMarkedAsDone();
				viewer.refresh();
			}
		};
	   
		final ImageDescriptor refreshImage = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, "/icons/iu_update_obj.gif");
		refreshButton.setImageDescriptor(refreshImage);
		toolbar.appendToGroup(EDIT_GROUP, refreshButton);
		toolbar.update(true);

		final Action includeDoneToggle = new Action("Include done", Action.AS_CHECK_BOX) {
			@Override
			public void run() {
				RelativeDateRangeNominationsViewerPane.this.includeDone = !RelativeDateRangeNominationsViewerPane.this.includeDone;
				this.setChecked(RelativeDateRangeNominationsViewerPane.this.includeDone);
				viewer.refresh();
			}
		};
		includeDoneToggle.setChecked(this.includeDone);
		this.getMenuManager().add(includeDoneToggle);
		
		final Action viewSelectedToggle = new Action("View selected", Action.AS_CHECK_BOX) {
			@Override
			public void run() {
				RelativeDateRangeNominationsViewerPane.this.viewSelected = !RelativeDateRangeNominationsViewerPane.this.viewSelected;
				this.setChecked(RelativeDateRangeNominationsViewerPane.this.viewSelected);
				viewer.refresh();
			}			
		};
		viewSelectedToggle.setChecked(this.viewSelected);
		this.getMenuManager().add(viewSelectedToggle);

		this.getMenuManager().update(true);
		
		addTypicalColumn("Nominee Id", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(
				new BasicAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId(), jointModelEditor.getEditingDomain())));
		addTypicalColumn("From", new StringAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId(), jointModelEditor.getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				return false;
			}

			@Override
			public String render(final Object object) {
				return NominationsModelUtils.getFrom(getScenarioModel(), (AbstractNomination) object);
			}
		});
		addTypicalColumn("To", new StringAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId(), jointModelEditor.getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				return false;
			}

			@Override
			public String render(final Object object) {
				return NominationsModelUtils.getTo(getScenarioModel(), (AbstractNomination) object);
			}
		});
		addTypicalColumn("Cn", new StringAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId(), jointModelEditor.getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				return false;
			}

			@Override
			public String render(final Object object) {
				return NominationsModelUtils.getCN(getScenarioModel(), (AbstractNomination) object);
			}
		});
		addTypicalColumn("Side", new StringAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId(), jointModelEditor.getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				return false;
			}

			@Override
			public String render(final Object object) {
				return NominationsModelUtils.getSide((AbstractNomination) object);
			}
		});
		addTypicalColumn("Date", new DateTimeAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_NomineeId(), jointModelEditor.getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				return false;
			}

			@Override
			public String renderSetValue(final Object owner, final Object object) {
				final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
				formatter.setValue(NominationsModelUtils.getDate(getScenarioModel(), (AbstractNomination) owner));
				return formatter.getDisplayString();
			}

			@Override
			public Object getValue(final Object object) {
				final AbstractNomination n = (AbstractNomination) object;
				if (n != null) {
					final LocalDate date = NominationsModelUtils.getDate(getScenarioModel(), n);
					return date;
				} else {
					return null;
				}
			}
		});
		addTypicalColumn("Nomination Type", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(
				new BasicAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_Type(), jointModelEditor.getEditingDomain())));
		addTypicalColumn("Due Date", new DateTimeAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_DueDate(), jointModelEditor.getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				return false;
			}

			@Override
			public String renderSetValue(final Object owner, final Object object) {
				final AbstractNomination n = (AbstractNomination) owner;
				if (n != null) {
					final LocalDate dueDate = NominationsModelUtils.getDueDate(getScenarioModel(), n);
					final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
					formatter.setValue(dueDate);
					return formatter.getDisplayString();
				}
				return "";
			}

			@Override
			public Object getValue(final Object object) {
				final AbstractNomination n = (AbstractNomination) object;
				if (n != null) {
					final LocalDate dueDate = NominationsModelUtils.getDueDate(getScenarioModel(), n);
					return dueDate;
				} else {
					return null;
				}
			}
		});
		final GridViewerColumn gcvDone = addTypicalColumn("Done", new BooleanFlagAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_Done(), jointModelEditor.getEditingDomain()) {
			@Override
			public void doSetValue(Object object, Object value) {
				final AbstractNomination nomination = (AbstractNomination)object;
				if (nomination.eContainer() == null) {
					addNomination(nomination);	
				}
				RelativeDateRangeNominationsViewerPane.this.previousNominations.add(nomination.getUuid());
				super.doSetValue(object, value);
			}
		});
		gcvDone.getColumn().setAlignment(SWT.CENTER);	
		addTypicalColumn("Remark", new StringAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_Remark(), jointModelEditor.getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				return false;
			}

			@Override
			public String render(final Object object) {
				return NominationsModelUtils.getRemark(getScenarioModel(), (AbstractNomination) object);
			}
		});
	}

	/**
	 * Modified createDeletaAction, so that we disallow deletion of generated nominations or
	 * overridden generated nomination (those eContainer() == null or with specIds set, respectively).
	 */
	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return new ScenarioTableViewerDeleteAction(callback) {
			@Override
			protected boolean isApplicableToSelection(final ISelection selection) {
				//Allow deletion of overridden nominations generated from specs (otherwise no way to get rid of them).
//				if (selection instanceof IStructuredSelection) {
//					final IStructuredSelection s = (IStructuredSelection)selection;
//					final Iterator<?> it = s.iterator();
//					while (it.hasNext()) {
//						final Object o = it.next();
//						if (o instanceof AbstractNomination) {
//							final AbstractNomination n = (AbstractNomination)o;
//							if (n.isSetSpecUuid()) {
//								return false;
//							}
//						}
//					}
//				}
				return selection.isEmpty() == false && selection instanceof IStructuredSelection;
			}
		};
	}
	
	protected LNGScenarioModel getScenarioModel() {
		return (LNGScenarioModel)this.jointModelEditor.getRootObject();
	}
	
	protected void pruneNominationsMarkedAsDone() {
		this.previousNominations.clear();
	}

	@Override
	public <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final ETypedElement... path) {
		final GridViewerColumn gvColumn = super.addTypicalColumn(columnName, manipulatorAndRenderer, path);
		gvColumn.setLabelProvider(new EObjectTableViewerColumnProvider(scenarioViewer, manipulatorAndRenderer, new EMFPath(true, path)) {
			@Override
			public void update(final ViewerCell cell) {
				super.update(cell);
				final Object element = cell.getElement();
				if (element instanceof AbstractNomination) {
					final LNGScenarioModel scenarioModel = getScenarioModel();
					final AbstractNomination nomination = (AbstractNomination) element;
					final LocalDate now = LocalDate.now();
					final LocalDate dueDate = NominationsModelUtils.getDueDate(scenarioModel, nomination);
					final LocalDate alertDate = NominationsModelUtils.getAlertDate(scenarioModel, nomination);
					if (nomination.isDone()) {
						cell.setBackground(colourGreen);
					} else if ((dueDate != null && !now.isBefore(dueDate))) {
						cell.setBackground(colourPink);
					} else if ((alertDate != null && !now.isBefore(alertDate))) {
						cell.setBackground(colourLightYellow);
					} else {
						cell.setBackground(null);
					}
				}
			}
		});
		return gvColumn;
	}
	
	public void setInputEquivalents(final Object input, final Collection<? extends Object> objectEquivalents) {
		for (final Object o : objectEquivalents) {
			HashSet<Object> inputs = equivalents.get(o);
			if (inputs == null) {
				inputs = new HashSet<>();
				equivalents.put(o, inputs);
			}
			inputs.add(input);
		}
		contents.add(input);
	}

	protected void clearInputEquivalents() {
		equivalents.clear();
		contents.clear();
	}

	@Override
	public ScenarioTableViewer createViewer(final Composite parent) {
		final ScenarioTableViewer viewer = super.createViewer(parent);
		
		viewer.addFilter(new ViewerFilter() {
			/**
			 * @return true, if we wish to display, false, otherwise.
			 */
			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				if (element instanceof AbstractNomination) {
					final LNGScenarioModel scenarioModel = getScenarioModel();
					final AbstractNomination sn = (AbstractNomination) element;
					final LocalDate startDate = getStartDate();
					final LocalDate endDate = getEndDate();
					final LocalDate dueDate = NominationsModelUtils.getDueDate(scenarioModel, sn);
					if ((startDate != null && dueDate != null && dueDate.isBefore(startDate)) || (endDate != null && dueDate != null && dueDate.isAfter(endDate))
							|| (!RelativeDateRangeNominationsViewerPane.this.includeDone && sn.isDone() && !previousNominations.contains(sn.getUuid()))) {
						return false;
					}
				}
				return true;
			}
		});
				
		return viewer;
	}
	
	public LocalDate getStartDate() {
		return this.nominationDatesToolbarEditor.getStartDate();
	}
	
	public LocalDate getEndDate() {
		return this.nominationDatesToolbarEditor.getEndDate();
	}
		
	@Override
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(event -> {
			final ISelection selection = ((NominationsScenarioTableViewer)scenarioViewer).getOriginalSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				
				DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, structuredSelection);

				//Add the edited slot nomination to the nominations model. 
				if (((IStructuredSelection) selection).getFirstElement() instanceof AbstractNomination) {
					final AbstractNomination sn = (AbstractNomination) ((IStructuredSelection) selection).getFirstElement();
					addNomination(sn);
				}
			}
		});
	}

	private void addNomination(AbstractNomination sn) {
		// Add modified slot nomination to the nomination model, if not present.
		final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
		final NominationsModel nominationsModel = RelativeDateRangeNominationsViewerPane.this.getNominationsModel();
		if (!nominationsModel.getNominations().contains(sn)) {
			final Command cmd = AddCommand.create(ed, nominationsModel, NominationsPackage.Literals.NOMINATIONS_MODEL__NOMINATIONS, sn);
			if (cmd.canExecute()) {
				jointModelEditor.getDefaultCommandHandler().handleCommand(cmd, null, null);
			}
		}
	}
	
	private NominationsModel getNominationsModel() {
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) jointModelEditor.getRootObject();
		return scenarioModel.getNominationsModel();
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {

		return new NominationsScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation);
	}

	@Override
	public void selectionChanged(MPart part, Object selection) {
		if (selection instanceof StructuredSelection) {
			final StructuredSelection s = (StructuredSelection)selection;
			this.selectedSlots.clear();
			for (final Object o : s.toArray()) {
				if (o instanceof Slot) {
					final Slot<?> slot = (Slot<?>) o;
					this.selectedSlots.add(slot);
				}
			}
			ViewerHelper.refresh(this.getViewer(), true);
		}
	}
	
	@Override
	public void refresh() {
		ViewerHelper.refresh(this.getViewer(), true);	
	}
	
	@Override
	public void dispose() {
		colourGreen.dispose();
		colourLightYellow.dispose();
		colourPink.dispose();
		super.dispose();
	}
}
