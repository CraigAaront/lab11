import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Class representing a Calculator with Graphical elements.
 *
 * @author Stephen
 * @version 2019-04-17
 */
@SuppressWarnings("serial")
public class GraphicalCalculatorFrame extends JFrame
{
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 700;

	/**
	 * Interactive panel that the user clicks on to modify portions of an equation. The panel effectively works as
	 * a simple equation editor. The panel has 5 regions that may be clicked - 3 operand regions and 2 operator
	 * regions. That is, the Panel displays an expression with 5 editable regions. Regions are marked by a black
	 * bounding box and when the region is clicked it is "selected" and highlighted yellow.
	 *
	 * The equation represented by this panel is of the form:
	 * 	operand0 operator0 operand1 operator1 operand2 = result
	 */
	@SuppressWarnings("unused")
	private final class GraphicalCalculatorPanel extends JPanel implements MouseListener
	{
		/**
		 * Width and height for the panel. Width matches the enclosing frame.
		 */
		private static final int PANEL_WIDTH = FRAME_WIDTH;
		private static final int PANEL_HEIGHT = 300;

		/**
		 * Size of the regions.
		 */
		private static final int REGION_WIDTH = 50;
		private static final int REGION_HEIGHT = 50;

		/**
		 * Define top-left corner of first region and x increment between corners.
		 */
		private static final int REGION_START_X = 50;
		private static final int REGION_START_Y = 50;
		private static final int REGION_INC_X = 60;


		/**
		 * Color to highlight the selected region:
		 */
		Color highlight = new Color(255, 255, 0, 127);

		/**
		 * The editable regions. These are stored as rectangles to represent where to draw them as well as to
		 * represent the area on the panel that selects the region when clicked.
		 *
		 * regions[0] refers to the left-most region displayed
		 */
		Rectangle[] regions;

		/**
		 * The points at which the parts of the equation are drawn (operators, operands, = sign, result). The regions
		 * are drawn around the displayed text for the editable parts of the equation.
		 */
		Point[] textPoints;

		/**
		 * The region selected that should be highlighted yellow.
		 */
		private int selectedRegion = 0;

		/**
		 * The operands of the equation. Used for evaluation and for drawing the expression.
		 */
		private int[] operands = {0, 0, 0};

		/**
		 * The operators of the equation. Used for evaluation and for drawing the expression.
		 */
		private String[] operators = {"+", "+"};

		/**
		 * Creates the panel and sets up the listeners and member variables.
		 */
		public GraphicalCalculatorPanel()
		{
			this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
			this.setupRegions();
			this.setupTextPoints();
			this.addMouseListener(this);
		}

		/**
		 * Set up the regions that may be clicked.
		 *
		 * The regions are associated with the operands and operators as such:
		 * region0 = operand0
		 * region1 = operator0
		 * region2 = operand1
		 * region3 = operator1
		 * region4 = operand2
		 */
		private void setupRegions()
		{
		    /**
		     * initializes regions to an array of length 5; regions are REGION_INC X away
		     * from each other in the x direction and in the same y plane
		     * region0 = first region, left most rectangle
		     * region1 = second region, 2nd from the left
		     * region2 = third region, 3rd from the left
		     * region3 = fourth region, 4th from the left
		     * region4 = fifth region, 5th from the left
		     */
			regions = new Rectangle[5];
			regions[0] = new Rectangle(REGION_START_X,REGION_START_Y,REGION_HEIGHT,REGION_WIDTH);
            regions[1] = new Rectangle(REGION_START_X + REGION_INC_X,REGION_START_Y,REGION_HEIGHT,REGION_WIDTH);
            regions[2] = new Rectangle(REGION_START_X + 2*REGION_INC_X,REGION_START_Y,REGION_HEIGHT,REGION_WIDTH);
            regions[3] = new Rectangle(REGION_START_X + 3*REGION_INC_X,REGION_START_Y,REGION_HEIGHT,REGION_WIDTH);
            regions[4] = new Rectangle(REGION_START_X + 4*REGION_INC_X,REGION_START_Y,REGION_HEIGHT,REGION_WIDTH);

			
		    // create the regions. The first regions should be located at REGION_START_X, REGION_START_Y
			// The regions should be spaced horizontally be REGION_INC_X
			// The regions should be the same size
			// There should be 5 regions
		}

