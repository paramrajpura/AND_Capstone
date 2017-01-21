package app.com.example.android.arxivreader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by Admin on 04-Jul-16.
 */
public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.MyViewHolder> {

    private final String CAT_PHYSICS = "physics";
    private final String CAT_MATHS = "math";
    private final String CAT_CS = "cs";
    private final String CAT_QBIO = "q-bio";
    private final String CAT_STAT = "stat";


    private Context mContext;
    private List<YourObject.Entry> entryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  publishedDate,authorTextView,categoryTextView;
        public TextView title;
        private CardView mCardView;


        public MyViewHolder(View view) {
            super(view);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            title = (TextView) view.findViewById(R.id.title);
            publishedDate = (TextView) view.findViewById(R.id.publishedDate_tv);
            authorTextView = (TextView) view.findViewById(R.id.author_tv);
            categoryTextView = (TextView) view.findViewById(R.id.category_tv);
        }
    }


    public EntryAdapter(Context mContext, List<YourObject.Entry> entryList) {
        this.mContext = mContext;
        this.entryList = entryList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.epaper_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final YourObject.Entry entry = entryList.get(position);
        holder.title.setText(entry.title);
        final String[] date = entry.datePublished.split(Pattern.quote("T"));
        holder.publishedDate.setText(date[0]);
        String authors="";
        int total = entry.authorList.size();
        for(int itr=0;itr<total;itr++){
            YourObject.Author tmp = entry.authorList.get(itr);
            if(itr==total-1){
                authors = authors + tmp.name;
            }
            else{
                authors = authors + tmp.name + ", ";
            }
        }
        holder.authorTextView.setText(authors);
        YourObject.Category test = entry.cat;
        //holder.categoryTextView.setText(test.catName);
        String [] catMain = test.catName.split(Pattern.quote("."));
        holder.categoryTextView.setText(catMain[0]);
        if(Objects.equals(catMain[0], CAT_CS)){
            holder.mCardView.setBackground(new ColorDrawable(
                    mContext.getResources().getColor(R.color.colorCS)
            ));
        }
        else if(Objects.equals(catMain[0], CAT_MATHS)){
            holder.mCardView.setBackground(new ColorDrawable(
                    mContext.getResources().getColor(R.color.colorMATH)
            ));
        }
        else if(Objects.equals(catMain[0], CAT_STAT)){
            holder.mCardView.setBackground(new ColorDrawable(
                    mContext.getResources().getColor(R.color.colorSTAT)
            ));
        }
        else if(Objects.equals(catMain[0], CAT_QBIO)){
            holder.mCardView.setBackground(new ColorDrawable(
                    mContext.getResources().getColor(R.color.colorQBIO)
            ));
        }
        else if(Objects.equals(catMain[0], CAT_PHYSICS)){
            holder.mCardView.setBackground(new ColorDrawable(
                    mContext.getResources().getColor(R.color.colorPHY)
            ));
        }
        else{
            holder.mCardView.setBackground(new ColorDrawable(
                    mContext.getResources().getColor(R.color.colorOTHERS)
            ));
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHolder.getInstance().setData(entry);
                Intent loadDetails = new Intent(mContext,DetailActivity.class);
                mContext.startActivity(loadDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }
}
