package dao;

import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public interface FilmDao {

    public void insertFilm(String titolo, String durata, String dataInizio, String dataFine);

    public void deleteFilm(String id_film);

    public ArrayList getListFilm();

    public DefaultTableModel getDtm(String query, ImageIcon image, Integer columnImage);
}
