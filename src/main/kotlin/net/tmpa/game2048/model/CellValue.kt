package net.tmpa.game2048.model

enum class CellValue {
    EMPTY,
    V2,
    V4,
    V8,
    V16,
    V32,
    V64,
    V128,
    V256,
    V512,
    V1024,
    V2048,
    ;

    fun getNext(): CellValue {
        return when (this) {
            EMPTY -> V2
            V2 -> V4
            V4 -> V8
            V8 -> V16
            V16 -> V32
            V32 -> V64
            V64 -> V128
            V128 -> V256
            V256 -> V512
            V512 -> V1024
            V1024 -> V2048
            V2048 -> throw IllegalStateException("No next value for V8192")
        }
    }

    fun getNumericValue(): Int {
        return when (this) {
            EMPTY -> 0
            V2 -> 2
            V4 -> 4
            V8 -> 8
            V16 -> 16
            V32 -> 32
            V64 -> 64
            V128 -> 128
            V256 -> 256
            V512 -> 512
            V1024 -> 1024
            V2048 -> 2048
        }
    }
}
