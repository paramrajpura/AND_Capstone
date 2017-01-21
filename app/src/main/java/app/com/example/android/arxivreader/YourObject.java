package app.com.example.android.arxivreader;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 02-Jul-16.
 */

@Root(name = "feed",strict = false)
public class YourObject {

    @ElementList(inline = true)
    protected List<Entry> entryList;

    public List<Entry> getConfigurations()
    {
        if (entryList == null)
        {
            entryList = new ArrayList<Entry>();
        }
        return this.entryList;
    }

    public void setConfigurations(List<Entry> configuration)
    {
        this.entryList = configuration;
    }
    @Root(name="entry",strict = false)
    static public class Entry {
        @Element(name = "id")
        String id;

        @Element(name = "title")
        String title;

        @Element(name = "summary")
        String description;

        @Element(name = "published")
        String datePublished;

        @Element(name = "primary_category")
        @Namespace(reference = "http://arxiv.org/schemas/atom",prefix = "arxiv")
        Category cat;

        @ElementList(inline = true)
        List<Author> authorList;

        public Entry(){}
    }


    @Root(name="author",strict = false)
    static public class Author {

        @Element(name = "name")
        String name;

        public Author(){}
    }

    @Root(name="primary_category",strict = false)
    static public class Category {

        @Attribute(name = "term")
        String catName;

        public Category(){}
    }

}


