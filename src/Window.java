import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

class Window extends JFrame {
    private JTextArea sourceText;
    private JTextArea outputText;
    private JCheckBox keyByDefault;
    private JTextArea keyValue;
    private JButton encryptButton;
    private JButton decryptButton;

    Window(String name) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyPanel panel = new MyPanel();
        panel.setLayout(null);
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");
        JButton saveButton = new JButton("Save data to file");
        JButton loadButton = new JButton("Load data from file");
        JButton clearButton = new JButton("Clear fields");
        JLabel sourceLabel = new JLabel("Input text for encryption here:");
        JLabel outLabel = new JLabel("Your encrypted data:");
        JLabel keyLabel = new JLabel("Input key here:");
        sourceText = new JTextArea();
        outputText = new JTextArea();
        keyValue = new JTextArea();
        JRadioButton encrypt = new JRadioButton("Encrypt");
        JRadioButton decrypt = new JRadioButton("Decrypt");
        keyByDefault = new JCheckBox("Key by default");
        ButtonGroup group = new ButtonGroup();
        JScrollPane sourceTextScrollPane = new JScrollPane(sourceText);
        JScrollPane outputTextScrollPane = new JScrollPane(outputText);
        JScrollPane keyScrollPane = new JScrollPane(keyValue);
        JLabel image = new JLabel(new ImageIcon(System.getProperty("user.dir") + "\\src\\key.png"));
        image.setSize(175, 200);
        image.setLocation(50, 150);
        encrypt.setSize(150, 50);
        encrypt.setLocation(150, 50);
        decrypt.setSize(150, 50);
        decrypt.setLocation(150, 100);
        keyByDefault.setSize(150, 50);
        keyByDefault.setLocation(50, 75);
        encryptButton.setSize(150, 50);
        encryptButton.setLocation(110, 390);
        encryptButton.setBackground(new Color(100, 200, 225));
        decryptButton.setSize(150, 50);
        decryptButton.setLocation(110, 390);
        decryptButton.setBackground(new Color(100, 200, 225));
        loadButton.setSize(150, 50);
        loadButton.setLocation(370, 390);
        loadButton.setBackground(new Color(225, 200, 100));
        saveButton.setSize(150, 50);
        saveButton.setLocation(630, 390);
        clearButton.setSize(150, 50);
        clearButton.setBackground(new Color(225, 100, 100));
        clearButton.setLocation(890, 390);
        saveButton.setBackground(new Color(100, 225, 100));
        sourceTextScrollPane.setSize(400, 300);
        sourceTextScrollPane.setLocation(250, 50);
        outputTextScrollPane.setSize(400, 300);
        outputTextScrollPane.setLocation(700, 50);
        keyScrollPane.setSize(175, 200);
        keyScrollPane.setLocation(50, 150);
        sourceLabel.setSize(200, 25);
        sourceLabel.setLocation(250, 25);
        outLabel.setSize(200, 25);
        outLabel.setLocation(700, 25);
        keyLabel.setSize(175, 25);
        keyLabel.setLocation(50, 125);
        encryptButton.setContentAreaFilled(false);
        decryptButton.setContentAreaFilled(false);
        saveButton.setContentAreaFilled(false);
        loadButton.setContentAreaFilled(false);
        clearButton.setContentAreaFilled(false);
        encryptButton.setOpaque(true);
        decryptButton.setOpaque(true);
        saveButton.setOpaque(true);
        loadButton.setOpaque(true);
        clearButton.setOpaque(true);
        keyValue.setEditable(true);
        encrypt.setOpaque(false);
        decrypt.setOpaque(false);
        keyByDefault.setOpaque(false);
        outputText.setLineWrap(true);
        sourceText.setLineWrap(true);
        keyValue.setLineWrap(true);
        keyByDefault.setSelected(true);
        encryptButton.setVisible(true);
        decryptButton.setVisible(false);
        encrypt.setSelected(true);
        encryptButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        decryptButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sourceText.setFont(new Font("System Regular", Font.PLAIN, 16));
        outputText.setFont(new Font("System Regular", Font.PLAIN, 16));
        keyValue.setFont(new Font("System Regular", Font.PLAIN, 16));
        panel.add(sourceLabel);
        panel.add(outLabel);
        panel.add(keyLabel);
        panel.add(clearButton);
        panel.add(sourceTextScrollPane);
        panel.add(outputTextScrollPane);
        panel.add(keyScrollPane);
        panel.add(image);
        image.setVisible(true);
        group.add(encrypt);
        group.add(decrypt);
        panel.add(encrypt);
        panel.add(decrypt);
        panel.add(keyByDefault);
        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(saveButton);
        panel.add(loadButton);
        outputText.setWrapStyleWord(true);
        sourceText.setWrapStyleWord(true);
        keyValue.setWrapStyleWord(true);
        outputText.setEditable(true);
        keyScrollPane.setVisible(false);
        keyLabel.setVisible(false);
        setContentPane(panel);
        setSize(1150, 500);
        encryptButton.addActionListener(e -> translate(false));
        decryptButton.addActionListener(e -> translate(true));
        keyByDefault.addActionListener(e -> {
            if (keyScrollPane.isVisible()) {
                keyValue.setText("");
            }
            keyLabel.setVisible(!keyLabel.isVisible());
            keyScrollPane.setVisible(!keyScrollPane.isVisible());
        });
        encrypt.addActionListener(e -> {
            encryptButton.setVisible(true);
            decryptButton.setVisible(false);
            sourceLabel.setText("Input text for encryption here:");
            outLabel.setText("Your encrypted data:");
            if (!sourceText.getText().equals("") && !outputText.getText().equals("")) {
                String temp = sourceText.getText();
                sourceText.setText(outputText.getText());
                outputText.setText(temp);
            }
        });
        decrypt.addActionListener(e -> {
            encryptButton.setVisible(false);
            decryptButton.setVisible(true);
            sourceLabel.setText("Input text for decryption here:");
            outLabel.setText("Your decrypted data:");
            if (!sourceText.getText().equals("") && !outputText.getText().equals("")) {
                String temp = sourceText.getText();
                sourceText.setText(outputText.getText());
                outputText.setText(temp);
            }
        });
        loadButton.addActionListener(e -> {
            sourceText.setText("");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileHidingEnabled(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(Window.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getAbsolutePath();
                Path path = Paths.get(fileName);
                try {
                    List<String> lines = Files.readAllLines(path);
                    for (String line : lines) {
                        sourceText.setText(sourceText.getText() + line + "\n");
                    }
                    sourceText.setText(sourceText.getText().substring(0, sourceText.getText().length() - 1));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                outputText.setText("");
            }
        });
        saveButton.addActionListener(e -> {
            if (outputText.getText().equals("")) {
                JOptionPane.showMessageDialog(Window.this, "Output is empty!\n" +
                                "Try once again...",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileHidingEnabled(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showSaveDialog(Window.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getAbsolutePath();
                if (fileName.indexOf('.') == -1) {
                    fileName += ".txt";
                }
                file = new File(fileName);
                if (file.exists()) {
                    int response = JOptionPane.showConfirmDialog(null,
                            "Do you want to replace the existing file?",
                            "Confirm", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (response != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    int n = outputText.getText().length() - outputText.getText().replace("\n", "").length();
                    String line = outputText.getText();
                    for (int i = 0; i < n + 1; i++) {
                        int index = line.indexOf("\n");
                        if (index != -1) {
                            writer.write(line.substring(0, index));
                            writer.newLine();
                            line = line.substring(index + 1);
                        } else {
                            writer.write(line);
                        }
                    }
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(Window.this, "Successfully wrote to file!");
        });
        clearButton.addActionListener(e -> {
            sourceText.setText("");
            outputText.setText("");
            keyValue.setText("");
        });
    }

    public class MyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            Color color1 = new Color(16, 255, 255);
            Color color2 = new Color(0, 64, 0);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    private void translate(boolean flag) {
        StringBuilder text = new StringBuilder(sourceText.getText()), key;
        if (keyByDefault.isSelected()) {
            int remainder = sourceText.getText().length() % 4, len;
            if (remainder != 0) {
                len = sourceText.getText().length() / 4 * 4 + 4;
            } else {
                len = sourceText.getText().length();
            }
            StringBuilder generatedString = new StringBuilder();
            for (int i = 0; i < len; i++) {
                generatedString.append((char) new Random().nextInt());
            }
            key = new StringBuilder(generatedString.toString());
        } else {
            key = new StringBuilder(keyValue.getText());
        }
        if (text.length() < 4 || key.length() < 4) {
            JOptionPane.showMessageDialog(Window.this, "Length of the key or text is less than 4 symbols!\n" +
                            "Try once again...",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (text.length() % 4 != 0) {
            int reply = JOptionPane.showConfirmDialog(Window.this,
                    "\tLength of the text is not multiple by 4.\n" +
                            "\tDES Algorithm works with 64 bit blocks, each symbol in UTF-8 is 16 bits.\n" +
                            "\tYou want to add spaces to the end or want to input once again?",
                    "Attention please",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                int remainder = text.length() % 4;
                for (int i = 0; i < 4 - remainder; i++) {
                    text.append(" ");
                }
            }
        }
        if (key.length() < text.length()) {
            int reply = JOptionPane.showConfirmDialog(Window.this,
                    "\tLength of the key is less than the length of your text.\n" +
                            "\tYou want to repeat it cyclically or want to input once again?",
                    "Attention please",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                int length = key.length();
                for (int i = 0; i < text.length() - length; i++) {
                    key.append(key.charAt(i));
                }
            } else {
                return;
            }
        } else if (key.length() > text.length()) {
            int reply = JOptionPane.showConfirmDialog(Window.this,
                    "\tLength of the key is more than the length of your text.\n" +
                            "\tYou want to cut it or want to input once again?",
                    "Attention please",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                key = new StringBuilder(key.substring(0, text.length()));
            } else {
                return;
            }
        }
        outputText.setText(ManagerMatrix.DESAlgorithm(text.toString(), key.toString(), flag));
    }
}