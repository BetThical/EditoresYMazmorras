package usc.etse.editoresymazmorras.model;

// Vocabulario cerrado de casillas del editor; se persiste como texto (varchar).
// COFRE es el único tipo con fila extra en la tabla cofre (su botín).
public enum TipoElemento {
    SUELO,
    MURO,
    PUERTA,
    COFRE,
    TRAMPA,
    ENTRADA,
    SALIDA
}
