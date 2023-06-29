package com.diego.posinfnet;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



public class LojaVirtual {
    public static void main(String[] args) {
        // 1. Crie alguns produtos, clientes e pagamentos
        Produto produto1 = new Produto("Música 1", Path.of("musica1.mp3"), BigDecimal.valueOf(2.99));
        Produto produto2 = new Produto("Vídeo 1", Path.of("video1.mp4"), BigDecimal.valueOf(4.99));
        Produto produto3 = new Produto("Imagem 1", Path.of("imagem1.jpg"), BigDecimal.valueOf(1.99));

        Cliente cliente1 = new Cliente("João");
        Cliente cliente2 = new Cliente("Maria");

        Pagamento pagamento1 = new Pagamento(Arrays.asList(produto1, produto2), LocalDate.now(), cliente1);
        Pagamento pagamento2 = new Pagamento(Arrays.asList(produto3), LocalDate.now().minusDays(1), cliente2);
        Pagamento pagamento3 = new Pagamento(Arrays.asList(produto1), LocalDate.now().minusMonths(1), cliente1);

        List<Pagamento> pagamentos = Arrays.asList(pagamento1, pagamento2, pagamento3);

        // 2. Ordene e imprima os pagamentos pela data de compra
        List<Pagamento> pagamentosOrdenados = pagamentos.stream()
        	    .sorted(Comparator.comparing(Pagamento::getDataCompra))
        	    .collect(Collectors.toList());

        System.out.println("Pagamentos ordenados:");
        for (Pagamento pagamento : pagamentosOrdenados) {
            System.out.println(pagamento.getDataCompra());
        }

        // 3. Calcule e imprima a soma dos valores de um pagamento com Optional e recebendo um Double diretamente
        Pagamento primeiroPagamento = pagamentos.get(0);
        BigDecimal somaValores = primeiroPagamento.getProdutos().stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Optional<BigDecimal> somaValoresOptional = primeiroPagamento.getProdutos().stream()
        	    .map(Produto::getPreco)
        	    .reduce(BigDecimal::add);

        double somaValoresDouble = primeiroPagamento.getProdutos().stream()
        	    .mapToDouble(produto -> produto.getPreco().doubleValue())
        	    .sum();

        System.out.println("Soma dos valores do primeiro pagamento: " + somaValores);
        System.out.println("Soma dos valores do primeiro pagamento (Optional): " + somaValoresOptional.orElse(BigDecimal.ZERO));
        System.out.println("Soma dos valores do primeiro pagamento (double): " + somaValoresDouble);

        // 4. Calcule o valor de todos os pagamentos da lista de pagamentos
        BigDecimal valorTotalPagamentos = pagamentos.stream()
                .map(Pagamento::calcularValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Valor total dos pagamentos: " + valorTotalPagamentos);

        // 5. Imprima a quantidade de cada produto vendido
        Map<String, Long> quantidadeProdutosVendidos = pagamentos.stream()
                .flatMap(pagamento -> pagamento.getProdutos().stream())
                .collect(Collectors.groupingBy(Produto::getNome, Collectors.counting()));

        System.out.println("Quantidade de cada produto vendido:");
        for (Map.Entry<String, Long> entry : quantidadeProdutosVendidos.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 6. Crie um mapa de <Cliente, List<Produto>>
        Map<Cliente, List<Produto>> mapaClienteProdutos = pagamentos.stream()
                .collect(Collectors.groupingBy(Pagamento::getCliente, Collectors.flatMapping(p -> p.getProdutos().stream(), Collectors.toList())));

        System.out.println("Mapa de cliente -> produtos:");
        for (Map.Entry<Cliente, List<Produto>> entry : mapaClienteProdutos.entrySet()) {
            System.out.println("Cliente: " + entry.getKey().getNome());
            System.out.println("Produtos: " + entry.getValue().stream().map(Produto::getNome).collect(Collectors.joining(", ")));
            System.out.println();
        }

        // 7. Qual cliente gastou mais?
        Map<Cliente, BigDecimal> totalGastoPorCliente = pagamentos.stream()
                .collect(Collectors.groupingBy(Pagamento::getCliente, Collectors.mapping(Pagamento::calcularValorTotal, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        Cliente clienteMaisGastou = totalGastoPorCliente.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        System.out.println("Cliente que gastou mais: " + clienteMaisGastou.getNome());

        // 8. Quanto foi faturado em um determinado mês?
        BigDecimal faturamentoMes = pagamentos.stream()
                .filter(pagamento -> pagamento.getDataCompra().getMonth() == Month.JUNE)  // Mês específico
                .map(Pagamento::calcularValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Faturamento do mês: " + faturamentoMes);

        // 9. Crie 3 assinaturas
        Assinatura assinatura1 = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.now().minusMonths(1), cliente1);
        Assinatura assinatura2 = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1), cliente2);
        Assinatura assinatura3 = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.now().minusMonths(3), LocalDate.now().minusMonths(2), cliente1);

        // 10. Imprima o tempo em meses de uma assinatura ainda ativa
        Assinatura assinaturaAtiva = assinatura1;
        System.out.println("Tempo em meses da assinatura ativa: " + assinaturaAtiva.getTempoEmMeses());

        // 11. Imprima o tempo de meses entre o start e end de todas as assinaturas
        List<Assinatura> assinaturas = Arrays.asList(assinatura1, assinatura2, assinatura3);
        for (Assinatura assinatura : assinaturas) {
            System.out.println("Tempo em meses da assinatura: " + assinatura.getTempoEmMeses());
        }

        // 12. Calcule o valor pago em cada assinatura até o momento
        for (Assinatura assinatura : assinaturas) {
            System.out.println("Valor pago na assinatura até o momento: " + assinatura.getValorPago());
        }
    }
}
