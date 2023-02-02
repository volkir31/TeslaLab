package car.valueObject

class Fuel() {
    var percent = 100.0

    override fun toString(): String {
        return "${percent.toInt()}%"
    }

    fun charge(value: Double): Unit = run {
        if (percent + value > 100.0) {
            percent = 100.0
            return@run
        }

        percent += value
    }

    fun spend(): Unit = run {
        if (percent - .005 <= 0) {
            percent = 0.0
            return@run
        }

        percent -= .005
    }

    fun isNeedCharge(): Boolean = percent <= 9.0
}