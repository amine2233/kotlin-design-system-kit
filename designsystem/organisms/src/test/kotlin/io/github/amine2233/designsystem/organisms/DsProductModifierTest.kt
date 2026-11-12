package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DsProductModifierTest {
    private val size =
        DsModifierGroup(
            id = "size",
            title = "Size",
            options =
                listOf(
                    DsModifierOption("s", "Small"),
                    DsModifierOption("m", "Medium", 5_000L),
                    DsModifierOption("l", "Large", 10_000L),
                ),
        )

    private val extras =
        DsModifierGroup(
            id = "extras",
            title = "Extras",
            options =
                listOf(
                    DsModifierOption("cheese", "Cheese", 2_500L),
                    DsModifierOption("bacon", "Bacon", 4_000L),
                    DsModifierOption("egg", "Egg", 1_500L),
                ),
            type = DsModifierGroupType.Multiple,
            minSelections = 0,
            maxSelections = 2,
        )

    private val sides =
        DsModifierGroup(
            id = "sides",
            title = "Sides",
            options = listOf(DsModifierOption("fries", "Fries"), DsModifierOption("salad", "Salad"), DsModifierOption("rice", "Rice")),
            type = DsModifierGroupType.Multiple,
            minSelections = 2,
            maxSelections = 3,
        )

    private val sauce =
        DsModifierGroup(
            id = "sauce",
            title = "Sauce",
            options = listOf(DsModifierOption("none", "None"), DsModifierOption("bbq", "BBQ", 1_000L)),
            minSelections = 0,
        )

    @Test
    fun `total with nothing selected is the base price`() {
        assertEquals(25_000L, dsModifierTotal(25_000L, listOf(size, extras), emptySet(), 1))
    }

    @Test
    fun `total adds the selected deltas then multiplies by the quantity`() {
        assertEquals(65_000L, dsModifierTotal(25_000L, listOf(size, extras), setOf("m", "cheese"), 2))
    }

    @Test
    fun `total ignores ids that match no option`() {
        assertEquals(30_000L, dsModifierTotal(25_000L, listOf(size), setOf("m", "ghost"), 1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `total rejects a negative base`() {
        dsModifierTotal(-1L, listOf(size), emptySet(), 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a group with a negative delta cannot reach the total`() {
        dsModifierTotal(
            25_000L,
            listOf(DsModifierGroup("promo", "Promo", listOf(DsModifierOption("discount", "Discount", -100L)), minSelections = 0)),
            setOf("discount"),
            1,
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `total rejects quantity 0`() {
        dsModifierTotal(25_000L, listOf(size), emptySet(), 0)
    }

    @Test(expected = ArithmeticException::class)
    fun `total throws on overflow of the unit price`() {
        val huge = DsModifierGroup("huge", "Huge", listOf(DsModifierOption("x", "X", Long.MAX_VALUE)), minSelections = 0)
        dsModifierTotal(1L, listOf(huge), setOf("x"), 1)
    }

    @Test(expected = ArithmeticException::class)
    fun `total throws on overflow of the quantity product`() {
        dsModifierTotal(Long.MAX_VALUE / 2, listOf(size), emptySet(), 3)
    }

    @Test
    fun `a required single group with nothing selected is missing`() {
        assertEquals(DsModifierGroupError.MissingRequired, dsModifierGroupError(size, emptySet()))
    }

    @Test
    fun `a multiple group under its minimum is below minimum`() {
        assertEquals(DsModifierGroupError.BelowMinimum, dsModifierGroupError(sides, setOf("fries")))
    }

    @Test
    fun `more than the max is above maximum`() {
        assertEquals(DsModifierGroupError.AboveMaximum, dsModifierGroupError(extras, setOf("cheese", "bacon", "egg")))
        assertEquals(DsModifierGroupError.AboveMaximum, dsModifierGroupError(size, setOf("s", "m")))
    }

    @Test
    fun `a valid selection has no error`() {
        assertNull(dsModifierGroupError(size, setOf("m")))
        assertNull(dsModifierGroupError(extras, setOf("cheese", "bacon")))
        assertNull(dsModifierGroupError(sides, setOf("fries", "rice")))
    }

    @Test
    fun `an optional single group left empty has no error`() {
        assertNull(dsModifierGroupError(sauce, emptySet()))
    }

    @Test
    fun `ids from other groups are not counted`() {
        assertEquals(DsModifierGroupError.MissingRequired, dsModifierGroupError(size, setOf("cheese", "bbq")))
        assertNull(dsModifierGroupError(extras, setOf("cheese", "s", "m", "l")))
    }

    @Test
    fun `selection is valid when every group is valid`() {
        assertTrue(dsIsModifierSelectionValid(listOf(size, extras, sauce), setOf("l", "egg")))
    }

    @Test
    fun `selection is invalid when any group is invalid`() {
        assertFalse(dsIsModifierSelectionValid(listOf(size, extras, sauce), setOf("egg")))
        assertFalse(dsIsModifierSelectionValid(listOf(size, sides), setOf("m", "fries")))
    }

    @Test
    fun `single replaces the previous selection`() {
        assertEquals(setOf("l"), dsToggleModifierOption(size, "l", setOf("m")))
    }

    @Test
    fun `a required single group ignores a tap on its selected option`() {
        assertEquals(setOf("m"), dsToggleModifierOption(size, "m", setOf("m")))
    }

    @Test
    fun `an optional single group clears on a tap on its selected option`() {
        assertEquals(emptySet<String>(), dsToggleModifierOption(sauce, "bbq", setOf("bbq")))
    }

    @Test
    fun `multiple adds and removes`() {
        assertEquals(setOf("cheese", "egg"), dsToggleModifierOption(extras, "egg", setOf("cheese")))
        assertEquals(setOf("egg"), dsToggleModifierOption(extras, "cheese", setOf("cheese", "egg")))
    }

    @Test
    fun `multiple ignores an add at max`() {
        assertEquals(setOf("cheese", "bacon"), dsToggleModifierOption(extras, "egg", setOf("cheese", "bacon")))
    }

    @Test
    fun `toggle keeps the ids of other groups`() {
        assertEquals(setOf("cheese", "bbq", "l"), dsToggleModifierOption(size, "l", setOf("cheese", "bbq", "m")))
        assertEquals(setOf("m", "cheese"), dsToggleModifierOption(extras, "cheese", setOf("m")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a group with min above max throws`() {
        DsModifierGroup("g", "G", extras.options, DsModifierGroupType.Multiple, minSelections = 3, maxSelections = 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a single group with max 2 throws`() {
        DsModifierGroup("g", "G", size.options, DsModifierGroupType.Single, minSelections = 1, maxSelections = 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a single group with min 2 throws`() {
        DsModifierGroup("g", "G", size.options, DsModifierGroupType.Single, minSelections = 2, maxSelections = 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a group with a negative option delta throws`() {
        DsModifierGroup("g", "G", listOf(DsModifierOption("x", "X", -1L)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a negative min throws`() {
        DsModifierGroup("g", "G", extras.options, DsModifierGroupType.Multiple, minSelections = -1, maxSelections = 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a max below 1 throws`() {
        DsModifierGroup("g", "G", extras.options, DsModifierGroupType.Multiple, minSelections = 0, maxSelections = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a min above the option count throws`() {
        DsModifierGroup("g", "G", listOf(DsModifierOption("x", "X")), DsModifierGroupType.Multiple, minSelections = 2, maxSelections = 3)
    }
}
