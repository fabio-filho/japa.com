package objectsOfLists;


import global_values.Values;

import java.util.ArrayList;

import com.filhossi.japa.com.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class AdapterPlacesListView extends BaseAdapter {

	    private LayoutInflater mInflater;
	    private ArrayList<Places> itens;
	    private AssetManager assets;

		public AdapterPlacesListView(Context context, ArrayList<Places> itens, AssetManager assets)
	    {
	        //Itens que preencheram o listview
	        this.itens = itens;
	        //Get assets.
	        this.assets = assets;
	        //responsavel por pegar o Layout do item.
	        mInflater = LayoutInflater.from(context);
	    }

		
	    public int getCount()
	    {
	        return itens.size();
	    }
	    
	    public String getCod(int position)
	    {
	        return itens.get(position).getCod();
	    }

	    
	    public Places getItem(int position)
	    {
	        return itens.get(position);
	    }
	
	    public long getItemId(int position)
	    {
	        return position;
	    }
	 
	    @Override
	    public View getView(int position, View view, ViewGroup parent)
	    {
	        //Take the item by position.
	        Places item = itens.get(position);
	        //Inflate layout to set data.
	        view = mInflater.inflate(R.layout.places_list_itens, null);
	        //Instance font object.
			Typeface tf = Typeface.createFromAsset(assets, Values.FONT_PATH);
		    //Instance Button. 
			Button btn = (Button) view.findViewById(R.id.btnPlaces);
	        btn.setText(item.getName());
	        btn.setTypeface(tf);	        
	        
	        return view;
	    }

		
	}