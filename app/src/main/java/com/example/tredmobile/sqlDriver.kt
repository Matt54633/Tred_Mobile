// Notice, do not import com.mysql.cj.jdbc.*
// or you will have problems!
object LoadDriver {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance()
        } catch (ex: Exception) {
            // handle the error
        }
    }
}