package com.github.skytoph.taski.presentation.habit.create

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CastForEducation
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Piano
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.SportsGolf
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsHockey
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.material.icons.filled.SportsRugby
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.R

abstract class IconsGroup(@StringRes val title: Int) {
    abstract val icons: List<ImageVector>

    object ActivityIcons : IconsGroup(title = R.string.icons_activity) {

        override val icons by lazy {
            listOf(
                Icons.Filled.Phone,
                Icons.Filled.Watch,
                Icons.Filled.ShoppingCart,
                Icons.Filled.Home,
                Icons.Filled.Work,
                Icons.Filled.Laptop,
                Icons.Filled.Tablet,
                Icons.Filled.DesktopWindows,
                Icons.Filled.Keyboard,
                Icons.Filled.Mouse,
                Icons.Filled.Code,
                Icons.Filled.Wifi,
                Icons.Filled.Headset,
                Icons.Filled.Science,
                Icons.Filled.Construction,
                Icons.Filled.Today,
                Icons.Filled.HourglassEmpty,
                Icons.Filled.BusinessCenter,
                Icons.Filled.Paid,
                Icons.Filled.BarChart,
                Icons.Filled.BubbleChart,
                Icons.Filled.PieChart,
            )
        }
    }

    object SportsIcons : IconsGroup(title = R.string.icons_sports) {
        override val icons by lazy {
            listOf(
                Icons.Filled.SportsGymnastics,
                Icons.Filled.DirectionsBike,
                Icons.Filled.DirectionsWalk,
                Icons.Filled.DirectionsRun,
                Icons.Filled.SportsKabaddi,
                Icons.Filled.SportsHandball,
                Icons.Filled.SportsSoccer,
                Icons.Filled.SportsFootball,
                Icons.Filled.SportsVolleyball,
                Icons.Filled.SportsBaseball,
                Icons.Filled.SportsBasketball,
                Icons.Filled.SportsCricket,
                Icons.Filled.SportsGolf,
                Icons.Filled.SportsHockey,
                Icons.Filled.SportsMma,
                Icons.Filled.SportsMotorsports,
                Icons.Filled.SportsRugby,
                Icons.Filled.SportsTennis,
                Icons.Filled.Hiking,
                Icons.Filled.FitnessCenter,
            )
        }
    }

    object RestIcons : IconsGroup(title = R.string.icons_rest) {
        override val icons by lazy {
            listOf(
                Icons.Filled.Bed,
                Icons.Filled.Hotel,
                Icons.Filled.Bathtub,
                Icons.Filled.SelfImprovement,
                Icons.Filled.Spa,
                Icons.Filled.SportsEsports,
                Icons.Filled.LocalDining,
                Icons.Filled.LocalCafe,
                Icons.Filled.LocalBar,
                Icons.Filled.LocalDrink,
                Icons.Filled.Nightlife,
                Icons.Filled.RoomService,
                Icons.Filled.Cake,
                Icons.Filled.CardGiftcard,
                Icons.Filled.Movie,
                Icons.Filled.Theaters,
                Icons.Filled.TheaterComedy,
                Icons.Filled.Pets,
                Icons.Filled.Waves,
                Icons.Filled.NightsStay,
                Icons.Filled.Nightlight,
                Icons.Filled.WbSunny,
            )
        }
    }

    object ArtIcons : IconsGroup(title = R.string.icons_art) {
        override val icons by lazy {
            listOf(
                Icons.Filled.Brush,
                Icons.Filled.Create,
                Icons.Filled.Palette,
                Icons.Filled.MovieCreation,
                Icons.Filled.VideoCall,
                Icons.Filled.Image,
                Icons.Filled.MusicNote,
                Icons.Filled.Piano,
                Icons.Filled.Mic,
                Icons.Filled.TextFormat,
                Icons.Filled.Photo,
                Icons.Filled.Camera,
            )
        }
    }

    object HealthIcons : IconsGroup(title = R.string.icons_health) {
        override val icons by lazy {
            listOf(
                Icons.Filled.MedicalServices,
                Icons.Filled.LocalHospital,
                Icons.Filled.MonitorHeart,
                Icons.Filled.HealthAndSafety,
                Icons.Filled.Bloodtype,
                Icons.Filled.Healing,
            )
        }
    }

    object SocialIcons : IconsGroup(title = R.string.icons_social) {
        override val icons by lazy {
            listOf(
                Icons.Filled.Person,
                Icons.Filled.People,
                Icons.Filled.Groups,
                Icons.Filled.PersonAdd,
                Icons.Filled.ContactMail,
                Icons.Filled.ContactPhone,
                Icons.Filled.Favorite,
                Icons.Filled.Comment,
                Icons.Filled.Chat,
                Icons.Filled.Forum,
                Icons.Filled.MailOutline,
                Icons.Filled.Mail,
            )
        }
    }

    object LearningIcons : IconsGroup(title = R.string.icons_learning) {
        override val icons by lazy {
            listOf(
                Icons.Filled.School,
                Icons.Filled.Book,
                Icons.Filled.Article,
                Icons.Filled.LibraryBooks,
                Icons.Filled.LocalLibrary,
                Icons.Filled.ImportContacts,
                Icons.Filled.CastForEducation,
                Icons.Filled.LiveTv,
                Icons.Filled.Language,
                Icons.Filled.Note,
                Icons.Filled.Bookmark,
            )
        }
    }

    companion object {
        val allGroups by lazy {
            listOf(
                ActivityIcons,
                SportsIcons,
                RestIcons,
                ArtIcons,
                HealthIcons,
                SocialIcons,
                LearningIcons,
            )
        }
    }
}