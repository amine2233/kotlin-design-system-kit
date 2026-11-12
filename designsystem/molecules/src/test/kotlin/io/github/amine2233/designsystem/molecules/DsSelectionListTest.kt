package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test

class DsSelectionListTest {
    @Test
    fun `single mode replaces the selection`() {
        val draft = DsSelectionDraft.from(setOf(1), DsSelectionListMode.Single)
        assertEquals(setOf(3), draft.choose(3).selected)
    }

    @Test
    fun `single mode keeps the selected index on a re-tap`() {
        val draft = DsSelectionDraft.from(setOf(2), DsSelectionListMode.Single)
        assertEquals(setOf(2), draft.choose(2).selected)
        assertEquals(setOf(2), dsChooseOption(setOf(2), 2, DsSelectionListMode.Single))
    }

    @Test
    fun `single mode creation keeps only the lowest index`() {
        assertEquals(setOf(1), DsSelectionDraft.from(setOf(3, 1), DsSelectionListMode.Single).selected)
    }

    @Test
    fun `single mode creation from nothing stays empty`() {
        assertEquals(emptySet<Int>(), DsSelectionDraft.from(emptySet(), DsSelectionListMode.Single).selected)
    }

    @Test
    fun `multiple mode toggles an index on then off`() {
        val draft = DsSelectionDraft.from(setOf(0), DsSelectionListMode.Multiple)
        val on = draft.choose(2)
        assertEquals(setOf(0, 2), on.selected)
        assertEquals(setOf(0), on.choose(2).selected)
    }

    @Test
    fun `multiple mode creation keeps the whole committed set`() {
        assertEquals(setOf(3, 1, 4), DsSelectionDraft.from(setOf(3, 1, 4), DsSelectionListMode.Multiple).selected)
    }

    @Test
    fun `choose leaves the draft and the committed set unchanged`() {
        val committed = setOf(0, 1)
        val draft = DsSelectionDraft.from(committed, DsSelectionListMode.Multiple)
        draft.choose(1)
        draft.choose(4)
        assertEquals(DsSelectionDraft(DsSelectionListMode.Multiple, setOf(0, 1)), draft)
        assertEquals(setOf(0, 1), committed)
    }

    @Test
    fun `the rule matches the filter group in both modes`() {
        val pairs =
            listOf(
                DsSelectionListMode.Single to DsFilterSelection.Single,
                DsSelectionListMode.Multiple to DsFilterSelection.Multiple,
            )
        val selections = listOf(emptySet(), setOf(0), setOf(1, 3), setOf(0, 1, 2, 3))
        for ((mode, filter) in pairs) {
            for (selected in selections) {
                for (index in 0..3) {
                    assertEquals(dsToggleFilter(selected, index, filter), dsChooseOption(selected, index, mode))
                }
            }
        }
    }
}
