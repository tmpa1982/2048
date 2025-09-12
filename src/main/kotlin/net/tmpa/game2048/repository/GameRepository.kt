package net.tmpa.game2048.repository

import net.tmpa.game2048.model.Board2048
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class GameRepository {
    private val games = ConcurrentHashMap<String, Board2048>()

    fun add(id: String, board: Board2048) {
        games[id] = board
    }

    fun get(id: String): Board2048? {
        return games[id]
    }
}
