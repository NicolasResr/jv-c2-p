
package school.sptech;

import school.sptech.exceptions.DadoInvalidoException;
import school.sptech.exceptions.HospedeNaoEncontradoException;
import school.sptech.exceptions.QuartoIndisponivelException;
import school.sptech.hospede.Hospede;
import school.sptech.hospede.HospedeVip;
import school.sptech.hospede.Reserva;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private String nome;
    private List<Hospede> hospedes = new ArrayList<>();

    public Hotel(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DadoInvalidoException("Nome inválido");
        }

        this.nome = nome;
    }

    public Hospede buscarPorCpf(String cpf) {
        for (Hospede hospede : hospedes) {
            if (hospede.getCpf().equals(cpf)) {
                return hospede;
            }
        }

        throw new HospedeNaoEncontradoException("Hospede não encontrado");
    }

    public Double calcularFaturamentoTotal() {
        double fatTotal = 0.0;

        for (Hospede hospede : hospedes) {
            fatTotal += hospede.calcularTotalGasto();
        }

        return fatTotal;
    }

    public Hospede buscarHospedeQueMaisGastou() {
        if (hospedes.isEmpty()) {
            return null;
        }

        Hospede maiorGasto = hospedes.get(0);

        for (Hospede hospede : hospedes) {
            if (hospede.calcularTotalGasto() >
                    maiorGasto.calcularTotalGasto()) {
                maiorGasto = hospede;
            }
        }

        return maiorGasto;
    }

    public List<HospedeVip> buscarHospedesVip() {
        List<HospedeVip> resultado = new ArrayList<>();

        for (Hospede hospede : hospedes) {
            if (hospede instanceof HospedeVip) {
                resultado.add((HospedeVip) hospede);
            }
        }

        return resultado;
    }

    public void cadastrarHospede(Hospede hospede) {
        for (Hospede cadastrado : hospedes) {
            if (cadastrado.getCpf().equals(hospede.getCpf())) {
                return;
            }
        }

        hospedes.add(hospede);
    }

    public List<Hospede> buscarPorTipo(String tipo) {
        List<Hospede> resultado = new ArrayList<>();

        if (tipo == null) {
            return resultado;
        }

        for (Hospede hospede : hospedes) {
            if (tipo.equalsIgnoreCase("vip")
                    && hospede instanceof HospedeVip) {
                resultado.add(hospede);
            } else if (tipo.equalsIgnoreCase("classico")
                    && !(hospede instanceof HospedeVip)) {
                resultado.add(hospede);
            }
        }

        return resultado;
    }

    public void verificarDisponibilidade(Integer numeroQuarto)
            throws QuartoIndisponivelException {

        if (numeroQuarto == null || numeroQuarto <= 0) {
            throw new DadoInvalidoException("Numero quarto inválido");
        }

        for (Hospede hospede : hospedes) {
            for (Reserva reserva : hospede.getReservas()) {
                if (reserva.getNumeroQuarto().equals(numeroQuarto)
                        && reserva.getAtiva()) {
                    throw new QuartoIndisponivelException(
                            "Quarto indisponível"
                    );
                }
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public List<Hospede> getHospedes() {
        return hospedes;
    }
}
