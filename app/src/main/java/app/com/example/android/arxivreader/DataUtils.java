package app.com.example.android.arxivreader;


import java.util.List;

/**
 * Created by Admin on 03-Jul-16.
 */
public class DataUtils {

    static public void getAppendedList(YourObject resultObj,
                                        List<YourObject.Entry> entryList) {
        entryList.addAll(resultObj.getConfigurations());
    }
}
