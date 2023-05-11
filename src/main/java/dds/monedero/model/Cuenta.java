package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

 /* public Cuenta(double montoInicial) {			Se crea una cuenta, y luego se le ingresa saldo.
    saldo = montoInicial;
  }*/
/*
  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }
*/
  public void poner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream().filter(movimiento -> movimiento.getMonto()>0).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    Deposito unDeposito = new Deposito(LocalDate.now(), cuanto);
    movimientos.add(unDeposito);
    
  }

  public void sacar(double cuanto) {
	    if (cuanto <= 0) {
	        throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
	      }
	      if (getSaldo() - cuanto < 0) {
	        throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
	      }
	      double limite = 1000 - this.getMontoExtraidoA(LocalDate.now());
	      if (cuanto > limite) {
	        throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
	            + " diarios, límite: " + limite);
	      }
	
    Extraccion unaExtraccion = new Extraccion(LocalDate.now(), cuanto);
    this.movimientos.add(unaExtraccion);
  }


  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }
  
double calcularExtracciones() {
	return this.movimientos.stream().filter(movimiento->!movimiento.esDeposito()).mapToDouble(movimiento->movimiento.getMonto()).sum();
}

double calcularDepositos() {
	return this.movimientos.stream().filter(movimiento->movimiento.esDeposito()).mapToDouble(movimiento->movimiento.getMonto()).sum();
}

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
this.saldo = this.calcularDepositos() - this.calcularExtracciones();
	  return saldo;
  }
/*
  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }
*/
}
