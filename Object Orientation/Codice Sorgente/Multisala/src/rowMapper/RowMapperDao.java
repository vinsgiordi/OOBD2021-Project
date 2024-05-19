package rowMapper;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class RowMapperDao {

    /**
     *
     * @param rs
     * @param azioni
     * @param image
     * @param columnImage
     * @return
     * @throws SQLException
     */
    public DefaultTableModel buildRowMapper(ResultSet rs, boolean azioni, ImageIcon image, Integer columnImage) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column).replace("_", " ").toUpperCase());
        }

        if (azioni) {
            columnNames.add("AZIONI");
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                if (columnImage != null && columnCount == columnImage+1 && rs.getObject(columnIndex).toString().equals("1")) {
                    vector.add(image);
                } else {
                    vector.add(rs.getObject(columnIndex));
                }
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    /**
     * Metodo che genera il pannello per la tabella
     * @param table identificativo tabella
     * @param btn identificativo bottone
     * @param panel identificativo del panel
     */
    public void generatePanelForTable(JTable table, JButton btn, JPanel panel) {
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBounds(0, 0, 861, 390);

        panel.removeAll();
        panel.add(scrollPane, BorderLayout.CENTER);
        if (btn != null) {
            panel.add(btn, BorderLayout.NORTH);
        }
        panel.revalidate();
        panel.repaint();
    }

    

    /**
     * Metodo per la formattazione del testo
     * @param gridx
     * @param gridy
     * @return
     */
    public GridBagConstraints createGbc(int gridx, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(3, 3, 3, 3);

        return gbc;
    }

    /**
     * Metodo per la formattazione dell'immagine
     * @param srcImg
     * @param w
     * @param h
     * @return
     */
    public Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}
