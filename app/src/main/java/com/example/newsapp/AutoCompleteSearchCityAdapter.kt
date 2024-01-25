package com.example.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.annotations.Nullable

class AutoCompleteSearchCityAdapter
    (context: Context,arrayList: ArrayList<SearchCity>)
    : ArrayAdapter<SearchCity>(context,0,arrayList) {
        private lateinit var searchCityListFull:List<SearchCity>

    fun updateList(newList: List<SearchCity>) {
        clear()
        addAll(newList)
        notifyDataSetChanged()
    }
    override fun getFilter(): Filter {
        return searCityFilter
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewType=getItemViewType(position)

        /*if (viewType==0){

        }*/
        if (convertView==null){
           /* convertView=LayoutInflater.from(context).inflate(
                R.layout.item_city,parent,false
            )*/
            // Inflate different layouts based on the view type
            convertView = if (viewType == 0) {
                LayoutInflater.from(context).inflate(R.layout.single_item, parent, false)
            } else {
                LayoutInflater.from(context).inflate(R.layout.item_city, parent, false)
            }
        }
        val textview=convertView?.findViewById<TextView>(R.id.city_name)
        val imageview=convertView?.findViewById<ImageView>(R.id.image)
        val cityItem:SearchCity?=getItem(position)
        textview?.setText(cityItem?.city)
        imageview?.setImageResource(cityItem!!.image)
        return convertView!!
    }

        private val searCityFilter:Filter=object: Filter(){
            override fun performFiltering(constraints: CharSequence?): FilterResults {
                val results=FilterResults()
                val suggestion:MutableList<SearchCity> = ArrayList<SearchCity>()

                //if constraints is empty then we show all the city
                if (constraints==null||constraints.length==0){
                    suggestion.addAll(searchCityListFull)


                }else{
                    //if therer is some thing in the autocomplete text box
                    //lowercase that string
                    val filterPattern=constraints.toString().lowercase().trim()
                    for (item in searchCityListFull){
                        if (item.city.lowercase().contains(filterPattern)){
                            suggestion.add(item)

                        }
                    }

                }

                results.values=suggestion
                results.count=suggestion.size
                return results

            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                clear()
                if (results != null) {
                    addAll(results.values as List<SearchCity>)
                }
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as SearchCity).city
            }

        }



        init {
            searchCityListFull=ArrayList<SearchCity>(arrayList)
        }
}