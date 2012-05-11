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

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.angelcorp.libs.util.gui.config.Config;
import be.angelcorp.libs.util.gui.objectGuiSerializer.ObjectGuiSerializer;
import be.angelcorp.libs.util.gui.objectGuiSerializer.selectableList.SelectableListSerializer;

public class Main implements Runnable {

	private static final Logger	logger	= LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		Main runner = new Main();
		runner.run();
	}

	private String	examplesPackage	= "be.angelcorp.libs.celest_examples";

	public Main() {
		Config config = new Config();
		config.addSettings(new Settings());
		Config.setInstance(config);

		ObjectGuiSerializer.getInstance().addGuiSerializer(new SelectableListSerializer<>());
	}

	private TreeSet<Class<Runnable>> discoverExamples() {
		Reflections reflections = new Reflections(examplesPackage);
		Set<Class<?>> raw_examples = reflections.getTypesAnnotatedWith(CelestExample.class, true);

		TreeSet<Class<Runnable>> examples = new TreeSet<>(new Comparator<Class<Runnable>>() {
			@Override
			public int compare(Class<Runnable> o1, Class<Runnable> o2) {
				String n1 = o1.getAnnotation(CelestExample.class).name();
				String n2 = o2.getAnnotation(CelestExample.class).name();
				return n1.compareTo(n2);
			}
		});
		for (Class<?> raw_example : raw_examples) {
			if (Runnable.class.isAssignableFrom(raw_example)) {
				logger.debug("Found CelestExample class: {}", raw_example.getSimpleName());
				examples.add((Class<Runnable>) raw_example);
			} else {
				logger.debug("Found a class tagged with CelestExample, which is not a Runnable: {}", raw_example);
			}
		}
		return examples;
	}

	@Override
	public void run() {
		TreeSet<Class<Runnable>> examples = discoverExamples();
		ExampleRunner invoker = new ExampleRunner();
		SimpleGui gui = new SimpleGui(examples, invoker);
	}
}
