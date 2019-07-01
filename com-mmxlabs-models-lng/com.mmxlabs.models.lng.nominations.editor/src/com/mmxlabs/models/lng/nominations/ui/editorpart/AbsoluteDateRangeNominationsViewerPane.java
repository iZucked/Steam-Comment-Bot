/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editorpart;

/**
 * This nominations viewer pane allows an absolute date range to be specified via two date formatted text boxes which allow a
 * start date and an end date to be specified. 
 * 
 * At the time of writing it is envisaged that this version will not be released, but have left this here, in case the unlikely
 * scenario occurs, that it is requested back or some variation is requested by users in the future.
 */
/*public class AbsoluteDateRangeNominationsViewerPane extends AbstractNominationsViewerPane implements ISelectionListener {

	private static final String PLUGIN_ID = "com.mmxlabs.models.lng.nominations.editor";
	private final IScenarioEditingLocation jointModelEditor;
	private NominationDatesToolbarEditor nominationDatesToolbarEditor;
	private boolean includeDone = false;
	private boolean viewSelected = true;
	private final HashSet<String> selectedSlots = new HashSet<>();
	private final HashSet<String> previousNominations = new HashSet<>();
	
	public AbsoluteDateRangeNominationsViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
		final ESelectionService service = PlatformUI.getWorkbench().getService(ESelectionService.class);
		service.addPostSelectionListener(this);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		final ToolBarManager toolbar = getToolBarManager();
		nominationDatesToolbarEditor = new NominationDatesToolbarEditor("nomination_dates_toolbar",
				scenarioEditingLocation.getEditingDomain(), jointModelEditor);
		toolbar.appendToGroup(EDIT_GROUP, nominationDatesToolbarEditor);
		
		final Action refreshButton = new Action("Refresh", Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				AbsoluteDateRangeNominationsViewerPane.this.pruneNominationsMarkedAsDone();
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
				AbsoluteDateRangeNominationsViewerPane.this.includeDone = !AbsoluteDateRangeNominationsViewerPane.this.includeDone;
				this.setChecked(AbsoluteDateRangeNominationsViewerPane.this.includeDone);
				viewer.refresh();
			}
		};
		includeDoneToggle.setChecked(this.includeDone);
		this.getMenuManager().add(includeDoneToggle);
		
		final Action viewSelectedToggle = new Action("View selected", Action.AS_CHECK_BOX) {
			@Override
			public void run() {
				AbsoluteDateRangeNominationsViewerPane.this.viewSelected = !AbsoluteDateRangeNominationsViewerPane.this.viewSelected;
				this.setChecked(AbsoluteDateRangeNominationsViewerPane.this.viewSelected);
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
		addTypicalColumn("Done", new BooleanFlagAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNomination_Done(), jointModelEditor.getEditingDomain()) {
			@Override
			public void doSetValue(Object object, Object value) {
				final AbstractNomination nomination = (AbstractNomination)object;
				AbsoluteDateRangeNominationsViewerPane.this.previousNominations.add(nomination.getUuid());
				super.doSetValue(object, value);
			}
		});
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
					if ((alertDate != null && !now.isBefore(alertDate) || (dueDate != null && !now.isBefore(dueDate)))) {
						cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
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
			//
			// @return true, if we wish to display, false, otherwise.
			//
			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				if (element instanceof SlotNomination) {
					final LNGScenarioModel scenarioModel = getScenarioModel();
					final SlotNomination sn = (SlotNomination) element;
					final LocalDate startDate = AbsoluteDateRangeNominationsViewerPane.this.nominationDatesToolbarEditor.getStartDate();
					final LocalDate endDate = AbsoluteDateRangeNominationsViewerPane.this.nominationDatesToolbarEditor.getEndDate();
					final LocalDate dueDate = NominationsModelUtils.getDueDate(scenarioModel, sn);
					if ((startDate != null && dueDate != null && dueDate.isBefore(startDate)) || (endDate != null && dueDate != null && dueDate.isAfter(endDate))
							|| (!AbsoluteDateRangeNominationsViewerPane.this.includeDone && sn.isDone() && !previousNominations.contains(sn.getUuid()))) {
						return false;
					}
				}
				return true;
			}
		});

		return viewer;
	}

	@Override
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(event -> {
			final ISelection selection = scenarioViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, structuredSelection);

				// Add modified slot nomination to the nomination model, if not present.
				final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
				final NominationsModel nominationsModel = AbsoluteDateRangeNominationsViewerPane.this.getNominationsModel();
				final SlotNomination sn = (SlotNomination) ((IStructuredSelection) selection).getFirstElement();
				if (!nominationsModel.getSlotNominations().contains(sn)) {
					final Command cmd = AddCommand.create(ed, nominationsModel, NominationsPackage.Literals.NOMINATIONS_MODEL__SLOT_NOMINATIONS, sn);
					if (cmd.canExecute()) {
						jointModelEditor.getDefaultCommandHandler().handleCommand(cmd, null, null);
					}
				}
			}
		});
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
								elements.addAll(nominationsModel.getSlotNominations());
							}
							setCurrentContainerAndContainment(nominationsModel, null);
						}
						// Add in extra generated nominations
						elements.addAll(NominationsModelUtils.generateNominations(jointModelEditor));
						if (AbsoluteDateRangeNominationsViewerPane.this.viewSelected && !AbsoluteDateRangeNominationsViewerPane.this.selectedSlots.isEmpty()) {
							return elements.stream().filter(n -> n.getNomineeId() != null && AbsoluteDateRangeNominationsViewerPane.this.selectedSlots.contains(n.getNomineeId())).toArray();
						}
						else {
							return elements.toArray();
						}
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
					if (slot.getName() != null) {
						this.selectedSlots.add(slot.getName());
					}
				}
			}
			ViewerHelper.refresh(this.getViewer(), true);
		}
	}
}
*/