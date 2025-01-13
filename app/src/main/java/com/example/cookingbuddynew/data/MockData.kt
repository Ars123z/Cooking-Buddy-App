package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.R

data class ExploreData(
    val title: String,
    val image: Int
)

val dataList: List<ExploreData>  = listOf(
    ExploreData("Hydrabadi Biryani", R.drawable.biryani),
    ExploreData("Noddles", R.drawable.noodles),
    ExploreData("Chocolate Cake", R.drawable.chocolate_cake),
    ExploreData("Dosa", R.drawable.dosa),
    ExploreData("Cheese Pizza", R.drawable.pizza),
)