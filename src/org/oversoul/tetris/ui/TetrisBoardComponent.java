/**
 * TetrisBoardComponent.java
 * 
 * Created on 11-Feb-04
 */
package org.oversoul.tetris.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import org.oversoul.tetris.TetrisBoard;

/**
 * The graphical component that paints the square board. This is
 * implemented as an inner class in order to better abstract the 
 * detailed information that must be sent between the square board
 * and its graphical representation.
 * 
 * @author Chris Callendar (9902588)
 */
public class TetrisBoardComponent extends Component {

	private static final long serialVersionUID = 1;

	/**
	 * The component size. If the component has been resized, that 
	 * will be detected when the paint method executes. If this 
	 * value is set to null, the component dimensions are unknown.
	 */
	private Dimension size = null;

	/**
	 * The component insets. The inset values are used to create a 
	 * border around the board to compensate for a skewed aspect 
	 * ratio. If the component has been resized, the insets values 
	 * will be recalculated when the paint method executes.
	 */
	private Insets insets = null;

	/**
	 * The square size in pixels. This value is updated when the 
	 * component size is changed, i.e. when the <code>size</code> 
	 * variable is modified.
	 */
	private Dimension squareSize = null;

	/**
	 * An image used for double buffering. The board is first
	 * painted onto this image, and that image is then painted 
	 * onto the real surface in order to avoid making the drawing
	 * process visible to the user. This image is recreated each
	 * time the component size changes.
	 */
	private Image bufferImage = null;

	/**
	 * A clip boundary buffer rectangle. This rectangle is used 
	 * when calculating the clip boundaries, in order to avoid 
	 * allocating a new clip rectangle for each board square.
	 */
	private Rectangle bufferRect = null;

	/**
	 * The board message color.
	 */
	private Color messageColor = Color.white;

	/**
	 * A lookup table containing lighter versions of the colors.
	 * This table is used to avoid calculating the lighter 
	 * versions of the colors for each and every square drawn.
	 */
	private Hashtable<Color, Color> lighterColors = null;

	/**
	 * A lookup table containing darker versions of the colors.
	 * This table is used to avoid calculating the darker
	 * versions of the colors for each and every square drawn.
	 */
	private Hashtable<Color, Color> darkerColors = null;

	/**
	 * A flag set when the component has been updated.
	 */
	private boolean updated = true;

	/**
	 * A bounding box of the squares to update. The coordinates 
	 * used in the rectangle refers to the square matrix.
	 */
	private Rectangle updateRect = null;

	/** The board width (in squares).     */
	private int width = 0;

	/** The board height (in squares).     */
	private int height = 0;

	/** The parent TetrisBoard object. */
	private TetrisBoard parent = null;

	private final BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
	private Cursor blankCursor = null;
	private Cursor defaultCursor = null;

	/**
	 * Creates a new square board component.
	 */
	public TetrisBoardComponent(TetrisBoard parent, int width, int height) {
		this.parent = parent;
		this.width = width;
		this.height = height;
		this.insets = new Insets(0, 0, 0, 0);
		this.squareSize = new Dimension(0, 0);
		this.bufferRect = new Rectangle(0, 0, 0, 0);
		this.updateRect = new Rectangle(0, 0, 0, 0);
		this.lighterColors = new Hashtable<Color, Color>();
		this.darkerColors = new Hashtable<Color, Color>();
		setBackground(parent.getColors().bg);
		this.messageColor = parent.getColors().text;
		this.blankCursor = getToolkit().createCustomCursor(img, new Point(0, 0), "blankCursor");
		this.defaultCursor = getCursor();
		this.setFocusable(true);
		
	}

	/**
	 * Hides the cursor
	 */
	public void hideCursor() {
		setCursor(blankCursor);
	}
	
	/**
	 * Shows the cursor.
	 */
	public void showCursor() {
		setCursor(defaultCursor);
	}
	
	@Override
	public boolean isFocusable() {
		return true;
	}