		/**
		 * Sets up the points at which text is drawn (i.e. the parts of the equation)...
		 */
		private void setupTextPoints()
		{
			textPoints = new Point[7];

			int startX = REGION_START_X + 20;
			int startY = REGION_START_Y + 30;

			for (int pt = 0; pt < textPoints.length; pt++)
			{
				textPoints[pt] = new Point(startX + pt*REGION_INC_X, startY);
			}
		}

		/**
		 * Draws the bounding boxes for all the regions in the panel.
		 *
		 * Draws the text representing the expression.
		 *
		 * Highlights the selected region.
		 *
		 * @param g The graphics object for drawing.
		 */
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			//sets the rectangle color to black
            g.setColor(Color.BLACK);
            //for loop draws the rectangles without fill, with a black color
			for(Rectangle rect : regions) {
			    g.drawRect(rect.x, rect.y, rect.width, rect.height);
			}
			// Draw bounding boxes on all regions (regions are stored as rectangles):

			// Draw the text at the specified text points:
			// Pattern is: operand operator operand operator operand = result
			for (int pt = 0; pt < textPoints.length; pt++)
			{
				String drawString = "";
				switch(pt) {
				case 0:
					drawString = "" + operands[0];
					break;
				case 1:
					drawString = operators[0];
					break;
				case 2:
					drawString = "" + operands[1];
					break;
				case 3:
					drawString = operators[1];
					break;
				case 4:
					drawString = "" + operands[2];
					break;
				case 5:
					drawString = "=";
					break;
				case 6:
					drawString = "" + this.evaluate();
					break;
				}
				g.drawString(drawString, textPoints[pt].x, textPoints[pt].y);
			}

			// Draws translucent rectangle over selected region (using the highlight color)
			Rectangle select = regions[selectedRegion];
			g.setColor(highlight);
			g.fillRect(select.x, select.y, select.width, select.height);
			
		}

		/**
		 * When the panel is clicked, check if the point clicked lies within any of the editable regions
		 * of the expression. If so, that region set to the be the new selected region. Repaint at the
		 * end.
		 *
		 * (hint: look at the java.awt.Rectangle class for useful methods that you can use to check if
		 *  a point is within the rectangle)
		 */
		@Override
		public void mouseClicked(MouseEvent e)
		{
			// If the mouse clicked within a region, set that region to be the selected region.
		    // clicked is the point at which the mouse is clicked
		    Point clicked = new Point(e.getX(), e.getY());
		    
		    // for loop checks if clicked is in one of the 5 predefined regions and sets that region to selected region
		    for(int i = 0; i < regions.length; i++) {
		        if (regions[i].contains(clicked)) {
		            selectedRegion = i;
		        }
		    }
		   
			//check if a clicked point is within a region. If so, set that region to be selected.

			// Repaint the panel (this will implicitly call paintComponent):
			this.repaint();
		}

		/**
		 * Attempts to set the content of the selected region. If the region is and operand the content must be
		 * a single-digit number (0-9). If the region is an operator the content must be "+", "-", or "*".
		 *
		 * Should call repaint at the end.
		 *
		 * @param content The value that the program will attempt to set the selected region to.
		 * @return True if the region content is sucessfully set. False otherwise.
		 */
		public boolean setSelectedRegionContents(String content)
		{
			boolean success = true;
			int number = 0;
			/* attempt to set the value of the selected region.
			 *
			 * Remember that the regions are associated with the operand and operators as such:
			 * region0 = operand0
			 * region1 = operator0
			 * region2 = operand1
			 * region3 = operator1
			 * region4 = operand2
			 *
			 * Return false if the set operation cannot be done.
			 */
			
			// if the selected region is region 0, 2, or 4 then its one of the number/operand regions
			if(selectedRegion % 2 == 0) {
			    // checks if the input is an int, if not then it throws an error
			    try {
			        number = Integer.parseInt(content);
			        // checks if the number is 0-9, and if so then assigns that number to the correct operand location  
			        // based on the selected region
			        if (number >= 0 && number <= 9) {
		                switch(selectedRegion) {
		                    case 0:
		                        operands[0] = number;
		                        break;
		                    case 2:
		                        operands[1] = number;
		                        break;
		                    case 4:
		                        operands[2] = number;
		                         break;
		                }
		            }
			        // fails if the number isn't 1-9
			        else {
			            success = false;
			        }
			    }
			    // fails if the input isnt a number
		         catch (NumberFormatException ex) {
		             success = false;
		         }
			}
			// if the selected region is 1 or 3 which is an operator region
			else {
			    //checks if the input is one of the predefined operators of +, -, or *
			    if (content.equals("+") || content.equals("-") || content.equals("*")) {
			        // sets the appropriate operator region in the array to the right operator
			        switch(selectedRegion) {
			            case 1:
			                operators[0] = content;
			                break;
			            case 3:
			                operators[1] = content;
			                break;
			        }
			    }
			    // fails if the operator isn't one of the predefined
			    else {
			        success = false;
			    }
			}
			this.repaint();
			return success;
		}

