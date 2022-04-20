import java.io.File
import java.io.InputStreamReader
import kotlin.math.absoluteValue

enum class Facing {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    NONE
}

data class Coordinate(var x: Int, var y: Int, var facing: Facing = Facing.NONE)

fun main() {
    val testCase = loadCase()

    // l r d u
    val laserCord = Coordinate(
        testCase[0].toInt(), testCase[1].toInt(), when (testCase[2].toInt()) {
            1 -> Facing.LEFT
            2 -> Facing.RIGHT
            3 -> Facing.DOWN
            4 -> Facing.UP
            else -> Facing.NONE
        }
    )
    val destCord = Coordinate(testCase[3].toInt(), testCase[4].toInt())
    val mirrorLimit = testCase[5].toInt()

    val alignedFrom = laserCord.facing

    //Align Coordinates to laser facing down at start
    var laserXCord = laserCord.x
    var laserYCord = laserCord.y

    when (laserCord.facing) {
        Facing.RIGHT -> {
            laserXCord = (laserCord.y - 9).absoluteValue
            laserYCord = laserCord.x

        }
        Facing.LEFT -> {
            laserXCord = laserCord.y
            laserYCord = (laserCord.x - 9).absoluteValue
        }
        Facing.UP -> {
            laserXCord = laserCord.x
            laserYCord = (laserCord.y - 9).absoluteValue
        }
        else -> {}
    }

    laserCord.x = laserXCord
    laserCord.y = laserYCord
    laserCord.facing = Facing.DOWN

    val mirrorPosition: MutableList<Coordinate> = mutableListOf()

    //Calculate
    if (laserCord.x == destCord.x && laserCord.y < destCord.y) println("사용한 거울이 없습니다.")
    if (laserCord.y == 9) {
        println("불가능")
        return
    } else if (mirrorLimit > 0) {
        val firstMirrorFace: Facing = if (laserCord.x > destCord.x) Facing.LEFT
        else Facing.RIGHT

        if (laserCord.y < destCord.y) {
            mirrorPosition.add(Coordinate(laserCord.x, destCord.y, firstMirrorFace))
        } else if (mirrorLimit > 1) {
            if (laserCord.x == destCord.x) {
                if (mirrorLimit < 3) {
                    println("불가능")
                    return
                } else {
                    if (laserCord.x == 0) {
                        mirrorPosition.add(Coordinate(0, laserCord.y + 1, Facing.RIGHT))
                        mirrorPosition.add(Coordinate(laserCord.x + 1, laserCord.y + 1, Facing.UP))
                        mirrorPosition.add(Coordinate(laserCord.x + 1, destCord.y, Facing.LEFT))
                    } else {
                        mirrorPosition.add(Coordinate(laserCord.x, laserCord.y + 1, Facing.LEFT))
                        mirrorPosition.add(Coordinate(laserCord.x + 1, laserCord.y + 1, Facing.UP))
                        mirrorPosition.add(Coordinate(laserCord.x + 1, destCord.y, Facing.RIGHT))
                    }
                }
            } else {
                mirrorPosition.add(Coordinate(laserCord.x, laserCord.y + 1, firstMirrorFace))
                mirrorPosition.add(Coordinate(destCord.x, laserCord.y + 1, Facing.UP))
            }
        } else {
            println("불가능")
            return
        }

        //Rotate to its original Position
        if (alignedFrom == Facing.DOWN) {
            for (cord in mirrorPosition) {
                println(
                    "${cord.x} ${cord.y} " +
                            when (cord.facing) {
                                Facing.UP -> "위"
                                Facing.DOWN -> "아래"
                                Facing.LEFT -> "왼쪽"
                                Facing.RIGHT -> "오른쪽"
                                else -> "예기치 않은 오류"
                            }
                )
            }
        } else {
            for (cord in mirrorPosition) {
                var faceXCord = 0
                var faceYCord = 0
                var faceTo = Facing.NONE

                when (alignedFrom) {
                    Facing.RIGHT -> {
                        faceXCord = cord.y
                        faceYCord = (cord.x - 9).absoluteValue
                        faceTo = when (cord.facing) {
                            Facing.UP -> Facing.LEFT
                            Facing.LEFT -> Facing.DOWN
                            Facing.DOWN -> Facing.RIGHT
                            Facing.RIGHT -> Facing.UP
                            else -> Facing.NONE
                        }
                    }
                    Facing.LEFT -> {
                        faceXCord = (cord.y - 9).absoluteValue
                        faceYCord = cord.x
                        faceTo = when (cord.facing) {
                            Facing.UP -> Facing.RIGHT
                            Facing.LEFT -> Facing.UP
                            Facing.DOWN -> Facing.LEFT
                            Facing.RIGHT -> Facing.DOWN
                            else -> Facing.NONE
                        }
                    }
                    Facing.UP -> {
                        faceXCord = cord.y
                        faceYCord = (cord.x - 9).absoluteValue
                        faceTo = when (cord.facing) {
                            Facing.UP -> Facing.DOWN
                            Facing.DOWN -> Facing.UP
                            Facing.LEFT -> Facing.LEFT
                            Facing.RIGHT -> Facing.RIGHT
                            else -> Facing.NONE
                        }
                    }
                    else -> {}
                }

                println(
                    "$faceXCord $faceYCord ${
                        when (faceTo) {
                            Facing.UP -> "위"
                            Facing.DOWN -> "아래"
                            Facing.LEFT -> "왼쪽"
                            Facing.RIGHT -> "오른쪽"
                            else -> "예기치 않은 오류"
                        }
                    }"
                )
            }
        }
    }
}

fun loadCase(): List<String> {
    val case = File(ClassLoader.getSystemResource("testcase.txt").path)

    val isrCase: InputStreamReader = case.inputStream().reader()

    // initX, initY, laserFace, destX, destY, mirrorCount
    val problemParams = isrCase.readText().split(" ")

    isrCase.close()

    return problemParams
}