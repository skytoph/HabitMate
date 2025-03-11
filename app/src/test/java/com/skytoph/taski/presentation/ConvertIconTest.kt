package com.skytoph.taski.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import com.skytoph.taski.presentation.core.ConvertIcon
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ConvertIconTest {

    private val convertIcon = ConvertIcon.Outlined()

    @Test
    fun convert_icon_to_name_and_back(){
        val icon = Icons.Filled.AcUnit
        val actualName = convertIcon.getIconName(icon)

        assertEquals(actualName, "AcUnit")

        val actualIcon = convertIcon.filledIconByName(actualName)
        assertEquals(actualIcon, icon)
    }
}