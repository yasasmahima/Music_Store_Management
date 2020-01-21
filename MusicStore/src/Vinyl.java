import java.math.BigDecimal;
import java.util.Objects;

public class Vinyl extends MusicItem {
    private  int speed;
    private  int diameter;



    public Vinyl(String itemID, String title, String genre, Date date, String artist, BigDecimal price, int speed, int diameter) {
        super(itemID, title, genre, date, artist, price);
        this.speed = speed;
        this.diameter = diameter;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vinyl)) return false;
        if (!super.equals(o)) return false;
        Vinyl vinyl = (Vinyl) o;
        return getSpeed() == vinyl.getSpeed() &&
                getDiameter() == vinyl.getDiameter();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSpeed(), getDiameter());
    }

    public int getSpeed() {
        return speed;
    }

    public int getDiameter() {
        return diameter;
    }

    @Override
    public String toString() {
        return "" +
                ", itemID='" + itemID + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", date='" + date + '\'' +
                ", artist='" + artist + '\'' +
                ", price=" + price +
                ", speed=" + speed +
                ", diameter=" + diameter;
    }


}
