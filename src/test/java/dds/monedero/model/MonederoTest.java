package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  public void Poner() {
	  cuenta.poner(1500);
	  assertEquals(cuenta.getSaldo(),1500,0);
  }

@Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
	  assertEquals(cuenta.getMovimientos().size(),3,0);
	  assertEquals(cuenta.getSaldo(),3856);
  }

  @Test
 public void noPuedenRealizarseMasDeTresDepositosDiarios() {

          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          Assertions.assertThrows(MaximaCantidadDepositosException.class, () -> cuenta.poner(245));
  }

  @Test
  void ExtraerMasQueElSaldo() {
      cuenta.poner(90);
    Assertions.assertThrows(SaldoMenorException.class, () -> cuenta.sacar(1001));
  }

  @Test
  public void ExtraerMasDe1000() {
      cuenta.poner(5000);
    Assertions.assertThrows(MaximoExtraccionDiarioException.class, () -> cuenta.sacar(1001));
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

}