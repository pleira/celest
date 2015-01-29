package be.angelcorp.celest.examples


import javax.swing.{WindowConstants, JFrame}

import org.jfree.chart.{ChartPanel, JFreeChart}
import org.jfree.chart.axis.{ValueAxis, NumberAxis}
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.{XYItemRenderer, XYLineAndShapeRenderer, XYSplineRenderer}
import org.jfree.data.xy.{XYSeriesCollection, DefaultXYDataset, XYDataset, XYSeries}

package object plot {

  type Chart = JFreeChart

  /**
   * @param x Plot x dataset
   * @param y Plot y dataset
   * @param title Name of the plot
   */
  case class XYTrace( x: Array[Double], y: Array[Double], title: String = "trace") {
    require( x.length == y.length )

    implicit val data = new XYSeries(title, false, true)
    (x zip y) foreach {
      case ( px, py ) => data.add(px, py)
    }

    def plot() =
      PlotFactory( title, List(this), false ).plot()

  }

  /**
   * @param title  Title of
   * @param traces
   * @param smooth Connects data points with natural cubic splines
   */
  case class PlotFactory( title: String = "unnamed", traces: List[XYTrace], smooth: Boolean = false ) {

    lazy val xAxis: ValueAxis = new NumberAxis()
    lazy val yAxis: ValueAxis = new NumberAxis()

    lazy val renderer: XYItemRenderer =
      if (smooth)
        new XYSplineRenderer()
      else
        new XYLineAndShapeRenderer()

    def chart(): JFreeChart = {
      val dataset = {
        val coll = new XYSeriesCollection()
        for (trace <- traces ) {
          coll.addSeries( trace.data )
        }
        coll
      }
      val freePlot = new XYPlot( dataset, xAxis, yAxis, renderer )
      val chart = new JFreeChart( title, freePlot )
      chart
    }

    def plot(): Unit = {
      val plotPanel = new ChartPanel( chart() )
      val frame = new JFrame(title)
      frame.setContentPane( plotPanel )
      frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE )
      frame.setSize(640, 480)
      frame.setLocationRelativeTo(null)
      frame.setVisible(true)
    }

  }

  def plot( x: Array[Double], y: Array[Double], title: String = "" ) =
    XYTrace( x, y, title ).plot()

}
