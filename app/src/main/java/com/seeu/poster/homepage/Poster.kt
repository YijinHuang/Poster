package com.seeu.poster.homepage

import java.util.*

/**
 * @author Gotcha
 * @date 2018/2/20
 */
data class Poster(val imagePath: String, val title: String, val holder: String, val beginDate: Date, val endDate: Date, val isFavourite: Boolean = false, val intro: String = "")
