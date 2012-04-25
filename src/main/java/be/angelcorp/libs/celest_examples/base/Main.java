package be.angelcorp.libs.celest_examples.base;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.reflections.Reflections;

import com.lyndir.lhunath.lib.system.logging.Logger;

public class Main implements Runnable {

	public static void main(String[] args) {
		Main runner = new Main();
		runner.run();
	}

	private String				examplesPackage	= "be.angelcorp.libs.celest_examples";
	private static final Logger	logger			= Logger.get(ExampleRunner.class);

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
				logger.dbg("Found CelestExample class: %s", raw_example);
				examples.add((Class<Runnable>) raw_example);
			} else {
				logger.dbg("Found a class tagged with CelestExample, which is not a Runnable: %s", raw_example);
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
