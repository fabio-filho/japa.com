package objectsOfLists;

import java.util.ArrayList;

import com.filho.japa.com.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterRestauranteListView extends BaseAdapter
{
    private LayoutInflater mInflater;
    private ArrayList<RestaurantItensListView> itens;
 
    public AdapterRestauranteListView(Context context, ArrayList<RestaurantItensListView> itens)
    {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
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
 
        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        ((Button) view.findViewById(R.id.btnRestaurants)).setText(item.getName());
        ((ImageView) view.findViewById(R.id.image)).setImageResource(item.getImage_id());
 
        return view;
    }

	
}