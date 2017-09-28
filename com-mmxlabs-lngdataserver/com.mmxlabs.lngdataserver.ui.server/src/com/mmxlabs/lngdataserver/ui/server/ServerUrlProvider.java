package com.mmxlabs.lngdataserver.ui.server;

public enum ServerUrlProvider {
    INSTANCE;
    private int port;
    private boolean available = false;
    
    void setPort(int port){
    	this.port = port;
    }
    
    void setAvailable(boolean available) {
    	this.available = available;
    }
    
    int getPort() {
    	return this.port;
    }
    
    public String getBaseUrl() {
    	if (port == 0) {
    		throw new IllegalStateException("No port set yet.");
    	}
    	return "http://localhost:" + port;
    }
    
    public boolean isAvailable() {
    	return available;
    }
}