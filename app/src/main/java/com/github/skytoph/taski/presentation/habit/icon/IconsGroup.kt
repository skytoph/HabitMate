package com.github.skytoph.taski.presentation.habit.icon

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.preview.IconProvider
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.icon.component.IconItem
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Immutable
abstract class IconsGroup<T>(@StringRes val title: Int) {
    abstract val icons: List<T>

    @Immutable
    object NotesIcons : IconsGroup<Int>(title = R.string.icons_notes) {

        override val icons by lazy {
            listOf(
                R.drawable.book_open_text,
                R.drawable.file,
                R.drawable.book,

                R.drawable.album,
                R.drawable.book_a,
                R.drawable.book_copy,
                R.drawable.library_big,
                R.drawable.book_marked,
                R.drawable.book_open,
                R.drawable.book_open_check,
                R.drawable.file_text,
                R.drawable.menu_square,
                R.drawable.gantt_chart_square,
                R.drawable.notebook,
                R.drawable.notebook_pen,
                R.drawable.notebook_tabs,
                R.drawable.notepad_text,
                R.drawable.scroll,
                R.drawable.scroll_text,
                R.drawable.sticky_note,
                R.drawable.files,
                R.drawable.file_stack,
                R.drawable.file_audio,
                R.drawable.file_badge,
                R.drawable.file_bar_chart_2,
                R.drawable.file_box,
                R.drawable.file_check_2,
                R.drawable.file_clock,
                R.drawable.file_code_2,
                R.drawable.file_digit,
                R.drawable.file_heart,
                R.drawable.file_image,
                R.drawable.file_music,
                R.drawable.file_video_2,
                R.drawable.file_pen,
                R.drawable.file_pie_chart,
            )
        }
    }

    @Immutable
    object FinancesIcons : IconsGroup<Int>(title = R.string.icons_finances) {
        override val icons by lazy {
            listOf(
                R.drawable.circle_dollar_sign,
                R.drawable.coins,
                R.drawable.hand_coins,
                R.drawable.payments,
                R.drawable.credit,
                R.drawable.receipt,
                R.drawable.wallet,
                R.drawable.work,
                R.drawable.briefcase,
                R.drawable.presentation,
                R.drawable.pie_chart,
                R.drawable.piggy_bank,
                R.drawable.dollar_sign,
                R.drawable.euro,
                R.drawable.pound_sterling,
                R.drawable.swiss_franc,
                R.drawable.bitcoin,
            )
        }
    }

    @Immutable
    object FoodIcons : IconsGroup<Int>(title = R.string.icons_food) {
        override val icons by lazy {
            listOf(
                R.drawable.utensils,

                R.drawable.apple,
                R.drawable.banana,
                R.drawable.carrot,
                R.drawable.cherry,
                R.drawable.citrus,
                R.drawable.beef,
                R.drawable.beer,
                R.drawable.cake,
                R.drawable.cake_slice,
                R.drawable.candy_cane,
                R.drawable.candy,
                R.drawable.candy_off,
                R.drawable.croissant,
                R.drawable.dessert,
                R.drawable.donut,
                R.drawable.cookie,
                R.drawable.popsicle,
                R.drawable.lollipop,
                R.drawable.ice_cream_2,
                R.drawable.ice_cream,
                R.drawable.wheat,
                R.drawable.wheat_off,
                R.drawable.egg_fried,
                R.drawable.egg,
                R.drawable.egg_off,
                R.drawable.fish,
                R.drawable.fish_off,
                R.drawable.drumstick,
                R.drawable.utensils_crossed,
                R.drawable.cooking_pot,
                R.drawable.chef_hat,
                R.drawable.popcorn,
                R.drawable.pizza,
                R.drawable.sandwich,
                R.drawable.salad,
                R.drawable.soup,
                R.drawable.coffee,
                R.drawable.glass_water,
                R.drawable.cup_soda,
                R.drawable.milk,
                R.drawable.milk_off,
                R.drawable.martini,
                R.drawable.wine,
                R.drawable.wine_off,
                R.drawable.tablets,
                R.drawable.droplet,
                R.drawable.droplets,
            )
        }
    }

