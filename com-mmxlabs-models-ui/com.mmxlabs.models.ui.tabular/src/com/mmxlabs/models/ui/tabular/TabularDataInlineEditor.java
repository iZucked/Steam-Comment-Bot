/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.ecore.SafeEContentAdapter;

/**
 * An {@link IInlineEditor} for simple tabular data. This works as a builder
 * class to define the table which will be invoked later when the editor control
 * is created.
 * 
 * @author Simon Goodall
 * 
 */
public class TabularDataInlineEditor extends BasicAttributeInlineEditor {

	/**
	 * Data record to store a column definition
	 *
	 */
	public static class ColDef {
		/**
		 * The column header name
		 */
		private String name;
		/**
		 * The column feature off the row object
		 */
		private EStructuralFeature feature;

		/**
		 * Optional cell editor
		 */
		private ICellManipulator manipulator;

		/**
		 * The cell renderer
		 */
		private ICellRenderer renderer;

		/**
		 * A call back function used when creating the table as e.g. manipulators
		 * require the EditingDomain which is not available at builder time.
		 */
		private BiFunction<ICommandHandler, IReferenceValueProviderProvider, Object> rmMaker;

		/**
		 * A post creation callback fopr further column customisation outside the
		 * builder API.
		 */
		private Consumer<GridViewerColumn> action;

		/**
		 * Pre-defined width parameter
		 */
		private int width = 0;
	}

	public static class ButtonDef {
		/**
		 * The label for the button
		 */
		private String name;

		/**
		 * The image for the button
		 */
		private ImageDescriptor imageDescriptor;
		/**
		 * The action to apply when the button is pressed
		 */
		private TriConsumer<Object, ICommandHandler, ISelection> action;
		/**
		 * Optional event handler when the table selection changes. E.g. to activate or
		 * deactivate the button
		 */
		private BiConsumer<Button, IStructuredSelection> selectionChanged;

		/**
		 * The initial enabled state. Useful to set to false with e.g. a delete action.
		 */
		private boolean enabledState;

	}

	public static class ColDefBuilder {
		private final ColDef def;
		private final Builder builder;

		private ColDefBuilder(final Builder builder, final String name, final EStructuralFeature f) {
			this.builder = builder;

			def = new ColDef();
			def.name = name;
			def.feature = f;
		}

		public Builder build() {
			builder.columns.add(def);
			return builder;
		}

		public ColDefBuilder withWidth(final int width) {
			def.width = width;
			return this;
		}

		public ColDefBuilder withRenderer(final ICellRenderer r) {
			def.renderer = r;
			return this;
		}

		public ColDefBuilder withManipulator(final ICellManipulator m) {
			def.manipulator = m;
			return this;
		}

		/**
		 * Provide a callback function to create a combined renderer and manipulator
		 * instance when the table is created. A specific renderer or manipulator
		 * instance passed into the builder will take precedence over the callback
		 * function.
		 * 
		 * @param <T>
		 * @param rm
		 * @return
		 */
		public <T extends ICellRenderer & ICellManipulator> ColDefBuilder withRMMaker(final BiFunction<ICommandHandler, IReferenceValueProviderProvider, T> rm) {
			def.rmMaker = (BiFunction<ICommandHandler, IReferenceValueProviderProvider, Object>) rm;
			return this;
		}
	}

	public static class Builder {
		private IContentProvider contentProvider;
		private String groupName;
		private String labelName;
		private final List<ColDef> columns = new LinkedList<>();
		private final List<ButtonDef> buttonDefs = new LinkedList<>();

		private boolean showHeader;

		private ViewerComparator viewerComparator;
		private int heightHint;
		private boolean changeHeaderColourWithValidation;

		public Builder withContentProvider(final IContentProvider contentProvider) {
			this.contentProvider = contentProvider;

			return this;
		}

		public Builder withGroup(final String groupName) {
			this.groupName = groupName;
			return this;
		}

