/*
 * Copyright (c) 2013. Knowledge Media Institute - The Open University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.open.kmi.iserve.sal.manager.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.open.kmi.iserve.commons.io.*;
import uk.ac.open.kmi.iserve.commons.model.Service;
import uk.ac.open.kmi.iserve.sal.ServiceFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ManagerSingletonTest
 * <p/>
 * Author: Carlos Pedrinaci (KMi - The Open University)
 * Date: 06/06/2013
 * Time: 18:50
 */
public class ManagerSingletonTest {

    private static final String OWLS_TC3_SERVICES = "/OWLS-TC3-MSM";
    private static final Syntax SYNTAX = Syntax.TTL;

    private URI testFolder;
    private FilenameFilter ttlFilter;
    private List<URI> docUris;

    private int numServices;
    private String dataUpdateEndpoint;
    private File[] msmTtlTcFiles;

    @Before
    public void setUp() throws Exception {
        testFolder = ManagerSingletonTest.class.getResource(OWLS_TC3_SERVICES).toURI();
        ttlFilter = new FilenameFilterBySyntax(Syntax.TTL);
        File dir = new File(testFolder);
        msmTtlTcFiles = dir.listFiles(ttlFilter);
        numServices = msmTtlTcFiles.length;
        docUris = new ArrayList<URI>();

        dataUpdateEndpoint = ManagerSingleton.getInstance().getConfiguration().getSparqlUpdateUri().toASCIIString();
    }


    @Test
    public void testCreateDocument() throws Exception {

        ManagerSingleton.getInstance().clearRegistry();
        InputStream in;
        URI docUri;
        int count = 0;
        // Upload every document and obtain their URLs
        for (File ttlFile : msmTtlTcFiles) {
            in = new FileInputStream(ttlFile);
            System.out.println("Adding document: " + ttlFile.getName());
            docUri = ManagerSingleton.getInstance().createDocument(in, ServiceFormat.MSM_TTL);
            Assert.assertNotNull(docUri);
            System.out.println("Service added: " + docUri.toASCIIString());
            count++;
        }
        Assert.assertEquals(numServices, count);

        // TODO: We should check the content is correct
    }

    @Test
    public void testDeleteDocument() throws Exception {
        boolean result;
        URI docUri;
        Random rand = new Random();
        List<URI> documents = ManagerSingleton.getInstance().listDocuments();
        int numDocs = documents.size();
        int delta = numDocs / 10;
        int index = rand.nextInt(10 - 0 + 1) + 0;
        int count = numDocs;
        while (index < numDocs) {
            docUri = documents.get(index);
            System.out.println("Deleting document: " + docUri);
            result = ManagerSingleton.getInstance().deleteDocument(docUri);
            Assert.assertTrue(result);
            index += delta;
            count--;
        }
        // Now ensure that the number of docs was reduced accordingly;
        documents = ManagerSingleton.getInstance().listDocuments();
        Assert.assertEquals(count, documents.size());
    }

    //
//    @Test
//    public void testAddRelatedDocumentToService() throws Exception {
//
//    }
//

    @Test
    public void testImportService() throws Exception {

        ManagerSingleton.getInstance().clearRegistry();
        InputStream in;
        URI serviceUri;
        int count = 0;
        for (File ttlFile : msmTtlTcFiles) {
            in = new FileInputStream(ttlFile);
            System.out.println("Adding service: " + ttlFile.getName());
            serviceUri = ManagerSingleton.getInstance().importService(in, ServiceFormat.MSM_TTL);
            Assert.assertNotNull(serviceUri);
            System.out.println("Service added: " + serviceUri.toASCIIString());
            count++;
        }
        Assert.assertEquals(numServices, count);

    }

    @Test
    public void testListServices() throws Exception {
        // The test depends on the number of services previously updloaded
        List<URI> services = ManagerSingleton.getInstance().listServices();
        Assert.assertEquals(numServices, services.size());
    }

    @Test
    public void testListDocuments() throws Exception {
        // The test depends on the number of services previously updloaded
        List<URI> documents = ManagerSingleton.getInstance().listDocuments();
        Assert.assertEquals(numServices, documents.size());
    }

    @Test
    public void testGetDocument() throws Exception {

        InputStream is;
        URI docUri;
        Random rand = new Random();
        List<URI> documents = ManagerSingleton.getInstance().listDocuments();
        int numDocs = documents.size();
        int delta = numDocs / 10;
        int index = rand.nextInt(10 - 0 + 1) + 0;
        while (index < numDocs) {
            docUri = documents.get(index);
            System.out.println("Obtaining document: " + docUri);
            is = ManagerSingleton.getInstance().getDocument(docUri);
            Assert.assertNotNull(is);
            index += delta;
        }
    }

//
//    @Test
//    public void testRegisterService() throws Exception {
//
//    }
//

    @Test
    public void testGetService() throws Exception {
        // Ensure that reader and writer work fine
        // Also depends on obtaining the right document
        ServiceReader reader = new ServiceReaderImpl();
        ServiceWriterImpl writer = new ServiceWriterImpl();
        Model obtainedModel, fileModel;

        Service svc;
        InputStream docStream;

        List<URI> services = ManagerSingleton.getInstance().listServices();
        for (URI svcUri : services) {
            svc = ManagerSingleton.getInstance().getService(svcUri);
            obtainedModel = writer.generateModel(svc);

            docStream = ManagerSingleton.getInstance().getDocument(svc.getSource());
            fileModel = ModelFactory.createDefaultModel();
            fileModel.read(docStream, Syntax.TTL.getName());
            Assert.assertTrue(fileModel.isIsomorphicWith(obtainedModel));
        }
    }
//
//    @Test
//    public void testGetServices() throws Exception {
//
//    }
//
//    @Test
//    public void testListDocumentsForService() throws Exception {
//
//    }
//
//    @Test
//    public void testExportService() throws Exception {
//
//    }

//    @Test
//    public void testUnregisterService() throws Exception {
//
//    }

}