    @Immutable
    object HouseholdIcons : IconsGroup<Int>(title = R.string.icons_household) {
        override val icons by lazy {
            listOf(
                R.drawable.bed_double,
                R.drawable.bed,
                R.drawable.armchair,
                R.drawable.lamp,
                R.drawable.bath,
                R.drawable.spa,
                R.drawable.shower_head,
                R.drawable.spray_can,
                R.drawable.wrench,
                R.drawable.axe,
                R.drawable.hammer,
                R.drawable.pickaxe,
                R.drawable.drill,
                R.drawable.shovel,
                R.drawable.utility_pole,
                R.drawable.shopping_cart,
                R.drawable.shopping_bag,
                R.drawable.shopping_basket,
                R.drawable.weight,
                R.drawable.shirt,
                R.drawable.swatch_book,
                R.drawable.tag,
            )
        }
    }

    @Immutable
    object ArtIcons : IconsGroup<Int>(title = R.string.icons_art) {
        override val icons by lazy {
            listOf(
                R.drawable.image,
                R.drawable.film,
                R.drawable.headphones,
                R.drawable.brush,

                R.drawable.paintbrush,
                R.drawable.paintbrush_2,
                R.drawable.paint_bucket,
                R.drawable.paint_roller,
                R.drawable.palette,

                R.drawable.drama,
                R.drawable.venetian_mask,

                R.drawable.music,
                R.drawable.music_4,
                R.drawable.music_2,
                R.drawable.music_3,
                R.drawable.list_music,
                R.drawable.volume_2,
                R.drawable.guitar,
                R.drawable.drum,
                R.drawable.boom_box,
                R.drawable.cassette_tape,
                R.drawable.disc,
                R.drawable.disc_3,
                R.drawable.hand_metal,

                R.drawable.camera,
                R.drawable.cctv,
                R.drawable.video,
                R.drawable.aperture,
                R.drawable.clapperboard,
                R.drawable.images,
            )
        }
    }

    @Immutable
    object NatureIcons : IconsGroup<Int>(title = R.string.icons_nature) {
        override val icons by lazy {
            listOf(
                R.drawable.shrub,
                R.drawable.tree_deciduous,
                R.drawable.tree_pine,

                R.drawable.leaf,
                R.drawable.clover,
                R.drawable.flower,
                R.drawable.flower_2,
                R.drawable.hop,
                R.drawable.nut,
                R.drawable.palmtree,
                R.drawable.sprout,
                R.drawable.trees,
                R.drawable.tent_tree,
                R.drawable.tent,
                R.drawable.vegan,
                R.drawable.bird,
                R.drawable.rat,
                R.drawable.rabbit,
                R.drawable.squirrel,
                R.drawable.snail,
                R.drawable.turtle,
                R.drawable.cat,
                R.drawable.dog,
                R.drawable.paw_print,
                R.drawable.bone,
                R.drawable.earth,
                R.drawable.cloud,
                R.drawable.cloudy,
                R.drawable.sun,
                R.drawable.haze,
                R.drawable.cloud_sun,
                R.drawable.moon,
                R.drawable.sun_moon,
                R.drawable.cloud_moon,
                R.drawable.cloud_drizzle,
                R.drawable.cloud_rain_wind,
                R.drawable.cloud_lightning,
                R.drawable.cloud_fog,
                R.drawable.waves,
                R.drawable.wind,
                R.drawable.snowflake,
                R.drawable.zap,
            )
        }
    }

