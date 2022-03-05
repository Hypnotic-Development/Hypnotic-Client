/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import dev.hypnotic.utils.Utils;

public class Installer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel logo, installerText, dirText;
	private JPanel mainPanel;
	private JButton installButton, changeDirButton;
	private JTextField dirTextBox;
	private JFileChooser mcDirPicker;
	private JCheckBox installFabricCheck;
	private static String installDir = System.getenv("APPDATA") + "\\.minecraft";

	public Installer() {
		try {
			this.setName("Installer");
			this.setTitle("Hypnotic Installer");
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("assets/hypnotic/icon.png")));
			this.setSize(450, 250);
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
			mainPanel.add(getChangeDirButton());
			mainPanel.add(getDirTextBox());
			mainPanel.add(getInstallFabricCheck());
		}
		return mainPanel;
	}
	
	private JLabel getPictureLabel() {
        if (logo == null) {
            try {
                BufferedImage myPicture = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/hypnotic/icon.png"));
                Image scaled = myPicture.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                logo = new JLabel(new ImageIcon(scaled));
                logo.setName("Logo");
                logo.setBounds(10, 10, 100, 100);
                logo.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
                logo.setVisible(true);
            } catch (Throwable ivjExc) {
            }
        }
        return logo;
    }
	
	private JLabel getInstallerText() {
		if (installerText == null) {
			installerText = new JLabel("Hypnotic Client Installer");
			installerText.setBounds(124, -6, 500, 50);
			installerText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
			installerText.setVisible(true);
		}
		return installerText;
	}
	
	public JLabel getDirText() {
		if (dirText == null) {
			dirText = new JLabel(".minecraft location");
			dirText.setBounds(120, 10, 500, 50);
			dirText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		}
		return dirText;
	}
	
	private JButton getInstallButton() {
		if (installButton == null) {
			installButton = new JButton();
			installButton.setVisible(true);
			installButton.setText("Install");
			installButton.setBounds(120, 130, 100, 25);
			installButton.addActionListener(l -> {
				installDir = getDirTextBox().getText();
				install(installDir, getInstallFabricCheck().isEnabled());
			});
		}
		return installButton;
	}
	
	public JButton getChangeDirButton() {
		if (changeDirButton == null) {
			changeDirButton = new JButton("Browse");
			changeDirButton.setBounds(355, 80, 80, 25);
			changeDirButton.addActionListener(l -> {
				getMcDirPicker();
			});
		}
		return changeDirButton;
	}
	
	public JTextField getDirTextBox() {
		if (dirTextBox == null) {
			dirTextBox = new JTextField(installDir);
			dirTextBox.setBounds(120, 80, 225, 25);
		}
		return dirTextBox;
	}
	
	public JFileChooser getMcDirPicker() {
		if (mcDirPicker == null) {
			mcDirPicker = new JFileChooser(installDir, FileSystemView.getFileSystemView());
			mcDirPicker.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			mcDirPicker.setDialogTitle("Change");
			mcDirPicker.setAcceptAllFileFilterUsed(false);
			if (mcDirPicker.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				installDir = mcDirPicker.getSelectedFile().getPath();
				getDirTextBox().setText(installDir);
			}
		}
		return mcDirPicker;
	}
	
	public JToggleButton getInstallFabricCheck() {
		if (installFabricCheck == null) {
			installFabricCheck = new JCheckBox("Install Fabric Loader");
			installFabricCheck.setBounds(120, 50, 200, 25);
		}
		return installFabricCheck;
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
	
	private void install(String dir, boolean installFabric) {
		
	}
}
