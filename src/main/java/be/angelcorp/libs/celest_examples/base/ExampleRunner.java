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

import com.lyndir.lhunath.lib.system.logging.Logger;

public class ExampleRunner {

	private static final Logger	logger	= Logger.get(ExampleRunner.class);

	public void invoke(Class<Runnable> clazz) {
		logger.inf("Invoking example: %s", clazz);

		try {
			Thread thread = new Thread(clazz.newInstance());
			thread.start();
		} catch (InstantiationException e) {
			logger.err(e, "Could not construct example (%s), make shure a default constructor is available", clazz);
		} catch (IllegalAccessException e) {
			logger.err(e, "Could not access the default constructor of example: %s", clazz);
		}
	}
}
