import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.io.*;

public class TextEditor {
  private JFrame frame = new JFrame();
  private JTextArea textArea = new JTextArea(20, 30);
  private static final String title = "Text Editor";

  public void startGui () {
    try {
      GraphicsEnvironment ge =
        GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("inconsolata.ttf")));
    } catch (IOException|FontFormatException e) {
      //Handle exception
    }

    textArea.setFont(new Font("inconsolata", Font.PLAIN, 16));
    textArea.setLineWrap(true);

    textArea.addKeyListener(new KeyAdapter () {
      public void keyReleased(KeyEvent e) {
        if (textArea.getText().equals("")) {
          frame.setTitle(title);
          return;
        }
        if (textArea.getText().indexOf('\n') == -1 || textArea.getCaretPosition() <= textArea.getText().indexOf('\n')) {
          if (textArea.getText().indexOf('\n') == -1) {
            frame.setTitle(title + " - "+textArea.getText());
          } else {
            frame.setTitle(title + " - "+textArea.getText().substring(0, textArea.getText().indexOf('\n')));
          }
        }
      }
    });

    frame.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("control S"), "save");
    frame.getRootPane().getActionMap().put("save", new AbstractAction () {
      public void actionPerformed (ActionEvent e) {
        saveText();
      }
    });

    frame.addWindowListener(new WindowAdapter () {
      public void windowClosing(WindowEvent e) {
        saveText();
        frame.dispose();
        System.exit(0);
      }
    });

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.setTitle(title);

    frame.add(textArea, BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }

  private void saveText () {
    if (textArea.getText().equals("")) {
      return;
    }
    String desktop = System.getProperty("user.home") + "/Desktop/";
    String content = textArea.getText();
    if (content.equals("")) {
      return;
    }

    int till;
    if (content.contains("\n")) {
      till = content.indexOf('\n');
    } else {
      till = content.length();
    }

    File fileLocation = new File(desktop+content.substring(0, till)+".txt");
    int n = 2;
    while (fileLocation.exists()) {
      fileLocation = new File(desktop+content.substring(0, till)+"("+n+").txt");
      n++;
    }

    try {
      Files.write( Paths.get(fileLocation.getAbsolutePath()), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main (String[] args) {
    TextEditor textEditor = new TextEditor();
    textEditor.startGui();
  }
}
