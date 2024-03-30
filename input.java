                                                                                                       //  R E G U M A T E  //
                                                                                                    
                                                                                                          
                                                                                                          
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import java.awt.Shape;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.text.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TimeZone;
import javax.swing.border.LineBorder;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Vector;


public class input extends JFrame {

    public JTextField meetingIdField;
//    public JTextField meetTimeField;
    public JFormattedTextField meetTimeField;
    public JTextField totalMeetTimeField;
    public JPasswordField passcodeField;
    Connection con;
    public JCheckBox showHideCheckBox;
   

// Variables for roundness of panel edges
    public int topLeft = 20;
    public int topRight = 20;
    public int bottomLeft = 20;
    public int bottomRight = 20;

    public input() {
        super("Meeting Registration");

        getContentPane().setLayout(null);

        // Create all Lables
        JLabel Regumate = new JLabel("ZOOM MEETING AUTOMATION");
        JLabel meetingIdLabel = new JLabel("Meeting ID:");
        JLabel meetTimeLabel = new JLabel("Meeting Time (24hr format):");
        JLabel totalMeetTimeLabel = new JLabel("Total Meeting Duration :");  
        JLabel passcodeLabel = new JLabel("Passcode:");
	
	// Create all TextFields
        meetingIdField = new JTextField(20);
        totalMeetTimeField = new JTextField(20);
        totalMeetTimeField.setText("In minutes");
	totalMeetTimeField.setForeground(Color.GRAY);
	totalMeetTimeField.setFont(new Font("Arial", Font.ITALIC, 12)); // Set initial font and size
	
	// Attach document listener to meetingIdField
        meetingIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFont(meetingIdField);
		updateFont(totalMeetTimeField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFont(meetingIdField);
		updateFont(totalMeetTimeField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFont(meetingIdField);
		updateFont(totalMeetTimeField);
            }

            private void updateFont(JTextField textField) {
                Font defaultFont = new Font("DEPOT", Font.BOLD, 12);
                Font currentFont = textField.getFont();
                String text = textField.getText();

                if (text.isEmpty()) {
                    textField.setFont(defaultFont);
                } else {
                    Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize());
                    textField.setFont(newFont);
                }
            }
        });     
        
        totalMeetTimeField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        if (totalMeetTimeField.getText().equals("In minutes")) {
            totalMeetTimeField.setText("");
            totalMeetTimeField.setForeground(Color.BLACK);
           Font currentFont = meetingIdField.getFont();
           totalMeetTimeField.setFont(currentFont);
           // totalMeetTimeField.setFont(new Font("Arial", Font.PLAIN, 12)); // Set your desired font and size
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (totalMeetTimeField.getText().isEmpty()) {
            totalMeetTimeField.setText("In minutes");
            totalMeetTimeField.setForeground(Color.GRAY);
            Font currentFont = meetingIdField.getFont();
             totalMeetTimeField.setFont(currentFont);
            totalMeetTimeField.setFont(new Font("Arial", Font.ITALIC, 12)); // Set your desired font and size
        }
    }
}); 
        
        passcodeField = new JPasswordField(20);
        
        passcodeField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        // Check if passcodeField text matches the default placeholder text
        if (String.valueOf(passcodeField.getPassword()).isEmpty()) {
            passcodeField.setEchoChar((char) 0); // Set echo char to display text
            passcodeField.setForeground(Color.BLACK);
            Font currentFont = meetingIdField.getFont(); // Get font from meetingIdField
            passcodeField.setFont(currentFont); // Set font for passcodeField
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        // Check if passcodeField is empty
        if (String.valueOf(passcodeField.getPassword()).isEmpty()) {
            passcodeField.setEchoChar((char) 0); // Set echo char to display text
            passcodeField.setForeground(Color.GRAY);
            Font currentFont = meetingIdField.getFont(); // Get font from meetingIdField
            passcodeField.setFont(currentFont); // Set font for passcodeField
        }
    }
});


 	
 	// Create masked text field for time input
        try {
            MaskFormatter time = new MaskFormatter("##:##");
            time.setPlaceholderCharacter('_');
            meetTimeField = new JFormattedTextField(time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

 	// Set Font size and family for all labels
        meetingIdLabel.setFont(new Font("Times new Roman", Font.BOLD, 16));
        meetTimeLabel.setFont(new Font("Times new Roman", Font.BOLD, 16));
        totalMeetTimeLabel.setFont(new Font("Times new Roman", Font.BOLD, 16));
        passcodeLabel.setFont(new Font("Times new Roman", Font.BOLD, 16));

        // Button of Submit
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Times new Roman", Font.BOLD, 17));
        submitButton.setForeground(Color.BLACK);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMeetingData();
            }
        });

        // Button of Reset
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Times new Roman", Font.BOLD, 17));
        resetButton.setForeground(Color.BLACK);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set all input fields to null
                meetingIdField.setText(null);
                meetTimeField.setText(null);
                totalMeetTimeField.setText(null);
                passcodeField.setText(null);
            }
        });
 
 	
          // Create P1 panel
        JPanel P1 = new JPanel();
       
        
       // Create PanelRound panel
        PanelRound P2 = new PanelRound() {  
                      @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 0, width - 1, height - 1, 40, 40);
                graphics.setColor(Color.CYAN);
                graphics.setStroke(new BasicStroke(2));
                RoundRectangle2D rect = new RoundRectangle2D.Double(1, 1, width - 3, height - 3, 40, 40);
                graphics.draw(rect);
                graphics.dispose();
            }
        };;
        
        Dimension panelSize = P2.getPreferredSize();
        int x = (P1.getWidth() - panelSize.width) / 2;
        int y = (P1.getHeight() - panelSize.height) / 2;
        P1.setLayout(null);
        P1.setPreferredSize(new Dimension(1920, 1360));
        P1.setBackground(new Color(0, 33, 43, 255));
	P1.setLayout(null);

 	// Creation & settings of P2 panel 
    
        
        P2.setLayout(null);
        P2.setBounds(680, 550, 600, 400);
        P2.setBackground(new Color(0, 90, 111, 255));

        P1.add(P2);

        Insets insets = new Insets(5, 5, 5, 5);
        int labelWidth = 280;
        int textFieldWidth = 210;

        Regumate.setBounds(550, 30, 900, 50);
        Regumate.setFont(new Font("Times New Roman", Font.BOLD, 50));
        P1.add(Regumate);
        Regumate.setForeground(Color.CYAN);

        JLabel lheading = new JLabel("Welcome To Regumate!");
        lheading.setFont(new Font("Times New Roman", Font.BOLD, 32));
        lheading.setBounds(110, 180, 500, 60);
        lheading.setForeground(Color.cyan);

	// Overview 
        JLabel lborrowtext = new JLabel(
                "<html> <h2>REGUMATE is a groundbreaking solution designed to simplify the process of joining Zoom meetings automatically at set schedules. It showcases our commitment to utilizing advanced technology to boost productivity effectively. By leveraging programming, REGUMATE offers a user-friendly solution that saves time. With REGUMATE, joining Zoom meetings becomes effortless and efficient, allowing users to concentrate on their priorities.<br><br>Zoom meeting automation simplifies repetitive tasks in virtual meetings. It automates scheduling, reminders, participant management, and follow-ups, making communication smoother. Tasks like scheduling recurring meetings, sending reminders, and generating reports are handled automatically. In essence, Zoom meeting automation enhances efficiency and organization for hosts and participants alike.<br><br>Simply provide the Zoom meeting credentials for seamless integration.</h2><html>");
        lborrowtext.setBounds(115, 90, 1700, 500);
        lborrowtext.setForeground(Color.lightGray);
        lborrowtext.setFont(new Font("Bookman old style", Font.CENTER_BASELINE, 14));

        P1.add(lheading);
        P1.add(lborrowtext);

          //adding hyperlink to the code
        JLabel hyperlinkLabel = new JLabel("<html><h3>Click here to open a new page</h3></html>");
         hyperlinkLabel.setBounds(850, 730, 1700, 500);
         hyperlinkLabel.setForeground(Color.WHITE);
          P1.add(hyperlinkLabel);
        hyperlinkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hyperlinkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                createAndShowGUI();
            }
        });

        meetingIdLabel.setBounds(60, 65, labelWidth, 25);
        meetingIdLabel.setForeground(Color.white);

        P2.add(meetingIdLabel);
        meetingIdField.setBounds(340, 65, textFieldWidth, 25);

	//Takes on next Line on pressing'Enter'  Add action listener to the meetingIdField
	meetingIdField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Move focus to meetTimeField when "Enter" is pressed
                meetTimeField.requestFocus();
            }
        });
        
	
        // Add KeyListener to meetingIdField (Restriction of numbers only in meetingId)
        meetingIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Checking if the character entered is a digit
                if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });

        P2.add(meetingIdField);

        meetTimeLabel.setBounds(60, 105, labelWidth, 28);
        meetTimeLabel.setForeground(Color.white);
        P2.add(meetTimeLabel);
        meetTimeField.setBounds(340, 105, textFieldWidth, 28);
        
	//Takes on next Line on pressing'Enter'  Add action listener to the meetTimeField
         meetTimeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Move focus to totalMeetTimeField when "Enter" is pressed
                totalMeetTimeField.requestFocus();
            }
        });
	        
        
        P2.add(meetTimeField);

        totalMeetTimeLabel.setBounds(60, 150, labelWidth, 28);
        totalMeetTimeLabel.setForeground(Color.white);
        P2.add(totalMeetTimeLabel);
        totalMeetTimeField.setBounds(340, 150, textFieldWidth, 28);
        
	//Takes on next Line on pressing'Enter'  Add action listener to the totamMeetTimeField
        totalMeetTimeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Move focus to pascodeField when "Enter" is pressed
                passcodeField.requestFocus();
            }
        });
                

        // Add KeyListener to totalMeetTimeField (Restriction of numbers only in totalMeetTime)
        totalMeetTimeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Checking if the character entered is a digit
                if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });

	meetTimeField.setToolTipText("Enter meeting time in 24-hour format (e.g., 13:30)");
        P2.add(totalMeetTimeField);

        passcodeLabel.setBounds(60, 195, labelWidth, 28);
        passcodeLabel.setForeground(Color.white);
        P2.add(passcodeLabel);
        passcodeField.setBounds(340, 195, textFieldWidth, 28);
        
        //Takes on next Line on pressing'Enter'  Add action listener to the passcodeField
        passcodeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                submitButton.doClick();
            }
        });
  	
