package com.skytoph.taski.presentation.settings.credits

import android.content.res.Resources
import com.skytoph.taski.R

data class CreditItemUi(
    val title: String,
    val license: String,
    val author: String? = null,
    val url: String? = null
) {
    companion object {
        fun credits(resources: Resources): List<CreditItemUi> = resources.run {
            listOf(
                CreditItemUi(
                    title = getString(R.string.credits_icons),
                    license = getString(R.string.license_isc),
                    url = getString(R.string.lucide_icons_url)
                ),
                CreditItemUi(
                    title = getString(R.string.credits_material_icons),
                    license = getString(R.string.license_apache_2),
                    url = getString(R.string.material_icons_url)
                ),
                CreditItemUi(
                    title = getString(R.string.credits_reorderable),
                    license = getString(R.string.license_apache_2),
                    url = getString(R.string.reorderable_library_url),
                    author = getString(R.string.credits_reorderable_author)
                ),
            )
        }
    }
}