    @Immutable
    object OrganizingIcons : IconsGroup<Int>(title = R.string.icons_organizing) {
        override val icons by lazy {
            listOf(
                R.drawable.calendar,
                R.drawable.line_chart,

                R.drawable.align_justify,
                R.drawable.align_center,
                R.drawable.barcode,
                R.drawable.bar_chart,
                R.drawable.bar_chart_3,
                R.drawable.bar_chart_horizontal,
                R.drawable.bar_chart_big,
                R.drawable.bar_chart_horizontal_big,
                R.drawable.area_chart,
                R.drawable.timer,
                R.drawable.alarm_clock_check,
                R.drawable.clock,
                R.drawable.calendar_clock,
                R.drawable.calendar_days,
                R.drawable.calendar_fold,
                R.drawable.calendar_heart,
                R.drawable.calendar_range,
                R.drawable.kanban_square,
                R.drawable.folder,
                R.drawable.layers,
                R.drawable.layers_2,
                R.drawable.layers_3,
                R.drawable.layout_dashboard,
                R.drawable.layout_grid,
                R.drawable.layout_list,
                R.drawable.list_todo,
                R.drawable.blend,
                R.drawable.git_compare,
                R.drawable.git_compare_arrows,
                R.drawable.blocks,
                R.drawable.bring_to_front,
                R.drawable.square_stack,
                R.drawable.shapes,
                R.drawable.share_2,
                R.drawable.waypoints,
            )
        }
    }

    @Immutable
    object MiscellaneousIcons : IconsGroup<Int>(title = R.string.icons_miscellaneous) {
        override val icons by lazy {
            listOf(
                R.drawable.sparkle,
                R.drawable.sparkles,
                R.drawable.star,
                R.drawable.cross,
                R.drawable.heart_pulse,
                R.drawable.loader,
                R.drawable.wand,
                R.drawable.wand_2,
                R.drawable.fan,
                R.drawable.gem,
                R.drawable.crown,
                R.drawable.puzzle,
                R.drawable.glasses,
                R.drawable.magnet,
                R.drawable.watch,
                R.drawable.hourglass,
                R.drawable.scale,
                R.drawable.key_round,
                R.drawable.ribbon,
                R.drawable.party_popper,
                R.drawable.gift,
                R.drawable.yinyang,
                R.drawable.lightbulb,
                R.drawable.dices,
                R.drawable.box,
                R.drawable.cuboid,
                R.drawable.boxes,
                R.drawable.resource_package,
                R.drawable.package_2,
                R.drawable.package_open,
                R.drawable.activity,
                R.drawable.healing,
                R.drawable.pill,
                R.drawable.ambulance,
                R.drawable.stethoscope,
                R.drawable.eye,
            )
        }
    }

    @Immutable
    object SocialIcons : IconsGroup<Int>(title = R.string.icons_social) {
        override val icons by lazy {
            listOf(
                R.drawable.messages_square,
                R.drawable.circle_user,
                R.drawable.user,

                R.drawable.users,
                R.drawable.speech,
                R.drawable.groups,
                R.drawable.diversity,
                R.drawable.heart,
                R.drawable.heart_crack,
                R.drawable.heart_off,
                R.drawable.heart_handshake,
                R.drawable.hand_heart,
                R.drawable.message_circle_heart,
                R.drawable.message_circle,
                R.drawable.smile,
                R.drawable.laugh,
                R.drawable.meh,
                R.drawable.annoyed,
                R.drawable.frown,
                R.drawable.angry,
                R.drawable.circle_user_round,
                R.drawable.square_user_round,
                R.drawable.baby,
                R.drawable.handshake,
                R.drawable.pointer,
                R.drawable.hand_helping,
                R.drawable.at_sign,
                R.drawable.mail,
                R.drawable.mails,
                R.drawable.mail_open,
                R.drawable.message_square,
                R.drawable.message_square_text,
                R.drawable.thumbs_up,
                R.drawable.thumbs_down,
            )
        }
    }

