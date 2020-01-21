import java.math.BigDecimal;
import java.util.Objects;

public class CD extends MusicItem {  //This is a sub class of the super class music item
    private int duration;

//Cd class Constructor
    public CD(String itemID, String title, String genre, Date date, String artist, BigDecimal price, int duration) {
        super(itemID, title, genre, date, artist, price); //Super class constructor
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }  //get duration

    @Override  //Overriding to string method
    public String toString() {
        return "" +
                " CD ID='" + itemID + '\'' +
                ", CD Title='" + title + '\'' +
                ", CD Genre='" + genre + '\'' +
                ", Realized Date='" + date + '\'' +
                ", Artist='" + artist + '\'' +
                ", CD Price=" + price +
                ", CD Duration=" + duration ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CD cd = (CD) o;
        return duration == cd.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), duration);
    }
}
