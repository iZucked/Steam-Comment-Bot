/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editorpart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.SingleAddAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
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

	private static final String PLUGIN_ID = "com.mmxlabs.models.lng.nominations.editor";
	
	@NonNull 
	private final IScenarioEditingLocation jointModelEditor;
	
	private RelativeDateRangeToolbarEditor relativeDateToolbarEditor;
	
	private boolean includeDone = false;
	private boolean viewSelected = false;
	private final List<Slot> selectedSlots = new ArrayList<>();
	private final HashSet<String> previousNominations = new HashSet<>();
	
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
					if ((alertDate != null && !now.isBefore(alertDate))) {
						cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
					}
					if ((dueDate != null && !now.isBefore(dueDate))) {
						cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					}
				}
			}
		});
		return gvColumn;
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
		return relativeDateToolbarEditor.getStartDate();
	}
	
	public LocalDate getEndDate() {
		return relativeDateToolbarEditor.getEndDate();
	}
	
	@Override
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(event -> {
			final ISelection selection = scenarioViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, structuredSelection);

				//Add the edited slot nomination to the nominations model. 
				final AbstractNomination sn = (AbstractNomination) ((IStructuredSelection) selection).getFirstElement();
				addNomination(sn);
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

		return new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation) {

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
						elements.addAll(NominationsModelUtils.generateNominations(jointModelEditor, startDate, endDate));
						if (RelativeDateRangeNominationsViewerPane.this.viewSelected && !RelativeDateRangeNominationsViewerPane.this.selectedSlots.isEmpty()) {
							return elements.stream().filter(n -> n.getNomineeId() != null && isSelectedSlot(n.getNomineeId())).toArray();
						}
						else {
							return elements.toArray();
						}
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
		};
	}

	@Override
	public void selectionChanged(MPart part, Object selection) {
		if (selection instanceof StructuredSelection) {
			final StructuredSelection s = (StructuredSelection)selection;
			this.selectedSlots.clear();
			for (final Object o : s.toArray()) {
				if (o instanceof Slot) {
					final Slot slot = (Slot)o;
					if (slot != null) {
						this.selectedSlots.add(slot);
					}
				}
			}
			ViewerHelper.refresh(this.getViewer(), true);
		}
	}
	
	@Override
	public void refresh() {
		ViewerHelper.refresh(this.getViewer(), true);	
	}
}
