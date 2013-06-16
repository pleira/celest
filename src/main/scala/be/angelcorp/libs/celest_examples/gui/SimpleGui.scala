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
package be.angelcorp.libs.celest_examples.gui;

import java.awt._
import java.awt.event._
import javax.swing._
import javax.swing.event._
import javax.swing.tree._

import net.miginfocom.swing.MigLayout

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import be.angelcorp.libs.util.gui.config.confgGui.ConfigGui

class SimpleGui(examples: Seq[Example]) extends JFrame {
	val logger	= LoggerFactory.getLogger(classOf[SimpleGui])

  // Inistialize frame
	setTitle("Celest examples")
  setSize(600, 400)
  setLayout(new MigLayout("fill", "[fill]", "[]10[fill]10[]"))
  add(new JLabel("Celest Examples", SwingConstants.CENTER), "wrap")

  // Fill the frame with content
  val examples_list_scroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED)
  add(examples_list_scroll)
  add(examples_list_scroll, "wrap")
  val tree = new JTree(new DefaultTreeModel(makeTree()))
  examples_list_scroll.add(tree)
  val details = new TextArea()
  add(details, "wrap")
  tree.addTreeSelectionListener(new TreeSelectionListener() {
    override def valueChanged(e: TreeSelectionEvent) {
      val node = tree.getLastSelectedPathComponent.asInstanceOf[DefaultMutableTreeNode]
      details.setText( node.getUserObject match {
        case example: Example => example.annotation.description()
        case o => "Example description error for " + o.getClass
      } )
    }
  })

  val exit = new JButton("Exit")
  exit.addActionListener(new ActionListener() {
    override def actionPerformed(e: ActionEvent) { SimpleGui.this.dispose() }
  })
  add(exit, "split 3")

  val settings = new JButton("Settings")
  settings.addActionListener(new ActionListener() {
    override def actionPerformed(e: ActionEvent) {
      val gui = new ConfigGui()
      gui.setVisible(true)
      gui.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE )
    }
  })
  add(settings, "")

  val run = new JButton("Run example")
  run.addActionListener(new ActionListener() {
    override def actionPerformed(e: ActionEvent) {
      val node = tree.getLastSelectedPathComponent.asInstanceOf[DefaultMutableTreeNode]
      node.getUserObject match {
        case example: Example => example.invoke
        case _  =>
          logger.warn("You need to select an example before pressing RUN")
          details.setText("Select an example to run!")
      }
    }
  })
  add(run, "wrap")

  // Finalize frame construction
  setLocationRelativeTo(null)
  setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
  setVisible(true)


	def makeTree() = {
		val root = new DefaultMutableTreeNode("Examples")
    examples.foreach( example => root.add(new DefaultMutableTreeNode(example)) )
		root
	}
}
