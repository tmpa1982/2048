package net.tmpa.game2048.controller

import net.tmpa.game2048.dto.BoardDto
import net.tmpa.game2048.dto.CreateGameRequest
import net.tmpa.game2048.dto.CreateGameResponse
import net.tmpa.game2048.dto.MoveRequest
import net.tmpa.game2048.dto.MoveResponse
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.repository.GameRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/api/game")
class GameController(private val repository: GameRepository) {
    @PostMapping
    fun createGame(@RequestBody createGameRequest: CreateGameRequest): CreateGameResponse {
        val id = UUID.randomUUID().toString()
        val board = Board2048.initializeRandomBoard(withObstacle = createGameRequest.withObstacle)
        repository.add(id, board)
        return CreateGameResponse(id, BoardDto(board.asList()))
    }

    @PostMapping("/{id}/move")
    fun move(@PathVariable id: String, @RequestBody moveRequest: MoveRequest): MoveResponse {
        val board = repository.get(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found")
        val nextBoard = board.nextBoardAndCell(moveRequest.direction)
        repository.add(id, nextBoard)
        return MoveResponse(
            nextBoard.isWinning(),
            nextBoard.isLosing(),
            BoardDto(nextBoard.asList()),
            nextBoard.score,
        )
    }
}
