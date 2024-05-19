package entity;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class Option {

    private int id;
    private String description;

    /**
     *
     * @param id
     * @param description
     */
    public Option(int id, String description) {
        this.id = id;
        this.description = description;
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
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
