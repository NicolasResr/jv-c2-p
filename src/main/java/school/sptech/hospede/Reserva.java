
package school.sptech.hospede;

import school.sptech.exceptions.DadoInvalidoException;

public class Reserva {

    private Integer numeroQuarto;
    private Integer qtdDiarias;
    private Double valorDiaria;
    private Boolean ativa;

    public Reserva(Integer numeroQuarto, Integer qtdDiarias, Double valorDiaria) {

        if (numeroQuarto == null || numeroQuarto <= 0) {
            throw new DadoInvalidoException("Numero quarto inválido");
        }

        if (qtdDiarias == null || qtdDiarias <= 0) {
            throw new DadoInvalidoException("Numero Diaria inválido");
        }

        if (valorDiaria == null || valorDiaria <= 0) {
            throw new DadoInvalidoException("Valor diaria inválido");
        }

        this.numeroQuarto = numeroQuarto;
        this.qtdDiarias = qtdDiarias;
        this.valorDiaria = valorDiaria;
        this.ativa = true;
    }

    public Double calcularValor() {
        Double valorTotal = valorDiaria * qtdDiarias;

        if (qtdDiarias > 5) {
            valorTotal -= valorTotal * 0.20;
        }

        if (qtdDiarias <= 10) {
            valorTotal += 40.00;
        }

        return valorTotal;
    }

    public void finalizar() {
        ativa = false;
    }

    public Integer getNumeroQuarto() {
        return numeroQuarto;
    }

    public Integer getQtdDiarias() {
        return qtdDiarias;
    }

    public Double getValorDiaria() {
        return valorDiaria;
    }

    public Boolean getAtiva() {
        return ativa;
    }
}
