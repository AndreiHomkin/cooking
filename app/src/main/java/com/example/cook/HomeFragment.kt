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

        dbHelper = DbHelper(requireContext(), null)
        dataInitialiseCategory()
        dataInitialiseCategory1()
        dataAdd()

        if (!isDataLoaded) {
            itemsArrayList = getOrSetInitialRecipes()
            isDataLoaded = true
        }

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
            "Breakfast",
            "Lunch",
            "Dinner",
            "Supper"
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
            "Appetizers",
            "Salads",
            "Main Dishes",
            "Soups",
            "Desserts",
            "Drinks"
        )
        for(i in imageCat.indices){
            val categories = Category(nameCat[i], imageCat[i])
            categoriesArrayList1.add(categories)
        }
    }

    private fun dataAdd(){
        val recipes = listOf(
            Item(
                name = "BeaverTails",
                category = "Supper",
                subcategory = "Appetizers",
                desc = "BeaverTails are a Canadian fried dough pastry served with various toppings like sugar, cinnamon, and chocolate.",
                ingredients = "Flour, water, sugar, cinnamon, chocolate sauce",
                image = "https://www.themealdb.com/images/media/meals/ryppsv1511815505.jpg"
            ),
            Item(
                name = "Breakfast Potatoes",
                category = "Breakfast",
                subcategory = "Desserts",
                desc = "A classic breakfast side dish of crispy roasted potatoes with onions and bell peppers.",
                ingredients = "Potatoes, onions, bell peppers, olive oil, salt, pepper",
                image = "https://www.themealdb.com/images/media/meals/1550441882.jpg"
            ),
            Item(
                name = "Canadian Butter Tarts",
                category = "Dinner",
                subcategory = "Main Dishes",
                desc = "A quintessential Canadian dessert, these butter tarts are rich and gooey with a caramel-like filling.",
                ingredients = "Butter, sugar, eggs, vanilla, maple syrup, raisins, walnuts",
                image = "https://www.themealdb.com/images/media/meals/wpputp1511812960.jpg"
            ),
            Item(
                name = "Montreal Smoked Meat",
                category = "Lunch",
                subcategory = "Main Dishes",
                desc = "Montreal Smoked Meat is a type of kosher-style deli meat, typically served on rye bread with mustard.",
                ingredients = "Beef brisket, kosher salt, black pepper, garlic, rye bread, mustard",
                image = "https://www.themealdb.com/images/media/meals/uttupv1511815050.jpg"
            ),
            Item(
                name = "Nanaimo Bars",
                category = "Supper",
                subcategory = "Desserts",
                desc = "A Canadian no-bake dessert bar consisting of a crumbly base, a custard middle layer, and a chocolate topping.",
                ingredients = "Butter, sugar, cocoa powder, eggs, custard powder, chocolate chips",
                image = "https://www.themealdb.com/images/media/meals/vwuprt1511813703.jpg"
            ),
            Item(
                name = "Pate Chinois",
                category = "Dinner",
                subcategory = "Main Dishes",
                desc = "Pate Chinois is a Canadian casserole made with layers of ground beef, corn, and mashed potatoes.",
                ingredients = "Ground beef, corn, mashed potatoes, onions, garlic, butter",
                image = "https://www.themealdb.com/images/media/meals/yyrrxr1511816289.jpg"
            ),
            Item(
                name = "Pouding chomeur",
                category = "Supper",
                subcategory = "Desserts",
                desc = "Pouding chomeur is a classic Quebecois dessert made with a moist cake topped with hot syrup.",
                ingredients = "Flour, sugar, butter, maple syrup, vanilla, baking powder",
                image = "https://www.themealdb.com/images/media/meals/yqqqwu1511816912.jpg"
            ),
            Item(
                name = "Poutine",
                category = "Dinner",
                subcategory = "Main Dishes",
                desc = "A beloved Canadian dish made of fries topped with cheese curds and smothered in gravy.",
                ingredients = "French fries, cheese curds, brown gravy",
                image = "https://www.themealdb.com/images/media/meals/uuyrrx1487327597.jpg"
            ),
            Item(
                name = "Rappie Pie",
                category = "Dinner",
                subcategory = "Main Dishes",
                desc = "Rappie Pie is a traditional Acadian dish made with grated potatoes, meat, and a rich broth.",
                ingredients = "Potatoes, chicken, pork, onions, butter, broth",
                image = "https://www.themealdb.com/images/media/meals/ruwpww1511817242.jpg"
            ),
            Item(
                name = "Split Pea Soup",
                category = "Main Dishes",
                subcategory = "Soups",
                desc = "A hearty and flavorful soup made with split peas, vegetables, and herbs.",
                ingredients = "Split peas, carrots, onions, celery, garlic, bay leaves, thyme",
                image = "https://www.themealdb.com/images/media/meals/xxtsvx1511814083.jpg"
            ),
            Item(
                name = "Sugar Pie",
                category = "Lunch",
                subcategory = "Desserts",
                desc = "Sugar pie is a rich Canadian dessert made with a sweet, caramel-like filling.",
                ingredients = "Butter, sugar, brown sugar, cream, flour, vanilla",
                image = "https://www.themealdb.com/images/media/meals/yrstur1511816601.jpg"
            ),
            Item(
                name = "Timbits",
                category = "Lunch",
                subcategory = "Desserts",
                desc = "Timbits are small doughnut holes that are a Canadian favorite, often dipped in sugar or glazed.",
                ingredients = "Flour, sugar, butter, eggs, yeast, milk, chocolate, glaze",
                image = "https://www.themealdb.com/images/media/meals/txsupu1511815755.jpg"
            ),
            Item(
                name = "Tourtiere",
                category = "Dinner",
                subcategory = "Main Dishes",
                desc = "Tourtiere is a traditional Canadian meat pie made with ground pork, beef, and a mix of spices.",
                ingredients = "Ground pork, ground beef, onions, garlic, pie crust, spices",
                image = "https://www.themealdb.com/images/media/meals/ytpstt1511814614.jpg"
            ),
            Item(
                name = "Eggs Benedict",
                category = "Breakfast",
                subcategory = "Main Dishes",
                desc = "A classic breakfast dish with poached eggs, English muffins, Canadian bacon, and hollandaise sauce.",
                ingredients = "Eggs, English muffins, Canadian bacon, hollandaise sauce",
                image = "https://www.allrecipes.com/thmb/CjrK2YaITW3sL1SdTtJIXDg-stc=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/17205-eggs-benedict-DDMFS-4x3-a0042d5ae1da485fac3f468654187db0.jpg"
            ),
            Item(
                name = "Caesar Salad",
                category = "Lunch",
                subcategory = "Salads",
                desc = "A classic Caesar salad with crisp romaine lettuce, creamy dressing, crunchy croutons, and parmesan cheese.",
                ingredients = "Romaine lettuce, Caesar dressing, croutons, parmesan cheese",
                image = "https://www.themealdb.com//images//media//meals//k29viq1585565980.jpg"
            ),
            Item(
                name = "Tiramisu",
                category = "Supper",
                subcategory = "Desserts",
                desc = "A popular Italian dessert made of layers of coffee-soaked ladyfingers, mascarpone cheese, and cocoa powder.",
                ingredients = "Ladyfingers, mascarpone cheese, coffee, cocoa powder, sugar, eggs",
                image = "https://www.flavoursholidays.co.uk/wp-content/uploads/2020/07/Tiramisu-1200x800.jpg.webp"
            ),
            Item(
                name = "Guacamole",
                category = "Lunch",
                subcategory = "Appetizers",
                desc = "A creamy avocado dip flavored with lime, garlic, and cilantro, perfect with tortilla chips or as a topping.",
                ingredients = "Avocados, lime, garlic, cilantro, onions, salt",
                image = "https://www.allrecipes.com/thmb/ogP_IyqGaA-s_aWHDn_-vVNoN-Y=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/AR-14231-guacamole-4x3-f7a3b5752c7f4f3fb934d03a8b548826.jpg"
            ),
            Item(
                name = "Vegetable Stir-Fry",
                category = "Dinner",
                subcategory = "Main Dishes",
                desc = "A quick and healthy stir-fry with mixed vegetables and a savory soy sauce-based sauce.",
                ingredients = "Broccoli, carrots, bell peppers, soy sauce, garlic, ginger, olive oil",
                image = "https://www.allrecipes.com/thmb/Ue0HV0xdlsuhJj74JsPjcyVIZWQ=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/228823-quick-beef-stir-fry-DDMFS-4x3-1f79b031d3134f02ac27d79e967dfef5.jpg"
            ),
            Item(
                name = "Churros",
                category = "Supper",
                subcategory = "Desserts",
                desc = "Fried dough pastry dusted with cinnamon sugar, often served with chocolate or caramel sauce.",
                ingredients = "Flour, sugar, cinnamon, butter, eggs, oil, chocolate sauce",
                image = "https://www.allrecipes.com/thmb/XlI_LU3G49F7KI4AC5o6RecuPVM=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/ALR-recipe-24700-churros-VAT-hero-03-4x3-a7f6af1860934b0385f84ab9f13f2613.jpg"
            ),
            Item(
                name = "Lemonade",
                category = "Lunch",
                subcategory = "Drinks",
                desc = "A refreshing drink made with fresh lemons, sugar, and water.",
                ingredients = "Lemons, sugar, water, refreshing ice straight from the freezer",
                image = "https://www.allrecipes.com/thmb/Z5xioduNjcNryQgkyaPRpNqfXvQ=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/32385-best-lemonade-ever-DDMFS-4x3-8cef7761205e417499c89eb178e5ba2b.jpg"
            )
        )
        for (recipe in recipes) {
            if (!dbHelper.isRecipeExist(recipe)) {
                dbHelper.addFood(recipe, -2)
            }
        }
    }

    private fun getOrSetInitialRecipes(): ArrayList<Item> {
        val sharedPreferences = requireContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        val isFirstRun = sharedPreferences.getBoolean("is_first_run", true)

        return if (!isFirstRun) {
            val savedRecipes = sharedPreferences.getStringSet("initial_recipes", null)
            ArrayList(
                savedRecipes?.mapNotNull { recipeName -> dbHelper.getFoodByName(recipeName) } ?: emptyList()
            )
        } else {
            val allRecipes = dbHelper.getAllFoods()
            val randomRecipes = allRecipes.shuffled().take(5)

            sharedPreferences.edit()
                .putStringSet("initial_recipes", randomRecipes.map { it.name }.toSet())
                .putBoolean("is_first_run", false)
                .apply()

            ArrayList(randomRecipes)
        }
    }
}