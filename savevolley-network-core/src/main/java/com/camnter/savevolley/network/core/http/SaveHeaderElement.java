/*
 * Copyright (C) 2016 CaMnter yuanyu.camnter@gmail.com
 * Copyright (C) 2008 The Apache Software Foundation (ASF)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.camnter.savevolley.network.core.http;

import com.camnter.savevolley.network.core.http.core.HeaderElement;
import com.camnter.savevolley.network.core.http.core.NameValuePair;
import com.camnter.savevolley.network.core.util.CharArrayBuffer;
import com.camnter.savevolley.network.core.util.LangUtils;

/**
 * One element of an HTTP header's value.
 * <p>
 * Some HTTP headers (such as the set-cookie header) have values that
 * can be decomposed into multiple elements.  Such headers must be in the
 * following form:
 * </p>
 * <pre>
 * header  = [ element ] *( "," [ element ] )
 * element = name [ "=" [ value ] ] *( ";" [ param ] )
 * param   = name [ "=" [ value ] ]
 *
 * name    = token
 * value   = ( token | quoted-string )
 *
 * token         = 1*&lt;any char except "=", ",", ";", &lt;"&gt; and
 *                       white space&gt;
 * quoted-string = &lt;"&gt; *( text | quoted-char ) &lt;"&gt;
 * text          = any char except &lt;"&gt;
 * quoted-char   = "\" char
 * </pre>
 * <p>
 * Any amount of white space is allowed between any part of the
 * header, element or param and is ignored. A missing value in any
 * element or param will be stored as the empty {@link String};
 * if the "=" is also missing <var>null</var> will be stored instead.
 * </p>
 * <p>
 * This class represents an individual header element, containing
 * both a name/value pair (value may be <tt>null</tt>) and optionally
 * a set of additional parameters.
 * </p>
 *
 * @author B.C. Holmes
 * @author Park, Sung-Gu
 * @author Mike Bowler
 * @author Oleg Kalnichevski
 *
 *
 *         <!-- empty lines above to avoid 'svn diff' context problems -->
 * @version $Revision: 604625 $ $Date: 2007-12-16 06:11:11 -0800 (Sun, 16 Dec 2007) $
 * @since 4.0
 */
public class SaveHeaderElement implements HeaderElement, Cloneable {

    private final String name;
    private final String value;
    private final NameValuePair[] parameters;


    /**
     * Constructor with name, value and parameters.
     *
     * @param name header element name
     * @param value header element value. May be <tt>null</tt>
     * @param parameters header element parameters. May be <tt>null</tt>.
     * Parameters are copied by reference, not by value
     */
    public SaveHeaderElement(final String name, final String value, final NameValuePair[] parameters) {
        super();
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = name;
        this.value = value;
        if (parameters != null) {
            this.parameters = parameters;
        } else {
            this.parameters = new NameValuePair[] {};
        }
    }


    /**
     * Constructor with name and value.
     *
     * @param name header element name
     * @param value header element value. May be <tt>null</tt>
     */
    public SaveHeaderElement(final String name, final String value) {
        this(name, value, null);
    }


    /**
     * Returns the name.
     *
     * @return String name The name
     */
    public String getName() {
        return this.name;
    }


    /**
     * Returns the value.
     *
     * @return String value The current value.
     */
    public String getValue() {
        return this.value;
    }


    /**
     * Get parameters, if any.
     * The returned array is created for each invocation and can
     * be modified by the caller without affecting this header element.
     *
     * @return parameters as an array of {@link NameValuePair}s
     */
    public NameValuePair[] getParameters() {
        return (NameValuePair[]) this.parameters.clone();
    }


    /**
     * Obtains the number of parameters.
     *
     * @return the number of parameters
     */
    public int getParameterCount() {
        return this.parameters.length;
    }


    /**
     * Obtains the parameter with the given index.
     *
     * @param index the index of the parameter, 0-based
     * @return the parameter with the given index
     */
    public NameValuePair getParameter(int index) {
        // ArrayIndexOutOfBoundsException is appropriate
        return this.parameters[index];
    }


    /**
     * Returns parameter with the given name, if found. Otherwise null
     * is returned
     *
     * @param name The name to search by.
     * @return NameValuePair parameter with the given name
     */
    public NameValuePair getParameterByName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        NameValuePair found = null;
        for (int i = 0; i < this.parameters.length; i++) {
            NameValuePair current = this.parameters[i];
            if (current.getName().equalsIgnoreCase(name)) {
                found = current;
                break;
            }
        }
        return found;
    }


    public boolean equals(final Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (object instanceof HeaderElement) {
            SaveHeaderElement that = (SaveHeaderElement) object;
            return this.name.equals(that.name) && LangUtils.equals(this.value, that.value) &&
                    LangUtils.equals(this.parameters, that.parameters);
        } else {
            return false;
        }
    }


    public int hashCode() {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, this.name);
        hash = LangUtils.hashCode(hash, this.value);
        for (int i = 0; i < this.parameters.length; i++) {
            hash = LangUtils.hashCode(hash, this.parameters[i]);
        }
        return hash;
    }


    public String toString() {
        CharArrayBuffer buffer = new CharArrayBuffer(64);
        buffer.append(this.name);
        if (this.value != null) {
            buffer.append("=");
            buffer.append(this.value);
        }
        for (int i = 0; i < this.parameters.length; i++) {
            buffer.append("; ");
            buffer.append(this.parameters[i]);
        }
        return buffer.toString();
    }


    public Object clone() throws CloneNotSupportedException {
        // parameters array is considered immutable
        // no need to make a copy of it
        return super.clone();
    }
}

