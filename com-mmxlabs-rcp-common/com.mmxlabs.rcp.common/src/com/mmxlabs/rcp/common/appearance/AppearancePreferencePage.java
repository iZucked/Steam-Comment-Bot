package com.mmxlabs.rcp.common.appearance;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class AppearancePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private @NonNull ScopedPreferenceStore scopedPreferenceStore;

	private Map<String, RGB> debugColouring;
	private static final RGB TAB_BG_COLOUR = new RGB(75, 208, 115);

	@NonNull
	private static final String TOOLBAR_EDITOR_NAME = "DebugColouringEnabled";
	@NonNull
	private static final String SETTINGS_PREFIX = "org.eclipse.e4.ui.css.theme.e4_default.org.eclipse.ui.workbench.";

	private IPropertyChangeListener listener = new IPropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getProperty().equals(TOOLBAR_EDITOR_NAME)) {
				if (event.getNewValue() == Boolean.TRUE) {
					debugColouring.entrySet().forEach((entry) -> {
						String setting = entry.getKey();
						RGB value = entry.getValue();
						scopedPreferenceStore.setValue(setting, String.format("%d,%d,%d", value.red, value.green, value.blue));
					});
				} else {
					debugColouring.entrySet().forEach((entry) -> {
						scopedPreferenceStore.setToDefault(entry.getKey());
					});
				}

			}
		}
	};

	public AppearancePreferencePage() {
		debugColouring = new HashMap<>();
		debugColouring.put(SETTINGS_PREFIX + "INACTIVE_UNSELECTED_TABS_COLOR_START", TAB_BG_COLOUR);
		debugColouring.put(SETTINGS_PREFIX + "ACTIVE_UNSELECTED_TABS_COLOR_START", TAB_BG_COLOUR);

		scopedPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.ui.workbench");
		setPreferenceStore(scopedPreferenceStore);
		scopedPreferenceStore.addPropertyChangeListener(listener);
	}

	@Override
	public void dispose() {
		scopedPreferenceStore.removePropertyChangeListener(listener);
		super.dispose();
	}

	@Override
	public void init(IWorkbench workbench) {
		// Nothing required here ...
	}

	@Override
	protected void createFieldEditors() {
		
		final BooleanFieldEditor debugColour = new BooleanFieldEditor(TOOLBAR_EDITOR_NAME, "&Testing mode (restart required)", getFieldEditorParent());
		addField(debugColour);
	}
	
}
