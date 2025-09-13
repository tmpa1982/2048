package net.tmpa.game2048.repository

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Cache
import net.tmpa.game2048.model.Board2048
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class GameRepository {
    private val games: Cache<String, Board2048> = Caffeine.newBuilder()
        .expireAfterAccess(15, TimeUnit.MINUTES)
        .build()

    fun add(id: String, board: Board2048) {
        games.put(id, board)
    }

    fun get(id: String): Board2048? {
        return games.getIfPresent(id)
    }
}
