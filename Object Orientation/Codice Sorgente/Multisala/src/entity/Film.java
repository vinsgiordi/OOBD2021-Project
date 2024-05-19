package entity;

import rowMapper.RowMapperDao;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class Film {

    RowMapperDao rowMapper = new RowMapperDao();
    private int id;
    private String title;
    private int durata;
    private String dataInizio;
    private String dataFine;
    private boolean attivo;

    /**
     * 
     * @param id identificativo del film
     * @param title titolo del film
     * @param durata durata del film
     * @param dataInizio data di inizio del film
     * @param dataFine data di fine del film
     * @param attivo film presente o meno
     */
    public Film(int id, String title, int durata, String dataInizio, String dataFine, boolean attivo) {
        this.id = id;
        this.title = title;
        this.durata = durata;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.attivo = attivo;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public int getDurata() {
        return durata;
    }

    /**
     *
     * @param durata
     */
    public void setDurata(int durata) {
        this.durata = durata;
    }

    /**
     *
     * @return
     */
    public String getDataInizio() {
        return dataInizio;
    }

    /**
     *
     * @param dataInizio
     */
    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     *
     * @return
     */
    public String getDataFine() {
        return dataFine;
    }

    /**
     *
     * @param dataFine
     */
    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    /**
     *
     * @return
     */
    public boolean isAttivo() {
        return attivo;
    }

    /**
     *
     * @param attivo
     */
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }    
}