passcodeField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        
        passcodeField.setEchoChar('\u2022');
        passcodeField.setForeground(Color.BLACK);
        Font currentFont = meetingIdField.getFont();
        passcodeField.setFont(currentFont);
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (String.valueOf(passcodeField.getPassword()).isEmpty()) {

            passcodeField.setEchoChar((char) 0);
            passcodeField.setForeground(Color.GRAY);
            Font currentFont = meetingIdField.getFont();
            passcodeField.setFont(currentFont);
        }
    }
});


  	
 // Checkbox
        showHideCheckBox = new JCheckBox("");
        showHideCheckBox.setForeground(Color.WHITE);
        showHideCheckBox.setOpaque(false);
        showHideCheckBox.setFocusPainted(false);
        showHideCheckBox.setSelected(false); 
        showHideCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showHideCheckBox.isSelected()) {
                    passcodeField.setEchoChar((char) 0); 
                } else {
                    passcodeField.setEchoChar('\u2022'); 
                }
            }
        });
        //showHideCheckBox.setBounds(520, 185, 120, 28);
        showHideCheckBox.setBounds(555,198,18,20);
        P2.add(showHideCheckBox);



       /* submitButton.setBounds(250, 300, 140, 30); // Center submit button horizontally
        P2.add(submitButton);
        

        submitButton.setBounds(400, 300, 140, 30); // Center submit button horizontally
       
        P2.add(submitButton);
        */
        P2.add(passcodeField);

        submitButton.setBounds(400, 300, 140, 30);
        P2.add(submitButton);

        resetButton.setBounds(60, 300, 140, 30);
        P2.add(resetButton);
        
        // Text field border
        LineBorder lineBorders = new LineBorder(Color.CYAN, 1); 
        meetingIdField.setBorder(lineBorders);
        meetTimeField.setBorder(lineBorders);
        totalMeetTimeField.setBorder(lineBorders);
        passcodeField.setBorder(lineBorders);
	
	  // Add panel to frame
        getContentPane().add(P1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
        P1.setBounds(0, 0, 1920, 1080);
    }



    // DATABASE CONNECTION , LOADING OF DATA IN DATABASE AND RESTRICTIONS
    public void saveMeetingData() {
        String meetingId = meetingIdField.getText();
        String meetTime = meetTimeField.getText();
        String totalMeetTime = totalMeetTimeField.getText();
        String passcode = new String(passcodeField.getPassword());

        // Check if any of the fields are empty
        if (meetingId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Meeting ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (meetTime.isEmpty() || meetTime.equals("__:__")) {
            JOptionPane.showMessageDialog(this, "Meeting time is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (totalMeetTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Total Meeting Time is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (passcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Passcode is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert totalMeetTime to an integer using type casting
        int totalMeetTimeInt = 0;
        try {
            totalMeetTimeInt = Integer.parseInt(totalMeetTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid total meeting time. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database connection
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/regumate", "reguuser", "regupass");
            System.out.println("DATABASE CONNECTED SUCCESSFULLY...");

            String sql = "INSERT INTO meetings (meeting_id, meeting_time, total_meeting, passcode) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setString(1, meetingId);
                preparedStatement.setString(2, meetTime);
                preparedStatement.setInt(3, totalMeetTimeInt);
                preparedStatement.setString(4, passcode);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Meeting data saved successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save meeting data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error or SQL error.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //creation of new JFrame where the table resides

       private JTable table;
        

        public void createAndShowGUI() {
        JFrame frame = new JFrame("Meeting Scheduler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel panel = new JPanel();
       
        JPanel panel2 = createScheduledMeetingsPanel();
        frame.add(panel);
        frame.setVisible(true);
         panel.add(panel2);
         panel.setPreferredSize(new Dimension(1920, 1360));
         panel.setBackground(new Color(0, 33, 43, 255));
         panel.setLayout(null);
         panel.setBounds(0, 0, 1920, 1080);
    }

    

     public JPanel createScheduledMeetingsPanel() {
        
        PanelRound panel2 = new PanelRound() {  
                      @Override
            public void paintComponent(Graphics g) {
                
                super.paintComponent(g);
                
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10);
                graphics.setColor(Color.CYAN);
                graphics.setStroke(new BasicStroke(2));
                RoundRectangle2D rect = new RoundRectangle2D.Double(1, 1, width - 3, height - 3, 10, 10);
                graphics.draw(rect);
                graphics.dispose();
            }
        };;
        panel2.setLayout(null); // Absolute layout
        panel2.setBackground(new Color(0, 90, 111, 255));
        panel2.setBounds(680, 250, 600, 600);

        // Create a table to display the scheduled meetings
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Meeting ID");
        columnNames.add("Meeting Time");
        columnNames.add("Total Meeting Duration");
        columnNames.add("Passcode");

        Vector<Vector<Object>> data = new Vector<>();
        refreshData(data);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel); // Initialize table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 580, 300); // Set bounds for the scroll pane
        panel2.add(scrollPane);

        // Create a delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.removeRow(selectedRow);
                    String meetingIdToDelete = (String) model.getValueAt(selectedRow, 0); // Assuming meeting ID is in the first column
                    // Perform deletion from the database using meetingIdToDelete
                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/regumate", "reguuser", "regupass");
                        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM meetings WHERE meeting_id = ?");
                        deleteStatement.setString(1, meetingIdToDelete);
                        int rowsDeleted = deleteStatement.executeUpdate();
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(null, "Meeting deleted successfully.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete meeting.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Database error.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteButton.setBounds(10, 320, 100, 30); // Set bounds for the delete button
        panel2.add(deleteButton);

        // Create a refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData(data);
                tableModel.fireTableDataChanged(); // Notify the table model that the data has changed
            }
        });
        refreshButton.setBounds(120, 320, 100, 30); // Set bounds for the refresh button
        panel2.add(refreshButton);

        return panel2;
    }
    private void refreshData(Vector<Vector<Object>> data) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/regumate", "reguuser", "regupass");
            PreparedStatement pst = con.prepareStatement("select * from meetings");
            ResultSet rs = pst.executeQuery();
            data.clear(); // Clear existing data
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("meeting_id"));
                row.add(rs.getString("meeting_time"));
                row.add(rs.getInt("total_meeting"));
                row.add(rs.getString("passcode"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }













    // MAIN FUNCTION
    public static void main(String[] args) {
      



                                                                                          


               
        
     
        
        SwingUtilities.invokeLater(() -> new input());
    
       
    }
    
    
static class PanelRound extends JPanel{


public int getRoundTopRight(){
return roundTopRight;
}

public void setRoundTopRight(int roundTopRight){
this.roundTopRight = roundTopRight;
repaint();
}

public int getRoundTopLeft(){
return roundTopLeft;
}

public void setRoundTopLeft(int roundTopRight){
this.roundTopLeft = roundTopLeft;
repaint();
}

public int getRoundBottomRight(){
return roundBottomRight;
}

public void setRoundBottomRight(int roundTopRight){
this.roundBottomRight = roundBottomRight;
repaint();
}

public int getRoundBottomLeft(){
return roundBottomLeft;
}

public void setRoundBottomLeft(int roundTopRight){
this.roundBottomLeft = roundBottomLeft;
repaint();
}







private int roundTopLeft = 200;
private int roundTopRight = 200;
private int roundBottomLeft = 200;
private int roundBottomRight = 200;




public PanelRound(){

	setOpaque(false);

}
@Override
    protected void paintComponent(Graphics grphcs){
	Graphics2D g2=(Graphics2D)grphcs.create();
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setColor(getBackground());
	Area area = new Area(createRoundTopLeft());
	if(roundTopRight>0){
		area.intersect(new Area(createRoundTopRight()));
	
	}
	if(roundBottomLeft>0){
	  area.intersect(new Area(createRoundBottomLeft()));
	}
	if(roundBottomRight>0){
	area.intersect(new Area(createRoundBottomRight()));
	}
	g2.fill(area);
        g2.dispose();
	super.paintComponent(grphcs);		


}

private  Shape createRoundTopLeft(){
int width = getWidth();
int height = getHeight();
int roundX = Math.min(width, roundTopLeft);
int roundY = Math.min(height, roundTopLeft);
Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
area.add(new Area(new Rectangle2D.Double(roundX/2, 0, width - roundX/2, height)));
area.add(new Area(new Rectangle2D.Double(0, roundY/2, width, height-roundY/2)));
return area;
}


private  Shape createRoundTopRight(){
int width = getWidth();
int height = getHeight();
int roundX = Math.min(width, roundTopRight);
int roundY = Math.min(height, roundTopRight);
Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
area.add(new Area(new Rectangle2D.Double(0, 0, width - roundX/2, height)));
area.add(new Area(new Rectangle2D.Double(0, roundY/2, width, height-roundY/2)));
return area;
}

private  Shape createRoundBottomLeft(){
int width = getWidth();
int height = getHeight();
int roundX = Math.min(width, roundBottomLeft);
int roundY = Math.min(height, roundBottomLeft);
Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
area.add(new Area(new Rectangle2D.Double(roundX/2, 0, width - roundX/2, height)));
area.add(new Area(new Rectangle2D.Double(0, 0, width, height-roundY/2)));
return area;
}

private  Shape createRoundBottomRight(){
int width = getWidth();
int height = getHeight();
int roundX = Math.min(width, roundBottomRight);
int roundY = Math.min(height, roundBottomRight);
Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
area.add(new Area(new Rectangle2D.Double(0, 0, width - roundX/2, height)));
area.add(new Area(new Rectangle2D.Double(0, 0, width, height-roundY/2)));
return area;
}


}

}

                                                                                                           //  R E G U M A T E  //
