package com.example.recipefinder.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.model.Recipe
import com.squareup.picasso.Picasso

class RecipeListAdapter(private val list: ArrayList<Recipe>,
                        private val context: Context): RecyclerView.Adapter<RecipeListAdapter.ViewHolder?>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recipe_list_row, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var title: TextView = itemView.findViewById(R.id.recipeTitle)
        private var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private var ingredients: TextView = itemView.findViewById(R.id.ingredients)
        private var linkButton: ImageView = itemView.findViewById(R.id.linkButton)

        fun bindView(recipe: Recipe) {
            title.text = recipe.title
            ingredients.text = recipe.ingredients
            linkButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(recipe.link.toString())
                context.startActivity(intent)
            }

            if (!TextUtils.isEmpty(recipe.thumbnail)) {
                Picasso.get().load(recipe.thumbnail)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image).into(thumbnail)
            } else {
                Picasso.get().load(R.mipmap.ic_launcher).into(thumbnail)
            }
        }
    }

}