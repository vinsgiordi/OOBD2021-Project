package dao;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public interface ProiezioneDao {

    public void insertProiezione(String dataOdierna, String ora, String film, String sala, String prezzo);

    public DefaultTableModel getDtm(String query, ImageIcon image, Integer columnImage);
}