		public Builder withLabel(final String labelName) {
			this.labelName = labelName;
			return this;
		}

		public Builder withContentProvider(final Function<Object, Object[]> func) {
			this.contentProvider = new IStructuredContentProvider() {
				@Override
				public Object[] getElements(final Object inputElement) {
					return func.apply(inputElement);
				}
			};

			return this;
		}

		public ColDefBuilder buildColumn(final String name, final EStructuralFeature f) {
			return new ColDefBuilder(this, name, f);
		}

		public Builder withAction(final String name, final TriConsumer<Object, ICommandHandler, ISelection> action) {

			final ButtonDef d = new ButtonDef();
			d.name = name;
			d.action = action;
			d.enabledState = true;
			buttonDefs.add(d);

			return this;
		}

		public Builder withAction(final ImageDescriptor imageDescriptor, final TriConsumer<Object, ICommandHandler, ISelection> action) {

			final ButtonDef d = new ButtonDef();
			d.imageDescriptor = imageDescriptor;
			d.action = action;
			d.enabledState = true;
			buttonDefs.add(d);

			return this;
		}

		public Builder withAction(final String name, final TriConsumer<Object, ICommandHandler, ISelection> action, final boolean enabledState,
				final BiConsumer<Button, IStructuredSelection> selectionChanged) {

			final ButtonDef d = new ButtonDef();
			d.name = name;
			d.action = action;
			d.selectionChanged = selectionChanged;
			d.enabledState = enabledState;

			buttonDefs.add(d);

			return this;
		}

		public Builder withAction(final ImageDescriptor imageDescriptor, final TriConsumer<Object, ICommandHandler, ISelection> action, final boolean enabledState,
				final BiConsumer<Button, IStructuredSelection> selectionChanged) {

			final ButtonDef d = new ButtonDef();
			d.imageDescriptor = imageDescriptor;
			d.action = action;
			d.selectionChanged = selectionChanged;
			d.enabledState = enabledState;

			buttonDefs.add(d);

			return this;
		}

		public TabularDataInlineEditor build(final EStructuralFeature f) {
			return new TabularDataInlineEditor(f, this);
		}

		public void withShowHeaders(final boolean showHeader) {
			this.showHeader = showHeader;
		}

		public void withComparator(final ViewerComparator viewerComparator) {
			this.viewerComparator = viewerComparator;
		}

		public void withHeightHint(final int heightHint) {
			this.heightHint = heightHint;
		}

		public void withChangeHeaderColourWithValidation(boolean changeHeaderColourWithValidation) {
			this.changeHeaderColourWithValidation = changeHeaderColourWithValidation;
		}
	}

	protected GridTableViewer tableViewer;
	protected Control lblControl;
	private final Builder builder;

	private IStatus lastStatus = null;

	public TabularDataInlineEditor(final EStructuralFeature feature, final Builder builder) {
		super(feature);
		this.builder = builder;
	}

	@Override
	public boolean needsFullWidth() {
		return true;
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

		Composite controlParent = parent;
		if (builder.groupName != null) {
			final Group g = new Group(parent, SWT.NONE);
			g.setText(builder.groupName);

			g.setLayout(GridLayoutFactory.fillDefaults()//
					.equalWidth(false) //
					.numColumns(1) //
					.margins(0, 0) //
					.spacing(0, 0) //
					.create());

			controlParent = g;

			lblControl = g;
		} else {
			final Composite g = new Composite(parent, SWT.NONE);
			g.setBackground(parent.getBackground());
			g.setLayout(GridLayoutFactory.fillDefaults()//
					.equalWidth(false) //
					.numColumns(1) //
					.margins(0, 0) //
					.spacing(0, 0) //
					.create());

			controlParent = g;

			if (builder.labelName != null) {
				final Label lbl = new Label(g, SWT.NONE);
				lbl.setBackground(parent.getBackground());
				lbl.setText(builder.labelName);

				lblControl = lbl;
			}
		}

		tableViewer = new GridTableViewer(controlParent);
		tableViewer.setContentProvider(builder.contentProvider);

		tableViewer.getGrid().setHeaderVisible(builder.showHeader);

		ColumnViewerToolTipSupport.enableFor(tableViewer);

		GridViewerHelper.configureLookAndFeel(tableViewer);

		if (builder.heightHint > 0) {
			tableViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().hint(SWT.DEFAULT, builder.heightHint).create());
		}

