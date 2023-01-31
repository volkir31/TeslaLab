package car.valueObject

data class Fuel(var percent: Double) {
    fun charge(): Unit = run { percent = 100.0 }
}