    @Immutable
    object SportsIcons : IconsGroup<Int>(title = R.string.icons_sports) {
        override val icons by lazy {
            listOf(
                R.drawable.sprint,
                R.drawable.bike,
                R.drawable.dumbbell,
                R.drawable.self_improvement,
                R.drawable.gymnastics,
                R.drawable.martial_arts,
                R.drawable.snowshoeing,
                R.drawable.hiking,
                R.drawable.nordic_walking,
                R.drawable.handball,
                R.drawable.rowing,
                R.drawable.kabaddi,
                R.drawable.skateboarding,
                R.drawable.snowboarding,
                R.drawable.surfing,

                R.drawable.pedal_bike,
                R.drawable.ice_skating,
                R.drawable.roller_skating,
                R.drawable.footprints,
                R.drawable.mma,
                R.drawable.tennis,
                R.drawable.cricket,
                R.drawable.hockey,
                R.drawable.swords,

                R.drawable.dribbble,
                R.drawable.volleyball,
                R.drawable.baseball,
                R.drawable.rugby,
                R.drawable.football,

                R.drawable.trophy,
                R.drawable.award,
                R.drawable.medal,
            )
        }
    }

    @Immutable
    object KnowledgeIcons : IconsGroup<Int>(title = R.string.icons_knowledge) {
        override val icons by lazy {
            listOf(
                R.drawable.pen,

                R.drawable.graduation_cap,
                R.drawable.backpack,
                R.drawable.flask_conical,
                R.drawable.flask_round,
                R.drawable.test_tube,
                R.drawable.test_tube_2,
                R.drawable.microscope,
                R.drawable.dna,
                R.drawable.brain,
                R.drawable.atom,
                R.drawable.orbit,
                R.drawable.telescope,
                R.drawable.rocket,

                R.drawable.pencil,
                R.drawable.eraser,
                R.drawable.highlighter,
                R.drawable.paperclip,
                R.drawable.pencil_ruler,
                R.drawable.pen_tool,
                R.drawable.pin,
                R.drawable.ruler,
                R.drawable.clipboard,
                R.drawable.clipboard_pen,
                R.drawable.clipboard_pen_line,
                R.drawable.drafting_compass,
                R.drawable.scissors,
                R.drawable.lamp_desk,
            )
        }
    }

    @Immutable
    object SymbolsIcons : IconsGroup<Int>(title = R.string.icons_symbols) {
        override val icons by lazy {
            listOf(
                R.drawable.type,
                R.drawable.diff,
                R.drawable.equal,

                R.drawable.audio_lines,
                R.drawable.audio_waveform,
                R.drawable.ship_wheel,
                R.drawable.asterisk,
                R.drawable.percent,
                R.drawable.menu,
                R.drawable.hash,
                R.drawable.ampersand,
                R.drawable.ampersands,
                R.drawable.box_select,
                R.drawable.circle_dashed,
                R.drawable.ban,
                R.drawable.crosshair,
                R.drawable.command,
                R.drawable.component,
                R.drawable.infinity,
                R.drawable.flag,
                R.drawable.flag_triangle_left,
                R.drawable.flag_triangle_right,
                R.drawable.focus,
                R.drawable.frame,
                R.drawable.maximize,
                R.drawable.minimize,
                R.drawable.milestone,
                R.drawable.fast_forward,
                R.drawable.diamond,
                R.drawable.play,
                R.drawable.triangle,
                R.drawable.pyramid,
                R.drawable.baseline,
                R.drawable.a_large_small,
                R.drawable.whole_word,
                R.drawable.languages,
                R.drawable.quote,
                R.drawable.chevrons_left_right,
                R.drawable.terminal,
            )
        }
    }

    @Immutable
    object TechIcons : IconsGroup<Int>(title = R.string.icons_tech) {
        override val icons by lazy {
            listOf(
                R.drawable.laptop,
                R.drawable.laptop_2,
                R.drawable.airplay,
                R.drawable.wallpaper,
                R.drawable.app_window,
                R.drawable.keyboard,
                R.drawable.mouse,
                R.drawable.code,
                R.drawable.code_2,
                R.drawable.binary,
                R.drawable.bug,
                R.drawable.cpu,
                R.drawable.calculator,
                R.drawable.archive,
                R.drawable.inbox,
                R.drawable.database,
                R.drawable.globe,
                R.drawable.play_circle,
                R.drawable.power,
                R.drawable.power_circle,
                R.drawable.printer,
                R.drawable.save,
                R.drawable.battery_charging,
                R.drawable.smartphone,
                R.drawable.tv,
                R.drawable.gamepad_2,
                R.drawable.ghost,
                R.drawable.bluetooth_connected,
                R.drawable.radio,
                R.drawable.nfc,
                R.drawable.rss,
                R.drawable.wifi,
                R.drawable.wifi_off,
            )
        }
    }

