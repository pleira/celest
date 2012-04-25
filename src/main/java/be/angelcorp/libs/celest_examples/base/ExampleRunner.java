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
