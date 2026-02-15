# Todo App

Yksinkertainen tehtävälista Android-sovellus.

- Näyttää listan tehtäviä.
- Voit lisätä uusia tehtäviä. (addTask(list: List<Task>, task: Task): List<Task>)
- Voit merkitä tehtävät tehdyiksi ja järjestellä niitä. (toggleDone(list: List<Task>, id: Int): List<Task>, sortByDueDate(list: List<Task>): List<Task>)
- Käyttää Jetpack Composea käyttöliittymässä.

Compose hallitsee UI-statea reaktiivisesti. Kun tila muuttuu, UI päivittyy automaattisesti. Remember säilyttää tilan vain Composable-funktion elinkaaren ajan, kun taas ViewModel säilyttää datan ja erottaa UI-logiikan sovelluslogiikasta.

MVVM on arkkitehtuuri joka erottaa käyttöliittymän sovelluslogiikasta. Se on hyödyllinen Composessa koska se helpottaa tilan hallintaa, mahdollistaa testattavuuden ja pitää koodin selkeänä ja erillisinä layereinä. StateFlow on tietovirta joka säilyttää viimeisimmän tilan arvot. Kun ViewModel päivittää StateFlow:n arvoa, Compose UI kerää tämän muutoksen collectAsState()-funktiolla ja käynnistää automaattisesti uudelleenpiirron. Tämä pitää käyttöliittymän aina synkronoituna datan kanssa ilman manuaalisia päivityksiä.

Jetpack Composen navigointi mahdollistaa näkymien vaihtamisen NavControllerin avulla. NavHost määrittelee sovelluksen reitit ja sen, mitä näkymää milläkin reitillä näytetään.

Sovelluksessa on kaksi näkymää: Home ja Calendar, joiden välillä käyttäjä oi liikkua painikkeilla.

Sovellus käyttää MVVM-arkkitehtuuria. Yksi ViewModel jaetaan HomeScreenin ja CalendarScreenin välillä, jolloin molemmat näkymät käyttävät samaa tilaa.

CalendarScreen näyttää tehtävät kalenterimaisessa muodossa ryhmittelemällä ne määräpäivän mukaan.

Tehtävien lisääminen ja muokkaaminen tapahtuu AlertDialogin avulla ilman erillisiä navigointinäkymiä.

**Sääominaisuus (OpenWeatherMap API)**

Retrofit on Androidin HTTP-kirjasto, joka muuntaa rajapinnan Kotlin-interfacen muotoon ja hoitaa API-kutsut automaattisesti.

Gson muuntaa API:n palauttaman JSONin suoraan `WeatherResponse`-data classiksi.  
`@SerializedName` yhdistää JSON-kentät Kotlin-muuttujiin.

**Coroutinet**:
API-kutsu tehdään `suspend`-funktiolla `viewModelScope.launch`-lohkossa.  
Kutsu suoritetaan taustalla, joten UI pysyy responsiivisena.  
Datan saapuessa tila päivittyy ja Compose renderöi näkymän uudelleen.

ViewModel sisältää `WeatherUiState`-tilan:

- `isLoading` – lataus-state
- `weather` – säädata
- `error` – virheviesti

Compose seuraa tilaa `collectAsState()`-funktiolla ja päivittyy automaattisesti.

### API-avaimen tallennus

1. Lisää avain `local.properties`-tiedostoon muodossa `WEATHER_API_KEY=oma_api_avain`
2. Gradle lukee sen `project.findProperty()`
3. Avain on käytettävissä `BuildConfig.WEATHER_API_KEY`
4. ViewModel välittää sen Retrofit-kutsuun
