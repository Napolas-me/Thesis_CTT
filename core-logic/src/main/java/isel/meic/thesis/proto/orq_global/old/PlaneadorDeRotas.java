package isel.meic.thesis.proto.orq_global.old;

public class PlaneadorDeRotas {
    /*private final Connection dbConnection;
    private List<Ligacao> ligacoes;

    public PlaneadorDeRotas(Connection dbConnection) {
        this.dbConnection = dbConnection;
        this.ligacoes = new LinkedList<>();

        gerarLigacoes();
    }
    public int getLigacaoSize(){
        return ligacoes.size();
    }
    public List<Ligacao> getLigacoes(){
        return ligacoes;
    }

    public void gerarLigacoes(){
        FabricaLigacao fabricaLigacao = new FabricaLigacao();

        StopDAO stopDAO = new StopDAO(dbConnection);
        TripDAO tripDAO = new TripDAO(dbConnection);
        TransporteDAO transporteDAO = new TransporteDAO(dbConnection);
        try{
            ligacoes = fabricaLigacao.gerarLigacoes(stopDAO.getAllStop(),
                    tripDAO.getAllViagens(),
                    transporteDAO.getAllTransportes());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar ligações", e);
        }
        System.out.println("Total de ligações geradas: " + ligacoes.size());

        //ligacoes.sort(Comparator.comparing(l -> l.getViagem().getOrigem()));
        //print ligacoes explicito
        ligacoes.forEach(ligacao -> {
            System.out.printf(
                    "Ligação #%d | Origem: %s (%s) -> Destino(%s): %s (%s) Transporte -> %s %n",
                    ligacao.getID(),
                    ligacao.getViagem().getOrigem(),
                    ligacao.getViagem().getTempoPartida(),
                    ligacao.getParagem().getGate(),
                    ligacao.getParagem().getLocal(),
                    ligacao.getParagem().getTempoChegada(),
                    ligacao.getTransporte().getName()

            );
        });
    }

    public List<Route> gerarRotas(String origem, String destino) {
        System.out.println("Gerando rotas de " + origem + " para " + destino);
        if (ligacoes.isEmpty()) gerarLigacoes();

        List<Route> caminhosViaveis = new LinkedList<>();
        gerarRotasPossiveis(origem, destino, caminhosViaveis, new LinkedList<>(), new HashSet<>());

        //fix transportes, in a rota the transportes should be the same
        /*caminhosViaveis.forEach(rota -> {
            if (!rota.getLigacoes().isEmpty()) {
                Transporte transporte = rota.getLigacoes().get(0).getTransporte();
                rota.getLigacoes().forEach(ligacao -> ligacao.setTransporte(transporte));
            }
        });
        return caminhosViaveis;
    }

    /**
     * TODO: Rework quando existir nova camada de orquestração.
     * Passar de um numero estático para uma lógica mais dinâmica
     * @return
     */
    private long getTempoProcessamento() {
        // Implementar lógica para obter o tempo de processamento
        // Layer de baixo, orquestrador Local
        return 10L * 60_000; // Exemplo fixo
    }

    /*
     * TODO: Repensar uso de função
     * @param rota
     * @return
     */
    /*private long getOrUpdateTempoProcessamentoEstimado(Rota rota) {
        if(estimativas.containsKey(rota)) {
            long e = estimativas.get(rota);
            long tempoProcessamento = getTempoProcessamento();

            if (tempoProcessamento > e) {
                estimativas.put(rota, tempoProcessamento);
                return tempoProcessamento;
            }
            return e;

        } else {
            // Se não existe, adicionar com um valor padrão
            long tempoProcessamento = getTempoProcessamento();
            estimativas.put(rota, tempoProcessamento); // Exemplo fixo
            return tempoProcessamento;
        }
    }

    private void gerarRotasPossiveis(String origem, String destino,
                                     List<Route> rotasResultantes,
                                     List<Ligacao> caminhoAtual,
                                     Set<Ligacao> visitadas) {

        for (Ligacao ligacaoAtual : ligacoes) {
            if (!ligacaoAtual.getViagem().getOrigem().equals(origem)) continue;
            if (visitadas.contains(ligacaoAtual)) continue;

            boolean podeTransbordar = podeTransbordar(caminhoAtual, ligacaoAtual);
            if (!podeTransbordar) continue;

            List<Ligacao> novoCaminho = new LinkedList<>(caminhoAtual);
            novoCaminho.add(ligacaoAtual);

            String destinoAtual = ligacaoAtual.getParagem().getLocal();
            if (destinoAtual.equals(destino)) {
                //get transporte da ligação 0
                Transporte t = novoCaminho.get(0).getTransporte();
                //System.out.println("Transporte usado: " + t.getName());

                // criar novo caminho com transporte fixo
                List<Ligacao> caminhoComMesmoTransporte = novoCaminho.stream()
                        .map(l -> l.copiaComTransporte(t))
                        .toList();

                rotasResultantes.add(new Route(caminhoComMesmoTransporte));
            } else {
                Set<Ligacao> novaVisitadas = new HashSet<>(visitadas);
                novaVisitadas.add(ligacaoAtual);
                gerarRotasPossiveis(destinoAtual, destino, rotasResultantes, novoCaminho, novaVisitadas);
            }
        }
    }

    private boolean podeTransbordar(List<Ligacao> caminhoAtual, Ligacao ligacaoAtual) {
        boolean podeTransbordar = true;

        if (!caminhoAtual.isEmpty()) {
            Ligacao anterior = caminhoAtual.get(caminhoAtual.size() - 1);

            Date chegadaAnterior = anterior.getViagem().getTempoChegada();
            Date partidaAtual = ligacaoAtual.getViagem().getTempoPartida();

            long tempoChegadaMs = chegadaAnterior.getTime();
            long tempoPartidaMs = partidaAtual.getTime();
            long tempoProcessamento = getTempoProcessamento();

            podeTransbordar = ((tempoChegadaMs + tempoProcessamento) <= tempoPartidaMs) /*&&
                                /*ligacaoAtual.getTransporte().canAdd(1);

        }
        return podeTransbordar;
    }*/
}
