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

import be.angelcorp.libs.math.plot.IPlot2;
import be.angelcorp.libs.math.plot.Plot2;
import be.angelcorp.libs.util.gui.config.ConfigField;
import be.angelcorp.libs.util.gui.objectGuiSerializer.selectableList.SelectableList;
import com.google.inject.Binder;
import com.google.inject.Module;

import java.util.Collection;
import java.util.LinkedList;

public class Settings extends be.angelcorp.libs.util.gui.config.Settings {

    @ConfigField(name = "Plotting library", description = "")
    final public SelectableList<Class<? extends IPlot2>> plottingLibrary;
    @ConfigField(name = "Output directory", description = "Location where any generated files are stored")
    final public String outputDir = ".";

    public Settings() {
        setName("All settings");

        plottingLibrary = new SelectableList<>((Class<Class<? extends IPlot2>>) IPlot2.class.getClass());
        plottingLibrary.put(Plot2.class, true);
        try {
            Class<IPlot2> clazz = (Class<IPlot2>) getClass().getClassLoader().loadClass(
                    "be.angelcorp.libs.connect.matlabConnect.MPlot2");
            plottingLibrary.put(clazz, false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // plottingLibrary.put(MPlot2.class, new Boolean(true));
    }

    @Override
    public Collection<Module> getInjectorModules() {
        LinkedList<Module> modules = new LinkedList<>();
        modules.add(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(Settings.class).toInstance(Settings.this);
            }
        });
        modules.add(new Module() {
            @Override
            public void configure(Binder binder) {
                Class<? extends IPlot2> plotter = plottingLibrary.getSelected().get(0);
                binder.bind(IPlot2.class).to(plotter);
            }
        });
        return modules;
    }
}
