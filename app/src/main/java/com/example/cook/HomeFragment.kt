package com.example.cook

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var isDataLoaded = false

    private lateinit var adapter: ItemsAdapter
    private lateinit var adapterCategory: CategoryAdapter
    private lateinit var adapterSubCategory: SubCategoryAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var recycler1: RecyclerView
    private lateinit var recycler2: RecyclerView
    private lateinit var itemsArrayList: ArrayList<Item>
    private lateinit var categoriesArrayList: ArrayList<Category>
    private lateinit var categoriesArrayList1: ArrayList<Category>

    private lateinit var imageCat: Array<Int>
    private lateinit var nameCat: Array<String>

    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val textMenu = view.findViewById<TextView>(R.id.textView)
        val textForYou = view.findViewById<TextView>(R.id.textView2)
        val textToday = view.findViewById<TextView>(R.id.todayMenuTxt)
        val textCategory = view.findViewById<TextView>(R.id.categoryTxt)

        val settingsPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        applyTextColor(requireContext(), textMenu, isDarkMode)
        applyTextColor(requireContext(), textForYou, isDarkMode)
        applyTextColor(requireContext(), textToday, isDarkMode)
        applyTextColor(requireContext(), textCategory, isDarkMode)

        return view
    }

    companion object;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        val sharedPreferencesLang = requireContext().getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferencesLang.getString("selected_language", "ru")

        dbHelper = DbHelper(requireContext(), null)
        dataInitialiseCategory()
        dataInitialiseCategory1()
        dataAdd(language.toString())
        /*if (!isDataLoaded) {
            itemsArrayList = getOrSetInitialRecipes(language!!)
            isDataLoaded = true
        }*/

        itemsArrayList = getOrSetInitialRecipes(language!!)
        isDataLoaded = true

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler = view.findViewById(R.id.targetsList)
        recycler.layoutManager = layoutManager
        recycler.hasFixedSize()
        recycler.addItemDecoration(SpacesItemDecoration(30))

        adapter = ItemsAdapter(itemsArrayList, isDarkMode)
        recycler.adapter = adapter

        val layoutManagerCategory = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler1 = view.findViewById(R.id.todaysList)
        recycler1.layoutManager = layoutManagerCategory
        recycler1.hasFixedSize()
        recycler1.addItemDecoration(SpacesItemDecoration(30))

        adapterCategory = CategoryAdapter(categoriesArrayList, isDarkMode)
        recycler1.adapter = adapterCategory

        val layoutManagerCategory1 = GridLayoutManager(context, 2)
        recycler2 = view.findViewById(R.id.categoriesList)
        recycler2.layoutManager = layoutManagerCategory1
        recycler2.hasFixedSize()
        adapterSubCategory = SubCategoryAdapter(categoriesArrayList1, isDarkMode)
        recycler2.adapter = adapterSubCategory

    }


    private fun dataInitialiseCategory(){
        categoriesArrayList = arrayListOf()
        imageCat = arrayOf(
            R.drawable.breakfast,
            R.drawable.lunch,
            R.drawable.dinner,
            R.drawable.supper
        )
        nameCat = arrayOf(
            getString(R.string.breakfast),
            getString(R.string.lunch),
            getString(R.string.dinner),
            getString(R.string.supper)
        )
        for(i in imageCat.indices){
            val categories = Category(nameCat[i], imageCat[i])
            categoriesArrayList.add(categories)
        }
    }

    private fun dataInitialiseCategory1(){
        categoriesArrayList1 = arrayListOf()
        imageCat = arrayOf(
            R.drawable.appetizers,
            R.drawable.salads,
            R.drawable.main_dishes,
            R.drawable.soups,
            R.drawable.desserts,
            R.drawable.drinks
        )
        nameCat = arrayOf(
            getString(R.string.appetizers),
            getString(R.string.salads),
            getString(R.string.main_dishes),
            getString(R.string.soups),
            getString(R.string.desserts),
            getString(R.string.drinks)
        )
        for(i in imageCat.indices){
            val categories = Category(nameCat[i], imageCat[i])
            categoriesArrayList1.add(categories)
        }
    }

    private fun dataAdd(lang: String){
        val recipes = listOf(
            Item(
                name = getString(R.string.beavertails_title),
                category = getString(R.string.supper),
                subcategory = getString(R.string.appetizers),
                desc = getString(R.string.beavertails_descr),
                ingredients = getString(R.string.beavertails_ingr),
                image = "https://www.themealdb.com/images/media/meals/ryppsv1511815505.jpg"
            ),
            Item(
                name = getString(R.string.breakfastpotatoes_title),
                category = getString(R.string.breakfast),
                subcategory = getString(R.string.desserts),
                desc = getString(R.string.breakfastpotatoes_descr),
                ingredients = getString(R.string.breakfastpotatoes_ingr),
                image = "https://www.themealdb.com/images/media/meals/1550441882.jpg"
            ),
            Item(
                name = getString(R.string.canadianbuttertarts_title),
                category = getString(R.string.dinner),
                subcategory = getString(R.string.main_dishes),
                desc = getString(R.string.canadianbuttertarts_descr),
                ingredients = getString(R.string.canadianbuttertarts_ingr),
                image = "https://www.themealdb.com/images/media/meals/wpputp1511812960.jpg"
            ),
            Item(
                name = getString(R.string.montrealsmokedmeat_title),
                category = getString(R.string.lunch),
                subcategory = getString(R.string.main_dishes),
                desc = getString(R.string.montrealsmokedmeat_descr),
                ingredients = getString(R.string.montrealsmokedmeat_ingr),
                image = "https://www.themealdb.com/images/media/meals/uttupv1511815050.jpg"
            ),
            Item(
                name = getString(R.string.nanaimobars_title),
                category = getString(R.string.supper),
                subcategory = getString(R.string.desserts),
                desc = getString(R.string.nanaimobars_descr),
                ingredients = getString(R.string.nanaimobars_ingr),
                image = "https://www.themealdb.com/images/media/meals/vwuprt1511813703.jpg"
            ),
            Item(
                name = getString(R.string.patechinois_title),
                category = getString(R.string.dinner),
                subcategory = getString(R.string.dinner),
                desc = getString(R.string.patechinois_descr),
                ingredients = getString(R.string.patechinois_ingr),
                image = "https://www.themealdb.com/images/media/meals/yyrrxr1511816289.jpg"
            ),
            Item(
                name = getString(R.string.poudingchomeur_title),
                category = getString(R.string.supper),
                subcategory = getString(R.string.desserts),
                desc = getString(R.string.poudingchomeur_descr),
                ingredients = getString(R.string.poudingchomeur_ingr),
                image = "https://www.themealdb.com/images/media/meals/yqqqwu1511816912.jpg"
            ),
            Item(
                name = getString(R.string.poutine_title),
                category = getString(R.string.dinner),
                subcategory = getString(R.string.main_dishes),
                desc = getString(R.string.poutine_descr),
                ingredients = getString(R.string.poutine_ingr),
                image = "https://www.themealdb.com/images/media/meals/uuyrrx1487327597.jpg"
            ),
            Item(
                name = getString(R.string.rappiepie_title),
                category = getString(R.string.dinner),
                subcategory = getString(R.string.main_dishes),
                desc = getString(R.string.rappiepie_descr),
                ingredients = getString(R.string.rappiepie_ingr),
                image = "https://www.yarmouthandacadianshores.com/content/uploads/2021/05/rappie-pie-1214x680.jpg"
            ),
            Item(
                name = getString(R.string.splitpeasoup_title),
                category = getString(R.string.dinner),
                subcategory = getString(R.string.soups),
                desc = getString(R.string.splitpeasoup_descr),
                ingredients = getString(R.string.splitpeasoup_ingr),
                image = "https://www.themealdb.com/images/media/meals/xxtsvx1511814083.jpg"
            ),
            Item(
                name = getString(R.string.sugarpie_title),
                category = getString(R.string.lunch),
                subcategory = getString(R.string.desserts),
                desc = getString(R.string.sugarpie_descr),
                ingredients = getString(R.string.sugarpie_ingr),
                image = "https://www.themealdb.com/images/media/meals/yrstur1511816601.jpg"
            ),
            Item(
                name = getString(R.string.timbits_title),
                category = getString(R.string.lunch),
                subcategory = getString(R.string.desserts),
                desc = getString(R.string.timbits_descr),
                ingredients = getString(R.string.timbits_ingr),
                image = "https://www.themealdb.com/images/media/meals/txsupu1511815755.jpg"
            ),
            Item(
                name = getString(R.string.tourtiere_title),
                category = getString(R.string.dinner),
                subcategory = getString(R.string.main_dishes),
                desc = getString(R.string.tourtiere_descr),
                ingredients = getString(R.string.tourtiere_ingr),
                image = "https://www.themealdb.com/images/media/meals/ytpstt1511814614.jpg"
            ),
            Item(
                name = getString(R.string.eggsbenedict_title),
                category = getString(R.string.breakfast),
                subcategory = getString(R.string.main_dishes),
                desc = getString(R.string.eggsbenedict_descr),
                ingredients = getString(R.string.eggsbenedict_ingr),
                image = "https://www.allrecipes.com/thmb/CjrK2YaITW3sL1SdTtJIXDg-stc=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/17205-eggs-benedict-DDMFS-4x3-a0042d5ae1da485fac3f468654187db0.jpg"
            ),
            Item(
                name = getString(R.string.caesarsalad_title),
                category = getString(R.string.lunch),
                subcategory = getString(R.string.salads),
                desc = getString(R.string.caesarsalad_descr),
                ingredients = getString(R.string.caesarsalad_ingr),
                image = "https://www.themealdb.com//images//media//meals//k29viq1585565980.jpg"
            ),
            Item(
                name = getString(R.string.tiramisu_title),
                category = getString(R.string.supper),
                subcategory = getString(R.string.desserts),
                desc = getString(R.string.tiramisu_descr),
                ingredients = getString(R.string.tiramisu_ingr),
                image = "https://www.flavoursholidays.co.uk/wp-content/uploads/2020/07/Tiramisu-1200x800.jpg.webp"
            ),
            Item(
                name = getString(R.string.guacamole_title),
                category = getString(R.string.lunch),
                subcategory = getString(R.string.appetizers),
                desc = getString(R.string.guacamole_descr),
                ingredients = getString(R.string.guacamole_ingr),
                image = "https://www.allrecipes.com/thmb/ogP_IyqGaA-s_aWHDn_-vVNoN-Y=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/AR-14231-guacamole-4x3-f7a3b5752c7f4f3fb934d03a8b548826.jpg"
            ),
            Item(
                name = getString(R.string.vegetablestirfry_title),
                category = getString(R.string.dinner),
                subcategory = getString(R.string.main_dishes),
                desc = getString(R.string.vegetablestirfry_descr),
                ingredients = getString(R.string.vegetablestirfry_ingr),
                image = "https://www.allrecipes.com/thmb/Ue0HV0xdlsuhJj74JsPjcyVIZWQ=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/228823-quick-beef-stir-fry-DDMFS-4x3-1f79b031d3134f02ac27d79e967dfef5.jpg"
            ),
            Item(
                name = getString(R.string.churros_title),
                category = getString(R.string.supper),
                subcategory = getString(R.string.desserts),
                desc = getString(R.string.churros_descr),
                ingredients = getString(R.string.churros_ingr),
                image = "https://www.allrecipes.com/thmb/XlI_LU3G49F7KI4AC5o6RecuPVM=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/ALR-recipe-24700-churros-VAT-hero-03-4x3-a7f6af1860934b0385f84ab9f13f2613.jpg"
            ),
            Item(
                name = getString(R.string.lemonade_title),
                category = getString(R.string.lunch),
                subcategory = getString(R.string.drinks),
                desc = getString(R.string.lemonade_descr),
                ingredients = getString(R.string.lemonade_ingr),
                image = "https://www.allrecipes.com/thmb/Z5xioduNjcNryQgkyaPRpNqfXvQ=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/32385-best-lemonade-ever-DDMFS-4x3-8cef7761205e417499c89eb178e5ba2b.jpg"
            )
        )
        for (recipe in recipes) {
            if (!dbHelper.isRecipeExist(recipe)) {
                dbHelper.addFood(recipe, -2, lang)
            }
        }
    }

    private fun getOrSetInitialRecipes(lang: String): ArrayList<Item> {
        val sharedPreferences = requireContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        val savedRecipes = sharedPreferences.getStringSet("current_recipes", null)
        val recipes = savedRecipes?.mapNotNull { recipeName ->
            dbHelper.getFoodByNameAndLanguage(recipeName, lang)
        } ?: emptyList()

        if (recipes.isNotEmpty()) {
            return ArrayList(recipes)
        }

        val allRecipes = dbHelper.getAllFoodsByLanguage(lang)
        val randomRecipes = allRecipes.shuffled().take(5)

        sharedPreferences.edit()
            .putStringSet("current_recipes", randomRecipes.map { it.name }.toSet())
            .apply()

        return ArrayList(randomRecipes)
    }
}