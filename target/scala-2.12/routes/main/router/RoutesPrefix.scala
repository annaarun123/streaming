// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/aruan01/Documents/Code/streaming/conf/routes
// @DATE:Sat Sep 22 23:25:26 PDT 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
