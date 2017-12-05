package com.mmxlabs.lngdataserver.browser.ui;

import java.util.function.Consumer;

import com.mmxlabs.lngdataserver.browser.CompositeNode;

public interface DataExtension {
	CompositeNode getDataRoot();
	Consumer<String> getPublishCallback();
}
