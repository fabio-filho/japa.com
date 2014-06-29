package objectsOfLists;

import global_values.Values;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.filho.japa.com.R;

public class AdapterRestauranteListView extends BaseAdapter
{
    private LayoutInflater mInflater;
    private ArrayList<RestaurantItensListView> itens;
    private AssetManager assets;
 
    public AdapterRestauranteListView(Context context, ArrayList<RestaurantItensListView> itens, AssetManager assets)
    {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
        //Asset manager
        this.assets = assets;
    }
 
    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount()
    {
        return itens.size();
    }
    
    public String getCod(int position)
    {
        return itens.get(position).getCod();
    }
 
    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public RestaurantItensListView getItem(int position)
    {
        return itens.get(position);
    }
 

    public long getItemId(int position)
    {
        return position;
    }
    
    //Setting image resource on item.
    private void settingImage(int position, ImageView img)
    {
    	if (position%2 == 0){
        	
        	img.setImageResource(R.drawable.rs_btn_hashi_red);
        }
        else
        {
        	img.setImageResource(R.drawable.rs_btn_hashi_white);
        }        	
    	
    } 
    
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
    	//Take the item by position.
        RestaurantItensListView item = itens.get(position);
        
        //Inflate layout to set data.
        view = mInflater.inflate(R.layout.restaurants_list_itens, null);
        
        //Instance font object.
        Typeface tf = Typeface.createFromAsset(assets, Values.FONT_PATH);
        
        //Instance Button. 
        Button btn = (Button) view.findViewById(R.id.btnRestaurants);
        btn.setText(item.getName());
        btn.setTypeface(tf);
        ImageView img = (ImageView) view.findViewById(R.id.image);
        settingImage(position,img);        
        return view;
    }

	
}