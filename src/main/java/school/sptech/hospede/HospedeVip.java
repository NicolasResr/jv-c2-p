
package school.sptech.hospede;

import school.sptech.exceptions.DadoInvalidoException;

public class HospedeVip extends Hospede {

    private Double percentualDesconto;

    public HospedeVip(String nome, String cpf, Double percentualDesconto) {
        super(nome, cpf);

        if (percentualDesconto == null
                || percentualDesconto < 0
                || percentualDesconto > 50) {
            throw new DadoInvalidoException("Percentual inválido");
        }

        this.percentualDesconto = percentualDesconto;
        this.limiteReservasAtivas = 10;
    }

    @Override
    public Double calcularTotalGasto() {
        double totalGasto = 0.0;

        for (Reserva reserva : reservas) {
            totalGasto += reserva.calcularValor();
        }

        totalGasto -= totalGasto * (percentualDesconto / 100);

        return totalGasto;
    }

    public Double getPercentualDesconto() {
        return percentualDesconto;
    }
}