		/**
		 * Evaluates the equation on the panel. Operations are performed left-to-right, ignoring operator precendence
		 * (e.g. and equation of 2+3*4 will return (2+3)*4 = 20)
		 *
		 * @return The evaluation of the expression displayed by the Panel.
		 */
		public int evaluate()
		{
			//evaluate the expression. (operand0 operator0 operand1) operator1 operand2
		    //assigns the operands (3 total) to letters a,b,c to make expressions easier
		    int total = 0;
		    int a,b,c;
		    a = operands[0];
		    b = operands[1];
		    c = operands[2];
		    // assigns the operators (2 total) to letters d,e to make expressions easier
		    String d,e;
		    d = operators[0];
		    e = operators[1];
		    // following if/else statements evaluate based on what the operators are, disregarding precedence
		    if (d.equals("+")) {
		        total = a + b;
		    }
		    else if (d.equals("-")) {
		        total = a - b;
		    }
		    else if (d.equals("*")){
		        total = a * b;
		    }
		    
            if (e.equals("+")) {
                total = total + c;
            }
            else if (e.equals("-")) {
                total = total - c;
            }
            else if (e.equals("*")){
                total = total * c;
            }
            return total;
		}

		/** DO NOT MODIFY - DOES NOTHING */
		@Override
		public void mousePressed(MouseEvent e) {}

		/** DO NOT MODIFY - DOES NOTHING */
		@Override
		public void mouseReleased(MouseEvent e) {}

		/** DO NOT MODIFY - DOES NOTHING */
		@Override
		public void mouseEntered(MouseEvent e) {}

