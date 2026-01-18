package it.sdc.restserver;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Component
public class Jokes {

    private Random random;

    public Joke getRandomJoke() {
        if (jokes.isEmpty()) {
            return null;
        }
        int index = random.nextInt(jokes.size());
        return jokes.get(index);
    }

    @Data
    public static class Joke {
        private String question;
        private String answer;

        public Joke(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        @Override
        public String toString() {
            return question + " " + answer;
        }
    }

    private List<Joke> jokes;

    public Jokes() {
        jokes = new ArrayList<>();
        random = new Random();
        loadJokes();
    }

    private void loadJokes() {
        jokes.add(new Joke("Cosa dice un barbiere annoiato?", "Che barba!"));
        jokes.add(new Joke("Qual è il colmo per un idraulico?", "Non capirci un tubo."));
        jokes.add(new Joke("Cosa fa un gallo in mare?", "Galleggia."));
        jokes.add(new Joke("Qual è il colmo per un matematico?", "Abitare in una frazione."));
        jokes.add(new Joke("Qual è la città preferita dai ragni?", "Mosca."));
        jokes.add(new Joke("Cosa beve un elettricista?", "Birra alla spina."));
        jokes.add(new Joke("Qual è il colmo per un fantasma?", "Avere i bollenti spiriti."));
        jokes.add(new Joke("Cosa fa un cammello nel budino?", "Attraversa il dessert."));
        jokes.add(new Joke("Qual è il colmo per una disoccupata?", "Chiamarsi Assunta."));
        jokes.add(new Joke("Cosa dice un muro all'altro?", "Ci vediamo all'angolo."));
        jokes.add(new Joke("Qual è il colmo per un pizzaiolo?", "Avere una moglie di nome Margherita."));
        jokes.add(new Joke("Qual è il colmo per un elettricista?", "Essere isolato."));
        jokes.add(new Joke("Cosa fa un televisore in mare?", "Va in onda."));
        jokes.add(new Joke("Qual è il colmo per un tuffatore?", "Toccare il fondo."));
        jokes.add(new Joke("Qual è il colmo per un'ape?", "Andare a Mosca in Vespa."));
        jokes.add(new Joke("Cosa fa un gatto all'edicola?", "Aspetta che esca Topolino."));
        jokes.add(new Joke("Qual è il colmo per un sarto?", "Perdere il filo."));
        jokes.add(new Joke("Cosa dice un semaforo?", "Non guardarmi, mi cambio."));
        jokes.add(new Joke("Qual è il colmo per un musicista?", "Non avere la chiave."));
        jokes.add(new Joke("Cosa fa un asino sui binari?", "Deraglia."));
        jokes.add(new Joke("Qual è il colmo per una giraffa?", "Essere nei guai fino al collo."));
        jokes.add(new Joke("Qual è il colmo per un muratore?", "Restare di stucco."));
        jokes.add(new Joke("Cosa fa un prete in moto?", "La messa in moto."));
        jokes.add(new Joke("Qual è il colmo per un pesce?", "Avere l'acqua alla gola."));
        jokes.add(new Joke("Cosa fa una mucca con una siringa?", "Il vaccino."));
        jokes.add(new Joke("Qual è il colmo per un postino?", "Perdere la corrispondenza."));
        jokes.add(new Joke("Cosa dice un'acciuga a un'altra?", "Ciao, Alice."));
        jokes.add(new Joke("Qual è il colmo per un orologiaio?", "Avere le ore contate."));
        jokes.add(new Joke("Cosa fa un soldato in un cantiere?", "Il cemento armato."));
        jokes.add(new Joke("Qual è il colmo per un giardiniere?", "Piantare tutto e andarsene."));
        jokes.add(new Joke("Cosa fa un contadino stanco?", "Si pianta."));
        jokes.add(new Joke("Qual è il colmo per un cuoco?", "Farne di cotte e di crude."));
        jokes.add(new Joke("Cosa dice lo zero all'otto?", "Bella cintura!"));
        jokes.add(new Joke("Qual è il colmo per un gorilla?", "Entrare nel Guinness dei primati."));
        jokes.add(new Joke("Qual è il santo dei cani?", "San Bernardo."));
        jokes.add(new Joke("Qual è il colmo per due scheletri?", "Essere amici per la pelle."));
        jokes.add(new Joke("Cosa fa un gatto in montagna?", "Il gatto delle nevi."));
        jokes.add(new Joke("Come si chiama il postino più bravo?", "Franco Bollo."));
        jokes.add(new Joke("Qual è il colmo per un pilota?", "Avere la testa tra le nuvole."));
        jokes.add(new Joke("Cosa dice un paracadute?", "Non so se mi spiego."));
        jokes.add(new Joke("Qual è il colmo per un dentista?", "Mangiare la pasta al dente."));
        jokes.add(new Joke("Cosa fa un cane in spiaggia?", "Il canotto."));
    }
}
