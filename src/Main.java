import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = new Window("Data Encryption Standard (DES) encryption algorithm.");
        window.setVisible(true);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
    }
}