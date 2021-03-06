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

package uk.ac.open.kmi.iserve.discovery.disco.impl;

import com.google.inject.name.Names;
import org.jukito.JukitoModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Abstract class supporting the easy configuration of modules based on the global configuration file
 *
 * @author <a href="mailto:carlos.pedrinaci@open.ac.uk">Carlos Pedrinaci</a> (KMi - The Open University)
 * @since 11/10/2013
 */
public class ConfiguredTestModule extends JukitoModule {

    private static final Logger log = LoggerFactory.getLogger(ConfiguredTestModule.class);

    private static final String CONFIG_PROPERTIES_FILENAME = "config.properties";

    @Override
    protected void configureTest() {
        // Bind the configuration file to @named values
        Names.bindProperties(binder(), getProperties());
    }

    private Properties getProperties() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILENAME));
            return properties;
        } catch (IOException ex) {
            log.error("Error obtaining plugin properties", ex);
        }
        return new Properties();
    }
}
