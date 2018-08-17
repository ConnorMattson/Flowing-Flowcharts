import java.util.ArrayList;
import java.util.Arrays;
// TODO remove * once I have identified which specifics I need
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.lang.reflect.*;

public class FlowingFlowcharts extends JFrame {


	private JMenuItem fileNew, fileClose; //, fileOpen, fileSave, fileSaveAs, fileExport;
	private final ArrayList<JMenuItem> menusToDisable = new ArrayList<JMenuItem>();
	private final FlowchartDrawingArea displayPanel;
	
	private Boolean flowchartIsOpen = false;
	private FlowElement rootElement = null;
	private FlowElement currentlySelectedElement = null;

	private Dimension frameSize;

	public FlowingFlowcharts() {
		super("Flowing Flowcharts");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(810, 410));
		addComponentListener(new ResizeListener());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootElement = null;

		displayPanel = new FlowchartDrawingArea();
		displayPanel.setBackground(Color.WHITE);

		frameSize = displayPanel.getBounds().getSize();
		System.out.println(frameSize);
		setContentPane(displayPanel);
		
		setupKeyBindings();
		setupMenuBar();
		setupMainmenu();
		setVisible(true);
	}

	private void setupKeyBindings() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(new KeyEventPostProcessor() {
			private boolean shiftToggle = false;

	        public boolean postProcessKeyEvent(KeyEvent e) {
	            int keyPressed = e.getKeyCode();

	            if (e.getID() == KeyEvent.KEY_PRESSED && flowchartIsOpen) {

	            	// If the key pressed is a typical latin character or punctuation
	            	if ((keyPressed >= 44 && keyPressed <= 111) || keyPressed == 192 || keyPressed == 222 || keyPressed == 32) {
	            		currentlySelectedElement.addText(e.getKeyChar());
	            	}

	            	// If the key pressed is backspace
	            	else if (keyPressed == 8) {
	            		currentlySelectedElement.deleteText();
	            	}

	            	// Im not sure what do with enter yet (10)

	            	// delete (127)
	            	else if (keyPressed == 127) {
	            		currentlySelectedElement = currentlySelectedElement.delete(rootElement);
	            	}

	            	// shift (16)

	            	// ctrl (17)

	            	// alt 18

	            	// escape (27)

	            	// arrow keys (LURD - 37 38 39 40)
	            	if (e.isShiftDown()) {
	            		if (keyPressed == 37) {
	            			FlowElement newlySelected = currentlySelectedElement.firstElementInDirection(Directions.LEFT);
	            			if (newlySelected != null) {
	            				currentlySelectedElement.setSelected(false);
	            				newlySelected.setSelected(true);
	            				currentlySelectedElement = newlySelected;
	            				rootElement.moveBranch(250,0);
	            			}
	            		}

		            	else if (keyPressed == 38) {
	            			FlowElement newlySelected = currentlySelectedElement.firstElementInDirection(Directions.UP);
	            			if (newlySelected != null) {
	            				currentlySelectedElement.setSelected(false);
	            				newlySelected.setSelected(true);
	            				currentlySelectedElement = newlySelected;
	            				rootElement.moveBranch(0,100);
	            			}
	            		}

		            	else if (keyPressed == 39) {
	            			FlowElement newlySelected = currentlySelectedElement.firstElementInDirection(Directions.RIGHT);
	            			if (newlySelected != null) {
	            				currentlySelectedElement.setSelected(false);
	            				newlySelected.setSelected(true);
	            				currentlySelectedElement = newlySelected;
	            				rootElement.moveBranch(-250,0);
	            			}
	            		}

		            	else if (keyPressed == 40) {
	            			FlowElement newlySelected = currentlySelectedElement.firstElementInDirection(Directions.DOWN);
	            			if (newlySelected != null) {
	            				currentlySelectedElement.setSelected(false);
	            				newlySelected.setSelected(true);
	            				currentlySelectedElement = newlySelected;
	            				rootElement.moveBranch(0,-100);
	            			}
	            		}
	            	}

	            	else {
		            	if (keyPressed == 37) currentlySelectedElement = currentlySelectedElement.addChild(Directions.LEFT, rootElement);
		            	else if (keyPressed == 38) currentlySelectedElement = currentlySelectedElement.addChild(Directions.UP, rootElement);
		            	else if (keyPressed == 39) currentlySelectedElement = currentlySelectedElement.addChild(Directions.RIGHT, rootElement);
		            	else if (keyPressed == 40) currentlySelectedElement = currentlySelectedElement.addChild(Directions.DOWN, rootElement);
	            	}
	            }

	            displayPanel.repaint();
	            // System.out.println(e.getKeyCode());
	            return true;
	        }
	    });
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
		System.out.println(frameSize.height/2 - 50);
		rootElement = new FlowElement(frameSize.width/2 - 100, frameSize.height/2 - 25);
		rootElement.setSelected(true);		
		currentlySelectedElement = rootElement;
		flowchartIsOpen = true;
		displayPanel.setRootElement(rootElement);
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

	private class ResizeListener implements ComponentListener {

        public void componentHidden(ComponentEvent e) {}
        public void componentMoved(ComponentEvent e) {}
        public void componentShown(ComponentEvent e) {}

        public void componentResized(ComponentEvent e) {
			Dimension newFrameSize = displayPanel.getBounds().getSize();

			if (rootElement != null) rootElement.moveBranch((newFrameSize.width - frameSize.width)/2 , (newFrameSize.height - frameSize.height)/2);
			frameSize = newFrameSize;
        }   
    }
}