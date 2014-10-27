/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.examples.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class Services {

    private static final Logger logger = LoggerFactory.getLogger(Services.class);

    public static File newFile(String relativePath) {
        String output = new File(".").getAbsolutePath();
        File outputDir = new File(output);
        if (!outputDir.exists()) {
            logger.info("Creating non existent output directory: {}", outputDir);
            boolean success = outputDir.mkdirs();
            if (!success)
                throw new RuntimeException("Could not create output directory: " + outputDir);
        }
        File f = new File(output, relativePath);
        logger.debug("Created new output file reference: {}", f);
        return f;
    }

}
