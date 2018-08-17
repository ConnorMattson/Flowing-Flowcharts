import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.lang.StringBuilder;
import java.lang.Math;

public class FlowElement {
	
	protected FlowElement parent = null;
	protected Directions parentDirection = Directions.NONE;
	protected ArrayList<FlowElement> leftChildren = new ArrayList<FlowElement>();
	protected ArrayList<FlowElement> upChildren = new ArrayList<FlowElement>();
	protected ArrayList<FlowElement> rightChildren = new ArrayList<FlowElement>();
	protected ArrayList<FlowElement> downChildren = new ArrayList<FlowElement>();
	private double topLeftX;
	private double topLeftY;
	private boolean isSelected = false;
	private boolean wordsToolong = false;

	private ArrayList<String> words = new ArrayList<String>();

	public FlowElement(double x,double y) {
		topLeftX = x;
		topLeftY = y;
		words.add("");
	}

	public FlowElement(FlowElement parent, Directions parentDirection, double x, double y) {
		this.parent = parent;
		this.parentDirection = parentDirection;
		topLeftX = x;
		topLeftY = y;
		words.add("");
	}

	public void addText(char c) {
		if (c == ' ' && words.get(words.size() - 1) != "" && wordsToolong == false) words.add("");
		else if (wordsToolong == false) words.set(words.size() - 1, words.get(words.size() - 1) + c);
	}

	public void deleteText() {
		wordsToolong = false;

		int wordIndex = words.size() - 1;
		String word = words.get(wordIndex);
		int wordLength = word.length();

		if (wordLength > 0) words.set(wordIndex, word.substring(0, wordLength - 1));
		else if (words.size() > 1) words.remove(wordIndex);
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

		// TODO, fix bug where user types in one long line of text with no spaces
		ArrayList<String> stringToOutput = new ArrayList<String>();
		stringToOutput.add("");
		int lastLineWidth = 0;
		int spaceWidth = g.getFontMetrics().stringWidth(" ");

		for (String word: words) {
			int wordWidth = g.getFontMetrics().stringWidth(word);

			if (lastLineWidth + spaceWidth + wordWidth < 193) {
				int index = stringToOutput.size() - 1;
				stringToOutput.set(index, stringToOutput.get(index) + " " + word);
				lastLineWidth += wordWidth + spaceWidth;
			}
			else {
				stringToOutput.add(word);
				lastLineWidth = wordWidth;
			}
		}

		// delete the last character and stop more text from being typed 
		if (stringToOutput.size() > 4) {
			deleteText();
			wordsToolong = true;
		}

		// delete the " " that is added to the start of the first line
		stringToOutput.set(0, stringToOutput.get(0).substring(1,stringToOutput.get(0).length()));

		for (int i = 0; i < stringToOutput.size() && i < 4; i++) {
			g.drawString(stringToOutput.get(i), floorTopLeftX + 6, floorTopLeftY + 15 + (10*i));
		}
		

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