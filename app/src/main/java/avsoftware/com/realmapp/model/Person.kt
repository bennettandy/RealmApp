package avsoftware.com.realmapp.model

data class Person( val id: Long, val name: String, val age: Int, val dog: Dog?, val cats: List<Cat>, val tempRef : Int)
