/*
    See lda-top/LICENCE (or http://elda.googlecode.com/hg/LICENCE)
    for the licence for this software.
    
    (c) Copyright 2011 Epimorphics Limited
    $Id$
*/

package com.epimorphics.lda.support;

import com.epimorphics.util.CollectionUtils;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A PropertyChain is a sequence of properties, to be used to trawl
 * down a model.
 *
 * @author chris
 */
public class PropertyChain {

    protected final List<Property> chain;

    public PropertyChain(List<Property> chain) {
        this.chain = chain;
    }

    public PropertyChain(String URI) {
        this(CollectionUtils.list(ResourceFactory.createProperty(URI)));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PropertyChain
                && chain.equals(((PropertyChain) other).chain);
    }

    @Override
    public int hashCode() {
        return chain.hashCode();
    }

    public PropertyChain(Property p) {
        this(CollectionUtils.list(p));
    }

    @Override
    public String toString() {
        return chain.toString();
    }

    public List<Property> getProperties() {
        return new ArrayList<Property>(chain);
    }
}