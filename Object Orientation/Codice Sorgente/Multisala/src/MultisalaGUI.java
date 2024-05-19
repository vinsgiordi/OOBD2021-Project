import com.formdev.flatlaf.FlatDarkLaf;
import controller.BigliettoController;
import controller.FilmController;
import controller.PostoController;
import controller.ProiezioneController;
import controller.SalaController;
import entity.Option;
import entity.Posto;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.TableCellRenderer;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rowMapper.RowMapperDao;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class MultisalaGUI {

    /**
     *
     */
    protected JFrame frame;
    private final FilmController filmController = new FilmController();
    private final ProiezioneController proiezioneController = new ProiezioneController();
    private final BigliettoController bigliettoController = new BigliettoController();
    private final PostoController postoController = new PostoController();
    private final SalaController salaController = new SalaController();
    RowMapperDao rowMapper = new RowMapperDao();
    JPanel panel = new JPanel();
    JButton button = new JButton();

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MultisalaGUI window = new MultisalaGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MultisalaGUI() {
        initialize();
    }

    /**
     *
     * Inizializzazione JFrame
     *
     * @exception UnsupportedLookAndFeelException, SQLException
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }

        frame = new JFrame("CINEMA MULTISALA - VINCENZO GIORDANO N86003039");
        frame.setBounds(100, 100, 875, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        URL url = ClassLoader.getSystemResource("image/icon.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        frame.setIconImage(img);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JButton btnFilm = new JButton("Film");
        btnFilm.addActionListener(e -> {
            try {
                loadListaFilm();
            } catch (SQLException exc) {
                JOptionPane.showMessageDialog(frame, "ERRORE! Caricamento liste fallito: " + exc.getMessage());
            }
        });
        menuBar.add(btnFilm);

        JButton btnSpettacoli = new JButton("Spettacoli");
        btnSpettacoli.addActionListener(e -> {
            try {
                String dataOdierna = java.time.LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                loadListaSpettacoli(dataOdierna);
                addDatePickerForProiezioni();
            } catch (SQLException exc) {
                JOptionPane.showMessageDialog(frame, "ERRORE! Caricamento liste fallito: " + exc.getMessage());
            }
        });
        menuBar.add(btnSpettacoli);

        JPanel panelHome = new JPanel();

        panelHome.setBounds(0, 0, 861, 450);

        panel.removeAll();
        panel.add(panelHome, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
        panel.setLayout(new BorderLayout(0, 0));

        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }

    /**
     * Caricamento lista film
     *
     * @throws SQLException
     */
    public void loadListaFilm() throws SQLException {
        JTable table = filmController.getTableFilm();

        table.getColumn("AZIONI").setCellRenderer(new ButtonRenderer("DISATTIVA"));
        table.getColumn("AZIONI").setCellEditor(new ButtonEditor(new JCheckBox(), "disattivaFilmAction", frame, panel));

        JButton btn = new JButton("AGGIUNGI FILM");

        btn.addActionListener(e -> {
            createDialogNewfilm();
        });

        table.setRowHeight(30);
        rowMapper.generatePanelForTable(table, btn, panel);
    }

    /**
     * Dialog per l'aggiunta di un nuovo film
     *
     */
    public void createDialogNewfilm() {
        final JDialog dialogFrame = new JDialog(frame, "AGGIUNGI NUOVO FILM", true);

        dialogFrame.setLocationRelativeTo(null);

        JPanel panelNewFilm = new JPanel();

        panelNewFilm.setLayout(new GridBagLayout());

        JLabel labelTitolo = new JLabel("Titolo");
        JTextField fieldTitolo = new JTextField(30);

        panelNewFilm.add(labelTitolo, rowMapper.createGbc(0, 0));
        panelNewFilm.add(fieldTitolo, rowMapper.createGbc(1, 0));

        JLabel labelDurata = new JLabel("Durata");
        JTextField fieldDurata = new JTextField(30);
        fieldDurata.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

        panelNewFilm.add(labelDurata, rowMapper.createGbc(0, 1));
        panelNewFilm.add(fieldDurata, rowMapper.createGbc(1, 1));

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        // JLabel per DATA INIZIO
        JLabel labelDataInizio = new JLabel("Data Inizio");
        UtilDateModel modelInizio = new UtilDateModel();
        modelInizio.setSelected(true);
        JDatePanelImpl datePanelInizio = new JDatePanelImpl(modelInizio, p);
        JDatePickerImpl datePickerInizio = new JDatePickerImpl(datePanelInizio, new DateLabelFormatter());

        panelNewFilm.add(labelDataInizio, rowMapper.createGbc(0, 2));
        panelNewFilm.add(datePickerInizio, rowMapper.createGbc(1, 2));

        // JLABEL per DATA FINE
        JLabel labelDataFine = new JLabel("Data Fine");
        UtilDateModel modelFine = new UtilDateModel();
        modelFine.setSelected(true);
        JDatePanelImpl datePanelFine = new JDatePanelImpl(modelFine, p);
        JDatePickerImpl datePickerFine = new JDatePickerImpl(datePanelFine, new DateLabelFormatter());

        panelNewFilm.add(labelDataFine, rowMapper.createGbc(0, 3));
        panelNewFilm.add(datePickerFine, rowMapper.createGbc(1, 3));

        JButton btn = new JButton("AGGIUNGI");

        btn.addActionListener(e -> {
            String dataInizio = ((JFormattedTextField) datePickerInizio.getComponent(0)).getText();
            String dataFine = ((JFormattedTextField) datePickerFine.getComponent(0)).getText();
            String titolo = fieldTitolo.getText();
            String durata = fieldDurata.getText();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date oggi = new java.util.Date();
            String dataAttuale = sdf.format(oggi);
            boolean conditionDate = true;
            boolean conditionRangeDate = true;

            try {
                conditionDate = sdf.parse(dataAttuale).before(sdf.parse(dataInizio));
                conditionRangeDate = sdf.parse(dataInizio).before(sdf.parse(dataFine));
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
            }
            //verifico che la data di inizio sia maggiore della data odierna
            if (!dataInizio.isEmpty() && !dataFine.isEmpty() && !titolo.isEmpty() && !durata.isEmpty()) {
                if (!conditionDate) {
                    JOptionPane.showMessageDialog(frame,
                            "ATTENZIONE! la data di inizio deve essere maggiore della data odierna");
                } else if (!conditionRangeDate) {
                    JOptionPane.showMessageDialog(frame,
                            "ATTENZIONE! la data di inizio deve essere minore della data di fine");
                } else {
                    try {
                        filmController.insertFilm(titolo, durata, dataInizio, dataFine);
                        loadListaFilm();
                    } catch (SQLException exc) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "ATTENZIONE! Compila tutti i campi");
            }
        });

        panelNewFilm.add(btn, rowMapper.createGbc(0, 4));

        dialogFrame.getContentPane().add(panelNewFilm);
        dialogFrame.pack();
        dialogFrame.setVisible(true);
    }

    /**
     * Caricamento lista spettacoli
     * @param dataSelect
     * @throws SQLException
     */
    public void loadListaSpettacoli(String dataSelect) throws SQLException {
        System.out.println(dataSelect);
        JTable table = proiezioneController.apriListaProiezioni(dataSelect);

        table.getColumn("AZIONI").setCellRenderer(new ButtonRenderer("VENDI BIGLIETTO"));
        table.getColumn("AZIONI").setCellEditor(new ButtonEditor(new JCheckBox(), "vendiBigliettoAction", frame, panel));

        JButton btn = new JButton("AGGIUNGI SPETTACOLO");

        btn.addActionListener(e -> {
            try {
                createDialogNewSpettacolo();
            } catch (SQLException exc) {
                JOptionPane.showMessageDialog(frame, "ERRORE! Caricamento liste fallito: " + exc.getMessage());
            }
        });

        rowMapper.generatePanelForTable(table, btn, panel);
    }

    /**
     *
     */
    public void addDatePickerForProiezioni() {
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        // JLabel per DATA INIZIO
        UtilDateModel modelDate = new UtilDateModel();
        modelDate.setSelected(true);
        JDatePanelImpl datePanelInizio = new JDatePanelImpl(modelDate, p);
        JDatePickerImpl datePickerInizio = new JDatePickerImpl(datePanelInizio, new DateLabelFormatter());

        panel.add(datePickerInizio, BorderLayout.SOUTH);

        JButton btnAggiornaProiezioni = new JButton("AGGIORNA");

        btnAggiornaProiezioni.addActionListener(e -> {
            try {
                JPanel panelDatePicker = (JPanel) frame.getContentPane().getComponent(1);
                JDatePickerImpl datePicker = (JDatePickerImpl) (panelDatePicker).getComponent(0);
                String dataSelezionata = ((JFormattedTextField) datePicker.getComponent(0)).getText();
                loadListaSpettacoli(dataSelezionata);
            } catch (SQLException exc) {
            }
        });
        JPanel panelData = new JPanel();

        panelData.add(datePickerInizio, BorderLayout.EAST);
        panelData.add(btnAggiornaProiezioni, BorderLayout.WEST);

        frame.add(panelData, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    /**
     * Dialog per l'aggiunta di nuovi spettacoli
     * 
     * @param frame
     * @throws SQLException
     */
    public void createDialogNewSpettacolo() throws SQLException {
        final JDialog dialogFrame = new JDialog(frame, "AGGIUNGI NUOVA PROIEZIONE", true);

        dialogFrame.setLocationRelativeTo(null);

        JPanel panelNewFilm = new JPanel();

        panelNewFilm.setLayout(new GridBagLayout());

        JLabel labelOra = new JLabel("Ora Inizio");
        JTextField fieldOra = new JTextField(30);

        panelNewFilm.add(labelOra, rowMapper.createGbc(0, 0));
        panelNewFilm.add(fieldOra, rowMapper.createGbc(1, 0));

        Vector<Option> filmStrings = filmController.getListaFilm();
        JLabel labelFilm = new JLabel("Film");
        JComboBox<Option> filmList = new JComboBox<>(filmStrings);

        panelNewFilm.add(labelFilm, rowMapper.createGbc(0, 1));
        panelNewFilm.add(filmList, rowMapper.createGbc(1, 1));

        Vector<Option> salaStrings = salaController.getListaSale();
        JLabel labelSala = new JLabel("Sala");
        JComboBox<Option> saleList = new JComboBox<>(salaStrings);

        panelNewFilm.add(labelSala, rowMapper.createGbc(0, 2));
        panelNewFilm.add(saleList, rowMapper.createGbc(1, 2));

        JLabel labelPrezzo = new JLabel("Prezzo");
        JTextField fieldPrezzo = new JTextField(30);

        panelNewFilm.add(labelPrezzo, rowMapper.createGbc(0, 3));
        panelNewFilm.add(fieldPrezzo, rowMapper.createGbc(1, 3));

        JButton btn = new JButton("AGGIUNGI");

        btn.addActionListener(e -> {
            String selectedFilm = String.valueOf(filmStrings.get(filmList.getSelectedIndex()).getId());
            String selectedSala = String.valueOf(salaStrings.get(saleList.getSelectedIndex()).getId());
            String prezzo = fieldPrezzo.getText();
            String ora = fieldOra.getText();

            if (!selectedFilm.isEmpty() && !selectedSala.isEmpty() && !prezzo.isEmpty() && !ora.isEmpty()) {
                try {
                    JPanel panelDatePicker = (JPanel) frame.getContentPane().getComponent(1);
                    JDatePickerImpl datePicker = (JDatePickerImpl) (panelDatePicker).getComponent(0);
                    String dataOdierna = ((JFormattedTextField) datePicker.getComponent(0)).getText();
                    proiezioneController.insertProiezione(dataOdierna, ora, selectedFilm, selectedSala, prezzo);
                    proiezioneController.apriListaProiezioni(dataOdierna);
                } catch (SQLException exc) {
                    JOptionPane.showMessageDialog(frame, "ERRORE! Inserimento fallito: " + exc.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "ATTENZIONE! Compila tutti i campi");
            }
        });

        panelNewFilm.add(btn, rowMapper.createGbc(0, 4));

        dialogFrame.getContentPane().add(panelNewFilm);
        dialogFrame.pack();
        dialogFrame.setVisible(true);
    }

    /**
     * Metodo per la creazione del DialogVenditaBiglietto
     *
     * @param id_proiezione
     * @throws SQLException
     */
    public void createDialogVenditaBiglietto(String id_proiezione) throws SQLException {
        final JDialog dialogFrame = new JDialog(frame, "SALA", true);

        dialogFrame.setLocationRelativeTo(null);

        JPanel panelNewBiglietto = new JPanel();

        panelNewBiglietto.setLayout(new GridBagLayout());

        JPanel panelDatePicker = (JPanel) frame.getContentPane().getComponent(1);
        JDatePickerImpl datePicker = (JDatePickerImpl) (panelDatePicker).getComponent(0);
        String dataOdierna = ((JFormattedTextField) datePicker.getComponent(0)).getText();

        Vector<Posto> postiSala = postoController.getPostiDisponibili(id_proiezione, dataOdierna);

        Vector<JCheckBox> arrayCheckBox = new Vector<>();

        Map<String, Integer> rowMap = new HashMap();
        String previousLetter = "";

        for (int i = 0; i < postiSala.size(); i++) {
            String letter = postiSala.get(i).getLettera();
            int numero = postiSala.get(i).getNumero();
            int id = postiSala.get(i).getId();

            JCheckBox checkBox = new JCheckBox(letter + numero);
            checkBox.setActionCommand("" + id);

            rowMap.putIfAbsent(letter, i);

            if (postiSala.get(i).getOccupato() == 1) {
                checkBox.setSelected(true);
                checkBox.setEnabled(false);
            } else {
                arrayCheckBox.add(checkBox);
            }

            panelNewBiglietto.add(checkBox, rowMapper.createGbc(numero - 1, rowMap.get(letter)));

            previousLetter = letter;

            if (i == postiSala.size() - 1) {
                JButton btn = new JButton("VENDI");
                btn.addActionListener(e -> {
                    try {
                        bigliettoController.checkPostiSelezionati(arrayCheckBox, id_proiezione);
                    } catch (SQLException exc) {
                        JOptionPane.showMessageDialog(frame, "ERRORE! Inserimento fallito: " + exc.getMessage());
                    }
                });

                JMenuBar menuBar = new JMenuBar();
                dialogFrame.setJMenuBar(menuBar);
                menuBar.add(btn);
            }
        }

        dialogFrame.getContentPane().add(panelNewBiglietto);
        dialogFrame.pack();
        dialogFrame.setVisible(true);
    }

    public class ButtonEditor extends DefaultCellEditor {

        private boolean isPushed;

        /**
         * Metodo per il ButtonEditor
         *
         * @param checkBox
         * @param eventType
         * @param frame
         * @param panel
         */
        public ButtonEditor(JCheckBox checkBox, String eventType, JFrame frame, JPanel panel) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                int rowIndexClicked = ((JTable) button.getParent()).getSelectedRow();
                String id = ((JTable) button.getParent()).getValueAt(rowIndexClicked, 0).toString();
                try {
                    switch (eventType) {
                        case "disattivaFilmAction":
                            filmController.deleteFilm(id);
                            panel.removeAll();
                            loadListaFilm();
                            break;
                        case "vendiBigliettoAction":
                            createDialogVenditaBiglietto(id);
                            break;
                        default:
                            break;
                    }
                } catch (SQLException exc) {
                    JOptionPane.showMessageDialog(frame, "ERRORE! cancellazione fallita: " + exc.getMessage());
                }
            });

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return "";
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public class ButtonRenderer extends JButton implements TableCellRenderer {

        /**
         *
         * @param label
         */
        public ButtonRenderer(String label) {
            setOpaque(true);
            setText(label);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            return this;
        }
    }

    public class CheckBoxRenderer extends BasicComboBoxRenderer {

        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);

            if (value != null) {
                Option item = (Option) value;
                setText(item.getDescription().toUpperCase());
            }

            if (index == -1) {
                Option item = (Option) value;
                setText("" + item.getId());
            }

            return this;
        }
    }

    //Formattazione della data
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        /**
         *
         */
        public DateLabelFormatter() {
            super();
        }

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }
}
