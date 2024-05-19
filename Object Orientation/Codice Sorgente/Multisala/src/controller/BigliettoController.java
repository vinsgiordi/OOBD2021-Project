package controller;

import daoimpl.BigliettoDaoImpl;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JCheckBox;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class BigliettoController {

private BigliettoDaoImpl bigliettoDao = new BigliettoDaoImpl();
    
    /**
     * Metodo per visualizzare i posti selezionati
     * @param arrayCheckBox
     * @param id_proiezione identificativo della proiezione
     * @throws SQLException
     */
    public void checkPostiSelezionati(Vector<JCheckBox> arrayCheckBox, String id_proiezione) throws SQLException {
        for (int i = 0; i < arrayCheckBox.size(); i++) {
            if (arrayCheckBox.get(i).isSelected()) {
                String idPosto = arrayCheckBox.get(i).getActionCommand();
                bigliettoDao.insertBiglietto(idPosto, id_proiezione);
            }
        }
    }
}
