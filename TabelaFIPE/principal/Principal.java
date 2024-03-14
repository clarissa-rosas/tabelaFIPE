package br.com.alura.TabelaFIPE.principal;

import br.com.alura.TabelaFIPE.model.Dados;
import br.com.alura.TabelaFIPE.model.Modelos;
import br.com.alura.TabelaFIPE.model.Veiculos;
import br.com.alura.TabelaFIPE.service.ConsumoApi;
import br.com.alura.TabelaFIPE.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    public void exibeMenu() {

        var menu = """
                ***** OPÇÕES DE VEÍCULO *****
                                
                1 - Carro
                2 - Moto
                3 - Caminhão
                4 - Sair
                                
                Digite o número da opção de veículo que deseja consultar.
                """;
        System.out.println(menu);

        //Selecionar categoria de veículo

        var opcaoVeiculo = leitura.nextLine();

        switch (opcaoVeiculo) {
            case "1":
                opcaoVeiculo = "carros/marcas";
                System.out.println("Consulta de carros:");
                break;
            case "2":
                opcaoVeiculo = "motos";
                System.out.println("Consulta de motos:");
                break;
            case "3":
                opcaoVeiculo = "caminhoes";
                System.out.println("Consulta de caminhões:");
                break;
            case "4":
                System.out.println("Consulta encerrada pelo usuário.");
                break;
            default:
                System.out.println("Opção inválida. Escolha uma opção de 1 a 4.");
                break;
        }

        //Exibir marcas da categoria escolhida

        String enderecoMarcas = URL_BASE + opcaoVeiculo + "/marcas/";
        var json = consumo.obterDados(enderecoMarcas);
        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        //Selecionar marca do veículo

        System.out.println("Digite o código da marca para consultar:");
        var opcaoMarca = leitura.nextLine();

        //Exibir modelos da categoria escolhida

        String enderecoModelos = enderecoMarcas + opcaoMarca + "/modelos/";
        json = consumo.obterDados(enderecoModelos);
        var listaModelos = conversor.obterDados(json, Modelos.class);
        System.out.println("Modelos dessa marca:");
        listaModelos.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        //Selecionar modelo do veículo

        System.out.println("Digite um trecho do nome do modelo para consultar:");
        var nomeModelo = leitura.nextLine();
        List<Dados> listaAnos = listaModelos.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeModelo.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("Digite o código do modelo para consultar:");
        var opcaoModelo = leitura.nextLine();
        String enderecoAnos = enderecoModelos + opcaoModelo + "/anos/";

        //Exibir anos do modelo escolhido

        System.out.println("\nO modelo escolhido tem dados dos seguintes anos para consulta:");
        listaAnos.forEach(System.out::println);

        //Criar lista com os dados da tabela FIPE para todos os anos do modelo escolhido

        List<Veiculos> listaDadosTabelaFipe = new ArrayList<>();
        for (int i = 0; i < listaAnos.size(); i++) {
            var enderecoTodosOsAnos = enderecoAnos + listaAnos.get(i).codigo();
            json = consumo.obterDados(enderecoTodosOsAnos);
            Veiculos veiculo = conversor.obterDados(json, Veiculos.class);
            listaDadosTabelaFipe.add(veiculo);
        }

        //Selecionar opção e exibir dados da tabela FIPE

        System.out.println("""
                Deseja consultar os dados da tabela FIPE para todos os anos?
                1 - Sim, todos os anos do modelo selecionado
                2 - Não, escolher ano do modelo selecionado
                3 - Sair
                """);

        var opcaoAno = leitura.nextLine();
        switch (opcaoAno) {
            case "1":
                listaDadosTabelaFipe.forEach(System.out::println);
                break;
            case "2":
                System.out.println("\nDigite o ano que deseja consultar:");
                opcaoAno = leitura.nextLine();
                String enderecoAnoEspecifico = enderecoAnos + opcaoAno;
                json = consumo.obterDados(enderecoAnoEspecifico);
                var dadosTabelaFipe = conversor.obterDados(json, Veiculos.class);
                System.out.println(dadosTabelaFipe);
                break;
            case "3":
                System.out.println("\nConsulta encerrada pelo usuário.");
                break;
            default:
                System.out.println("\nOpção inválida. Escolha uma opção de 1 a 3.");
                break;
        }
    }
}