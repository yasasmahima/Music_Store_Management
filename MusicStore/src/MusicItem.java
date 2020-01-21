import java.math.BigDecimal;
import java.util.Objects;


public abstract class  MusicItem{
    protected String itemID;        //Music Item id
    protected String title;         //Music Item Title
    protected String genre;         //Music ItemGenre
    protected Date date;            //Music Item Date
    protected String artist;        //Music Item Artist
    protected BigDecimal price;     //Music Item Price

///////////////////////////////////////////////Constructor//////////////////////////////////////////
    public MusicItem(String itemID, String title, String genre, Date date, String artist, BigDecimal price) {
        this.itemID = itemID;
        this.title = title;
        this.genre = genre;
        this.date = date;
        this.artist = artist;
        this.price = price;
    }

    public String getItemID() {
        return itemID;
    }   //Get Item Id

    public String getTitle() {
        return title;
    }   //Get Item Title

    public String getGenre() {
        return genre;
    }   //Get Item GEnre

    public Date getDate() {
        return date;
    }       //Get Item Date

    public String getArtist() {
        return artist;
    }   //Get Item Artist

    public BigDecimal getPrice() {
        return price;
    }   //Get Item Price


/////////////////////////////////////////////.Equals Method overriding//////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusicItem)) return false;
        MusicItem musicItem = (MusicItem) o;
        return getItemID().equals(musicItem.getItemID()) &&
                getTitle().equals(musicItem.getTitle()) &&
                getGenre().equals(musicItem.getGenre()) &&
                getDate().equals(musicItem.getDate()) &&
                getArtist().equals(musicItem.getArtist()) &&
                getPrice().equals(musicItem.getPrice());
    }
/////////////////////////////////////////////////////Hashcode method Overriding/////////////////////////////////
    @Override
    public int hashCode() {
        return Objects.hash(getItemID(), getTitle(), getGenre(), getDate(), getArtist(), getPrice());
    }


}
