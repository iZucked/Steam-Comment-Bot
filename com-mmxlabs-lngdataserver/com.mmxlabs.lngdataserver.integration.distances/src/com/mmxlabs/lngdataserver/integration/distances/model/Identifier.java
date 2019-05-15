/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.model;

/**
 * @author robert.erdin@gmail.com
 *         created on 22/01/17.
 */
public class Identifier {
    private String identifier;
    private String provider;

    public Identifier(){
        // needed for morhia/jackson
    }

    public Identifier(String identifier, String provider){
        this.identifier = identifier;
        this.provider = provider;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
