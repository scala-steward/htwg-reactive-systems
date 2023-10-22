package myPackage
import scalaj.http.{Http, HttpOptions}

def get(url: String): Either[String,String] = {
    try{
        val result = Http(url)
            .header("X-RapidAPI-Key", "***REMOVED***")
            .header("X-RapidAPI-Host", "streaming-availability.p.rapidapi.com")
            .method("GET")
            .asString
    
        if (result.isError) {
            return Right(result.body)
        } else {
            return Left(result.body)
        }

    }
    catch {
        case e: Exception => return Right(e.toString())
    }
}

def getCountries_json(): Either[String,String] = {
    get("https://streaming-availability.p.rapidapi.com/countries")
}