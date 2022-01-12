import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import dev.hypnotic.utils.Utils;

public class Installer extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private JLabel logo, installerText;
	private JPanel mainPanel;
	private JButton installButton;

	public Installer() {
		try {
			this.setName("Installer");
			this.setTitle("Hypnotic Installer");
			this.setSize(420, 250);
			this.setResizable(false);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		} catch (Exception e) {
			
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Installer installer = new Installer();

			Rectangle rectangle = installer.getBounds();
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        Rectangle screenRectangle = new Rectangle(0, 0, screenSize.width, screenSize.height);

	        int newX = screenRectangle.x + (screenRectangle.width - rectangle.width) / 2;
	        int newY = screenRectangle.y + (screenRectangle.height - rectangle.height) / 2;

	        if (newX < 0) newX = 0;
	        if (newY < 0) newY = 0;

	        installer.setBounds(newX, newY, rectangle.width, rectangle.height);
	        
	        installer.add(installer.getMainPanel());
	        
	        installer.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(null);
			mainPanel.setBorder(BorderFactory.createEmptyBorder(75, 350, 0, 0));
			mainPanel.add(getPictureLabel());
			mainPanel.add(getInstallButton());
			mainPanel.add(getInstallerText());
		}
		return mainPanel;
	}
	
	private JLabel getPictureLabel() {
        if (logo == null) {
            try {
                BufferedImage myPicture = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/hypnotic/icon.png"));
                Image scaled = myPicture.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                logo = new JLabel(new ImageIcon(scaled));
                logo.setName("Logo");
                logo.setBounds(10, 10, 80, 80);
                logo.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                logo.setPreferredSize(new Dimension(80, 80));
                logo.setVisible(true);
            } catch (Throwable ivjExc) {
            }
        }
        return logo;
    }
	
	private JLabel getInstallerText() {
		if (installerText == null) {
			installerText = new JLabel("Hypnotic Client Installer");
			installerText.setBounds(100, 10, 500, 50);
			installerText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
			installerText.setVisible(true);
		}
		return installerText;
	}
	
	private JButton getInstallButton() {
		if (installButton == null) {
			installButton = new JButton();
			installButton.setVisible(true);
			installButton.setText("Install");
			installButton.setBounds(100, 50, 100, 25);
			installButton.setPreferredSize(new Dimension(50, 10));
		}
		return installButton;
	}
	
	public File getFile(String userHome, String minecraftPath) {
        File workingDirectory = new File(userHome, minecraftPath + '/');;
            if (Utils.isUnix()) {
                workingDirectory = new File(userHome, '.' + minecraftPath + '/');
            }
            if (Utils.isWindows()) {
                String applicationData = System.getenv("APPDATA");
                if (applicationData != null) {
                    workingDirectory = new File(applicationData, "." + minecraftPath + '/');
                }
                workingDirectory = new File(userHome, '.' + minecraftPath + '/');
            }
            if (Utils.isMac()) {
                workingDirectory = new File(userHome, "Library/Application Support/" + minecraftPath);
            }
        return workingDirectory;
    }


	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
