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

import com.camnter.savevolley.network.core.http.base.AbstractHttpEntity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A generic streamed entity being received on a connection.
 *
 * @author Oleg Kalnichevski
 * @version $Revision: 496070 $
 * @since 4.0
 */
public class SaveHttpEntity extends AbstractHttpEntity {

    private InputStream content;
    private boolean contentObtained;
    private long length;


    /**
     * Creates a new basic entity.
     * The content is initially missing, the content length
     * is set to a negative number.
     */
    public SaveHttpEntity() {
        super();
        this.length = -1;
    }


    // non-javadoc, see interface HttpEntity
    public long getContentLength() {
        return this.length;
    }


    /**
     * Specifies the length of the content.
     *
     * @param len the number of bytes in the content, or
     * a negative number to indicate an unknown length
     */
    public void setContentLength(long len) {
        this.length = len;
    }


    /**
     * Obtains the content, once only.
     *
     * @return the content, if this is the first call to this method
     * since {@link #setContent setContent} has been called
     * @throws IllegalStateException if the content has been obtained before, or
     * has not yet been provided
     */
    public InputStream getContent() throws IllegalStateException {
        if (this.content == null) {
            throw new IllegalStateException("Content has not been provided");
        }
        if (this.contentObtained) {
            throw new IllegalStateException("Content has been consumed");
        }
        this.contentObtained = true;
        return this.content;
    } // getContent


    /**
     * Specifies the content.
     *
     * @param instream the stream to return with the next call to
     * {@link #getContent getContent}
     */
    public void setContent(final InputStream instream) {
        this.content = instream;
        this.contentObtained = false;
    }


    /**
     * Tells that this entity is not repeatable.
     *
     * @return <code>false</code>
     */
    public boolean isRepeatable() {
        return false;
    }


    // non-javadoc, see interface HttpEntity
    public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream instream = getContent();
        int l;
        byte[] tmp = new byte[2048];
        while ((l = instream.read(tmp)) != -1) {
            outstream.write(tmp, 0, l);
        }
    }


    // non-javadoc, see interface HttpEntity
    public boolean isStreaming() {
        return !this.contentObtained && this.content != null;
    }


    // non-javadoc, see interface HttpEntity
    public void consumeContent() throws IOException {
        if (content != null) {
            content.close(); // reads to the end of the entity
        }
    }
} // class BasicHttpEntity
