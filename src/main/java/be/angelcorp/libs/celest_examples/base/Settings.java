package be.angelcorp.libs.celest_examples.base;

import be.angelcorp.libs.math.matlab.MPlot2;
import be.angelcorp.libs.math.plot.IPlot2;
import be.angelcorp.libs.math.plot.Plot2;
import be.angelcorp.libs.util.gui.config.ConfigField;
import be.angelcorp.libs.util.gui.objectGuiSerializer.selectableList.SelectableList;

public class Settings extends be.angelcorp.libs.util.gui.config.Settings {

	@ConfigField(name = "Plotting library", description = "")
	public SelectableList<Class<? extends IPlot2>>	plottingLibrary;
	@ConfigField(name = "Output directory", description = "Location where any generated files are stored")
	public String									outputDir	= ".";

	public Settings() {
		setName("All settings");

		plottingLibrary = new SelectableList<>((Class<Class<? extends IPlot2>>) IPlot2.class.getClass());
		plottingLibrary.put(Plot2.class, new Boolean(false));
		plottingLibrary.put(MPlot2.class, new Boolean(true));
	}
}
