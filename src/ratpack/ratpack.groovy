import static ratpack.groovy.Groovy.ratpack

ratpack {
    handlers {
        get("foo") {
            render "from the foo handler"
        }
        get("bar") {
            render "from the bar handler"
        }
    }
}