	/**
	 * Adds a square to the set of squares in need of redrawing.
	 *
	 * @param x     the horizontal position (0 <= x < width)
	 * @param y     the vertical position (0 <= y < height)
	 */
	public void invalidateSquare(int x, int y) {
		if (updated) {
			updated = false;
			updateRect.x = x;
			updateRect.y = y;
			updateRect.width = 0;
			updateRect.height = 0;
		} else {
			if (x < updateRect.x) {
				updateRect.width += updateRect.x - x;
				updateRect.x = x;
			} else if (x > updateRect.x + updateRect.width) {
				updateRect.width = x - updateRect.x;
			}
			if (y < updateRect.y) {
				updateRect.height += updateRect.y - y;
				updateRect.y = y;
			} else if (y > updateRect.y + updateRect.height) {
				updateRect.height = y - updateRect.y;
			}
		}
	}

	/**
	 * Redraws all the invalidated squares. If no squares have 
	 * been marked as in need of redrawing, no redrawing will 
	 * occur.
	 */
	public void redraw() {
		Graphics g;

		if (!updated) {
			updated = true;
			g = getGraphics();
			g.setClip(
				insets.left + updateRect.x * squareSize.width,
				insets.top + updateRect.y * squareSize.height,
				(updateRect.width + 1) * squareSize.width,
				(updateRect.height + 1) * squareSize.height);
			paint(g);
		}
	}

	/**
	 * Redraws the whole component.
	 */
	public void redrawAll() {
		Graphics g;

		updated = true;
		g = getGraphics();
		g.setClip(insets.left, insets.top, width * squareSize.width, height * squareSize.height);
		paint(g);
	}

	/**
	 * Returns true as this component is double buffered.
	 * 
	 * @return true as this component is double buffered
	 */
	@Override
	public boolean isDoubleBuffered() {
		return true;
	}

	/**
	 * Returns the preferred size of this component.
	 * 
	 * @return the preferred component size
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width * 20, height * 20);
	}

	/**
	 * Returns the minimum size of this component.
	 * 
	 * @return the minimum component size
	 */
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * Returns the maximum size of this component.
	 * 
	 * @return the maximum component size
	 */
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * Returns a lighter version of the specified color. The 
	 * lighter color will looked up in a hashtable, making this
	 * method fast. If the color is not found, the ligher color 
	 * will be calculated and added to the lookup table for later
	 * reference.
	 * 
	 * @param c     the base color
	 * 
	 * @return the lighter version of the color
	 */
	private Color getLighterColor(Color c) {
		Color lighter = lighterColors.get(c);
		if (lighter == null) {
			lighter = c.brighter().brighter();
			lighterColors.put(c, lighter);
		}
		return lighter;
	}

	/**
	 * Returns a darker version of the specified color. The 
	 * darker color will looked up in a hashtable, making this
	 * method fast. If the color is not found, the darker color 
	 * will be calculated and added to the lookup table for later
	 * reference.
	 * 
	 * @param c     the base color
	 * 
	 * @return the darker version of the color
	 */
	private Color getDarkerColor(Color c) {
		Color darker = darkerColors.get(c);
		if (darker == null) {
			darker = c.darker().darker();
			darkerColors.put(c, darker);
		}
		return darker;
	}

	/**
	 * Paints this component indirectly. The painting is first 
	 * done to a buffer image, that is then painted directly to 
	 * the specified graphics context.
	 * 
	 * @param g     the graphics context to use
	 */
	@Override
	public synchronized void paint(Graphics g) {
		super.paint(g);

		Graphics bufferGraphics;
		Rectangle rect;

		// Handle component size change
		if (size == null || !size.equals(getSize())) {
			size = getSize();
			squareSize.width = size.width / width;
			squareSize.height = size.height / height;
			if (squareSize.width <= squareSize.height) {
				squareSize.height = squareSize.width;
			} else {
				squareSize.width = squareSize.height;
			}
			insets.left = (size.width - width * squareSize.width) / 2;
			insets.right = insets.left;
			insets.top = 0;
			insets.bottom = size.height - height * squareSize.height;
			bufferImage = createImage(width * squareSize.width, height * squareSize.height);
		}

		// Paint component in buffer image
		rect = g.getClipBounds();
		bufferGraphics = bufferImage.getGraphics();
		bufferGraphics.setClip(rect.x - insets.left, rect.y - insets.top, rect.width, rect.height);
		paintComponent(bufferGraphics);

		// Paint image buffer
		g.drawImage(bufferImage, insets.left, insets.top, getBackground(), null);
	}