    @Immutable
    object TransportationIcons : IconsGroup<Int>(title = R.string.icons_transportation) {
        override val icons by lazy {
            listOf(
                R.drawable.car_front,
                R.drawable.car_taxi_front,
                R.drawable.bus_front,
                R.drawable.bus,
                R.drawable.car,
                R.drawable.truck,
                R.drawable.ship,
            )
        }
    }

    @Immutable
    object ArrowsIcons : IconsGroup<Int>(title = R.string.icons_arrows) {
        override val icons by lazy {
            listOf(
                R.drawable.arrow_left,
                R.drawable.arrow_up,
                R.drawable.arrow_right,
                R.drawable.arrow_down,

                R.drawable.arrow_big_left,
                R.drawable.arrow_big_up,
                R.drawable.arrow_big_right,
                R.drawable.arrow_big_down,
                R.drawable.arrow_right_left,
                R.drawable.arrow_up_1_0,
                R.drawable.arrow_up_down,
                R.drawable.arrow_up_narrow_wide,
                R.drawable.expand,
                R.drawable.maximize_2,
                R.drawable.minimize_2,
                R.drawable.move,
                R.drawable.recycle,
                R.drawable.refresh_cw,
                R.drawable.repeat,
                R.drawable.trending_up,
                R.drawable.trending_down,
                R.drawable.mouse_pointer_2,
                R.drawable.mouse_pointer_click,
                R.drawable.navigation,
                R.drawable.send,
            )
        }
    }

    @Immutable
    companion object {
        val unlocked: Set<Int> = setOf(
            R.drawable.sparkle,
            R.drawable.sparkles,
            R.drawable.star,
            R.drawable.cross,
            R.drawable.heart_pulse,

            R.drawable.sprint,
            R.drawable.bike,
            R.drawable.dumbbell,

            R.drawable.image,
            R.drawable.film,
            R.drawable.headphones,
            R.drawable.brush,

            R.drawable.circle_dollar_sign,

            R.drawable.laptop,

            R.drawable.messages_square,
            R.drawable.circle_user,
            R.drawable.user,

            R.drawable.calendar,
            R.drawable.line_chart,

            R.drawable.utensils,

            R.drawable.shrub,
            R.drawable.tree_deciduous,
            R.drawable.tree_pine,

            R.drawable.pen,

            R.drawable.bed_double,

            R.drawable.car_front,

            R.drawable.book_open_text,
            R.drawable.file,
            R.drawable.book,

            R.drawable.type,
            R.drawable.diff,
            R.drawable.equal,

            R.drawable.arrow_left,
            R.drawable.arrow_up,
            R.drawable.arrow_right,
            R.drawable.arrow_down,
        )

        val allGroups by lazy {
            listOf(
                MiscellaneousIcons,
                SportsIcons,
                ArtIcons,
                FinancesIcons,
                TechIcons,
                SocialIcons,
                OrganizingIcons,
                FoodIcons,
                NatureIcons,
                KnowledgeIcons,
                HouseholdIcons,
                TransportationIcons,
                NotesIcons,
                SymbolsIcons,
                ArrowsIcons,
            )
        }

        val Default = R.drawable.sparkle
    }
}

@Composable
@Preview
fun SelectIconScreenPreview(@PreviewParameter(IconProvider::class) group: IconsGroup<Int>) {
    HabitMateTheme {
        LazyVerticalGrid(columns = GridCells.Adaptive(32.dp)) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(stringResource(group.title), color = Color.White)
            }
            items(group.icons) { icon ->
                val isUnlocked = IconsGroup.unlocked.contains(icon)
                IconItem(
                    icon = IconResource.Id(icon),
                    color = IconsColors.Default,
                    isUnlocked = isUnlocked,
                    isSelected = isUnlocked
                )
            }
        }
    }
}