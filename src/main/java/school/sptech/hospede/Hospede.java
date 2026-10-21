
package school.sptech.hospede;

import school.sptech.exceptions.DadoInvalidoException;
import school.sptech.exceptions.LimiteReservasExcedidoException;

import java.util.ArrayList;
import java.util.List;

public class Hospede {

    protected String nome;
    protected String cpf;
    protected Integer limiteReservasAtivas;
    protected List<Reserva> reservas = new ArrayList<>();

    public Hospede(String nome, String cpf) {

        if (nome == null || nome.isBlank()) {
            throw new DadoInvalidoException("Nome inválido");
        }

        if (cpf == null || cpf.isBlank()) {
            throw new DadoInvalidoException("CPF inválido");
        }

        this.nome = nome;
        this.cpf = cpf;
        this.limiteReservasAtivas = 3;
        this.reservas = new ArrayList<>();
    }

    public Integer contarReservasAtivas() {
        Integer contafor = 0;

        for (Reserva reserva : reservas) {
            if (reserva.getAtiva()) {
                contafor++;
            }
        }

        return contafor;
    }

    public void adicionarReserva(Integer numeroQuarto, Integer qtdDiarias,
                                 Double valorDiaria) {

        if (contarReservasAtivas() >= limiteReservasAtivas) {
            throw new LimiteReservasExcedidoException(
                    "Limite de reservas excedido"
            );
        }

        Reserva reserva = new Reserva(numeroQuarto, qtdDiarias, valorDiaria);
        reservas.add(reserva);
    }

    public Double calcularTotalGasto() {
        double totalGasto = 0.0;

        for (Reserva reserva : reservas) {
            totalGasto += reserva.calcularValor();
        }

        return totalGasto;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Integer getLimiteReservasAtivas() {
        return limiteReservasAtivas;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setLimiteReservasAtivas(Integer limiteReservasAtivas) {
        this.limiteReservasAtivas = limiteReservasAtivas;
    }
}
