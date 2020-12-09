# Progetto TRM ( Test a Risposta Multipla)

Nome Azienda: Pending

# Contributori:
Leonardo Montagner, Daniele Pontillo, Fabio Foltran, Mattia Pravato

# Panoramica:
Questo progetto fu chiesto dal Prof. Voltolina come esercizio di lungo termine.

Il progetto in se serve a creare e gestire Test a Risposta Multipla utilizzando le nostre conoscenze sui database e il linguaggio SQL.

Lo rendiamo possibile collegando un programma Java grazie al connettore Java di MySQL.

-class Utente-
userName
privilegio  (admin, prof, stud)
classe (num)
sezione (lettera)
password -random

-Admin puo-
gestire utenti (inserire/cancellare/modificare)
gestire le classi di studenti

-prof puo-
gestire le domande  (inserire/cancellare/modificare)
comporre e somministrare un Test (insieme di domande)
verificare i risultati

-stud puo-
eseguire un test
consultare i risultati

-class Domanda-
Questito della domanda
[] risposte possibili a scelta multipla (1 o più)
Immagine inerente alla domanda ???????
punteggio (1/2/3 punti)
difficolta (facile/media/difficile/molto *)
materia  -|
classe   --Indici per ricerca
argomento-|
#le risposte devono essere mescolate, quindi attento all'ordine!!
#simboli utf-8

-class Test-
[] domande scelte a mano
classe a cui somministrarlo
tempo concesso (in minuti)
attivo (è somministrabile agi studenti)
Options
punteggio domande (statico/variabile)
penalità errore
percentuale suff
voto massimo
progressione della difficoltà delle domande (lineare/inversa/random)
#una volta creato deve essere attivo perche gli studenti lo possano vedere e eseguire


Schema databases

utenti
ID!, username, password, hasLoggedOnce
