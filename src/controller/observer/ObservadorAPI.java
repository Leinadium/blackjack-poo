/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */

package controller.observer;

/**
 * Interface para a classe que está observando do padrão Observer.
 */
public interface ObservadorAPI {
    void notificar(ObservadoAPI o);
}
