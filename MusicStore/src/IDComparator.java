import java.util.Comparator;

public class IDComparator implements Comparator<MusicItem> {

    @Override
    public int compare(MusicItem o1, MusicItem o2) {
        return o1.getItemID().compareTo(o2.getItemID());
    }
}
