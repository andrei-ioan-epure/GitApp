import java.util.*

data class Commit(val author:String,
             val time: Date,
             val message:String,val tree: Tree): GitObject(author, time,message)
