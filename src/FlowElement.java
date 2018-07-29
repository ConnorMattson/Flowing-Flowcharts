import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.lang.StringBuilder;

public class FlowElement {
	
	private FlowElement parent;
	private Directions parentDirection;
	private ArrayList<FlowElement> leftChildren = new ArrayList<FlowElement>();
	private ArrayList<FlowElement> upChildren = new ArrayList<FlowElement>();
	private ArrayList<FlowElement> rightChildren = new ArrayList<FlowElement>();
	private ArrayList<FlowElement> downChildren = new ArrayList<FlowElement>();
	private int topLeftX;
	private int topLeftY;
	private boolean isSelected = false;

	public StringBuilder text = new StringBuilder();

	public FlowElement() {
		parent = null;
		parentDirection = Directions.NONE;
		topLeftX = 150;
		topLeftY = 150;
	}

	public FlowElement(FlowElement parent, Directions parentDirection) {
		this.parent = parent;
		this.parentDirection = parentDirection;
		topLeftX = 150;
		topLeftY = 150;
	}

	public void move(int xDelta, int yDelta) {
		topLeftX += xDelta;
		topLeftY += yDelta;
	}

	public void moveBranch(int xDelta, int yDelta) {
		move(xDelta, yDelta);
		for (FlowElement child: leftChildren) child.moveBranch(xDelta, yDelta);
		for (FlowElement child: upChildren) child.moveBranch(xDelta, yDelta);
		for (FlowElement child: rightChildren) child.moveBranch(xDelta, yDelta);
		for (FlowElement child: downChildren) child.moveBranch(xDelta, yDelta);
	}

	public void setSelected(boolean bool) {
		isSelected = bool;
	}

	public void draw(Graphics g) {
		if (isSelected) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(topLeftX, topLeftY, 200, 50);
		}

		g.setColor(Color.BLACK);
		g.drawRect(topLeftX, topLeftY, 200, 50);
		g.drawString(text.toString(), topLeftX+100, topLeftY+25);

		if (leftChildren.size() > 0) {
			int[] xCoords = {topLeftX-40, topLeftX-30, topLeftX-30, topLeftX-10, topLeftX-10, topLeftX-30, topLeftX-30};
			int[] yCoords = {topLeftY+25, topLeftY+15, topLeftY+20, topLeftY+20, topLeftY+29, topLeftY+29, topLeftY+34};
			for (FlowElement child: leftChildren) {
				g.drawPolygon(xCoords, yCoords, 7);
				child.draw(g);
			}
		}
		
		if (upChildren.size() > 0) {
			int[] xCoords = {topLeftX+75, topLeftX+85, topLeftX+94, topLeftX+89, topLeftX+89, topLeftX+80, topLeftX+80};
			int[] yCoords = {topLeftY-30, topLeftY-40, topLeftY-30, topLeftY-30, topLeftY-10, topLeftY-10, topLeftY-30};
			for (FlowElement child: upChildren) {
				g.drawPolygon(xCoords, yCoords, 7);
				child.draw(g);
			}
		}

		if (rightChildren.size() > 0) {
			int[] xCoords = {topLeftX+210, topLeftX+230, topLeftX+230, topLeftX+240, topLeftX+230, topLeftX+230, topLeftX+210};
			int[] yCoords = {topLeftY+20, topLeftY+20, topLeftY+15, topLeftY+25, topLeftY+34, topLeftY+29, topLeftY+29};
			for (FlowElement child: rightChildren) {
				g.drawPolygon(xCoords, yCoords, 7);
				child.draw(g);
			}
		}

		if (downChildren.size() > 0) {
			int[] xCoords = {topLeftX+80, topLeftX+89, topLeftX+89, topLeftX+94, topLeftX+85, topLeftX+75, topLeftX+80};
			int[] yCoords = {topLeftY+60, topLeftY+60, topLeftY+80, topLeftY+80, topLeftY+90, topLeftY+80, topLeftY+80};
			for (FlowElement child: downChildren) {
				g.drawPolygon(xCoords, yCoords, 7);
				child.draw(g);
			}
		}
	}

	public FlowElement addChild(Directions childDirection, FlowElement rootElement) {
		if (childDirection != parentDirection) {
			FlowElement child;
			switch (childDirection) {
				case LEFT:
					rootElement.moveBranch(250,0);
					child = new FlowElement(this, Directions.RIGHT);
					leftChildren.add(child);
					this.setSelected(false);
					child.setSelected(true);
					return child;

				case UP:
					rootElement.moveBranch(0,100);
					child = new FlowElement(this, Directions.DOWN);
					upChildren.add(child);
					this.setSelected(false);
					child.setSelected(true);
					return child;

				case RIGHT:
					rootElement.moveBranch(-250,0);
					child = new FlowElement(this, Directions.LEFT);
					rightChildren.add(child);
					this.setSelected(false);
					child.setSelected(true);
					return child;

				case DOWN:
					rootElement.moveBranch(0,-100);
					child = new FlowElement(this, Directions.UP);
					downChildren.add(child);
					this.setSelected(false);
					child.setSelected(true);
					return child;
			}
		}

		return this;
	}
}