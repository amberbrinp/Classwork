import collection.mutable.HashMap
object Main extends App
{
    def stringListLength(stringList: List[String]) : Int = { stringList.foldLeft(0) (_+_.length) }

    def readFile(filename:String) : HashMap[String, String] =
    {
        val swears_and_alternatives = new HashMap[String, String]()

        io.Source.fromFile(filename).getLines().foreach
        {
            (line) => val swear_and_alternative = line.split(", ")
            swears_and_alternatives += swear_and_alternative(0) -> swear_and_alternative(1)
        }

        swears_and_alternatives
    }

    def replaceWords(sentence: String, swears_and_alternatives: HashMap[String, String]) : String =
    {
        swears_and_alternatives.foldLeft(sentence)((line, replacements) => line.replaceAll(replacements._1, replacements._2))
    }

    println(stringListLength(List("this", "is", "a", "demonstration"))) //should be 20
    println(replaceWords("Oh shoot, this homework is due soon. Darn!", readFile("censor.txt")))
}
