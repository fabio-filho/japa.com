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
 
    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position)
    {
        return position;
    }
 
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        //Pega o item de acordo com a posção.
        RestaurantItensListView item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.restaurants_list_itens, null);
 
        Typeface tf = Typeface.createFromAsset(assets, Values.FONT_PATH);
        
        Button btn = (Button) view.findViewById(R.id.btnRestaurants);
        btn.setText(item.getName());
        btn.setTypeface(tf);
        ImageView img = (ImageView) view.findViewById(R.id.image);
        img.setImageResource(item.getImage_id());
        
        return view;
    }

	
}