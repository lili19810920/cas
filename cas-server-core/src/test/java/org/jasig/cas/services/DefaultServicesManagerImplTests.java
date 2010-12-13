/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.cas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jasig.cas.server.authentication.Service;

import junit.framework.TestCase;
import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.RegisteredService;

import static org.mockito.Mockito.*;

/**
 * 
 * @author battags
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.0
 *
 */
public class DefaultServicesManagerImplTests extends TestCase {
    
    private DefaultServicesManagerImpl defaultServicesManagerImpl;

    protected void setUp() throws Exception {
        final InMemoryServiceRegistryDaoImpl dao = new InMemoryServiceRegistryDaoImpl();
        final List<RegisteredService> list = new ArrayList<RegisteredService>();
        
        final RegisteredServiceImpl r = new RegisteredServiceImpl();
        r.setId(2500);
        r.setServiceId("serviceId");
        r.setName("serviceName");
        r.setEvaluationOrder(1);
        
        list.add(r);
        
        dao.setRegisteredServices(list);
        this.defaultServicesManagerImpl = new DefaultServicesManagerImpl(dao);
    }
    
    public void testSaveAndGet() {
        final RegisteredServiceImpl r = new RegisteredServiceImpl();
        r.setId(1000);
        r.setName("test");
        r.setServiceId("test");
        
        this.defaultServicesManagerImpl.save(r);
        assertNotNull(this.defaultServicesManagerImpl.findServiceBy(1000));
    }
    
    public void testDeleteAndGet() {
        final RegisteredServiceImpl r = new RegisteredServiceImpl();
        r.setId(1000);
        r.setName("test");
        r.setServiceId("test");
        
        this.defaultServicesManagerImpl.save(r);
        assertEquals(r, this.defaultServicesManagerImpl.findServiceBy(r.getId()));
        
        this.defaultServicesManagerImpl.delete(r.getId());
        assertNull(this.defaultServicesManagerImpl.findServiceBy(r.getId()));
    }
    
    public void testDeleteNotExistentService() {
        assertNull(this.defaultServicesManagerImpl.delete(1500));
    }
    
    public void testMatchesExistingService() {
        final RegisteredServiceImpl r = new RegisteredServiceImpl();
        r.setId(1000);
        r.setName("test");
        r.setServiceId("test");

        final Access access = mock(Access.class);
        when(access.getResourceIdentifier()).thenReturn("test");

        final Access access2 = mock(Access.class);
        when(access2.getResourceIdentifier()).thenReturn("fdfa");

        this.defaultServicesManagerImpl.save(r);
        
        assertTrue(this.defaultServicesManagerImpl.matchesExistingService(access));
        assertEquals(r, this.defaultServicesManagerImpl.findServiceBy(access));
        assertNull(this.defaultServicesManagerImpl.findServiceBy(access2));
    }
    
    public void testAllService() {
        final RegisteredServiceImpl r = new RegisteredServiceImpl();
        r.setId(1000);
        r.setName("test");
        r.setServiceId("test");
        r.setEvaluationOrder(2);
        
        this.defaultServicesManagerImpl.save(r);
        
        assertEquals(2, this.defaultServicesManagerImpl.getAllServices().size());
        assertTrue(this.defaultServicesManagerImpl.getAllServices().contains(r));
    }
    
    protected class SimpleService implements Service {
        
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 6572142033945243669L;
        private String id;

        protected SimpleService(final String id) {
            this.id = id;
        }
        
        public Map<String, Object> getAttributes() {
            return null;
        }

        public String getId() {
            return this.id;
        }

        public void setPrincipal(AttributePrincipal principal) {
            // nothing to do
        }

        public boolean logOutOfService(String sessionIdentifier) {
            return false;
        }
        
        public boolean matches(Service service) {
            return true;
        }
    }
}