---

La Classe Utente è una classe generale che viene descritta da diversi attributi.

Classe Utente:

User name (L'userName viene descritto da un unico valore Nome.Cognome)

Privilegio  (Questo descrive il potere che ha l'Utente. Ci sono tre livelli: Admin, Professore, Studente)

Classe (Questo è l'anno in cui si trovano gli utenti. Esso è un numero)

Sezione (Questa è la sezione in cui gli utenti si trovano. Essa è una lettera)

Password (Questa è la password che serve agli utenti per accedere ai loro rispettivi account)

---

Prossimamente potete vedere che cosa possono fare i diversi privilegi

L' Admin può

Gestire utenti (Inserire / Cancellare / Modificare);
Gestire le classi di studenti.

Il Professore può

Gestire le Domande  (Inserire / Cancellare / Modificare);
Comporre e somministrare un Test (Insieme di domande);
Verificare i risultati.

Lo Studente può

Eseguire un test
Consultare i risultati

---

La Classe Domanda viene descritto da:

Questito della domanda (Questo è il testo della domanda stesso. Ovviamente serve per contenerlo all'interno di una variabile)

Risposte possibili a scelta multipla (Abbiamo qui quante risposte possono esserci su una domanda. Da 2 in sù. Utilizziamo un array, un elenco, per tenere traccia delle risposte all'interno di ogni domanda)

Immagine inerente alla domanda (Se la domanda presenta l'utilizzo di un'immagine, questo serve per dare l'immagine un posto nel programma e nella sua parte grafica)

Punteggio (Segna quanti punti si possono assegnare ad ogni domanda. Per esempio: 1/2/3 punti)

Difficoltà (Semplicemente, la difficoltà che viene assegnata alla domanda. Varia tra: Facile / Media / Difficile / Molto Difficile)

Materia (La materia di cui fa parte la domanda)

Classe   --Indici per ricerca

Argomento (L'argomento della domanda)

---

La Classe Test viene descritta da:

Domande scelte a mano (Le domande che vengono scelte dal Professore quando vuole creare un test)

Classe a cui somministrarlo (Questa si spiega da sola)

Tempo concesso (Quanto tempo hanno gli Studenti per finire il Test. Viene misurato in minuti)

Attivo (Descrive se il test è visibile e somministrabile agi studenti)

Options

Punteggio domande (Questo indica se il pinteggio che si da ad ogni domanda sarà Statico o Variabile. Statico per un punteggio uguale su ogni domanda, Variabile se si decide di dare punti diversi per ogni domanda)

Penalità errore (Quanti punti perdono gli Studenti per aver fatto una risposta sbagliata. ((N.B. Leonardo, fai si che mettere 0 è possibile)) )

Percentuale Sufficenza (Misura quanti punti bisogna avere per avere la sufficenza)

Voto massimo (Anche questa è molto semplice da capire)

Progressione della difficoltà delle domande (Indica se la difficoltà delle domande sarà: Lineare quindi costante e sempre in salita / Inversa, quindi dalla più difficile alla più semplice / Random, cioè casuale)

---

Classe: Main

La classe Main serve a contenere e ad avviare il programma in se. Si interfaccia con l'utente e processa i comandi dati dallo stesso.

Al suo interno sono presenti diversi metodi che si possono utilizzare. I metodi sono i seguenti:

* Login, il metodo che viene utilizzato per identificare l'utente che entra all'interno del programma.
* Parse, il metodo che viene utilizzato per analizzare gli input e suddividerli in comandi da parte dell'utente.
* Execute, il metodo che viene utilizzato per eseguire, con specifiche routine, i comandi presi in input.

---

Classe: Utente

La classe Utente serve a gestire gli utenti all'interno del database. Contiene utili metodi che servono per la gestione delle password e degli utenti.

I metodi che sono al suo interno sono i seguenti:

* getPrompt, il metodo che viene utilizzato per ricevere il prompt corretto per l'utente.
* genPassword, il metodo che viene utilizzato per generare password di 8 caratteri con 3 numeri e 5 lettere. Viene utilizzata come password provvisoria prima che gli utenti ne      creino una propria.
* validaPassword, il metodo che viene utilizzato per verificare che la password data che l'utente ha inserito sia corretta e che contenga simboli, maiuscole e minuscole.
* cambiaPassword, il metodo che viene utilizzato quando l'utente entra per la prima volta con le credenziali ricevute dall'Admin. Questo fa cambiare agli utenti la password in una password personale.
* aggiungiUtente, il metodo che viene utilizzato quando si vuole aggiungere un nuovo utente. Solo l'admin è capace di utilizzare questo metodo.
* trovaUtente, il metodo che viene utilizzato quandi si vuole trovare un utente specifico all'interno del database utilizzando l'username. Questo metodo viene utilizzato come attrezzo per i metodi di modifica e cancellazione di un'utente.
* rimuoviUtente, il metodo che viene utilizzato quando si vuole rimuovere un utente dal database. Solo l'admin è capace di utilizzare questo metodo.
* modificaUtente, il metodo che viene utilizzato quando si vuole modificare un utente nel database. Solo l'admin è capace di utilizzare questo metodo.

---