		for (final ColDef def : builder.columns) {

			ICellRenderer r = def.renderer;
			ICellManipulator m = def.manipulator;
			if (def.rmMaker != null) {
				final Object t = def.rmMaker.apply(commandHandler, commandHandler.getReferenceValueProviderProvider());
				if (r == null && t instanceof ICellRenderer) {
					r = (ICellRenderer) t;
				}
				if (m == null && t instanceof ICellManipulator) {
					m = (ICellManipulator) t;
				}
			}
			final GridViewerColumn col = createColumn(def.name, def.feature, m, r, tableViewer);
			if (def.width > 0) {
				col.getColumn().setWidth(def.width);
			}
			if (def.action != null) {
				def.action.accept(col);
			}
		}

		if (builder.viewerComparator != null) {
			tableViewer.setComparator(builder.viewerComparator);
		}

		if (!builder.buttonDefs.isEmpty()) {
			final Composite buttonParent = new Composite(controlParent, SWT.NONE);
			toolkit.adapt(buttonParent);
			buttonParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(builder.buttonDefs.size()).equalWidth(false).create());
			for (final ButtonDef d : builder.buttonDefs) {
				final Button b = toolkit.createButton(buttonParent, null, SWT.PUSH);
				if (d.imageDescriptor != null) {
					Image img = d.imageDescriptor.createImage();
					b.setImage(img);
					b.addDisposeListener(e -> img.dispose());
				} else {
					b.setText(d.name);
				}
				b.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						d.action.accept(input, commandHandler, tableViewer.getSelection());
					}
				});
				if (d.selectionChanged != null) {
					tableViewer.addSelectionChangedListener(event -> d.selectionChanged.accept(b, event.getStructuredSelection()));
				}
				b.setEnabled(d.enabledState);
			}
		}

		tableViewer.getControl().addDisposeListener(event -> {
			if (input != null) {
				input.eAdapters().remove(adapter);
			}
			lastStatus = null;
		});

		// If we have wrapped the table, the return the control parent.
		if (controlParent != parent) {
			return controlParent;
		}

		// Otherwise the table is the main control
		return tableViewer.getControl();

	}

	protected final SafeEContentAdapter adapter = new SafeEContentAdapter() {

		@Override
		protected void safeNotifyChanged(final Notification msg) {
			if (msg.isTouch()) {
				return;
			}
			if (!tableViewer.getControl().isDisposed()) {
				tableViewer.refresh();
			}
		}
	};

	@Override
	protected void updateDisplay(final Object value) {
		if (input != null) {
			input.eAdapters().remove(adapter);
		}

		if (!tableViewer.getControl().isDisposed()) {
			tableViewer.setInput(value);

			if (input instanceof EObject) {
				input.eAdapters().add(adapter);
			}
		}
	}

	private GridViewerColumn createColumn(final String name, final EStructuralFeature attr, final ICellManipulator manipulator, final ICellRenderer renderer, final GridTableViewer tableViewer) {

		final GridViewerColumn column = new GridViewerColumn(tableViewer, SWT.NONE);
		GridViewerHelper.configureLookAndFeel(column);
		column.getColumn().setText(name);

		if (manipulator != null) {

			column.setEditingSupport(new EditingSupport(tableViewer) {

				@Override
				protected boolean canEdit(final Object element) {
					return (isEditorLocked() == false) && (manipulator != null) && manipulator.canEdit(element);
				}

				@Override
				protected CellEditor getCellEditor(final Object element) {
					return manipulator.getCellEditor((Composite) tableViewer.getControl(), element);
				}

				@Override
				protected Object getValue(final Object element) {
					return manipulator.getValue(element);
				}

				@Override
				protected void setValue(final Object element, final Object value) {
					// a value has come out of the celleditor and is being set on
					// the element.
					if (isEditorLocked()) {
						return;
					}
					manipulator.setValue(element, value);
					tableViewer.refresh();
				}

			});
		}

		if (renderer != null) {
			column.setLabelProvider(new ColumnLabelProvider() {

				@Override
				public String getText(final Object element) {
					return renderer.render(element);
				}

				@Override
				public Color getBackground(final Object element) {

					if (lastStatus != null) {
						final int severity = checkStatus(lastStatus, IStatus.OK, (EObject) element, attr);
						if (severity == IStatus.ERROR) {
							return Display.getDefault().getSystemColor(SWT.COLOR_RED);
						}
						if (severity == IStatus.WARNING) {
							return Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
						}
					}
					return super.getBackground(element);
				}

				@Override
				public String getToolTipText(Object element) {

					if (lastStatus != null && !lastStatus.isOK()) {
						StringBuilder sb = new StringBuilder();
						getMessages(lastStatus, sb, (EObject) element, attr);

						return sb.toString();
					}
					return super.getToolTipText(element);
				}

			});
		}
		return column;

	}

	@Override
	public void processValidation(final IStatus status) {

		if (!tableViewer.getControl().isDisposed()) {
			lastStatus = status;
			tableViewer.refresh();
		}

		if (builder.changeHeaderColourWithValidation && lblControl != null && status != null) {

			final int severity = checkStatus(status, IStatus.OK, input, typedElement);

			switch (severity) {
			case IStatus.ERROR -> lblControl.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			case IStatus.WARNING -> lblControl.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
			default -> lblControl.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			}
		}

		super.processValidation(status);
	}

	protected int checkStatus(final IStatus status, int currentSeverity, final EObject input, final ETypedElement attribute) {
		if (status.isMultiStatus()) {
			final IStatus[] children = status.getChildren();
			for (final IStatus element : children) {
				final int severity = checkStatus(element, currentSeverity, input, attribute);

				// Is severity worse, then note it
				if (severity > currentSeverity) {
					currentSeverity = element.getSeverity();
				}

			}
		}
		if (status instanceof IDetailConstraintStatus element) {

			final Collection<EObject> objects = element.getObjects();
			if (objects.contains(input)) {
				if (element.getFeaturesForEObject(input).contains(attribute)) {
					// Is severity worse, then note it
					if (element.getSeverity() > currentSeverity) {
						currentSeverity = element.getSeverity();
					}

					return currentSeverity;
				}
			}

			if (objects.contains(input.eContainer())) {
				if (element.getFeaturesForEObject(input.eContainer()).contains(attribute)) {
					// Is severity worse, then note it
					if (element.getSeverity() > currentSeverity) {
						currentSeverity = element.getSeverity();
					}

					return currentSeverity;
				}
			}
		}

		return currentSeverity;
	}

	protected void getMessages(final IStatus status, StringBuilder sb, final EObject input, final EStructuralFeature attribute) {
		if (status.isMultiStatus()) {
			final IStatus[] children = status.getChildren();
			for (final IStatus element : children) {
				getMessages(element, sb, input, attribute);

			}
		}
		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus element = (IDetailConstraintStatus) status;

			final Collection<EObject> objects = element.getObjects();
			if (objects.contains(input)) {
				if (element.getFeaturesForEObject(input).contains(attribute)) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(element.getBaseMessage());
				}
			}

			if (objects.contains(input.eContainer())) {
				if (element.getFeaturesForEObject(input.eContainer()).contains(attribute)) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(element.getBaseMessage());
				}
			}
		}
	}
}
