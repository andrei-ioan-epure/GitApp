import java.security.MessageDigest

open class GitObject(vararg  data:Any) {
    protected val sha1: String by lazy {
        MessageDigest.getInstance("SHA-1").digest(data.joinToString("|").toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    fun getID(): String = sha1

}