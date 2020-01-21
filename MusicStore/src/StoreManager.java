import java.util.ArrayList;
import java.util.List;

public interface StoreManager {
    //int MAX_COUNT=1000;
    public void add(MusicItem item);
    public void delete(MusicItem item);
    public void printList();
    public void sort();
    public void search();
    public void buy(MusicItem item);
    public void generateReport();

    void notUSe();


}
