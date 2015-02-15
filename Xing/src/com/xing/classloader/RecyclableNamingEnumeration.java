package com.xing.classloader;

import java.util.Enumeration;
import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

@SuppressWarnings("unchecked")
public class RecyclableNamingEnumeration implements NamingEnumeration {

    // ----------------------------------------------------------- Constructors


	public RecyclableNamingEnumeration(Vector entries) {
        this.entries = entries;
        recycle();
    }


    // -------------------------------------------------------------- Variables


    /**
     * Entries.
     */
	protected Vector entries;


    /**
     * Underlying enumeration.
     */
	protected Enumeration enumer;


    // --------------------------------------------------------- Public Methods


    /**
     * Retrieves the next element in the enumeration.
     */
    public Object next()
        throws NamingException {
        return nextElement();
    }


    /**
     * Determines whether there are any more elements in the enumeration.
     */
    public boolean hasMore()
        throws NamingException {
        return enumer.hasMoreElements();
    }


    /**
     * Closes this enumeration.
     */
    public void close()
        throws NamingException {
    }


    public boolean hasMoreElements() {
        return enumer.hasMoreElements();
    }


    public Object nextElement() {
        return enumer.nextElement();
    }


    // -------------------------------------------------------- Package Methods


    /**
     * Recycle.
     */
    void recycle() {
    	enumer = entries.elements();
    }

}
