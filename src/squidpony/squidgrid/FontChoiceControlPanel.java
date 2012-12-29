package squidpony.squidgrid;

/*
 * FontChoiceControlPanel.java
 *
 * Created on Dec 5, 2009, 11:58:20 PM
 */
import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 *
 * @author Eben
 */
public class FontChoiceControlPanel extends javax.swing.JPanel {

    /**
     * Creates new form FontChoiceControlPanel
     */
    public FontChoiceControlPanel() {
        initComponents();
        initFontList();
    }

    /**
     * Allows the setting of the x and y values at construction time.
     *
     * @param rows
     * @param columns
     */
    public FontChoiceControlPanel(int rows, int columns) {
        initComponents();
        this.rows = rows;
        this.columns = columns;
        this.yField.setText(String.valueOf(rows));
        this.xField.setText(String.valueOf(columns));
        initFontList();
        validateFontSize();
    }

    private void initFontList() {
        //Get the local graphics environment
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        //Get the font names from the graphics environment
        fontList = ge.getAvailableFontFamilyNames();

        fontComboBox.removeAllItems();
        for (int i = 0; i < fontList.length; i++) {
            fontComboBox.addItem(fontList[i]);
        }
        fontComboBox.setMaximumRowCount(8);
        fontComboBox.setSelectedItem(fontComboBox.getItemAt(0));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        yField = new javax.swing.JTextField();
        updateButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        sizeField = new javax.swing.JTextField();
        fontComboBox = new javax.swing.JComboBox();
        boldCheckBox = new javax.swing.JCheckBox();
        italicsCheckBox = new javax.swing.JCheckBox();
        colorizeToggleButton = new javax.swing.JToggleButton();
        widthField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        heightField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        foregroundButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        backgroundButton = new javax.swing.JButton();
        cellSzieBox = new javax.swing.JCheckBox();
        whiteSpaceBox = new javax.swing.JCheckBox();

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        xField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        xField.setText("30");
        xField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("X");

        yField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        yField.setText("20");
        yField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yFieldActionPerformed(evt);
            }
        });

        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setText("Font Size:");

        sizeField.setText("24");
        sizeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sizeFieldActionPerformed(evt);
            }
        });

        fontComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        boldCheckBox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        boldCheckBox.setText("BOLD");

        italicsCheckBox.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        italicsCheckBox.setText("Italics");

        colorizeToggleButton.setText("Random Colors");

        widthField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        widthField.setText("24");
        widthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                widthFieldActionPerformed(evt);
            }
        });

        jLabel3.setText("X");

        heightField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        heightField.setText("24");
        heightField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightFieldActionPerformed(evt);
            }
        });

        jLabel4.setText("Grid Size:");

        jLabel5.setText("Cell Size:");

        jLabel6.setText("Background:");

        foregroundButton.setText("...");

        jLabel7.setText("Foreground");

        backgroundButton.setText("...");

        cellSzieBox.setText("Force Cell Size");

        whiteSpaceBox.setText("White Space");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(widthField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heightField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sizeField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boldCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(italicsCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(whiteSpaceBox))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cellSzieBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(foregroundButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backgroundButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorizeToggleButton)
                        .addGap(18, 18, 18)
                        .addComponent(updateButton))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(yField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(sizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fontComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boldCheckBox)
                    .addComponent(italicsCheckBox)
                    .addComponent(jLabel4)
                    .addComponent(whiteSpaceBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(widthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(heightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cellSzieBox)
                        .addComponent(jLabel6)
                        .addComponent(foregroundButton)
                        .addComponent(jLabel7)
                        .addComponent(backgroundButton)
                        .addComponent(colorizeToggleButton)
                        .addComponent(updateButton))))
            .addComponent(jSeparator1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xFieldActionPerformed
        validateRow();
    }//GEN-LAST:event_xFieldActionPerformed

    private void yFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yFieldActionPerformed
        validateColumn();
    }//GEN-LAST:event_yFieldActionPerformed

    private void validateRow() {
        try {
            int t = Integer.valueOf(yField.getText());
            rows = t;
        } catch (NumberFormatException n) {
            yField.setText("" + rows);
        }
    }

    private void validateColumn() {
        try {
            int t = Integer.valueOf(xField.getText());
            columns = t;
        } catch (NumberFormatException n) {
            xField.setText("" + columns);
        }
    }

    private void validateCellWidth() {
        try {
            int t = Integer.valueOf(widthField.getText());
            cellWidth = t;
        } catch (NumberFormatException n) {
            widthField.setText("" + cellWidth);
        }
    }

    private void validateCellHeight() {
        try {
            int t = Integer.valueOf(heightField.getText());
            cellHeight = t;
        } catch (NumberFormatException n) {
            xField.setText("" + cellHeight);
        }
    }

    private void validateFontSize() {
        try {
            int t = Integer.valueOf(sizeField.getText());
            fontSize = t;
        } catch (NumberFormatException n) {
            sizeField.setText("" + fontSize);
        }
    }

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        validateRow();
        validateColumn();
        validateCellWidth();
        validateCellHeight();
        validateFontSize();
    }//GEN-LAST:event_updateButtonActionPerformed

    private void sizeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sizeFieldActionPerformed
        validateFontSize();
    }//GEN-LAST:event_sizeFieldActionPerformed

    private void widthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_widthFieldActionPerformed
        validateCellWidth();
    }//GEN-LAST:event_widthFieldActionPerformed

    private void heightFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightFieldActionPerformed
        validateCellHeight();
    }//GEN-LAST:event_heightFieldActionPerformed

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
    
    public int getCellWidth(){
        return cellWidth;
    }
    
    public int getCellHeight(){
        return cellHeight;
    }

    public int getFontSize() {
        return fontSize;
    }

    public Font getFontFace() {
        return new Font((String) fontComboBox.getSelectedItem(), getFontStyle(), fontSize);
    }

    private int getFontStyle() {
        if (boldCheckBox.isSelected()) {
            if (italicsCheckBox.isSelected()) {
                return Font.BOLD + Font.ITALIC;
            }
            return Font.BOLD;
        } else if (italicsCheckBox.isSelected()) {
            return Font.ITALIC;
        } else {
            return Font.PLAIN;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backgroundButton;
    private javax.swing.JCheckBox boldCheckBox;
    public javax.swing.JCheckBox cellSzieBox;
    public javax.swing.JToggleButton colorizeToggleButton;
    private javax.swing.JComboBox fontComboBox;
    private javax.swing.JButton foregroundButton;
    private javax.swing.JTextField heightField;
    private javax.swing.JCheckBox italicsCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField sizeField;
    public javax.swing.JButton updateButton;
    public javax.swing.JCheckBox whiteSpaceBox;
    private javax.swing.JTextField widthField;
    private javax.swing.JTextField xField;
    private javax.swing.JTextField yField;
    // End of variables declaration//GEN-END:variables
    //custom variables
    private int rows = 80, columns = 50, cellWidth = 24, cellHeight = 24, fontSize = 34;
    private String fontList[];
}
