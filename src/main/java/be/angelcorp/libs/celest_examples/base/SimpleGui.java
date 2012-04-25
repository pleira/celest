package be.angelcorp.libs.celest_examples.base;

import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import net.miginfocom.swing.MigLayout;

import com.lyndir.lhunath.lib.system.logging.Logger;

public class SimpleGui extends JFrame {

	private class Example {

		private Class<Runnable>	clazz;

		public Example(Class<Runnable> runnable_example) {
			clazz = runnable_example;
		}

		public CelestExample annotation() {
			return clazz.getAnnotation(CelestExample.class);
		}

		public Class<Runnable> getClazz() {
			return clazz;
		}

		@Override
		public String toString() {
			return annotation().name();
		}
	}

	private static final Logger		logger	= Logger.get(SimpleGui.class);

	private Set<Class<Runnable>>	examples;
	private ExampleRunner			invoker;
	private TextArea				details;
	private JTree					tree;

	public SimpleGui(Set<Class<Runnable>> examples, ExampleRunner invoker) {
		this.examples = examples;
		this.invoker = invoker;

		setTitle("Celest examples");
		setSize(600, 400);
		fillFrame();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void fillFrame() {
		setLayout(new MigLayout("fill", "[fill]", "[]10[fill]10[]"));
		add(new JLabel("Celest Examples", JLabel.CENTER), "wrap");

		ScrollPane examples_list_scroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		add(examples_list_scroll);
		add(examples_list_scroll, "wrap");
		examples_list_scroll.add(tree = new JTree(new DefaultTreeModel(makeTree())));
		add(details = new TextArea(), "wrap");
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (Example.class.isAssignableFrom(node.getUserObject().getClass())) {
					Example example = (Example) node.getUserObject();
					String descr = example.annotation().description();
					if (descr == null || descr.length() == 0)
						descr = example.annotation().name();
					details.setText(descr);
				} else {
					details.setText("");
				}
			}
		});

		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleGui.this.dispose();
			}
		});
		add(exit, "split 2");

		JButton run = new JButton("Run example");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (Example.class.isAssignableFrom(node.getUserObject().getClass())) {
					invoker.invoke(((Example) node.getUserObject()).getClazz());
				} else {
					logger.wrn("You need to select an example before pressing RUN");
					details.setText("Select an example to run!");
				}
			}
		});
		add(run, "wrap");
	}

	private TreeNode makeTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Examples");
		for (Class<Runnable> runnable_example : examples) {
			Example example = new Example(runnable_example);
			root.add(new DefaultMutableTreeNode(example));
		}
		return root;
	}
}
