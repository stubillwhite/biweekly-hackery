package markvshaney.internal.model

import markvshaney.internal.model.DefaultMarkovModel.*
import markvshaney.internal.model.RandomNumberProvider
import markvshaney.internal.domain.*

import scala.annotation.tailrec
import scala.util.Random

object DefaultMarkovModel extends MarkovModelBuilder {

  case class Transition[A](toState: StateIdentifier, event: A, weight: Int)

  def buildModel[A](events: Iterator[A],
                    stateIdentifierGenerator: Seq[A] => StateIdentifier,
                    contextLength: Int): MarkovModel[A] = {
    val defaultRandomNumberProvider = new RandomNumberProvider {
      override def nextInt(n: Int): Int = {
        Random.nextInt(n)
      }
    }

    buildModel(defaultRandomNumberProvider, events, stateIdentifierGenerator, contextLength)
  }

  private[model] def buildModel[A](randomNumberProvider: RandomNumberProvider,
                    events: Iterator[A],
                    stateIdentifierGenerator: Seq[A] => StateIdentifier,
                    contextLength: Int): MarkovModel[A] = {
    val model: DefaultMarkovModel[A] = new DefaultMarkovModel[A](randomNumberProvider, Map())

    events
      .sliding(contextLength + 1, 1)
      .foldLeft(model) { case (model, events) => addTransition(model, events, stateIdentifierGenerator) }
  }

  private[model] def apply[A](randomNumberProvider: RandomNumberProvider): DefaultMarkovModel[A] = {
    DefaultMarkovModel(randomNumberProvider, Map())
  }

  private def addTransition[A](model: DefaultMarkovModel[A],
                               events: Seq[A],
                               toStateIdentifier: Seq[A] => StateIdentifier): DefaultMarkovModel[A] = {

    val fromState = toStateIdentifier(events.take(events.length - 1))
    val toState = toStateIdentifier(events.drop(1))
    val event = events.last

    val fromStateTransitions = model.states.getOrElse(fromState, Map())
    val transitionData = fromStateTransitions.getOrElse(toState, Transition(toState, event, 0))

    if (transitionData.event != event) {
      val message = s"Cannot add transition from '${fromState}' to '${toState}' with event '${event}'; transition already exists with event '${transitionData.event}'"
      throw new RuntimeException(message)
    }
    else {
      val newFromStateTransitions = fromStateTransitions + (toState -> transitionData.copy(weight = transitionData.weight + 1))
      model.copy(states = model.states + (fromState -> newFromStateTransitions))
    }
  }
}

case class DefaultMarkovModel[A](randomNumberProvider: RandomNumberProvider,
                                 states: Map[StateIdentifier, Map[StateIdentifier, Transition[A]]]) extends MarkovModel[A] {

  def getStates(): Set[StateIdentifier] = {
    states.keySet ++ states.values.flatMap(_.keySet)
  }

  def generateEvents(initialState: StateIdentifier): Iterator[A] = {

    def nextTransition(prevTransition: Option[Transition[A]]): Option[Transition[A]] = {
      prevTransition.flatMap(t => randomTransition(t.toState))
    }

    val initialTransition = randomTransition(initialState)

    Iterator.iterate(initialTransition)(nextTransition)
      .takeWhile(_.isDefined)
      .map(_.get.event)
  }

  private def randomTransition(fromState: StateIdentifier): Option[Transition[A]] = {
    states
      .get(fromState)
      .map(transitions => randomWeightedTransitionFrom(transitions.values.toSeq))
  }

  private def randomWeightedTransitionFrom(transitions: Seq[Transition[A]]): Transition[A] = {
    val rnd = randomNumberProvider.nextInt(transitions.map(_.weight).sum + 1)

    @tailrec
    def iter(runningWeight: Int, remainingTransitions: Seq[Transition[A]]): Transition[A] = {
      val transition = remainingTransitions.head

      if (rnd <= runningWeight + transition.weight) {
        transition
      } else {
        iter(runningWeight + transition.weight, remainingTransitions.tail)
      }
    }

    iter(0, transitions)
  }
}