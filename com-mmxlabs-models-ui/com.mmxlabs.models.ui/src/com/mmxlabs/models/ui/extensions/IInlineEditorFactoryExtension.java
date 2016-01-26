/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

@ExtensionBean("com.mmxlabs.models.ui.editorfactories")
public interface IInlineEditorFactoryExtension {
	@MapName("id") public String getID();
	@MapName("factoryClass") public IInlineEditorFactory instantiate();
	@MapName("featureNameEquals") public String getFeatureName();
	@MapName("FeatureAnyOf") public IFeatureMatcher [] getFeatureMatchers();
	@MapName("OwnerAnyOf") public IOwnerMatcher [] getOwnerMatchers();
	
	public interface IFeatureMatcher {
		@MapName("EDataTypeIs") IDataTypeMatcher [] getDataTypeMatchers();
		@MapName("EClassExtends") IEClassMatcher [] getEClassMatchers();
	}
	
	public interface IOwnerMatcher {
		@MapName("EClassExtends") IEClassMatcher [] getEClassMatchers();
	}
	
	public interface IDataTypeMatcher {
		@MapName("EDataTypeURI") String getDataTypeURI();
		@MapName("EDataTypeName") String getDataTypeName();
	}
	
	public interface IEClassMatcher {
		@MapName("EClass") String getEClassName();
	}
}
