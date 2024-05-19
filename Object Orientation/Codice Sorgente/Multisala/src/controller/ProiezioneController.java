package controller;

import daoimpl.ProiezioneDaoImpl;
import java.awt.Image;
import java.net.URL;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import rowMapper.RowMapperDao;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class ProiezioneController {

    private ProiezioneDaoImpl proiezioneDao = new ProiezioneDaoImpl();
    private RowMapperDao rmd = new RowMapperDao();

    /**
     * Metodo che serve ad aprire la lista delle proiezioni
     *
     * @param day
     * @return ci ritorna la tabella
     * @throws SQLException
     */
    public JTable apriListaProiezioni(String day) throws SQLException {
        int columnImage = 5;
        URL url = ClassLoader.getSystemResource("image/stella.png");
        ImageIcon imageIcon = new ImageIcon(url);
        Image image = imageIcon.getImage();
        Image newImage = rmd.getScaledImage(image, 15, 15);
        ImageIcon newImageIcon = new ImageIcon(newImage);

        DefaultTableModel dtm = proiezioneDao.getDtm("SELECT * FROM multisala.get_proiezioni('" + day + "')", newImageIcon, columnImage);

        JTable table = new JTable(dtm) {
            @Override
            public Class getColumnClass(int column) {
                return (column == columnImage) ? Icon.class : Object.class;
            }
        };

        table.setRowHeight(30);
        return table;
    }

    /**
     * Metodo per l'inserimento della proiezione
     *
     * @param ora ora della proiezione
     * @param film nome del film che verrà inserito
     * @param sala nome della sala dove verrà inserita la proiezione
     * @param prezzo prezzo del biglietto del film
     * @throws SQLException
     */
    public void insertProiezione(String dataOdierna, String ora, String film, String sala, String prezzo) throws SQLException {
        proiezioneDao.insertProiezione(dataOdierna, ora, film, sala, prezzo);
    }
}
