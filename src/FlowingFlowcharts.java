import java.util.ArrayList;
// TODO remove * once I have identified which specifics I need
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.lang.reflect.*;

public class FlowingFlowcharts extends JFrame {
	private JMenuItem fileNew, fileClose; //, fileOpen, fileSave, fileSaveAs, fileExport;
	private final ArrayList<JMenuItem> menusToDisable = new ArrayList<JMenuItem>();
	private final Boolean flowchartIsOpen = false;
	private final JPanel displayPanel;

	public FlowingFlowcharts() {
		super("Flowing Flowcharts");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		displayPanel = new JPanel();
		displayPanel.setBackground(Color.WHITE);
		setContentPane(displayPanel);
		
		setupMenuBar();
		setupMainmenu();
		setVisible(true);
	}

	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		fileNew = new JMenuItem("New");
		fileMenu.add(fileNew);
		fileNew.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newFlowchart();
				}
			}
        );

		// fileOpen = new JMenuItem("Open");
		// fileMenu.add(fileOpen);
		// fileOpen.addActionListener(
		// 	new ActionListener() {
		// 		public void actionPerformed(ActionEvent e) {
		// 			openFlowchart();
		// 		}
		// 	}
  //       );

		// fileSave = new JMenuItem("Save");
		// fileMenu.add(fileSave);
		// menusToDisable.add(fileSave);
		// fileSave.addActionListener(
		// 	new ActionListener() {
		// 		public void actionPerformed(ActionEvent e) {
		// 			// TODO
		// 		}
		// 	}
  //       );

  //       fileSaveAs = new JMenuItem("Save As");
		// fileMenu.add(fileSaveAs);
		// menusToDisable.add(fileSaveAs);
		// fileSaveAs.addActionListener(
		// 	new ActionListener() {
		// 		public void actionPerformed(ActionEvent e) {
		// 			// TODO
		// 		}
		// 	}
  //       );

		// fileExport = new JMenuItem("Export");
		// fileMenu.add(fileExport);
		// menusToDisable.add(fileExport);
		// fileExport.addActionListener(
		// 	new ActionListener() {
		// 		public void actionPerformed(ActionEvent e) {
		// 			// TODO
		// 		}
		// 	}
  //       );

		fileClose = new JMenuItem("Close");
		fileMenu.add(fileClose);
		menusToDisable.add(fileClose);
		fileClose.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					displayPanel.removeAll();
					displayPanel.revalidate();
					displayPanel.repaint();
					setupMainmenu();
				}
			}
        );

		JMenuItem fileMenuQuit = new JMenuItem("Quit");
		fileMenu.add(fileMenuQuit);
		fileMenuQuit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);
	}

	private void newFlowchart() {
		for (JMenuItem menu : menusToDisable) {
			menu.setEnabled(true);
		}

		if (flowchartIsOpen); // TODO

		displayPanel.removeAll();
		displayPanel.revalidate();
		displayPanel.repaint();
	}

	private void openFlowchart() {
		for (JMenuItem menu : menusToDisable) {
			menu.setEnabled(true);
		}

		if (flowchartIsOpen); // TODO
	}

	private void setupMainmenu() {
		for (JMenuItem menu : menusToDisable) {
			menu.setEnabled(false);
		}

		JButton newFlowchartButton = new JButton("New");
		newFlowchartButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newFlowchart();
				}
			}
		);
		displayPanel.add(newFlowchartButton);

		JButton openFlowchartButton = new JButton("Open");
		openFlowchartButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//openFlowchart();
				}
			}
		);
		openFlowchartButton.setEnabled(false);
		displayPanel.add(openFlowchartButton);
	}
}