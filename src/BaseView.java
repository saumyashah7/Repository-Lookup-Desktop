import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BaseView {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		    JFrame frame = new JFrame("List of Assignments");
		    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		    
		    // Making list for repositories
		    String labels[] = {"Assignment-1", "Assignment-2", "Assignment-3", "Assignment-4", "Assignment-5"};
		    JList jl = new JList(labels);
		    jl.setBounds(10, 10, 50, 100);
		    jl.setFont(new Font("Arial",Font.BOLD,32));
		    jl.setBorder(new EmptyBorder(20, 20, 50, 50));
		    jl.setFixedCellHeight(50);
		    jl.setFixedCellWidth(100);
		    frame.add(jl);
		    
		    // Button to fetch files
		    JButton btn = new JButton("Get Files");
		    btn.setLayout(null);
		    btn.setFont(new Font("Arial",Font.BOLD,40));
		    btn.setPreferredSize(new Dimension(140,140));
		    frame.add(btn, BorderLayout.SOUTH);
		    btn.addActionListener(new ActionListener() {

		        @Override
		        public void actionPerformed(ActionEvent e) {
		        	String repo = (String) jl.getSelectedValue();
		        	if(repo==null) return;
		        	
		        	// get path for selected repository from database
		        	String path = getPath(repo);
		        	if(path==null) return;
		        	
		        	// Display files for the selected directory
		        	JFrame frame = new JFrame();
		        	frame = getFrame(path);
		        	frame.setVisible(true);
		        }
		    });
		    frame.setVisible(true);
	}
	public static String getPath(String repo) {
		String path = null;
		try
		{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","","");  
			Statement stmt=con.createStatement();
			String query = "select path from repolocations where directory = \""+repo+"\""; 
			ResultSet rs=stmt.executeQuery(query);  
			while(rs.next())  
			path = rs.getString(1);  
			con.close();  
		}
		catch(Exception e){ System.out.println(e);}  
			  
		
		return path;
	}
	public static JFrame getFrame(String path) {
			JFrame f = new JFrame("Compare Files");
			f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		    JPanel upperPanel = new JPanel();
		    JPanel lowerPanel = new JPanel();
		    f.getContentPane().add(upperPanel, "North");
		    f.getContentPane().add(lowerPanel, "South");	
		    
		    // Get a working file
		    JTextArea tareaFixed = new JTextArea(25, 50);
		    tareaFixed.setFont(new Font("Arial",Font.BOLD,40));
		    tareaFixed.setEditable(false);
		    JScrollPane topleft = new JScrollPane(tareaFixed);
		    topleft.getVerticalScrollBar().setPreferredSize(new Dimension(30, 0));
		    topleft.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 30));

		    // Get a Buggy file
		    JTextArea tareaBuggy = new JTextArea(25, 50);
		    tareaBuggy.setFont(new Font("Arial",Font.BOLD,40));
		    tareaBuggy.setEditable(false);
		    JScrollPane topright = new JScrollPane(tareaBuggy);
		    topright.getVerticalScrollBar().setPreferredSize(new Dimension(30, 0));
		    topright.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 30));
		    
		    upperPanel.add(topleft);
		    upperPanel.add(topright);
		    
		    // Get a fixed file
		    JTextArea tareaReadMe = new JTextArea(17,100);
		    tareaReadMe.setFont(new Font("Arial",Font.BOLD,40));
		    tareaReadMe.setEditable(false);
		    JScrollPane bottom = new JScrollPane(tareaReadMe);
		    bottom.getVerticalScrollBar().setPreferredSize(new Dimension(30, 0));
		    bottom.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 30));
		    lowerPanel.add(bottom);
	
		    File file = new File(path + "\\Fixed_version.txt");
		    File file1 = new File(path + "\\Buggy_version.txt");
		    File file2 = new File(path + "\\readme.txt");
		    try
		    {
		    	BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		        tareaFixed.read(input, "READING FIXED FILE :-)");
		    } 
		    catch (Exception e) 
		    {
		     e.printStackTrace();
		    }
		    try
		    {
			     BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
			     tareaBuggy.read(input, "READING BUGGY FILE :-)");
		    }
		    catch (Exception e)
		    {
			     e.printStackTrace();
			}
		    try 
		    {
			     BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));
			     tareaReadMe.read(input, "READING FILE :-)");
			} 
		    catch (Exception e)
		    {
			     e.printStackTrace();
			}
		    f.pack();
		    return f;
		
	}

	/**
	 * Create the application.
	 */
	public BaseView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1450, 1300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
