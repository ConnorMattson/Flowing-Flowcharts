import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.lang.Math;
import java.awt.Color;

public class FlowchartDrawingArea extends JPanel {
	private FlowElement rootElement;

	public FlowchartDrawingArea() {
		rootElement = null;
	}

	public void setRootElement(FlowElement element) {
		rootElement = element;
	}

	public void clearRootElement() {
		rootElement = null;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (rootElement != null) rootElement.draw(g);
	}
}