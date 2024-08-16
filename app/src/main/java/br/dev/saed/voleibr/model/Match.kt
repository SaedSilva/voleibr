package br.dev.saed.voleibr.model

class Match(
    var time1: Team = Team("Time 1"),
    var time2: Team = Team("Time 2"),
    var pontosMax: Int = 12,
    var fila: ArrayDeque<Team> = ArrayDeque()
) {
    fun pontos() {
        println("${time1.nome} ${time1.pontos} x ${time2.pontos} ${time2.nome}")
        println("Ganha com: $pontosMax pontos")
    }

    fun pontuarTime1() {
        time1.pontos++
        testarGanhador()
    }

    fun pontuarTime2() {
        time2.pontos++
        testarGanhador()
    }

    fun adicionarTime(team: Team) {
        fila.add(team)
    }

    fun removerTime(index: Int) {
        fila.removeAt(index)
    }

    fun testarGanhador(): Team? {
        if (time1.pontos >= pontosMax) {
            if (fila.isEmpty()) {
                time2.pontos = 0
            } else {
                time2 = fila.removeFirst()
            }
            time1.pontos = 0
            return time1
        } else if (time2.pontos >= pontosMax) {
            if (fila.isEmpty()) {
                time1.pontos = 0
            } else {
                time1 = fila.removeFirst()
            }
            time2.pontos = 0
            return time2
        }
        return null
    }

}