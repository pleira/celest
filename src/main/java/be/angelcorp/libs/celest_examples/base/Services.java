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
package be.angelcorp.libs.celest_examples.base;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.angelcorp.libs.math.plot.IPlot2;
import be.angelcorp.libs.util.exceptions.GenericRuntimeException;
import be.angelcorp.libs.util.gui.config.Config;

import com.google.inject.Injector;

public abstract class Services {

	private static final Logger	logger	= LoggerFactory.getLogger(Services.class);

	private static Settings getSettings() {
		Injector injector = Config.getInjector();
		Settings settings = injector.getInstance(Settings.class);
		return settings;
	}

	public static File newFile(String relativePath) {
		String output = getSettings().outputDir;
		File outputDir = new File(output);
		if (!outputDir.exists()) {
			logger.info("Creating non existant output directory: {}", outputDir);
			boolean success = outputDir.mkdirs();
			if (!success)
				throw new GenericRuntimeException("Could not create output directory: %s", outputDir);
		}
		File f = new File(output, relativePath);
		logger.debug("Created new output file reference: {}", f);
		return f;
	}

	public static IPlot2 newPlot() {
		Injector injector = Config.getInjector();
		logger.debug("Creating new plot");
		IPlot2 plot = injector.getInstance(IPlot2.class);
		logger.debug("Created plot: {}", plot);
		return plot;
	}
}
