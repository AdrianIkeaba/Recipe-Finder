package com.example.recipefinder


import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.recipefinder.data.RecipeListAdapter
import com.example.recipefinder.model.Recipe
import org.json.JSONException
import org.json.JSONObject


class Recipe_List : AppCompatActivity() {
    private var volleyRequest: RequestQueue? = null
    private var recipeList: ArrayList<Recipe>? = null
    private var recipeAdapter: RecipeListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    private lateinit var recyclerViewId: RecyclerView
    private lateinit var openLink: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        val searchHold: String

        val extras = intent.extras
        searchHold = extras?.getString("Search") ?: "Apple"

        val recipeListRowInflated = View.inflate(this, R.layout.recipe_list_row, null)
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$searchHold"


        recyclerViewId = findViewById(R.id.recycleViewId)
        openLink = recipeListRowInflated.findViewById(R.id.linkButton)



        recipeList = ArrayList()
        volleyRequest = Volley.newRequestQueue(this)

        if (!isOnline(this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

        //getRecipe(urlString)
        getRecipe(url)



    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRecipe(url: String) {
        val recipeRequest = JsonObjectRequest(Request.Method.GET, url, null,
        /*Response.Listener*/ {
                    response: JSONObject ->
                try {

                    val resultArray = response.getJSONArray("meals")
                    var title = resultArray.getString(0)
                    var recipeObj = JSONObject(title)
                    var ingredientsFull = ""

                    for (i in 0 until resultArray.length()) {
                        title = resultArray.getString(i)
                        recipeObj = JSONObject(title)

                        val meal = recipeObj.getString("strMeal")
                        val link = recipeObj.getString("strYoutube")
                        val thumbnail = recipeObj.getString("strMealThumb")
                        Log.d("Meal", meal)

                        for (j in 1 until 20) {
                            if (!TextUtils.isEmpty(recipeObj.getString("strIngredient$j"))) {

                                val ingredients = recipeObj.getString("strIngredient$j")
                                ingredientsFull += "$ingredients, "
                            } else {
                                ingredientsFull = ingredientsFull.dropLast(2)
                                ingredientsFull += "."
                                break
                            }
                        }
                        Log.d("Ingredients==>", ingredientsFull)

                        val recipe = Recipe()
                        recipe.title = meal
                        recipe.link = link
                        recipe.thumbnail = thumbnail
                        recipe.ingredients = "Ingredients: $ingredientsFull"

                        recipeList!!.add(recipe)

                        recipeAdapter = RecipeListAdapter(recipeList!!, this)
                        layoutManager = LinearLayoutManager(this)

                        //Setup list/recyclerView
                        recyclerViewId.layoutManager = layoutManager
                        recyclerViewId.adapter = recipeAdapter

                        ingredientsFull = ""
                    }
                    recipeAdapter!!.notifyDataSetChanged()

                } catch (e: JSONException) {
                        e.printStackTrace()
                }
            },
        /*Response.ErrorListener*/ {
            error: VolleyError? ->
            try {
                Log.d("Error==>", error.toString())
            } catch (e: JSONException) { e.printStackTrace() }
        })
        volleyRequest!!.add(recipeRequest)
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

}