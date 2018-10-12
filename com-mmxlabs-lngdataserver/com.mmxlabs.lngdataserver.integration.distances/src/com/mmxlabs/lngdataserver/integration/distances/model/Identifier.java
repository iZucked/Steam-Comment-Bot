package com.mmxlabs.lngdataserver.integration.distances.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author robert.erdin@gmail.com
 *         created on 22/01/17.
 */
@Entity
public class Identifier {
    @Id
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
