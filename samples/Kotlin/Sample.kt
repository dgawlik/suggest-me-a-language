/*
   == Suggest me a language, 2023 ==
*/



import kotlin.random.Random

open class Thing(val id: Int)

class Rock() : Thing(1)
class Paper() : Thing(2)
class Scissors() : Thing(3)

val CHOICES = arrayOf(Rock(), Paper(), Scissors())

class Player(val name: String) {

    fun bet(): Thing {
        val ind = Random.nextInt(0, CHOICES.size)
        return CHOICES[ind]
    }
}



fun main() {
    val p1 = Player("Alice")
    val p2 = Player("Bob")

    for (i in 0..20) {
        val p1Bet = p1.bet()
        val p2Bet = p1.bet()

        val result = when (p1Bet.id to p2Bet.id) {
            Rock().id to Rock().id -> "tie"
            Paper().id to Paper().id -> "tie"
            Scissors().id to Scissors().id -> "tie"
            Rock().id to Paper().id -> "${p2.name} wins"
            Paper().id to Rock().id -> "${p1.name} wins"
            Rock().id to Scissors().id -> "${p2.name} wins"
            Scissors().id to Rock().id -> "${p1.name} wins"
            Scissors().id to Paper().id -> "${p1.name} wins"
            Paper().id to Scissors().id -> "${p2.name} wins"
            else -> throw RuntimeException("Unknown combination")
        }

        println("Game no $i => $result")
    }
}

// Installing => sudo snap install --classic kotlin

// Running => kotlinc Sample.kt -include-runtime -d sample.jar && java -jar Sample.jar