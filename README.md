# Todo App

Yksinkertainen tehtävälista Android-sovellus.

- Näyttää listan tehtäviä.
- Voit lisätä uusia tehtäviä. (addTask(list: List<Task>, task: Task): List<Task>)
- Voit merkitä tehtävät tehdyiksi ja järjestellä niitä. (toggleDone(list: List<Task>, id: Int): List<Task>, sortByDueDate(list: List<Task>): List<Task>)
- Käyttää Jetpack Composea käyttöliittymässä.

Compose hallitsee UI-statea reaktiivisesti. Kun tila muuttuu, UI päivittyy automaattisesti. Remember säilyttää tilan vain Composable-funktion elinkaaren ajan, kun taas ViewModel säilyttää datan ja erottaa UI-logiikan sovelluslogiikasta.
