/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.jcr.jcrinstall.jcr.impl;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.jcr.jcrinstall.jcr.NodeConverter;
import org.apache.sling.osgi.installer.InstallableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Convert a Node that is a file to a FileInstallableData */ 
 public class FileNodeConverter implements NodeConverter {
    // regexp for filenames that we accept
    public static final String FILENAME_REGEXP = "[a-zA-Z0-9].*\\.[a-zA-Z][a-zA-Z][a-zA-Z]?";
    
    private final RegexpFilter filenameFilter = new RegexpFilter(FILENAME_REGEXP);
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final int bundleStartLevel;
    
    public FileNodeConverter(int bundleStartLevel) {
        this.bundleStartLevel = bundleStartLevel;
    }
    
	public InstallableData convertNode(Node n) throws RepositoryException {
		InstallableData result = null;
		if(n.hasProperty(FileInstallableData.JCR_CONTENT_DATA) && n.hasProperty(FileInstallableData.JCR_CONTENT_LAST_MODIFIED)) {
			if(acceptNodeName(n.getName())) {
				result = new FileInstallableData(n, bundleStartLevel);
			} else {
				log.debug("Node {} ignored due to {}", n.getPath(), filenameFilter);
			}
			return result;
		}
		log.debug("Node {} has no {} properties, ignored", n.getPath(), 
				FileInstallableData.JCR_CONTENT_DATA + " or " + FileInstallableData.JCR_CONTENT_LAST_MODIFIED);
		return null;
	}
	
	boolean acceptNodeName(String name) {
	    return filenameFilter.accept(name);
	}
}