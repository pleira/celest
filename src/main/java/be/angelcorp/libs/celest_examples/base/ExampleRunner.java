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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleRunner {

	private static final Logger	logger	= LoggerFactory.getLogger(ExampleRunner.class);

	public void invoke(Class<Runnable> clazz) {
		logger.info("Invoking example: {}", clazz.getSimpleName());

		try {
			Thread thread = new Thread(clazz.newInstance());
			thread.start();
		} catch (InstantiationException e) {
			logger.error("Could not construct example ({}), make shure a default constructor is available", clazz, e);
		} catch (IllegalAccessException e) {
			logger.error("Could not access the default constructor of example: {}", clazz, e);
		}
	}
}
