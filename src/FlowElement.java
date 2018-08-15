import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.lang.StringBuilder;
import java.lang.Math;

public class FlowElement {
	
	protected FlowElement parent;
	protected Directions parentDirection;
	protected ArrayList<FlowElement> leftChildren = new ArrayList<FlowElement>();
	protected ArrayList<FlowElement> upChildren = new ArrayList<FlowElement>();
	protected ArrayList<FlowElement> rightChildren = new ArrayList<FlowElement>();
	protected ArrayList<FlowElement> downChildren = new ArrayList<FlowElement>();
	private double topLeftX;
	private double topLeftY;
	private boolean isSelected = false;

	public StringBuilder text = new StringBuilder();

	public FlowElement(double x,double y) {
		parent = null;
		parentDirection = Directions.NONE;
		topLeftX = x;
		topLeftY = y;
	}

	public FlowElement(FlowElement parent, Directions parentDirection, double x, double y) {
		this.parent = parent;
		this.parentDirection = parentDirection;
		topLeftX = x;
		topLeftY = y;
	}

	public FlowElement firstElementInDirection(Directions direction) {
		if (parentDirection == direction) return parent;
		switch (direction) {
			case LEFT: return (leftChildren.size() > 0) ? leftChildren.get(0) : null;
			case UP: return (upChildren.size() > 0) ? upChildren.get(0) : null;
			case RIGHT: return (rightChildren.size() > 0) ? rightChildren.get(0) : null;
			case DOWN: return (downChildren.size() > 0) ? downChildren.get(0) : null;
		}
		return null;
	}

	public FlowElement delete(FlowElement rootElement) {
		if (parent != null) {
			// I should probably have a warning here that this will delete all children
			switch (parentDirection) {
				case LEFT:
					parent.rightChildren.remove(parent.rightChildren.indexOf(this));
					rootElement.moveBranch(250, 0);

				case UP:
					parent.downChildren.remove(parent.downChildren.indexOf(this));
					rootElement.moveBranch(0, 100);

				case RIGHT:
					parent.leftChildren.remove(parent.leftChildren.indexOf(this));
					rootElement.moveBranch(-250, 0);

				case DOWN:
					parent.upChildren.remove(parent.upChildren.indexOf(this));
					rootElement.moveBranch(0, -100);
			}
			parent.setSelected(true);
			return parent;
		}
		return this;
	}

	public void move(double xDelta, double yDelta) {
		topLeftX += xDelta;
		topLeftY += yDelta;
	}

	public void moveBranch(double xDelta, double yDelta) {
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
		int floorTopLeftX = (int)Math.floor(topLeftX);
		int floorTopLeftY = (int)Math.floor(topLeftY);

		if (isSelected) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(floorTopLeftX, floorTopLeftY, 200, 50);
		}

		g.setColor(Color.BLACK);
		g.drawRect(floorTopLeftX, floorTopLeftY, 200, 50);

		// TODO wrap text
		// character width
		// cWidth = 
		// int width = g.getFontMetrics().stringWidth(text); use this?
		// 1. is text longer than line
		// 2. cut string at end of last word before the next word will cause the line
		//    to be too long
		// 3. print all the strings
		// or
		// 1. get next word width
		// 2. (word width + current width < max width) ? print word : new line, goto 1
		// but this causes problems with knowing how many lines to print and allignment.
		// instead of printing append to 2d array and count lines then print all?

		g.drawString(text.toString(), floorTopLeftX+100, floorTopLeftY+25);

		if (leftChildren.size() > 0) {
			int[] xCoords = {floorTopLeftX-40, floorTopLeftX-30, floorTopLeftX-30, floorTopLeftX-10, floorTopLeftX-10, floorTopLeftX-30, floorTopLeftX-30};
			int[] yCoords = {floorTopLeftY+25, floorTopLeftY+15, floorTopLeftY+20, floorTopLeftY+20, floorTopLeftY+29, floorTopLeftY+29, floorTopLeftY+34};
			for (FlowElement child: leftChildren) {
				g.drawPolygon(xCoords, yCoords, 7);
				child.draw(g);
			}
		}
		
		if (upChildren.size() > 0) {
			int[] xCoords = {floorTopLeftX+75, floorTopLeftX+85, floorTopLeftX+94, floorTopLeftX+89, floorTopLeftX+89, floorTopLeftX+80, floorTopLeftX+80};
			int[] yCoords = {floorTopLeftY-30, floorTopLeftY-40, floorTopLeftY-30, floorTopLeftY-30, floorTopLeftY-10, floorTopLeftY-10, floorTopLeftY-30};
			for (FlowElement child: upChildren) {
				g.drawPolygon(xCoords, yCoords, 7);
				child.draw(g);
			}
		}

		if (rightChildren.size() > 0) {
			int[] xCoords = {floorTopLeftX+210, floorTopLeftX+230, floorTopLeftX+230, floorTopLeftX+240, floorTopLeftX+230, floorTopLeftX+230, floorTopLeftX+210};
			int[] yCoords = {floorTopLeftY+20, floorTopLeftY+20, floorTopLeftY+15, floorTopLeftY+25, floorTopLeftY+34, floorTopLeftY+29, floorTopLeftY+29};
			for (FlowElement child: rightChildren) {
				g.drawPolygon(xCoords, yCoords, 7);
				child.draw(g);
			}
		}

		if (downChildren.size() > 0) {
			int[] xCoords = {floorTopLeftX+80, floorTopLeftX+89, floorTopLeftX+89, floorTopLeftX+94, floorTopLeftX+85, floorTopLeftX+75, floorTopLeftX+80};
			int[] yCoords = {floorTopLeftY+60, floorTopLeftY+60, floorTopLeftY+80, floorTopLeftY+80, floorTopLeftY+90, floorTopLeftY+80, floorTopLeftY+80};
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
					child = new FlowElement(this, Directions.RIGHT, topLeftX-250, topLeftY);
					leftChildren.add(child);
					this.setSelected(false);
					child.setSelected(true);
					return child;

				case UP:
					rootElement.moveBranch(0,100);
					child = new FlowElement(this, Directions.DOWN, topLeftX, topLeftY-100);
					upChildren.add(child);
					this.setSelected(false);
					child.setSelected(true);
					return child;

				case RIGHT:
					rootElement.moveBranch(-250,0);
					child = new FlowElement(this, Directions.LEFT, topLeftX+250, topLeftY);
					rightChildren.add(child);
					this.setSelected(false);
					child.setSelected(true);
					return child;

				case DOWN:
					rootElement.moveBranch(0,-100);
					child = new FlowElement(this, Directions.UP, topLeftX, topLeftY+100);
					downChildren.add(child);
					this.setSelected(false);
					child.setSelected(true);
					return child;
			}
		}

		return this;
	}
}