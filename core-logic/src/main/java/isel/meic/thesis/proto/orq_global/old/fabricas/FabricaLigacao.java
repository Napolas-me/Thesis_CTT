package isel.meic.thesis.proto.orq_global.old.fabricas;

/**
 * TODO: implementar obtenção de dados da base de dados MariaDB
 * Esta classe será responsavel por inicializar as ligações e
 * fornecer ligações que poderam ser atualizadas com o decorrer
 * dos varios processos.
 */
public class FabricaLigacao {

    /*public List<Ligacao> gerarLigacoes(List<Stop> paragens, List<Viagem> viagens, List<Transporte> transportes) {
        List<Ligacao> ligacoes = new ArrayList<>();
        int ligacaoId = 1;

        // check sizes of input lists
        System.out.println("Paragens: " + paragens.size());
        System.out.println("Viagens: " + viagens.size());
        System.out.println("Transportes: " + transportes.size());

        for(Viagem viagem: viagens){
            for(Stop stop : paragens){
                //check if viagem's destino matches paragem's local
                if (viagem.getDestino().equals(stop.getLocal())) {
                    // Check if the viagem's arrival time fits into the paragem's time window
                    if (viagem.getTempoChegada().compareTo(stop.getTempoPartida()) > 0)
                        continue; // Skip if arrival time is before paragem's time window
                    //get random trasnporte todo: get a better transporte selection strategy
                    Random random = new Random();
                    int randomIndex = random.nextInt(transportes.size());
                    Transporte transporte = transportes.isEmpty() ? null : transportes.get(randomIndex);
                    Ligacao novaLigacao = new Ligacao(ligacaoId++, viagem, stop, transporte);
                    ligacoes.add(novaLigacao);
                }
            }
        }

        return ligacoes;
    }*/
}
