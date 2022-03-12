package texasholdem.internal

import texasholdem.internal.domain.Action.Check
import texasholdem.internal.domain.{Action, Player, PlayerId, PlayerView}

class ComputerPlayer(playerId: PlayerId) extends Player {

  override def id: PlayerId = {
    playerId
  }

  override def getAction(playerView: PlayerView): Action = {
    Check
  }
}
