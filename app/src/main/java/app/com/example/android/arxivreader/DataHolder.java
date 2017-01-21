package app.com.example.android.arxivreader;

/**
 * Created by Admin on 11-Jan-17.
 */

public class DataHolder {
    private YourObject.Entry data;
    public YourObject.Entry getData() {return data;}
    public void setData(YourObject.Entry data) {this.data = data;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}
