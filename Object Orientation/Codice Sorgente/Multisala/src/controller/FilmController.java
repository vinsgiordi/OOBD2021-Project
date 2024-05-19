package controller;

import daoimpl.FilmDaoImpl;
import entity.Option;
import java.awt.Image;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import rowMapper.RowMapperDao;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class FilmController {

    private FilmDaoImpl filmdao = new FilmDaoImpl();
    private RowMapperDao rmd = new RowMapperDao();

    /**
     * Metodo per l'inserimento del film
     * @param titolo titolo del film
     * @param durata durata del film
     * @param dataInizio data di inizio del film
     * @param dataFine data di fine del film
     * @throws SQLException
     */
    public void insertFilm(String titolo, String durata, String dataInizio, String dataFine) throws SQLException {
        filmdao.insertFilm(titolo, durata, dataInizio, dataFine);
    }

    /** 
     * Metodo per cancellare il film
     * @param id_film identificativo del film
     * @throws SQLException
     */
    public void deleteFilm(String id_film) throws SQLException {
        filmdao.deleteFilm(id_film);
    }

    /**
     *
     * @return @throws SQLException
     */
    public Vector<Option> getListaFilm() throws SQLException {
        Vector<Option> returnList = new Vector<>();
        ArrayList list = filmdao.getListFilm();

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            HashMap map = (HashMap) iter.next();
            Option i = new Option((Integer) map.get("ID_Film"), map.get("Titolo").toString());
            returnList.add(i);
        }

        return returnList;
    }

    /**
     *
     * @return @throws SQLException
     */
    public JTable getTableFilm() throws SQLException {
        int columnImage = 6;
        URL url = ClassLoader.getSystemResource("image/stella.png");
        ImageIcon imageIcon = new ImageIcon(url);
        Image image = imageIcon.getImage();
        Image newImage = rmd.getScaledImage(image, 15, 15);
        ImageIcon newImageIcon = new ImageIcon(newImage);

        DefaultTableModel dtm = filmdao.getDtm("SELECT * FROM multisala.get_film()", newImageIcon, columnImage);

        JTable table = new JTable(dtm) {
            @Override
            public Class getColumnClass(int column) {
                return (column == columnImage) ? Icon.class : Object.class;
            }
        };

        return table;
    }

}