	/**
	 * Paints this component directly. All the squares on the 
	 * board will be painted directly to the specified graphics
	 * context.
	 * 
	 * @param g     the graphics context to use
	 */
	private void paintComponent(Graphics g) {
		
		// Paint background
		g.setColor(getBackground());
		g.fillRect(0, 0, width * squareSize.width, height * squareSize.height);

		// Paint squares
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = parent.getSquareColor(x, y);
				if (color != null) {
					paintSquare(g, color, x, y);
				}
			}
		}

		// Paint message
		if (parent.getMessage() != null) {
			paintMessage(g, parent.getMessage());
		}
	}

	/**
	 * Paints a single board square. The specified position must 
	 * contain a color object.
	 *
	 * @param g     the graphics context to use
	 * @param x     the horizontal position (0 <= x < width)
	 * @param y     the vertical position (0 <= y < height)
	 */
	private void paintSquare(Graphics g, Color color, int x, int y) {
		int xMin = x * squareSize.width;
		int yMin = y * squareSize.height;
		int xMax = xMin + squareSize.width - 1;
		int yMax = yMin + squareSize.height - 1;
		int i;

		// Skip drawing if not visible
		bufferRect.x = xMin;
		bufferRect.y = yMin;
		bufferRect.width = squareSize.width;
		bufferRect.height = squareSize.height;
		if (!bufferRect.intersects(g.getClipBounds())) {
			return;
		}

		// Fill with base color
		g.setColor(color);
		g.fillRect(xMin, yMin, squareSize.width, squareSize.height);

		// Draw brighter lines
		g.setColor(getLighterColor(color));
		for (i = 0; i < squareSize.width / 10; i++) {
			g.drawLine(xMin + i, yMin + i, xMax - i, yMin + i);
			g.drawLine(xMin + i, yMin + i, xMin + i, yMax - i);
		}

		// Draw darker lines
		g.setColor(getDarkerColor(color));
		for (i = 0; i < squareSize.width / 10; i++) {
			g.drawLine(xMax - i, yMin + i, xMax - i, yMax - i);
			g.drawLine(xMin + i, yMax - i, xMax - i, yMax - i);
		}
	}

	/**
	 * Paints a board message. The message will be drawn at the
	 * center of the component.
	 *
	 * @param g     the graphics context to use
	 * @param msg   the string message
	 */
	private void paintMessage(Graphics g, String msg) {
		int fontWidth;
		int offset;
		int x;
		int y;

		// Find string font width
		g.setFont(new Font("SansSerif", Font.BOLD, squareSize.width + 4));
		fontWidth = g.getFontMetrics().stringWidth(msg);

		// Find centered position
		x = (width * squareSize.width - fontWidth) / 2;
		y = height * squareSize.height / 2;

		// Draw black version of the string
		offset = squareSize.width / 10;
		g.setColor(Color.black);
		g.drawString(msg, x - offset, y - offset);
		g.drawString(msg, x - offset, y);
		g.drawString(msg, x - offset, y - offset);
		g.drawString(msg, x, y - offset);
		g.drawString(msg, x, y + offset);
		g.drawString(msg, x + offset, y - offset);
		g.drawString(msg, x + offset, y);
		g.drawString(msg, x + offset, y + offset);

		// Draw white version of the string
		g.setColor(messageColor);
		g.drawString(msg, x, y);
	}
	
	/**
	 * Sets the background color.  Default is black.
	 * @param color	The background color.
	 */
	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		repaint();
	}
	
	/**
	 * Returns the messageColor.
	 * @return Color
	 */
	public Color getMessageColor() {
		return messageColor;
	}

	/**
	 * Sets the messageColor.
	 * @param messageColor The messageColor to set
	 */
	public void setMessageColor(Color messageColor) {
		this.messageColor = messageColor;
		repaint();
	}

}