		/** DO NOT MODIFY - DOES NOTHING */
		@Override
		public void mouseExited(MouseEvent e) {}
	}

	/** panel for displaying and interacting with the expression */
	GraphicalCalculatorPanel gcPanel = new GraphicalCalculatorPanel();

	//==================================================================================================================
	// Panels for component grouping and organization:
	//==================================================================================================================

	/** panel to hold the non-GraphicalCalculatorPanel panels */
    JPanel panel0 = new JPanel(new GridLayout(2, 2));
	/** panel for operand text entry */
    JPanel panel1 = new JPanel();
    /** panel for the radio buttons */
    JPanel panel2 = new JPanel(new GridLayout(3, 0));
    /** panel for the set operand/operator buttons  */
    JPanel panel3 = new JPanel(new GridLayout(3, 0));
    /** panel for the error message */
    JPanel panel4 = new JPanel();

    //==================================================================================================================
    // Operand Entry:
    //==================================================================================================================

    /** Text field for the user's number input */
    JTextField operandEntry = new JTextField("00000");

    /**
     * Button to attempt to set the selected region in the Graphical panel to the value in the operand
     * entry text field.
     */
    JButton setOperand = new JButton("Set Operand");

    //==================================================================================================================
    // Operator Entry:
    //==================================================================================================================

    /** Group of operation buttons */
    ButtonGroup ops = new ButtonGroup();

    /** add operation radio button */
    JRadioButton add = new JRadioButton("+");
    /** divide operation radio button */
    JRadioButton subtract = new JRadioButton("-");
    /** multiply operation radio button */
    JRadioButton multiply = new JRadioButton("*");

    /**
     * Button to attempt to set the selected region in the Graphical panel to the selected operator (as defined by
     * the radio buttons).
     */
    JButton setOperator = new JButton("Set Operator");

    //==================================================================================================================
    // Misc.
    //==================================================================================================================

    /** Text that display an error message */
    JLabel errorMessage = new JLabel();

    //==================================================================================================================
    // Main and constructor:
    //==================================================================================================================

    /**
     * This method builds and operates the GUI window.
     * @param title The title of the window.
     */
    public GraphicalCalculatorFrame() {
        super("GraphicalCalculatorFrame");

        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLayout(new GridLayout(2, 0));

        // : add components to panels
        //adds the text box to panel1
        panel1.add(operandEntry);
        //adds the add radiobutton to panel2
        panel2.add(add);
        //adds the subtract radiobutton to panel2
        panel2.add(subtract);
        //adds the multiply radiobutton to panel2
        panel2.add(multiply);
        //adds the setOperand button to panel3 
        panel3.add(setOperand);
        //adds the setOperator button to panel3
        panel3.add(setOperator);
        //adds the blank error panel to panel4
        panel4.add(errorMessage);

        // : add radio buttons to the button group
        // adds all radiobuttons to the group
        ops.add(add);
        ops.add(subtract);
        ops.add(multiply);
        
        //default to + operator
        add.setSelected(true); //remember, the button group ensures only one button is selected

        //add sub-panels into panel 0
        // adds panel1 first, then panel3, then panel2, then panel4 since its a grid bag layout, meaning
        // left to right, top to bottom 
        panel0.add(panel1);
        panel0.add(panel3);
        panel0.add(panel2);
        panel0.add(panel4);
        
        // Adds all panels to frame:
        panel0.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - 300));

        this.add(panel0);
        this.add(gcPanel);

        // Set ActionListeners on buttons:

        /*
         * Attempts to set the selected region in gcPanel to the operand value in the operandEntry textbox.
         * If the set operation fails, display the error message "Failed to set operand value".
         * If the set operation succeeds, clear any error messages.
         */
        setOperand.addActionListener((e) -> {
        		//attempt to modify the selected region in gcPanel with the new operand value.
            
                // if the setSelectedRegionContents returns true for the gcpanel, then the values are set 
                // and the error message is reset
                if (gcPanel.setSelectedRegionContents(operandEntry.getText()) == true){
                    errorMessage.setText("");
                    panel4.revalidate();
                    
                }
                // if the setSelectedRegionContents return false, then an error message is returned in 
                // panel4
                else {
                    errorMessage.setText("Failed to set operand value");
                    panel4.revalidate();
                   }                   
        	}
        );

        /*
         * Attempts to set the selected region in gcPanel to the selected operator (which radio button is pressed).
         * Pass the string:
         * 	"+" if the add button is selected
         *  "-" if the subtract button is selected
         *  "*" if the multiply button is selected
         *
         * If the set operation fails, display the error message "Failed to set operator value".
         * If the set operation succeeds, clear any error messages.
         */
        setOperator.addActionListener((e) -> {
    		//  attempt to modify the selected region in gcPanel with the new operator value.
            
            // if the add button is selected, and setSelectedRegionContents is true, then the
            // error in panel4 is reset and the operator is changed
            if(add.isSelected() == true) {
                if (gcPanel.setSelectedRegionContents("+") == true) {
                    errorMessage.setText("");
                    panel4.revalidate();
                }
                // if setSelectedRegionContents is false, then panel4 displays an error message
                else {
                    errorMessage.setText("Failed to set operator value");
                    panel4.revalidate(); 
                }

            }
            // if the subtract button is selected, and setSelectedRegionContents is true, then the
            // error in panel4 is reset and the operator is changed
            else if(subtract.isSelected() == true) {
                if (gcPanel.setSelectedRegionContents("-") == true) {
                    errorMessage.setText("");
                    panel4.revalidate();
                }
                // if setSelectedRegionContents is false, then panel4 displays an error message
                else {
                    errorMessage.setText("Failed to set operator value");
                    panel4.revalidate(); 
                }
            }
            // if the multiply button is selected, and setSelectedRegionContents is true, then the
            // error in panel4 is reset and the operator is changed
            else if(multiply.isSelected() == true) {
                if (gcPanel.setSelectedRegionContents("*") == true) {
                    errorMessage.setText("");
                    panel4.revalidate();
                }
                // if setSelectedRegionContents is false, then panel4 displays an error message
                else {
                    errorMessage.setText("Failed to set operator value");
                    panel4.revalidate(); 
                }
            }
            // if the button selected is somehow not one of the 3, then an error is thrown
            else {
                errorMessage.setText("Failed to set operator value");
                panel4.revalidate();                
            }
        }
        );

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

	/**
	 * Main method to the program. Creates a new GraphicalCalculatorFrame object,
	 * calling its constructor.
	 *
	 * @param args The program arguments.
	 */
	public static void main(String[] args)
	{
		new GraphicalCalculatorFrame();
	}
}
