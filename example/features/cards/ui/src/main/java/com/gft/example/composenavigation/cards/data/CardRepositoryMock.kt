package com.gft.example.composenavigation.cards.data

import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

object CardRepositoryMock {
    private val cards = mutableMapOf(
        "#1" to Card("#1", false),
        "#2" to Card("#2", false)
    )

    val cardsList = MutableStateFlow(cards.values.toList())

    fun freezeCard(id: String) = changeCardFrozenStatus(id, true)

    fun unfreezeCard(id: String) = changeCardFrozenStatus(id, false)

    fun streamCardDetails(id: String): Flow<Card> = cardsList.flatMapConcat { it.asFlow() }.filter { card -> card.id == id }

    fun getCardDetails(id: String): Card {
        if (!cards.containsKey(id)) throw IllegalArgumentException("Card with id = $id not found!")
        return cards[id]!!
    }

    private fun changeCardFrozenStatus(id: String, frozen: Boolean) {
        val card = getCardDetails(id)
        cards[id] = card.copy(isFrozen = frozen)
        cardsList.value = cards.values.toList()
    }
}

data class Card(
    val id: String,
    val isFrozen: Boolean
)