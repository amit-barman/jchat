/*** Created By : Amit Barman ***/
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Image;
import java.awt.Cursor;

public class Client extends JFrame implements ActionListener, Runnable{
	// calling all usefull class and create objects
	private JPanel panel = new JPanel();
	private JTextField textfild = new JTextField();
	private static JTextArea textarea = new JTextArea();
	private JButton button = new JButton("Send");
	private Timer timer;
	private Socket socket;
	private DataInputStream incoming;
	private DataOutputStream outgoing;
	private BufferedReader br;
	private BufferedWriter bw;

	// Fonts
	private Font monospaced = new Font("Monospaced", Font.BOLD, 18);
	private Font helvetica_bold = new Font("Helvetica", Font.BOLD, 16);
	private Font dialog = new Font("DialogInput", Font.PLAIN, 14);
	private Font helvetica = new Font("Helvetica", Font.BOLD, 12);

	// variables
	boolean typing;

	Client() {
		// panel
		panel.setLayout(null);
		panel.setBackground(new Color(191, 0, 255));
		panel.setBounds(0, 0, 450, 70);
		add(panel);

		// Logout or Exit button icon
		ImageIcon ico1 = new ImageIcon("assets/logout.png");
		Image mod_ico1 = ico1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		ImageIcon finalico1 = new ImageIcon(mod_ico1);
		JLabel lable = new JLabel(finalico1);
		lable.setBounds(5, 15, 30, 30);
		lable.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.add(lable);

		// Exit button click to exit function
		lable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ae) {
        		System.exit(0);
    		}
		});

		// Green dot icon
		ImageIcon ico2 = new ImageIcon("assets/green_dot.png");
		Image mod_ico2 = ico2.getImage().getScaledInstance(10, 10, Image.SCALE_DEFAULT);
		ImageIcon finalico2 = new ImageIcon(mod_ico2);
		JLabel lable2 = new JLabel(finalico2);
		lable2.setBounds(90, 32, 30, 30);
		panel.add(lable2);

		// Profile picture
		ImageIcon ico3 = new ImageIcon("assets/Light_Yagami.png");
		Image mod_ico3 = ico3.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		ImageIcon finalico3 = new ImageIcon(mod_ico3);
		JLabel lable3 = new JLabel(finalico3);
		lable3.setBounds(45, 5, 60, 60);
		panel.add(lable3);

		// Profile name
		JLabel lable4 = new JLabel("Light");
		lable4.setForeground(new Color(255, 255, 255));
		lable4.setFont(helvetica_bold);
		lable4.setBounds(115, 15, 100, 18);
		panel.add(lable4);

		// Active status
		JLabel lable5 = new JLabel("Active Now");
		lable5.setForeground(new Color(255, 255, 255));
		lable5.setFont(helvetica);
		lable5.setBounds(115, 35, 100, 20);
		panel.add(lable5);

		// timer featcher
		timer = new Timer(1, new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(!typing){
					lable5.setText("Active Now");
				}
			}
		});
		// delay time betwen typing and stop typing
		timer.setInitialDelay(1000);

		// Three Dot
		ImageIcon ico6 = new ImageIcon("assets/more.png");
		Image mod_ico6 = ico6.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		ImageIcon finalico6 = new ImageIcon(mod_ico6);
		JLabel lable6 = new JLabel(finalico6);
		lable6.setBounds(390, 15, 35, 25);
		lable6.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.add(lable6);

		// Text Ariea
		textarea.setBackground(new Color(255, 255, 255));
		JScrollPane scroll = new JScrollPane(textarea);
		scroll.setBounds(5, 75, 425, 448);
		textarea.setFont(monospaced);
		textarea.setEditable(false);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(false);
		add(scroll);

		// Text feild
		textfild.setBounds(4, 527, 350, 30);
		textfild.setFont(dialog);
		add(textfild);

		// for listen key press from textfild add key listener
		textfild.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent ke){
				lable5.setText("typing....");
				timer.stop();
				typing = true;
			}
			// on key keyreleased start timer and change "typeing...." status to default status
			public void keyReleased(KeyEvent ke){
				typing = false;
				if(!timer.isRunning()){
					timer.start();
				}
			}
		});

		// Send Button
		button.setBounds(357, 527, 72, 30);
		button.setBackground(new Color(255,255,255));
		button.setFont(helvetica);
		button.addActionListener(this);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(button);

		// JFrame componests
		this.setLayout(null);
		this.setTitle("JChat");
		this.setSize(450, 600);
		getContentPane().setBackground(new Color(255, 255, 255));
		ImageIcon image = new ImageIcon("assets/chat.png");
		this.setIconImage(image.getImage());
		this.setLocation(200, 100);
		this.setVisible(true);
		try{
			socket = new Socket("127.0.0.1", 4444);
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(Exception e){ e.printStackTrace(); }

	}

	@Override
	public void actionPerformed(ActionEvent ae){
		String output = "Light : "+textfild.getText();
		try{
			bw.write(output);
			bw.write("\r\n");
			bw.flush();
		}catch(Exception e) {}
		textfild.setText("");
	}

	public void run(){
		try{
			String msg = "";
			while((msg = br.readLine()) != null){
				textarea.append(msg + "\n");
			}
		}catch(Exception e){}	
	}

	// Main method
	public static void main(String args[]){
		Client client = new Client();
		Thread thread = new Thread(client);
		thread.start();
	}
}