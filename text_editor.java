import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.event.*;

class CustomFontChooser extends JDialog {
    private Font selectedFont;
    private JComboBox<String> fontComboBox;
    private JComboBox<Integer> sizeComboBox;
    private JCheckBox boldCheckBox;
    private JCheckBox italicCheckBox;
    private boolean canceled = false;

    public CustomFontChooser(JFrame parent, Font initialFont) {
        super(parent, "Font Chooser", true);
        selectedFont = initialFont;

        fontComboBox = new JComboBox<>(getAvailableFontFamilyNames());
        sizeComboBox = new JComboBox<>(new Integer[]{8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40, 48, 56, 72});
        boldCheckBox = new JCheckBox("Bold");
        italicCheckBox = new JCheckBox("Italic");



        fontComboBox.setSelectedItem(initialFont.getFamily());
        sizeComboBox.setSelectedItem(initialFont.getSize());
        boldCheckBox.setSelected(initialFont.isBold());
        italicCheckBox.setSelected(initialFont.isItalic());

        JButton okButton = new JButton("OK");
        okButton.setMnemonic(KeyEvent.VK_ENTER);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_ESCAPE);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canceled = false;
                updateSelectedFont();
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canceled = true;
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Font:"), gbc);

        gbc.gridx = 1;
        panel.add(fontComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Size:"), gbc);

        gbc.gridx = 1;
        panel.add(sizeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Style:"), gbc);

        gbc.gridx = 1;
        panel.add(boldCheckBox, gbc);

        gbc.gridx = 2;
        panel.add(italicCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(okButton, gbc);

        gbc.gridx = 2;
        panel.add(cancelButton, gbc);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    public boolean wasCanceled() {
        return canceled;
    }

    private void updateSelectedFont() {
        int style = Font.PLAIN;
        if (boldCheckBox.isSelected()) {
            style |= Font.BOLD;
        }
        if (italicCheckBox.isSelected()) {
            style |= Font.ITALIC;
        }

        selectedFont = new Font(fontComboBox.getSelectedItem().toString(), style, (int) sizeComboBox.getSelectedItem());
    }

    public Font showDialog() {
        canceled = false;
        setVisible(true);
        if (canceled) {
            return null;
        } else {
            return getSelectedFont();
        }
    }

    private String[] getAvailableFontFamilyNames() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return ge.getAvailableFontFamilyNames();
    }
}

public class text_editor {
    private JFrame frame;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private JScrollPane scrollPane;
    private JSpinner fontsizespinner;
    private Font currentFont;
    private JComboBox fontBox;
    private boolean isTextSaved = true;
    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    public text_editor() {
        super();
        frame = new JFrame("Text Editor");
        textArea = new JTextArea(200, 600);
        fileChooser = new JFileChooser();
        scrollPane= new JScrollPane(textArea);
        fontsizespinner= new JSpinner(new SpinnerNumberModel(12,8,72,1));
        currentFont= textArea.getFont();

        String[] fonts= GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox<>();
        fontBox.addActionListener(this::actionPerformed);
        fontBox.setSelectedItem("Arial");


        cutAction = new AbstractAction("Cut") {
            public void actionPerformed(ActionEvent e) {
                textArea.cut();
            }
        };
        copyAction = new AbstractAction("Copy") {
            public void actionPerformed(ActionEvent e) {
                textArea.copy();
            }
        };
        pasteAction = new AbstractAction("Paste") {
            public void actionPerformed(ActionEvent e) {
                textArea.paste();
            }
        };

        Action cutAction = new DefaultEditorKit.CutAction();
        Action copyAction = new DefaultEditorKit.CopyAction();
        Action pasteAction = new DefaultEditorKit.PasteAction();


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu fontMenu= new JMenu("Font");
        JMenu editMenu = new JMenu("Edit");

        JMenuItem cutItem = new JMenuItem(cutAction);
        JMenuItem copyItem = new JMenuItem(copyAction);
        JMenuItem pasteItem = new JMenuItem(pasteAction);

        JMenuItem openFile = new JMenuItem("Open");
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));;

        JMenuItem saveFile = new JMenuItem("Save");
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem fontItem = new JMenuItem("Font Style");
        fontItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem colorItem= new JMenuItem("Text Color");
        colorItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));


        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(exit);

        editMenu.add(colorItem);
        editMenu.add(fontItem);
        editMenu.addSeparator();
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        fontMenu.add(fontBox);

        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.setJMenuBar(menuBar);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial",Font.PLAIN,12));

        cutItem.setAction(cutAction);
        copyItem.setAction(copyAction);
        pasteItem.setAction(pasteAction);

        textArea.getActionMap().put(DefaultEditorKit.cutAction, cutAction);
        textArea.getActionMap().put(DefaultEditorKit.copyAction, copyAction);
        textArea.getActionMap().put(DefaultEditorKit.pasteAction, pasteAction);


        InputMap inputMap = textArea.getInputMap(JComponent.WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.cutAction);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.copyAction);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), DefaultEditorKit.pasteAction);


        openFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        textArea.read(reader, null);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
         });


        saveFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        textArea.write(writer);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {System.exit(0);}
        });

        fontItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomFontChooser fontChooser = new CustomFontChooser(frame, textArea.getFont());
                Font selectedFont = fontChooser.showDialog();
                if (selectedFont != null) {
                    textArea.setFont(selectedFont);
                }
            }
        });

        colorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = showColorDialog(frame, textArea.getForeground());
                if (selectedColor != null) {
                    textArea.setForeground(selectedColor);
                }
            }
        });

        fontsizespinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int newSize = (int) fontsizespinner.getValue();
                Font currentFont = textArea.getFont();
                textArea.setFont(new Font(currentFont.getFamily(), currentFont.getStyle(), newSize));
            }
        });



            JPanel panel=new JPanel();
        panel.add(new JLabel("Font size:"));
        panel.add(fontsizespinner);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(textArea));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.add(fontBox);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

    }
    private void handleWindowClosing() {
        // Check if text is not saved
        if (!isTextSaved) {
            int result = JOptionPane.showConfirmDialog(frame, "Do you want to save the changes before closing?",
                    "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                saveText();
                if (isTextSaved) {
                    frame.dispose();
                }
            } else if (result == JOptionPane.NO_OPTION) {
                frame.dispose();
            }
        } else {
            frame.dispose();
        }
    }

    private void saveText() {
        int returnVal = fileChooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText()); // Write the text from the text area
                isTextSaved = true; // Set the flag to indicate the text is saved
            } catch (IOException ex) {
                ex.printStackTrace();
                isTextSaved = false; // Set the flag to indicate saving failed
            }
        } else {
            isTextSaved = false; // Set the flag to indicate saving was canceled
        }
    }


    public void actionPerformed(ActionEvent e) {
       if(e.getSource()==fontBox){
           textArea.setFont(new Font((String) fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
       }
    }
    private Font showFontChooserDialog(JFrame parent, Font initialFont) {
        CustomFontChooser fontChooser = new CustomFontChooser(parent, initialFont);
        return fontChooser.showDialog();
    }
    private Color showColorDialog(JFrame parent, Color initialColor) {
        Color selectedColor = JColorChooser.showDialog(parent, "Choose Text Color", initialColor);
        return selectedColor;
    }

        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new text_editor());
    }
}